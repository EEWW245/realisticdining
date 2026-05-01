package com.realisticdining.blocks;

import com.realisticdining.blockentities.ChoppingBoardBlockEntity;
import com.realisticdining.compat.KaleidoscopeCookeryCompat;
import com.realisticdining.registry.ModBlockEntities;
import com.realisticdining.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ChoppingBoardBlock extends Block implements EntityBlock {

    private static final VoxelShape SHAPE = Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0/16.0, 1.0);

    public ChoppingBoardBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChoppingBoardBlockEntity(pos, state);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, net.minecraft.world.level.block.entity.BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        
        if (!level.isClientSide) {
            ItemStack board = new ItemStack(ModItems.CHOPPING_BOARD.get());
            popResource(level, pos, board);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ChoppingBoardBlockEntity choppingBoardEntity)) {
            return InteractionResult.PASS;
        }

        int cutStage = choppingBoardEntity.getCutStage();

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

        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ChoppingBoardBlockEntity choppingBoardEntity)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        int cutStage = choppingBoardEntity.getCutStage();

        if (!choppingBoardEntity.hasMeat() && !choppingBoardEntity.hasNapaCabbage() && !choppingBoardEntity.hasGreenOnion() && !choppingBoardEntity.hasChicken() && !choppingBoardEntity.hasRedPepper() && !choppingBoardEntity.hasGreenChili() && !choppingBoardEntity.hasCoriander() && !choppingBoardEntity.hasYellowSteak() && !choppingBoardEntity.hasTomato()) {
            if (isPorkItem(stack)) {
                ItemStack copyStack = stack.copy();
                copyStack.setCount(1);
                choppingBoardEntity.setMeatItem(copyStack, getPorkStage(copyStack));
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (isCabbageItem(stack)) {
                ItemStack copyStack = stack.copy();
                copyStack.setCount(1);
                choppingBoardEntity.setNapaCabbageItem(copyStack);
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (isGreenOnionItem(stack)) {
                ItemStack copyStack = stack.copy();
                copyStack.setCount(1);
                choppingBoardEntity.setGreenOnionItem(copyStack);
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (isChickenItem(stack)) {
                ItemStack copyStack = stack.copy();
                copyStack.setCount(1);
                choppingBoardEntity.setChickenItem(copyStack);
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (isRedPepperItem(stack)) {
                ItemStack copyStack = stack.copy();
                copyStack.setCount(1);
                choppingBoardEntity.setRedPepperItem(copyStack);
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (isGreenChiliItem(stack)) {
                ItemStack copyStack = stack.copy();
                copyStack.setCount(1);
                choppingBoardEntity.setGreenChiliItem(copyStack);
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (isCorianderItem(stack)) {
                ItemStack copyStack = stack.copy();
                copyStack.setCount(1);
                choppingBoardEntity.setCorianderItem(copyStack);
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (isBeefItem(stack)) {
                ItemStack copyStack = new ItemStack(ModItems.YELLOW_STEAK.get());
                copyStack.setCount(1);
                choppingBoardEntity.setYellowSteakItem(copyStack);
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (isYellowSteakItem(stack)) {
                ItemStack copyStack = stack.copy();
                copyStack.setCount(1);
                choppingBoardEntity.setYellowSteakItem(copyStack);
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (isTomatoItem(stack)) {
                ItemStack copyStack = stack.copy();
                copyStack.setCount(1);
                choppingBoardEntity.setTomatoItem(copyStack);
                
                if (!level.isClientSide) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        if (stack.is(net.minecraft.tags.ItemTags.SWORDS)) {
            if (choppingBoardEntity.hasMeat()) {
                if (cutStage < 25) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasNapaCabbage()) {
                if (cutStage < 4) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasGreenOnion()) {
                if (cutStage < 12) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasChicken()) {
                if (cutStage < 20) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasRedPepper()) {
                if (cutStage < 8) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasGreenChili()) {
                if (cutStage < 8) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasCoriander()) {
                if (cutStage < 7) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasYellowSteak()) {
                if (cutStage < 9) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            if (choppingBoardEntity.hasTomato()) {
                if (cutStage < 4) {
                    choppingBoardEntity.incrementCutStage();
                    if (!level.isClientSide) {
                        choppingBoardEntity.playParticlesSound();
                        stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private boolean isPorkItem(ItemStack stack) {
        return stack.is(Items.PORKCHOP) || stack.is(ModItems.PORK_PIECES.get());
    }

    private boolean isCabbageItem(ItemStack stack) {
        return stack.is(ModItems.NAPA_CABBAGE.get()) || KaleidoscopeCookeryCompat.isLettuce(stack);
    }

    private boolean isGreenOnionItem(ItemStack stack) {
        return stack.is(ModItems.GREEN_ONION.get());
    }

    private boolean isChickenItem(ItemStack stack) {
        return stack.is(Items.CHICKEN);
    }

    private boolean isRedPepperItem(ItemStack stack) {
        return stack.is(ModItems.RED_PEPPER.get()) || KaleidoscopeCookeryCompat.isRedChili(stack);
    }

    private boolean isGreenChiliItem(ItemStack stack) {
        return KaleidoscopeCookeryCompat.isGreenChili(stack);
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
        return KaleidoscopeCookeryCompat.isTomato(stack);
    }

    private int getPorkStage(ItemStack stack) {
        if (stack.is(Items.PORKCHOP)) return 0;
        if (stack.is(ModItems.PORK_PIECES.get())) return 0;
        return 0;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null;
    }
}
