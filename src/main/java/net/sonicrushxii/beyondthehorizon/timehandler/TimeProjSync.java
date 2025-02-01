package net.sonicrushxii.beyondthehorizon.timehandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.sonicrushxii.beyondthehorizon.event_handler.client_handlers.ClientPacketHandler;

public class TimeProjSync {
    private final int entityId;
    private final boolean noGravity;
    private final Vec3 deltaMovement;

    public TimeProjSync(int entityId,Vec3 deltaMovement,boolean noGravity) {
        this.entityId = entityId;
        this.noGravity = noGravity;
        this.deltaMovement = deltaMovement;
    }

    public TimeProjSync(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.noGravity = buffer.readBoolean();
        this.deltaMovement = buffer.readVec3();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeInt(this.entityId);
        buffer.writeBoolean(this.noGravity);
        buffer.writeVec3(this.deltaMovement);
    }

    public void handle(CustomPayloadEvent.Context ctx){
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientPacketHandler.clientProjSync(entityId, noGravity, deltaMovement);
        });
        ctx.setPacketHandled(true);
    }
}
