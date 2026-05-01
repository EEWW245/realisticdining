package com.realisticdining.blocks;

import com.realisticdining.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CorianderCropBlock extends CropBlock {

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 1);
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(6.0D, 0.0D, 6.0D, 10.0D, 8.0D, 10.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D)
    };

    public CorianderCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 1;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.CORIANDER_SEEDS.get();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, net.minecraft.world.level.block.entity.BlockEntity blockEntity, ItemStack tool) {
        if (!level.isClientSide) {
            if (this.isMaxAge(state)) {
                popResource(level, pos, new ItemStack(ModItems.CORIANDER.get()));
            }
            popResource(level, pos, new ItemStack(ModItems.CORIANDER_SEEDS.get()));
        }
    }

    private int getFortuneLevel(ItemStack stack, Level level) {
        if (stack.isEmpty()) return 0;
        var registry = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        var fortuneHolder = registry.getHolder(ResourceLocation.withDefaultNamespace("fortune"));
        if (fortuneHolder.isEmpty()) return 0;
        return EnchantmentHelper.getItemEnchantmentLevel((Holder<Enchantment>) fortuneHolder.get(), stack);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (this.isMaxAge(state)) {
            if (!level.isClientSide) {
                popResource(level, pos, new ItemStack(ModItems.CORIANDER.get()));
                popResource(level, pos, new ItemStack(ModItems.CORIANDER_SEEDS.get()));
                level.setBlock(pos, this.getStateForAge(0), 2);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}
