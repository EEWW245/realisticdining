package com.realisticdining.blocks;

import com.realisticdining.blockentities.WokBlockEntity;
import com.realisticdining.blockentities.WokFriedEggBlockEntity;
import com.realisticdining.blockentities.WokYellowSteakBlockEntity;
import com.realisticdining.compat.KaleidoscopeCookeryCompat;
import com.realisticdining.platform.PlatformHelper;
import com.realisticdining.registry.ModBlocks;
import com.realisticdining.registry.ModItems;
import com.realisticdining.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
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

public class WokBlock extends Block implements EntityBlock {
    public static final BooleanProperty HAS_OIL = BooleanProperty.create("has_oil");
    public static final BooleanProperty HAS_SPATULA = BooleanProperty.create("has_spatula");
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

    private static final VoxelShape SHAPE = Shapes.or(
        Shapes.box(2.0/16.0, 0.0, 2.0/16.0, 14.0/16.0, 1.0/16.0, 14.0/16.0),
        Shapes.box(2.0/16.0, 1.0/16.0, 2.0/16.0, 14.0/16.0, 3.0/16.0, 3.0/16.0),
        Shapes.box(2.0/16.0, 1.0/16.0, 13.0/16.0, 14.0/16.0, 3.0/16.0, 14.0/16.0),
        Shapes.box(2.0/16.0, 1.0/16.0, 3.0/16.0, 3.0/16.0, 3.0/16.0, 13.0/16.0),
        Shapes.box(13.0/16.0, 1.0/16.0, 3.0/16.0, 14.0/16.0, 3.0/16.0, 13.0/16.0)
    );

    public WokBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HAS_OIL, false)
                .setValue(HAS_SPATULA, false)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_OIL, HAS_SPATULA, FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (PlatformHelper.getPlatform() == PlatformHelper.Platform.NEOFORGE) {
            return Shapes.block();
        }
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection();
        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WokBlockEntity(pos, state);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (!level.isClientSide) {
            popResource(level, pos, new ItemStack(ModItems.WOK.get()));
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (state.getValue(HAS_SPATULA)) {
            level.setBlock(pos, state.setValue(HAS_SPATULA, false), 3);
            ItemStack spatula = new ItemStack(ModItems.SPATULA.get());
            if (!player.getInventory().add(spatula)) {
                player.drop(spatula, false);
            }
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof WokBlockEntity wokEntity)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        long gameTime = level.getGameTime();

        if (!state.getValue(HAS_SPATULA) && !state.getValue(HAS_OIL) && stack.is(ModItems.SPATULA.get())) {
            if (!player.isCreative()) stack.shrink(1);
            level.setBlock(pos, state.setValue(HAS_SPATULA, true), 3);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        }

        if (!wokEntity.hasOil()) {
            if (!wokEntity.hasHeatSource()) {
                player.displayClientMessage(Component.literal("需要放在点燃的炉灶上！"), true);
                return ItemInteractionResult.CONSUME;
            }

            boolean addedOil = false;

            if (stack.is(ModItems.VEGETABLE_OIL.get())) {
                if (!player.isCreative()) stack.shrink(1);
                addedOil = true;
            }
            else if (KaleidoscopeCookeryCompat.isOilPot(stack) && KaleidoscopeCookeryCompat.hasOil(stack)) {
                if (!player.isCreative()) {
                    KaleidoscopeCookeryCompat.shrinkOilCount(stack);
                }
                addedOil = true;
            }

            if (addedOil) {
                wokEntity.setHasOilWithTime(true, gameTime);
                level.setBlock(pos, state.setValue(HAS_OIL, true), 3);
                level.playSound(null, pos, ModSounds.POUR_OIL.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                return ItemInteractionResult.SUCCESS;
            }
        }

        if (wokEntity.hasOil() && !wokEntity.hasPork() && !wokEntity.hasChicken() &&
                (stack.is(ModItems.PORK_PIECES.get()) ||
                 stack.is(ModItems.CHOPPED_PORK.get()))) {

            if (!player.isCreative()) stack.shrink(1);

            wokEntity.setHasPork(true);
            level.playSound(null, pos, ModSounds.MEAT_IN_THE_POT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        }

        if (wokEntity.hasOil() && !wokEntity.hasPork() && !wokEntity.hasChicken() && !wokEntity.hasSteak() &&
                stack.is(Items.EGG)) {
            if (!player.isCreative()) stack.shrink(1);

            Direction facing = state.getValue(FACING);
            BlockState newState = ModBlocks.WOK_FRIED_EGG.get().defaultBlockState()
                    .setValue(WokFriedEggBlock.FACING, facing)
                    .setValue(WokFriedEggBlock.HAS_OIL, true);

            level.setBlock(pos, newState, 3);

            if (level.getBlockEntity(pos) instanceof WokFriedEggBlockEntity newEntity) {
                newEntity.setChanged();
                level.sendBlockUpdated(pos, newState, newState, 3);
            }

            level.playSound(null, pos, ModSounds.MEAT_IN_THE_POT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        }

        if (wokEntity.hasOil() && !wokEntity.hasPork() && !wokEntity.hasChicken() && !wokEntity.hasSteak() &&
                stack.is(ModItems.BEEF_SLICE.get())) {
            if (!player.isCreative()) stack.shrink(1);

            Direction facing = state.getValue(FACING);
            BlockState newState = ModBlocks.WOK_YELLOW_STEAK.get().defaultBlockState()
                    .setValue(WokYellowSteakBlock.FACING, facing)
                    .setValue(WokYellowSteakBlock.HAS_OIL, true);

            level.setBlock(pos, newState, 3);

            if (level.getBlockEntity(pos) instanceof WokYellowSteakBlockEntity newEntity) {
                newEntity.setHasOilWithTime(true, gameTime);
                newEntity.setHasSteak(true);
                newEntity.setChanged();
                level.sendBlockUpdated(pos, newState, newState, 3);
            }

            level.playSound(null, pos, ModSounds.MEAT_IN_THE_POT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        }

        if (stack.is(ModItems.BEEF_SLICE.get()) && !wokEntity.hasOil()) {
            player.displayClientMessage(Component.literal("请先倒油！"), true);
            return ItemInteractionResult.CONSUME;
        }

        if (wokEntity.hasOil() && !wokEntity.hasPork() && !wokEntity.hasChicken() &&
                stack.is(ModItems.CHOPPED_CHICKEN.get())) {

            if (!player.isCreative()) stack.shrink(1);

            wokEntity.setHasChicken(true);
            level.playSound(null, pos, ModSounds.MEAT_IN_THE_POT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        }

        if (wokEntity.hasOil() && wokEntity.hasChicken() && !wokEntity.hasRedPepper() &&
                (stack.is(ModItems.CHOPPED_RED_PEPPER.get()) || KaleidoscopeCookeryCompat.isRedChili(stack))) {

            if (!player.isCreative()) stack.shrink(1);

            wokEntity.setHasRedPepper(true);
            level.playSound(null, pos, ModSounds.MEAT_IN_THE_POT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        }

        if (wokEntity.hasOil() && wokEntity.hasChicken() && wokEntity.hasRedPepper() && !wokEntity.hasGreenOnion() &&
                stack.is(ModItems.CHOPPED_GREEN_ONION.get())) {

            if (!player.isCreative()) stack.shrink(1);

            wokEntity.setHasGreenOnion(true);
            level.playSound(null, pos, ModSounds.MEAT_IN_THE_POT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);

            if (wokEntity.getCookingStartTime() == 0) {
                wokEntity.startCookingTimer(gameTime);
            }
            return ItemInteractionResult.SUCCESS;
        }

        if (wokEntity.hasOil() && wokEntity.hasPork() && !wokEntity.hasCabbage()) {
            if (stack.is(ModItems.CHOPPED_GREENS.get()) || KaleidoscopeCookeryCompat.isLettuce(stack)) {
                if (!player.isCreative()) stack.shrink(1);
                wokEntity.setHasCabbage(true);
                wokEntity.startCookingTimer(gameTime);
                level.playSound(null, pos, ModSounds.GREEN_VEGETABLES_IN_THE_POT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                return ItemInteractionResult.SUCCESS;
            }
        }

        if (wokEntity.isReadyToStir() && stack.is(ModItems.SPATULA.get())) {
            wokEntity.performStir(gameTime);
            float randomPitch = 0.2F + level.random.nextFloat() * 1.6F;
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, randomPitch);

            int currentStir = wokEntity.getStirCount();
            if (currentStir == 30) {
                player.displayClientMessage(Component.literal("已翻炒30次"), true);
            }
            return ItemInteractionResult.SUCCESS;
        }

        if (wokEntity.isReadyToStir() && stack.is(Items.BOWL)) {
            if (wokEntity.isBurned(gameTime)) {
                ItemStack charcoal = wokEntity.burnAndClear();
                level.setBlock(pos, state.setValue(HAS_OIL, false), 3);

                ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, charcoal);
                level.addFreshEntity(itemEntity);

                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 0.5F);
                return ItemInteractionResult.SUCCESS;
            }

            if (!wokEntity.canServe(gameTime)) {
                int remainingTime = wokEntity.getRemainingCookSeconds(gameTime);
                if (remainingTime > 0) {
                    player.displayClientMessage(Component.literal("还没到40秒！剩" + remainingTime + "秒"), true);
                }
                return ItemInteractionResult.CONSUME;
            }

            ItemStack dish;
            boolean isSpicyChicken = wokEntity.hasChicken() && wokEntity.hasRedPepper() && wokEntity.hasGreenOnion();
            boolean isPorkCabbage = wokEntity.hasPork() && wokEntity.hasCabbage();

            if (isSpicyChicken) {
                dish = new ItemStack(ModItems.PEPPERY_CHICKEN.get());
            } else if (isPorkCabbage) {
                dish = new ItemStack(ModItems.STIR_FRIED_PORK_CABBAGE.get());
            } else {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }

            if (!player.isCreative()) {
                stack.shrink(1);
                if (stack.isEmpty()) {
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
            level.setBlock(pos, state.setValue(HAS_OIL, false), 3);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof WokBlockEntity wokEntity) {
                long currentTime = lvl.getGameTime();

                if (wokEntity.shouldPlaySauteSound(currentTime)) {
                    lvl.playSound(null, pos, ModSounds.SAUTE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    wokEntity.setLastSauteSoundTime(currentTime);
                }

                if (wokEntity.isReadyToStir() && wokEntity.isBurned(currentTime)) {
                    ItemStack charcoal = wokEntity.burnAndClear();
                    lvl.setBlock(pos, st.setValue(HAS_OIL, false), 3);

                    ItemEntity itemEntity = new ItemEntity(lvl, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, charcoal);
                    lvl.addFreshEntity(itemEntity);

                    lvl.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 0.5F);
                }
            }
        };
    }
}
