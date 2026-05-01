package com.example.realisticdining.blockentities;

import com.example.realisticdining.compat.KaleidoscopeCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

import com.example.realisticdining.menus.WokMenu;

public class WokBlockEntity extends BaseContainerBlockEntity implements Container {
    // ================= 基础 GUI 与配方系统变量 =================
    private NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private int cookTime = 0;
    private final int cookTimeTotal = 200;
    private int cookStage = 0;

    // ================= ✨ 高级物理炒菜专属变量 ✨ =================
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

    // ================= ✨ 新增：计时系统 ✨ =================
    private long cookingStartTime = 0;
    private static final long COOK_DURATION = 40 * 20;
    private static final long BURN_DURATION = 20 * 20;
    private static final int REQUIRED_STIRS = 30;

    // ================= ✨ 油滋啦声计时系统 ✨ =================
    private long oilPourTime = 0;
    private long lastSauteSoundTime = 0;
    private static final long SAUTE_DELAY = 10 * 20;  // 10秒后开始播放
    private static final long SAUTE_DURATION = 10 * 20; // 每10秒播放一次

    public WokBlockEntity(BlockPos pos, BlockState state) {
        super(com.example.realisticdining.init.ModBlockEntities.WOK.get(), pos, state);
    }

    protected WokBlockEntity(net.minecraft.world.level.block.entity.BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // ================= ✨ 物理炒菜专属方法 ✨ =================

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
        return hasOil && (hasPork || hasChicken || hasRedPepper || hasGreenOnion) && hasCabbage;
    }

    public int getStirCount() { return stirCount; }
    public long getLastStirTime() { return lastStirTime; }
    public int getCurrentStirVariant() { return currentStirVariant; }
    public long getCookingStartTime() { return cookingStartTime; }

    public void performStir(long gameTime) {
        this.stirCount++;
        this.lastStirTime = gameTime;
        // 辣子鸡使用30个变体，其他使用8个变体
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

    private void sync() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    // ================= 数据保存与网络同步 =================

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, items);
        nbt.putInt("CookTime", cookTime);
        nbt.putInt("CookStage", cookStage);

        nbt.putBoolean("HasOil", hasOil);
        nbt.putBoolean("HasPork", hasPork);
        nbt.putBoolean("HasChicken", hasChicken);
        nbt.putBoolean("HasRedPepper", hasRedPepper);
        nbt.putBoolean("HasGreenOnion", hasGreenOnion);
        nbt.putBoolean("HasCabbage", hasCabbage);
        nbt.putBoolean("HasSteak", hasSteak);
        nbt.putBoolean("HasGreenChili", hasGreenChili);
        nbt.putBoolean("HasCoriander", hasCoriander);
        nbt.putInt("StirCount", stirCount);
        nbt.putLong("LastStirTime", lastStirTime);
        nbt.putInt("CurrentStirVariant", currentStirVariant);
        nbt.putLong("CookingStartTime", cookingStartTime);
        nbt.putLong("OilPourTime", oilPourTime);
        nbt.putLong("LastSauteSoundTime", lastSauteSoundTime);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        items = NonNullList.withSize(2, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, items);
        cookTime = nbt.getInt("CookTime");
        cookStage = nbt.getInt("CookStage");

        hasOil = nbt.getBoolean("HasOil");
        hasPork = nbt.getBoolean("HasPork");
        hasChicken = nbt.getBoolean("HasChicken");
        hasRedPepper = nbt.getBoolean("HasRedPepper");
        hasGreenOnion = nbt.getBoolean("HasGreenOnion");
        hasCabbage = nbt.getBoolean("HasCabbage");
        hasSteak = nbt.getBoolean("HasSteak");
        hasGreenChili = nbt.getBoolean("HasGreenChili");
        hasCoriander = nbt.getBoolean("HasCoriander");
        stirCount = nbt.getInt("StirCount");
        lastStirTime = nbt.getLong("LastStirTime");
        currentStirVariant = nbt.getInt("CurrentStirVariant");
        cookingStartTime = nbt.getLong("CookingStartTime");
        oilPourTime = nbt.getLong("OilPourTime");
        lastSauteSoundTime = nbt.getLong("LastSauteSoundTime");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(net.minecraft.network.Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            load(tag);
        }
    }

    // ================= 基础容器逻辑 =================

    public boolean addIngredient(ItemStack ingredient) {
        if (items.get(0).isEmpty() && items.get(1).isEmpty()) {
            items.set(0, ingredient);
            cookTime = 0;
            cookStage = 0;
            setChanged();
            return true;
        }
        return false;
    }

    public ItemStack takeResult() {
        ItemStack result = items.get(1);
        if (!result.isEmpty()) {
            items.set(1, ItemStack.EMPTY);
            setChanged();
        }
        return result;
    }

    public void cook() {
        if (!items.get(0).isEmpty() && items.get(1).isEmpty()) {
            cookTime++;
            updateCookStage();
            if (cookTime >= cookTimeTotal) {
                items.set(1, processIngredient(items.get(0)));
                items.set(0, ItemStack.EMPTY);
                cookTime = 0;
                cookStage = 0;
                setChanged();
            }
        }
    }

    private void updateCookStage() {
        int newStage = (cookTime * 8) / cookTimeTotal;
        if (newStage > 7) newStage = 7;
        if (newStage != cookStage) {
            cookStage = newStage;
            setChanged();
        }
    }

    private ItemStack processIngredient(ItemStack input) {
        ItemStack result = getRecipeResult(input);
        if (!result.isEmpty()) return result;
        return com.example.realisticdining.compat.FoodCompatibilityManager.processFood(input);
    }

    private ItemStack getRecipeResult(ItemStack input) {
        Level level = this.level;
        if (input.isEmpty() || level == null) return ItemStack.EMPTY;
        com.example.realisticdining.recipes.WokRecipeInput recipeInput = new com.example.realisticdining.recipes.WokRecipeInput(input);
        return level.getRecipeManager().getRecipeFor(com.example.realisticdining.recipes.WokRecipe.WOK_RECIPE_TYPE, recipeInput, level)
                .map(recipe -> recipe.getResultItem(level.registryAccess()))
                .orElse(ItemStack.EMPTY);
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
    public boolean stillValid(@NotNull Player player) {
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

    @Override
    protected Component getDefaultName() { return Component.literal("Wok"); }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new WokMenu(id, inventory, this);
    }

    public int getCookTime() { return cookTime; }
    public int getCookTimeTotal() { return cookTimeTotal; }
    public int getCookStage() { return cookStage; }

    public boolean hasHeatSource() {
        if (level == null) return false;
        BlockPos belowPos = worldPosition.below();
        BlockState belowState = level.getBlockState(belowPos);
        
        if (KaleidoscopeCompat.isStoveLit(level, belowState)) {
            return true;
        }
        
        if (belowState.hasProperty(BlockStateProperties.LIT)) {
            return belowState.getValue(BlockStateProperties.LIT);
        }
        
        return false;
    }
}
