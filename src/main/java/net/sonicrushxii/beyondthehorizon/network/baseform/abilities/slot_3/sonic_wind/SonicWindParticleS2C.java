package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_wind;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.sonicrushxii.beyondthehorizon.Utilities;
import org.joml.Vector3f;

public class SonicWindParticleS2C
{
    public double absX,absY,absZ;
    public byte phase;

    public SonicWindParticleS2C(double absX, double absY, double absZ, byte phase) {
        this.absX = absX;
        this.absY = absY;
        this.absZ = absZ;
        this.phase = phase;
    }

    public SonicWindParticleS2C(FriendlyByteBuf buffer){
        this.absX = buffer.readDouble();
        this.absY = buffer.readDouble();
        this.absZ = buffer.readDouble();
        this.phase = buffer.readByte();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeDouble(absX);
        buffer.writeDouble(absY);
        buffer.writeDouble(absZ);
        buffer.writeByte(this.phase);
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(() -> {
            // This code is run on the client side
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                Minecraft mc = Minecraft.getInstance();
                ClientLevel world = mc.level;
                LocalPlayer player = mc.player;

                if (player != null && world != null)
                {
                    float p = 10.0F-phase*1.25F;
                    for(double offset = 0; offset <= 2*Math.PI; offset += Math.PI/2)
                    {
                        //Get Vertical Component
                        double particleY = (0.4)*p*Math.sin(phase/2.0F+offset+p/2);

                        //Get Horizontal Component
                        double horizontalPart = (0.4)*p*Math.cos(phase/2.0F+offset+p/2);
                        double particleX = Math.sin((-player.getYRot()+90) * (Math.PI / 180)) * 0.707 * horizontalPart;
                        double particleZ = Math.cos((-player.getYRot()+90) * (Math.PI / 180)) * 0.707 * horizontalPart;

                        Utilities.displayParticle(world,
                                new DustParticleOptions(new Vector3f(0.00f,0.0f,1f),1f),
                                absX+particleX,absY+particleY,absZ+particleZ,
                                0.35f-(0.035f)*p,0.25f-(0.035f)*p,0.25f-(0.035f)*p,
                                0.001, 2, false
                        );
                        Utilities.displayParticle(world,
                                new DustParticleOptions(new Vector3f(0.00f,1f,1f),1f),
                                absX+particleX,absY+particleY,absZ+particleZ,
                                0.25f-(0.025f)*p,0.25f-(0.025f)*p,0.25f-(0.025f)*p,
                                0.001, 3, false
                        );
                    }
                }
            });
        });
    }
}
