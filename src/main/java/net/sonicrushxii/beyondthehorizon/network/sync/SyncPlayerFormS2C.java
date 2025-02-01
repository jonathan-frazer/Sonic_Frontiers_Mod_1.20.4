package net.sonicrushxii.beyondthehorizon.network.sync;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.event_handler.client_handlers.ClientPacketHandler;

public class SyncPlayerFormS2C {
    private final int playerId;
    private final PlayerSonicForm playerSonicForm;

    public SyncPlayerFormS2C(int playerId, PlayerSonicForm playerSonicForm) {
        this.playerId = playerId;
        this.playerSonicForm = playerSonicForm;
    }

    public SyncPlayerFormS2C(FriendlyByteBuf buffer) {
        this.playerId = buffer.readInt();
        this.playerSonicForm = new PlayerSonicForm();
        CompoundTag nbtData = buffer.readNbt();
        if(nbtData != null)
            playerSonicForm.loadNBTData(nbtData);
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeInt(this.playerId);
        CompoundTag nbtData = new CompoundTag();
        this.playerSonicForm.saveNBTData(nbtData);
        buffer.writeNbt(nbtData);
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(()->{
            //On Client Side
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ClientPacketHandler.playerFormSync(playerId,playerSonicForm);
            });
        });
        ctx.setPacketHandled(true);
    }
}

