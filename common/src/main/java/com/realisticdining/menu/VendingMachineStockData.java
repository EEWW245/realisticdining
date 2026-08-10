package com.realisticdining.menu;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 自动售货机每日库存数据（1.21.1 SavedData）。
 *
 * <p>按玩家 UUID 记录当天每种物品已被购买的次数。
 * 到了新的一天（level.getDayTime() / 24000L 改变）自动清空所有计数。
 *
 * <p>每种物品每个玩家每天最多购买 {@link #MAX_PURCHASES_PER_ITEM} 次，
 * 不论从哪台售货机购买、换多少台售货机都不会重置额度。
 */
public class VendingMachineStockData extends SavedData {

    private static final String DATA_NAME = "realisticdining_vending_machine_stock";
    public static final int MAX_PURCHASES_PER_ITEM = 6;

    /** key: 玩家 UUID, value: (itemId -> purchases today) */
    private final Map<UUID, Map<ResourceLocation, Integer>> stock = new HashMap<>();
    private long lastDay = -1L;

    public VendingMachineStockData() {}

    public VendingMachineStockData(CompoundTag tag, HolderLookup.Provider provider) {
        this.lastDay = tag.getLong("lastDay");
        ListTag entries = tag.getList("entries", Tag.TAG_COMPOUND);
        for (int i = 0; i < entries.size(); i++) {
            CompoundTag entry = entries.getCompound(i);
            UUID playerUUID = entry.getUUID("uuid");
            ListTag items = entry.getList("items", Tag.TAG_COMPOUND);
            Map<ResourceLocation, Integer> itemMap = new HashMap<>();
            for (int j = 0; j < items.size(); j++) {
                CompoundTag itemEntry = items.getCompound(j);
                itemMap.put(ResourceLocation.parse(itemEntry.getString("id")), itemEntry.getInt("count"));
            }
            this.stock.put(playerUUID, itemMap);
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putLong("lastDay", this.lastDay);
        ListTag entries = new ListTag();
        for (Map.Entry<UUID, Map<ResourceLocation, Integer>> e : this.stock.entrySet()) {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("uuid", e.getKey());
            ListTag items = new ListTag();
            for (Map.Entry<ResourceLocation, Integer> ie : e.getValue().entrySet()) {
                CompoundTag itemEntry = new CompoundTag();
                itemEntry.putString("id", ie.getKey().toString());
                itemEntry.putInt("count", ie.getValue());
                items.add(itemEntry);
            }
            entry.put("items", items);
            entries.add(entry);
        }
        tag.put("entries", entries);
        return tag;
    }

    public static VendingMachineStockData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(
                        VendingMachineStockData::new,
                        VendingMachineStockData::new,
                        null),
                DATA_NAME);
    }

    /** 检查日期变化；若进入新的一天则清空所有计数。 */
    private void checkDay(ServerLevel level) {
        long currentDay = level.getDayTime() / 24000L;
        if (currentDay != this.lastDay) {
            this.stock.clear();
            this.lastDay = currentDay;
            setDirty();
        }
    }

    /** 返回该玩家该物品今日剩余可购买次数。 */
    public int getRemaining(ServerLevel level, UUID playerUUID, ResourceLocation itemId) {
        checkDay(level);
        Map<ResourceLocation, Integer> itemMap = this.stock.get(playerUUID);
        if (itemMap == null) return MAX_PURCHASES_PER_ITEM;
        Integer count = itemMap.get(itemId);
        if (count == null) return MAX_PURCHASES_PER_ITEM;
        return Math.max(0, MAX_PURCHASES_PER_ITEM - count);
    }

    /**
     * 尝试购买一次：若仍有库存返回 true 并 +1 计数；否则返回 false。
     * 调用方应在返回 false 时给玩家发送"货物数量不足"提示。
     */
    public boolean tryPurchase(ServerLevel level, UUID playerUUID, ResourceLocation itemId) {
        checkDay(level);
        Map<ResourceLocation, Integer> itemMap = this.stock.computeIfAbsent(playerUUID, k -> new HashMap<>());
        Integer count = itemMap.get(itemId);
        int current = count == null ? 0 : count;
        if (current >= MAX_PURCHASES_PER_ITEM) return false;
        itemMap.put(itemId, current + 1);
        setDirty();
        return true;
    }
}
