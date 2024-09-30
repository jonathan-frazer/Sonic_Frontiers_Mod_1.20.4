package net.sonicrushxii.beyondthehorizon.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.KeyBindings;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformActiveAbility;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformProperties;

import java.util.Arrays;
import java.util.List;

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

    //Helper Methods and Records
    private record Ability(ResourceLocation texture, byte cooldown) {}
    private static void renderSlot(Ability SLOT_ABILITY, GuiGraphics guiComponent, int x, int y, int[] textureDimensions)
    {
        ResourceLocation SLOT_TEXTURE = SLOT_ABILITY.texture();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        //Checks if On Cooldown or Not
        if(SLOT_ABILITY.cooldown() != (byte)0)  RenderSystem.setShaderColor(0.1F, 0.1F, 0.1F, 0.2F);
        else                                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, SLOT_TEXTURE);

        //Draw the Actual Texture
        guiComponent.blit(SLOT_TEXTURE,x,y,
                0,0,textureDimensions[0],textureDimensions[1],textureDimensions[0],textureDimensions[1]);
    }
    private static String shortenName(String currName)
    {
        return switch (currName) {
            case "Right Button" -> "RMB";
            default -> currName;
        };
    }

    //Register the Main Overlay
    public static final IGuiOverlay ABILITY_HUD = ((ForgeGui gui, GuiGraphics guiComponent, float partialTick, int screenWidth, int screenHeight)-> {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        switch(ClientFormData.getPlayerForm())
        {
            case BASEFORM -> renderBaseFormSlots(player,gui,guiComponent,partialTick,screenWidth,screenHeight);
            case SUPERFORM -> renderSuperFormSlots(player,gui,guiComponent,partialTick,screenWidth,screenHeight);
            case STARFALLFORM -> renderStarfallFormSlots(player,gui,guiComponent,partialTick,screenWidth,screenHeight);
            case HYPERFORM -> renderHyperFormSlots(player,gui,guiComponent,partialTick,screenWidth,screenHeight);
        }
    });

    public static void renderBaseFormSlots(LocalPlayer player, ForgeGui gui, GuiGraphics guiComponent, float partialTick, int screenWidth, int screenHeight)
    {
        final int[] textureDimensions = {22,22};
        int x = textureDimensions[0]; //screenWidth  - (int)(textureDimensions[0]*1.5);
        int y = 0;

        BaseformProperties baseformProperties = (BaseformProperties) ClientFormData.getPlayerFormDetails();
        byte[] cooldownArray = baseformProperties.getAllCooldowns();

        String slotName;
        List<Ability> iconTextures;
        List<InputConstants.Key> keyBindings = Arrays.asList(
                KeyBindings.INSTANCE.useAbility1.getKey(),
                KeyBindings.INSTANCE.useAbility2.getKey(),
                KeyBindings.INSTANCE.useAbility3.getKey(),
                KeyBindings.INSTANCE.useAbility4.getKey(),
                KeyBindings.INSTANCE.useAbility5.getKey(),
                KeyBindings.INSTANCE.useAbility6.getKey()
        );

        //Creates a List of all the Ability
        switch(VirtualSlotHandler.getCurrAbility())
        {
            case 0 :
                slotName = "Boost";
                iconTextures = (Arrays.asList(
                        new Ability(BOOST_SLOT,cooldownArray[BaseformActiveAbility.BOOST.ordinal()]),
                        new Ability(LIGHT_SPEED_ATTACK_SLOT,cooldownArray[BaseformActiveAbility.LIGHT_SPEED_ATTACK.ordinal()]),
                        new Ability(POWER_BOOST_SLOT,cooldownArray[BaseformActiveAbility.POWER_BOOST.ordinal()]))
                );
                break;

            case 1 :
                slotName = "Combo";
                iconTextures = (Arrays.asList(
                        new Ability(HOMING_ATTACK_SLOT,cooldownArray[BaseformActiveAbility.HOMING_ATTACK.ordinal()]),
                        new Ability(DODGE_SLOT,cooldownArray[BaseformActiveAbility.DODGE.ordinal()]),
                        new Ability(MELEE_ATTACK_SLOT,cooldownArray[BaseformActiveAbility.MELEE_ATTACK.ordinal()]),
                        new Ability(SPEED_BLITZ_SLOT,cooldownArray[BaseformActiveAbility.SPEED_BLITZ.ordinal()]),
                        new Ability(SMASH_HIT_SLOT,cooldownArray[BaseformActiveAbility.SMASH_HIT.ordinal()]))
                );
                break;

            case 2 :
                slotName = "Melee";
                iconTextures = (Arrays.asList(
                        new Ability(TORNADO_JUMP_SLOT,cooldownArray[BaseformActiveAbility.TORNADO_JUMP.ordinal()]),
                        new Ability(SPINSLASH_SLOT,cooldownArray[BaseformActiveAbility.SPINSLASH.ordinal()]),
                        new Ability(WILDRUSH_SLOT,cooldownArray[BaseformActiveAbility.WILDRUSH.ordinal()]),
                        new Ability(LOOPKICK_SLOT,cooldownArray[BaseformActiveAbility.LOOPKICK.ordinal()]),
                        new Ability(STOMP_SLOT,cooldownArray[BaseformActiveAbility.STOMP.ordinal()])
                ));
                if(player.isShiftKeyDown()){
                    iconTextures.set(0,new Ability(MIRAGE_SLOT,cooldownArray[BaseformActiveAbility.MIRAGE.ordinal()]));
                    iconTextures.set(1,new Ability(CYCLONE_KICK_SLOT,cooldownArray[BaseformActiveAbility.CYCLONE_KICK.ordinal()]));
                }
                break;

            case 3 :
                slotName = "Ranged";
                iconTextures = (Arrays.asList(
                        new Ability(SONIC_BOOM_SLOT,cooldownArray[BaseformActiveAbility.SONIC_BOOM.ordinal()]),
                        new Ability(CROSS_SLASH_SLOT,cooldownArray[BaseformActiveAbility.CROSS_SLASH.ordinal()]),
                        new Ability(SONIC_WIND_SLOT,cooldownArray[BaseformActiveAbility.SONIC_WIND.ordinal()]),
                        new Ability(HOMING_SHOT_SLOT,cooldownArray[BaseformActiveAbility.HOMING_SHOT.ordinal()]))
                );
                break;

            case 4 :
                slotName = "Parry";
                iconTextures = (Arrays.asList(
                        new Ability(PARRY_SLOT,cooldownArray[BaseformActiveAbility.PARRY.ordinal()]),
                        new Ability(GRAND_SLAM_SLOT,cooldownArray[BaseformActiveAbility.GRAND_SLAM.ordinal()]))
                );
                break;

            case 5 :
                slotName = "Cyloop";
                iconTextures = (Arrays.asList(
                        new Ability(CYLOOP_SLOT,cooldownArray[BaseformActiveAbility.CYLOOP.ordinal()])
                )
                );
                keyBindings.set(0,KeyBindings.INSTANCE.useSingleAbility.getKey());
                break;

            case 6 :
                slotName = "Ultimate";
                iconTextures = (Arrays.asList(
                        new Ability(PHANTOM_RUSH_SLOT,cooldownArray[BaseformActiveAbility.PHANTOM_RUSH.ordinal()])
                ));
                keyBindings.set(0,KeyBindings.INSTANCE.useSingleAbility.getKey());
                break;

            default:
                slotName = "NULL";
                iconTextures = (Arrays.asList(
                        new Ability(EMPTY_SLOT,(byte)0),
                        new Ability(EMPTY_SLOT,(byte)0),
                        new Ability(EMPTY_SLOT,(byte)0),
                        new Ability(EMPTY_SLOT,(byte)0),
                        new Ability(EMPTY_SLOT,(byte)0),
                        new Ability(EMPTY_SLOT,(byte)0))
                );
        }

        //Render Slot Name
        guiComponent.drawCenteredString(Minecraft.getInstance().font, slotName,
                x+textureDimensions[0]/3,
                y+textureDimensions[1]/3,
                0xFFFFFF);

        for(int i=1;i<=iconTextures.size();++i)
        {
            //Slot Icon
            renderSlot(iconTextures.get(i-1),guiComponent,
                    x,y+i*textureDimensions[1],textureDimensions);

            //Keybinding for Ability
            guiComponent.drawCenteredString(Minecraft.getInstance().font, shortenName(keyBindings.get(i-1).getDisplayName().getString()),
                    x - textureDimensions[0]/2,
                    y + textureDimensions[1]/2 + i*textureDimensions[1],
                    0xFFFFFF);
        }

    }
    public static void renderSuperFormSlots(LocalPlayer player, ForgeGui gui, GuiGraphics guiComponent, float partialTick, int screenWidth, int screenHeight)
    {
        final int[] textureDimensions = {22,22};
        int x = textureDimensions[0]; //screenWidth  - (int)(textureDimensions[0]*1.5);
        int y = 0;

    }
    public static void renderStarfallFormSlots(LocalPlayer player, ForgeGui gui, GuiGraphics guiComponent, float partialTick, int screenWidth, int screenHeight)
    {
        final int[] textureDimensions = {22,22};
        int x = textureDimensions[0]; //screenWidth  - (int)(textureDimensions[0]*1.5);
        int y = 0;

    }
    public static void renderHyperFormSlots(LocalPlayer player, ForgeGui gui, GuiGraphics guiComponent, float partialTick, int screenWidth, int screenHeight)
    {
        final int[] textureDimensions = {22,22};
        int x = textureDimensions[0]; //screenWidth  - (int)(textureDimensions[0]*1.5);
        int y = 0;

    }
}
