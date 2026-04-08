package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.boost;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class Sidestep implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<Sidestep> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "sidestep"));

    public static final StreamCodec<FriendlyByteBuf, Sidestep> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), Sidestep::new);

    private final boolean steppingRight;

    public Sidestep(boolean steppingRight) {
        this.steppingRight = steppingRight;
    }

    public Sidestep(FriendlyByteBuf buffer){
        this.steppingRight = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeBoolean(this.steppingRight);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void performSideStep(ServerPlayer player, boolean steppingRight){
        player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
        Vec3 directionVector = player.getLookAngle().cross(new Vec3(0,(steppingRight)?1:-1,0));
        player.addDeltaMovement(directionVector.scale(3.0));
        player.connection.send(new ClientboundSetEntityMotionPacket(player));
    }


    public static void handle(Sidestep msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null){
                        performSideStep(player,msg.steppingRight);
                    }
                });
    }
}


