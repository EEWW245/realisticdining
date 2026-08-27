package com.example.realisticdining.common;

import com.example.realisticdining.platform.ServiceHelper;
import net.minecraft.world.entity.player.Player;
import java.lang.reflect.Method;

/**
 * LegendarySurvivalOverhaul（LSO）联动桥接器。
 *
 * <p>用反射调用 LSO 的 {@code sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil.takeDrink}，
 * 避免在 build.gradle 中硬依赖 LSO（LSO 未安装时也能正常加载本模组）。
 *
 * <p>LSO 提供的水分系统：每个玩家有水分值（thirst，类似饥饿值，上限 20）和水分饱和度
 * （saturation，类似饱和度）。喝饮料时调用 takeDrink(player, hydration, saturation) 即可补充。
 *
 * <p>本模组所有饮料（除米饭等主食）喝完后均会通过本类调用 LSO 的 takeDrink。
 * 若 LSO 未加载则跳过，不影响本模组原有功能。
 */
public final class LSOCompat {

    private static final String LSO_MODID = "legendarysurvivaloverhaul";
    private static final String THIRST_UTIL_CLASS =
            "sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil";

    private static Boolean loaded;
    private static Method takeDrinkMethod;
    private static boolean initFailed;

    private LSOCompat() {}

    /** LSO 模组是否加载（懒加载，结果缓存）。 */
    public static boolean isLoaded() {
        if (loaded == null) {
            loaded = ServiceHelper.getPlatformServices().isModLoaded(LSO_MODID);
        }
        return loaded;
    }

    /**
     * 按 drinkId 查表补充对应饮料的水分和水分饱和度。
     *
     * <p>数值参考 vanilla 食物配置：水分上限通常 20，瓶装饮料喝 2 口给约 8 点水分。
     * 单次消耗的饮料（maxUses=1）一次性给满；瓶装饮料（maxUses=2）的每次消耗给一半水分。
     *
     * <p>LSO 未加载时静默跳过。
     */
    public static void applyThirstByDrink(Player player, String drinkId, int maxUses) {
        if (!isLoaded() || player == null) return;

        int hydration;
        float saturation;
        switch (drinkId) {
            case "mineral_water":        hydration = 8; saturation = 0.5f; break;
            case "peach_grapefruit_tea": hydration = 6; saturation = 0.4f; break;
            case "sports_drink":         hydration = 7; saturation = 0.6f; break;  // 运动饮料补水效果好
            case "iced_tea":             hydration = 6; saturation = 0.4f; break;
            case "coconut_juice":        hydration = 7; saturation = 0.5f; break;
            case "orange_juice":         hydration = 6; saturation = 0.5f; break;
            case "milk_beer":            hydration = 5; saturation = 0.4f; break;
            case "soymilk":              hydration = 6; saturation = 0.6f; break;
            case "cola":                 hydration = 5; saturation = 0.3f; break;
            case "beer":                 hydration = 3; saturation = 0.3f; break;  // 啤酒利尿，补水效果差
            case "pearl_milk_tea":       hydration = 6; saturation = 0.5f; break;  // 奶茶补水
            default: return;
        }

        // 瓶装饮料每次消耗给一半水分（瓶口喝一次，第二次喝完才给满）
        if (maxUses >= 2) {
            hydration = Math.max(1, hydration / 2);
            saturation = saturation / 2.0f;
        }

        applyThirst(player, hydration, saturation);
    }

    /**
     * 反射调用 LSO 的 ThirstUtil.takeDrink(Player, int, float)。
     * 反射失败（API 变更等）静默忽略并标记 initFailed，避免重复尝试。
     */
    private static void applyThirst(Player player, int hydration, float saturation) {
        if (initFailed) return;
        try {
            if (takeDrinkMethod == null) {
                Class<?> cls = Class.forName(THIRST_UTIL_CLASS);
                takeDrinkMethod = cls.getMethod("takeDrink",
                        Player.class, int.class, float.class);
            }
            takeDrinkMethod.invoke(null, player, hydration, saturation);
        } catch (Exception e) {
            initFailed = true;
            // LSO API 不兼容或未就绪，静默忽略
        }
    }
}
