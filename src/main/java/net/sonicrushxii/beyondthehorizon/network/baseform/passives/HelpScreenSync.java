package net.sonicrushxii.beyondthehorizon.network.baseform.passives;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class HelpScreenSync implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<HelpScreenSync> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "help_screen_sync"));

    public static final StreamCodec<FriendlyByteBuf, HelpScreenSync> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), HelpScreenSync::new);

    private final int helpScreenNum;

    public HelpScreenSync(int helpScreenNum) {
        this.helpScreenNum = helpScreenNum;
    }

    public HelpScreenSync(FriendlyByteBuf buffer) {
        this.helpScreenNum = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.helpScreenNum);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(HelpScreenSync msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null)
                    {
                        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();
                        baseformProperties.helpScreenPageNo = msg.helpScreenNum;

                        PacketHandler.sendToALLPlayers(
                                new SyncPlayerFormS2C(
                                        player.getId(),
                                        playerSonicForm
                                ));
                    }
                });
    }
}
