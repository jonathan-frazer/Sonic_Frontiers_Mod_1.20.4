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
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StartSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StopSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step.StepDown;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step.StepDownDouble;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.danger_sense.DangerSenseToggle;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.doublejump.*;
import net.sonicrushxii.beyondthehorizon.network.sync.*;

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
            INSTANCE.messageBuilder(ParticleRaycastPacketS2C.class, NetworkDirection.PLAY_TO_CLIENT).encoder(ParticleRaycastPacketS2C::encode).decoder(ParticleRaycastPacketS2C::new).consumerMainThread(ParticleRaycastPacketS2C::handle).add();
            INSTANCE.messageBuilder(PlayerPlaySoundPacketS2C.class, NetworkDirection.PLAY_TO_CLIENT).encoder(PlayerPlaySoundPacketS2C::encode).decoder(PlayerPlaySoundPacketS2C::new).consumerMainThread(PlayerPlaySoundPacketS2C::handle).add();
            INSTANCE.messageBuilder(PlayerStopSoundPacketS2C.class, NetworkDirection.PLAY_TO_CLIENT).encoder(PlayerStopSoundPacketS2C::encode).decoder(PlayerStopSoundPacketS2C::new).consumerMainThread(PlayerStopSoundPacketS2C::handle).add();
            INSTANCE.messageBuilder(VirtualSlotSyncS2C.class, NetworkDirection.PLAY_TO_CLIENT).encoder(VirtualSlotSyncS2C::encode).decoder(VirtualSlotSyncS2C::new).consumerMainThread(VirtualSlotSyncS2C::handle).add();
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
                INSTANCE.messageBuilder(SidestepLeft.class, NetworkDirection.PLAY_TO_SERVER).encoder(SidestepLeft::encode).decoder(SidestepLeft::new).consumerMainThread(SidestepLeft::handle).add();
                INSTANCE.messageBuilder(SidestepRight.class, NetworkDirection.PLAY_TO_SERVER).encoder(SidestepRight::encode).decoder(SidestepRight::new).consumerMainThread(SidestepRight::handle).add();

                //Light Speed Attack
                INSTANCE.messageBuilder(LightspeedCharge.class, NetworkDirection.PLAY_TO_SERVER).encoder(LightspeedCharge::encode).decoder(LightspeedCharge::new).consumerMainThread(LightspeedCharge::handle).add();
                INSTANCE.messageBuilder(LightspeedEffect.class, NetworkDirection.PLAY_TO_SERVER).encoder(LightspeedEffect::encode).decoder(LightspeedEffect::new).consumerMainThread(LightspeedEffect::handle).add();
                INSTANCE.messageBuilder(LightspeedDecay.class, NetworkDirection.PLAY_TO_SERVER).encoder(LightspeedDecay::encode).decoder(LightspeedDecay::new).consumerMainThread(LightspeedDecay::handle).add();
                INSTANCE.messageBuilder(LightspeedCancel.class, NetworkDirection.PLAY_TO_SERVER).encoder(LightspeedCancel::encode).decoder(LightspeedCancel::new).consumerMainThread(LightspeedCancel::handle).add();
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
