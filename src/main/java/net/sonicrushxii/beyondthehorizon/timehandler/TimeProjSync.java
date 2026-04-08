package net.sonicrushxii.beyondthehorizon.timehandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.event_handler.client_handlers.ClientPacketHandler;

public class TimeProjSync implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<TimeProjSync> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "time_proj_sync"));

    public static final StreamCodec<FriendlyByteBuf, TimeProjSync> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), TimeProjSync::new);

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

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(TimeProjSync msg, IPayloadContext ctx){
        ctx.enqueueWork(() -> {
            ClientPacketHandler.clientProjSync(msg.entityId, msg.noGravity, msg.deltaMovement);
        });
    }
}
