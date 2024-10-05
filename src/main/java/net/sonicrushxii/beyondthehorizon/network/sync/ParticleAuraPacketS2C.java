package net.sonicrushxii.beyondthehorizon.network.sync;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonicrushxii.beyondthehorizon.Utilities;
import org.joml.Vector3f;

public class ParticleAuraPacketS2C {
    private final String particleType;
    private final double absX, absY, absZ;
    private final double speed;
    private final float radiusX,radiusY,radiusZ;
    private final short count;
    private final boolean force;
    private final float red, green, blue, scale; // Optional for DustParticleOptions

    public ParticleAuraPacketS2C(ParticleOptions particleType,
                                 double absX, double absY, double absZ,
                                 double speed,
                                 float radiusX, float radiusY, float radiusZ,
                                 int count, boolean force) {
        this.particleType = ForgeRegistries.PARTICLE_TYPES.getKey(particleType.getType()).toString();
        this.absX = absX;   this.absY = absY;   this.absZ = absZ;
        this.speed = speed;
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

    public ParticleAuraPacketS2C(ParticleOptions particleType,
                                 double absX, double absY, double absZ,
                                 double speed,
                                 float radius,
                                 int count, boolean force) {
        this.particleType = ForgeRegistries.PARTICLE_TYPES.getKey(particleType.getType()).toString();
        this.absX = absX;
        this.absY = absY;
        this.absZ = absZ;
        this.speed = speed;
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

    public ParticleAuraPacketS2C(FriendlyByteBuf buf) {
        this.particleType = buf.readUtf(1024);
        this.absX = buf.readDouble();
        this.absY = buf.readDouble();
        this.absZ = buf.readDouble();
        this.speed = buf.readDouble();
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
        buf.writeDouble(this.speed);
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
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                Minecraft mc = Minecraft.getInstance();
                ClientLevel world = mc.level;
                LocalPlayer player = mc.player;

                if (player != null && world != null) {
                    ParticleType<?> particleType = ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(this.particleType));
                    ParticleOptions particleOptions;

                    if (particleType == ParticleTypes.DUST) {
                        particleOptions = new DustParticleOptions(new Vector3f(this.red, this.green, this.blue), this.scale);
                    } else {
                        particleOptions = (ParticleOptions) particleType;
                    }

                    assert particleOptions != null;
                    Utilities.displayParticle(player, particleOptions, this.absX, this.absY, this.absZ,
                            this.radiusX, this.radiusY, this.radiusZ,
                            this.speed, this.count, this.force);
                }
            });
        });

        ctx.setPacketHandled(true);
    }
}
