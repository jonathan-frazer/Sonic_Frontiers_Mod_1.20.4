package net.sonicrushxii.beyondthehorizon.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.network.test.TestPacket;

public class PacketHandler {
    private static int PROTOCOL_VERSION = 1;
    private static final SimpleChannel INSTANCE = ChannelBuilder
            .named(new ResourceLocation(BeyondTheHorizon.MOD_ID, "main"))
            .networkProtocolVersion(PROTOCOL_VERSION)
            .simpleChannel();

    public static void register() {
        System.out.println("REGISTERED PACKET HANDLER");
        INSTANCE.messageBuilder(TestPacket.class, NetworkDirection.PLAY_TO_SERVER).encoder(TestPacket::encode).decoder(TestPacket::new).consumerMainThread(TestPacket::handle).add();
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
