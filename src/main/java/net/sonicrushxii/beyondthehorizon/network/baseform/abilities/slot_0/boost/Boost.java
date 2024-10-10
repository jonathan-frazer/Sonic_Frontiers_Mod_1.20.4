package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.boost;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StartSprint;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;


public class Boost {

    public Boost() {}

    public Boost(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    public static void performBoost(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();
            baseformProperties.boostLvl = (byte)((baseformProperties.boostLvl+1)%4);

            if(player.isSprinting())
                switch(baseformProperties.boostLvl)
                {
                    case 0 : player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
                             break;
                    case 1 : player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.75);
                             break;
                    case 2 : player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(1.00);
                             break;
                    case 3 : StartSprint.sonicBoomEffect(player);
                             player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(1.25);
                             break;
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
                        performBoost(player);
                });
        ctx.setPacketHandled(true);
    }
}