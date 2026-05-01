package com.example.realisticdining.blocks;

import com.example.realisticdining.blockentities.WokBlockEntity;
import com.example.realisticdining.blockentities.WokYellowSteakBlockEntity;
import com.example.realisticdining.compat.KaleidoscopeCompat;
import com.example.realisticdining.init.ModBlockEntities;
import com.example.realisticdining.init.ModBlocks;
import com.example.realisticdining.init.ModItems;
import com.example.realisticdining.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WokYellowSteakBlock extends BaseEntityBlock {
    public static final BooleanProperty HAS_OIL = BooleanProperty.create("has_oil");
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    
    private static final VoxelShape SHAPE = Shapes.or(
        Shapes.box(2.0/16.0, 0.0, 2.0/16.0, 14.0/16.0, 1.0/16.0, 14.0/16.0),
        Shapes.box(2.0/16.0, 1.0/16.0, 2.0/16.0, 14.0/16.0, 3.0/16.0, 3.0/16.0),
        Shapes.box(2.0/16.0, 1.0/16.0, 13.0/16.0, 14.0/16.0, 3.0/16.0, 14.0/16.0),
        Shapes.box(2.0/16.0, 1.0/16.0, 3.0/16.0, 3.0/16.0, 3.0/16.0, 13.0/16.0),
        Shapes.box(13.0/16.0, 1.0/16.0, 3.0/16.0, 14.0/16.0, 3.0/16.0, 13.0/16.0)
    );

    public WokYellowSteakBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HAS_OIL, false)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_OIL, FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection();
        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                                          @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof WokYellowSteakBlockEntity wokEntity)) {
            return InteractionResult.PASS;
        }

        ItemStack heldItem = player.getItemInHand(hand);
        long gameTime = level.getGameTime();

        if (!wokEntity.hasOil()) {
            if (!wokEntity.hasHeatSource()) {
                player.displayClientMessage(Component.literal("需要放在点燃的炉灶上！"), true);
                return InteractionResult.CONSUME;
            }

            boolean addedOil = false;
            if (heldItem.is(ModItems.VEGETABLE_OIL.get())) {
                if (!player.isCreative()) heldItem.shrink(1);
                addedOil = true;
            } else if (KaleidoscopeCompat.isOilPot(heldItem) && KaleidoscopeCompat.hasOil(heldItem)) {
                if (!player.isCreative()) {
                    KaleidoscopeCompat.shrinkOilCount(heldItem);
                }
                addedOil = true;
            }

            if (addedOil) {
                wokEntity.setHasOilWithTime(true, gameTime);
                level.setBlock(pos, state.setValue(HAS_OIL, true), 3);
                level.playSound(null, pos, ModSounds.POUR_OIL.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.CONSUME;
            }
        }

        if (wokEntity.hasOil() && !wokEntity.hasSteak() && heldItem.is(ModItems.BEEF_SLICE.get())) {
            if (!player.isCreative()) heldItem.shrink(1);
            wokEntity.setHasSteak(true);
            level.playSound(null, pos, ModSounds.MEAT_IN_THE_POT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.CONSUME;
        }

        if (wokEntity.hasOil() && wokEntity.hasSteak() && !wokEntity.hasGreenChili()) {
            if (heldItem.is(ModItems.RESULT_CHILI.get())) {
                if (!player.isCreative()) heldItem.shrink(1);
                wokEntity.setHasGreenChili(true);
                level.playSound(null, pos, ModSounds.GREEN_VEGETABLES_IN_THE_POT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.CONSUME;
            }
        }

        if (wokEntity.hasOil() && wokEntity.hasSteak() && wokEntity.hasGreenChili() && !wokEntity.hasRedPepper()) {
            if (heldItem.is(ModItems.CHOPPED_RED_PEPPER.get())) {
                if (!player.isCreative()) heldItem.shrink(1);
                wokEntity.setHasRedPepper(true);
                level.playSound(null, pos, ModSounds.MEAT_IN_THE_POT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.CONSUME;
            }
        }

        if (wokEntity.hasOil() && wokEntity.hasSteak() && wokEntity.hasGreenChili() && wokEntity.hasRedPepper() && !wokEntity.hasCoriander()) {
            if (heldItem.is(ModItems.RESULT_CORIANDER.get())) {
                if (!player.isCreative()) heldItem.shrink(1);
                wokEntity.setHasCoriander(true);
                wokEntity.startCookingTimer(gameTime);
                level.playSound(null, pos, ModSounds.GREEN_VEGETABLES_IN_THE_POT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.CONSUME;
            }
        }

        if (wokEntity.isReadyToStir() && heldItem.is(ModItems.SPATULA.get())) {
            if (wokEntity.isCurrentlyStirring()) {
                return InteractionResult.CONSUME;
            }
            
            wokEntity.performStir(gameTime);
            wokEntity.startStirAnimation();
            float randomPitch = 0.2F + level.random.nextFloat() * 1.6F;
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, randomPitch);
            int currentStir = wokEntity.getStirCount();
            if (currentStir == 30) {
                player.displayClientMessage(Component.literal("已翻炒30次"), true);
            }
            return InteractionResult.CONSUME;
        }

        if (wokEntity.isReadyToStir() && heldItem.is(Items.BOWL)) {
            if (wokEntity.isBurned(gameTime)) {
                ItemStack charcoal = wokEntity.burnAndClear();
                level.setBlockAndUpdate(pos, ModBlocks.WOK_BLOCK.get().defaultBlockState().setValue(WokBlock.FACING, state.getValue(FACING)));
                ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, charcoal);
                level.addFreshEntity(itemEntity);
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 0.5F);
                return InteractionResult.SUCCESS;
            }
            if (!wokEntity.canServe(gameTime)) {
                int remainingTime = wokEntity.getRemainingCookSeconds(gameTime);
                if (remainingTime > 0) {
                    player.displayClientMessage(Component.literal("还没到40秒！剩" + remainingTime + "秒"), true);
                }
                return InteractionResult.CONSUME;
            }
            ItemStack dish = new ItemStack(ModItems.STIR_FRIED_YELLOW_BEEF.get());
            if (!player.isCreative()) {
                heldItem.shrink(1);
                if (heldItem.isEmpty()) {
                    player.setItemInHand(hand, dish);
                } else {
                    if (!player.getInventory().add(dish)) {
                        player.drop(dish, false);
                    }
                }
            } else {
                if (!player.getInventory().add(dish)) {
                    player.drop(dish, false);
                }
            }
            wokEntity.clearWok();
            level.setBlockAndUpdate(pos, ModBlocks.WOK_BLOCK.get().defaultBlockState().setValue(WokBlock.FACING, state.getValue(FACING)));
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof WokYellowSteakBlockEntity wokEntity) {
                long currentTime = lvl.getGameTime();
                if (wokEntity.shouldPlaySauteSound(currentTime)) {
                    lvl.playSound(null, pos, ModSounds.SAUTE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    wokEntity.setLastSauteSoundTime(currentTime);
                }
                if (wokEntity.isReadyToStir() && wokEntity.isBurned(currentTime)) {
                    ItemStack charcoal = wokEntity.burnAndClear();
                    lvl.setBlockAndUpdate(pos, ModBlocks.WOK_BLOCK.get().defaultBlockState().setValue(WokBlock.FACING, st.getValue(FACING)));
                    ItemEntity itemEntity = new ItemEntity(lvl, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, charcoal);
                    lvl.addFreshEntity(itemEntity);
                    lvl.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 0.5F);
                }
            }
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new WokYellowSteakBlockEntity(pos, state);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }
}
