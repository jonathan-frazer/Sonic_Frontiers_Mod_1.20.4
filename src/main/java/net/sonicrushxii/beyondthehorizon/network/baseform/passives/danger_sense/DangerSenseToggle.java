package net.sonicrushxii.beyondthehorizon.network.baseform.passives.danger_sense;

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

public class DangerSenseToggle implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DangerSenseToggle> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "danger_sense_toggle"));

    public static final StreamCodec<FriendlyByteBuf, DangerSenseToggle> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), DangerSenseToggle::new);

    public DangerSenseToggle() {}

    public DangerSenseToggle(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static void handle(DangerSenseToggle msg, IPayloadContext ctx){
        ctx.enqueueWork(()->{
            ServerPlayer player = (ServerPlayer) ctx.player();
            if(player != null) {
                PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                //Modify Tags
                baseformProperties.dangerSenseActive = !baseformProperties.dangerSenseActive;
                PacketHandler.sendToALLPlayers(
                        new SyncPlayerFormS2C(
                                player.getId(),
                                playerSonicForm
                        ));
            }
        });
    }
}