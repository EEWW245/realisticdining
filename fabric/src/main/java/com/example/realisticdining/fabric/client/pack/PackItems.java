package com.example.realisticdining.fabric.client.pack;

import com.example.realisticdining.RealisticDining;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/**
 * 材质包扩展物品的注册（Fabric 1.20.1）。
 *
 * <p>用 {@link Registry#register} 注册 {@code pack_empty} 物品——所有材质包扩展物品
 * 共用这一个 GeoItem，不进创造栏，玩家永远拿不到。渲染时通过 {@link PackCustomRenderer}
 * 替换为各扩展物品的模型/材质/动画（路径由定义文件名派生）。
 *
 * <p>Fabric 1.20.1 不使用 Forge DeferredRegister，{@code Registry.register} 是同步调用，
 * 因此 {@link #EMPTY_ITEM} 在 {@link #register()} 内直接赋值即可（注册即可拿到实例）。
 * 这同时保证服务端也能访问 EMPTY_ITEM，从而让 {@link com.example.realisticdining.fabric.network.PackAnimationPacket}
 * 的服务端 handler 能调用 triggerAnim。
 */
public final class PackItems {

    /** 客户端启动后由 {@link #register} 注入的实例引用（便于代码内直接调用）。 */
    public static PackEmpty EMPTY_ITEM = null;

    private PackItems() {
    }

    /** 在 FabricRealisticDining.onInitialize 阶段调用，立即注册并赋值 EMPTY_ITEM。 */
    public static void register() {
        if (EMPTY_ITEM != null) {
            return;
        }
        EMPTY_ITEM = Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(RealisticDining.MOD_ID, "pack_empty"),
                new PackEmpty(new Item.Properties())
        );
    }
}
