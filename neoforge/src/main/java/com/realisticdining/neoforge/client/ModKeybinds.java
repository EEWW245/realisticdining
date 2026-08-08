package com.realisticdining.neoforge.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.realisticdining.RealisticDining;
import com.realisticdining.client.WokModeConfig;
import com.realisticdining.neoforge.client.arm.FpArmRenderSystem;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = RealisticDining.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
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
