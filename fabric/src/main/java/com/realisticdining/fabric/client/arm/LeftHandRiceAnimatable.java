package com.realisticdining.fabric.client.arm;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LeftHandRiceAnimatable implements SingletonGeoAnimatable {

    private static LeftHandRiceAnimatable INSTANCE;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);

    public static final String CONTROLLER_NAME = "left_hand_rice_controller";
    private static final long INSTANCE_ID = 0L;

    private LeftHandRiceAnimatable() {
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    public static LeftHandRiceAnimatable getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LeftHandRiceAnimatable();
        }
        return INSTANCE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, CONTROLLER_NAME, 0, state -> {
            return PlayState.STOP;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public double getTick(Object object) {
        if (object instanceof Entity entity) {
            return entity.tickCount;
        }
        return Minecraft.getInstance().level != null
                ? Minecraft.getInstance().level.getGameTime()
                : 0;
    }
}
