package com.example.realisticdining.init;

import com.example.realisticdining.RealisticDining;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * 模组自定义物品标签。
 *
 * <p>用于跨模组食材识别：森罗物语 / 农夫乐事 / 其他整合包统一后的食材
 * 只要带这些标签即被本模组识别为对应语义（番茄、生菜、辣椒、米饭等）。
 *
 * <p>标签内容由 {@code data/realisticdining/tags/items/*.json} 定义，
 * 整合包作者可通过数据包覆盖或追加，无需改源码。
 */
public class ModTags {

    public static final class Items {
        /** 生菜/白菜（森罗 lettuce / 农夫乐事 cabbage / 整合包统一物品） */
        public static final TagKey<Item> LETTUCE = create("lettuce");
        /** 番茄（森罗 / 农夫乐事 / 整合包统一物品） */
        public static final TagKey<Item> TOMATO = create("tomato");
        /** 红辣椒（森罗 / 农夫乐事 dry_red_pepper / 整合包统一物品） */
        public static final TagKey<Item> RED_CHILI = create("red_chili");
        /** 青辣椒（森罗 / 整合包统一物品） */
        public static final TagKey<Item> GREEN_CHILI = create("green_chili");
        /** 米饭（森罗 / 农夫乐事 cooked_rice / 整合包统一物品） */
        public static final TagKey<Item> RICE = create("rice");
        /** 蛋炒饭（森罗 / 整合包统一物品） */
        public static final TagKey<Item> EGG_FRIED_RICE = create("egg_fried_rice");

        private static TagKey<Item> create(String name) {
            return TagKey.create(net.minecraft.core.registries.Registries.ITEM,
                    new ResourceLocation(RealisticDining.MOD_ID, name));
        }
    }
}
