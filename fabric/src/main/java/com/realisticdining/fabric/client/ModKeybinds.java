package com.realisticdining.fabric.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.realisticdining.client.WokModeConfig;
import com.realisticdining.fabric.client.arm.FpArmRenderSystem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {

    private static KeyMapping triggerEatRiceKey;
    private static KeyMapping toggleArmRenderKey;
    private static KeyMapping enableSimplifiedModeKey;
    private static KeyMapping restoreNormalModeKey;
    private static KeyMapping triggerDrinkKey;

    public static void register() {
        triggerEatRiceKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.realisticdining.trigger_eat_rice",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_T,
                "key.categories.realisticdining"
        ));

        toggleArmRenderKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.realisticdining.toggle_arm_render",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                "key.categories.realisticdining"
        ));

        enableSimplifiedModeKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.realisticdining.enable_simplified_mode",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                "key.categories.realisticdining"
        ));

        restoreNormalModeKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.realisticdining.restore_normal_mode",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "key.categories.realisticdining"
        ));

        triggerDrinkKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.realisticdining.trigger_drink",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_U,
                "key.categories.realisticdining"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (triggerEatRiceKey.consumeClick()) {
                FpArmRenderSystem.triggerEatRiceAnimation();
            }
            while (toggleArmRenderKey.consumeClick()) {
                FpArmRenderSystem.toggleArmRender();
            }
            while (enableSimplifiedModeKey.consumeClick()) {
                WokModeConfig.enableSimplifiedMode();
            }
            while (restoreNormalModeKey.consumeClick()) {
                WokModeConfig.disableSimplifiedMode();
            }
            while (triggerDrinkKey.consumeClick()) {
                FpArmRenderSystem.triggerDrinkForMainHand();
            }
        });
    }
}
