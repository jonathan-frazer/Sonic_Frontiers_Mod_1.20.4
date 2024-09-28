package net.sonicrushxii.beyondthehorizon.network.test;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerThirstProvider;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncThirstS2C;

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
                        if(player.onGround())
                            player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst->{
                                thirst.addThirst(1);
                                PacketHandler.sendToPlayer(player,new SyncThirstS2C(thirst.getThirst()));
                            });
                        else
                            player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst->{
                                thirst.subThirst(1);
                                PacketHandler.sendToPlayer(player,new SyncThirstS2C(thirst.getThirst()));
                            });
                    }
                });
        ctx.setPacketHandled(true);
    }
}

