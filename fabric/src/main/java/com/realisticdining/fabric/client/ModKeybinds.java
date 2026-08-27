package com.realisticdining.fabric.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.realisticdining.client.WokModeConfig;
import com.realisticdining.fabric.client.arm.FpArmRenderSystem;
import com.realisticdining.fabric.client.pack.PackKeyRouter;
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
                triggerDrinkPressed();
            }
        });
    }

    /**
     * 触发饮用键（默认 U）对应的动作：优先匹配材质包扩展物品，未命中走原饮用动画。
     */
    public static void triggerDrinkPressed() {
        if (!PackKeyRouter.tryRoutePackAnimation()) {
            FpArmRenderSystem.triggerDrinkForMainHand();
        }
    }

    /**
     * 判断「饮用键」（默认 U）当前是否绑定为鼠标右键。
     * <p>供右键放置展示台失败时的兜底逻辑使用：只有玩家主动把饮用键改绑成右键时，
     * 右键兜底才触发饮用动画，避免与 U 键默认绑定冲突。
     */
    public static boolean isDrinkKeyBoundToRightMouse() {
        return triggerDrinkKey != null && triggerDrinkKey.matchesMouse(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
    }
}
