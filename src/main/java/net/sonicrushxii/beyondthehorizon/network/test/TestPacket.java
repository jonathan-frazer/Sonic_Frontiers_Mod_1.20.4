package net.sonicrushxii.beyondthehorizon.network.test;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class TestPacket {
    public TestPacket() {}

    public TestPacket(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }


    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null) {
                        System.out.println("Year Year Days");
                    }
                });
        ctx.setPacketHandled(true);
    }
}

