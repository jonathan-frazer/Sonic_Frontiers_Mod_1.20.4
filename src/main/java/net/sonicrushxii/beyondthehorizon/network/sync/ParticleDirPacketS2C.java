package net.sonicrushxii.beyondthehorizon.network.sync;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonicrushxii.beyondthehorizon.event_handler.client_handlers.ClientPacketHandler;
import org.joml.Vector3f;

import java.util.Objects;

public class ParticleDirPacketS2C {
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
        this.particleType = Objects.requireNonNull(ForgeRegistries.PARTICLE_TYPES.getKey(particleType.getType())).toString();
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
        this.particleType = Objects.requireNonNull(ForgeRegistries.PARTICLE_TYPES.getKey(particleType.getType())).toString();
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

    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // This code is run on the client side
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.clientParticleDir(this.particleType,
                    this.absX,this.absY,this.absZ,
                    this.speedX,this.speedY,this.speedZ,
                    this.radiusX,this.radiusY,this.radiusZ,
                    this.count,this.force,
                    this.red,this.green,this.blue,this.scale));
        });

        ctx.setPacketHandled(true);
    }
}
