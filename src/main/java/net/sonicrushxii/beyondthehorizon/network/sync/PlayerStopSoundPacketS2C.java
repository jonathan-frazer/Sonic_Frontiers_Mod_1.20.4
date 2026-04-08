package net.sonicrushxii.beyondthehorizon.network.sync;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.event_handler.client_handlers.ClientPacketHandler;

public class PlayerStopSoundPacketS2C implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlayerStopSoundPacketS2C> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "player_stop_sound_packet_s2c"));

    public static final StreamCodec<FriendlyByteBuf, PlayerStopSoundPacketS2C> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), PlayerStopSoundPacketS2C::new);

    private final ResourceLocation soundLocation;

    public PlayerStopSoundPacketS2C(ResourceLocation soundLocation) {
        this.soundLocation = soundLocation;
    }

    public PlayerStopSoundPacketS2C(FriendlyByteBuf buf) {
        this.soundLocation = buf.readResourceLocation();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.soundLocation);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PlayerStopSoundPacketS2C msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientPacketHandler.clientStopSound(msg.soundLocation);
        });
    }
}
