package net.sonicrushxii.beyondthehorizon.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;

public class HelpScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui."+BeyondTheHorizon.MOD_ID+".sonic_help_screen.title");
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

    public static String padNumber(int num)
    {
        StringBuilder sb = new StringBuilder();

        //Pad 0's based on your max slot
        for(int i=0;i<((byte)Math.log10(MAX_SLOT)-(byte)Math.log10(num));++i)
            sb.append('0');
        sb.append(num);

        return sb.toString();
    }

    private ResourceLocation getTexture()
    {
        return new ResourceLocation(BeyondTheHorizon.MOD_ID,"textures/help_screen_gui/sonic_block_screen_"+this.scrollIdx+".png");
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
        guiGraphics.drawString(this.font, padNumber(this.scrollIdx)+"/"+MAX_SLOT, this.leftPos + 17*this.imageWidth/40, this.topPos - 16, 0xFFFFFF, false);
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
