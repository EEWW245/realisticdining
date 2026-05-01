package com.realisticdining.blockentities;

import com.realisticdining.compat.KaleidoscopeCookeryCompat;
import com.realisticdining.registry.ModBlockEntities;
import com.realisticdining.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class WokBlockEntity extends BlockEntity implements Container {
    private NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private int cookTime = 0;
    private final int cookTimeTotal = 200;
    private int cookStage = 0;

    private boolean hasOil = false;
    private boolean hasPork = false;
    private boolean hasChicken = false;
    private boolean hasRedPepper = false;
    private boolean hasGreenOnion = false;
    private boolean hasCabbage = false;
    private boolean hasSteak = false;
    private boolean hasGreenChili = false;
    private boolean hasCoriander = false;

    private int stirCount = 0;
    private long lastStirTime = 0;
    private int currentStirVariant = 1;

    private long cookingStartTime = 0;
    private static final long COOK_DURATION = 40 * 20;
    private static final long BURN_DURATION = 20 * 20;
    private static final int REQUIRED_STIRS = 30;

    private long oilPourTime = 0;
    private long lastSauteSoundTime = 0;
    private static final long SAUTE_DELAY = 10 * 20;
    private static final long SAUTE_DURATION = 10 * 20;

    public WokBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public WokBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.WOK.get(), pos, state);
    }

    public boolean hasOil() { return hasOil; }
    public void setHasOil(boolean hasOil) {
        this.hasOil = hasOil;
        sync();
    }

    public void setHasOilWithTime(boolean hasOil, long gameTime) {
        this.hasOil = hasOil;
        if (hasOil) {
            this.oilPourTime = gameTime;
            this.lastSauteSoundTime = gameTime;
        }
        sync();
    }

    public long getOilPourTime() { return oilPourTime; }
    public long getLastSauteSoundTime() { return lastSauteSoundTime; }
    public void setLastSauteSoundTime(long time) {
        this.lastSauteSoundTime = time;
    }

    public boolean shouldPlaySauteSound(long currentGameTime) {
        if (!hasOil) return false;
        if (oilPourTime == 0) return false;
        
        long timeSinceOilPour = currentGameTime - oilPourTime;
        long timeSinceLastSound = currentGameTime - lastSauteSoundTime;
        
        return timeSinceOilPour >= SAUTE_DELAY && timeSinceLastSound >= SAUTE_DURATION;
    }

    public boolean hasPork() { return hasPork; }
    public void setHasPork(boolean hasPork) {
        this.hasPork = hasPork;
        sync();
    }

    public boolean hasChicken() { return hasChicken; }
    public void setHasChicken(boolean hasChicken) {
        this.hasChicken = hasChicken;
        sync();
    }

    public boolean hasRedPepper() { return hasRedPepper; }
    public void setHasRedPepper(boolean hasRedPepper) {
        this.hasRedPepper = hasRedPepper;
        sync();
    }

    public boolean hasGreenOnion() { return hasGreenOnion; }
    public void setHasGreenOnion(boolean hasGreenOnion) {
        this.hasGreenOnion = hasGreenOnion;
        sync();
    }

    public boolean hasCabbage() { return hasCabbage; }
    public void setHasCabbage(boolean hasCabbage) {
        this.hasCabbage = hasCabbage;
        if (hasCabbage) {
            this.cookingStartTime = 0;
        }
        sync();
    }

    public boolean hasSteak() { return hasSteak; }
    public void setHasSteak(boolean hasSteak) {
        this.hasSteak = hasSteak;
        sync();
    }

    public boolean hasGreenChili() { return hasGreenChili; }
    public void setHasGreenChili(boolean hasGreenChili) {
        this.hasGreenChili = hasGreenChili;
        sync();
    }

    public boolean hasCoriander() { return hasCoriander; }
    public void setHasCoriander(boolean hasCoriander) {
        this.hasCoriander = hasCoriander;
        sync();
    }

    public int getYellowSteakStage() {
        if (!hasSteak) return -1;
        if (hasCoriander) return 3;
        if (hasGreenChili && hasRedPepper) return 2;
        if (hasGreenChili) return 1;
        return 0;
    }

    public boolean isReadyToStir() {
        boolean isSpicyChicken = hasChicken && hasRedPepper && hasGreenOnion;
        if (isSpicyChicken) {
            return hasOil;
        }
        boolean isYellowSteak = hasSteak && hasCoriander;
        if (isYellowSteak) {
            return true;
        }
        if (hasPork && hasCabbage) {
            return hasOil && !hasRedPepper && !hasGreenOnion;
        }
        return false;
    }

    public int getStirCount() { return stirCount; }
    public long getLastStirTime() { return lastStirTime; }
    public int getCurrentStirVariant() { return currentStirVariant; }
    public long getCookingStartTime() { return cookingStartTime; }

    public void performStir(long gameTime) {
        this.stirCount++;
        this.lastStirTime = gameTime;
        boolean isSpicyChicken = hasChicken && hasRedPepper && hasGreenOnion;
        int maxVariants = isSpicyChicken ? 30 : 8;
        this.currentStirVariant = (this.currentStirVariant % maxVariants) + 1;
        sync();
    }

    public void startCookingTimer(long gameTime) {
        this.cookingStartTime = gameTime;
        sync();
    }

    public boolean canServe(long currentGameTime) {
        if (!isReadyToStir()) return false;
        if (stirCount < REQUIRED_STIRS) return false;
        if (cookingStartTime == 0) return false;
        
        long elapsed = currentGameTime - cookingStartTime;
        return elapsed >= COOK_DURATION;
    }

    public boolean isBurned(long currentGameTime) {
        if (cookingStartTime == 0) return false;
        
        long elapsed = currentGameTime - cookingStartTime;
        return elapsed >= (COOK_DURATION + BURN_DURATION);
    }

    public int getRemainingCookSeconds(long currentGameTime) {
        if (cookingStartTime == 0) return 40;
        long elapsed = currentGameTime - cookingStartTime;
        long remaining = COOK_DURATION - elapsed;
        return remaining > 0 ? (int)(remaining / 20) : 0;
    }

    public int getRemainingBurnSeconds(long currentGameTime) {
        if (cookingStartTime == 0) return 60;
        long elapsed = currentGameTime - cookingStartTime;
        long remaining = (COOK_DURATION + BURN_DURATION) - elapsed;
        return remaining > 0 ? (int)(remaining / 20) : 0;
    }

    public void clearWok() {
        this.hasOil = false;
        this.hasPork = false;
        this.hasChicken = false;
        this.hasRedPepper = false;
        this.hasGreenOnion = false;
        this.hasCabbage = false;
        this.hasSteak = false;
        this.hasGreenChili = false;
        this.hasCoriander = false;
        this.stirCount = 0;
        this.lastStirTime = 0;
        this.currentStirVariant = 1;
        this.cookingStartTime = 0;
        this.oilPourTime = 0;
        this.lastSauteSoundTime = 0;
        sync();
    }

    public ItemStack burnAndClear() {
        clearWok();
        return new ItemStack(Items.CHARCOAL, 1);
    }

    public ItemStack serveDish() {
        ItemStack result = getResultDish();
        clearWok();
        return result;
    }

    private ItemStack getResultDish() {
        if (hasChicken && hasRedPepper && hasGreenOnion) {
            return new ItemStack(ModItems.PEPPERY_CHICKEN.get(), 1);
        }
        if (hasPork && hasCabbage) {
            return new ItemStack(ModItems.STIR_FRIED_PORK_CABBAGE.get(), 1);
        }
        if (hasSteak && hasCoriander) {
            return new ItemStack(ModItems.STIR_FRIED_YELLOW_BEEF.get(), 1);
        }
        return ItemStack.EMPTY;
    }

    private void sync() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    // 客户端同步数据包
    @Override
    public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }

    // 创建同步数据标签
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("HasOil", hasOil);
        tag.putBoolean("HasPork", hasPork);
        tag.putBoolean("HasChicken", hasChicken);
        tag.putBoolean("HasRedPepper", hasRedPepper);
        tag.putBoolean("HasGreenOnion", hasGreenOnion);
        tag.putBoolean("HasCabbage", hasCabbage);
        tag.putBoolean("HasSteak", hasSteak);
        tag.putBoolean("HasGreenChili", hasGreenChili);
        tag.putBoolean("HasCoriander", hasCoriander);
        tag.putInt("StirCount", stirCount);
        tag.putLong("LastStirTime", lastStirTime);
        tag.putInt("CurrentStirVariant", currentStirVariant);
        tag.putLong("CookingStartTime", cookingStartTime);
        return tag;
    }



    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
        tag.putInt("CookTime", cookTime);
        tag.putInt("CookStage", cookStage);

        tag.putBoolean("HasOil", hasOil);
        tag.putBoolean("HasPork", hasPork);
        tag.putBoolean("HasChicken", hasChicken);
        tag.putBoolean("HasRedPepper", hasRedPepper);
        tag.putBoolean("HasGreenOnion", hasGreenOnion);
        tag.putBoolean("HasCabbage", hasCabbage);
        tag.putBoolean("HasSteak", hasSteak);
        tag.putBoolean("HasGreenChili", hasGreenChili);
        tag.putBoolean("HasCoriander", hasCoriander);
        tag.putInt("StirCount", stirCount);
        tag.putLong("LastStirTime", lastStirTime);
        tag.putInt("CurrentStirVariant", currentStirVariant);
        tag.putLong("CookingStartTime", cookingStartTime);
        tag.putLong("OilPourTime", oilPourTime);
        tag.putLong("LastSauteSoundTime", lastSauteSoundTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items = NonNullList.withSize(2, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
        cookTime = tag.getInt("CookTime");
        cookStage = tag.getInt("CookStage");

        hasOil = tag.getBoolean("HasOil");
        hasPork = tag.getBoolean("HasPork");
        hasChicken = tag.getBoolean("HasChicken");
        hasRedPepper = tag.getBoolean("HasRedPepper");
        hasGreenOnion = tag.getBoolean("HasGreenOnion");
        hasCabbage = tag.getBoolean("HasCabbage");
        hasSteak = tag.getBoolean("HasSteak");
        hasGreenChili = tag.getBoolean("HasGreenChili");
        hasCoriander = tag.getBoolean("HasCoriander");
        stirCount = tag.getInt("StirCount");
        lastStirTime = tag.getLong("LastStirTime");
        currentStirVariant = tag.getInt("CurrentStirVariant");
        cookingStartTime = tag.getLong("CookingStartTime");
        oilPourTime = tag.getLong("OilPourTime");
        lastSauteSoundTime = tag.getLong("LastSauteSoundTime");
    }

    @Override
    public int getContainerSize() { return items.size(); }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) { return items.get(slot); }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = ContainerHelper.removeItem(items, slot, amount);
        if (!stack.isEmpty()) setChanged();
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) { return ContainerHelper.takeItem(items, slot); }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public int getMaxStackSize() { return 64; }

    @Override
    public boolean stillValid(Player player) {
        if (this.level == null) return false;
        return this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void clearContent() {
        items.clear();
        cookTime = 0;
        cookStage = 0;
        clearWok();
        setChanged();
    }

    public int getCookTime() { return cookTime; }
    public int getCookTimeTotal() { return cookTimeTotal; }
    public int getCookStage() { return cookStage; }

    public boolean hasHeatSource() {
        if (level == null) return false;
        BlockPos belowPos = worldPosition.below();
        var belowState = level.getBlockState(belowPos);
        
        // 检查主模组的炉灶
        if (KaleidoscopeCookeryCompat.isStoveLit(level, belowState)) {
            return true;
        }
        
        // 检查营火（作为备选）
        if (belowState.getBlock() instanceof CampfireBlock && belowState.getValue(CampfireBlock.LIT)) {
            return true;
        }
        
        return false;
    }
}
