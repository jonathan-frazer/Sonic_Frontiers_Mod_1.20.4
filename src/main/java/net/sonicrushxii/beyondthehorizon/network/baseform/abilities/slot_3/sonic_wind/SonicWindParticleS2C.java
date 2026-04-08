package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_wind;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.event_handler.client_handlers.ClientPacketHandler;

public class SonicWindParticleS2C implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<SonicWindParticleS2C> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "sonic_wind_particle_s2c"));

    public static final StreamCodec<FriendlyByteBuf, SonicWindParticleS2C> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), SonicWindParticleS2C::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public double absX,absY,absZ;
    public byte phase;

    public SonicWindParticleS2C(double absX, double absY, double absZ, byte phase) {
        this.absX = absX;
        this.absY = absY;
        this.absZ = absZ;
        this.phase = phase;
    }

    public SonicWindParticleS2C(FriendlyByteBuf buffer){
        this.absX = buffer.readDouble();
        this.absY = buffer.readDouble();
        this.absZ = buffer.readDouble();
        this.phase = buffer.readByte();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeDouble(absX);
        buffer.writeDouble(absY);
        buffer.writeDouble(absZ);
        buffer.writeByte(this.phase);
    }

    public static void handle(SonicWindParticleS2C msg, IPayloadContext ctx){
        ctx.enqueueWork(() -> {
            ClientPacketHandler.sonicWindParticle(msg.absX, msg.absY, msg.absZ, msg.phase);
        });
    }
}
