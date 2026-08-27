package com.realisticdining.neoforge.client.arm.drink;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.realisticdining.registry.ModSounds;
import com.realisticdining.registry.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;

/**
 * 饮用动画注册中心。
 *
 * <p>新增饮料步骤：
 * <ol>
 *   <li>把贴图 png 放到 common/src/main/resources/assets/realisticdining/textures/block/</li>
 *   <li>如需新动画类型：把 geo.json + animation.json 放到对应目录</li>
 *   <li>在下面 register 一行配置，传入对应音效组</li>
 *   <li>在 ModKeybinds 加一个按键，调用 FpArmRenderSystem.triggerDrink("xxx")</li>
 * </ol>
 *
 * <p>同一种动画类型可被多个饮料复用（例如未来加苏打水复用 BOTTLE，加王老吉复用 CAN），
 * 仅贴图路径不同，每个饮料保持独立 controller 名。音效组同理：复用 CAN 动画就复用 CAN_SOUNDS。
 */
public class DrinkAnimRegistry {

    private static final Map<String, DrinkAnimHandler> DRINKS = new HashMap<>();

    // === 动画类型（geo + animation 资源组合，可被多个饮料复用） ===
    // BOTTLE：矿泉水瓶形动画
    private static final String BOTTLE_GEO = "geo/mineralwaterbottle_geckolib.geo.json";
    private static final String BOTTLE_ANIM = "animations/mineralwaterbottle_geckolib.animation.json";
    private static final String BOTTLE_RAW_ANIM = "animation.unknown.new";

    // BOTTLED_DRINKS：新瓶形动画（用于果汁/茶饮等瓶装饮料）
    private static final String BOTTLED_DRINKS_GEO = "geo/bottled_drinks.geo.json";
    private static final String BOTTLED_DRINKS_ANIM = "animations/bottled_drinks.animation.json";
    private static final String BOTTLED_DRINKS_RAW_ANIM = "animation.unknown.new";

    // CAN：罐装饮料动画
    private static final String CAN_GEO = "geo/mik_beer.geo.json";
    private static final String CAN_ANIM = "animations/mik_beer.animation.json";
    private static final String CAN_RAW_ANIM = "animation.unknown.new";

    // ENERGY_BAR：军用能量棒动画
    private static final String ENERGY_BAR_GEO = "geo/energy_bar.geo.json";
    private static final String ENERGY_BAR_ANIM = "animations/energy_bar.animation.json";
    private static final String ENERGY_BAR_RAW_ANIM = "animation.unknown.new";

    // CRISP_1：薯片变体动画（crisp 的变体，用于各种口味薯片）
    private static final String CRISP_1_GEO = "geo/crisp_1.geo.json";
    private static final String CRISP_1_ANIM = "animations/crisp_1.animation.json";
    private static final String CRISP_1_RAW_ANIM = "animation.unknown.new";

    // BISCUIT：饼干动画（能量棒的变体，模型变大变成饼干）
    private static final String BISCUIT_GEO = "geo/biscuit.geo.json";
    private static final String BISCUIT_ANIM = "animations/biscuit.animation.json";
    private static final String BISCUIT_RAW_ANIM = "animation.unknown.new";

    // MILKTEA：珍珠奶茶动画（独立 pickup + eat 双动画，pickup 定格持物）
    private static final String MILKTEA_GEO = "geo/cooked_beef_milktea.geo.json";
    private static final String MILKTEA_ANIM = "animations/cooked_beef_milktea.animation.json";
    private static final String MILKTEA_RAW_ANIM = "eat";

    // === 音效组（与动画类型一一对应，可被同类型饮料复用） ===
    // 注：直接传递 RegistrySupplier 本身（不调用 .get()），由 DrinkSoundCue 在播放时懒解析，
    //     避免 DrinkAnimRegistry 静态初始化时 registry 未填充导致拿到 null。
    // CRISP：薯片（7.5s，6 cue）
    private static final List<DrinkSoundCue> CRISP_SOUNDS = List.of(
            DrinkSoundCue.once(ModSounds.DRINK_CRISP_BAG_PICKUP, 0.0),
            DrinkSoundCue.once(ModSounds.DRINK_CRISP_BAG_OPEN, 1.25),
            DrinkSoundCue.once(ModSounds.DRINK_CRISP_CRUNCH, 3.0),
            DrinkSoundCue.once(ModSounds.DRINK_CRISP_CRUNCH, 4.0),
            DrinkSoundCue.once(ModSounds.DRINK_CRISP_CRUNCH, 5.0),
            DrinkSoundCue.once(ModSounds.DRINK_CRISP_CRUNCH, 6.0)
    );

    // WATER：矿泉水/瓶装饮料（6.0s，3 cue）—— 拧瓶盖 1.0s + 喝水 3.0s + AHH 5.0s
    private static final List<DrinkSoundCue> WATER_SOUNDS = List.of(
            DrinkSoundCue.once(ModSounds.DRINK_WATER_CAP_OFF, 1.0),
            DrinkSoundCue.once(ModSounds.DRINK_WATER_GULP, 3.0),
            DrinkSoundCue.once(ModSounds.DRINK_WATER_AHH, 5.0)
    );

    // ENERGY_BAR：能量棒（5.0s，3 cue）
    private static final List<DrinkSoundCue> ENERGY_BAR_SOUNDS = List.of(
            DrinkSoundCue.once(ModSounds.DRINK_ENERGYBAR_WRAPPER_PICKUP, 0.0),
            DrinkSoundCue.once(ModSounds.DRINK_ENERGYBAR_WRAPPER_OPEN, 1.25),
            DrinkSoundCue.once(ModSounds.DRINK_ENERGYBAR_BAR_CHEW, 2.5)
    );

    // CAN：罐装饮料（7.1667s，3 cue）—— 拉环 1.0s + 喝水 2.8s + AHH 6.5s
    private static final List<DrinkSoundCue> CAN_SOUNDS = List.of(
            DrinkSoundCue.once(ModSounds.DRINK_CAN_PULL_TAB, 1.0),
            DrinkSoundCue.once(ModSounds.DRINK_CAN_GULP, 2.8),
            DrinkSoundCue.once(ModSounds.DRINK_CAN_AHH, 6.5)
    );

    // MILKTEA：珍珠奶茶（5.0833s，2 cue）—— 插吸管 1.0s + 喝水 1.75s
    private static final List<DrinkSoundCue> MILKTEA_SOUNDS = List.of(
            DrinkSoundCue.once(ModSounds.DRINK_MILKTEA_STRAW_INSERT, 1.0),
            DrinkSoundCue.once(ModSounds.DRINK_MILKTEA_GULP, 1.75)
    );

    // === 饮料实例 ===
    // v2.1.4+ 持物前缀模式：holdDuration > 0 时物品进入主手自动播放 drink 动画前 N 秒并定格
    // hideLeftArm：持物阶段隐藏 Left Arm 骨骼（瓶装/罐装/薯片需要）
    public static final DrinkAnimHandler MINERAL_WATER = register(
            "realisticdining", "mineral_water", 6.75,
            BOTTLE_GEO, BOTTLE_ANIM, BOTTLE_RAW_ANIM,
            "textures/block/mineralwaterbottle.png",
            "mineral_water_controller", "drink",
            WATER_SOUNDS,
            0.75, true);

    public static final DrinkAnimHandler MILK_BEER = register(
            "realisticdining", "milk_beer", 7.1667,
            CAN_GEO, CAN_ANIM, CAN_RAW_ANIM,
            "textures/block/milk_beer.png",
            "milk_beer_controller", "drink",
            CAN_SOUNDS,
            0.75, true);

    public static final DrinkAnimHandler ENERGY_BAR = register(
            "realisticdining", "energy_bar", 5.0,
            ENERGY_BAR_GEO, ENERGY_BAR_ANIM, ENERGY_BAR_RAW_ANIM,
            "textures/block/energy_bar.png",
            "energy_bar_controller", "drink",
            ENERGY_BAR_SOUNDS,
            0.75, false);

    // === 瓶装组（套用新瓶形动画 + WATER_SOUNDS，持物 0.5s 隐藏左臂） ===
    public static final DrinkAnimHandler PEACH_GRAPEFRUIT_TEA = register(
            "realisticdining", "peach_grapefruit_tea", 6.0,
            BOTTLED_DRINKS_GEO, BOTTLED_DRINKS_ANIM, BOTTLED_DRINKS_RAW_ANIM,
            "textures/item/peach_grapefruit_tea_64x64.png",
            "peach_grapefruit_tea_controller", "drink",
            WATER_SOUNDS,
            0.5, true);

    public static final DrinkAnimHandler SPORTS_DRINK = register(
            "realisticdining", "sports_drink", 6.0,
            BOTTLED_DRINKS_GEO, BOTTLED_DRINKS_ANIM, BOTTLED_DRINKS_RAW_ANIM,
            "textures/item/sports_drink_64x64.png",
            "sports_drink_controller", "drink",
            WATER_SOUNDS,
            0.5, true);

    public static final DrinkAnimHandler ICED_TEA = register(
            "realisticdining", "iced_tea", 6.0,
            BOTTLED_DRINKS_GEO, BOTTLED_DRINKS_ANIM, BOTTLED_DRINKS_RAW_ANIM,
            "textures/item/iced_tea_64x64.png",
            "iced_tea_controller", "drink",
            WATER_SOUNDS,
            0.5, true);

    public static final DrinkAnimHandler COCONUT_JUICE = register(
            "realisticdining", "coconut_juice", 6.0,
            BOTTLED_DRINKS_GEO, BOTTLED_DRINKS_ANIM, BOTTLED_DRINKS_RAW_ANIM,
            "textures/item/coconut_juice_64x64.png",
            "coconut_juice_controller", "drink",
            WATER_SOUNDS,
            0.5, true);

    public static final DrinkAnimHandler ORANGE_JUICE = register(
            "realisticdining", "orange_juice", 6.0,
            BOTTLED_DRINKS_GEO, BOTTLED_DRINKS_ANIM, BOTTLED_DRINKS_RAW_ANIM,
            "textures/item/orange_juice_64x64.png",
            "orange_juice_controller", "drink",
            WATER_SOUNDS,
            0.5, true);

    // === 罐装组（套用罐装饮料动画 + CAN_SOUNDS，持物 0.75s 隐藏左臂） ===
    public static final DrinkAnimHandler SOYMILK = register(
            "realisticdining", "soymilk", 7.1667,
            CAN_GEO, CAN_ANIM, CAN_RAW_ANIM,
            "textures/item/soymilk_texture_64x64.png",
            "soymilk_controller", "drink",
            CAN_SOUNDS,
            0.75, true);

    public static final DrinkAnimHandler COLA = register(
            "realisticdining", "cola", 7.1667,
            CAN_GEO, CAN_ANIM, CAN_RAW_ANIM,
            "textures/item/cola_texture_64x64.png",
            "cola_controller", "drink",
            CAN_SOUNDS,
            0.75, true);

    public static final DrinkAnimHandler BEER = register(
            "realisticdining", "beer", 7.1667,
            CAN_GEO, CAN_ANIM, CAN_RAW_ANIM,
            "textures/item/beer_texture_64x64.png",
            "beer_controller", "drink",
            CAN_SOUNDS,
            0.75, true);

    // === 薯片变体组（套用 crisp_1 动画 + CRISP_SOUNDS，持物 0.75s 隐藏左臂） ===
    public static final DrinkAnimHandler POTATO_CHIPS = register(
            "realisticdining", "potato_chips", 7.5,
            CRISP_1_GEO, CRISP_1_ANIM, CRISP_1_RAW_ANIM,
            "textures/item/potato_chips.png",
            "potato_chips_controller", "drink",
            CRISP_SOUNDS,
            0.75, true);

    public static final DrinkAnimHandler POTATO_CHIPS_BBQ = register(
            "realisticdining", "potato_chips_bbq", 7.5,
            CRISP_1_GEO, CRISP_1_ANIM, CRISP_1_RAW_ANIM,
            "textures/item/potato_chips_bbq.png",
            "potato_chips_bbq_controller", "drink",
            CRISP_SOUNDS,
            0.75, true);

    public static final DrinkAnimHandler POTATO_CHIPS_CUCUMBER = register(
            "realisticdining", "potato_chips_cucumber", 7.5,
            CRISP_1_GEO, CRISP_1_ANIM, CRISP_1_RAW_ANIM,
            "textures/item/potato_chips_cucumber.png",
            "potato_chips_cucumber_controller", "drink",
            CRISP_SOUNDS,
            0.75, true);

    public static final DrinkAnimHandler POTATO_CHIPS_TOMATO = register(
            "realisticdining", "potato_chips_tomato", 7.5,
            CRISP_1_GEO, CRISP_1_ANIM, CRISP_1_RAW_ANIM,
            "textures/item/potato_chips_tomato.png",
            "potato_chips_tomato_controller", "drink",
            CRISP_SOUNDS,
            0.75, true);

    public static final DrinkAnimHandler CRISPY_FISH_CHIPS = register(
            "realisticdining", "crispy_fish_chips", 7.5,
            CRISP_1_GEO, CRISP_1_ANIM, CRISP_1_RAW_ANIM,
            "textures/item/crispy_fish_chips.png",
            "crispy_fish_chips_controller", "drink",
            CRISP_SOUNDS,
            0.75, true);

    // === 饼干组（套用 biscuit 动画 + ENERGY_BAR_SOUNDS，持物 0.75s 不隐藏左臂） ===
    public static final DrinkAnimHandler COOKIE_BAG = register(
            "realisticdining", "cookie_bag", 5.0,
            BISCUIT_GEO, BISCUIT_ANIM, BISCUIT_RAW_ANIM,
            "textures/block/cookie_bag.png",
            "cookie_bag_controller", "drink",
            ENERGY_BAR_SOUNDS,
            0.75, false);

    public static final DrinkAnimHandler COOKIE_BAG_COCONUT_LATTE = register(
            "realisticdining", "cookie_bag_coconut_latte", 5.0,
            BISCUIT_GEO, BISCUIT_ANIM, BISCUIT_RAW_ANIM,
            "textures/block/cookie_bag_coconut_latte.png",
            "cookie_bag_coconut_latte_controller", "drink",
            ENERGY_BAR_SOUNDS,
            0.75, false);

    // === 珍珠奶茶（独立 pickup + eat 双动画，holdDuration=0 走旧 pickup 路径，持物阶段隐藏左臂） ===
    public static final DrinkAnimHandler PEARL_MILK_TEA = register(
            "realisticdining", "pearl_milk_tea", 5.0833,
            MILKTEA_GEO, MILKTEA_ANIM, MILKTEA_RAW_ANIM,
            "textures/item/cooked_beef_milktea.png",
            "pearl_milk_tea_controller", "drink",
            MILKTEA_SOUNDS,
            0, true);

    static {
        // 珍珠奶茶：配置独立 pickup 动画（0.5s，播完定格持物）；putdown 传 null（切物品瞬间消失）
        registerPickup(PEARL_MILK_TEA, "pickup", 0.5, null, 0);
    }

    // === 物品 → 饮料 id 绑定（供 U 键查询主手物品对应的动画） ===
    private static final Map<Supplier<Item>, String> ITEM_TO_DRINK = new HashMap<>();

    static {
        bindItem(ModItems.MINERAL_WATER, "mineral_water");
        bindItem(ModItems.MILK_BEER, "milk_beer");
        bindItem(ModItems.ENERGY_BAR, "energy_bar");
        // v2.0.6+ 新增物品绑定
        bindItem(ModItems.PEACH_GRAPEFRUIT_TEA, "peach_grapefruit_tea");
        bindItem(ModItems.SPORTS_DRINK, "sports_drink");
        bindItem(ModItems.ICED_TEA, "iced_tea");
        bindItem(ModItems.COCONUT_JUICE, "coconut_juice");
        bindItem(ModItems.ORANGE_JUICE, "orange_juice");
        bindItem(ModItems.SOYMILK, "soymilk");
        bindItem(ModItems.COLA, "cola");
        bindItem(ModItems.BEER, "beer");
        bindItem(ModItems.POTATO_CHIPS, "potato_chips");
        bindItem(ModItems.POTATO_CHIPS_BBQ, "potato_chips_bbq");
        bindItem(ModItems.POTATO_CHIPS_CUCUMBER, "potato_chips_cucumber");
        bindItem(ModItems.POTATO_CHIPS_TOMATO, "potato_chips_tomato");
        bindItem(ModItems.CRISPY_FISH_CHIPS, "crispy_fish_chips");
        bindItem(ModItems.COOKIE_BAG, "cookie_bag");
        bindItem(ModItems.COOKIE_BAG_COCONUT_LATTE, "cookie_bag_coconut_latte");
        bindItem(ModItems.PEARL_MILK_TEA, "pearl_milk_tea");
    }

    /**
     * 绑定物品到饮料 id。附属可在初始化阶段调用此方法，
     * 让 U 键自动识别附属饮料物品并触发对应动画。
     * @param itemSupplier 物品供应者（RegistrySupplier 实现了 Supplier）
     * @param drinkId 在 {@link #register} 中注册的饮料 id
     */
    public static void bindItem(Supplier<Item> itemSupplier, String drinkId) {
        ITEM_TO_DRINK.put(itemSupplier, drinkId);
    }

    /** 查询物品对应的饮料 id，无绑定返回 null。 */
    public static String drinkIdForItem(Item item) {
        for (Map.Entry<Supplier<Item>, String> e : ITEM_TO_DRINK.entrySet()) {
            Item bound = e.getKey().get();
            if (bound != null && bound == item) return e.getValue();
        }
        return null;
    }

    /**
     * 注册一个饮料动画 handler（单贴图版本，向后兼容）。
     * 等价于调用 {@link #register(String, String, double, String, String, String, String, String, String, List, Map)}
     * 并传入空 Map。
     *
     * <p><b>骨骼命名约定（必须遵守）</b>：
     * <ul>
     *   <li>{@code Left Arm} / {@code Right Arm} / {@code Right_Arm2} 三根骨骼名严格区分大小写和空格，UV 必须映射到 64×64 玩家标准皮肤布局（左臂起始 32,48；右臂起始 40,16）。建议在 BlockBench 用 "Java Entity / Player" 模板新建模型</li>
     *   <li>除上述三根手臂骨骼外，所有骨骼自动走 {@code texturePath} 食物贴图（必须 64×64 PNG）</li>
     *   <li>贴图查表规则：手臂骨骼→玩家皮肤；命中 boneTextures 的骨骼→对应贴图；其他→texturePath 默认贴图</li>
     * </ul>
     *
     * @return 该饮料的 handler，可用于主动触发动画
     */
    public static DrinkAnimHandler register(String modId, String id, double duration,
                                              String geoPath, String animationPath, String rawAnimationName,
                                              String texturePath,
                                              String controllerName, String triggerName,
                                              List<DrinkSoundCue> soundCues) {
        return register(modId, id, duration, geoPath, animationPath, rawAnimationName,
                texturePath, controllerName, triggerName, soundCues, java.util.Collections.emptyMap());
    }

    /**
     * v2.1.4+ 注册一个饮料动画 handler（带持物前缀 + 左臂隐藏配置，单贴图版本）。
     *
     * @param holdDuration  持物阶段时长（秒）；> 0 时启用持物前缀模式，物品进入主手时自动播放 drink 动画的
     *                      0~holdDuration 秒并定格，U 键后从 holdDuration 继续播到结尾。传 0 走旧 IDLE↔DRINK 路径。
     * @param hideLeftArm   持物阶段（PICKUP/HOLD）是否隐藏 Left Arm 骨骼
     */
    public static DrinkAnimHandler register(String modId, String id, double duration,
                                              String geoPath, String animationPath, String rawAnimationName,
                                              String texturePath,
                                              String controllerName, String triggerName,
                                              List<DrinkSoundCue> soundCues,
                                              double holdDuration, boolean hideLeftArm) {
        return register(modId, id, duration, geoPath, animationPath, rawAnimationName,
                texturePath, controllerName, triggerName, soundCues, java.util.Collections.emptyMap(),
                holdDuration, hideLeftArm);
    }

    /**
     * 注册一个饮料动画 handler（多贴图版本，单模型，向后兼容）。
     * 主模组传 modId = "realisticdining"；附属传自己的 mod id，
     * geo/animation/texture 资源会从附属自己的 namespace 下加载。
     *
     * <p><b>骨骼命名约定（必须遵守）</b>：
     * <ul>
     *   <li>{@code Left Arm} / {@code Right Arm} / {@code Right_Arm2} 三根骨骼名严格区分大小写和空格，UV 必须映射到 64×64 玩家标准皮肤布局（左臂起始 32,48；右臂起始 40,16）</li>
     *   <li>除上述三根手臂骨骼外，所有骨骼走食物贴图（必须 64×64 PNG）</li>
     *   <li>贴图查表规则：手臂骨骼→玩家皮肤（自动，无需在 boneTextures 声明）；命中 boneTextures 的骨骼→对应贴图；其他→texturePath 默认贴图</li>
     *   <li>boneTextures 的 key 是 BlockBench 骨骼名，value 是相对 assets/modId/ 的贴图路径（不带 modId 前缀）</li>
     * </ul>
     *
     * @param boneTextures 骨骼名 → 贴图路径映射；为空 Map 时所有非手臂骨骼走 texturePath 默认贴图
     * @return 该饮料的 handler，可用于主动触发动画
     */
    public static DrinkAnimHandler register(String modId, String id, double duration,
                                              String geoPath, String animationPath, String rawAnimationName,
                                              String texturePath,
                                              String controllerName, String triggerName,
                                              List<DrinkSoundCue> soundCues,
                                              Map<String, String> boneTextures) {
        return register(modId, id, duration, geoPath, animationPath, rawAnimationName,
                texturePath, controllerName, triggerName,
                null, null, null, null, null, null,
                soundCues, boneTextures, null,
                0, false);
    }

    /**
     * v2.1.4+ 注册一个饮料动画 handler（带持物前缀 + 左臂隐藏配置，多贴图版本）。
     */
    public static DrinkAnimHandler register(String modId, String id, double duration,
                                              String geoPath, String animationPath, String rawAnimationName,
                                              String texturePath,
                                              String controllerName, String triggerName,
                                              List<DrinkSoundCue> soundCues,
                                              Map<String, String> boneTextures,
                                              double holdDuration, boolean hideLeftArm) {
        return register(modId, id, duration, geoPath, animationPath, rawAnimationName,
                texturePath, controllerName, triggerName,
                null, null, null, null, null, null,
                soundCues, boneTextures, null,
                holdDuration, hideLeftArm);
    }

    /**
     * 注册一个饮料动画 handler（双模型版本，v2.0.7+）。
     *
     * <p>双模型模式下，Handler 会同时持有两套 model/animatable/renderer，每帧依次渲染两个独立 geo 模型，
     * 类似吃米饭动画的"左手碗+右手筷子"双模型架构。适用于左右手物品完全不同的进食动画
     * （如左拿汤勺右拿碗、左拿面包右拿刀）。
     *
     * <p>第一组与第二组各有独立的 geo/animation/texture/controller/trigger/boneTextures。
     * 两组共享同一个 duration 和 soundCues（音效只播一遍，不重复）。
     *
     * <p>注意：两组的 controllerName 必须互不相同，且与模组内其他动画的 controller 不冲突。
     *
     * @param modId             附属 mod id（如 "myaddon"）
     * @param id                动画唯一 id
     * @param duration          动画时长（秒，两组必须一致）
     * @param geoPath1          第一组 geo 路径
     * @param animationPath1    第一组 animation 路径
     * @param rawAnimationName1 第一组 animation.json 内的动画名
     * @param texturePath1      第一组默认贴图
     * @param controllerName1   第一组 GeckoLib controller 名（必须唯一）
     * @param triggerName1      第一组 trigger 名
     * @param geoPath2          第二组 geo 路径
     * @param animationPath2    第二组 animation 路径
     * @param rawAnimationName2 第二组 animation.json 内的动画名
     * @param texturePath2      第二组默认贴图
     * @param controllerName2   第二组 GeckoLib controller 名（必须唯一，且 ≠ controllerName1）
     * @param triggerName2      第二组 trigger 名
     * @param soundCues         音效时间轴（共享，只播一遍）
     * @param boneTextures1     第一组骨骼名 → 贴图路径映射；可为空 Map
     * @param boneTextures2     第二组骨骼名 → 贴图路径映射；可为空 Map
     *
     * <p><b>骨骼命名约定（必须遵守，两组都适用）</b>：
     * <ul>
     *   <li>{@code Left Arm} / {@code Right Arm} / {@code Right_Arm2} 三根骨骼名严格区分大小写和空格，UV 必须映射到 64×64 玩家标准皮肤布局（左臂起始 32,48；右臂起始 40,16）</li>
     *   <li>建议第一组只画左臂+左物、第二组只画右臂+右物，避免两组在同一只手上骨骼重叠</li>
     *   <li>除手臂骨骼外，所有骨骼走食物贴图（必须 64×64 PNG）</li>
     *   <li>贴图查表规则：手臂骨骼→玩家皮肤；命中 boneTextures1/2 的骨骼→对应组贴图；其他→texturePath1/2 默认贴图</li>
     * </ul>
     *
     * @return 该饮料的 handler，可用于主动触发动画
     */
    public static DrinkAnimHandler register(String modId, String id, double duration,
                                              String geoPath1, String animationPath1, String rawAnimationName1,
                                              String texturePath1,
                                              String controllerName1, String triggerName1,
                                              String geoPath2, String animationPath2, String rawAnimationName2,
                                              String texturePath2,
                                              String controllerName2, String triggerName2,
                                              List<DrinkSoundCue> soundCues,
                                              Map<String, String> boneTextures1,
                                              Map<String, String> boneTextures2,
                                              double holdDuration, boolean hideLeftArm) {
        DrinkAnimConfig cfg = new DrinkAnimConfig(modId, id, duration,
                geoPath1, animationPath1, rawAnimationName1, texturePath1, controllerName1, triggerName1,
                soundCues, boneTextures1,
                geoPath2, animationPath2, rawAnimationName2, texturePath2, controllerName2, triggerName2,
                boneTextures2,
                holdDuration, hideLeftArm);
        DrinkAnimHandler handler = new DrinkAnimHandler(cfg);
        DRINKS.put(id, handler);
        return handler;
    }

    /**
     * 为已注册的饮料配置 pickup（拿起）/ putdown（放下）动画（v2.1.3+，可选）。
     *
     * <p>配置后，玩家将该饮料物品切到主手时会自动播放 pickup 动画，播完后定格在最后一帧
     * 进入持物等待（HOLD）状态；按 U 键播放 drink 动画；将物品切出主手时播放 putdown 动画。
     *
     * <p><b>关键约束</b>：pickup/putdown 动画必须与 drink 动画在<b>同一个 animation.json 文件</b>内
     * （GeckoLib GeoModel 一次只能加载一个 animation 资源）。附属在 BlockBench 导出时把
     * pickup/drink/putdown 三个动画放在同一个 animation.json 即可。
     *
     * <p>未调用此方法的饮料保持旧行为（IDLE ↔ DRINK，无拿起/放下/持物）。
     *
     * @param handler              {@link #register} 返回的 handler
     * @param pickupRawAnimName    pickup 动画名（animation.json 内，如 "animation.bottle.pickup"）
     * @param pickupDuration       pickup 动画时长（秒，如 0.5）
     * @param putdownRawAnimName   putdown 动画名（可为 null，表示切物品时直接消失不播放下动画）
     * @param putdownDuration      putdown 动画时长（秒，putdownRawAnimName 为 null 时传 0）
     */
    public static void registerPickup(DrinkAnimHandler handler,
                                      String pickupRawAnimName, double pickupDuration,
                                      String putdownRawAnimName, double putdownDuration) {
        if (handler == null) return;
        handler.configurePickup(pickupRawAnimName, pickupDuration,
                                putdownRawAnimName, putdownDuration);
    }

    public static DrinkAnimHandler byId(String id) {
        return DRINKS.get(id);
    }

    public static Collection<DrinkAnimHandler> all() {
        return DRINKS.values();
    }

    /** 是否有任意饮料正在播放（供 Mixin 判断是否屏蔽原版双手渲染）。 */
    public static boolean anyPlaying() {
        for (DrinkAnimHandler h : DRINKS.values()) {
            if (h.isPlaying()) return true;
        }
        return false;
    }

    /** 重置所有饮料状态（关闭手臂渲染时调用）。 */
    public static void resetAll() {
        for (DrinkAnimHandler h : DRINKS.values()) {
            h.reset();
        }
    }
}
