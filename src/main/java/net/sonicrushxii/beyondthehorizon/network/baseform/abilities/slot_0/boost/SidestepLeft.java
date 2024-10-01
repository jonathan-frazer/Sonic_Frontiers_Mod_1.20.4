package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.boost;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.function.Supplier;

public class SidestepLeft {

    public SidestepLeft() {    }

    public SidestepLeft(FriendlyByteBuf buffer){    }

    public void encode(FriendlyByteBuf buffer){    }

    public static void performLeftSideStep(ServerPlayer player){
        player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
        Vec3 directionVector = player.getLookAngle().cross(new Vec3(0,-1,0));
        player.addDeltaMovement(directionVector.scale(3.0));
        player.connection.send(new ClientboundSetEntityMotionPacket(player));
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null){
                        performLeftSideStep(player);
                    }
                });
        ctx.setPacketHandled(true);
    }
}


