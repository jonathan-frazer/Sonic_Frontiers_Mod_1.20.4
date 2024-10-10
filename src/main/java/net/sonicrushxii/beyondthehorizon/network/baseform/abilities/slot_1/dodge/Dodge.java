package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.dodge;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class Dodge {

    private final boolean dodgingRight;

    public Dodge(boolean dodgingRight) {
        this.dodgingRight = dodgingRight;
    }

    public Dodge(FriendlyByteBuf buffer){
        this.dodgingRight = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeBoolean(this.dodgingRight);
    }

    public static void performRightDodge(ServerPlayer player)
    {
        player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
        Vec3 directionVector = player.getLookAngle().cross(new Vec3(0,1,0));
        player.addDeltaMovement(directionVector.scale(1.0));
        player.connection.send(new ClientboundSetEntityMotionPacket(player));
    }

    public static void performLeftDodge(ServerPlayer player)
    {
        player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
        Vec3 directionVector = player.getLookAngle().cross(new Vec3(0,-1,0));
        player.addDeltaMovement(directionVector.scale(1.0));
        player.connection.send(new ClientboundSetEntityMotionPacket(player));
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                    {
                        if(dodgingRight) performRightDodge(player);
                        else performLeftDodge(player);
                    }
                });
        ctx.setPacketHandled(true);
    }
}


