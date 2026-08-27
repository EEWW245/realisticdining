package com.example.realisticdining.client.screen;

import com.example.realisticdining.menu.EatRiceGuideMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EatRiceGuideScreen extends AbstractContainerScreen<EatRiceGuideMenu> {

    private static final int BOOK_WIDTH = 320;
    private static final int BOOK_HEIGHT = 220;

    private int currentPage = 0;
    private final List<PageContent> pages = new ArrayList<>();

    private Button prevButton;
    private Button nextButton;
    private Button closeButton;

    public EatRiceGuideScreen(EatRiceGuideMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = BOOK_WIDTH;
        this.imageHeight = BOOK_HEIGHT;
        initPages();
    }

    private void initPages() {
        pages.add(new PageContent(
                "=== 吃米饭动画说明 ===",
                List.of(
                        "把森罗物语的米饭物品切换到副手，会显示手臂拿着米饭的3D模型，",
                        "然后主手拿着本模组的筷子物品，按一次T建可播放一次吃饭动画，",
                        "T键是默认的，可在按键绑定配置那里更改。",
                        "也可以按y键（默认）关闭本模组手臂以及吃米饭动画。"
                )
        ));

        pages.add(new PageContent(
                "=== 进食动画说明 ===",
                List.of(
                        "把模组内的饮料和零食，",
                        "用右手拿着，按U键（可改），",
                        "可播进食放动画。",
                        "瓶装饮料可以喝两次，",
                        "剩下的只能喝一次。",
                        "放置自动购货机的3D模型，",
                        "右键打开购买界面，",
                        "并用金粒购买。",
                        "每一种物品可购买六次，",
                        "第二天刷新。"
                )
        ));

        pages.add(new PageContent(
                "=== 模组兼容性 ===",
                List.of(
                        "进食动画已完成对以下模组的兼容：",
                        "更真实的第一人称模型，史诗战斗，punchy，",
                        "ysm（是，史蒂夫模型），",
                        "Forge/NeoForge端的Hold My Items - Reforged",
                        "注意：1.fabric端对更真实的第一人称模型",
                        "兼容效果不太好，可按按键绑定配置里的",
                        "更真实的第一人称模型切换第一人称按键。",
                        "2.与fabric端Hold My Items 不兼容，与另一款",
                        "Forge/NeoForge端的模组Hold My Items - ReFoxed不兼容"
                )
        ));

        pages.add(new PageContent(
                "=== 装饰性 ===",
                List.of(
                        "可以把模组内的饮料、零食切换到右手，",
                        "右键地面可放置展示3D模型，一个方块位置",
                        "大小可放置的数量为4种任意饮料或零食。",
                        "空手右键可以重新获得该物品。"
                )
        ));

        pages.add(new PageContent(
                "=== 最后要注意的是 ===",
                List.of(
                        "如果把森罗物语的米饭物品切换到副手，如果不显示手臂模型，",
                        "按对应的按键无法触发动画的话，",
                        "检查是否安装了我以上说的没有兼容到的其他手臂动画模组。",
                        "",
                        "如果有问题，可以在mc百科，b站上反馈。"
                )
        ));
    }

    @Override
    protected void init() {
        super.init();

        int left = this.leftPos;
        int top = this.topPos;

        this.prevButton = this.addRenderableWidget(Button.builder(
                Component.literal("◀ 上一页"),
                button -> changePage(-1)
        ).bounds(left + 10, top + imageHeight + 5, 60, 20).build());

        this.nextButton = this.addRenderableWidget(Button.builder(
                Component.literal("下一页 ▶"),
                button -> changePage(1)
        ).bounds(left + imageWidth - 70, top + imageHeight + 5, 60, 20).build());

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
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        guiGraphics.fill(x, y, x + imageWidth, y + imageHeight, 0xFF8B4513);

        guiGraphics.fill(x + 5, y + 5, x + imageWidth - 5, y + imageHeight - 5, 0xFFF5DEB3);

        renderPageContent(guiGraphics, x, y);

        String pageText = (currentPage + 1) + " / " + pages.size();
        guiGraphics.drawString(
                this.font,
                pageText,
                x + imageWidth / 2 - this.font.width(pageText) / 2,
                y + imageHeight - 45,
                0xFF8B4513,
                false
        );
    }

    private void renderPageContent(GuiGraphics guiGraphics, int x, int y) {
        PageContent page = pages.get(currentPage);

        String title = page.title();
        guiGraphics.drawString(
                this.font,
                title,
                x + imageWidth / 2 - this.font.width(title) / 2,
                y + 20,
                0xFF8B0000,
                false
        );

        guiGraphics.fill(
                x + imageWidth / 4,
                y + 35,
                x + imageWidth * 3 / 4,
                y + 36,
                0xFF8B4513
        );

        int lineY = y + 45;
        for (String line : page.content()) {
            guiGraphics.drawString(
                    this.font,
                    line,
                    x + 15,
                    lineY,
                    0xFF000000,
                    false
            );
            lineY += 10;
        }
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollAmount) {
        if (scrollAmount > 0) {
            changePage(-1);
        } else if (scrollAmount < 0) {
            changePage(1);
        }
        return super.mouseScrolled(mouseX, mouseY, scrollAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 263) {
            changePage(-1);
            return true;
        }
        if (keyCode == 262) {
            changePage(1);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private record PageContent(String title, List<String> content) {
    }
}
