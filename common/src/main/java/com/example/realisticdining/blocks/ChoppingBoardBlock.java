package com.example.realisticdining.blocks;

import com.example.realisticdining.blockentities.ChoppingBoardBlockEntity;
import com.example.realisticdining.compat.KaleidoscopeCompat;
import com.example.realisticdining.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ChoppingBoardBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE = Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0/16.0, 1.0);

    public ChoppingBoardBlock(Properties properties) {
        super(properties);
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
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChoppingBoardBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ChoppingBoardBlockEntity choppingBoardEntity)) {
            return InteractionResult.PASS;
        }

        ItemStack itemInHand = player.getItemInHand(hand);
        int cutStage = choppingBoardEntity.getCutStage();

        if (!choppingBoardEntity.hasMeat() && !choppingBoardEntity.hasNapaCabbage() && !choppingBoardEntity.hasGreenOnion() && !choppingBoardEntity.hasChicken() && !choppingBoardEntity.hasRedPepper() && !choppingBoardEntity.hasGreenChili() && !choppingBoardEntity.hasCoriander() && !choppingBoardEntity.hasYellowSteak() && !choppingBoardEntity.hasTomato()) {
            if (isPorkItem(itemInHand)) {
                ItemStack copyStack = itemInHand.copy();
                copyStack.setCount(1);
                choppingBoardEntity.setMeatItem(copyStack, getPorkStage(copyStack));
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        itemInHand.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            if (isCabbageItem(itemInHand)) {
                ItemStack copyStack = itemInHand.copy();
                copyStack.setCount(1);
                choppingBoardEntity.setNapaCabbageItem(copyStack);
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        itemInHand.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            if (isGreenOnionItem(itemInHand)) {
                ItemStack copyStack = itemInHand.copy();
                copyStack.setCount(1);
                choppingBoardEntity.setGreenOnionItem(copyStack);
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        itemInHand.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            if (isChickenItem(itemInHand)) {
                ItemStack copyStack = itemInHand.copy();
                copyStack.setCount(1);
                choppingBoardEntity.setChickenItem(copyStack);
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        itemInHand.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            if (isRedPepperItem(itemInHand)) {
                ItemStack copyStack = itemInHand.copy();
                copyStack.setCount(1);
                choppingBoardEntity.setRedPepperItem(copyStack);
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        itemInHand.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            if (isGreenChiliItem(itemInHand)) {
                ItemStack copyStack = itemInHand.copy();
                copyStack.setCount(1);
                choppingBoardEntity.setGreenChiliItem(copyStack);
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        itemInHand.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            if (isCorianderItem(itemInHand)) {
                ItemStack copyStack = itemInHand.copy();
                copyStack.setCount(1);
                choppingBoardEntity.setCorianderItem(copyStack);
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        itemInHand.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            if (isBeefItem(itemInHand)) {
                ItemStack copyStack = new ItemStack(ModItems.YELLOW_STEAK.get());
                copyStack.setCount(1);
                choppingBoardEntity.setYellowSteakItem(copyStack);
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        itemInHand.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            if (isYellowSteakItem(itemInHand)) {
                ItemStack copyStack = itemInHand.copy();
                copyStack.setCount(1);
                choppingBoardEntity.setYellowSteakItem(copyStack);
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        itemInHand.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            if (isTomatoItem(itemInHand)) {
                ItemStack copyStack = itemInHand.copy();
                copyStack.setCount(1);
                choppingBoardEntity.setTomatoItem(copyStack);
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        itemInHand.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        if (isKnifeTool(itemInHand)) {
            if (choppingBoardEntity.hasMeat()) {
                if (cutStage < 25) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        itemInHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasNapaCabbage()) {
                if (cutStage < 4) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        itemInHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasGreenOnion()) {
                if (cutStage < 12) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        itemInHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasChicken()) {
                if (cutStage < 20) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        itemInHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasRedPepper()) {
                if (cutStage < 8) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        itemInHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasGreenChili()) {
                if (cutStage < 8) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        itemInHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasCoriander()) {
                if (cutStage < 7) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        itemInHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasYellowSteak()) {
                if (cutStage < 9) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        itemInHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasTomato()) {
                if (cutStage < 4) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        itemInHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        if (itemInHand.isEmpty()) {
            if (choppingBoardEntity.hasMeat() && cutStage >= 25) {
                if (!level.isClientSide) {
                    ItemStack result = new ItemStack(ModItems.CHOPPED_PORK.get(), 1);
                    choppingBoardEntity.clearBoard();
                    if (!player.getInventory().add(result)) {
                        player.drop(result, false);
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                } else {
                    choppingBoardEntity.clearBoard();
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasNapaCabbage() && cutStage == 4) {
                if (!level.isClientSide) {
                    ItemStack result = new ItemStack(ModItems.CHOPPED_GREENS.get(), 1);
                    choppingBoardEntity.clearBoard();
                    if (!player.getInventory().add(result)) {
                        player.drop(result, false);
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                } else {
                    choppingBoardEntity.clearBoard();
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasGreenOnion() && cutStage == 12) {
                if (!level.isClientSide) {
                    ItemStack result = new ItemStack(ModItems.CHOPPED_GREEN_ONION.get(), 1);
                    choppingBoardEntity.clearBoard();
                    if (!player.getInventory().add(result)) {
                        player.drop(result, false);
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                } else {
                    choppingBoardEntity.clearBoard();
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasChicken() && cutStage == 20) {
                if (!level.isClientSide) {
                    ItemStack result = new ItemStack(ModItems.CHOPPED_CHICKEN.get(), 1);
                    choppingBoardEntity.clearBoard();
                    if (!player.getInventory().add(result)) {
                        player.drop(result, false);
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                } else {
                    choppingBoardEntity.clearBoard();
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasRedPepper() && cutStage == 8) {
                if (!level.isClientSide) {
                    ItemStack result = new ItemStack(ModItems.CHOPPED_RED_PEPPER.get(), 1);
                    choppingBoardEntity.clearBoard();
                    if (!player.getInventory().add(result)) {
                        player.drop(result, false);
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                } else {
                    choppingBoardEntity.clearBoard();
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasGreenChili() && cutStage == 8) {
                if (!level.isClientSide) {
                    ItemStack result = new ItemStack(ModItems.RESULT_CHILI.get(), 1);
                    choppingBoardEntity.clearBoard();
                    if (!player.getInventory().add(result)) {
                        player.drop(result, false);
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                } else {
                    choppingBoardEntity.clearBoard();
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasCoriander() && cutStage == 7) {
                if (!level.isClientSide) {
                    ItemStack result = new ItemStack(ModItems.RESULT_CORIANDER.get(), 1);
                    choppingBoardEntity.clearBoard();
                    if (!player.getInventory().add(result)) {
                        player.drop(result, false);
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                } else {
                    choppingBoardEntity.clearBoard();
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasYellowSteak() && cutStage == 9) {
                if (!level.isClientSide) {
                    ItemStack result = new ItemStack(ModItems.BEEF_SLICE.get(), 1);
                    choppingBoardEntity.clearBoard();
                    if (!player.getInventory().add(result)) {
                        player.drop(result, false);
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                } else {
                    choppingBoardEntity.clearBoard();
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasTomato() && cutStage == 4) {
                if (!level.isClientSide) {
                    ItemStack result = new ItemStack(ModItems.TOMATO_SLICE.get(), 1);
                    choppingBoardEntity.clearBoard();
                    if (!player.getInventory().add(result)) {
                        player.drop(result, false);
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                } else {
                    choppingBoardEntity.clearBoard();
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }

    private void dropResult(Level level, BlockPos pos, ItemStack stack) {
        ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, stack);
        level.addFreshEntity(itemEntity);
    }

    private boolean isPorkItem(ItemStack stack) {
        return stack.is(Items.PORKCHOP) || stack.is(ModItems.PORK_STAGE_0.get()) ||
                stack.is(ModItems.PORK_STAGE_1.get()) || stack.is(ModItems.PORK_STAGE_2.get()) ||
                stack.is(ModItems.PORK_STAGE_3.get()) || stack.is(ModItems.PORK_STAGE_4.get()) ||
                stack.is(ModItems.PORK_STAGE_5.get()) || stack.is(ModItems.PORK_STAGE_6.get()) ||
                stack.is(ModItems.PORK_STAGE_7.get());
    }

    private boolean isCabbageItem(ItemStack stack) {
        return stack.is(ModItems.NAPA_CABBAGE.get()) || KaleidoscopeCompat.isLettuce(stack);
    }

    private boolean isGreenOnionItem(ItemStack stack) {
        return stack.is(ModItems.GREEN_ONION.get());
    }

    private boolean isChickenItem(ItemStack stack) {
        return stack.is(Items.CHICKEN);
    }

    private boolean isRedPepperItem(ItemStack stack) {
        return KaleidoscopeCompat.isRedChili(stack);
    }

    private boolean isGreenChiliItem(ItemStack stack) {
        return KaleidoscopeCompat.isGreenChili(stack);
    }

    private boolean isCorianderItem(ItemStack stack) {
        return stack.is(ModItems.CORIANDER.get());
    }

    private boolean isBeefItem(ItemStack stack) {
        return stack.is(Items.BEEF);
    }

    private boolean isYellowSteakItem(ItemStack stack) {
        return stack.is(ModItems.YELLOW_STEAK.get());
    }

    private boolean isTomatoItem(ItemStack stack) {
        return KaleidoscopeCompat.isTomato(stack);
    }

    private int getPorkStage(ItemStack stack) {
        if (stack.is(Items.PORKCHOP)) return 0;
        if (stack.is(ModItems.PORK_STAGE_0.get())) return 0;
        if (stack.is(ModItems.PORK_STAGE_1.get())) return 1;
        if (stack.is(ModItems.PORK_STAGE_2.get())) return 2;
        if (stack.is(ModItems.PORK_STAGE_3.get())) return 3;
        if (stack.is(ModItems.PORK_STAGE_4.get())) return 4;
        if (stack.is(ModItems.PORK_STAGE_5.get())) return 5;
        if (stack.is(ModItems.PORK_STAGE_6.get())) return 6;
        if (stack.is(ModItems.PORK_STAGE_7.get())) return 7;
        return 0;
    }

    private boolean isKnifeTool(ItemStack stack) {
        if (stack.isEmpty()) return false;
        
        if (stack.getItem() instanceof SwordItem) return true;
        if (stack.getItem() instanceof AxeItem) return true;
        
        String itemId = stack.getItem().toString().toLowerCase();
        return itemId.contains("knife");
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null;
    }
}
