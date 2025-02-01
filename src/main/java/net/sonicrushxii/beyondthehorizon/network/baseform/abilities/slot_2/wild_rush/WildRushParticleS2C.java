package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.wild_rush;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.sonicrushxii.beyondthehorizon.event_handler.client_handlers.ClientPacketHandler;

public class WildRushParticleS2C {

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

    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientPacketHandler.wildRushParticle(absX,absY,absZ);
        }));
        ctx.setPacketHandled(true);
    }
}
