package net.sonicrushxii.beyondthehorizon.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
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
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StartSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StopSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step.StepDown;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step.StepDownDouble;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.danger_sense.DangerSenseToggle;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.doublejump.*;
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
                INSTANCE.messageBuilder(DoubleJumpEnd.class, NetworkDirection.PLAY_TO_SERVER).encoder(DoubleJumpEnd::encode).decoder(DoubleJumpEnd::new).consumerMainThread(DoubleJumpEnd::handle).add();

                //Auto Step
                INSTANCE.messageBuilder(StepDown.class, NetworkDirection.PLAY_TO_SERVER).encoder(StepDown::encode).decoder(StepDown::new).consumerMainThread(StepDown::handle).add();
                INSTANCE.messageBuilder(StepDownDouble.class, NetworkDirection.PLAY_TO_SERVER).encoder(StepDownDouble::encode).decoder(StepDownDouble::new).consumerMainThread(StepDownDouble::handle).add();

                //Danger Sense
                INSTANCE.messageBuilder(DangerSenseToggle.class, NetworkDirection.PLAY_TO_SERVER).encoder(DangerSenseToggle::encode).decoder(DangerSenseToggle::new).consumerMainThread(DangerSenseToggle::handle).add();

                //Hunger
            }

            //Slot 1
            {
                //Air Boost
                INSTANCE.messageBuilder(AirBoost.class, NetworkDirection.PLAY_TO_SERVER).encoder(AirBoost::encode).decoder(AirBoost::new).consumerMainThread(AirBoost::handle).add();
                INSTANCE.messageBuilder(ResetAirBoost.class, NetworkDirection.PLAY_TO_SERVER).encoder(ResetAirBoost::encode).decoder(ResetAirBoost::new).consumerMainThread(ResetAirBoost::handle).add();
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
            }

            //Slot 2
            {
                INSTANCE.messageBuilder(ChargeSpindash.class, NetworkDirection.PLAY_TO_SERVER).encoder(ChargeSpindash::encode).decoder(ChargeSpindash::new).consumerMainThread(ChargeSpindash::handle).add();
                INSTANCE.messageBuilder(LaunchSpindash.class, NetworkDirection.PLAY_TO_SERVER).encoder(LaunchSpindash::encode).decoder(LaunchSpindash::new).consumerMainThread(LaunchSpindash::handle).add();
                INSTANCE.messageBuilder(RevertFromSpindash.class, NetworkDirection.PLAY_TO_SERVER).encoder(RevertFromSpindash::encode).decoder(RevertFromSpindash::new).consumerMainThread(RevertFromSpindash::handle).add();

                INSTANCE.messageBuilder(HomingAttack.class, NetworkDirection.PLAY_TO_SERVER).encoder(HomingAttack::encode).decoder(HomingAttack::new).consumerMainThread(HomingAttack::handle).add();

                INSTANCE.messageBuilder(Dodge.class,NetworkDirection.PLAY_TO_SERVER).encoder(Dodge::encode).decoder(Dodge::new).consumerMainThread(Dodge::handle).add();

                INSTANCE.messageBuilder(MeleeSwipes.class,NetworkDirection.PLAY_TO_SERVER).encoder(MeleeSwipes::encode).decoder(MeleeSwipes::new).consumerMainThread(MeleeSwipes::handle).add();

                INSTANCE.messageBuilder(SpeedBlitz.class,NetworkDirection.PLAY_TO_SERVER).encoder(SpeedBlitz::encode).decoder(SpeedBlitz::new).consumerMainThread(SpeedBlitz::handle).add();

                INSTANCE.messageBuilder(SmashHitToggle.class,NetworkDirection.PLAY_TO_SERVER).encoder(SmashHitToggle::encode).decoder(SmashHitToggle::new).consumerMainThread(SmashHitToggle::handle).add();
                INSTANCE.messageBuilder(SetSmashHitChargeC2S.class,NetworkDirection.PLAY_TO_SERVER).encoder(SetSmashHitChargeC2S::encode).decoder(SetSmashHitChargeC2S::new).consumerMainThread(SetSmashHitChargeC2S::handle).add();
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
