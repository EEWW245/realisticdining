package com.realisticdining.fabric.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.realisticdining.client.WokModeConfig;
import com.realisticdining.common.SnackItemRegistry;
import com.realisticdining.fabric.client.arm.FpArmRenderSystem;
import com.realisticdining.fabric.client.pack.PackDefinitionManager;
import com.realisticdining.fabric.client.pack.PackKeyRouter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
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
                GLFW.GLFW_KEY_F8,
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
                // 饮用键=右键 且 主手是零食/饮料时，让位给右键事件（放置展示台/兜底饮用），
                // 跳过动画触发，防止动画期间 ServerEatingState 拦截后续放置（与 NeoForge 端让位机制一致）。
                // 仅跳过动画，不代理右键——右键处理由原版 useKey 与 UseBlockCallback 兜底完成。
                if (isDrinkKeyBoundToRightMouse() && isMainHandSnack()) {
                    continue;
                }
                // 饮用键=右键 且 主手是 BlockItem 材质包扩展物品时，让位给原版右键放置方块（生成 3D 模型）。
                // 不触发动画——右键空气时由 UseItemCallback 兜底触发动画。
                if (isDrinkKeyBoundToRightMouse() && isMainHandBlockPackItem()) {
                    continue;
                }
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

    /**
     * 判断「吃米饭键」（默认 T）当前是否绑定为鼠标右键。
     * <p>T 键绑右键时会与原版 useKey 产生单键映射冲突，需要代理原版右键交互。
     */
    public static boolean isEatRiceKeyBoundToRightMouse() {
        return triggerEatRiceKey != null && triggerEatRiceKey.matchesMouse(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
    }

    /** 主手物品是否为主模组零食/饮料（这类物品右键会走放置展示台/兜底饮用逻辑）。 */
    public static boolean isMainHandSnack() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;
        ItemStack mainHand = mc.player.getMainHandItem();
        return !mainHand.isEmpty() && SnackItemRegistry.isSnackItem(mainHand.getItem());
    }

    /** 主手物品是否是材质包扩展物品（Pack）。 */
    public static boolean isMainHandPackItem() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;
        ItemStack mainHand = mc.player.getMainHandItem();
        if (mainHand.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(mainHand.getItem());
        return id != null && PackDefinitionManager.containsItem(id.toString());
    }

    /**
     * 主手物品是否是 BlockItem 材质包扩展物品（可放置 3D 方块模型，如原版橡树原木/石头等）。
     * <p>用于「饮用键=右键」时让位给原版右键放置方块——BlockItem 右键地面应放置方块而非播放动画。
     */
    public static boolean isMainHandBlockPackItem() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;
        ItemStack mainHand = mc.player.getMainHandItem();
        if (mainHand.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(mainHand.getItem());
        return id != null
                && PackDefinitionManager.containsItem(id.toString())
                && mainHand.getItem() instanceof BlockItem;
    }
}
