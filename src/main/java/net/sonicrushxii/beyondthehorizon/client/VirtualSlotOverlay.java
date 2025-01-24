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
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformActiveAbility;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;

import javax.annotation.Nullable;
import java.util.ArrayList;
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
    private static final ResourceLocation HOMING_SHOT_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/homing_shot_slot.png");
    private static final ResourceLocation HOMING_ATTACK_SLOT = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/homing_slot.png");
    private static final ResourceLocation LIGHT_SPEED_ATTACK_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/light_speed_attack_slot.png");
    private static final ResourceLocation LIGHT_SPEED_ATTACK_ACTIVE  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/light_speed_attack_active.png");
    private static final ResourceLocation LOOPKICK_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/loopkick_slot.png");
    private static final ResourceLocation HUMMING_TOP_SLOT = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/humming_top_slot.png");
    private static final ResourceLocation GRAND_SLAM_SLOT = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/grand_slam_slot.png");
    private static final ResourceLocation PARRY_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/parry_slot.png");
    private static final ResourceLocation PHANTOM_RUSH_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/phantom_rush_slot.png");
    private static final ResourceLocation POWER_BOOST_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/power_boost_slot.png");
    private static final ResourceLocation POWER_BOOST_ACTIVE  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/power_boost_active.png");
    private static final ResourceLocation SMASH_HIT_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/smash_hit_slot.png");
    private static final ResourceLocation SMASH_HIT_ACTIVE  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/smash_hit_active.png");
    private static final ResourceLocation SONIC_BOOM_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/sonic_boom_slot.png");
    private static final ResourceLocation SONIC_WIND_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/sonic_wind_slot.png");
    private static final ResourceLocation SPEED_BLITZ_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/speed_blitz_slot.png");
    private static final ResourceLocation SPEED_BLITZ_ACTIVE  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/speed_blitz_active.png");
    private static final ResourceLocation SPINSLASH_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/spinslash_slot.png");
    private static final ResourceLocation STOMP_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/stomp_slot.png");
    private static final ResourceLocation TORNADO_JUMP_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/tornado_jump_slot.png");
    private static final ResourceLocation WILDRUSH_SLOT  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/custom_gui/baseform/wildrush_slot.png");

    private static final ResourceLocation GOOD_100  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/combo_system/100_good.png");
    private static final ResourceLocation NICE_200  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/combo_system/200_nice.png");
    private static final ResourceLocation GREAT_300  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/combo_system/300_great.png");
    private static final ResourceLocation JAMMIN_400  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/combo_system/400_jammin.png");
    private static final ResourceLocation COOL_500  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/combo_system/500_cool.png");
    private static final ResourceLocation RADICAL_600  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/combo_system/600_radical.png");
    private static final ResourceLocation TIGHT_800  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/combo_system/800_tight.png");
    private static final ResourceLocation AWESOME_1000  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/combo_system/1000_awesome.png");
    private static final ResourceLocation EXTREME_1500  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/combo_system/1500_extreme.png");
    private static final ResourceLocation PERFECT_2000  = new ResourceLocation(BeyondTheHorizon.MOD_ID,
            "textures/combo_system/2000_perfect.png");

    //Helper Methods and Records
    private record Ability(ResourceLocation texture, @Nullable String sideinfo, byte cooldown, @Nullable Double chargePercent, @Nullable Integer barAlphaColor) {}

    private static final int SLOT_BAR_WIDTH = 15;
    private static final int SLOT_BAR_HEIGHT = 1;

    private static final int ULT_BAR_WIDTH = 90;
    private static final int ULT_BAR_HEIGHT = 5;

    private static void renderSlot(Ability SLOT_ABILITY, GuiGraphics guiComponent, int x, int y, int[] textureDimensions)
    {
        ResourceLocation SLOT_TEXTURE = SLOT_ABILITY.texture();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        //Checks if On Cooldown or Not
        if(SLOT_ABILITY.cooldown() == (byte)0) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
        else{
            RenderSystem.setShaderColor(0.1F, 0.1F, 0.1F, 0.2F);
            guiComponent.drawString(Minecraft.getInstance().font,
                    (SLOT_ABILITY.cooldown<10)?"0"+SLOT_ABILITY.cooldown:""+SLOT_ABILITY.cooldown,
                    x+3*textureDimensions[0]/10,y+2*textureDimensions[1]/5,0xFFFFFF);
        }
        RenderSystem.setShaderTexture(0, SLOT_TEXTURE);

        //Draw the Actual Texture
        guiComponent.blit(SLOT_TEXTURE,x,y,
                0,0,textureDimensions[0],textureDimensions[1],textureDimensions[0],textureDimensions[1]);

        //Draw Bar over
        //Cyloop
        if(SLOT_ABILITY.chargePercent() != null && SLOT_ABILITY.barAlphaColor() != null){
            final int barX = x+3*textureDimensions[0]/10-2;
            final int barY = y+2*textureDimensions[1]/5+10;
            int barWidth = (int) (0.01*SLOT_ABILITY.chargePercent()* SLOT_BAR_WIDTH);
            guiComponent.fill(barX,barY, barX+ SLOT_BAR_WIDTH, barY- SLOT_BAR_HEIGHT,0xFF000000);
            guiComponent.fill(barX,barY, barX+barWidth, barY- SLOT_BAR_HEIGHT,SLOT_ABILITY.barAlphaColor());
        }


        //Additional Info
        if(SLOT_ABILITY.sideinfo != null)
            guiComponent.drawString(Minecraft.getInstance().font, SLOT_ABILITY.sideinfo,
                    x+5*textureDimensions[0]/4,y+textureDimensions[1]/3,0xFFFFFF);

        //Return Render Color back to same
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
    private static String shortenName(String currName)
    {
        return switch (currName) {
            case "Right Button" -> "RMB";
            case "Backspace" -> "BkSpc";
            case "Caps Lock" -> "CAPS";
            case "Left Shift" -> "LShft";
            case "Right Shift" -> "RShft";
            case "Enter" -> "ENT";
            case "Left Alt" -> "LAlt";
            case "Right ALt" -> "RAlt";
            default -> currName;
        };
    }

    //Register the Main Overlay
    public static final IGuiOverlay ABILITY_HUD = ((ForgeGui gui, GuiGraphics guiComponent, float partialTick, int screenWidth, int screenHeight)-> {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
            switch(playerSonicForm.getCurrentForm())
            {
                case BASEFORM -> renderBaseFormSlots(player,gui,guiComponent,partialTick,screenWidth,screenHeight);
                case SUPERFORM -> renderSuperFormSlots(player,gui,guiComponent,partialTick,screenWidth,screenHeight);
                case STARFALLFORM -> renderStarfallFormSlots(player,gui,guiComponent,partialTick,screenWidth,screenHeight);
                case HYPERFORM -> renderHyperFormSlots(player,gui,guiComponent,partialTick,screenWidth,screenHeight);
            }
        });

    });

    public static void renderBaseFormSlots(LocalPlayer player, ForgeGui gui, GuiGraphics guiComponent, float partialTick, int screenWidth, int screenHeight)
    {
        final int[] textureDimensions = {22,22};
        int x = textureDimensions[0]; //screenWidth  - (int)(textureDimensions[0]*1.5);
        int y = 0;

        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            byte[] cooldownArray = baseformProperties.getAllCooldowns();

            //Render Slot Ability
            {
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
                switch (VirtualSlotHandler.getCurrAbility()) {
                    case 0:
                        slotName = "Boost";
                        iconTextures = (Arrays.asList(
                                new Ability(BOOST_SLOT, "Lv. " + baseformProperties.boostLvl, cooldownArray[BaseformActiveAbility.BOOST.ordinal()], null, null),
                                new Ability((baseformProperties.lightSpeedState == 2) ? LIGHT_SPEED_ATTACK_ACTIVE : LIGHT_SPEED_ATTACK_SLOT, null, cooldownArray[BaseformActiveAbility.LIGHT_SPEED_ATTACK.ordinal()], null, null),
                                new Ability((baseformProperties.powerBoost) ? POWER_BOOST_ACTIVE : POWER_BOOST_SLOT, null, cooldownArray[BaseformActiveAbility.POWER_BOOST.ordinal()], null, null),
                                new Ability(CYLOOP_SLOT, null, cooldownArray[BaseformActiveAbility.CYLOOP.ordinal()], baseformProperties.qkCyloopMeter, 0xFF00DDDD))
                        );
                        keyBindings.set(3, KeyBindings.INSTANCE.useSingleAbility.getKey());
                        break;

                    case 1:
                        slotName = "Combo";
                        iconTextures = (Arrays.asList(
                                new Ability(HOMING_ATTACK_SLOT, null, cooldownArray[BaseformActiveAbility.HOMING_ATTACK.ordinal()], null, null),
                                new Ability(HUMMING_TOP_SLOT, null, cooldownArray[BaseformActiveAbility.HUMMING_TOP.ordinal()], null, null),
                                new Ability((baseformProperties.speedBlitz) ? SPEED_BLITZ_ACTIVE : SPEED_BLITZ_SLOT, null, cooldownArray[BaseformActiveAbility.SPEED_BLITZ.ordinal()], null, null),
                                new Ability((baseformProperties.smashHit == 0) ? SMASH_HIT_SLOT : SMASH_HIT_ACTIVE, null, cooldownArray[BaseformActiveAbility.SMASH_HIT.ordinal()], null, null),
                                new Ability(STOMP_SLOT, null, cooldownArray[BaseformActiveAbility.STOMP.ordinal()], null, null)
                        ));
                        break;

                    case 2:
                        slotName = "Melee";
                        iconTextures = (Arrays.asList(
                                new Ability(TORNADO_JUMP_SLOT, null, cooldownArray[BaseformActiveAbility.TORNADO_JUMP.ordinal()], null, null),
                                new Ability(SPINSLASH_SLOT, null, cooldownArray[BaseformActiveAbility.SPINSLASH.ordinal()], null, null),
                                new Ability(WILDRUSH_SLOT, null, cooldownArray[BaseformActiveAbility.WILDRUSH.ordinal()], null, null),
                                new Ability(LOOPKICK_SLOT, null, cooldownArray[BaseformActiveAbility.LOOPKICK.ordinal()], null, null)
                        ));
                        if (player.isShiftKeyDown()) {
                            iconTextures.set(0, new Ability(MIRAGE_SLOT, null, cooldownArray[BaseformActiveAbility.MIRAGE.ordinal()], null, null));
                            iconTextures.set(1, new Ability(CYCLONE_KICK_SLOT, null, cooldownArray[BaseformActiveAbility.CYCLONE_KICK.ordinal()], null, null));
                        }
                        break;

                    case 3:
                        slotName = "Ranged";
                        iconTextures = (Arrays.asList(
                                new Ability(SONIC_BOOM_SLOT, null, cooldownArray[BaseformActiveAbility.SONIC_BOOM.ordinal()], null, null),
                                new Ability(CROSS_SLASH_SLOT, null, cooldownArray[BaseformActiveAbility.CROSS_SLASH.ordinal()], null, null),
                                new Ability(SONIC_WIND_SLOT, null, cooldownArray[BaseformActiveAbility.SONIC_WIND.ordinal()], null, null),
                                new Ability(HOMING_SHOT_SLOT, null, cooldownArray[BaseformActiveAbility.HOMING_SHOT.ordinal()], null, null))
                        );
                        break;

                    case 4:
                        slotName = "Counter";
                        iconTextures = (Arrays.asList(
                                new Ability(GRAND_SLAM_SLOT, null, cooldownArray[BaseformActiveAbility.GRAND_SLAM.ordinal()], null, null))
                        );
                        keyBindings.set(0, KeyBindings.INSTANCE.useSingleAbility.getKey());
                        break;

                    case 5:
                        slotName = "Transform";
                        iconTextures = new ArrayList<Ability>();
                        break;

                    default:
                        slotName = "NULL";
                        iconTextures = (Arrays.asList(
                                new Ability(EMPTY_SLOT, null, (byte) 0, null, null),
                                new Ability(EMPTY_SLOT, null, (byte) 0, null, null),
                                new Ability(EMPTY_SLOT, null, (byte) 0, null, null),
                                new Ability(EMPTY_SLOT, null, (byte) 0, null, null),
                                new Ability(EMPTY_SLOT, null, (byte) 0, null, null),
                                new Ability(EMPTY_SLOT, null, (byte) 0, null, null))
                        );
                }

                //Render Slot Name
                guiComponent.drawCenteredString(Minecraft.getInstance().font, slotName,
                        x + textureDimensions[0] / 3,
                        y + textureDimensions[1] / 3,
                        0xFFFFFF);

                for (int i = 1; i <= iconTextures.size(); ++i) {
                    //Slot Icon
                    renderSlot(iconTextures.get(i - 1), guiComponent,
                            x, y + i * textureDimensions[1], textureDimensions);

                    //Keybinding for Ability
                    guiComponent.drawCenteredString(Minecraft.getInstance().font, shortenName(keyBindings.get(i - 1).getDisplayName().getString()),
                            x - textureDimensions[0] / 2,
                            y + textureDimensions[1] / 2 + i * textureDimensions[1],
                            0xFFFFFF);
                }
            }

            //Render Ultimate Bar
            {
                final int imageWidth = 16;
                final int imageHeight = 16;

                final int barX = (screenWidth - ULT_BAR_WIDTH + imageWidth + shortenName(KeyBindings.INSTANCE.useUltimateAbility.getKey().getDisplayName().getString()).length()*5 - 4) / 2; // Center horizontally
                int noOfHealthBars = (int)Math.ceil(((int)Math.ceil(player.getAbsorptionAmount())+(int)Math.ceil(player.getMaxHealth()))/20.0);
                final int barY = screenHeight - Math.min(2*screenHeight/5,((5+noOfHealthBars)*screenHeight/24));

                int barWidth = (int) (0.01*baseformProperties.ultimateAtkMeter*ULT_BAR_WIDTH);
                guiComponent.fill(barX+1,barY+1, barX+ULT_BAR_WIDTH+1, barY-ULT_BAR_HEIGHT,0xFF000000);
                guiComponent.fill(barX,barY, barX+ULT_BAR_WIDTH, barY-ULT_BAR_HEIGHT,0xFF444444);
                guiComponent.fill(barX,barY, barX+barWidth, barY-ULT_BAR_HEIGHT,(baseformProperties.ultReady)?0xFF0033DD:0xFF00DDDD);

                // Draw the image (Assuming you have a method to draw the image)
                int imageX = barX - imageWidth - 5; // Image is to the left of the bar
                int imageY = barY - (ULT_BAR_HEIGHT + imageHeight) / 2; // Center the image vertically with the bar
                //Draw the Actual Texture
                guiComponent.blit(PHANTOM_RUSH_SLOT,imageX,imageY,
                        0,0,imageWidth,imageHeight,imageWidth,imageHeight);

                if(baseformProperties.ultReady)
                {
                    int keyX = imageX - shortenName(KeyBindings.INSTANCE.useUltimateAbility.getKey().getDisplayName().getString()).length()*4 - 4;
                    int keyY = imageY + imageHeight/3;

                    //Keybinding for Ability
                    guiComponent.drawCenteredString(Minecraft.getInstance().font,
                            shortenName(KeyBindings.INSTANCE.useUltimateAbility.getKey().getDisplayName().getString()),
                            keyX,
                            keyY,
                            0xFFFFFF);
                }
            }

            //Render Combo System
            {
                // Render the texture
                int imageWidth = 100;  // Desired width of the rendered combo points
                int imageHeight = 60; // Desired height of the rendered combo points

                int comboX = screenWidth - imageWidth; // Adjust as needed
                int comboY = screenHeight / 2 - (imageHeight); // Slightly above the cursor

                // Render your texture
                switch(Math.abs(baseformProperties.comboPointDisplay))
                {
                    case 0: break;
                    case 1: guiComponent.blit(GOOD_100, comboX, comboY, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);break;
                    case 2: guiComponent.blit(NICE_200, comboX, comboY, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);break;
                    case 3: guiComponent.blit(GREAT_300, comboX, comboY, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);break;
                    case 4: guiComponent.blit(JAMMIN_400, comboX, comboY, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);break;
                    case 5: guiComponent.blit(COOL_500, comboX, comboY, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);break;
                    case 6: guiComponent.blit(RADICAL_600, comboX, comboY, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);break;
                    case 7: guiComponent.blit(TIGHT_800, comboX, comboY, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);break;
                    case 8: guiComponent.blit(AWESOME_1000, comboX, comboY, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);break;
                    case 9: guiComponent.blit(EXTREME_1500, comboX, comboY, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight); break;
                    default: guiComponent.blit(PERFECT_2000, comboX, comboY, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
                }
            }
        });

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
