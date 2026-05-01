package com.realisticdining.menu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CookbookScreen extends AbstractContainerScreen<CookbookMenu> {

    private static final int BOOK_WIDTH = 300;
    private static final int BOOK_HEIGHT = 200;

    private int currentPage = 0;
    private final List<PageContent> pages = new ArrayList<>();

    private Button prevButton;
    private Button nextButton;
    private Button closeButton;

    public CookbookScreen(CookbookMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = BOOK_WIDTH;
        this.imageHeight = BOOK_HEIGHT;
        initPages();
    }

    private void initPages() {
        // 第 1 页：模组介绍
        pages.add(new PageContent(
                "=== 真实用餐模组 ===",
                List.of(
                        "欢迎来到真实用餐模组！",
                        "",
                        "本模组与森罗物语厨房联动，",
                        "为你带来真实的烹饪体验。",
                        "",
                        "本模组新增多种食材、",
                        "烹饪工具和完整的烹饪流程。"
                )
        ));

        // 第 2 页：森罗物语厨房物品
        pages.add(new PageContent(
                "=== 森罗物语厨房物品 ===",
                List.of(
                        "需要使用的物品：",
                        "",
                        "• 油壶：倒油",
                        "• 红辣椒：切好的辣椒",
                        "• 生菜：切好的青菜",
                        "• 米饭物品：生成米饭模型",
                        "• 炉灶：热源",
                        "• 番茄：切成番茄片",
                        "• 青辣椒：切成青辣椒片",
                        "• 蛋炒饭物品：生成蛋炒饭模型"
                )
        ));

        // 第 3 页：种植系统
        pages.add(new PageContent(
                "=== 种植系统 ===",
                List.of(
                        "获取葱种子：",
                        "• 用剪刀左键破坏草",
                        "• 45% 概率获得葱种子",
                        "",
                        "获取香菜种子：",
                        "• 用剪刀左键破坏草",
                        "• 45% 概率获得香菜种子",
                        "• 两种种子可同时掉落",
                        "",
                        "种植：",
                        "• 右键耕地种植",
                        "• 生长速度与小麦相同",
                        "• 完全成熟后收获"
                )
        ));

        // 第 4 页：刀具切割（基础食材）
        pages.add(new PageContent(
                "=== 刀具切割（基础食材）===",
                List.of(
                        "生猪肉 → 切好的猪肉（25 刀）",
                        "生鸡肉 → 切好的鸡肉（20 刀）",
                        "葱 → 切好的葱（12 刀）",
                        "红辣椒 → 切好的红辣椒（8 刀）",
                        "生菜 → 切好的青菜（4 刀）",
                        "",
                        "切割方法：",
                        "• 食材放在菜板上",
                        "• 用刀具右键切割",
                        "• 切到最后一刀后",
                        "• 空手右键收集成品",
                        "• 刀具右键不会获得"
                )
        ));

        // 第 5 页：刀具切割（进阶食材）
        pages.add(new PageContent(
                "=== 刀具切割（进阶食材）===",
                List.of(
                        "青辣椒 → 青辣椒片（8 刀）",
                        "香菜 → 香菜段（7 刀）",
                        "原版牛肉 → 牛肉片（9 刀）",
                        "番茄 → 番茄片（4 刀）",
                        "",
                        "切割方法：",
                        "• 食材放在菜板上",
                        "• 用刀具右键切割",
                        "• 切到最后一刀后",
                        "• 空手右键收集成品",
                        "• 刀具右键不会获得"
                )
        ));

        // 第 6 页：炒锅使用
        pages.add(new PageContent(
                "=== 炒锅使用 ===",
                List.of(
                        "基础步骤：",
                        "",
                        "1. 放在点燃的炉灶上",
                        "2. 用油壶倒入植物油",
                        "3. 放入切好的食材",
                        "4. 用铲子翻炒 30 次",
                        "5. 等待 40 秒后用碗出锅",
                        "",
                        "注意：",
                        "• 40 秒后超过 20 秒会烧焦",
                        "• 烧焦后生成木炭",
                        "• 铲子可放在炒锅上"
                )
        ));

        // 第 7 页：辣子鸡配方
        pages.add(new PageContent(
                "=== 辣子鸡配方 ===",
                List.of(
                        "需要材料：",
                        "• 切好的鸡肉",
                        "• 切好的红辣椒",
                        "• 切好的葱",
                        "",
                        "步骤：",
                        "倒油→放鸡肉→放辣椒→放葱",
                        "翻炒 30 次→等待 40 秒",
                        "",
                        "用碗出锅，超时 20 秒烧焦"
                )
        ));

        // 第 8 页：小炒黄牛肉配方
        pages.add(new PageContent(
                "=== 小炒黄牛肉配方 ===",
                List.of(
                        "需要材料：",
                        "• 牛肉片",
                        "• 青辣椒片",
                        "• 切好的红辣椒",
                        "• 香菜段",
                        "",
                        "步骤：",
                        "倒油→放牛肉片→放青辣椒片→",
                        "放切好的红辣椒→放香菜段",
                        "翻炒 30 次→等待 40 秒",
                        "",
                        "用碗出锅，40 秒后超过 20 秒烧焦"
                )
        ));

        // 第 9 页：猪肉炒菜配方
        pages.add(new PageContent(
                "=== 猪肉炒菜配方 ===",
                List.of(
                        "需要材料：",
                        "• 切好的猪肉",
                        "• 切好的青菜",
                        "",
                        "步骤：",
                        "倒油→放切好猪肉→放切好的青菜",
                        "翻炒 30 次→等待 40 秒",
                        "",
                        "用碗出锅，40 秒后超过 20 秒烧焦"
                )
        ));

        // 第 10 页：番茄荷包蛋配方
        pages.add(new PageContent(
                "=== 番茄荷包蛋配方 ===",
                List.of(
                        "需要材料：",
                        "• 鸡蛋",
                        "• 番茄片",
                        "",
                        "步骤：",
                        "倒油→放鸡蛋→放番茄片",
                        "翻炒 6 次→等待 25 秒",
                        "",
                        "用碗出锅，25 秒后超过 20 秒烧焦"
                )
        ));

        // 第 11 页：蛋炒饭制作
        pages.add(new PageContent(
                "=== 蛋炒饭制作 ===",
                List.of(
                        "制作方法：",
                        "",
                        "• 使用森罗物语厨房的",
                        "  蛋炒饭物品",
                        "",
                        "• 副手持有蛋炒饭物品",
                        "",
                        "• 右键地面生成",
                        "  蛋炒饭模型",
                        "",
                        "• 用筷子食用",
                        "",
                        "无需炒锅，直接放置"
                )
        ));

        // 第 12 页：米饭放置
        pages.add(new PageContent(
                "=== 米饭放置 ===",
                List.of(
                        "获得米饭碗：",
                        "",
                        "• 使用森罗物语厨房的",
                        "  米饭物品",
                        "",
                        "• 副手持有米饭物品",
                        "",
                        "• 副手右键地面生成",
                        "  米饭模型",
                        "",
                        "• 可以用筷子夹米饭吃",
                        "",
                        "可放置在任意地面"
                )
        ));

        // 第 13 页：筷子使用
        pages.add(new PageContent(
                "=== 筷子使用 ===",
                List.of(
                        "筷子可以夹取食物：",
                        "",
                        "• 右键盘子夹菜",
                        "• 右键空气吃菜{可获得 buff,吃完整盘菜，有额外的奖励 buff 时长}",
                        "• 饱食度满时也能吃",
                        "• 筷子可重复使用",
                        "",
                        "特殊用法：",
                        "",
                        "• 可以右键放在空碗上，空手右键收回",
                        "• 可以右键放在空盘子上，空手右键收回",
                        "• 可以装饰美观"
                )
        ));

        // 第 14 页：铲子使用
        pages.add(new PageContent(
                "=== 铲子使用 ===",
                List.of(
                        "铲子用途：",
                        "",
                        "• 翻炒锅中食材",
                        "• 需要翻炒 30 次",
                        "• 某些菜品 6 次即可",
                        "不用担心，到了翻炒特定次数会有提示",
                        "",
                        "特殊功能：",
                        "",
                        "• 可以放在炒锅上",
                        "• 作为装饰美观",
                        "• 方便随时取用",
                        "",
                        "可重复使用"
                )
        ));

        // 第 15 页：烤鸡
        pages.add(new PageContent(
                "=== 烤鸡 ===",
                List.of(
                        "使用方法：",
                        "",
                        "• 右键放置在地上",
                        "• 空手右键吃掉",
                        "",
                        "烤鸡是可以食用的！",
                        "",
                        "⚠️ Fabric 端注意：",
                        "Fabric 版本暂时没有",
                        "烤鸡功能，敬请期待！"
                )
        ));
    }

    @Override
    protected void init() {
        super.init();

        int left = this.leftPos;
        int top = this.topPos;

        // 上一页按钮（书本外部下方）
        this.prevButton = this.addRenderableWidget(Button.builder(
                Component.literal("◀ 上一页"),
                button -> changePage(-1)
        ).bounds(left + 10, top + imageHeight + 5, 60, 20).build());

        // 下一页按钮（书本外部下方）
        this.nextButton = this.addRenderableWidget(Button.builder(
                Component.literal("下一页 ▶"),
                button -> changePage(1)
        ).bounds(left + imageWidth - 70, top + imageHeight + 5, 60, 20).build());

        // 关闭按钮（书本外部下方）
        this.closeButton = this.addRenderableWidget(Button.builder(
                Component.literal("关闭"),
                button -> this.minecraft.player.closeContainer()
        ).bounds(left + imageWidth / 2 - 25, top + imageHeight + 5, 50, 20).build());

        updateButtonVisibility();
    }

    private void changePage(int delta) {
        int newPage = currentPage + delta;
        if (newPage >= 0 && newPage < pages.size()) {
            currentPage = newPage;
            updateButtonVisibility();
        }
    }

    private void updateButtonVisibility() {
        this.prevButton.active = currentPage > 0;
        this.nextButton.active = currentPage < pages.size() - 1;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        // 绘制书本外框
        guiGraphics.fill(x, y, x + imageWidth, y + imageHeight, 0xFF8B4513);

        // 绘制页面背景（整页）
        guiGraphics.fill(x + 5, y + 5, x + imageWidth - 5, y + imageHeight - 5, 0xFFF5DEB3);

        // 绘制当前页内容
        renderPageContent(guiGraphics, x, y);

        // 绘制页码
        String pageText = (currentPage + 1) + " / " + pages.size();
        guiGraphics.drawString(
                this.font,
                pageText,
                x + imageWidth / 2 - this.font.width(pageText) / 2,
                y + imageHeight - 45,
                0xFF8B4513
        );
    }

    private void renderPageContent(GuiGraphics guiGraphics, int x, int y) {
        PageContent page = pages.get(currentPage);

        // 绘制标题
        String title = page.title();
        guiGraphics.drawString(
                this.font,
                title,
                x + imageWidth / 2 - this.font.width(title) / 2,
                y + 20,
                0xFF8B0000
        );

        // 绘制分隔线
        guiGraphics.fill(
                x + imageWidth / 4,
                y + 35,
                x + imageWidth * 3 / 4,
                y + 36,
                0xFF8B4513
        );

        // 绘制内容行
        int lineY = y + 45;
        for (String line : page.content()) {
            guiGraphics.drawString(
                    this.font,
                    line,
                    x + imageWidth / 2 - this.font.width(line) / 2,
                    lineY,
                    0xFF000000
            );
            lineY += 11;
        }
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // 不需要额外的标签
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY > 0) {
            changePage(-1);
        } else if (scrollY < 0) {
            changePage(1);
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // 左箭头键翻上一页
        if (keyCode == 263) { // GLFW_KEY_LEFT
            changePage(-1);
            return true;
        }
        // 右箭头键翻下一页
        if (keyCode == 262) { // GLFW_KEY_RIGHT
            changePage(1);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private record PageContent(String title, List<String> content) {
    }
}
