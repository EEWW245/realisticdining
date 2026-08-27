package com.realisticdining.neoforge.client.pack;

import com.realisticdining.RealisticDining;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * 材质包扩展物品的注册（NeoForge 1.21.1）。
 *
 * <p>用 NeoForge {@link DeferredRegister} 注册 {@code pack_empty} 物品——所有材质包扩展物品
 * 共用这一个 GeoItem，不进创造栏，玩家永远拿不到。渲染时通过 {@link PackCustomRenderer}
 * 替换为各扩展物品的模型/材质/动画（路径由定义文件名派生）。
 *
 * <p>1.21.1 NeoForge DeferredRegister.register 会自动调用 setId(ResourceKey) 注入
 * ResourceKey，无需手动设置（与 fabric 手动 Registry.register 不同）。
 */
public final class PackItems {

    public static final DeferredRegister<Item> PACK_ITEMS =
            DeferredRegister.create(Registries.ITEM, RealisticDining.MOD_ID);

    public static final Supplier<Item> PACK_EMPTY =
            PACK_ITEMS.register("pack_empty", () -> new PackEmpty(new Item.Properties().stacksTo(1)));

    /** 客户端启动后由 RealisticDiningNeoForge 注入的实例引用（便于代码内直接调用）。 */
    public static PackEmpty EMPTY_ITEM = null;

    private PackItems() {
    }

    public static void register(IEventBus modEventBus) {
        PACK_ITEMS.register(modEventBus);
    }

    /** 客户端 setup 阶段调用：把 Supplier 解析为实例，绑定到 EMPTY_ITEM 字段。 */
    public static void bindInstance() {
        EMPTY_ITEM = (PackEmpty) PACK_EMPTY.get();
    }
}
