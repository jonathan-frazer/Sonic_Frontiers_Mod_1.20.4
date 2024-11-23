package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_4;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.sonicrushxii.beyondthehorizon.KeyBindings;
import net.sonicrushxii.beyondthehorizon.client.VirtualSlotHandler;

public class GoToParrySlotS2C {

    public GoToParrySlotS2C() {}

    public GoToParrySlotS2C(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // This code is run on the client side
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                Minecraft mc = Minecraft.getInstance();
                ClientLevel world = mc.level;
                LocalPlayer player = mc.player;

                if (player != null && world != null) {
                    VirtualSlotHandler.setSlot((byte)(5));
                    //Consume All clicks before switching over to another slot
                    while(KeyBindings.INSTANCE.useAbility1.consumeClick());
                    while(KeyBindings.INSTANCE.useAbility2.consumeClick());
                    while(KeyBindings.INSTANCE.useAbility3.consumeClick());
                    while(KeyBindings.INSTANCE.useAbility4.consumeClick());
                    while(KeyBindings.INSTANCE.useAbility5.consumeClick());
                    while(KeyBindings.INSTANCE.useAbility6.consumeClick());
                }
            });
        });

        ctx.setPacketHandled(true);
    }
}
