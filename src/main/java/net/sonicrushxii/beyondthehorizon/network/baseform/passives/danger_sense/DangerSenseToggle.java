package net.sonicrushxii.beyondthehorizon.network.baseform.passives.danger_sense;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class DangerSenseToggle {
    public DangerSenseToggle() {}

    public DangerSenseToggle(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }


    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(()->{
            ServerPlayer player = ctx.getSender();
            if(player != null) {
                player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm->{
                    BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                    //Modify Tags
                    baseformProperties.dangerSenseActive = !baseformProperties.dangerSenseActive;
                    PacketHandler.sendToALLPlayers(
                            new SyncPlayerFormS2C(
                                    player.getId(),
                                    playerSonicForm
                            ));
                });
            }
        });

        ctx.setPacketHandled(true);
    }
}