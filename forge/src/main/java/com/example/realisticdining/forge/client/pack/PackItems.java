package com.example.realisticdining.forge.client.pack;

import com.example.realisticdining.RealisticDining;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 材质包扩展物品的注册（Forge 1.20.1）。
 *
 * <p>用 Forge {@link DeferredRegister} 注册 {@code pack_empty} 物品——所有材质包扩展物品
 * 共用这一个 GeoItem，不进创造栏，玩家永远拿不到。渲染时通过 {@link PackCustomRenderer}
 * 替换为各扩展物品的模型/材质/动画（路径由定义文件名派生）。
 */
public final class PackItems {

    public static final DeferredRegister<Item> PACK_ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RealisticDining.MOD_ID);

    public static final RegistryObject<PackEmpty> PACK_EMPTY =
            PACK_ITEMS.register("pack_empty", () -> new PackEmpty(new Item.Properties()));

    /** 客户端启动后由 ForgeRealisticDining 注入的实例引用（便于代码内直接调用）。 */
    public static PackEmpty EMPTY_ITEM = null;

    private PackItems() {
    }

    public static void register(IEventBus modEventBus) {
        PACK_ITEMS.register(modEventBus);
    }

    /** 客户端 setup 阶段调用：把 RegistryObject 解析为实例，绑定到 EMPTY_ITEM 字段。 */
    public static void bindInstance() {
        if (PACK_EMPTY.isPresent()) {
            EMPTY_ITEM = PACK_EMPTY.get();
        }
    }
}
