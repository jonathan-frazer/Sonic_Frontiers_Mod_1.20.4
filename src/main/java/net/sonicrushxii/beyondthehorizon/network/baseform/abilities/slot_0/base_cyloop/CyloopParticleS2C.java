package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.base_cyloop;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.event_handler.client_handlers.ClientPacketHandler;

public class CyloopParticleS2C implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CyloopParticleS2C> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "cyloop_particle_s2c"));

    public static final StreamCodec<FriendlyByteBuf, CyloopParticleS2C> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), CyloopParticleS2C::new);

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

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CyloopParticleS2C msg, IPayloadContext ctx){
        ctx.enqueueWork(() -> {
            ClientPacketHandler.cyloopParticle(msg.position);
        });
    }
}


