package com.example.realisticdining.forge.loot;

import com.example.realisticdining.RealisticDining;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Forge 1.20.1 全局战利品修改器（GLM）注册。
 *
 * <p>通过 DeferredRegister&lt;Codec&lt;? extends IGlobalLootModifier&gt;&gt; 注册
 * {@link AddSnackLootModifier#CODEC}，配合
 * {@code data/forge/loot_modifiers/global_loot_modifiers.json} 与
 * {@code data/realisticdining/loot_modifiers/add_snack.json} 生效。
 */
public class ForgeLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLM =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, RealisticDining.MOD_ID);

    public static final RegistryObject<Codec<AddSnackLootModifier>> ADD_SNACK =
            GLM.register("add_snack", () -> AddSnackLootModifier.CODEC);

    public static void register(net.minecraftforge.eventbus.api.IEventBus modEventBus) {
        GLM.register(modEventBus);
    }
}
