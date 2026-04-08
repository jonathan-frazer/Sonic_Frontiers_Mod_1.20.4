package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.wild_rush;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.event_handler.client_handlers.ClientPacketHandler;

public class WildRushParticleS2C implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<WildRushParticleS2C> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "wild_rush_particle_s2c"));

    public static final StreamCodec<FriendlyByteBuf, WildRushParticleS2C> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), WildRushParticleS2C::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    double absX;
    double absY;
    double absZ;

    public WildRushParticleS2C(double absX, double absY, double absZ) {
        this.absX = absX;
        this.absY = absY;
        this.absZ = absZ;
    }

    public WildRushParticleS2C(FriendlyByteBuf buf) {
        this.absX = buf.readDouble();
        this.absY = buf.readDouble();
        this.absZ = buf.readDouble();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(this.absX);
        buf.writeDouble(this.absY);
        buf.writeDouble(this.absZ);
    }

    public static void handle(WildRushParticleS2C msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientPacketHandler.wildRushParticle(msg.absX, msg.absY, msg.absZ);
        });
    }
}
