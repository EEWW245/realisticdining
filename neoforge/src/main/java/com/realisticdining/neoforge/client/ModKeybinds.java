package com.realisticdining.neoforge.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.realisticdining.RealisticDining;
import com.realisticdining.client.WokModeConfig;
import com.realisticdining.common.SnackItemRegistry;
import com.realisticdining.neoforge.client.arm.FpArmRenderSystem;
import com.realisticdining.neoforge.client.pack.PackKeyRouter;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
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
            // 饮用键=右键 且 主手是零食/饮料时，交给右键事件（放置展示台/兜底饮用），跳过，避免重复；
            // Pack 扩展物品不受右键事件处理，需正常触发材质包动画
            if (isDrinkKeyBoundToRightMouse() && isMainHandSnack()) {
                return;
            }
            triggerDrinkPressed();
        }
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

    /** 主手物品是否为主模组零食/饮料（这类物品右键会走放置展示台/兜底饮用逻辑）。 */
    public static boolean isMainHandSnack() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;
        ItemStack mainHand = mc.player.getMainHandItem();
        return !mainHand.isEmpty() && SnackItemRegistry.isSnackItem(mainHand.getItem());
    }
}
