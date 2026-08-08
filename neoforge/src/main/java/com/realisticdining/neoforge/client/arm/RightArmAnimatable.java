package com.realisticdining.neoforge.client.arm;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RightArmAnimatable implements SingletonGeoAnimatable {

    private static RightArmAnimatable INSTANCE;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);

    public static final String CONTROLLER_NAME = "right_arm_controller";
    private static final long INSTANCE_ID = 0L;

    private RightArmAnimatable() {
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    public static RightArmAnimatable getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RightArmAnimatable();
        }
        return INSTANCE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, CONTROLLER_NAME, 0, state -> {
            return PlayState.STOP;
        })
        .triggerableAnim("bite_1", RawAnimation.begin().thenPlay("animation.right_arm_fp.new"))
        .triggerableAnim("bite_2", RawAnimation.begin().thenPlay("animation.right_arm_fp.new2"))
        .triggerableAnim("bite_3", RawAnimation.begin().thenPlay("animation.right_arm_fp.new3"))
        .triggerableAnim("bite_4", RawAnimation.begin().thenPlay("animation.right_arm_fp.new4"))
        .triggerableAnim("bite_5", RawAnimation.begin().thenPlay("animation.right_arm_fp.new5"))
        .triggerableAnim("bite_6", RawAnimation.begin().thenPlay("animation.right_arm_fp.new6"))
        .triggerableAnim("bite_7", RawAnimation.begin().thenPlay("animation.right_arm_fp.new7"))
        .triggerableAnim("bite_8", RawAnimation.begin().thenPlay("animation.right_arm_fp.new8"))
        .triggerableAnim("bite_9", RawAnimation.begin().thenPlay("animation.right_arm_fp.new9")));
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

    public void triggerBiteAnimation() {
        EatRiceState state = EatRiceState.getInstance();
        if (state.isFinished()) {
            return;
        }
        
        if (state.isAnimationPlaying()) {
            return;
        }
        
        int biteIndex = state.getCurrentBiteIndex() + 1;
        String animName = "bite_" + biteIndex;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.level != null) {
            triggerAnim(mc.player, INSTANCE_ID, CONTROLLER_NAME, animName);
        }
    }
}
