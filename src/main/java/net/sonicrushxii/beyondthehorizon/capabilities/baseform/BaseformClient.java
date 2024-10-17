package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sonicrushxii.beyondthehorizon.KeyBindings;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformActiveAbility;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.client.ClientFormData;
import net.sonicrushxii.beyondthehorizon.client.DoubleTapDirection;
import net.sonicrushxii.beyondthehorizon.client.VirtualSlotHandler;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.boost.AirBoost;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.boost.Boost;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.boost.Sidestep;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack.LightspeedCancel;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack.LightspeedCharge;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack.LightspeedDecay;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack.LightspeedEffect;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.power_boost.PowerBoostActivate;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.power_boost.PowerBoostDeactivate;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.dodge.Dodge;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.homing_attack.HomingAttack;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.humming_top.HummingTop;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.smash_hit.SetSmashHitChargeC2S;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.speed_blitz.SpeedBlitz;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash.ChargeSpindash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash.LaunchSpindash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.stomp.Stomp;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.danger_sense.DangerSenseToggle;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.doublejump.DoubleJump;
import net.sonicrushxii.beyondthehorizon.scheduler.ScheduledTask;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;

public class BaseformClient {
    public static class ClientOnlyData
    {
        private static ScheduledTask lightSpeedCanceller = null;
        public static UUID homingAttackReticle = null;
        private static boolean airBoostLock = false;
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

            //Double Jump
            {
                if (KeyBindings.INSTANCE.doubleJump.consumeClick()
                        && !player.onGround() && !player.isSpectator()
                        && baseformProperties.hasDoubleJump
                        && playerNBT.getCompound("abilities").getByte("flying") == 0
                        && !baseformProperties.isAttacking()) {
                    PacketHandler.sendToServer(new DoubleJump());
                }
            }
            //Auto Step

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
                if ( VirtualSlotHandler.getCurrAbility() == 0 &&
                        KeyBindings.INSTANCE.useAbility1.consumeClick()) {
                    //Boost
                    PacketHandler.sendToServer(new Boost(player.isShiftKeyDown()));
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
                    PacketHandler.sendToServer(new LaunchSpindash());

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
                         && baseformProperties.homingAttackAirTime == 0 && KeyBindings.INSTANCE.useAbility1.isDown())
                {
                    //Perform an Obligatory Scan Foward again
                    HomingAttack.scanFoward(player);
                    //If scan fails do air boost
                    if(ClientOnlyData.homingAttackReticle == null && !ClientOnlyData.airBoostLock) {
                        PacketHandler.sendToServer(new AirBoost());
                        ClientOnlyData.airBoostLock = true;
                        Scheduler.scheduleTask(()->{ClientOnlyData.airBoostLock = false;},5);
                    }
                    else
                        PacketHandler.sendToServer(new HomingAttack(ClientOnlyData.homingAttackReticle));
                }
            }

            //Melee Swipes
            {
                if (VirtualSlotHandler.getCurrAbility() == 1 && !baseformProperties.isAttacking() &&
                baseformProperties.getCooldown(BaseformActiveAbility.HUMMING_TOP) == (byte)0 && KeyBindings.INSTANCE.useAbility2.consumeClick())
                {
                    PacketHandler.sendToServer(new HummingTop());
                }
            }

            //Speed Blitz
            {
                if(VirtualSlotHandler.getCurrAbility() == 1 && baseformProperties.getCooldown(BaseformActiveAbility.SPEED_BLITZ) == (byte)0
                && KeyBindings.INSTANCE.useAbility3.consumeClick())
                {
                    PacketHandler.sendToServer(new SpeedBlitz());
                }
            }

            //Smash Hit
            {
                //Increase Smash hit
                if(VirtualSlotHandler.getCurrAbility() == 1 && baseformProperties.getCooldown(BaseformActiveAbility.SMASH_HIT) == (byte)0
                        && KeyBindings.INSTANCE.useAbility4.isDown())
                {
                    //Add Number
                    baseformProperties.smashHit = (byte) Math.min(baseformProperties.smashHit+1,65);
                    PacketHandler.sendToServer(new SetSmashHitChargeC2S(baseformProperties.smashHit));
                }

                //If Smash hit is Enabled
                if(baseformProperties.smashHit > 0)
                {
                    //Turn off automatically if you switch to another slot or start another attack
                    if(VirtualSlotHandler.getCurrAbility() != 1 || baseformProperties.isAttacking() || !KeyBindings.INSTANCE.useAbility4.isDown()) {
                        baseformProperties.smashHit = 0;
                        PacketHandler.sendToServer(new SetSmashHitChargeC2S((byte) 0));
                    }
                }
            }

            //Stomp
            {
                if(VirtualSlotHandler.getCurrAbility() == 1 && baseformProperties.getCooldown(BaseformActiveAbility.STOMP) == (byte)0
                    &&  !player.onGround() && !baseformProperties.isAttacking() && KeyBindings.INSTANCE.useAbility5.consumeClick())
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
        else if(player.isShiftKeyDown()){
            switch(doubleTapDirection){
                case RIGHT_PRESS: PacketHandler.sendToServer(new Dodge(true)); break;
                case LEFT_PRESS: PacketHandler.sendToServer(new Dodge(false)); break;
            }
        }
    }
}
