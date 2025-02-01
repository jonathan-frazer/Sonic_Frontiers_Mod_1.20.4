package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_wind;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.sonicrushxii.beyondthehorizon.event_handler.client_handlers.ClientPacketHandler;

public class SonicWindParticleS2C
{
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

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(() -> {
            // This code is run on the client side
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ClientPacketHandler.sonicWindParticle(absX,absY,absZ, phase);
            });
        });
    }
}
