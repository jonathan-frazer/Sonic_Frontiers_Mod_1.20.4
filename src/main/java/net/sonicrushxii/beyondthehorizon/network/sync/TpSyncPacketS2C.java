package net.sonicrushxii.beyondthehorizon.network.sync;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;

public class TpSyncPacketS2C {
    private final Vec3 position;
    private final float rotationY,rotationX;

    public TpSyncPacketS2C(Vec3 position) {
        this.position = position;
        this.rotationY = -999.0f;
        this.rotationX = -999.0f;
    }

    public TpSyncPacketS2C(Vec3 position, float rotationY, float rotationX) {
        this.position = position;
        this.rotationY = rotationY;
        this.rotationX = rotationX;
    }

    public TpSyncPacketS2C(FriendlyByteBuf buf) {
        this.position = buf.readVec3();
        this.rotationY = buf.readFloat();
        this.rotationX = buf.readFloat();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVec3(this.position);
        buf.writeFloat(this.rotationY);
        buf.writeFloat(this.rotationX);
    }

    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            // This code is run on the client side
            Minecraft mc = Minecraft.getInstance();
            ClientLevel world = mc.level;
            LocalPlayer player = mc.player;

            if(player != null && world != null) {
                player.setPos(position);
                if(rotationX > -990.0f && rotationY > -990.0f) {
                    player.setXRot(rotationX);
                    player.setYRot(rotationY);
                }
            }
        }));
        ctx.setPacketHandled(true);
    }
}
