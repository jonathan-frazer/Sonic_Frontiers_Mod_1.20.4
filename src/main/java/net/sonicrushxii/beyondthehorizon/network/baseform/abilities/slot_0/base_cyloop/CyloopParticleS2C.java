package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.base_cyloop;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.sonicrushxii.beyondthehorizon.event_handler.client_handlers.ClientPacketHandler;

public class CyloopParticleS2C {

    private final Vec3 position;

    public CyloopParticleS2C(Vec3 position) {
        this.position = position;
    }

    public CyloopParticleS2C(FriendlyByteBuf buffer){
        this.position = buffer.readVec3();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeVec3(this.position);
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientPacketHandler.cyloopParticle(position);
        }));
        ctx.setPacketHandled(true);
    }
}


