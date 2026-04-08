package net.sonicrushxii.beyondthehorizon.network.sync;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.client.VirtualSlotHandler;

public class GoToVirtualSlotS2C implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<GoToVirtualSlotS2C> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "go_to_virtual_slot_s2c"));

    public static final StreamCodec<FriendlyByteBuf, GoToVirtualSlotS2C> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), GoToVirtualSlotS2C::new);

    byte targetSlot;

    public GoToVirtualSlotS2C(byte targetSlot) {
        this.targetSlot = targetSlot;
    }

    public GoToVirtualSlotS2C(FriendlyByteBuf buf) {
        this.targetSlot = buf.readByte();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeByte(this.targetSlot);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(GoToVirtualSlotS2C msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            // This code is run on the client side
            VirtualSlotHandler.goToSlot(msg.targetSlot);
        });
    }
}
