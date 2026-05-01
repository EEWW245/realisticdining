package com.example.realisticdining.blockentities;

import com.example.realisticdining.init.ModBlockEntities;
import com.example.realisticdining.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;


public class WokYellowSteakBlockEntity extends WokBlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int stirPhase = 0;
    private boolean animationStarted = false;
    private long animationStartTime = 0;
    private long animationEndTime = 0;

    public WokYellowSteakBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WOK_YELLOW_STEAK.get(), pos, state);
    }

    public int getStirPhase() {
        return stirPhase;
    }

    public boolean isCurrentlyStirring() {
        long currentTime = System.currentTimeMillis();
        return animationEndTime > 0 && currentTime < animationEndTime;
    }

    public void startStirAnimation() {
        long currentTime = System.currentTimeMillis();
        if (animationEndTime > 0 && currentTime < animationEndTime) {
            return;
        }
        
        this.stirPhase = (this.stirPhase % 3) + 1;
        
        this.animationStarted = false;
        this.animationStartTime = currentTime;
        
        double animLength = (this.stirPhase == 1) ? 0.625 : ((this.stirPhase == 2) ? 0.7917 : 1.2968);
        this.animationEndTime = currentTime + (long)(animLength * 1000);

        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
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
        tag.putInt("StirPhase", stirPhase);
        tag.putLong("AnimationStartTime", animationStartTime);
        tag.putLong("AnimationEndTime", animationEndTime);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.stirPhase = tag.getInt("StirPhase");
        this.animationStartTime = tag.getLong("AnimationStartTime");
        this.animationEndTime = tag.getLong("AnimationEndTime");
        this.animationStarted = false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", state -> {
            long currentTime = System.currentTimeMillis();
            boolean isAnimating = animationEndTime > 0 && currentTime < animationEndTime;

            if (isAnimating) {
                if (!animationStarted) {
                    animationStarted = true;
                    state.getController().forceAnimationReset();
                }

                return state.setAndContinue(RawAnimation.begin().thenPlay("animation"));
            }

            if (stirPhase == 0) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.wok_yellow_steak.idle"));
            }

            return state.setAndContinue(RawAnimation.begin().thenPlay("animation"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
