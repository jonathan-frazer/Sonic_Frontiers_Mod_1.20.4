package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.speed_blitz;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;


public class SpeedBlitz {

    public SpeedBlitz() {}

    public SpeedBlitz(FriendlyByteBuf buffer) {}

    public void encode(FriendlyByteBuf buffer) {}

    public static void performToggleSpeedBlitz(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Add Data
            baseformProperties.speedBlitz = !baseformProperties.speedBlitz;

            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            baseformProperties
                    ));
        });
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                    {
                        performToggleSpeedBlitz(player);
                    }
                });
        ctx.setPacketHandled(true);
    }
}
