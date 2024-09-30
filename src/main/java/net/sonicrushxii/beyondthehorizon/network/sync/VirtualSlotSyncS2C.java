package net.sonicrushxii.beyondthehorizon.network.sync;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.sonicrushxii.beyondthehorizon.client.VirtualSlotHandler;


public class VirtualSlotSyncS2C {
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

    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> VirtualSlotHandler.initialize(this.slotLength));
        });
        ctx.setPacketHandled(true);
    }
}