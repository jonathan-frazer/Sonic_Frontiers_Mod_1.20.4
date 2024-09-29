package net.sonicrushxii.beyondthehorizon.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;

public class VirtualSlotOverlay {
    private static final ResourceLocation BASEFORM_SONIC_ICON  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/baseform_sonic_icon.png");
    private static final ResourceLocation EMPTY_SLOT = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/empty_slot.png");
    private static final ResourceLocation MIRAGE_SLOT = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/mirage_slot.png");
    private static final ResourceLocation BOOST_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/boost_slot.png");
    private static final ResourceLocation CROSS_SLASH_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/cross_slash_slot.png");
    private static final ResourceLocation CYCLONE_KICK_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/cyclone_kick_slot.png");
    private static final ResourceLocation CYLOOP_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/cyloop_slot.png");
    private static final ResourceLocation DODGE_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/dodge_slot.png");
    private static final ResourceLocation HOMING_SHOT_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/homing_shot_slot.png");
    private static final ResourceLocation HOMING_ATTACK_SLOT = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/homing_slot.png");
    private static final ResourceLocation LIGHT_SPEED_ATTACK_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/light_speed_attack_slot.png");
    private static final ResourceLocation LOOPKICK_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/loopkick_slot.png");
    private static final ResourceLocation MELEE_ATTACK_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/melee_attack_slot.png");
    private static final ResourceLocation GRAND_SLAM_SLOT = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/grand_slam_slot.png");
    private static final ResourceLocation PARRY_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/parry_slot.png");
    private static final ResourceLocation PHANTOM_RUSH_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/phantom_rush_slot.png");
    private static final ResourceLocation POWER_BOOST_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/power_boost_slot.png");
    private static final ResourceLocation SMASH_HIT_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/smash_hit_slot.png");
    private static final ResourceLocation SONIC_BOOM_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/sonic_boom_slot.png");
    private static final ResourceLocation SONIC_WIND_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/sonic_wind_slot.png");
    private static final ResourceLocation SPEED_BLITZ_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/speed_blitz_slot.png");
    private static final ResourceLocation SPINSLASH_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/spinslash_slot.png");
    private static final ResourceLocation STOMP_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/stomp_slot.png");
    private static final ResourceLocation TORNADO_JUMP_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/tornado_jump_slot.png");
    private static final ResourceLocation WILDRUSH_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/wildrush_slot.png");

    public static final IGuiOverlay ABILITY_HUD = ((ForgeGui gui, GuiGraphics guiComponent, float partialTick, int screenWidth, int screenHeight)-> {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        switch(ClientFormData.getPlayerForm())
        {
            case BASEFORM -> renderBaseFormSlots(gui,guiComponent,partialTick,screenWidth,screenHeight);
            case SUPERFORM -> renderSuperFormSlots(gui,guiComponent,partialTick,screenWidth,screenHeight);
            case STARFALLFORM -> renderStarfallFormSlots(gui,guiComponent,partialTick,screenWidth,screenHeight);
            case HYPERFORM -> renderHyperFormSlots(gui,guiComponent,partialTick,screenWidth,screenHeight);
        }
    });

    public static void renderBaseFormSlots(ForgeGui gui, GuiGraphics guiComponent, float partialTick, int screenWidth, int screenHeight)
    {
        final int[] textureDimensions = {22,22};
        int x = textureDimensions[0]; //screenWidth  - (int)(textureDimensions[0]*1.5);
        int y = 0;

    }
    public static void renderSuperFormSlots(ForgeGui gui, GuiGraphics guiComponent, float partialTick, int screenWidth, int screenHeight)
    {
        final int[] textureDimensions = {22,22};
        int x = textureDimensions[0]; //screenWidth  - (int)(textureDimensions[0]*1.5);
        int y = 0;

    }
    public static void renderStarfallFormSlots(ForgeGui gui, GuiGraphics guiComponent, float partialTick, int screenWidth, int screenHeight)
    {
        final int[] textureDimensions = {22,22};
        int x = textureDimensions[0]; //screenWidth  - (int)(textureDimensions[0]*1.5);
        int y = 0;

    }
    public static void renderHyperFormSlots(ForgeGui gui, GuiGraphics guiComponent, float partialTick, int screenWidth, int screenHeight)
    {
        final int[] textureDimensions = {22,22};
        int x = textureDimensions[0]; //screenWidth  - (int)(textureDimensions[0]*1.5);
        int y = 0;

    }
}
