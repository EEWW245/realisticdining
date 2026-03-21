package com.example.realisticdining.blockentities;

import com.example.realisticdining.init.ModItems;
import net.minecraft.core.BlockPos;
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
import org.jetbrains.annotations.NotNull;

public class ChoppingBoardBlockEntity extends BlockEntity {
    private ItemStack heldItem = ItemStack.EMPTY;
    private int cutStage = 0;
    private boolean isCabbage = false;
    private boolean isGreenOnion = false;
    private boolean isChicken = false;
    private boolean isRedPepper = false;

    public ChoppingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(com.example.realisticdining.init.ModBlockEntities.CHOPPING_BOARD.get(), pos, state);
    }

    public boolean hasMeat() {
        return !heldItem.isEmpty() && !isCabbage && !isGreenOnion && !isChicken && !isRedPepper;
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

    public void setMeatItem(ItemStack stack, int stage) {
        this.heldItem = stack.copy();
        this.isCabbage = false;
        this.isGreenOnion = false;
        this.isChicken = false;
        this.isRedPepper = false;
        this.cutStage = stage;
        this.sync();
    }

    public void setNapaCabbageItem(ItemStack stack) {
        this.heldItem = stack.copy();
        this.isCabbage = true;
        this.isGreenOnion = false;
        this.isChicken = false;
        this.isRedPepper = false;
        this.cutStage = 0;
        this.sync();
    }

    public void setGreenOnionItem(ItemStack stack) {
        this.heldItem = stack.copy();
        this.isCabbage = false;
        this.isGreenOnion = true;
        this.isChicken = false;
        this.isRedPepper = false;
        this.cutStage = 0;
        this.sync();
    }

    public void setChickenItem(ItemStack stack) {
        this.heldItem = stack.copy();
        this.isCabbage = false;
        this.isGreenOnion = false;
        this.isChicken = true;
        this.isRedPepper = false;
        this.cutStage = 0;
        this.sync();
    }

    public void setRedPepperItem(ItemStack stack) {
        this.heldItem = stack.copy();
        this.isCabbage = false;
        this.isGreenOnion = false;
        this.isChicken = false;
        this.isRedPepper = true;
        this.cutStage = 0;
        this.sync();
    }

    public void clearBoard() {
        this.heldItem = ItemStack.EMPTY;
        this.isCabbage = false;
        this.isGreenOnion = false;
        this.isChicken = false;
        this.isRedPepper = false;
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
        System.out.println("[菜板实体] 切割进度增加: " + cutStage);
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

    public ItemStack getCabbageStageItem(int cuts) {
        return switch (cuts) {
            case 0 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_0.get());
            case 1 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_1.get());
            case 2 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_2.get());
            case 3 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_3.get());
            case 4 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_4.get());
            default -> ItemStack.EMPTY;
        };
    }

    public ItemStack getGreenOnionStageItem(int cuts) {
        return switch (cuts) {
            case 0 -> new ItemStack(ModItems.GREEN_ONION_STAGE_0.get());
            case 1 -> new ItemStack(ModItems.GREEN_ONION_STAGE_1.get());
            case 2 -> new ItemStack(ModItems.GREEN_ONION_STAGE_2.get());
            case 3 -> new ItemStack(ModItems.GREEN_ONION_STAGE_3.get());
            case 4 -> new ItemStack(ModItems.GREEN_ONION_STAGE_4.get());
            case 5 -> new ItemStack(ModItems.GREEN_ONION_STAGE_5.get());
            case 6 -> new ItemStack(ModItems.GREEN_ONION_STAGE_6.get());
            case 7 -> new ItemStack(ModItems.GREEN_ONION_STAGE_7.get());
            case 8 -> new ItemStack(ModItems.GREEN_ONION_STAGE_8.get());
            case 9 -> new ItemStack(ModItems.GREEN_ONION_STAGE_9.get());
            case 10 -> new ItemStack(ModItems.GREEN_ONION_STAGE_10.get());
            case 11 -> new ItemStack(ModItems.GREEN_ONION_STAGE_11.get());
            case 12 -> new ItemStack(ModItems.GREEN_ONION_STAGE_12.get());
            default -> ItemStack.EMPTY;
        };
    }

    public ItemStack getChickenStageItem(int cuts) {
        return switch (cuts) {
            case 0 -> new ItemStack(ModItems.CHICKEN_STAGE_0.get());
            case 1 -> new ItemStack(ModItems.CHICKEN_STAGE_1.get());
            case 2 -> new ItemStack(ModItems.CHICKEN_STAGE_2.get());
            case 3 -> new ItemStack(ModItems.CHICKEN_STAGE_3.get());
            case 4 -> new ItemStack(ModItems.CHICKEN_STAGE_4.get());
            case 5 -> new ItemStack(ModItems.CHICKEN_STAGE_5.get());
            case 6 -> new ItemStack(ModItems.CHICKEN_STAGE_6.get());
            case 7 -> new ItemStack(ModItems.CHICKEN_STAGE_7.get());
            case 8 -> new ItemStack(ModItems.CHICKEN_STAGE_8.get());
            case 9 -> new ItemStack(ModItems.CHICKEN_STAGE_9.get());
            case 10 -> new ItemStack(ModItems.CHICKEN_STAGE_10.get());
            case 11 -> new ItemStack(ModItems.CHICKEN_STAGE_11.get());
            case 12 -> new ItemStack(ModItems.CHICKEN_STAGE_12.get());
            case 13 -> new ItemStack(ModItems.CHICKEN_STAGE_13.get());
            case 14 -> new ItemStack(ModItems.CHICKEN_STAGE_14.get());
            case 15 -> new ItemStack(ModItems.CHICKEN_STAGE_15.get());
            case 16 -> new ItemStack(ModItems.CHICKEN_STAGE_16.get());
            case 17 -> new ItemStack(ModItems.CHICKEN_STAGE_17.get());
            case 18 -> new ItemStack(ModItems.CHICKEN_STAGE_18.get());
            case 19 -> new ItemStack(ModItems.CHICKEN_STAGE_19.get());
            case 20 -> new ItemStack(ModItems.CHICKEN_STAGE_20.get());
            default -> ItemStack.EMPTY;
        };
    }

    public ItemStack getRedPepperStageItem(int cuts) {
        return switch (cuts) {
            case 0 -> new ItemStack(ModItems.RED_PEPPER_STAGE_0.get());
            case 1 -> new ItemStack(ModItems.RED_PEPPER_STAGE_1.get());
            case 2 -> new ItemStack(ModItems.RED_PEPPER_STAGE_2.get());
            case 3 -> new ItemStack(ModItems.RED_PEPPER_STAGE_3.get());
            case 4 -> new ItemStack(ModItems.RED_PEPPER_STAGE_4.get());
            case 5 -> new ItemStack(ModItems.RED_PEPPER_STAGE_5.get());
            case 6 -> new ItemStack(ModItems.RED_PEPPER_STAGE_6.get());
            case 7 -> new ItemStack(ModItems.RED_PEPPER_STAGE_7.get());
            case 8 -> new ItemStack(ModItems.RED_PEPPER_STAGE_8.get());
            default -> ItemStack.EMPTY;
        };
    }

    private void sync() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (!heldItem.isEmpty()) {
            nbt.put("HeldItem", heldItem.save(new CompoundTag()));
        }
        nbt.putInt("CutStage", cutStage);
        nbt.putBoolean("IsCabbage", isCabbage);
        nbt.putBoolean("IsGreenOnion", isGreenOnion);
        nbt.putBoolean("IsChicken", isChicken);
        nbt.putBoolean("IsRedPepper", isRedPepper);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("HeldItem")) {
            heldItem = ItemStack.of(nbt.getCompound("HeldItem"));
        } else {
            heldItem = ItemStack.EMPTY;
        }
        cutStage = nbt.getInt("CutStage");
        isCabbage = nbt.getBoolean("IsCabbage");
        isGreenOnion = nbt.getBoolean("IsGreenOnion");
        isChicken = nbt.getBoolean("IsChicken");
        isRedPepper = nbt.getBoolean("IsRedPepper");
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(net.minecraft.network.Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            load(tag);
        }
    }
}