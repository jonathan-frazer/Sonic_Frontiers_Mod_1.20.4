package net.sonicrushxii.beyondthehorizon.network.baseform.passives;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step.StepUp;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class StopSprint {

    public StopSprint() {}

    public StopSprint(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                    {
                        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm->{
                            BaseformProperties baseformProperties =  (BaseformProperties) playerSonicForm.getFormProperties();
                            baseformProperties.sprintFlag = false;

                            PacketHandler.sendToPlayer(player,
                                    new SyncPlayerFormS2C(
                                            playerSonicForm.getCurrentForm(),
                                            baseformProperties
                                    ));
                        });

                        //Activate Auto Step
                        StepUp.performStepUpDeactivate(player);
                    }
                });
        ctx.setPacketHandled(true);
    }
}
