package com.example.realisticdining.client.screen;

import com.example.realisticdining.menu.VendingMachineMenu;
import com.example.realisticdining.menu.VendingMachinePrices;
import com.example.realisticdining.platform.ServiceHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动售货机 GUI
 * - 7 列 × 3 行 = 21 格（19 个饮料/零食 + 2 空）
 * - 每格显示物品图标 + 价格（Xg）
 * - 右下角关闭按钮
 * - 点击图标发送购买请求（扣金粒+给物品）
 */
public class VendingMachineScreen extends AbstractContainerScreen<VendingMachineMenu> {

    private static final int GRID_COLS = 7;
    private static final int GRID_ROWS = 3;
    private static final int CELL_WIDTH = 22;
    private static final int CELL_HEIGHT = 30;       // 16 图标 + 14 价格文字
    private static final int GRID_START_X = 11;
    private static final int GRID_START_Y = 22;

    private final List<ItemStack> displayItems = new ArrayList<>();
    private final List<ResourceLocation> displayIds = new ArrayList<>();

    public VendingMachineScreen(VendingMachineMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelY = 7;
        this.inventoryLabelY = -100;   // 隐藏 "Inventory"
    }

    private void loadItems() {
        displayItems.clear();
        displayIds.clear();
        for (ResourceLocation id : VendingMachinePrices.getAllItems()) {
            Item item = BuiltInRegistries.ITEM.get(id);
            if (item != Items.AIR) {
                displayItems.add(new ItemStack(item));
                displayIds.add(id);
            }
        }
    }

    @Override
    protected void init() {
        loadItems();
        super.init();
        // 右下角关闭按钮
        this.addRenderableWidget(Button.builder(
                Component.translatable("gui.realisticdining.vending_machine.close"),
                button -> this.onClose()
        ).bounds(this.leftPos + this.imageWidth - 60, this.topPos + this.imageHeight - 25, 50, 16).build());
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        // 鼠标悬停的物品 tooltip
        int hoverIndex = getHoveredCellIndex(mouseX, mouseY);
        if (hoverIndex >= 0 && hoverIndex < displayItems.size()) {
            guiGraphics.renderTooltip(this.font, displayItems.get(hoverIndex), mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        // 外框 + 内框
        guiGraphics.fill(x, y, x + imageWidth, y + imageHeight, 0xFF2B2B2B);
        guiGraphics.fill(x + 2, y + 2, x + imageWidth - 2, y + imageHeight - 2, 0xFF1A1A1A);

        // 标题
        guiGraphics.drawString(this.font, this.title, x + 8, y + this.titleLabelY, 0xFFFFFFFF, false);

        // 副标题：比标题小一号（使用 0.5x 缩放渲染），单行显示
        Component subtitle = Component.translatable("gui.realisticdining.vending_machine.subtitle");
        // 用 font 宽度算居中位置；用 PoseStack 缩小字体
        int subtitleWidth = this.font.width(subtitle);
        float scale = 0.5F;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x + (this.imageWidth - subtitleWidth * scale) / 2.0F, y + this.titleLabelY + 10, 0);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.drawString(this.font, subtitle, 0, 0, 0xFFAAAAAA, false);
        guiGraphics.pose().popPose();

        // 物品网格
        for (int i = 0; i < displayItems.size(); i++) {
            int col = i % GRID_COLS;
            int row = i / GRID_COLS;
            int cellX = x + GRID_START_X + col * CELL_WIDTH;
            int cellY = y + GRID_START_Y + row * CELL_HEIGHT;

            // 单元格背景
            guiGraphics.fill(cellX, cellY, cellX + 20, cellY + 28, 0xFF333333);
            guiGraphics.fill(cellX + 1, cellY + 1, cellX + 19, cellY + 27, 0xFF222222);

            // 物品图标
            guiGraphics.renderItem(displayItems.get(i), cellX + 2, cellY + 2);

            // 价格
            int price = VendingMachinePrices.getPrice(displayIds.get(i));
            String priceText = price + "g";
            int textWidth = this.font.width(priceText);
            // 价格颜色：≤1 绿色，2 黄绿色，3-4 黄色，≥5 橙色
            int priceColor;
            if (price <= 1) {
                priceColor = 0xFF55FF55;
            } else if (price == 2) {
                priceColor = 0xFFAAFF55;
            } else if (price <= 4) {
                priceColor = 0xFFFFFF55;
            } else {
                priceColor = 0xFFFFAA55;
            }
            guiGraphics.drawString(this.font, priceText,
                    cellX + 10 - textWidth / 2, cellY + 19, priceColor, false);
        }
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // 标签在 renderBg 中已处理
    }

    private int getHoveredCellIndex(double mouseX, double mouseY) {
        int x = this.leftPos + GRID_START_X;
        int y = this.topPos + GRID_START_Y;
        for (int i = 0; i < displayItems.size(); i++) {
            int col = i % GRID_COLS;
            int row = i / GRID_COLS;
            int cellX = x + col * CELL_WIDTH;
            int cellY = y + row * CELL_HEIGHT;
            if (mouseX >= cellX && mouseX < cellX + 20 && mouseY >= cellY && mouseY < cellY + 28) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int idx = getHoveredCellIndex(mouseX, mouseY);
        if (idx >= 0 && idx < displayIds.size()) {
            // 发送购买请求 C2S 包
            ServiceHelper.getPlatformServices().sendVendingPurchase(displayIds.get(idx));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
