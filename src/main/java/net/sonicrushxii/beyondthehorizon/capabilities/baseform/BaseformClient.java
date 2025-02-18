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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonicrushxii.beyondthehorizon.KeyBindings;
import net.sonicrushxii.beyondthehorizon.ModUtils;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
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
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.stomp.Stomp;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.loop_kick.LoopKick;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.spin_kick.CycloneKick;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.spin_kick.SpinSlash;
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
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_5.ultimate_ability.UltimateActivate;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.danger_sense.DangerSenseToggle;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.doublejump.DoubleJump;
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


        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
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

                    //WallBoost
                    if (!ModUtils.passableBlocks.contains(ForgeRegistries.BLOCKS.getKey(player.level().getBlockState(centrePos.offset(0, 1, 0)).getBlock()) + "")
                            && baseformProperties.boostLvl >= 1 && baseformProperties.boostLvl <= 3
                            && player.isSprinting() && !baseformProperties.wallBoosting && KeyBindings.INSTANCE.doubleJump.isDown())
                    {
                        PacketHandler.sendToServer(new WallBoost());
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
                                    Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ModSounds.LIGHT_SPEED_IDLE.get().getLocation())),
                                    SoundSource.MASTER, 0.33f, 1.0f, true);

                            Scheduler.scheduleTask(()->{
                                minecraft.getSoundManager().stop(ModSounds.LIGHT_SPEED_IDLE.get().getLocation(), SoundSource.MASTER);
                                PacketHandler.sendToServer(new LightspeedDecay());
                            },300);
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
                        while(KeyBindings.INSTANCE.useAbility3.consumeClick());
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
                    //Charge Spindash
                    if(VirtualSlotHandler.getCurrAbility() == 1 && player.isShiftKeyDown() &&
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
                    if(!player.isShiftKeyDown() && baseformProperties.ballFormState == (byte)1)
                    {
                        PacketHandler.sendToServer(new LaunchSpindash());
                    }

                    //Break blocks with Spindash
                    if(holdingLeftClick && baseformProperties.ballFormState == (byte)2)
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
                        //If scan fails do air boost
                        if(ClientOnlyData.homingAttackReticle == null && !ClientOnlyData.airBoostLock) {
                            PacketHandler.sendToServer(new AirBoost());
                            ClientOnlyData.airBoostLock = true;
                            Scheduler.scheduleTask(()-> ClientOnlyData.airBoostLock = false,5);
                        }
                        else
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

                //Smash Hit
                {
                    //Increase Smash hit
                    if(VirtualSlotHandler.getCurrAbility() == 1 && baseformProperties.getCooldown(BaseformActiveAbility.SMASH_HIT) == (byte)0
                            && KeyBindings.INSTANCE.useAbility4.isDown())
                    {
                        //Add Number
                        baseformProperties.smashHit = (byte) Math.min(baseformProperties.smashHit+ (byte)((baseformProperties.powerBoost)?2:1),65);
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
                            &&  !player.onGround() && !baseformProperties.isAttacking() && KeyBindings.INSTANCE.useAbility5.isDown())
                        PacketHandler.sendToServer(new Stomp());
                }
            }

            //Slot 3
            {
                //Tornado Jump
                {
                    if (VirtualSlotHandler.getCurrAbility() == 2 && !baseformProperties.isAttacking() && baseformProperties.tornadoJump == 0
                            && !player.isShiftKeyDown() && KeyBindings.INSTANCE.useAbility1.isDown()) {
                        PacketHandler.sendToServer(new TornadoJump());
                        baseformProperties.tornadoJump = 1;
                    }

                    if (baseformProperties.tornadoJump == -1 && player.onGround())
                        baseformProperties.tornadoJump = 0;
                }

                //Mirage
                {
                    if (VirtualSlotHandler.getCurrAbility() == 2 && !baseformProperties.isAttacking() && baseformProperties.lightSpeedState != 2
                            && baseformProperties.mirageTimer <= 0 && player.isShiftKeyDown() && baseformProperties.getCooldown(BaseformActiveAbility.MIRAGE) == 0
                            && player.onGround() && KeyBindings.INSTANCE.useAbility1.isDown()) {
                        PacketHandler.sendToServer(new Mirage());
                        baseformProperties.mirageTimer = 1;
                    }
                }

                //Light Speed Assault
                {
                    if (VirtualSlotHandler.getCurrAbility() == 2 && !baseformProperties.isAttacking() && baseformProperties.lightSpeedState == 2 &&
                            player.isShiftKeyDown() && player.onGround() && baseformProperties.getCooldown(BaseformActiveAbility.MIRAGE) == 0 &&
                            KeyBindings.INSTANCE.useAbility1.isDown()) {
                        ClientOnlyData.lightSpeedReticle = null;
                        LightSpeedAssault.scanFoward(player);
                        if(ClientOnlyData.lightSpeedReticle != null) {
                            PacketHandler.sendToServer(new LightSpeedAssault(ClientOnlyData.lightSpeedReticle));
                            baseformProperties.lightSpeedAssault = 1;
                        }
                    }
                }

                //Spin Slash
                {
                    if (VirtualSlotHandler.getCurrAbility() == 2 && !baseformProperties.isAttacking() &&
                            !player.isShiftKeyDown() && baseformProperties.getCooldown(BaseformActiveAbility.SPINSLASH) == 0 &&
                            KeyBindings.INSTANCE.useAbility2.isDown()) {
                        ClientOnlyData.spinSlashReticle = null;
                        SpinSlash.scanFoward(player);
                        if(ClientOnlyData.spinSlashReticle != null) {
                            PacketHandler.sendToServer(new SpinSlash(ClientOnlyData.spinSlashReticle));
                            baseformProperties.spinSlash = -60;
                        }
                    }
                }

                //Cyclone Kick
                {
                    if (VirtualSlotHandler.getCurrAbility() == 2 && !baseformProperties.isAttacking() &&
                            baseformProperties.getCooldown(BaseformActiveAbility.CYCLONE_KICK) == 0 && player.isShiftKeyDown() &&
                            KeyBindings.INSTANCE.useAbility2.isDown())
                    {
                        ClientOnlyData.cycloneReticle = null;
                        CycloneKick.scanFoward(player);
                        PacketHandler.sendToServer(new CycloneKick(ClientOnlyData.cycloneReticle));
                    }
                }

                //Wild Rush
                {
                    if (VirtualSlotHandler.getCurrAbility() == 2 && !baseformProperties.isAttacking() &&
                            baseformProperties.getCooldown(BaseformActiveAbility.WILDRUSH) == 0 &&
                            KeyBindings.INSTANCE.useAbility3.isDown())
                    {
                        WildRush.scanFoward(player);
                        if(ClientOnlyData.wildRushReticle != null) {
                            PacketHandler.sendToServer(new WildRush(ClientOnlyData.wildRushReticle));
                            baseformProperties.wildRushTime = 1;
                        }
                    }
                }

                //Loop Kick
                {
                    if (VirtualSlotHandler.getCurrAbility() == 2 && !baseformProperties.isAttacking() &&
                            baseformProperties.getCooldown(BaseformActiveAbility.LOOPKICK) == 0 &&
                            KeyBindings.INSTANCE.useAbility4.isDown()) {
                        PacketHandler.sendToServer(new LoopKick());
                        baseformProperties.loopKick = 1;
                    }
                }
            }

            //Slot 4
            {

                //Sonic Boom
                {
                    if (VirtualSlotHandler.getCurrAbility() == 3 && !baseformProperties.isAttacking() &&
                            baseformProperties.getCooldown(BaseformActiveAbility.SONIC_BOOM) == 0 &&
                            KeyBindings.INSTANCE.useAbility1.isDown())
                    {
                        PacketHandler.sendToServer(new SonicBoom());
                        baseformProperties.sonicBoom = 1;
                    }

                    if (VirtualSlotHandler.getCurrAbility() == 3 && baseformProperties.sonicBoom > 0 &&
                            !KeyBindings.INSTANCE.useAbility1.isDown())
                    {
                        PacketHandler.sendToServer(new EndSonicBoom());
                        baseformProperties.sonicBoom = 0;
                    }
                }

                //Cross Slash
                {
                    if (VirtualSlotHandler.getCurrAbility() == 3 && !baseformProperties.isAttacking() &&
                            baseformProperties.getCooldown(BaseformActiveAbility.CROSS_SLASH) == 0 &&
                            KeyBindings.INSTANCE.useAbility2.isDown())
                    {
                        PacketHandler.sendToServer(new CrossSlash());
                        baseformProperties.crossSlash = 1;
                    }

                    if (VirtualSlotHandler.getCurrAbility() == 3 && baseformProperties.crossSlash > 0 &&
                            !KeyBindings.INSTANCE.useAbility2.isDown())
                    {
                        PacketHandler.sendToServer(new EndCrossSlash());
                        baseformProperties.crossSlash = 0;
                    }
                }

                //Sonic Wind
                {
                    if (VirtualSlotHandler.getCurrAbility() == 3 && !baseformProperties.isAttacking() &&
                            baseformProperties.getCooldown(BaseformActiveAbility.SONIC_WIND) == 0 &&
                            KeyBindings.INSTANCE.useAbility3.isDown())
                    {
                        //Normal Version
                        if(!player.isShiftKeyDown() && baseformProperties.sonicWind == 0)
                        {
                            player.displayClientMessage(Component.translatable("Sonic Wind!").withStyle(Style.EMPTY.withColor(0x00EEFF)),true);
                            PacketHandler.sendToServer(new SonicWind());
                            baseformProperties.sonicWind = 1;
                        }
                        //Quick Version
                        if(player.isShiftKeyDown() && baseformProperties.profanedWind == 0)
                        {
                            player.displayClientMessage(Component.translatable("Sonic Wind").withStyle(Style.EMPTY.withColor(0x00FFFF)),true);
                            PacketHandler.sendToServer(new QuickSonicWind());
                            baseformProperties.profanedWind = 1;
                        }
                    }
                }

                //Homing Shot
                {
                    if (VirtualSlotHandler.getCurrAbility() == 3 && !baseformProperties.isAttacking() &&
                            baseformProperties.getCooldown(BaseformActiveAbility.HOMING_SHOT) == 0 &&
                            KeyBindings.INSTANCE.useAbility4.isDown())
                    {
                        PacketHandler.sendToServer(new HomingShot());
                        baseformProperties.homingShot = 1;
                    }
                }
            }

            //Slot 5
            {
                //Parry
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

                //GrandSlam
                if((!baseformProperties.isAttacking() || baseformProperties.parryTime < 0) && VirtualSlotHandler.getCurrAbility() == 4
                        && KeyBindings.INSTANCE.useSingleAbility.isDown())
                {
                    PacketHandler.sendToServer(new GrandSlam());
                }

            }

            //Slot 6
            {
                //Ultimate Ability
                if(!baseformProperties.isAttacking() && baseformProperties.ultReady &&
                        KeyBindings.INSTANCE.useUltimateAbility.isDown())
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
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()-> Minecraft.getInstance().setScreen(new HelpScreen(baseformProperties.helpScreenPageNo)));
                    while(KeyBindings.INSTANCE.helpButton.consumeClick());
                }
            }
        });
    }

    public static void performClientSecond(AbstractClientPlayer player, CompoundTag playerNBT)
    {

    }

    public static void performDoublePress(AbstractClientPlayer player, BaseformProperties baseformProperties, DoubleTapDirection doubleTapDirection)
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
