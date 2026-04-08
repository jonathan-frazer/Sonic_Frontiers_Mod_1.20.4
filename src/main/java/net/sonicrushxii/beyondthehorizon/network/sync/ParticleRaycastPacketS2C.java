package net.sonicrushxii.beyondthehorizon.network.sync;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.event_handler.client_handlers.ClientPacketHandler;
import org.joml.Vector3f;

import java.util.Objects;

public class ParticleRaycastPacketS2C implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ParticleRaycastPacketS2C> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "particle_raycast_packet_s2c"));

    public static final StreamCodec<FriendlyByteBuf, ParticleRaycastPacketS2C> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), ParticleRaycastPacketS2C::new);

    private final Vector3f pos1;
    private final Vector3f pos2;
    private final String particleType;
    private final float red, green, blue, scale; // For DustParticleOptions

    public ParticleRaycastPacketS2C(ParticleOptions particleType, Vec3 pos1, Vec3 pos2) {
        this.particleType = Objects.requireNonNull(BuiltInRegistries.PARTICLE_TYPE.getKey(particleType.getType())).toString();
        this.pos1 = pos1.toVector3f();
        this.pos2 = pos2.toVector3f();

        if (particleType instanceof DustParticleOptions) {
            Vector3f color = ((DustParticleOptions) particleType).getColor();
            this.red = color.x();
            this.green = color.y();
            this.blue = color.z();
            this.scale = ((DustParticleOptions) particleType).getScale();
        } else {
            this.red = this.green = this.blue = this.scale = 0.0f;
        }
    }

    public ParticleRaycastPacketS2C(FriendlyByteBuf buf) {
        this.particleType = buf.readUtf(1024);
        this.pos1 = buf.readVector3f();
        this.pos2 = buf.readVector3f();
        this.red = buf.readFloat();
        this.green = buf.readFloat();
        this.blue = buf.readFloat();
        this.scale = buf.readFloat();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.particleType, 1024);
        buf.writeVector3f(this.pos1);
        buf.writeVector3f(this.pos2);
        buf.writeFloat(this.red);
        buf.writeFloat(this.green);
        buf.writeFloat(this.blue);
        buf.writeFloat(this.scale);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ParticleRaycastPacketS2C msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> ClientPacketHandler.clientParticleRaycast(msg.pos1, msg.pos2, msg.particleType, msg.red, msg.green, msg.blue, msg.scale));
    }
}
