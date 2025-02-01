package net.sonicrushxii.beyondthehorizon.network.sync;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.sonicrushxii.beyondthehorizon.client.VirtualSlotHandler;

public class GoToVirtualSlotS2C {

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

    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // This code is run on the client side
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                VirtualSlotHandler.goToSlot(targetSlot);
            });
        });

        ctx.setPacketHandled(true);
    }
}
