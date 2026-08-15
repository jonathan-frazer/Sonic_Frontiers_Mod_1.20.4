package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.sonicrushxii.beyondthehorizon.KeyBindings;
import net.sonicrushxii.beyondthehorizon.ModUtils;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformActiveAbility;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.client.DoubleTapDirection;
import net.sonicrushxii.beyondthehorizon.client.HelpScreen;
import net.sonicrushxii.beyondthehorizon.client.VirtualSlotHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.base_cyloop.Cyloop;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.boost.AirBoost;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.boost.Boost;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.boost.Sidestep;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack.LightspeedCancel;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack.LightspeedCharge;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack.LightspeedDecay;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack.LightspeedEffect;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.power_boost.PowerBoostActivate;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.power_boost.PowerBoostDeactivate;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.quick_cyloop.QuickCyloop;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.dodge.Dodge;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.homing_attack.HomingAttack;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.humming_top.HummingTop;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.smash_hit.SetSmashHitChargeC2S;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.speed_blitz.SpeedBlitz;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.speed_blitz.SpeedBlitzDash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash.ChargeSpindash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash.LaunchSpindash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash.SpindashBreak;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.stomp.BounceJump;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.stomp.Stomp;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.loop_kick.LoopKick;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.afterimage.Afterimage;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.afterimage.AfterimageCounter;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.spin_kick.CycloneKick;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.spin_kick.EndWindmillKick;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.spin_kick.SpinSlash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.spin_kick.WindmillKick;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.tornado_jump.LightSpeedAssault;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.tornado_jump.Mirage;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.tornado_jump.TornadoJump;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.wild_rush.WildRush;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.cross_slash.CrossSlash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.cross_slash.EndCrossSlash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.homing_shot.HomingShot;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_boom.EndSonicBoom;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_boom.SonicBoom;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_wind.QuickSonicWind;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_wind.SonicWind;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_4.grand_slam.GrandSlam;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_4.parry.Parry;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_4.parry.StopParry;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_5.ultimate_ability.PhantomRushActivate;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_5.ultimate_ability.UltimateActivate;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.danger_sense.DangerSenseToggle;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.doublejump.DoubleJump;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.doublejump.InstaShield;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.boost.UniversalDash;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.wall_boost.WallBoost;
import net.sonicrushxii.beyondthehorizon.scheduler.ScheduledTask;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;
import java.util.UUID;

public class BaseformClient {
    public static class ClientOnlyData
    {
        private static ScheduledTask lightSpeedCanceller = null;
        public static UUID homingAttackReticle = null;

        //Melee Reticles
        public static UUID lightSpeedReticle = null;
        public static UUID wildRushReticle = null;
        public static UUID cycloneReticle = null;
        public static UUID spinSlashReticle = null;

        //Ultimate Reticle
        public static UUID ultTargetReticle = null;

        public static float[] wildRushYawPitch = {0f,0f};
        private static boolean airBoostLock = false;
        public static boolean bounceRequestSent = false;
    }

    public static void performClientTick(AbstractClientPlayer player, CompoundTag playerNBT) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = player.level();

        Vec3 playerDirCentre = ModUtils.calculateViewVector(0.0f, player.getViewYRot(0)).scale(0.75);
        BlockPos centrePos = player.blockPosition().offset(
                (int) Math.round(playerDirCentre.x),
                (Math.round(player.getY()) > player.getY()) ? 1 : 0,
                (int) Math.round(playerDirCentre.z)
        );

        final boolean isCtrlDown = (InputConstants.isKeyDown(minecraft.getWindow().getWindow(), InputConstants.KEY_RCONTROL)
                || InputConstants.isKeyDown(minecraft.getWindow().getWindow(), InputConstants.KEY_LCONTROL));
        final boolean isShiftDown = (InputConstants.isKeyDown(minecraft.getWindow().getWindow(), InputConstants.KEY_RSHIFT)
                || InputConstants.isKeyDown(minecraft.getWindow().getWindow(), InputConstants.KEY_LSHIFT));
        final boolean holdingLeftClick = minecraft.options.keyAttack.isDown();

        final boolean isMoving = player.getDeltaMovement().lengthSqr() > 0.01;


        {
            PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
            BaseformProperties baseformProperties = (BaseformProperties)playerSonicForm.getFormProperties();

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
                        while(KeyBindings.INSTANCE.doubleJump.consumeClick());
                    }
                    // Insta-shield: brief invulnerability on 3rd jump attempt
                    else if (KeyBindings.INSTANCE.doubleJump.consumeClick()
                            && !player.onGround() && !player.isSpectator()
                            && !baseformProperties.hasDoubleJump
                            && playerNBT.getCompound("abilities").getByte("flying") == 0
                            && !baseformProperties.isAttacking()) {
                        PacketHandler.sendToServer(new InstaShield());
                        while(KeyBindings.INSTANCE.doubleJump.consumeClick());
                    }
                }

                //Auto Step
                {}

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
                        while(KeyBindings.INSTANCE.useAbility1.consumeClick());
                    }

                    //Quickstep
                    //Double Press

                    //WallBoost — auto-trigger when sprinting into a wall taller than auto-step or under a ceiling
                    {
                        BlockPos pp = player.blockPosition();
                        // Wall ahead: both foot and head level solid = 2+ block wall
                        boolean wallAhead =
                            !ModUtils.passableBlocks.contains(BuiltInRegistries.BLOCK.getKey(player.level().getBlockState(centrePos).getBlock()) + "")
                            && !ModUtils.passableBlocks.contains(BuiltInRegistries.BLOCK.getKey(player.level().getBlockState(centrePos.above()).getBlock()) + "");
                        // Ceiling 2 blocks above player's feet
                        boolean ceilingAbove =
                            !ModUtils.passableBlocks.contains(BuiltInRegistries.BLOCK.getKey(player.level().getBlockState(pp.above(2)).getBlock()) + "");

                        if ((wallAhead || ceilingAbove) && baseformProperties.boostLvl >= 1 && baseformProperties.boostLvl <= 3
                                && player.isSprinting() && !baseformProperties.wallBoosting)
                        {
                            PacketHandler.sendToServer(new WallBoost());
                        }
                    }

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
                            level.playLocalSound(player.getX(),player.getY(),player.getZ(),
                                    Objects.requireNonNull(BuiltInRegistries.SOUND_EVENT.get(ModSounds.LIGHT_SPEED_IDLE.get().getLocation())),
                                    SoundSource.MASTER, 0.33f, 1.0f, true);

                            Scheduler.scheduleTask(()->{
                                minecraft.getSoundManager().stop(ModSounds.LIGHT_SPEED_IDLE.get().getLocation(), SoundSource.MASTER);
                                PacketHandler.sendToServer(new LightspeedDecay());
                            },300);
                        }, 1);
                    }

                    //Cancel Light Speed Attack
                    if (baseformProperties.lightSpeedState == (byte) 1 &&
                            ClientOnlyData.lightSpeedCanceller != null &&
                            !player.isShiftKeyDown()) {
                        ClientOnlyData.lightSpeedCanceller.cancel();
                        PacketHandler.sendToServer(new LightspeedCancel());
                    }
                }
                //Power Boost (V: increment level 1-4; Shift+V: reset/deactivate)
                {
                    if(VirtualSlotHandler.getCurrAbility() == 0 && !baseformProperties.isAttacking() &&
                            baseformProperties.getCooldown(BaseformActiveAbility.POWER_BOOST) == (byte) 0 &&
                            KeyBindings.INSTANCE.useAbility3.consumeClick())
                    {
                        if (player.isShiftKeyDown() && baseformProperties.powerBoost)
                            PacketHandler.sendToServer(new PowerBoostDeactivate());
                        else if (!player.isShiftKeyDown() && baseformProperties.powerBoostLevel < 4)
                            PacketHandler.sendToServer(new PowerBoostActivate());
                        while(KeyBindings.INSTANCE.useAbility3.consumeClick());
                    }
                }

                {
                    if (VirtualSlotHandler.getCurrAbility() == 0 &&
                            KeyBindings.INSTANCE.useAbility4.consumeClick()) {
                        PacketHandler.sendToServer(new net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.boost.BoostAuraToggle());
                        while (KeyBindings.INSTANCE.useAbility4.consumeClick());
                    }
                }

                //Base Cyloop
                if(VirtualSlotHandler.getCurrAbility() == 0 && isMoving && !baseformProperties.isAttacking() && KeyBindings.INSTANCE.useSingleAbility.isDown() && !player.isShiftKeyDown()) {
                    if(baseformProperties.cylooping <= 0){
                        PacketHandler.sendToServer(new Cyloop(true));
                        baseformProperties.cylooping = 1;
                    }
                }
                else if(baseformProperties.cylooping > 0) {
                    PacketHandler.sendToServer(new Cyloop(false));
                    baseformProperties.cylooping = 0;
                }

                //Quick Cyloop
                if(VirtualSlotHandler.getCurrAbility() == 0 && !baseformProperties.isAttacking() &&
                        KeyBindings.INSTANCE.useSingleAbility.isDown() && player.isShiftKeyDown() && baseformProperties.qkCyloopMeter > 50.0)
                {
                    PacketHandler.sendToServer(new QuickCyloop());
                    baseformProperties.quickCyloop = 1;
                    baseformProperties.qkCyloopMeter -= 50.0;
                }
            }

            //Slot 2
            {
                //Spin Dash
                {
                    if(VirtualSlotHandler.getCurrAbility() == 1 && player.isShiftKeyDown() &&
                            player.getXRot() < -80.0 && player.onGround() && baseformProperties.ballFormState == (byte) 0 &&
                            baseformProperties.getCooldown(BaseformActiveAbility.PEELOUT) == 0 &&
                            KeyBindings.INSTANCE.useAbility1.consumeClick() && !baseformProperties.isAttacking()) {
                        PacketHandler.sendToServer(new net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash.Peelout());
                        while(KeyBindings.INSTANCE.useAbility1.consumeClick());
                    }

                    //Charge Spindash (look DOWN + shift + R = ball form, free movement)
                    if(VirtualSlotHandler.getCurrAbility() == 1 && player.isShiftKeyDown() &&
                            player.getXRot() > 80.0 && baseformProperties.ballFormState == (byte) 0 &&
                            KeyBindings.INSTANCE.useAbility1.isDown() && !baseformProperties.isAttacking()) {
                        baseformProperties.ballFormState = 1;
                        PacketHandler.sendToServer(new ChargeSpindash());
                    }

                    //Launch Spindash
                    if(!player.isShiftKeyDown() && baseformProperties.ballFormState == (byte)1)
                    {
                        PacketHandler.sendToServer(new LaunchSpindash());
                    }

                    //Break blocks with Spindash, or beneath you when stomping
                    if(holdingLeftClick && (baseformProperties.ballFormState == (byte)2 || baseformProperties.stomp > 0))
                    {
                        PacketHandler.sendToServer(new SpindashBreak());
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
                        //If scan fails do air boost (only once in air unless enemy hit)
                        if(ClientOnlyData.homingAttackReticle == null && !ClientOnlyData.airBoostLock
                                && !baseformProperties.airSpindashUsed) {
                            PacketHandler.sendToServer(new AirBoost());
                            baseformProperties.airSpindashUsed = true;
                            ClientOnlyData.airBoostLock = true;
                            Scheduler.scheduleTask(()-> ClientOnlyData.airBoostLock = false,5);
                        }
                        else if (ClientOnlyData.homingAttackReticle != null)
                            PacketHandler.sendToServer(new HomingAttack(ClientOnlyData.homingAttackReticle));
                    }

                    //Break Blocks with Air Boost
                    if(holdingLeftClick && baseformProperties.ballFormState == (byte)3)
                    {
                        PacketHandler.sendToServer(new SpindashBreak());
                    }
                }

                //Humming Top
                {
                    if (VirtualSlotHandler.getCurrAbility() == 1 && !baseformProperties.isAttacking() && !player.onGround() &&
                            baseformProperties.getCooldown(BaseformActiveAbility.HUMMING_TOP) == (byte)0 && KeyBindings.INSTANCE.useAbility2.isDown())
                    {
                        PacketHandler.sendToServer(new HummingTop(true));
                        baseformProperties.hummingTop = 1;
                    }

                    if (VirtualSlotHandler.getCurrAbility() == 1 && baseformProperties.hummingTop > 1 && !KeyBindings.INSTANCE.useAbility2.isDown())
                    {
                        PacketHandler.sendToServer(new HummingTop(false));
                        baseformProperties.hummingTop = 0;
                    }
                }

                //Speed Blitz
                {
                    if(VirtualSlotHandler.getCurrAbility() == 1 && baseformProperties.getCooldown(BaseformActiveAbility.SPEED_BLITZ) == (byte)0
                            && KeyBindings.INSTANCE.useAbility3.consumeClick())
                    {
                        if(!player.isShiftKeyDown())
                            PacketHandler.sendToServer(new SpeedBlitz());
                        else if(baseformProperties.speedBlitzDashTimer == 0) {
                            PacketHandler.sendToServer(new SpeedBlitzDash());
                            baseformProperties.speedBlitzDashTimer = 1;
                        }
                        while(KeyBindings.INSTANCE.useAbility3.consumeClick());
                    }

                }

                {
                    if (VirtualSlotHandler.getCurrAbility() == 1 && player.isShiftKeyDown() &&
                            baseformProperties.getCooldown(BaseformActiveAbility.SMASH_BARRAGE) == 0 &&
                            baseformProperties.smashBarrage == 0 && KeyBindings.INSTANCE.useAbility3.isDown()) {
                        PacketHandler.sendToServer(new net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.smash_hit.SmashBarrage());
                        baseformProperties.smashBarrage = 1;
                    }
                    if (baseformProperties.smashBarrage > 0 && !KeyBindings.INSTANCE.useAbility3.isDown()) {
                        PacketHandler.sendToServer(new net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.smash_hit.EndSmashBarrage());
                        baseformProperties.smashBarrage = 0;
                    }
                }

                //Smash Hit
                {
                    //Increase Smash hit
                    if(VirtualSlotHandler.getCurrAbility() == 1 && baseformProperties.getCooldown(BaseformActiveAbility.SMASH_HIT) == (byte)0
                            && KeyBindings.INSTANCE.useAbility4.isDown())
                    {
                        //Add Number
                        baseformProperties.smashHit = (byte) Math.min(baseformProperties.smashHit+ (byte)((baseformProperties.powerBoostLevel > 0)?2:1),65);
                        PacketHandler.sendToServer(new SetSmashHitChargeC2S(baseformProperties.smashHit));
                    }

                    //If Smash hit is Enabled
                    if(baseformProperties.smashHit > 0)
                    {
                        //Turn off automatically if you switch to another slot or start another attack (speed blitz dash is allowed simultaneously)
                        if(VirtualSlotHandler.getCurrAbility() != 1 || (baseformProperties.isAttacking() && baseformProperties.speedBlitzDashTimer == 0) || !KeyBindings.INSTANCE.useAbility4.isDown()) {
                            baseformProperties.smashHit = 0;
                            PacketHandler.sendToServer(new SetSmashHitChargeC2S((byte) 0));
                        }
                    }
                }

                //Stomp/Updraft (F in air = kick up; shift+F = Sonic Eagle down)
                {
                    if(VirtualSlotHandler.getCurrAbility() == 1 && baseformProperties.getCooldown(BaseformActiveAbility.STOMP) == (byte)0
                            && !player.onGround() && !baseformProperties.isAttacking() && KeyBindings.INSTANCE.useAbility5.isDown()) {
                        if (player.isShiftKeyDown())
                            PacketHandler.sendToServer(new Stomp((byte)1));
                        else
                            PacketHandler.sendToServer(new Stomp());
                    }
                }

                //Ball form bounce — send once per landing when jump is held
                {
                    if(baseformProperties.stomp > 0 && baseformProperties.ballFormState > 0) {
                        if(!player.onGround())
                            ClientOnlyData.bounceRequestSent = false;
                        else if(Minecraft.getInstance().options.keyJump.isDown() && !ClientOnlyData.bounceRequestSent) {
                            PacketHandler.sendToServer(new BounceJump());
                            ClientOnlyData.bounceRequestSent = true;
                        }
                    } else {
                        ClientOnlyData.bounceRequestSent = false;
                    }
                }
            }

            // Slot cycling: cycle through 4 scrollable slots via cycleSlotKey (handled in InputSlotHandler)

            //Melee Slot (slot 4, toggled via Mouse 4)
            {
                final boolean inMeleeSlot = VirtualSlotHandler.getCurrAbility() == VirtualSlotHandler.SLOT_MELEE;

                //Tornado Jump / Mirage / Light Speed Assault
                {
                    if (inMeleeSlot && !baseformProperties.isAttacking() && baseformProperties.tornadoJump == 0
                            && !player.isShiftKeyDown() && KeyBindings.INSTANCE.useAbility1.isDown()) {
                        PacketHandler.sendToServer(new TornadoJump());
                        baseformProperties.tornadoJump = 1;
                        VirtualSlotHandler.goToSlot(VirtualSlotHandler.SLOT_COMBO);
                    }

                    if (baseformProperties.tornadoJump == -1 && player.onGround())
                        baseformProperties.tornadoJump = 0;
                }

                //Mirage
                {
                    if (inMeleeSlot && !baseformProperties.isAttacking() && baseformProperties.lightSpeedState != 2
                            && baseformProperties.mirageTimer <= 0 && player.isShiftKeyDown() && baseformProperties.getCooldown(BaseformActiveAbility.MIRAGE) == 0
                            && player.onGround() && KeyBindings.INSTANCE.useAbility1.isDown()) {
                        PacketHandler.sendToServer(new Mirage());
                        baseformProperties.mirageTimer = 1;
                    }
                }

                //Light Speed Assault
                {
                    if (inMeleeSlot && !baseformProperties.isAttacking() && baseformProperties.lightSpeedState == 2 &&
                            player.isShiftKeyDown() && player.onGround() && baseformProperties.getCooldown(BaseformActiveAbility.MIRAGE) == 0 &&
                            KeyBindings.INSTANCE.useAbility1.isDown()) {
                        ClientOnlyData.lightSpeedReticle = null;
                        LightSpeedAssault.scanFoward(player);
                        if(ClientOnlyData.lightSpeedReticle != null) {
                            PacketHandler.sendToServer(new LightSpeedAssault(ClientOnlyData.lightSpeedReticle));
                            baseformProperties.lightSpeedAssault = 1;
                            VirtualSlotHandler.goToSlot(VirtualSlotHandler.SLOT_COMBO);
                        }
                    }
                }

                {
                    if (inMeleeSlot && !baseformProperties.isAttacking() &&
                            player.isShiftKeyDown() && baseformProperties.getCooldown(BaseformActiveAbility.SPINSLASH) == 0 &&
                            KeyBindings.INSTANCE.useAbility2.isDown()) {
                        ClientOnlyData.spinSlashReticle = null;
                        SpinSlash.scanFoward(player);
                        if(ClientOnlyData.spinSlashReticle != null) {
                            PacketHandler.sendToServer(new SpinSlash(ClientOnlyData.spinSlashReticle));
                            baseformProperties.spinSlash = -15;
                            VirtualSlotHandler.goToSlot(VirtualSlotHandler.SLOT_COMBO);
                        }
                    }
                    // Windmill Kick start (no-shift X, holdable)
                    if (inMeleeSlot && !baseformProperties.isAttacking() &&
                            !player.isShiftKeyDown() && baseformProperties.getCooldown(BaseformActiveAbility.WINDMILL_KICK) == 0 &&
                            baseformProperties.windmillKick == 0 && KeyBindings.INSTANCE.useAbility2.isDown()) {
                        PacketHandler.sendToServer(new WindmillKick());
                        baseformProperties.windmillKick = 1;
                    }
                    // Windmill Kick end (released X)
                    if (inMeleeSlot && baseformProperties.windmillKick > 0 &&
                            !KeyBindings.INSTANCE.useAbility2.isDown()) {
                        PacketHandler.sendToServer(new EndWindmillKick());
                        baseformProperties.windmillKick = 0;
                    }
                }

                //Wild Rush (V key = useAbility3) / Loop Kick (shift+V)
                {
                    if (inMeleeSlot && !baseformProperties.isAttacking()) {
                        if (!player.isShiftKeyDown() && baseformProperties.getCooldown(BaseformActiveAbility.WILDRUSH) == 0 &&
                                KeyBindings.INSTANCE.useAbility3.isDown()) {
                            WildRush.scanFoward(player);
                            if(ClientOnlyData.wildRushReticle != null) {
                                PacketHandler.sendToServer(new WildRush(ClientOnlyData.wildRushReticle));
                                baseformProperties.wildRushTime = 1;
                                VirtualSlotHandler.goToSlot(VirtualSlotHandler.SLOT_COMBO);
                            }
                        }
                        // Loop Kick: shift+V
                        else if (player.isShiftKeyDown() && baseformProperties.getCooldown(BaseformActiveAbility.LOOPKICK) == 0 &&
                                KeyBindings.INSTANCE.useAbility3.isDown()) {
                            PacketHandler.sendToServer(new LoopKick());
                            baseformProperties.loopKick = 1;
                            VirtualSlotHandler.goToSlot(VirtualSlotHandler.SLOT_COMBO);
                        }
                    }
                }

                //Afterimage (C key = useAbility4)
                {
                    if (inMeleeSlot && !baseformProperties.isAttacking() &&
                            baseformProperties.afterimage == 0 && baseformProperties.afterimageCounter == 0 &&
                            baseformProperties.getCooldown(BaseformActiveAbility.AFTERIMAGE) == 0 &&
                            KeyBindings.INSTANCE.useAbility4.consumeClick()) {
                        PacketHandler.sendToServer(new Afterimage());
                        baseformProperties.afterimage = 1;
                        VirtualSlotHandler.goToSlot(VirtualSlotHandler.SLOT_COMBO);
                        while(KeyBindings.INSTANCE.useAbility4.consumeClick());
                    }
                }

                //GrandSlam (F key, no-shift) / Afterimage Counter (shift+F)
                {
                    if (inMeleeSlot && !player.isShiftKeyDown() &&
                            (!baseformProperties.isAttacking() || baseformProperties.parryTime < 0) &&
                            KeyBindings.INSTANCE.useAbility5.isDown())
                    {
                        PacketHandler.sendToServer(new GrandSlam());
                        VirtualSlotHandler.goToSlot(VirtualSlotHandler.SLOT_COMBO);
                    }
                    // Counter (shift+F): only usable during or after Afterimage
                    if (inMeleeSlot && player.isShiftKeyDown() &&
                            (baseformProperties.afterimage > 0 || baseformProperties.afterimageCounter > 0) &&
                            KeyBindings.INSTANCE.useAbility5.consumeClick()) {
                        PacketHandler.sendToServer(new AfterimageCounter());
                        baseformProperties.afterimageCounter = 40;
                        while(KeyBindings.INSTANCE.useAbility5.consumeClick());
                    }
                }
            }

            //Ranged Slot (slot 5, toggled via Mouse 5)
            {
                final boolean inRangedSlot = VirtualSlotHandler.getCurrAbility() == VirtualSlotHandler.SLOT_RANGED;

                //Sonic Boom (useAbility1 = R key, holdable - stays in ranged)
                {
                    if (inRangedSlot && !baseformProperties.isAttacking() &&
                            baseformProperties.getCooldown(BaseformActiveAbility.SONIC_BOOM) == 0 &&
                            KeyBindings.INSTANCE.useAbility1.isDown())
                    {
                        PacketHandler.sendToServer(new SonicBoom());
                        baseformProperties.sonicBoom = 1;
                    }

                    if (inRangedSlot && baseformProperties.sonicBoom > 0 &&
                            !KeyBindings.INSTANCE.useAbility1.isDown())
                    {
                        PacketHandler.sendToServer(new EndSonicBoom());
                        baseformProperties.sonicBoom = 0;
                    }
                }

                {
                    if (inRangedSlot && !baseformProperties.isAttacking() &&
                            baseformProperties.getCooldown(BaseformActiveAbility.CROSS_SLASH) == 0 &&
                            KeyBindings.INSTANCE.useAbility2.isDown())
                    {
                        PacketHandler.sendToServer(new CrossSlash());
                        baseformProperties.crossSlash = 1;
                    }

                    if (inRangedSlot && baseformProperties.crossSlash > 0 &&
                            !KeyBindings.INSTANCE.useAbility2.isDown())
                    {
                        PacketHandler.sendToServer(new EndCrossSlash());
                        baseformProperties.crossSlash = 0;
                    }
                }

                //Sonic Wind (useAbility3 = V key)
                {
                    if (inRangedSlot && !baseformProperties.isAttacking() &&
                            baseformProperties.getCooldown(BaseformActiveAbility.SONIC_WIND) == 0 &&
                            KeyBindings.INSTANCE.useAbility3.isDown())
                    {
                        //Normal Version
                        if(!player.isShiftKeyDown() && baseformProperties.sonicWind == 0)
                        {
                            player.displayClientMessage(Component.translatable("Sonic Wind!").withStyle(Style.EMPTY.withColor(0x00EEFF)),true);
                            PacketHandler.sendToServer(new SonicWind());
                            baseformProperties.sonicWind = 1;
                            VirtualSlotHandler.goToSlot(VirtualSlotHandler.SLOT_COMBO);
                        }
                        //Quick Version
                        if(player.isShiftKeyDown() && baseformProperties.profanedWind == 0)
                        {
                            player.displayClientMessage(Component.translatable("Sonic Wind").withStyle(Style.EMPTY.withColor(0x00FFFF)),true);
                            PacketHandler.sendToServer(new QuickSonicWind());
                            baseformProperties.profanedWind = 1;
                            VirtualSlotHandler.goToSlot(VirtualSlotHandler.SLOT_COMBO);
                        }
                    }
                }

                //Homing Shot (useAbility4 = C key)
                {
                    if (inRangedSlot && !baseformProperties.isAttacking() &&
                            baseformProperties.getCooldown(BaseformActiveAbility.HOMING_SHOT) == 0 &&
                            KeyBindings.INSTANCE.useAbility4.isDown())
                    {
                        PacketHandler.sendToServer(new HomingShot());
                        baseformProperties.homingShot = 1;
                        VirtualSlotHandler.goToSlot(VirtualSlotHandler.SLOT_COMBO);
                    }
                }

                //Sonic Wave/Storm (useAbility5 = F key; ground=Wave, air=Storm)
                {
                    if (inRangedSlot && !baseformProperties.isAttacking() &&
                            baseformProperties.getCooldown(BaseformActiveAbility.SONIC_WAVE) == 0 &&
                            KeyBindings.INSTANCE.useAbility5.consumeClick()) {
                        PacketHandler.sendToServer(new net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_wave.SonicWavePacket());
                        VirtualSlotHandler.goToSlot(VirtualSlotHandler.SLOT_COMBO);
                        while (KeyBindings.INSTANCE.useAbility5.consumeClick());
                    }
                }
            }

            //Universal Dash (G key - any slot)
            {
                if (!baseformProperties.isAttacking() &&
                        baseformProperties.getCooldown(BaseformActiveAbility.UNIVERSAL_DASH) == 0 &&
                        KeyBindings.INSTANCE.dashKey.consumeClick()) {
                    PacketHandler.sendToServer(new UniversalDash());
                    while (KeyBindings.INSTANCE.dashKey.consumeClick());
                }
            }

            //Parry / Block (Left Alt key - universal, not slot-specific)
            {
                if(!baseformProperties.isAttacking() && KeyBindings.INSTANCE.parryKey.isDown())
                {
                    PacketHandler.sendToServer(new Parry());
                    baseformProperties.parryTime = 1;
                }
                if (baseformProperties.parryTime > 0 && !KeyBindings.INSTANCE.parryKey.isDown())
                {
                    PacketHandler.sendToServer(new StopParry());
                    baseformProperties.parryTime = 0;
                }
            }

            //Ultimate Slot (slot 2)
            {
                final boolean inUltSlot = VirtualSlotHandler.getCurrAbility() == VirtualSlotHandler.SLOT_ULTIMATE;
                if(!baseformProperties.isAttacking() && baseformProperties.phantomRushReady() && baseformProperties.getCooldown(BaseformActiveAbility.PHANTOM_RUSH) == 0 &&
                        player.isShiftKeyDown() && KeyBindings.INSTANCE.useUltimateAbility.isDown())
                {
                    ClientOnlyData.ultTargetReticle = null;
                    PhantomRushActivate.scanForward(player);
                    if(ClientOnlyData.ultTargetReticle != null)
                    {
                        PacketHandler.sendToServer(new PhantomRushActivate(ClientOnlyData.ultTargetReticle));
                        baseformProperties.ultimateUse = 1;
                    }
                }

                //Ultimate Ability (useAbility1 = R key in Ultimate slot, or useUltimateAbility = H key)
                if(!baseformProperties.isAttacking() && baseformProperties.ultReady &&
                        !player.isShiftKeyDown() &&
                        (KeyBindings.INSTANCE.useUltimateAbility.isDown() || (inUltSlot && KeyBindings.INSTANCE.useAbility1.isDown())))
                {
                    ClientOnlyData.ultTargetReticle = null;
                    UltimateActivate.scanFoward(player);
                    if(ClientOnlyData.ultTargetReticle != null)
                    {
                        PacketHandler.sendToServer(new UltimateActivate(ClientOnlyData.ultTargetReticle));
                        baseformProperties.ultimateUse = 1;
                    }
                }
            }

            //Open Help Screen
            {
                if(KeyBindings.INSTANCE.helpButton.consumeClick())
                {
                    if (FMLEnvironment.dist == Dist.CLIENT) Minecraft.getInstance().setScreen(new HelpScreen(baseformProperties.helpScreenPageNo));
                    while(KeyBindings.INSTANCE.helpButton.consumeClick());
                }
            }
        }
    }

    public static void performClientSecond(AbstractClientPlayer player, CompoundTag playerNBT)
    {

    }

    public static void performDoublePress(AbstractClientPlayer player, BaseformProperties baseformProperties, DoubleTapDirection doubleTapDirection)
    {
        if(baseformProperties.isAttacking())
            return;

        //Quickstep
        if(player.isSprinting() && VirtualSlotHandler.getCurrAbility() == VirtualSlotHandler.SLOT_BOOST && baseformProperties.boostLvl > 0)
        {
            switch(doubleTapDirection) {
                case RIGHT_PRESS: PacketHandler.sendToServer(new Sidestep(true)); break;
                case LEFT_PRESS: PacketHandler.sendToServer(new Sidestep(false)); break;
            }
        }
        //Dodge (left/right: shift + double-tap A/D; backward: shift + double-tap S)
        else if(player.isShiftKeyDown()){
            switch(doubleTapDirection){
                case RIGHT_PRESS: PacketHandler.sendToServer(new Dodge(true)); break;
                case LEFT_PRESS: PacketHandler.sendToServer(new Dodge(false)); break;
                case BACK_PRESS: PacketHandler.sendToServer(new Dodge(null)); break;
            }
        }
    }
}
