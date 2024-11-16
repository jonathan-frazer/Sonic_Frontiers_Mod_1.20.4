package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.wild_rush;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.sonicrushxii.beyondthehorizon.Utilities;
import org.joml.Vector3f;

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
            Minecraft mc = Minecraft.getInstance();
            ClientLevel world = mc.level;
            LocalPlayer player = mc.player;

            if (player != null && world != null)
            {
                Utilities.displayParticle(player,
                        ParticleTypes.FIREWORK,
                        this.absX, this.absY, this.absZ,
                        0.5f, 0.5f, 0.5f,
                        0.01, 10, false);
                Utilities.displayParticle(player,
                        new DustParticleOptions(new Vector3f(0f,0f,1f),1.5f),
                        this.absX, this.absY, this.absZ,
                        0.5f, 0.5f, 0.5f,
                        0.01, 10, false);
            }
        }));
        ctx.setPacketHandled(true);
    }
}
