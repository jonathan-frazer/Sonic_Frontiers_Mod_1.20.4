package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonicrushxii.beyondthehorizon.KeyBindings;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformActiveAbility;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.client.ClientFormData;
import net.sonicrushxii.beyondthehorizon.client.DoubleTapDirection;
import net.sonicrushxii.beyondthehorizon.client.VirtualSlotHandler;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.boost.*;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack.LightspeedCancel;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack.LightspeedCharge;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack.LightspeedDecay;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack.LightspeedEffect;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.power_boost.PowerBoostActivate;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.power_boost.PowerBoostDeactivate;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.dodge.Dodge;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.homing_attack.HomingAttack;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.melee.MeleeSwipes;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.smash_hit.SetSmashHitChargeC2S;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.smash_hit.SmashHitToggle;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.speed_blitz.SpeedBlitz;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash.ChargeSpindash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash.LaunchSpindash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash.RevertFromSpindash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.stomp.Stomp;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StartSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StopSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step.StepDown;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step.StepDownDouble;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.danger_sense.DangerSenseToggle;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.doublejump.DoubleJump;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.doublejump.DoubleJumpEnd;
import net.sonicrushxii.beyondthehorizon.scheduler.ScheduledTask;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BaseformClient {
    public static class ClientOnlyData
    {
        private static ScheduledTask lightSpeedCanceller = null;
        public static UUID homingAttackReticle = null;
    }

    public static void performClientTick(LocalPlayer player, CompoundTag playerNBT) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = player.level();

        Vec3 playerDirCentre = Utilities.calculateViewVector(0.0f, player.getViewYRot(0)).scale(0.75);
        BlockPos centrePos = player.blockPosition().offset(
                (int) Math.round(playerDirCentre.x),
                (Math.round(player.getY()) > player.getY()) ? 1 : 0,
                (int) Math.round(playerDirCentre.z)
        );

        final boolean isCtrlDown = (InputConstants.isKeyDown(minecraft.getWindow().getWindow(), InputConstants.KEY_RCONTROL)
                || InputConstants.isKeyDown(minecraft.getWindow().getWindow(), InputConstants.KEY_LCONTROL));
        final boolean isShiftDown = (InputConstants.isKeyDown(minecraft.getWindow().getWindow(), InputConstants.KEY_RSHIFT)
                || InputConstants.isKeyDown(minecraft.getWindow().getWindow(), InputConstants.KEY_LSHIFT));

        BaseformProperties baseformProperties = (BaseformProperties) ClientFormData.getPlayerFormDetails();

        //Passive Abilities
        {
            //General Sprinting
            {
                //It handles Auto Step,
                if (player.isSprinting() && baseformProperties.sprintFlag == false)
                    PacketHandler.sendToServer(new StartSprint());
                if (!player.isSprinting() && baseformProperties.sprintFlag == true)
                    PacketHandler.sendToServer(new StopSprint());

            }
            //Double Jump
            {
                if (KeyBindings.INSTANCE.doubleJump.consumeClick()
                        && !player.onGround() && !player.isSpectator()
                        && baseformProperties.hasDoubleJump
                        && playerNBT.getCompound("abilities").getByte("flying") == 0
                        && !baseformProperties.isAttacking()) {
                    PacketHandler.sendToServer(new DoubleJump());
                }

                if (!baseformProperties.hasDoubleJump && player.onGround()) {
                    PacketHandler.sendToServer(new DoubleJumpEnd());
                }
            }
            //Auto Step
            {
                if (player.isSprinting() && !baseformProperties.isAttacking()) {
                    List<String> blocksinFront = new ArrayList<>();
                    blocksinFront.add(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos.offset(0, -3, 0)).getBlock()) + "");
                    blocksinFront.add(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos.offset(0, -2, 0)).getBlock()) + "");
                    blocksinFront.add(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos.offset(0, -1, 0)).getBlock()) + "");
                    blocksinFront.add(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos).getBlock()) + "");

                    if (Utilities.passableBlocks.contains(blocksinFront.get(3))
                            && Utilities.passableBlocks.contains(blocksinFront.get(2))
                            && !Utilities.passableBlocks.contains(blocksinFront.get(1))
                            && player.onGround())
                        PacketHandler.sendToServer(new StepDown());

                    if (Utilities.passableBlocks.contains(blocksinFront.get(3))
                            && Utilities.passableBlocks.contains(blocksinFront.get(2))
                            && Utilities.passableBlocks.contains(blocksinFront.get(1))
                            && !Utilities.passableBlocks.contains(blocksinFront.get(0))
                            && player.onGround())
                        PacketHandler.sendToServer(new StepDownDouble());
                }
            }

            //Danger Sense
            {
                //Danger Sense Toggle
                if(KeyBindings.INSTANCE.toggleDangerSense.consumeClick() && isShiftDown && isCtrlDown)
                {
                    player.displayClientMessage(
                            Component.nullToEmpty(
                                    (baseformProperties.dangerSenseActive)
                                            ?"Danger Sense Inhibited":"Danger Sense Activated"
                            ),true);
                    PacketHandler.sendToServer(new DangerSenseToggle());
                }
                //Danger Sense Emit-Server Second
            }

            //Hunger
            //Server Second

            //Can't swim
            if(player.isInWater()) {
                player.setSprinting(false);
            }

        }

        //Slot 1
        {
            //Boost
            {
                //Air Boosts
                if (baseformProperties.airBoosts < 3 && player.onGround() && !baseformProperties.isAttacking()) {
                    PacketHandler.sendToServer(new ResetAirBoost());
                }

                if ( VirtualSlotHandler.getCurrAbility() == 0 &&
                        KeyBindings.INSTANCE.useAbility1.consumeClick()) {
                    //Boost
                    if (player.onGround())
                        PacketHandler.sendToServer(new Boost());
                        //Air Boost
                    else if(!baseformProperties.isAttacking())
                        PacketHandler.sendToServer(new AirBoost());
                }

                //Quickstep
                //Double Press

            }
            //Light Speed Attack
            {
                //Activate if Player Presses X when Sneaking
                if ( VirtualSlotHandler.getCurrAbility() == 0 &&
                        player.isShiftKeyDown() &&
                        baseformProperties.lightSpeedState == (byte) 0 &&
                        baseformProperties.getCooldown(BaseformActiveAbility.LIGHT_SPEED_ATTACK) == (byte) 0 &&
                        KeyBindings.INSTANCE.useAbility2.isDown()) {
                    PacketHandler.sendToServer(new LightspeedCharge());

                    ClientOnlyData.lightSpeedCanceller = Scheduler.scheduleTask(() -> {
                        PacketHandler.sendToServer(new LightspeedEffect());
                        Scheduler.scheduleTask(() -> PacketHandler.sendToServer(new LightspeedDecay()), 300);
                    }, 66);
                }

                //Cancel Light Speed Attack
                if (baseformProperties.lightSpeedState == (byte) 1 &&
                        ClientOnlyData.lightSpeedCanceller != null &&
                        !player.isShiftKeyDown()) {
                    ClientOnlyData.lightSpeedCanceller.cancel();
                    PacketHandler.sendToServer(new LightspeedCancel());
                }
            }
            //Power Boost
            {
                //Activate if Player Presses Z
                if(VirtualSlotHandler.getCurrAbility() == 0 && !baseformProperties.isAttacking() &&
                        baseformProperties.getCooldown(BaseformActiveAbility.POWER_BOOST) == (byte) 0 &&
                        KeyBindings.INSTANCE.useAbility3.consumeClick())
                {
                    if(baseformProperties.powerBoost)   PacketHandler.sendToServer(new PowerBoostDeactivate());
                    else                                PacketHandler.sendToServer(new PowerBoostActivate());
                }
            }
        }

        //Slot 2
        {
            //Spin Dash
            {
                //Charge Spindash
                if (VirtualSlotHandler.getCurrAbility() == 1 && player.isShiftKeyDown() &&
                        player.getXRot() > 80.0 && baseformProperties.ballFormState == (byte) 0 &&
                        KeyBindings.INSTANCE.useAbility1.isDown() && !baseformProperties.isAttacking()) {
                    //Set Camera
                    player.setXRot(0.0f);

                    //Let go of Z
                    minecraft.keyboardHandler.keyPress(minecraft.getWindow().getWindow(), KeyBindings.INSTANCE.useAbility1.getKey().getValue(), 0, GLFW.GLFW_RELEASE, 0);

                    //Send Packet
                    baseformProperties.ballFormState = 1;
                    PacketHandler.sendToServer(new ChargeSpindash());
                }

                //Launch Spindash
                if (!player.isShiftKeyDown() && baseformProperties.ballFormState == (byte)1)
                {
                    //Force W Presses and lower Mouse Sens
                    minecraft.options.sensitivity().set(minecraft.options.sensitivity().get()/4.5f);
                    PacketHandler.sendToServer(new LaunchSpindash());
                    Scheduler.scheduleTask(()->
                    {
                        //Release W
                        minecraft.keyboardHandler.keyPress(minecraft.getWindow().getWindow(), InputConstants.KEY_W, 0, GLFW.GLFW_RELEASE, 0);

                        //Return Mouse Sensitivity
                        minecraft.options.sensitivity().set(minecraft.options.sensitivity().get()*4.5f);

                        PacketHandler.sendToServer(new RevertFromSpindash());
                        baseformProperties.ballFormState = 0;
                    },Math.min(baseformProperties.spinDashChargeTime/2, 60));
                }

                //Keep going forward
                if(baseformProperties.ballFormState == (byte)2) {
                    minecraft.keyboardHandler.keyPress(minecraft.getWindow().getWindow(), InputConstants.KEY_W, 0, GLFW.GLFW_PRESS, 0);
                }
            }

            //Homing Attack
            {
                //Reset Reticle
                ClientOnlyData.homingAttackReticle = null;

                //Spawn Reticle
                if(!player.onGround())
                    HomingAttack.scanFoward(player);

                //Perform homing attack
                if (VirtualSlotHandler.getCurrAbility() == 1 && !baseformProperties.isAttacking() && (player.getXRot() <= 80.0 || !player.isShiftKeyDown())
                         && baseformProperties.homingAttackAirTime == 0 && KeyBindings.INSTANCE.useAbility1.consumeClick())
                {
                    //Perform an Obligatory Scan Foward again
                    HomingAttack.scanFoward(player);
                    PacketHandler.sendToServer(new HomingAttack(ClientOnlyData.homingAttackReticle));
                }
            }

            //Melee Swipes
            {
                if (VirtualSlotHandler.getCurrAbility() == 1 && !baseformProperties.isAttacking() &&
                baseformProperties.getCooldown(BaseformActiveAbility.MELEE_ATTACK) == (byte)0 && KeyBindings.INSTANCE.useAbility2.consumeClick())
                {
                    PacketHandler.sendToServer(new MeleeSwipes());
                }
            }

            //Speed Blitz
            {
                if(VirtualSlotHandler.getCurrAbility() == 1 && KeyBindings.INSTANCE.useAbility3.consumeClick())
                {
                    PacketHandler.sendToServer(new SpeedBlitz());
                }
            }

            //Smash Hit
            {
                //Increase Smash hit
                if(VirtualSlotHandler.getCurrAbility() == 1 && KeyBindings.INSTANCE.useAbility4.isDown())
                {
                    //Slow down Player
                    if(!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.SMASH_HIT))
                        PacketHandler.sendToServer(new SmashHitToggle(true));

                    //Remove Effects
                    baseformProperties.smashHit = (byte) Math.min(baseformProperties.smashHit+1,65);
                    PacketHandler.sendToServer(new SetSmashHitChargeC2S(baseformProperties.smashHit));
                }

                //If Smash hit is Enabled
                if(baseformProperties.smashHit > 0)
                {
                    //When the key is released give speed back
                    if(!KeyBindings.INSTANCE.useAbility4.isDown())
                        PacketHandler.sendToServer(new SmashHitToggle(false));

                    //Turn off automatically if you switch to another slot or start another attack
                    if(VirtualSlotHandler.getCurrAbility() != 1 || baseformProperties.isAttacking()) {
                        baseformProperties.smashHit = 0;
                        PacketHandler.sendToServer(new SetSmashHitChargeC2S((byte) 0));
                        PacketHandler.sendToServer(new SmashHitToggle(false)); //Get Speed back
                    }
                }


            }

            //Stomp
            {
                if(VirtualSlotHandler.getCurrAbility() == 1 && !baseformProperties.isAttacking() && KeyBindings.INSTANCE.useAbility5.consumeClick())
                {
                    PacketHandler.sendToServer(new Stomp());
                }
            }
        }
    }

    public static void performClientSecond(LocalPlayer player, CompoundTag playerNBT)
    {

    }

    public static void performDoublePress(LocalPlayer player, BaseformProperties baseformProperties, DoubleTapDirection doubleTapDirection)
    {
        if(baseformProperties.isAttacking())
            return;

        //Quickstep
        if(player.isSprinting() && VirtualSlotHandler.getCurrAbility() == 0 && baseformProperties.boostLvl > 0)
        {
            switch(doubleTapDirection) {
                case RIGHT_PRESS: PacketHandler.sendToServer(new Sidestep(true)); break;
                case LEFT_PRESS: PacketHandler.sendToServer(new Sidestep(false)); break;
            }
        }
        //Dodge
        else{
            switch(doubleTapDirection){
                case RIGHT_PRESS: PacketHandler.sendToServer(new Dodge(true)); break;
                case LEFT_PRESS: PacketHandler.sendToServer(new Dodge(false)); break;
            }
        }
    }
}
