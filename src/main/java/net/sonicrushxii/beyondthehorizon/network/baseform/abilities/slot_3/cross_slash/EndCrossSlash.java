package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.cross_slash;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformActiveAbility;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class EndCrossSlash
{
    public EndCrossSlash() {}

    public EndCrossSlash(FriendlyByteBuf buffer){}

    public void encode(FriendlyByteBuf buffer){}

    public static void performEndCrossSlash(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Reset Counter to 0
            baseformProperties.crossSlash = 0;
            //Return Gravity
            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
            //Cooldown
            baseformProperties.setCooldown(BaseformActiveAbility.CROSS_SLASH, (byte) 5);

            PacketHandler.sendToALLPlayers(
                    new SyncPlayerFormS2C(
                            player.getId(),
                            playerSonicForm
                    ));
        });
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null){
                        performEndCrossSlash(player);
                    }
                });
        ctx.setPacketHandled(true);
    }
}
