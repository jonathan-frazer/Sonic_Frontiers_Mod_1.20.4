package net.sonicrushxii.beyondthehorizon.network.baseform.passives;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step.AutoStep;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class StopSprint {

    public StopSprint() {}

    public StopSprint(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    public static void performStopSprint(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm->{
            BaseformProperties baseformProperties =  (BaseformProperties) playerSonicForm.getFormProperties();
            baseformProperties.sprintFlag = false;

            //Undo Boost
            player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.50);

            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            baseformProperties
                    ));
        });

        //Activate Auto Step
        AutoStep.performStepUpDeactivate(player);
    }

    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                    {
                        performStopSprint(player);
                    }
                });
        ctx.setPacketHandled(true);
    }
}
