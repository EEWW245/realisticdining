package com.realisticdining.fabric.client.pack;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.realisticdining.RealisticDining;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 自定义半透明 + 不剔除背面 RenderType 工厂（Fabric 1.21.1）。
 *
 * <p>背景：vanilla 没有 public 的 "translucent + no cull" RenderType API。
 * <ul>
 *   <li>{@link RenderType#entityTranslucent(ResourceLocation)} 是否剔除背面不确定，
 *       且若剔除会导致第一人称相机贴脸贴图消失（与之前 entityTranslucentCull 同问题）。</li>
 *   <li>{@link RenderType#entityCutoutNoCull(ResourceLocation)} 不支持半透明像素，
 *       半透明玻璃材质的 alpha 会被二值化。</li>
 * </ul>
 *
 * <p>方案：用反射访问 {@link RenderType} 的 private static state shard 字段
 * （{@code TRANSLUCENT_TRANSPARENCY} / {@code LIGHTMAP} /
 * {@code RENDERTYPE_ENTITY_TRANSLUCENT} / {@code COLOR_DEPTH_WRITE}），
 * 把 cull state 替换为 public 的 {@code NO_CULL}，
 * 用 {@link RenderType.CompositeState#builder()} 组装一个新的 CompositeState，
 * 最后用 {@link RenderType#create} 包装成新的 RenderType。
 *
 * <p>所有反射结果缓存到 static 字段，仅首次调用时反射一次。
 * 失败时降级到 {@link RenderType#entityTranslucent(ResourceLocation)}（至少半透明像素有效）。
 *
 * <p>这个 RenderType 是 NO_CULL + 半透明混合 + COLOR_DEPTH_WRITE，
 * 与 vanilla 1.16.5 的 entityTranslucent 内部组合完全一致
 * （1.21.1 内部实现亦应一致，但反射方案不依赖具体实现细节）。
 */
public final class PackRenderTypes {

    private PackRenderTypes() {
    }

    // 缓存的反射结果（首次调用 translucentNoCull 时初始化）
    private static boolean initialized = false;
    private static RenderType cachedTranslucentNoCull = null;

    /**
     * 取一个 "半透明 + 不剔除背面" 的 RenderType。
     *
     * <p>注意：返回的 RenderType 是按 texture 缓存的实例，每个 texture 只反射一次。
     * 由于 CompositeState 内部包含 texture 字段，所以不同 texture 必须返回不同 RenderType 实例。
     *
     * @param texture 材质 ResourceLocation
     * @return 半透明 + 不剔除背面的 RenderType，反射失败时降级到 entityTranslucent
     */
    public static RenderType translucentNoCull(ResourceLocation texture) {
        try {
            return buildTranslucentNoCull(texture);
        } catch (Throwable t) {
            RealisticDining.LOGGER.warn("[PackRenderTypes] 反射构建 translucentNoCull 失败，降级到 entityTranslucent: {}",
                    t.toString());
            return RenderType.entityTranslucent(texture);
        }
    }

    /**
     * 反射组装 translucent + no cull 的 RenderType。
     * 每次调用都重新反射创建（不同 texture 必须独立 RenderType 实例）。
     */
    private static RenderType buildTranslucentNoCull(ResourceLocation texture) throws Exception {
        Class<?> rtClass = RenderType.class;
        Class<?> compositeStateClass = nested(rtClass, "CompositeState");
        Class<?> renderStateShardClass = Class.forName("net.minecraft.client.renderer.RenderStateShard");

        // 取 RenderType 类的 private static state shard 字段
        Object translucentTransparency = getStaticField(rtClass, "TRANSLUCENT_TRANSPARENCY");
        Object lightmap = getStaticField(rtClass, "LIGHTMAP");
        Object shader = getStaticField(rtClass, "RENDERTYPE_ENTITY_TRANSLUCENT");
        Object colorDepthWrite = getStaticField(rtClass, "COLOR_DEPTH_WRITE");
        Object noCull = getStaticField(rtClass, "NO_CULL");

        // 反射构造 TextureStateShard 实例（protected 构造，外部不可直接 new）
        Class<?> textureStateShardClass = nested(renderStateShardClass, "TextureStateShard");
        Constructor<?> textureCtor = textureStateShardClass.getDeclaredConstructor(
                ResourceLocation.class, boolean.class, boolean.class);
        textureCtor.setAccessible(true);
        Object textureState = textureCtor.newInstance(texture, false, false);

        // 调用 CompositeState.builder() 创建 builder
        Method builderMethod = compositeStateClass.getDeclaredMethod("builder");
        builderMethod.setAccessible(true);
        Object builder = builderMethod.invoke(null);

        // 调用 builder 的各 setXxxState 方法
        invokeSetter(builder, "setLightmapState",
                nested(renderStateShardClass, "LightmapStateShard"), lightmap);
        invokeSetter(builder, "setShaderState",
                nested(renderStateShardClass, "ShaderStateShard"), shader);
        invokeSetter(builder, "setTextureState", textureStateShardClass, textureState);
        invokeSetter(builder, "setTransparencyState",
                nested(renderStateShardClass, "TransparencyStateShard"), translucentTransparency);
        invokeSetter(builder, "setCullState",
                nested(renderStateShardClass, "CullStateShard"), noCull);
        invokeSetter(builder, "setWriteMaskState",
                nested(renderStateShardClass, "WriteMaskStateShard"), colorDepthWrite);

        // 调用 builder.build(false)
        Method buildMethod = builder.getClass().getDeclaredMethod("build", boolean.class);
        buildMethod.setAccessible(true);
        Object compositeState = buildMethod.invoke(builder, false);

        // 调用 RenderType.create(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, compositeState)
        Method createMethod = rtClass.getDeclaredMethod("create",
                String.class, VertexFormat.class, VertexFormat.Mode.class,
                int.class, boolean.class, boolean.class, compositeStateClass);
        createMethod.setAccessible(true);
        return (RenderType) createMethod.invoke(null,
                "pack_translucent_no_cull",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                1536,
                true,
                true,
                compositeState);
    }

    /** 反射读取 static 字段值。 */
    private static Object getStaticField(Class<?> clazz, String name) throws Exception {
        Field f = clazz.getDeclaredField(name);
        f.setAccessible(true);
        return f.get(null);
    }

    /** 反射调用 builder.setXxxState(stateShardClass, stateShardInstance)。 */
    private static void invokeSetter(Object builder, String methodName, Class<?> paramType, Object arg) throws Exception {
        Method m = builder.getClass().getDeclaredMethod(methodName, paramType);
        m.setAccessible(true);
        m.invoke(builder, arg);
    }

    /** 取嵌套类 Class 对象。 */
    private static Class<?> nested(Class<?> parent, String simpleName) throws ClassNotFoundException {
        String name = parent.getName() + "$" + simpleName;
        return Class.forName(name);
    }
}
