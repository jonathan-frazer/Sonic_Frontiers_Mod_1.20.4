package net.sonicrushxii.beyondthehorizon.network.sync;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonicrushxii.beyondthehorizon.Utilities;
import org.joml.Vector3f;

public class ParticleRaycastPacketS2C {
    private final Vector3f pos1;
    private final Vector3f pos2;
    private final String particleType;
    private final float red, green, blue, scale; // For DustParticleOptions

    public ParticleRaycastPacketS2C(ParticleOptions particleType, Vec3 pos1, Vec3 pos2) {
        this.particleType = ForgeRegistries.PARTICLE_TYPES.getKey(particleType.getType()).toString();
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

    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientLevel world = Minecraft.getInstance().level;
            if (world != null) {
                ParticleType<?> particleType = ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(this.particleType));
                ParticleOptions particleOptions;

                if (particleType == ParticleTypes.DUST) {
                    particleOptions = new DustParticleOptions(new Vector3f(this.red, this.green, this.blue), this.scale);
                } else {
                    particleOptions = (ParticleOptions) particleType;
                }

                Utilities.particleRaycast(
                        world, particleOptions,
                        new Vec3(pos1.x(), pos1.y(), pos1.z()),
                        new Vec3(pos2.x(), pos2.y(), pos2.z())
                );
            }
        }));
        ctx.setPacketHandled(true);
    }
}
