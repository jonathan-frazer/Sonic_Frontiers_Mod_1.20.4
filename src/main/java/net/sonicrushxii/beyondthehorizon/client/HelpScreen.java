package net.sonicrushxii.beyondthehorizon.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;

public class HelpScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui."+ BeyondTheHorizon.MOD_ID+".example_block_screen");
    private static final Component EXAMPLE_BUTTON = Component.translatable("gui."+BeyondTheHorizon.MOD_ID+".example_block_screen.button.example_button");
    private static final ResourceLocation TEXTURE = new ResourceLocation(BeyondTheHorizon.MOD_ID,"textures/gui/example_block_screen.png");
    private static final int OFFSET = 5;
    private static final int MAX_SLOT = 10;

    private Button button;
    private Button leftArrow;
    private Button rightArrow;

    private final int imageWidth, imageHeight;
    private int leftPos, topPos, scrollIdx;

    public HelpScreen() {
        super(TITLE);

        this.scrollIdx = 0;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        // Create left arrow button
        this.leftArrow = this.addRenderableWidget(
                Button.builder(
                        Component.literal("<"),
                        this::handlePageTurn)
                        .bounds(this.leftPos-20-OFFSET,this.topPos+(this.imageHeight/2)-10, 20, 20)
                        .build());

        // Create right arrow button
        this.rightArrow = this.addRenderableWidget(
                Button.builder(
                                Component.literal(">"),
                                this::handlePageTurn)
                        .bounds(this.leftPos+this.imageWidth+OFFSET,this.topPos+(this.imageHeight/2)-10, 20, 20)
                        .build());
    }

    private ResourceLocation getTexture()
    {
        return TEXTURE;
    }

    private void handlePageTurn(Button button)
    {
        switch(button.getMessage().getString())
        {
            case "<": this.scrollIdx =(this.scrollIdx ==0)  ?   MAX_SLOT    :   this.scrollIdx -1;   break;
            case ">": this.scrollIdx =(this.scrollIdx % MAX_SLOT)   +   1;                           break;
        }
        System.out.println("Scroll Pos" + this.scrollIdx);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.blit(getTexture(), this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        guiGraphics.drawString(this.font, TITLE, this.leftPos + 8, this.topPos + 8, 0x404040, false);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
