package com.example.realisticdining.blocks;

import com.example.realisticdining.blockentities.WokBlockEntity;
import com.example.realisticdining.blockentities.WokFriedEggBlockEntity;
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

public class WokBlock extends BaseEntityBlock {
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
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                                          @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof WokBlockEntity wokEntity)) {
            return InteractionResult.PASS;
        }

        ItemStack heldItem = player.getItemInHand(hand);
        long gameTime = level.getGameTime();

        if (heldItem.isEmpty() && state.getValue(HAS_SPATULA)) {
            level.setBlock(pos, state.setValue(HAS_SPATULA, false), 3);
            ItemStack spatula = new ItemStack(ModItems.SPATULA.get());
            if (!player.getInventory().add(spatula)) {
                player.drop(spatula, false);
            }
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.CONSUME;
        }

        if (!state.getValue(HAS_SPATULA) && !state.getValue(HAS_OIL) && heldItem.is(ModItems.SPATULA.get())) {
            if (!player.isCreative()) heldItem.shrink(1);
            level.setBlock(pos, state.setValue(HAS_SPATULA, true), 3);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.CONSUME;
        }

        // 1. 倒植物油（支持本模组植物油和主模组油壶）
        // 必须有热源（炉灶点燃）才能倒油
        if (!wokEntity.hasOil()) {
            // 检查是否有热源
            if (!wokEntity.hasHeatSource()) {
                player.displayClientMessage(Component.literal("需要放在点燃的炉灶上！"), true);
                return InteractionResult.CONSUME;
            }
            
            boolean addedOil = false;
            
            // 本模组植物油
            if (heldItem.is(ModItems.VEGETABLE_OIL.get())) {
                if (!player.isCreative()) heldItem.shrink(1);
                addedOil = true;
            }
            // 主模组油壶（需要有油）
            else if (KaleidoscopeCompat.isOilPot(heldItem) && KaleidoscopeCompat.hasOil(heldItem)) {
                if (!player.isCreative()) {
                    int oilCount = KaleidoscopeCompat.getOilCount(heldItem);
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

        if (wokEntity.hasOil() && !wokEntity.hasPork() && !wokEntity.hasChicken() && !wokEntity.hasSteak() &&
                heldItem.is(Items.EGG)) {
            if (!player.isCreative()) heldItem.shrink(1);

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
            return InteractionResult.CONSUME;
        }

        if (wokEntity.hasOil() && !wokEntity.hasPork() && !wokEntity.hasChicken() && !wokEntity.hasSteak() &&
                heldItem.is(ModItems.BEEF_SLICE.get())) {
            if (!player.isCreative()) heldItem.shrink(1);

            Direction facing = state.getValue(FACING);
            BlockState newState = ModBlocks.WOK_YELLOW_STEAK.get().defaultBlockState()
                    .setValue(WokYellowSteakBlock.FACING, facing)
                    .setValue(WokYellowSteakBlock.HAS_OIL, true);

            level.setBlock(pos, newState, 3);

            if (level.getBlockEntity(pos) instanceof WokYellowSteakBlockEntity newEntity) {
                newEntity.setHasOilWithTime(true, gameTime);
                newEntity.setHasSteak(true);
            }

            level.playSound(null, pos, ModSounds.MEAT_IN_THE_POT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.CONSUME;
        }

        if (wokEntity.hasOil() && !wokEntity.hasPork() && !wokEntity.hasChicken() &&
                (heldItem.is(ModItems.PORK_PIECES.get()) || 
                 heldItem.is(ModItems.CHOPPED_PORK.get()))) {

            if (!player.isCreative()) heldItem.shrink(1);

            wokEntity.setHasPork(true);
            level.playSound(null, pos, ModSounds.MEAT_IN_THE_POT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.CONSUME;
        }

        if (wokEntity.hasOil() && !wokEntity.hasPork() && !wokEntity.hasChicken() &&
                heldItem.is(ModItems.CHOPPED_CHICKEN.get())) {

            if (!player.isCreative()) heldItem.shrink(1);

            wokEntity.setHasChicken(true);
            level.playSound(null, pos, ModSounds.MEAT_IN_THE_POT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.CONSUME;
        }

        if (wokEntity.hasOil() && wokEntity.hasChicken() && !wokEntity.hasRedPepper() &&
                heldItem.is(ModItems.CHOPPED_RED_PEPPER.get())) {

            if (!player.isCreative()) heldItem.shrink(1);

            wokEntity.setHasRedPepper(true);
            level.playSound(null, pos, ModSounds.MEAT_IN_THE_POT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.CONSUME;
        }

        if (wokEntity.hasOil() && wokEntity.hasChicken() && wokEntity.hasRedPepper() && !wokEntity.hasGreenOnion() &&
                heldItem.is(ModItems.CHOPPED_GREEN_ONION.get())) {

            if (!player.isCreative()) heldItem.shrink(1);

            wokEntity.setHasGreenOnion(true);
            level.playSound(null, pos, ModSounds.MEAT_IN_THE_POT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            
            if (wokEntity.getCookingStartTime() == 0) {
                wokEntity.startCookingTimer(gameTime);
            }
            return InteractionResult.CONSUME;
        }

        if (wokEntity.hasPork() && !wokEntity.hasCabbage()) {
            boolean addedCabbage = false;
            
            if (heldItem.is(ModItems.CHOPPED_GREENS.get())) {
                if (!player.isCreative()) heldItem.shrink(1);
                addedCabbage = true;
            }
            else if (KaleidoscopeCompat.isLettuce(heldItem)) {
                if (!player.isCreative()) heldItem.shrink(1);
                addedCabbage = true;
            }
            
            if (addedCabbage) {
                wokEntity.setHasCabbage(true);
                wokEntity.startCookingTimer(gameTime);
                level.playSound(null, pos, ModSounds.GREEN_VEGETABLES_IN_THE_POT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.CONSUME;
            }
        }

        if (wokEntity.isReadyToStir() && heldItem.is(ModItems.SPATULA.get())) {
            wokEntity.performStir(gameTime);
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
                level.setBlock(pos, state.setValue(HAS_OIL, false), 3);
                
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

            ItemStack dish;
            boolean isSpicyChicken = wokEntity.hasChicken() && wokEntity.hasRedPepper() && wokEntity.hasGreenOnion();
            if (isSpicyChicken) {
                dish = new ItemStack(ModItems.PEPPERY_CHICKEN.get());
            } else if (wokEntity.hasChicken()) {
                dish = new ItemStack(ModItems.STIR_FRIED_PORK_CABBAGE.get());
            } else {
                dish = new ItemStack(ModItems.STIR_FRIED_PORK_CABBAGE.get());
            }
            
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
            level.setBlock(pos, state.setValue(HAS_OIL, false), 3);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }

        // 兼容逻辑：普通 GUI 配方烹饪
        // 现在不再允许任何食物放入，只允许未来的扩展或者特定需求，暂时注销普通食物的放入
        // 恢复支持农夫乐事等兼容食材的代码，但注销了允许一切可食用物品（isEdible）放进锅里的逻辑
        /*
        if (heldItem.isEdible() && !wokEntity.hasOil() && !wokEntity.hasPork()) {
            if (wokEntity.addIngredient(heldItem.copy())) {
                if (!player.isCreative()) {
                    heldItem.shrink(1);
                }
                player.displayClientMessage(Component.literal("放入了普通食材"), true);
                return InteractionResult.CONSUME;
            } else {
                player.displayClientMessage(Component.literal("锅正在使用中！"), true);
                return InteractionResult.CONSUME;
            }
        } else if (heldItem.isEmpty() && !wokEntity.isReadyToStir()) {
            ItemStack result = wokEntity.takeResult();
            if (!result.isEmpty()) {
                player.setItemInHand(hand, result);
                player.displayClientMessage(Component.literal("取出了食物！"), true);
                return InteractionResult.SUCCESS;
            }
        }
        */

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof WokBlockEntity wokEntity) {
                long currentTime = lvl.getGameTime();

                // 播放油滋啦声（倒油后10秒开始，每10秒循环）
                if (wokEntity.shouldPlaySauteSound(currentTime)) {
                    lvl.playSound(null, pos, ModSounds.SAUTE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    wokEntity.setLastSauteSoundTime(currentTime);
                }

                // 检查是否烤焦
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

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new WokBlockEntity(pos, state);
    }
}
