package net.sonicrushxii.beyondthehorizon.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.base_cyloop.Cyloop;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.base_cyloop.CyloopParticleS2C;
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
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.speed_blitz.SpeedBlitzOff;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash.ChargeSpindash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash.LaunchSpindash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.stomp.Stomp;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.loop_kick.LoopKick;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.spin_kick.CycloneKick;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.spin_kick.SpinSlash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.tornado_jump.LightSpeedAssault;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.tornado_jump.Mirage;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.tornado_jump.TornadoJump;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.wild_rush.WildRush;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.wild_rush.WildRushParticleS2C;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.wild_rush.WildRushRotationSyncS2C;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.cross_slash.CrossSlash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.cross_slash.EndCrossSlash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.homing_shot.HomingShot;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_boom.EndSonicBoom;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_boom.SonicBoom;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_wind.QuickSonicWind;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_wind.SonicWind;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_wind.SonicWindParticleS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.GoToVirtualSlotS2C;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_4.Parry;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_4.StopParry;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StartSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StopSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.danger_sense.DangerSenseToggle;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.doublejump.DoubleJump;
import net.sonicrushxii.beyondthehorizon.network.sync.*;
import net.sonicrushxii.beyondthehorizon.timehandler.TimeProjSync;

public class PacketHandler {
    private static final int PROTOCOL_VERSION = 1;
    private static final SimpleChannel INSTANCE = ChannelBuilder
            .named(new ResourceLocation(BeyondTheHorizon.MOD_ID, "main"))
            .networkProtocolVersion(PROTOCOL_VERSION)
            .simpleChannel();

    public static void register() {
        System.out.println("REGISTERED PACKET HANDLER");

        //Sync Packets
        {
            INSTANCE.messageBuilder(SyncPlayerFormS2C.class, NetworkDirection.PLAY_TO_CLIENT).encoder(SyncPlayerFormS2C::encode).decoder(SyncPlayerFormS2C::new).consumerMainThread(SyncPlayerFormS2C::handle).add();
            INSTANCE.messageBuilder(ParticleAuraPacketS2C.class, NetworkDirection.PLAY_TO_CLIENT).encoder(ParticleAuraPacketS2C::encode).decoder(ParticleAuraPacketS2C::new).consumerMainThread(ParticleAuraPacketS2C::handle).add();
            INSTANCE.messageBuilder(ParticleDirPacketS2C.class, NetworkDirection.PLAY_TO_CLIENT).encoder(ParticleDirPacketS2C::encode).decoder(ParticleDirPacketS2C::new).consumerMainThread(ParticleDirPacketS2C::handle).add();
            INSTANCE.messageBuilder(ParticleRaycastPacketS2C.class, NetworkDirection.PLAY_TO_CLIENT).encoder(ParticleRaycastPacketS2C::encode).decoder(ParticleRaycastPacketS2C::new).consumerMainThread(ParticleRaycastPacketS2C::handle).add();
            INSTANCE.messageBuilder(PlayerPlaySoundPacketS2C.class, NetworkDirection.PLAY_TO_CLIENT).encoder(PlayerPlaySoundPacketS2C::encode).decoder(PlayerPlaySoundPacketS2C::new).consumerMainThread(PlayerPlaySoundPacketS2C::handle).add();
            INSTANCE.messageBuilder(PlayerStopSoundPacketS2C.class, NetworkDirection.PLAY_TO_CLIENT).encoder(PlayerStopSoundPacketS2C::encode).decoder(PlayerStopSoundPacketS2C::new).consumerMainThread(PlayerStopSoundPacketS2C::handle).add();
            INSTANCE.messageBuilder(VirtualSlotSyncS2C.class, NetworkDirection.PLAY_TO_CLIENT).encoder(VirtualSlotSyncS2C::encode).decoder(VirtualSlotSyncS2C::new).consumerMainThread(VirtualSlotSyncS2C::handle).add();
            INSTANCE.messageBuilder(TimeProjSync.class, NetworkDirection.PLAY_TO_CLIENT).encoder(TimeProjSync::encode).decoder(TimeProjSync::new).consumerMainThread(TimeProjSync::handle).add();
        }

        //Base form
        {
            //Passives
            {
                INSTANCE.messageBuilder(StartSprint.class, NetworkDirection.PLAY_TO_SERVER).encoder(StartSprint::encode).decoder(StartSprint::new).consumerMainThread(StartSprint::handle).add();
                INSTANCE.messageBuilder(StopSprint.class, NetworkDirection.PLAY_TO_SERVER).encoder(StopSprint::encode).decoder(StopSprint::new).consumerMainThread(StopSprint::handle).add();

                //Double Jump
                INSTANCE.messageBuilder(DoubleJump.class, NetworkDirection.PLAY_TO_SERVER).encoder(DoubleJump::encode).decoder(DoubleJump::new).consumerMainThread(DoubleJump::handle).add();

                //Auto Step

                //Danger Sense
                INSTANCE.messageBuilder(DangerSenseToggle.class, NetworkDirection.PLAY_TO_SERVER).encoder(DangerSenseToggle::encode).decoder(DangerSenseToggle::new).consumerMainThread(DangerSenseToggle::handle).add();

                //Hunger
            }

            //Slot 1
            {
                //Air Boost
                INSTANCE.messageBuilder(AirBoost.class, NetworkDirection.PLAY_TO_SERVER).encoder(AirBoost::encode).decoder(AirBoost::new).consumerMainThread(AirBoost::handle).add();
                //Boost
                INSTANCE.messageBuilder(Boost.class, NetworkDirection.PLAY_TO_SERVER).encoder(Boost::encode).decoder(Boost::new).consumerMainThread(Boost::handle).add();
                //Quick Step
                INSTANCE.messageBuilder(Sidestep.class, NetworkDirection.PLAY_TO_SERVER).encoder(Sidestep::encode).decoder(Sidestep::new).consumerMainThread(Sidestep::handle).add();

                //Light Speed Attack
                INSTANCE.messageBuilder(LightspeedCharge.class, NetworkDirection.PLAY_TO_SERVER).encoder(LightspeedCharge::encode).decoder(LightspeedCharge::new).consumerMainThread(LightspeedCharge::handle).add();
                INSTANCE.messageBuilder(LightspeedEffect.class, NetworkDirection.PLAY_TO_SERVER).encoder(LightspeedEffect::encode).decoder(LightspeedEffect::new).consumerMainThread(LightspeedEffect::handle).add();
                INSTANCE.messageBuilder(LightspeedDecay.class, NetworkDirection.PLAY_TO_SERVER).encoder(LightspeedDecay::encode).decoder(LightspeedDecay::new).consumerMainThread(LightspeedDecay::handle).add();
                INSTANCE.messageBuilder(LightspeedCancel.class, NetworkDirection.PLAY_TO_SERVER).encoder(LightspeedCancel::encode).decoder(LightspeedCancel::new).consumerMainThread(LightspeedCancel::handle).add();

                //Power Boost
                INSTANCE.messageBuilder(PowerBoostActivate.class, NetworkDirection.PLAY_TO_SERVER).encoder(PowerBoostActivate::encode).decoder(PowerBoostActivate::new).consumerMainThread(PowerBoostActivate::handle).add();
                INSTANCE.messageBuilder(PowerBoostDeactivate.class, NetworkDirection.PLAY_TO_SERVER).encoder(PowerBoostDeactivate::encode).decoder(PowerBoostDeactivate::new).consumerMainThread(PowerBoostDeactivate::handle).add();

                //Cyloop
                INSTANCE.messageBuilder(Cyloop.class,NetworkDirection.PLAY_TO_SERVER).encoder(Cyloop::encode).decoder(Cyloop::new).consumerMainThread(Cyloop::handle).add();
                INSTANCE.messageBuilder(CyloopParticleS2C.class,NetworkDirection.PLAY_TO_CLIENT).encoder(CyloopParticleS2C::encode).decoder(CyloopParticleS2C::new).consumerMainThread(CyloopParticleS2C::handle).add();

                //Quick Cyloop
                INSTANCE.messageBuilder(QuickCyloop.class,NetworkDirection.PLAY_TO_SERVER).encoder(QuickCyloop::encode).decoder(QuickCyloop::new).consumerMainThread(QuickCyloop::handle).add();
            }

            //Slot 2
            {
                //Spin Dash
                INSTANCE.messageBuilder(ChargeSpindash.class, NetworkDirection.PLAY_TO_SERVER).encoder(ChargeSpindash::encode).decoder(ChargeSpindash::new).consumerMainThread(ChargeSpindash::handle).add();
                INSTANCE.messageBuilder(LaunchSpindash.class, NetworkDirection.PLAY_TO_SERVER).encoder(LaunchSpindash::encode).decoder(LaunchSpindash::new).consumerMainThread(LaunchSpindash::handle).add();

                //Homing Attack
                INSTANCE.messageBuilder(HomingAttack.class, NetworkDirection.PLAY_TO_SERVER).encoder(HomingAttack::encode).decoder(HomingAttack::new).consumerMainThread(HomingAttack::handle).add();

                //Dodge
                INSTANCE.messageBuilder(Dodge.class,NetworkDirection.PLAY_TO_SERVER).encoder(Dodge::encode).decoder(Dodge::new).consumerMainThread(Dodge::handle).add();

                //Humming Top
                INSTANCE.messageBuilder(HummingTop.class,NetworkDirection.PLAY_TO_SERVER).encoder(HummingTop::encode).decoder(HummingTop::new).consumerMainThread(HummingTop::handle).add();

                //Speed Blitz
                INSTANCE.messageBuilder(SpeedBlitz.class,NetworkDirection.PLAY_TO_SERVER).encoder(SpeedBlitz::encode).decoder(SpeedBlitz::new).consumerMainThread(SpeedBlitz::handle).add();
                INSTANCE.messageBuilder(SpeedBlitzDash.class,NetworkDirection.PLAY_TO_SERVER).encoder(SpeedBlitzDash::encode).decoder(SpeedBlitzDash::new).consumerMainThread(SpeedBlitzDash::handle).add();
                INSTANCE.messageBuilder(SpeedBlitzOff.class,NetworkDirection.PLAY_TO_SERVER).encoder(SpeedBlitzOff::encode).decoder(SpeedBlitzOff::new).consumerMainThread(SpeedBlitzOff::handle).add();

                //Set Smash Hit Charge
                INSTANCE.messageBuilder(SetSmashHitChargeC2S.class,NetworkDirection.PLAY_TO_SERVER).encoder(SetSmashHitChargeC2S::encode).decoder(SetSmashHitChargeC2S::new).consumerMainThread(SetSmashHitChargeC2S::handle).add();

                //Stomp
                INSTANCE.messageBuilder(Stomp.class,NetworkDirection.PLAY_TO_SERVER).encoder(Stomp::encode).decoder(Stomp::new).consumerMainThread(Stomp::handle).add();
            }

            //Slot 3
            {
                // Tornado Jump
                INSTANCE.messageBuilder(TornadoJump.class,NetworkDirection.PLAY_TO_SERVER).encoder(TornadoJump::encode).decoder(TornadoJump::new).consumerMainThread(TornadoJump::handle).add();
                INSTANCE.messageBuilder(Mirage.class,NetworkDirection.PLAY_TO_SERVER).encoder(Mirage::encode).decoder(Mirage::new).consumerMainThread(Mirage::handle).add();
                INSTANCE.messageBuilder(LightSpeedAssault.class,NetworkDirection.PLAY_TO_SERVER).encoder(LightSpeedAssault::encode).decoder(LightSpeedAssault::new).consumerMainThread(LightSpeedAssault::handle).add();

                //Spin Slash
                INSTANCE.messageBuilder(SpinSlash.class,NetworkDirection.PLAY_TO_SERVER).encoder(SpinSlash::encode).decoder(SpinSlash::new).consumerMainThread(SpinSlash::handle).add();

                //Cyclone Kick
                INSTANCE.messageBuilder(CycloneKick.class,NetworkDirection.PLAY_TO_SERVER).encoder(CycloneKick::encode).decoder(CycloneKick::new).consumerMainThread(CycloneKick::handle).add();

                //Wild Rush
                INSTANCE.messageBuilder(WildRush.class,NetworkDirection.PLAY_TO_SERVER).encoder(WildRush::encode).decoder(WildRush::new).consumerMainThread(WildRush::handle).add();
                INSTANCE.messageBuilder(WildRushRotationSyncS2C.class,NetworkDirection.PLAY_TO_CLIENT).encoder(WildRushRotationSyncS2C::encode).decoder(WildRushRotationSyncS2C::new).consumerMainThread(WildRushRotationSyncS2C::handle).add();
                INSTANCE.messageBuilder(WildRushParticleS2C.class,NetworkDirection.PLAY_TO_CLIENT).encoder(WildRushParticleS2C::encode).decoder(WildRushParticleS2C::new).consumerMainThread(WildRushParticleS2C::handle).add();

                //Loop Kick
                INSTANCE.messageBuilder(LoopKick.class,NetworkDirection.PLAY_TO_SERVER).encoder(LoopKick::encode).decoder(LoopKick::new).consumerMainThread(LoopKick::handle).add();
            }

            //Slot 4
            {
                //Cross Slash
                INSTANCE.messageBuilder(CrossSlash.class,NetworkDirection.PLAY_TO_SERVER).encoder(CrossSlash::encode).decoder(CrossSlash::new).consumerMainThread(CrossSlash::handle).add();
                INSTANCE.messageBuilder(EndCrossSlash.class,NetworkDirection.PLAY_TO_SERVER).encoder(EndCrossSlash::encode).decoder(EndCrossSlash::new).consumerMainThread(EndCrossSlash::handle).add();

                //Homing Shot
                INSTANCE.messageBuilder(HomingShot.class,NetworkDirection.PLAY_TO_SERVER).encoder(HomingShot::encode).decoder(HomingShot::new).consumerMainThread(HomingShot::handle).add();

                //Sonic Boom
                INSTANCE.messageBuilder(SonicBoom.class,NetworkDirection.PLAY_TO_SERVER).encoder(SonicBoom::encode).decoder(SonicBoom::new).consumerMainThread(SonicBoom::handle).add();
                INSTANCE.messageBuilder(EndSonicBoom.class,NetworkDirection.PLAY_TO_SERVER).encoder(EndSonicBoom::encode).decoder(EndSonicBoom::new).consumerMainThread(EndSonicBoom::handle).add();

                //Sonic Wind
                INSTANCE.messageBuilder(SonicWind.class,NetworkDirection.PLAY_TO_SERVER).encoder(SonicWind::encode).decoder(SonicWind::new).consumerMainThread(SonicWind::handle).add();
                INSTANCE.messageBuilder(QuickSonicWind.class,NetworkDirection.PLAY_TO_SERVER).encoder(QuickSonicWind::encode).decoder(QuickSonicWind::new).consumerMainThread(QuickSonicWind::handle).add();
                INSTANCE.messageBuilder(SonicWindParticleS2C.class,NetworkDirection.PLAY_TO_CLIENT).encoder(SonicWindParticleS2C::encode).decoder(SonicWindParticleS2C::new).consumerMainThread(SonicWindParticleS2C::handle).add();
            }

            //Slot 5
            {
                //Parry
                INSTANCE.messageBuilder(Parry.class,NetworkDirection.PLAY_TO_SERVER).encoder(Parry::encode).decoder(Parry::new).consumerMainThread(Parry::handle).add();
                INSTANCE.messageBuilder(StopParry.class,NetworkDirection.PLAY_TO_SERVER).encoder(StopParry::encode).decoder(StopParry::new).consumerMainThread(StopParry::handle).add();
                INSTANCE.messageBuilder(GoToVirtualSlotS2C.class,NetworkDirection.PLAY_TO_CLIENT).encoder(GoToVirtualSlotS2C::encode).decoder(GoToVirtualSlotS2C::new).consumerMainThread(GoToVirtualSlotS2C::handle).add();
            }
        }

        //Super form
        {

        }

        //Starfall form
        {

        }

        //Hyper form
        {

        }
    }

    public static void sendToServer(Object msg) {
        INSTANCE.send(msg,PacketDistributor.SERVER.noArg());
    }

    public static void sendToPlayer(ServerPlayer player, Object msg) {
        INSTANCE.send(msg,PacketDistributor.PLAYER.with(player));
    }

    public static void sendToChunkPlayers(LevelChunk levelChunk, Object msg) {
        INSTANCE.send(msg,PacketDistributor.TRACKING_CHUNK.with(levelChunk));
    }

    public static void sendToALLPlayers(Object msg) {
        INSTANCE.send(msg,PacketDistributor.ALL.noArg());
    }

}
