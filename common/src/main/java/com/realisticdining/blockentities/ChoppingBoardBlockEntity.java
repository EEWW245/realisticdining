package com.realisticdining.blockentities;

import com.realisticdining.registry.ModBlockEntities;
import com.realisticdining.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ChoppingBoardBlockEntity extends BlockEntity {
    private ItemStack heldItem = ItemStack.EMPTY;
    private int cutStage = 0;
    private boolean isCabbage = false;
    private boolean isGreenOnion = false;
    private boolean isChicken = false;
    private boolean isRedPepper = false;
    private boolean isGreenChili = false;
    private boolean isCoriander = false;
    private boolean isYellowSteak = false;
    private boolean isTomato = false;

    public ChoppingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHOPPING_BOARD.get(), pos, state);
    }

    public boolean hasMeat() {
        return !heldItem.isEmpty() && !isCabbage && !isGreenOnion && !isChicken && !isRedPepper && !isGreenChili && !isCoriander && !isYellowSteak && !isTomato;
    }

    public boolean hasNapaCabbage() {
        return !heldItem.isEmpty() && isCabbage;
    }

    public boolean hasGreenOnion() {
        return !heldItem.isEmpty() && isGreenOnion;
    }

    public boolean hasChicken() {
        return !heldItem.isEmpty() && isChicken;
    }

    public boolean hasRedPepper() {
        return !heldItem.isEmpty() && isRedPepper;
    }

    public boolean hasGreenChili() {
        return !heldItem.isEmpty() && isGreenChili;
    }

    public boolean hasCoriander() {
        return !heldItem.isEmpty() && isCoriander;
    }

    public boolean hasYellowSteak() {
        return !heldItem.isEmpty() && isYellowSteak;
    }

    public boolean hasTomato() {
        return !heldItem.isEmpty() && isTomato;
    }

    public void setMeatItem(ItemStack stack, int stage) {
        this.heldItem = stack.copy();
        this.isCabbage = false;
        this.isGreenOnion = false;
        this.isChicken = false;
        this.isRedPepper = false;
        this.isGreenChili = false;
        this.isCoriander = false;
        this.isYellowSteak = false;
        this.isTomato = false;
        this.cutStage = stage;
        this.sync();
    }

    public void setNapaCabbageItem(ItemStack stack) {
        this.heldItem = stack.copy();
        this.isCabbage = true;
        this.isGreenOnion = false;
        this.isChicken = false;
        this.isRedPepper = false;
        this.isGreenChili = false;
        this.isCoriander = false;
        this.isYellowSteak = false;
        this.isTomato = false;
        this.cutStage = 0;
        this.sync();
    }

    public void setGreenOnionItem(ItemStack stack) {
        this.heldItem = stack.copy();
        this.isCabbage = false;
        this.isGreenOnion = true;
        this.isChicken = false;
        this.isRedPepper = false;
        this.isGreenChili = false;
        this.isCoriander = false;
        this.isYellowSteak = false;
        this.isTomato = false;
        this.cutStage = 0;
        this.sync();
    }

    public void setChickenItem(ItemStack stack) {
        this.heldItem = stack.copy();
        this.isCabbage = false;
        this.isGreenOnion = false;
        this.isChicken = true;
        this.isRedPepper = false;
        this.isGreenChili = false;
        this.isCoriander = false;
        this.isYellowSteak = false;
        this.isTomato = false;
        this.cutStage = 0;
        this.sync();
    }

    public void setRedPepperItem(ItemStack stack) {
        this.heldItem = stack.copy();
        this.isCabbage = false;
        this.isGreenOnion = false;
        this.isChicken = false;
        this.isRedPepper = true;
        this.isGreenChili = false;
        this.isCoriander = false;
        this.isYellowSteak = false;
        this.isTomato = false;
        this.cutStage = 0;
        this.sync();
    }

    public void setGreenChiliItem(ItemStack stack) {
        this.heldItem = stack.copy();
        this.isCabbage = false;
        this.isGreenOnion = false;
        this.isChicken = false;
        this.isRedPepper = false;
        this.isGreenChili = true;
        this.isCoriander = false;
        this.isYellowSteak = false;
        this.isTomato = false;
        this.cutStage = 0;
        this.sync();
    }

    public void setCorianderItem(ItemStack stack) {
        this.heldItem = stack.copy();
        this.isCabbage = false;
        this.isGreenOnion = false;
        this.isChicken = false;
        this.isRedPepper = false;
        this.isGreenChili = false;
        this.isCoriander = true;
        this.isYellowSteak = false;
        this.isTomato = false;
        this.cutStage = 0;
        this.sync();
    }

    public void setYellowSteakItem(ItemStack stack) {
        this.heldItem = stack.copy();
        this.isCabbage = false;
        this.isGreenOnion = false;
        this.isChicken = false;
        this.isRedPepper = false;
        this.isGreenChili = false;
        this.isCoriander = false;
        this.isYellowSteak = true;
        this.isTomato = false;
        this.cutStage = 0;
        this.sync();
    }

    public void setTomatoItem(ItemStack stack) {
        this.heldItem = stack.copy();
        this.isCabbage = false;
        this.isGreenOnion = false;
        this.isChicken = false;
        this.isRedPepper = false;
        this.isGreenChili = false;
        this.isCoriander = false;
        this.isYellowSteak = false;
        this.isTomato = true;
        this.cutStage = 0;
        this.sync();
    }

    public void clearBoard() {
        this.heldItem = ItemStack.EMPTY;
        this.isCabbage = false;
        this.isGreenOnion = false;
        this.isChicken = false;
        this.isRedPepper = false;
        this.isCoriander = false;
        this.isYellowSteak = false;
        this.isTomato = false;
        this.cutStage = 0;
        this.sync();
    }

    public void clearMeat() {
        this.clearBoard();
    }

    public ItemStack getMeatItem() {
        return heldItem;
    }

    public int getCutStage() {
        return cutStage;
    }

    public void incrementCutStage() {
        this.cutStage++;
        this.sync();
    }

    public void playParticlesSound() {
        if (this.level instanceof ServerLevel serverLevel) {
            RandomSource random = serverLevel.getRandom();
            serverLevel.sendParticles(ParticleTypes.CRIT,
                    this.worldPosition.getX() + 0.25 + random.nextDouble() / 2,
                    this.worldPosition.getY() + 0.25,
                    this.worldPosition.getZ() + 0.25 + random.nextDouble() / 2,
                    2, 0, 0, 0, 0.1);
            serverLevel.playSound(null, this.worldPosition,
                    SoundEvents.WOOD_PLACE,
                    SoundSource.BLOCKS,
                    1, 1.5f + level.random.nextFloat() * 0.4f);
        }
    }

    private void sync() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!heldItem.isEmpty()) {
            tag.put("HeldItem", heldItem.save(registries));
        }
        tag.putInt("CutStage", cutStage);
        tag.putBoolean("IsCabbage", isCabbage);
        tag.putBoolean("IsGreenOnion", isGreenOnion);
        tag.putBoolean("IsChicken", isChicken);
        tag.putBoolean("IsRedPepper", isRedPepper);
        tag.putBoolean("IsGreenChili", isGreenChili);
        tag.putBoolean("IsCoriander", isCoriander);
        tag.putBoolean("IsYellowSteak", isYellowSteak);
        tag.putBoolean("IsTomato", isTomato);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("HeldItem")) {
            heldItem = ItemStack.parseOptional(registries, tag.getCompound("HeldItem"));
        } else {
            heldItem = ItemStack.EMPTY;
        }
        cutStage = tag.getInt("CutStage");
        isCabbage = tag.getBoolean("IsCabbage");
        isGreenOnion = tag.getBoolean("IsGreenOnion");
        isChicken = tag.getBoolean("IsChicken");
        isRedPepper = tag.getBoolean("IsRedPepper");
        isGreenChili = tag.getBoolean("IsGreenChili");
        isCoriander = tag.getBoolean("IsCoriander");
        isYellowSteak = tag.getBoolean("IsYellowSteak");
        isTomato = tag.getBoolean("IsTomato");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public ItemStack getTomatoStageItem(int cuts) {
        return switch (cuts) {
            case 0 -> new ItemStack(ModItems.TOMATO_STAGE_0.get());
            case 1 -> new ItemStack(ModItems.TOMATO_STAGE_1.get());
            case 2 -> new ItemStack(ModItems.TOMATO_STAGE_2.get());
            case 3 -> new ItemStack(ModItems.TOMATO_STAGE_3.get());
            case 4 -> new ItemStack(ModItems.TOMATO_STAGE_4.get());
            default -> ItemStack.EMPTY;
        };
    }
}
