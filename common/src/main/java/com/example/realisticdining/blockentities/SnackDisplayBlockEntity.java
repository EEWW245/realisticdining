package com.example.realisticdining.blockentities;

import com.example.realisticdining.ServerEatingState;
import com.example.realisticdining.blocks.SnackDisplayBlock;
import com.example.realisticdining.common.SnackItemRegistry;
import com.example.realisticdining.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * 一个方块大小、容纳 4 个槽位的零食/饮料展示台 BlockEntity。
 *
 * <p>槽位顺序（按玩家视角 facing 旋转后）：
 * <ul>
 *   <li>槽位 0：远左（玩家对面、玩家左手侧）</li>
 *   <li>槽位 1：远右（玩家对面、玩家右手侧）</li>
 *   <li>槽位 2：近左（玩家一侧、玩家左手侧）</li>
 *   <li>槽位 3：近右（玩家一侧、玩家右手侧）</li>
 * </ul>
 *
 * <p>放置规则：玩家右键方块 + 持合法零食/饮料 → 放入 nextSlot（0→3 顺序填）。
 * 取回规则：玩家右键方块 + 空手 → 取回 hit vec 命中的槽位的物品。
 *
 * <p>持久化只存物品 ID（不存贴图/模型路径），加载时通过 SnackItemRegistry 查回，
 * 这样后续更换贴图/模型路径不影响旧存档。
 */
public class SnackDisplayBlockEntity extends BlockEntity implements GeoBlockEntity {

    public static final int SLOT_COUNT = 4;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private final Item[] slots = new Item[SLOT_COUNT];
    private int nextSlot = 0;  // 下一个放置槽位索引

    /** 客户端渲染期间的当前槽位（transient，不持久化），由 SnackDisplayRenderer 设置 */
    private transient int currentRenderingSlot = -1;

    public SnackDisplayBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SNACK_DISPLAY.get(), pos, state);
    }

    @Nullable
    public Item getSlotItem(int index) {
        if (index < 0 || index >= SLOT_COUNT) return null;
        return slots[index];
    }

    public int getNextSlot() {
        return nextSlot;
    }

    public boolean isFull() {
        return nextSlot >= SLOT_COUNT;
    }

    /** 客户端渲染时由 Renderer 设置当前正在渲染的槽位 */
    public void setCurrentRenderingSlot(int slot) {
        this.currentRenderingSlot = slot;
    }

    public int getCurrentRenderingSlot() {
        return currentRenderingSlot;
    }

    /**
     * 玩家右键交互：
     * - 空手 + 命中槽位非空 → 取回对应物品（创造模式不变更，但消费 InteractionResult）
     * - 主手持合法零食/饮料 → 放入 nextSlot，生存模式扣 1 个
     * - 副手持物品 → 不放（设计上「全部交给主手」），返回 CONSUME 阻止原版默认处理
     */
    public InteractionResult tryInteract(Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);
        if (held.isEmpty()) {
            return tryPickup(player, hit);
        }
        if (hand != InteractionHand.MAIN_HAND) {
            // 副手不放零食/饮料，仅主手可放；返回 CONSUME 视为已处理，避免触发原版默认交互
            return InteractionResult.CONSUME;
        }
        return tryPlace(player, held, hand);
    }

    public InteractionResult tryPlace(Player player, ItemStack held, InteractionHand hand) {
        Item item = held.getItem();
        if (!SnackItemRegistry.isSnackItem(item)) {
            return InteractionResult.PASS;
        }
        // v2.3.0+ 防无限刷：动画播放期间禁止放入展示台（与 SnackDisplayPlacement 一致）
        if (ServerEatingState.isEating(player.getUUID())) {
            return InteractionResult.FAIL;
        }
        if (isFull()) {
            return InteractionResult.PASS;  // 4 槽全满
        }
        slots[nextSlot] = item;
        if (!player.isCreative()) {
            held.shrink(1);
            if (held.isEmpty()) {
                player.setItemInHand(hand, ItemStack.EMPTY);
            }
        }
        nextSlot = recomputeNextSlot();
        sync();
        return InteractionResult.CONSUME;
    }

    private InteractionResult tryPickup(Player player, BlockHitResult hit) {
        int slotIndex = hitToSlot(hit);
        if (slotIndex < 0 || slotIndex >= SLOT_COUNT) {
            return InteractionResult.PASS;
        }
        if (slots[slotIndex] == null) {
            return InteractionResult.PASS;  // 命中槽位是空
        }
        ItemStack returned = new ItemStack(slots[slotIndex]);
        if (!player.getInventory().add(returned)) {
            player.drop(returned, false);
        }
        slots[slotIndex] = null;
        nextSlot = recomputeNextSlot();
        // 4 槽全空 → 展示台自动消失（避免地上遗留空台）
        // 注意：不能用 nextSlot == 0 判断，因为 slots[0]=null 但其他槽位有物品时 nextSlot 也是 0
        if (isAllEmpty() && level != null && !level.isClientSide) {
            level.removeBlock(worldPosition, false);
            return InteractionResult.CONSUME;
        }
        sync();
        return InteractionResult.CONSUME;
    }

    /** 所有槽位都空时返回 true（用于判定展示台是否应自动消失） */
    private boolean isAllEmpty() {
        for (Item slot : slots) {
            if (slot != null) return false;
        }
        return true;
    }

    private int recomputeNextSlot() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (slots[i] == null) return i;
        }
        return SLOT_COUNT;
    }

    /**
     * 根据 hit vec 算出命中的槽位（0~3）。
     *
     * <p>玩家面对方块（站在方块的 facing.opposite() 一侧，朝 facing 方向看方块）：
     * <ul>
     *   <li>远 = facing 方向 = 方块远离玩家的一侧</li>
     *   <li>左 = 玩家左手方向 = facing.getCounterClockWise()</li>
     * </ul>
     */
    private int hitToSlot(BlockHitResult hit) {
        Direction facing = getBlockState().getValue(SnackDisplayBlock.FACING);
        BlockPos pos = hit.getBlockPos();
        Vec3 hitVec = hit.getLocation();
        double lx = hitVec.x - (pos.getX() + 0.5);  // -0.5 ~ +0.5
        double lz = hitVec.z - (pos.getZ() + 0.5);

        boolean isFar, isLeft;
        switch (facing) {
            case NORTH: isFar = lz < 0; isLeft = lx < 0; break;
            case SOUTH: isFar = lz > 0; isLeft = lx > 0; break;
            case EAST:  isFar = lx > 0; isLeft = lz < 0; break;
            case WEST:  isFar = lx < 0; isLeft = lz > 0; break;
            default: return -1;
        }
        if (isFar && isLeft) return 0;   // 远左
        if (isFar && !isLeft) return 1;  // 远右
        if (!isFar && isLeft) return 2;  // 近左
        return 3;                         // 近右
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
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (slots[i] != null) {
                String id = SnackItemRegistry.itemIdOf(slots[i]);
                if (id != null) {
                    nbt.putString("Slot" + i, id);
                }
            }
        }
        nbt.putInt("NextSlot", nextSlot);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        for (int i = 0; i < SLOT_COUNT; i++) {
            String key = "Slot" + i;
            if (nbt.contains(key)) {
                String idStr = nbt.getString(key);
                ResourceLocation loc = ResourceLocation.tryParse(idStr);
                if (loc != null) {
                    Item item = BuiltInRegistries.ITEM.get(loc);
                    slots[i] = item;
                } else {
                    slots[i] = null;
                }
            } else {
                slots[i] = null;
            }
        }
        nextSlot = nbt.getInt("NextSlot");
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
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

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // 不需要播放动画，注册一个空 controller 保持 GeckoLib 渲染管线不报错
        controllers.add(new AnimationController<>(this, "controller", 0, state -> PlayState.STOP));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
