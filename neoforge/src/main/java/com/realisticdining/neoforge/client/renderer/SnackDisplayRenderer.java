package com.realisticdining.neoforge.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.realisticdining.RealisticDining;
import com.realisticdining.blockentities.SnackDisplayBlockEntity;
import com.realisticdining.blocks.SnackDisplayBlock;
import com.realisticdining.common.SnackItemRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

/**
 * 零食/饮料展示台渲染器（NeoForge 端，MC 1.21.1）。
 *
 * <p>在 1 个 Renderer 中循环渲染 4 个槽位的 GeoModel（每个槽位可能是不同 put_ 模型 + 不同贴图）。
 * 通过 BlockEntity 的 {@code currentRenderingSlot} transient 字段在循环中切换，
 * 内部 {@link SnackDisplayModel} 在 getModelResource / getTextureResource 中读
 * animatable.getCurrentRenderingSlot() 返回对应槽位的 ResourceLocation。
 *
 * <p>每个槽位先 pushPose + translate 到槽位中心（方块本地坐标 0~1），
 * 模型几何中心默认在方块中心 (0.5, 0, 0.5)，所以 translate (slotX - 0.5, 0, slotZ - 0.5)。
 *
 * <p>槽位顺序：0=远左, 1=远右, 2=近左, 3=近右（按 facing 旋转后的玩家视角）。
 * 槽位空（slots[i]==null）时跳过，不渲染该槽位。
 */
public class SnackDisplayRenderer extends GeoBlockRenderer<SnackDisplayBlockEntity> {

    private static final ResourceLocation FALLBACK_MODEL =
            ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "geo/put_biscuit.geo.json");
    private static final ResourceLocation FALLBACK_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "textures/block/wok_metal.png");
    private static final ResourceLocation NO_ANIMATION =
            ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "animations/snack_display_empty.animation.json");

    private static final SnackDisplayModel MODEL_INSTANCE = new SnackDisplayModel();

    public SnackDisplayRenderer(BlockEntityRendererProvider.Context context) {
        super(MODEL_INSTANCE);
    }

    @Override
    public void render(@NotNull SnackDisplayBlockEntity entity, float partialTick, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Direction facing = entity.getBlockState().getValue(SnackDisplayBlock.FACING);

        for (int i = 0; i < SnackDisplayBlockEntity.SLOT_COUNT; i++) {
            if (entity.getSlotItem(i) == null) continue;

            poseStack.pushPose();
            double[] slotPos = getSlotPosition(i, facing);
            poseStack.translate(slotPos[0] - 0.5, 0.0, slotPos[2] - 0.5);

            entity.setCurrentRenderingSlot(i);
            super.render(entity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
            poseStack.popPose();
        }
        entity.setCurrentRenderingSlot(-1);
    }

    /**
     * 槽位 i 在 facing 朝向下的方块本地坐标中心（0~1 范围）。
     * 槽位 0=远左, 1=远右, 2=近左, 3=近右。
     */
    private static double[] getSlotPosition(int slot, Direction facing) {
        double lx = (slot == 0 || slot == 2) ? 0.25 : 0.75;
        double lz = (slot == 0 || slot == 1) ? 0.25 : 0.75;
        return switch (facing) {
            case NORTH -> new double[]{lx, 0, lz};
            case SOUTH -> new double[]{1 - lx, 0, 1 - lz};
            case EAST -> new double[]{1 - lz, 0, lx};
            case WEST -> new double[]{lz, 0, 1 - lx};
            default -> new double[]{lx, 0, lz};
        };
    }

    /**
     * 静态内部 GeoModel：通过 animatable.getCurrentRenderingSlot() 拿当前槽位，
     * 返回对应槽位的 ResourceLocation。
     */
    public static class SnackDisplayModel extends GeoModel<SnackDisplayBlockEntity> {

        @Override
        public ResourceLocation getModelResource(SnackDisplayBlockEntity animatable) {
            int slot = animatable.getCurrentRenderingSlot();
            if (slot < 0) return FALLBACK_MODEL;
            Item item = animatable.getSlotItem(slot);
            if (item == null) return FALLBACK_MODEL;
            String id = SnackItemRegistry.itemIdOf(item);
            SnackItemRegistry.SnackEntry entry = SnackItemRegistry.byItemId(id);
            if (entry == null) return FALLBACK_MODEL;
            return ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, entry.modelType().geoPath);
        }

        @Override
        public ResourceLocation getTextureResource(SnackDisplayBlockEntity animatable) {
            int slot = animatable.getCurrentRenderingSlot();
            if (slot < 0) return FALLBACK_TEXTURE;
            Item item = animatable.getSlotItem(slot);
            if (item == null) return FALLBACK_TEXTURE;
            String id = SnackItemRegistry.itemIdOf(item);
            SnackItemRegistry.SnackEntry entry = SnackItemRegistry.byItemId(id);
            if (entry == null) return FALLBACK_TEXTURE;
            return ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, entry.texturePath());
        }

        @Override
        public ResourceLocation getAnimationResource(SnackDisplayBlockEntity animatable) {
            return NO_ANIMATION;
        }
    }
}
