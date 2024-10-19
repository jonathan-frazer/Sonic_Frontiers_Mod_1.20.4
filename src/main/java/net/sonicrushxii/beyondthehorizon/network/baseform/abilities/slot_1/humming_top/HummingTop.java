package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.humming_top;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;


public class HummingTop {

    private final boolean activate;

    public HummingTop(boolean activate) {
        this.activate = activate;
    }

    public HummingTop(FriendlyByteBuf buffer) {
        this.activate = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.activate);
    }

    public static void performHummingTop(ServerPlayer player, boolean activate)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            if(activate)
            {
                //Gravity
                player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.0);
                //Modify Data
                baseformProperties.hummingTop = 1;

            }
            else
            {
                //Gravity
                player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                //Modify Data
                baseformProperties.hummingTop = 0;
            }


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
                        performHummingTop(player,this.activate);
                    }
                });
        ctx.setPacketHandled(true);
    }
}
