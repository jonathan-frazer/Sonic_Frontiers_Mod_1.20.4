package net.sonicrushxii.beyondthehorizon.network.sync;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.event_handler.client_handlers.ClientPacketHandler;
import org.joml.Vector3f;

import java.util.Objects;

public class ParticleDirPacketS2C implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ParticleDirPacketS2C> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "particle_dir_packet_s2c"));

    public static final StreamCodec<FriendlyByteBuf, ParticleDirPacketS2C> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), ParticleDirPacketS2C::new);

    private final String particleType;
    private final double absX, absY, absZ;
    private final double speedX,speedY,speedZ;
    private final float radiusX,radiusY,radiusZ;
    private final short count;
    private final boolean force;
    private final float red, green, blue, scale; // Optional for DustParticleOptions

    public ParticleDirPacketS2C(ParticleOptions particleType,
                                double absX, double absY, double absZ,
                                double speedX,double speedY,double speedZ,
                                float radiusX, float radiusY, float radiusZ,
                                int count, boolean force) {
        this.particleType = Objects.requireNonNull(BuiltInRegistries.PARTICLE_TYPE.getKey(particleType.getType())).toString();
        this.absX = absX;   this.absY = absY;   this.absZ = absZ;
        this.speedX = speedX; this.speedY = speedY; this.speedZ = speedZ;
        this.radiusX = radiusX; this.radiusY = radiusY; this.radiusZ = radiusZ;
        this.count = (short) count;
        this.force = force;

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

    public ParticleDirPacketS2C(ParticleOptions particleType,
                                double absX, double absY, double absZ,
                                double speedX, double speedY, double speedZ,
                                float radius,
                                int count, boolean force) {
        this.particleType = Objects.requireNonNull(BuiltInRegistries.PARTICLE_TYPE.getKey(particleType.getType())).toString();
        this.absX = absX;
        this.absY = absY;
        this.absZ = absZ;
        this.speedX = speedX;
        this.speedY = speedY;
        this.speedZ = speedZ;
        this.radiusX = radius;
        this.radiusY = radius;
        this.radiusZ = radius;
        this.count = (short) count;
        this.force = force;

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

    public ParticleDirPacketS2C(FriendlyByteBuf buf) {
        this.particleType = buf.readUtf(1024);
        this.absX = buf.readDouble();
        this.absY = buf.readDouble();
        this.absZ = buf.readDouble();
        this.speedX = buf.readDouble();
        this.speedY = buf.readDouble();
        this.speedZ = buf.readDouble();
        this.radiusX = buf.readFloat();
        this.radiusY = buf.readFloat();
        this.radiusZ = buf.readFloat();
        this.count = buf.readShort();
        this.force = buf.readBoolean();
        this.red = buf.readFloat();
        this.green = buf.readFloat();
        this.blue = buf.readFloat();
        this.scale = buf.readFloat();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.particleType);
        buf.writeDouble(this.absX);
        buf.writeDouble(this.absY);
        buf.writeDouble(this.absZ);
        buf.writeDouble(this.speedX);
        buf.writeDouble(this.speedY);
        buf.writeDouble(this.speedZ);
        buf.writeFloat(this.radiusX);
        buf.writeFloat(this.radiusY);
        buf.writeFloat(this.radiusZ);
        buf.writeShort(this.count);
        buf.writeBoolean(this.force);
        buf.writeFloat(this.red);
        buf.writeFloat(this.green);
        buf.writeFloat(this.blue);
        buf.writeFloat(this.scale);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ParticleDirPacketS2C msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            // This code is run on the client side
            ClientPacketHandler.clientParticleDir(msg.particleType,
                    msg.absX, msg.absY, msg.absZ,
                    msg.speedX, msg.speedY, msg.speedZ,
                    msg.radiusX, msg.radiusY, msg.radiusZ,
                    msg.count, msg.force,
                    msg.red, msg.green, msg.blue, msg.scale);
        });
    }
}
