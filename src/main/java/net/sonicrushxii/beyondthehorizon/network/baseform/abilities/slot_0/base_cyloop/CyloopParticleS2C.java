package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.base_cyloop;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.sonicrushxii.beyondthehorizon.Utilities;
import org.joml.Vector3f;

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
                            LocalPlayer player = Minecraft.getInstance().player;
                            Utilities.displayParticle(player, (new DustParticleOptions(new Vector3f(0.000f, 1.0f, 1.000f), 2f)),
                                    this.position.x, this.position.y + 0.5, this.position.z,
                                    0.55f, 0.55f, 0.55f,
                                    0.01, 3, true);
                            Utilities.displayParticle(player, (new DustParticleOptions(new Vector3f(0.000f, 0.11f, 1.000f), 2f)),
                                    this.position.x, this.position.y + 0.5, this.position.z,
                                    0.65f, 0.65f, 0.65f,
                                    0.01, 2, false);
                            Utilities.displayParticle(player, (new DustParticleOptions(new Vector3f(1.000f, 0.0f, 0.890f), 2f)),
                                    this.position.x, this.position.y + 0.5, this.position.z,
                                    0.55f, 0.55f, 0.55f,
                                    0.01, 2, false);
                            Utilities.displayParticle(player, (ParticleTypes.FIREWORK),
                                    this.position.x, this.position.y + 0.5, this.position.z,
                                    0.55f, 0.55f, 0.55f,
                                    0.01, 1, false);
                }));
        ctx.setPacketHandled(true);
    }
}


