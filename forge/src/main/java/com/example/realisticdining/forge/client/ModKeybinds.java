package com.example.realisticdining.forge.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.client.WokModeConfig;
import com.example.realisticdining.forge.client.arm.FpArmRenderSystem;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = RealisticDining.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModKeybinds {

    private static KeyMapping triggerEatRiceKey;
    private static KeyMapping toggleArmRenderKey;
    private static KeyMapping enableSimplifiedModeKey;
    private static KeyMapping restoreNormalModeKey;
    private static KeyMapping triggerDrinkKey;

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        triggerEatRiceKey = new KeyMapping(
                "key.realisticdining.trigger_eat_rice",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_T,
                "key.categories.realisticdining"
        );
        event.register(triggerEatRiceKey);

        toggleArmRenderKey = new KeyMapping(
                "key.realisticdining.toggle_arm_render",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                "key.categories.realisticdining"
        );
        event.register(toggleArmRenderKey);

        enableSimplifiedModeKey = new KeyMapping(
                "key.realisticdining.enable_simplified_mode",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                "key.categories.realisticdining"
        );
        event.register(enableSimplifiedModeKey);

        restoreNormalModeKey = new KeyMapping(
                "key.realisticdining.restore_normal_mode",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "key.categories.realisticdining"
        );
        event.register(restoreNormalModeKey);

        triggerDrinkKey = new KeyMapping(
                "key.realisticdining.trigger_drink",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_U,
                "key.categories.realisticdining"
        );
        event.register(triggerDrinkKey);
    }

    public static void checkKeybindings() {
        if (triggerEatRiceKey != null && triggerEatRiceKey.consumeClick()) {
            FpArmRenderSystem.triggerEatRiceAnimation();
        }
        if (toggleArmRenderKey != null && toggleArmRenderKey.consumeClick()) {
            FpArmRenderSystem.toggleArmRender();
        }
        if (enableSimplifiedModeKey != null && enableSimplifiedModeKey.consumeClick()) {
            WokModeConfig.enableSimplifiedMode();
        }
        if (restoreNormalModeKey != null && restoreNormalModeKey.consumeClick()) {
            WokModeConfig.disableSimplifiedMode();
        }
        if (triggerDrinkKey != null && triggerDrinkKey.consumeClick()) {
            FpArmRenderSystem.triggerDrinkForMainHand();
        }
    }
}
