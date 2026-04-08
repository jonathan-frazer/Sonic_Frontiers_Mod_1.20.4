package net.sonicrushxii.beyondthehorizon.network;

import com.mojang.logging.LogUtils;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
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
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash.SpindashBreak;
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
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_4.grand_slam.GrandSlam;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_4.parry.Parry;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_4.parry.StopParry;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_5.ultimate_ability.UltimateActivate;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.HelpScreenSync;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StartSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StopSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.danger_sense.DangerSenseToggle;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.doublejump.DoubleJump;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.wall_boost.WallBoost;
import net.sonicrushxii.beyondthehorizon.network.sync.*;
import net.sonicrushxii.beyondthehorizon.timehandler.TimeProjSync;
import org.slf4j.Logger;

public class PacketHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        LOGGER.info("Registering payload handlers");
        PayloadRegistrar registrar = event.registrar(BeyondTheHorizon.MOD_ID).versioned("1");

        //Sync Packets (S2C)
        registrar.playToClient(SyncPlayerFormS2C.TYPE, SyncPlayerFormS2C.STREAM_CODEC, SyncPlayerFormS2C::handle);
        registrar.playToClient(ParticleAuraPacketS2C.TYPE, ParticleAuraPacketS2C.STREAM_CODEC, ParticleAuraPacketS2C::handle);
        registrar.playToClient(ParticleDirPacketS2C.TYPE, ParticleDirPacketS2C.STREAM_CODEC, ParticleDirPacketS2C::handle);
        registrar.playToClient(ParticleRaycastPacketS2C.TYPE, ParticleRaycastPacketS2C.STREAM_CODEC, ParticleRaycastPacketS2C::handle);
        registrar.playToClient(PlayerPlaySoundPacketS2C.TYPE, PlayerPlaySoundPacketS2C.STREAM_CODEC, PlayerPlaySoundPacketS2C::handle);
        registrar.playToClient(PlayerStopSoundPacketS2C.TYPE, PlayerStopSoundPacketS2C.STREAM_CODEC, PlayerStopSoundPacketS2C::handle);
        registrar.playToClient(VirtualSlotSyncS2C.TYPE, VirtualSlotSyncS2C.STREAM_CODEC, VirtualSlotSyncS2C::handle);
        registrar.playToClient(TimeProjSync.TYPE, TimeProjSync.STREAM_CODEC, TimeProjSync::handle);
        registrar.playToClient(GoToVirtualSlotS2C.TYPE, GoToVirtualSlotS2C.STREAM_CODEC, GoToVirtualSlotS2C::handle);
        registrar.playToClient(CyloopParticleS2C.TYPE, CyloopParticleS2C.STREAM_CODEC, CyloopParticleS2C::handle);
        registrar.playToClient(WildRushRotationSyncS2C.TYPE, WildRushRotationSyncS2C.STREAM_CODEC, WildRushRotationSyncS2C::handle);
        registrar.playToClient(WildRushParticleS2C.TYPE, WildRushParticleS2C.STREAM_CODEC, WildRushParticleS2C::handle);
        registrar.playToClient(SonicWindParticleS2C.TYPE, SonicWindParticleS2C.STREAM_CODEC, SonicWindParticleS2C::handle);

        //Base form (C2S)
        //HelpScreen
        registrar.playToServer(HelpScreenSync.TYPE, HelpScreenSync.STREAM_CODEC, HelpScreenSync::handle);

        //Passives
        registrar.playToServer(StartSprint.TYPE, StartSprint.STREAM_CODEC, StartSprint::handle);
        registrar.playToServer(StopSprint.TYPE, StopSprint.STREAM_CODEC, StopSprint::handle);
        registrar.playToServer(DoubleJump.TYPE, DoubleJump.STREAM_CODEC, DoubleJump::handle);
        registrar.playToServer(DangerSenseToggle.TYPE, DangerSenseToggle.STREAM_CODEC, DangerSenseToggle::handle);
        registrar.playToServer(WallBoost.TYPE, WallBoost.STREAM_CODEC, WallBoost::handle);

        //Slot 1
        registrar.playToServer(AirBoost.TYPE, AirBoost.STREAM_CODEC, AirBoost::handle);
        registrar.playToServer(Boost.TYPE, Boost.STREAM_CODEC, Boost::handle);
        registrar.playToServer(Sidestep.TYPE, Sidestep.STREAM_CODEC, Sidestep::handle);
        registrar.playToServer(LightspeedCharge.TYPE, LightspeedCharge.STREAM_CODEC, LightspeedCharge::handle);
        registrar.playToServer(LightspeedEffect.TYPE, LightspeedEffect.STREAM_CODEC, LightspeedEffect::handle);
        registrar.playToServer(LightspeedDecay.TYPE, LightspeedDecay.STREAM_CODEC, LightspeedDecay::handle);
        registrar.playToServer(LightspeedCancel.TYPE, LightspeedCancel.STREAM_CODEC, LightspeedCancel::handle);
        registrar.playToServer(PowerBoostActivate.TYPE, PowerBoostActivate.STREAM_CODEC, PowerBoostActivate::handle);
        registrar.playToServer(PowerBoostDeactivate.TYPE, PowerBoostDeactivate.STREAM_CODEC, PowerBoostDeactivate::handle);
        registrar.playToServer(Cyloop.TYPE, Cyloop.STREAM_CODEC, Cyloop::handle);
        registrar.playToServer(QuickCyloop.TYPE, QuickCyloop.STREAM_CODEC, QuickCyloop::handle);

        //Slot 2
        registrar.playToServer(ChargeSpindash.TYPE, ChargeSpindash.STREAM_CODEC, ChargeSpindash::handle);
        registrar.playToServer(SpindashBreak.TYPE, SpindashBreak.STREAM_CODEC, SpindashBreak::handle);
        registrar.playToServer(LaunchSpindash.TYPE, LaunchSpindash.STREAM_CODEC, LaunchSpindash::handle);
        registrar.playToServer(HomingAttack.TYPE, HomingAttack.STREAM_CODEC, HomingAttack::handle);
        registrar.playToServer(Dodge.TYPE, Dodge.STREAM_CODEC, Dodge::handle);
        registrar.playToServer(HummingTop.TYPE, HummingTop.STREAM_CODEC, HummingTop::handle);
        registrar.playToServer(SpeedBlitz.TYPE, SpeedBlitz.STREAM_CODEC, SpeedBlitz::handle);
        registrar.playToServer(SpeedBlitzDash.TYPE, SpeedBlitzDash.STREAM_CODEC, SpeedBlitzDash::handle);
        registrar.playToServer(SpeedBlitzOff.TYPE, SpeedBlitzOff.STREAM_CODEC, SpeedBlitzOff::handle);
        registrar.playToServer(SetSmashHitChargeC2S.TYPE, SetSmashHitChargeC2S.STREAM_CODEC, SetSmashHitChargeC2S::handle);
        registrar.playToServer(Stomp.TYPE, Stomp.STREAM_CODEC, Stomp::handle);

        //Slot 3
        registrar.playToServer(TornadoJump.TYPE, TornadoJump.STREAM_CODEC, TornadoJump::handle);
        registrar.playToServer(Mirage.TYPE, Mirage.STREAM_CODEC, Mirage::handle);
        registrar.playToServer(LightSpeedAssault.TYPE, LightSpeedAssault.STREAM_CODEC, LightSpeedAssault::handle);
        registrar.playToServer(SpinSlash.TYPE, SpinSlash.STREAM_CODEC, SpinSlash::handle);
        registrar.playToServer(CycloneKick.TYPE, CycloneKick.STREAM_CODEC, CycloneKick::handle);
        registrar.playToServer(WildRush.TYPE, WildRush.STREAM_CODEC, WildRush::handle);
        registrar.playToServer(LoopKick.TYPE, LoopKick.STREAM_CODEC, LoopKick::handle);

        //Slot 4
        registrar.playToServer(CrossSlash.TYPE, CrossSlash.STREAM_CODEC, CrossSlash::handle);
        registrar.playToServer(EndCrossSlash.TYPE, EndCrossSlash.STREAM_CODEC, EndCrossSlash::handle);
        registrar.playToServer(HomingShot.TYPE, HomingShot.STREAM_CODEC, HomingShot::handle);
        registrar.playToServer(SonicBoom.TYPE, SonicBoom.STREAM_CODEC, SonicBoom::handle);
        registrar.playToServer(EndSonicBoom.TYPE, EndSonicBoom.STREAM_CODEC, EndSonicBoom::handle);
        registrar.playToServer(SonicWind.TYPE, SonicWind.STREAM_CODEC, SonicWind::handle);
        registrar.playToServer(QuickSonicWind.TYPE, QuickSonicWind.STREAM_CODEC, QuickSonicWind::handle);

        //Slot 5
        registrar.playToServer(Parry.TYPE, Parry.STREAM_CODEC, Parry::handle);
        registrar.playToServer(StopParry.TYPE, StopParry.STREAM_CODEC, StopParry::handle);
        registrar.playToServer(GrandSlam.TYPE, GrandSlam.STREAM_CODEC, GrandSlam::handle);

        //Slot 6
        registrar.playToServer(UltimateActivate.TYPE, UltimateActivate.STREAM_CODEC, UltimateActivate::handle);
    }

    public static void sendToServer(CustomPacketPayload msg) {
        PacketDistributor.sendToServer(msg);
    }

    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload msg) {
        PacketDistributor.sendToPlayer(player, msg);
    }

    public static void sendToChunkPlayers(LevelChunk levelChunk, CustomPacketPayload msg) {
        PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) levelChunk.getLevel(), levelChunk.getPos(), msg);
    }

    public static void sendToALLPlayers(CustomPacketPayload msg) {
        PacketDistributor.sendToAllPlayers(msg);
    }
}
