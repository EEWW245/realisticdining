package com.realisticdining.neoforge.client.pack;

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
 * 自定义半透明 + 不剔除背面 RenderType 工厂（NeoForge 1.21.1）。
 *
 * <p>背景与实现说明见同包 fabric 端同名类的注释。
 *
 * <p>方案：反射访问 {@link RenderType} 的 private static state shard 字段
 * （{@code TRANSLUCENT_TRANSPARENCY} / {@code LIGHTMAP} /
 * {@code RENDERTYPE_ENTITY_TRANSLUCENT} / {@code COLOR_DEPTH_WRITE}），
 * 把 cull state 替换为 public 的 {@code NO_CULL}，
 * 用 {@link RenderType.CompositeState#builder()} 组装新 CompositeState，
 * 最后用 {@link RenderType#create} 包装。
 *
 * <p>失败时降级到 {@link RenderType#entityTranslucent(ResourceLocation)}。
 */
public final class PackRenderTypes {

    private PackRenderTypes() {
    }

    public static RenderType translucentNoCull(ResourceLocation texture) {
        try {
            return buildTranslucentNoCull(texture);
        } catch (Throwable t) {
            RealisticDining.LOGGER.warn("[PackRenderTypes] 反射构建 translucentNoCull 失败，降级到 entityTranslucent: {}",
                    t.toString());
            return RenderType.entityTranslucent(texture);
        }
    }

    private static RenderType buildTranslucentNoCull(ResourceLocation texture) throws Exception {
        Class<?> rtClass = RenderType.class;
        Class<?> compositeStateClass = nested(rtClass, "CompositeState");
        Class<?> renderStateShardClass = Class.forName("net.minecraft.client.renderer.RenderStateShard");

        Object translucentTransparency = getStaticField(rtClass, "TRANSLUCENT_TRANSPARENCY");
        Object lightmap = getStaticField(rtClass, "LIGHTMAP");
        Object shader = getStaticField(rtClass, "RENDERTYPE_ENTITY_TRANSLUCENT");
        Object colorDepthWrite = getStaticField(rtClass, "COLOR_DEPTH_WRITE");
        Object noCull = getStaticField(rtClass, "NO_CULL");

        Class<?> textureStateShardClass = nested(renderStateShardClass, "TextureStateShard");
        Constructor<?> textureCtor = textureStateShardClass.getDeclaredConstructor(
                ResourceLocation.class, boolean.class, boolean.class);
        textureCtor.setAccessible(true);
        Object textureState = textureCtor.newInstance(texture, false, false);

        Method builderMethod = compositeStateClass.getDeclaredMethod("builder");
        builderMethod.setAccessible(true);
        Object builder = builderMethod.invoke(null);

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

        Method buildMethod = builder.getClass().getDeclaredMethod("build", boolean.class);
        buildMethod.setAccessible(true);
        Object compositeState = buildMethod.invoke(builder, false);

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

    private static Object getStaticField(Class<?> clazz, String name) throws Exception {
        Field f = clazz.getDeclaredField(name);
        f.setAccessible(true);
        return f.get(null);
    }

    private static void invokeSetter(Object builder, String methodName, Class<?> paramType, Object arg) throws Exception {
        Method m = builder.getClass().getDeclaredMethod(methodName, paramType);
        m.setAccessible(true);
        m.invoke(builder, arg);
    }

    private static Class<?> nested(Class<?> parent, String simpleName) throws ClassNotFoundException {
        String name = parent.getName() + "$" + simpleName;
        return Class.forName(name);
    }
}
