package net.sonicrushxii.beyondthehorizon.network.sync;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.event_handler.client_handlers.ClientPacketHandler;

public class SyncPlayerFormS2C implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncPlayerFormS2C> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "sync_player_form_s2c"));

    public static final StreamCodec<FriendlyByteBuf, SyncPlayerFormS2C> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), SyncPlayerFormS2C::new);

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

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncPlayerFormS2C msg, IPayloadContext ctx){
        ctx.enqueueWork(()->{
            //On Client Side
            ClientPacketHandler.playerFormSync(msg.playerId, msg.playerSonicForm);
        });
    }
}
