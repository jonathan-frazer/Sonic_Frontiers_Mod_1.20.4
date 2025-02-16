package net.sonicrushxii.beyondthehorizon.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.KeyBindings;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.HelpScreenSync;
import org.lwjgl.glfw.GLFW;

public class HelpScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui."+BeyondTheHorizon.MOD_ID+".sonic_help_screen.title");
    private static final int OFFSET = 5;
    private static final int MAX_SLOT = 25;

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

    public HelpScreen(int scrollIdx) {
        super(TITLE);

        this.scrollIdx = scrollIdx;
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
        sb.append("0".repeat(Math.max(0, ((byte) Math.log10(MAX_SLOT) - (byte) Math.log10(num)))));
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
            case "<": this.scrollIdx =(this.scrollIdx == 0)  ?   MAX_SLOT    :   this.scrollIdx-1;   break;
            case ">": this.scrollIdx = (this.scrollIdx == MAX_SLOT)  ?   0   :   this.scrollIdx+1;   break;
        }
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

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Close screen when <HelpButton> is pressed
        if (keyCode == KeyBindings.INSTANCE.helpButton.getKey().getValue()) {
            PacketHandler.sendToServer(new HelpScreenSync(this.scrollIdx));
            Minecraft.getInstance().setScreen(null);
            return true;
        }

        //Scroll Left and Right
        else if(keyCode == GLFW.GLFW_KEY_LEFT  || keyCode == GLFW.GLFW_KEY_A)
            this.scrollIdx = (this.scrollIdx == 0)  ?   MAX_SLOT   :   this.scrollIdx-1;
        else if(keyCode == GLFW.GLFW_KEY_RIGHT || keyCode == GLFW.GLFW_KEY_D)
            this.scrollIdx = (this.scrollIdx == MAX_SLOT)  ?   0   :   this.scrollIdx+1;

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
