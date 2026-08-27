package com.example.realisticdining.forge.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.client.WokModeConfig;
import com.example.realisticdining.forge.client.arm.FpArmRenderSystem;
import com.example.realisticdining.forge.client.pack.PackKeyRouter;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = RealisticDining.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
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
            // 饮用键=右键 且 准星对准方块时，交给右键事件放置展示台，跳过饮用触发，避免重复
            if (isDrinkKeyBoundToRightMouse() && isPointingAtBlock()) {
                return;
            }
            // U 键路由：优先材质包扩展物品（单动画 + finished 指令），未命中再走原饮料/零食状态机
            if (!PackKeyRouter.tryRoutePackAnimation()) {
                FpArmRenderSystem.triggerDrinkForMainHand();
            }
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

    /** 当前准星是否指向一个方块（而非空气/实体），用于区分「放置展示台」与「触发饮用」。 */
    public static boolean isPointingAtBlock() {
        Minecraft mc = Minecraft.getInstance();
        return mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.BLOCK;
    }
}
