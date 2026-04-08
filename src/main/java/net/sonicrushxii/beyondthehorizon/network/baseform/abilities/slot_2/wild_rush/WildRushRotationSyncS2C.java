package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.wild_rush;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformClient;

public class WildRushRotationSyncS2C implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<WildRushRotationSyncS2C> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "wild_rush_rotation_sync_s2c"));

    public static final StreamCodec<FriendlyByteBuf, WildRushRotationSyncS2C> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), WildRushRotationSyncS2C::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    private float yaw;
    private float pitch;

    public WildRushRotationSyncS2C(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public WildRushRotationSyncS2C(FriendlyByteBuf buf) {
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
    }

    public static void handle(WildRushRotationSyncS2C msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            BaseformClient.ClientOnlyData.wildRushYawPitch[0] = msg.yaw;
            BaseformClient.ClientOnlyData.wildRushYawPitch[1] = msg.pitch;
        });
    }
}
