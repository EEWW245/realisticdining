package com.realisticdining.neoforge.client.arm;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.cache.object.GeoVertex;

/**
 * 原版手臂渲染器（v2.2.0+，参照 TaCZ 方案，MC 1.21.1 Fabric 端）。
 *
 * <p>核心思路：不渲染自定义手臂骨骼模型，而是把 GeckoLib 手臂骨骼当"锚点"，
 * 在递归渲染到手臂骨骼时捕获其世界变换矩阵，模型 pass 完成后用该矩阵渲染
 * 原版玩家手臂（renderRightHand / renderLeftHand），自动带玩家皮肤。
 *
 * <p>骨骼命名约定（normalize 归一化后匹配）：
 * rightarm/rightarm2/righthandpos → 右臂；leftarm/leftarm2/lefthandpos → 左臂。
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
     * 必须在 RenderUtils.prepMatrixForBone(poseStack, bone) 之后调用。
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

        PoseStack poseStack = new PoseStack();
        poseStack.last().pose().set(capturedArm.pose());

        // v2.2.3 镜像补偿 + 平移复位（用户指定方案，替代已撤销的 v2.2.2 noCull）：
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
     * 把原版 PlayerModel 手臂 cuboid（4x12x4）映射到骨骼 preview cuboid 的矩阵：
     * 中心对齐 + 旋转继承 + 固定 Z 轴 π 旋转（修正 GeckoLib 第一人称坐标系转换）。
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
        float pivotX = (float) cube.pivot().x();
        float pivotY = (float) cube.pivot().y();
        float pivotZ = (float) cube.pivot().z();
        float rotX = (float) cube.rotation().x();
        float rotY = (float) cube.rotation().y();
        float rotZ = (float) cube.rotation().z();
        cubeTransform.translate(
                pivotX / 16.0F,
                pivotY / 16.0F,
                pivotZ / 16.0F
        );
        cubeTransform.rotateZ(rotZ);
        cubeTransform.rotateY(rotY);
        cubeTransform.rotateX(rotX);
        cubeTransform.translate(
                -pivotX / 16.0F,
                -pivotY / 16.0F,
                -pivotZ / 16.0F
        );

        Vector3f targetCenter = cubeTransform.transformPosition(previewCenter);
        Vector3f vanillaCenter = arm == HumanoidArm.RIGHT
                ? new Vector3f(-6.0F, 6.0F, 0.0F).mul(PIXEL)
                : new Vector3f(6.0F, 6.0F, 0.0F).mul(PIXEL);

        Matrix4f vanillaToPreview = new Matrix4f().identity();
        vanillaToPreview.translate(targetCenter);
        vanillaToPreview.rotateZ(rotZ);
        vanillaToPreview.rotateY(rotY);
        vanillaToPreview.rotateX(rotX);
        vanillaToPreview.rotateZ((float) Math.PI);
        vanillaToPreview.translate(
                -vanillaCenter.x(),
                -vanillaCenter.y(),
                -vanillaCenter.z()
        );

        return new Matrix4f(poseStack.last().pose()).mul(vanillaToPreview);
    }

    /** 计算 cube 所有顶点的包围盒。 */
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

