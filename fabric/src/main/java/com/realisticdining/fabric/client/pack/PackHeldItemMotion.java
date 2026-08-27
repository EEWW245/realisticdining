package com.realisticdining.fabric.client.pack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * 材质包扩展物品的程序化持物晃动（Fabric 1.21.1）。
 *
 * <p>复刻自 ImmersiveEating 的 HeldItemMotion，提供 4 种数学函数驱动的晃动，
 * 不依赖任何 GeckoLib 动画文件——持物状态完全用程序姿态解决。
 * <ul>
 *   <li>{@link #applyIdleMotion}：待机自然晃动</li>
 *   <li>{@link #applyWalkMotion}：走路晃动</li>
 *   <li>{@link #applyInertiaMotion}：惯性晃动（启停时物品落后/前摆）</li>
 *   <li>{@link #applyJumpMotion}：跳跃晃动</li>
 * </ul>
 */
public final class PackHeldItemMotion {

    private static Vec3 lastHorizontalVelocity = Vec3.ZERO;

    private static float inertiaPitch;
    private static float inertiaPitchVelocity;
    private static float inertiaRoll;
    private static float inertiaRollVelocity;

    private static boolean wasOnGround = true;
    private static float lastVerticalVelocity;

    private static float jumpY;
    private static float jumpYVelocity;

    private static float jumpPitch;
    private static float jumpPitchVelocity;

    private static float previousInertiaPitch;
    private static float previousInertiaRoll;
    private static float previousJumpY;
    private static float previousJumpPitch;

    private PackHeldItemMotion() {
    }

    /** 待机自然晃动（基于玩家 tick 计数的 sin/cos）。 */
    public static void applyIdleMotion(PoseStack poseStack, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        float time = mc.player.tickCount + partialTick;
        float x = Mth.sin(time * 0.075F) * 0.006F;
        float y = Mth.cos(time * 0.105F) * 0.004F;
        float pitch = Mth.cos(time * 0.060F) * 0.35F;
        float roll = Mth.sin(time * 0.085F) * 0.50F;
        poseStack.translate(x, y, 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        poseStack.mulPose(Axis.ZP.rotationDegrees(roll));
    }

    /** 走路晃动（基于 walkAnimation phase + speed）。 */
    public static void applyWalkMotion(PoseStack poseStack, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        Player p = mc.player;
        float phase = p.walkAnimation.position(partialTick);
        float amount = Mth.clamp(p.walkAnimation.speed(partialTick) * 1.35F, 0.0F, 1.0F);
        if (!p.onGround()) {
            amount *= 0.2F;
        }
        // 动画播放期间降低晃动幅度，避免与动画姿态冲突
        if (PackAnimationLock.isLocked()) {
            amount *= 0.15F;
        }
        float x = Mth.sin(phase) * 0.003F * amount;
        float y = Mth.cos(phase * 2.0F) * 0.0025F * amount;
        float pitch = Mth.cos(phase * 2.0F) * 0.45F * amount;
        float roll = Mth.sin(phase) * 0.55F * amount;
        poseStack.translate(x, y, 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        poseStack.mulPose(Axis.ZP.rotationDegrees(roll));
    }

    /** 惯性晃动（启停加速度反作用）。 */
    public static void applyInertiaMotion(PoseStack poseStack, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        float pitch = Mth.lerp(partialTick, previousInertiaPitch, inertiaPitch);
        float roll = Mth.lerp(partialTick, previousInertiaRoll, inertiaRoll);
        float intensity = mc.player.isUsingItem() ? 0.15F : 1.0F;
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch * intensity));
        poseStack.mulPose(Axis.ZP.rotationDegrees(roll * intensity));
    }

    public static void tickInertia() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            resetInertia();
            return;
        }
        previousInertiaPitch = inertiaPitch;
        previousInertiaRoll = inertiaRoll;
        Player player = mc.player;
        Vec3 velocity = player.getDeltaMovement();
        Vec3 currentHorizontalVelocity = new Vec3(velocity.x, 0.0, velocity.z);
        Vec3 acceleration = currentHorizontalVelocity.subtract(lastHorizontalVelocity);
        float yaw = player.getYRot() * Mth.DEG_TO_RAD;
        Vec3 forward = new Vec3(-Mth.sin(yaw), 0.0, Mth.cos(yaw));
        Vec3 right = new Vec3(Mth.cos(yaw), 0.0, Mth.sin(yaw));
        float forwardAcceleration = (float) acceleration.dot(forward);
        float sideAcceleration = (float) acceleration.dot(right);
        inertiaPitchVelocity -= forwardAcceleration * 16.0F;
        inertiaRollVelocity += sideAcceleration * 10.0F;
        inertiaPitchVelocity += -inertiaPitch * 0.20F;
        inertiaPitchVelocity *= 0.72F;
        inertiaPitch += inertiaPitchVelocity;
        inertiaRollVelocity += -inertiaRoll * 0.20F;
        inertiaRollVelocity *= 0.72F;
        inertiaRoll += inertiaRollVelocity;
        inertiaPitch = Mth.clamp(inertiaPitch, -8.0F, 8.0F);
        inertiaRoll = Mth.clamp(inertiaRoll, -6.0F, 6.0F);
        lastHorizontalVelocity = currentHorizontalVelocity;
    }

    public static void resetInertia() {
        lastHorizontalVelocity = Vec3.ZERO;
        inertiaPitch = 0.0F;
        inertiaPitchVelocity = 0.0F;
        inertiaRoll = 0.0F;
        inertiaRollVelocity = 0.0F;
    }

    public static void tickJump() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            resetJump();
            return;
        }
        previousJumpY = jumpY;
        previousJumpPitch = jumpPitch;
        Player player = mc.player;
        Vec3 velocity = player.getDeltaMovement();
        boolean onGround = player.onGround();
        if (wasOnGround && !onGround && velocity.y > 0.05) {
            jumpYVelocity += 0.035F;
            jumpPitchVelocity -= 1.0F;
        }
        if (!wasOnGround && onGround && lastVerticalVelocity < -0.05F) {
            float impact = Mth.clamp(-lastVerticalVelocity * 1.5F, 0.0F, 1.0F);
            jumpYVelocity -= 0.025F + impact * 0.035F;
            jumpPitchVelocity += 0.8F + impact * 1.4F;
        }
        jumpYVelocity += -jumpY * 0.16F;
        jumpYVelocity *= 0.78F;
        jumpY += jumpYVelocity;
        jumpPitchVelocity += -jumpPitch * 0.18F;
        jumpPitchVelocity *= 0.76F;
        jumpPitch += jumpPitchVelocity;
        jumpY = Mth.clamp(jumpY, -0.08F, 0.08F);
        jumpPitch = Mth.clamp(jumpPitch, -5.0F, 5.0F);
        lastVerticalVelocity = (float) velocity.y;
        wasOnGround = onGround;
    }

    public static void resetJump() {
        wasOnGround = true;
        lastVerticalVelocity = 0.0F;
        jumpY = 0.0F;
        jumpYVelocity = 0.0F;
        jumpPitch = 0.0F;
        jumpPitchVelocity = 0.0F;
    }

    public static void applyJumpMotion(PoseStack poseStack, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        float y = Mth.lerp(partialTick, previousJumpY, jumpY);
        float pitch = Mth.lerp(partialTick, previousJumpPitch, jumpPitch);
        float intensity = mc.player.isUsingItem() ? 0.15F : 1.0F;
        poseStack.translate(0.0F, y * intensity, 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch * intensity));
    }
}
