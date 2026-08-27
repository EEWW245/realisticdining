package com.example.realisticdining.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * 零食/饮料物品到放置模型（put_xxx.geo.json）和贴图的映射注册表。
 *
 * <p>本模组共 19 个零食/饮料物品，复用 6 种 put_ 骨骼模型：
 * <ul>
 *   <li>BOTTLE - 矿泉水独立瓶形（put_mineralwaterbottle_geckolib.geo.json）</li>
 *   <li>CAN - 罐装（put_mik_beer.geo.json）</li>
 *   <li>BOTTLED_DRINKS - 瓶装组（put_bottled_drinks.geo.json）</li>
 *   <li>CRISP - 薯片（put_crisp.geo.json）</li>
 *   <li>ENERGY_BAR - 能量棒（put_energy_bar.geo.json）</li>
 *   <li>BISCUIT - 饼干（put_biscuit.geo.json）</li>
 * </ul>
 *
 * <p>注册 key 是物品的 ResourceLocation 字符串形式（"modid:itemid"），便于 BlockEntity 持久化时直接存字符串。
 * 启动时通过 SnackItemRegistry 查回 ModelType + texturePath，存档迁移无痛。
 */
public class SnackItemRegistry {

    public enum ModelType {
        BOTTLE("geo/put_mineralwaterbottle_geckolib.geo.json"),
        CAN("geo/put_mik_beer.geo.json"),
        BOTTLED_DRINKS("geo/put_bottled_drinks.geo.json"),
        CRISP("geo/put_crisp.geo.json"),
        ENERGY_BAR("geo/put_energy_bar.geo.json"),
        BISCUIT("geo/put_biscuit.geo.json"),
        MILKTEA("geo/put_cooked_beef_milktea.geo.json");

        public final String geoPath;
        ModelType(String geoPath) { this.geoPath = geoPath; }
    }

    public record SnackEntry(ModelType modelType, String texturePath) {}

    private static final Map<String, SnackEntry> REGISTRY = new HashMap<>();

    static {
        // === 矿泉水独立瓶形 ===
        register("realisticdining:mineral_water", ModelType.BOTTLE, "textures/block/mineralwaterbottle.png");
        // === 罐装组（4 个）===
        register("realisticdining:milk_beer", ModelType.CAN, "textures/block/milk_beer.png");
        register("realisticdining:soymilk", ModelType.CAN, "textures/item/soymilk_texture_64x64.png");
        register("realisticdining:cola", ModelType.CAN, "textures/item/cola_texture_64x64.png");
        register("realisticdining:beer", ModelType.CAN, "textures/item/beer_texture_64x64.png");
        // === 瓶装组（5 个）===
        register("realisticdining:peach_grapefruit_tea", ModelType.BOTTLED_DRINKS, "textures/item/peach_grapefruit_tea_64x64.png");
        register("realisticdining:sports_drink", ModelType.BOTTLED_DRINKS, "textures/item/sports_drink_64x64.png");
        register("realisticdining:iced_tea", ModelType.BOTTLED_DRINKS, "textures/item/iced_tea_64x64.png");
        register("realisticdining:coconut_juice", ModelType.BOTTLED_DRINKS, "textures/item/coconut_juice_64x64.png");
        register("realisticdining:orange_juice", ModelType.BOTTLED_DRINKS, "textures/item/orange_juice_64x64.png");
        // === 薯片组（5 个）===
        register("realisticdining:potato_chips", ModelType.CRISP, "textures/item/potato_chips.png");
        register("realisticdining:potato_chips_bbq", ModelType.CRISP, "textures/item/potato_chips_bbq.png");
        register("realisticdining:potato_chips_cucumber", ModelType.CRISP, "textures/item/potato_chips_cucumber.png");
        register("realisticdining:potato_chips_tomato", ModelType.CRISP, "textures/item/potato_chips_tomato.png");
        register("realisticdining:crispy_fish_chips", ModelType.CRISP, "textures/item/crispy_fish_chips.png");
        // === 能量棒 ===
        register("realisticdining:energy_bar", ModelType.ENERGY_BAR, "textures/block/energy_bar.png");
        // === 饼干组（2 个）===
        register("realisticdining:cookie_bag", ModelType.BISCUIT, "textures/block/cookie_bag.png");
        register("realisticdining:cookie_bag_coconut_latte", ModelType.BISCUIT, "textures/block/cookie_bag_coconut_latte.png");
        // === 珍珠奶茶 ===
        register("realisticdining:pearl_milk_tea", ModelType.MILKTEA, "textures/item/cooked_beef_milktea.png");
    }

    public static void register(String itemId, ModelType type, String texturePath) {
        REGISTRY.put(itemId, new SnackEntry(type, texturePath));
    }

    public static SnackEntry byItemId(String itemId) {
        return REGISTRY.get(itemId);
    }

    public static String itemIdOf(Item item) {
        ResourceLocation loc = BuiltInRegistries.ITEM.getKey(item);
        return loc != null ? loc.toString() : null;
    }

    public static boolean isSnackItem(Item item) {
        String id = itemIdOf(item);
        return id != null && REGISTRY.containsKey(id);
    }

    /**
     * 判断是否是奶啤/豆浆——这两款饮料可消除消极 Buff，
     * tooltip 需要附加「可消除消极Buff」提示。
     */
    public static boolean isCureBeverage(Item item) {
        String id = itemIdOf(item);
        if (id == null) return false;
        return "realisticdining:milk_beer".equals(id)
                || "realisticdining:soymilk".equals(id);
    }

    /**
     * 判断是否是啤酒——累积喝 2/4/6 瓶触发不同等级醉酒效果，
     * tooltip 需要附加「连续喝 2 瓶微醺，4 瓶中度醉酒，6 瓶以上重度醉酒」提示。
     */
    public static boolean isBeer(Item item) {
        String id = itemIdOf(item);
        return "realisticdining:beer".equals(id);
    }
}
