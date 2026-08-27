package com.realisticdining.fabric.client.pack;

import com.realisticdining.RealisticDining;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/**
 * 材质包扩展物品的注册（Fabric 1.21.1）。
 *
 * <p>用 Fabric 的 {@link Registry#register} 注册 {@code pack_empty} 物品——所有材质包扩展物品
 * 共用这一个 GeoItem，不进创造栏，玩家永远拿不到。渲染时通过 {@link PackCustomRenderer}
 * 替换为各扩展物品的模型/材质/动画（路径由定义文件名派生）。
 */
public final class PackItems {

    public static final String PACK_EMPTY_ID = "pack_empty";

    /** 注册后注入的实例引用（便于代码内直接调用）。 */
    public static PackEmpty EMPTY_ITEM = null;

    private PackItems() {
    }

    /**
     * 注册共用 pack_empty GeoItem。必须在 onInitialize 阶段（注册表冻结前）调用。
     * <p>物品仅客户端渲染用、不进创造栏；幂等保护，避免重复注册。
     */
    public static void register() {
        if (EMPTY_ITEM != null) {
            return;
        }
        ResourceLocation rl = ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, PACK_EMPTY_ID);
        // 1.21.1 不强制要求 setId(ResourceKey)；与 vanilla Items.register 模式一致
        PackEmpty item = new PackEmpty(new Item.Properties().stacksTo(1));
        Registry.register(BuiltInRegistries.ITEM, rl, item);
        EMPTY_ITEM = item;
    }

    /** 客户端 setup 阶段调用：把注册的实例绑定到 EMPTY_ITEM 字段。 */
    public static void bindInstance() {
        // 注册时已绑定，保留方法以兼容 Forge 版的入口签名
        if (EMPTY_ITEM == null) {
            register();
        }
    }
}
