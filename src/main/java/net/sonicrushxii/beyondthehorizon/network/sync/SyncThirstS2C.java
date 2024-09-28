package net.sonicrushxii.beyondthehorizon.network.sync;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerThirstProvider;
import net.sonicrushxii.beyondthehorizon.client.ClientThirstData;

public class SyncThirstS2C {
    private final int thirst;
    public SyncThirstS2C(int thirst) {
        this.thirst = thirst;
    }

    public SyncThirstS2C(FriendlyByteBuf buffer) {
        this.thirst = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeInt(this.thirst);
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(()->{
            //On Client Side
            ClientThirstData.setPlayerThirst(thirst);
        });
        ctx.setPacketHandled(true);
    }
}

