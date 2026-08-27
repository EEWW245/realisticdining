package com.example.realisticdining.client.arm;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.cache.object.GeoVertex;

/**
 * 原版手臂渲染器（v2.2.0+，参照 TaCZ 方案）。
 *
 * <p>核心思路：不渲染自定义手臂骨骼模型，而是把 GeckoLib 手臂骨骼当"锚点"，
 * 在递归渲染到手
 * 骨骼时捕获其世界变换矩阵，模型 pass 完成后用该矩阵渲染原版玩家手臂
 * （{@link PlayerRenderer#renderRightHand} / {@link PlayerRenderer#renderLeftHand}）。
 *
 * <p>优点：
 * <ul>
 *   <li>自动使用玩家皮肤（含袖子、皮肤外层、披风），效果与原版第一人称手臂完全一致</li>
 *   <li>手臂跟随骨骼动画（旋转/位移/缩放），与 GeckoLib 动画管线天然兼容</li>
 *   <li>位置由骨骼 cube bounds 自动对准，无需手动调第一人称偏移</li>
 * </ul>
 *
 * <p>骨骼命名约定（{@link #normalize} 归一化后匹配）：
 * <ul>
 *   <li>rightarm / rightarm2 / righthandpos → 右臂</li>
 *   <li>leftarm / leftarm2 / lefthandpos → 左臂</li>
 * </ul>
 * 本模组饮用动画模型的 "Left Arm" / "Right Arm" / "Right_Arm2" 骨骼归一化后直接命中约定。
 *
 * <p>矩阵映射原理（{@link #createPreviewCubeArmPose}）：
 * 原版 PlayerModel 手臂 cuboid 与 GeckoLib 骨骼 preview cuboid 均为 4x12x4，
 * 把原版手臂中心 (-6,6,0)/16（右）或 (6,6,0)/16（左）映射到骨骼 cube bounds 中心，
 * 应用骨骼 cube 旋转后补一个固定 rotateZ(π) 修正 GeckoLib 第一人称坐标系翻转。
 */
public final class VanillaArmRenderer {

    private static final float PIXEL = 1.0F / 16.0F;

    private VanillaArmRenderer() {
    }

    /** 捕获的一次手臂姿态（哪只手 + 世界矩阵 + 法线矩阵）。 */
    public record CapturedArm(HumanoidArm arm, Matrix4f pose, Matrix3f normal) {
    }

    /** 判断骨骼是否为手臂锚点骨骼（是 → 不渲染自定义 cube，改为捕获矩阵）。 */
    public static boolean isArmBone(GeoBone bone) {
        if (isDedicatedAnchor(bone)) {
            return true;
        }

        return getArm(bone) != null && !hasDedicatedAnchorChild(bone);
    }

    /**
     * 在骨骼变换已应用的 poseStack 上捕获手臂矩阵。
     *
     * <p>必须在 {@code RenderUtils.prepMatrixForBone(poseStack, bone)} 之后调用，
     * 此时 poseStack.last().pose() 即骨骼的完整世界矩阵。
     *
     * @return 捕获结果；骨骼无 cube 或非手臂骨骼时返回 null
     */
    @Nullable
    public static CapturedArm captureArmPose(PoseStack poseStack, GeoBone bone) {
        HumanoidArm arm = getArm(bone);
        GeoCube armCube = findArmCube(bone);

        if (arm == null) {
            return null;
        }

        poseStack.pushPose();
        if (armCube == null) {
            poseStack.popPose();
            return null;
        }

        Matrix4f armPose = createPreviewCubeArmPose(poseStack, arm, armCube);
        CapturedArm capturedArm = new CapturedArm(
                arm,
                armPose,
                new Matrix3f(armPose).normal()
        );

        poseStack.popPose();
        return capturedArm;
    }

    /** 用捕获的矩阵渲染原版手臂（自动玩家皮肤）。 */
    public static void renderCapturedArm(
            CapturedArm capturedArm,
            MultiBufferSource bufferSource,
            int packedLight
    ) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null || minecraft.player.isInvisible()) {
            return;
        }

        PlayerRenderer playerRenderer =
                (PlayerRenderer) minecraft.getEntityRenderDispatcher().getRenderer(minecraft.player);

        // v2.2.1 防御：FirstPersonModel / Hold My Items 可能将 PlayerModel 手臂部件
        // 设为不可见（它们隐藏原版手臂的机制），残留状态会导致我们渲染的原版手臂
        // 贴图残缺/不渲染。渲染前强制恢复手臂 + 袖子部件可见。
        net.minecraft.client.model.PlayerModel<?> playerModel = playerRenderer.getModel();
        playerModel.rightArm.visible = true;
        playerModel.rightSleeve.visible = true;
        playerModel.leftArm.visible = true;
        playerModel.leftSleeve.visible = true;

        // 姿态归零防御：More Player Models / Serious Player Animations / ParCool 等
        // 玩家动画模组会往共享 PlayerModel 的手臂部件写入旋转/位移姿态。
        // 原版 renderHand 只设置 xRot，yRot/zRot/x/y/z 的残留会被叠加到捕获矩阵上，
        // 导致镜像补偿分支依赖的"手臂处于标准姿态"几何假设失效
        // （表现为整合包中吃米饭动画左臂错位、跟不上碗）。
        // 渲染前把当前手臂 + 袖子的姿态全部归零，使手臂姿态 100% 由捕获矩阵决定，
        // 免疫任何动画模组的姿态残留。其他模组下一帧 setupAnim 会自行重写，互不影响。
        if (capturedArm.arm() == HumanoidArm.RIGHT) {
            resetPartPose(playerModel.rightArm);
            resetPartPose(playerModel.rightSleeve);
        } else {
            resetPartPose(playerModel.leftArm);
            resetPartPose(playerModel.leftSleeve);
        }

        PoseStack poseStack = new PoseStack();
        poseStack.last().pose().set(capturedArm.pose());

        // v2.2.3 镜像补偿 + 平移复位（用户指定方案）：
        // 吃米饭渲染器的第一人称变换含 X 镜像（饮用渲染器没有），负行列式翻转
        // 三角形缠绕方向，背面剔除把正面剔掉 → 手臂缺面。
        // 补 scale(-1,1,1) 恢复绕向；但镜像绕模型原点（vanilla 手臂中心距原点 6px），
        // 会产生 ±12px（0.75 格）横移 → 按臂别反向平移复位：右臂 +0.75、左臂 -0.75。
        if (new org.joml.Matrix3f(capturedArm.pose()).determinant() < 0.0F) {
            poseStack.scale(-1.0F, 1.0F, 1.0F);
            poseStack.translate(capturedArm.arm() == HumanoidArm.RIGHT ? 0.75F : -0.75F, 0.0F, 0.0F);
            poseStack.last().normal().set(new org.joml.Matrix3f(poseStack.last().pose()).normal());
        } else {
            poseStack.last().normal().set(capturedArm.normal());
        }

        if (capturedArm.arm() == HumanoidArm.RIGHT) {
            playerRenderer.renderRightHand(poseStack, bufferSource, packedLight, minecraft.player);
        } else {
            playerRenderer.renderLeftHand(poseStack, bufferSource, packedLight, minecraft.player);
        }
    }

    /** 批量渲染捕获的手臂（模型 pass 完成后调用）。 */
    public static void renderCapturedArms(
            Iterable<CapturedArm> capturedArms,
            MultiBufferSource bufferSource,
            int packedLight
    ) {
        for (CapturedArm capturedArm : capturedArms) {
            renderCapturedArm(capturedArm, bufferSource, packedLight);
        }
    }

    /** 归零 ModelPart 姿态（位移 + 三轴旋转），防动画模组残留（见 renderCapturedArm 注释）。 */
    private static void resetPartPose(net.minecraft.client.model.geom.ModelPart part) {
        part.x = 0.0F;
        part.y = 0.0F;
        part.z = 0.0F;
        part.xRot = 0.0F;
        part.yRot = 0.0F;
        part.zRot = 0.0F;
    }

    @Nullable
    private static GeoCube findArmCube(GeoBone bone) {
        return bone.getCubes().isEmpty() ? null : bone.getCubes().get(0);
    }

    @Nullable
    private static HumanoidArm getArm(GeoBone bone) {
        String normalizedName = normalize(bone.getName());

        return switch (normalizedName) {
            case "rightarm", "rightarm2", "righthandpos" -> HumanoidArm.RIGHT;
            case "leftarm", "leftarm2", "lefthandpos" -> HumanoidArm.LEFT;
            default -> null;
        };
    }

    private static boolean isDedicatedAnchor(GeoBone bone) {
        String normalizedName = normalize(bone.getName());
        return normalizedName.equals("lefthandpos")
                || normalizedName.equals("righthandpos");
    }

    private static boolean hasDedicatedAnchorChild(GeoBone bone) {
        for (GeoBone child : bone.getChildBones()) {
            if (isDedicatedAnchor(child)) {
                return true;
            }
        }

        return false;
    }

    private static String normalize(String name) {
        return name.replace("_", "").replace(" ", "").toLowerCase();
    }

    /**
     * 把原版 PlayerModel 手臂 cuboid（4x12x4）映射到骨骼 preview cuboid 的矩阵。
     *
     * <p>两个 cuboid 尺寸一致，只需中心对齐 + 旋转继承 + 固定 Z 轴 π 旋转
     * （修正 GeckoLib 第一人称坐标系转换）。
     */
    private static Matrix4f createPreviewCubeArmPose(
            PoseStack poseStack,
            HumanoidArm arm,
            GeoCube cube
    ) {
        Bounds bounds = findBounds(cube);
        Vector3f previewCenter = new Vector3f(
                (bounds.minX() + bounds.maxX()) * 0.5F,
                (bounds.minY() + bounds.maxY()) * 0.5F,
                (bounds.minZ() + bounds.maxZ()) * 0.5F
        );

        Matrix4f cubeTransform = new Matrix4f().identity();
        Vec3 cubePivot = cube.pivot();
        Vec3 cubeRotation = cube.rotation();
        cubeTransform.translate(
                (float) cubePivot.x() / 16.0F,
                (float) cubePivot.y() / 16.0F,
                (float) cubePivot.z() / 16.0F
        );
        cubeTransform.rotateZ((float) cubeRotation.z());
        cubeTransform.rotateY((float) cubeRotation.y());
        cubeTransform.rotateX((float) cubeRotation.x());
        cubeTransform.translate(
                -(float) cubePivot.x() / 16.0F,
                -(float) cubePivot.y() / 16.0F,
                -(float) cubePivot.z() / 16.0F
        );

        Vector3f targetCenter = cubeTransform.transformPosition(previewCenter);
        Vector3f vanillaCenter = arm == HumanoidArm.RIGHT
                ? new Vector3f(-6.0F, 6.0F, 0.0F).mul(PIXEL)
                : new Vector3f(6.0F, 6.0F, 0.0F).mul(PIXEL);

        Matrix4f vanillaToPreview = new Matrix4f().identity();
        vanillaToPreview.translate(targetCenter);
        vanillaToPreview.rotateZ((float) cubeRotation.z());
        vanillaToPreview.rotateY((float) cubeRotation.y());
        vanillaToPreview.rotateX((float) cubeRotation.x());
        vanillaToPreview.rotateZ((float) Math.PI);
        vanillaToPreview.translate(
                -vanillaCenter.x(),
                -vanillaCenter.y(),
                -vanillaCenter.z()
        );

        return new Matrix4f(poseStack.last().pose()).mul(vanillaToPreview);
    }

    /** 计算 cube 所有顶点的包围盒（世界坐标由外部矩阵变换前的模型空间）。 */
    private static Bounds findBounds(GeoCube cube) {
        float minX = Float.POSITIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float minZ = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;
        float maxZ = Float.NEGATIVE_INFINITY;

        for (GeoQuad quad : cube.quads()) {
            if (quad == null) {
                continue;
            }

            for (GeoVertex vertex : quad.vertices()) {
                Vector3f position = vertex.position();
                minX = Math.min(minX, position.x());
                minY = Math.min(minY, position.y());
                minZ = Math.min(minZ, position.z());
                maxX = Math.max(maxX, position.x());
                maxY = Math.max(maxY, position.y());
                maxZ = Math.max(maxZ, position.z());
            }
        }

        if (Float.isInfinite(minX)) {
            return new Bounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        }

        return new Bounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private record Bounds(
            float minX,
            float minY,
            float minZ,
            float maxX,
            float maxY,
            float maxZ
    ) {
    }
}
