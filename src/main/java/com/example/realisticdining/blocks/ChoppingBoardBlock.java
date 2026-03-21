package com.example.realisticdining.blocks;

import com.example.realisticdining.blockentities.ChoppingBoardBlockEntity;
import com.example.realisticdining.compat.KaleidoscopeCompat;
import com.example.realisticdining.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ChoppingBoardBlock extends BaseEntityBlock {

    public ChoppingBoardBlock(Properties properties) {
        super(properties);
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

        if (!choppingBoardEntity.hasMeat() && !choppingBoardEntity.hasNapaCabbage() && !choppingBoardEntity.hasGreenOnion() && !choppingBoardEntity.hasChicken() && !choppingBoardEntity.hasRedPepper()) {
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
        }

        if (itemInHand.getItem() instanceof SwordItem) {
            if (choppingBoardEntity.hasMeat()) {
                if (cutStage < 25) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        player.displayClientMessage(Component.literal("切割进度: " + (cutStage + 1) + "/25"), true);
                        itemInHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    }
                } else {
                    if (!level.isClientSide) {
                        player.displayClientMessage(Component.literal("已切好! 空手右键取走"), true);
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
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }

            if (choppingBoardEntity.hasGreenOnion()) {
                if (cutStage < 12) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        player.displayClientMessage(Component.literal("切割进度: " + (cutStage + 1) + "/12"), true);
                        itemInHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    }
                } else {
                    if (!level.isClientSide) {
                        player.displayClientMessage(Component.literal("已切好! 空手右键取走"), true);
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasChicken()) {
                if (cutStage < 20) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        player.displayClientMessage(Component.literal("切割进度: " + (cutStage + 1) + "/20"), true);
                        itemInHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    }
                } else {
                    if (!level.isClientSide) {
                        player.displayClientMessage(Component.literal("已切好! 空手右键取走"), true);
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasRedPepper()) {
                if (cutStage < 8) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        player.displayClientMessage(Component.literal("切割进度: " + (cutStage + 1) + "/8"), true);
                        itemInHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    }
                } else {
                    if (!level.isClientSide) {
                        player.displayClientMessage(Component.literal("已切好! 空手右键取走"), true);
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
                    player.displayClientMessage(Component.literal("获得了切好的猪肉!"), true);
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
                    player.displayClientMessage(Component.literal("获得了切好的青菜!"), true);
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
                    player.displayClientMessage(Component.literal("获得了切好的葱花!"), true);
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
                    player.displayClientMessage(Component.literal("获得了切好的鸡肉块!"), true);
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
                    player.displayClientMessage(Component.literal("获得了切好的红辣椒!"), true);
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

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null;
    }
}
