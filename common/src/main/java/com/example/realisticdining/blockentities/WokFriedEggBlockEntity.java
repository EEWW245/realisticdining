package com.example.realisticdining.blockentities;

import com.example.realisticdining.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WokFriedEggBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private boolean hasTomatoSlice = false;
    private int spatulaClickCount = 0;
    private boolean animationStarted = false;
    private long animationStartTime = 0;
    private long animationEndTime = 0;
    private long tomatoSliceAddedTime = 0;
    private long cookingCompleteTime = 0;

    private static final double[] ANIMATION_LENGTHS = {1.0958, 1.0958, 0.75};
    private static final long WAIT_BEFORE_STIR_MS = 5000;
    private static final long STIR_DEADLINE_MS = 25000;
    private static final long BURN_WINDOW_MS = 15000;

    public WokFriedEggBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WOK_FRIED_EGG.get(), pos, state);
    }

    public boolean hasTomatoSlice() {
        return hasTomatoSlice;
    }

    public void setHasTomatoSlice(boolean hasTomatoSlice) {
        this.hasTomatoSlice = hasTomatoSlice;
        if (hasTomatoSlice) {
            this.tomatoSliceAddedTime = System.currentTimeMillis();
        }
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public int getSpatulaClickCount() {
        return spatulaClickCount;
    }

    public boolean isCurrentlyAnimating() {
        long currentTime = System.currentTimeMillis();
        return animationEndTime > 0 && currentTime < animationEndTime;
    }

    public boolean canStartStirring() {
        if (!hasTomatoSlice) return false;
        long currentTime = System.currentTimeMillis();
        return currentTime >= tomatoSliceAddedTime + WAIT_BEFORE_STIR_MS;
    }

    public int getRemainingWaitSeconds() {
        if (!hasTomatoSlice) return 0;
        long currentTime = System.currentTimeMillis();
        long remaining = tomatoSliceAddedTime + WAIT_BEFORE_STIR_MS - currentTime;
        return remaining > 0 ? (int) Math.ceil(remaining / 1000.0) : 0;
    }

    public int getRemainingStirSeconds() {
        if (!hasTomatoSlice) return 0;
        long currentTime = System.currentTimeMillis();
        long remaining = tomatoSliceAddedTime + STIR_DEADLINE_MS - currentTime;
        return remaining > 0 ? (int) Math.ceil(remaining / 1000.0) : 0;
    }

    public void startStirAnimation() {
        long currentTime = System.currentTimeMillis();
        if (animationEndTime > 0 && currentTime < animationEndTime) {
            return;
        }

        this.spatulaClickCount++;
        this.animationStarted = false;
        this.animationStartTime = currentTime;

        int animIndex = (this.spatulaClickCount - 1) % 3;
        double animLength = ANIMATION_LENGTHS[animIndex];
        this.animationEndTime = currentTime + (long) (animLength * 1000);

        if (spatulaClickCount >= 6 && cookingCompleteTime == 0) {
            this.cookingCompleteTime = tomatoSliceAddedTime + STIR_DEADLINE_MS;
        }

        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public boolean isCookingComplete() {
        if (spatulaClickCount < 6) return false;
        long currentTime = System.currentTimeMillis();
        return currentTime >= tomatoSliceAddedTime + STIR_DEADLINE_MS;
    }

    public int getRemainingServeSeconds() {
        if (spatulaClickCount < 6) return 0;
        long currentTime = System.currentTimeMillis();
        long remaining = tomatoSliceAddedTime + STIR_DEADLINE_MS - currentTime;
        return remaining > 0 ? (int) Math.ceil(remaining / 1000.0) : 0;
    }

    public boolean canGetSuspiciousStew() {
        if (!hasTomatoSlice) return false;
        if (spatulaClickCount >= 6) return false;
        long currentTime = System.currentTimeMillis();
        return currentTime > tomatoSliceAddedTime + STIR_DEADLINE_MS;
    }

    public boolean isBurnt() {
        if (!hasTomatoSlice) return false;
        long currentTime = System.currentTimeMillis();
        return currentTime > tomatoSliceAddedTime + STIR_DEADLINE_MS + BURN_WINDOW_MS;
    }

    public int getRemainingBurnSeconds() {
        if (!hasTomatoSlice) return 0;
        long currentTime = System.currentTimeMillis();
        long deadline = tomatoSliceAddedTime + STIR_DEADLINE_MS + BURN_WINDOW_MS;
        long remaining = deadline - currentTime;
        return remaining > 0 ? (int) Math.ceil(remaining / 1000.0) : 0;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("HasTomatoSlice", hasTomatoSlice);
        tag.putInt("SpatulaClickCount", spatulaClickCount);
        tag.putLong("AnimationStartTime", animationStartTime);
        tag.putLong("AnimationEndTime", animationEndTime);
        tag.putLong("TomatoSliceAddedTime", tomatoSliceAddedTime);
        tag.putLong("CookingCompleteTime", cookingCompleteTime);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.hasTomatoSlice = tag.getBoolean("HasTomatoSlice");
        this.spatulaClickCount = tag.getInt("SpatulaClickCount");
        this.animationStartTime = tag.getLong("AnimationStartTime");
        this.animationEndTime = tag.getLong("AnimationEndTime");
        this.tomatoSliceAddedTime = tag.getLong("TomatoSliceAddedTime");
        this.cookingCompleteTime = tag.getLong("CookingCompleteTime");
        this.animationStarted = false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> {
            long currentTime = System.currentTimeMillis();
            boolean isAnimating = animationEndTime > 0 && currentTime < animationEndTime;

            if (isAnimating) {
                if (!animationStarted) {
                    animationStarted = true;
                    state.getController().forceAnimationReset();
                }
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.model.new"));
            }

            if (spatulaClickCount > 0) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.model.new"));
            }

            return PlayState.STOP;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
