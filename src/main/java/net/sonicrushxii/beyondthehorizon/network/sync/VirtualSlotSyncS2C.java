package net.sonicrushxii.beyondthehorizon.network.sync;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.client.VirtualSlotHandler;


public class VirtualSlotSyncS2C implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<VirtualSlotSyncS2C> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "virtual_slot_sync_s2c"));

    public static final StreamCodec<FriendlyByteBuf, VirtualSlotSyncS2C> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), VirtualSlotSyncS2C::new);

    private final byte slotLength;

    public VirtualSlotSyncS2C(byte slotLength) {
        this.slotLength = slotLength;
    }

    public VirtualSlotSyncS2C(FriendlyByteBuf buf) {
        this.slotLength = buf.readByte();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeByte(this.slotLength);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(VirtualSlotSyncS2C msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            VirtualSlotHandler.initialize();
        });
    }
}
