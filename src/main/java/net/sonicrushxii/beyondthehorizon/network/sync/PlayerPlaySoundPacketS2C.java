package net.sonicrushxii.beyondthehorizon.network.sync;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.event_handler.client_handlers.ClientPacketHandler;

public class PlayerPlaySoundPacketS2C implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<PlayerPlaySoundPacketS2C> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "player_play_sound_packet_s2c"));

    public static final StreamCodec<FriendlyByteBuf, PlayerPlaySoundPacketS2C> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), PlayerPlaySoundPacketS2C::new);

    private final ResourceLocation soundLocation;
    private final BlockPos emitterPosition;
    private final float volume;
    private final float pitch;

    public PlayerPlaySoundPacketS2C(BlockPos emitterPosition, ResourceLocation soundLocation) {
        this.soundLocation = soundLocation;
        this.emitterPosition = emitterPosition;
        this.volume = 1.0f;
        this.pitch = 1.0f;
    }

    public PlayerPlaySoundPacketS2C(BlockPos emitterPosition, ResourceLocation soundLocation, float volume) {
        this.soundLocation = soundLocation;
        this.emitterPosition = emitterPosition;
        this.volume = volume;
        this.pitch = 1.0f;
    }

    public PlayerPlaySoundPacketS2C(BlockPos emitterPosition, ResourceLocation soundLocation, float volume, float pitch) {
        this.soundLocation = soundLocation;
        this.emitterPosition = emitterPosition;
        this.volume = volume;
        this.pitch = pitch;
    }

    public PlayerPlaySoundPacketS2C(FriendlyByteBuf buf) {
        this.soundLocation = buf.readResourceLocation();
        this.emitterPosition = buf.readBlockPos();
        this.volume = buf.readFloat();
        this.pitch = buf.readFloat();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.soundLocation);
        buf.writeBlockPos(this.emitterPosition);
        buf.writeFloat(this.volume);
        buf.writeFloat(this.pitch);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PlayerPlaySoundPacketS2C msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientPacketHandler.clientPlaysound(msg.soundLocation, msg.emitterPosition, msg.volume, msg.pitch);
        });
    }
}
