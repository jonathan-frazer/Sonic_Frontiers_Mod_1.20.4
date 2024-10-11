package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformHandler;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Objects;

public class AttackHandler {
    @SubscribeEvent
    public void onPlayerDamaged(LivingAttackEvent event)
    {
        /** Player Attacked*/
        if(event.getEntity() instanceof ServerPlayer player)
        {
            player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
                switch(playerSonicForm.getCurrentForm())
                {
                    case BASEFORM -> BaseformHandler.takeDamage(event,(BaseformProperties)playerSonicForm.getFormProperties());
                    /*case SUPERFORM ->
                    case STARFALLFORM ->
                    case HYPERFORM ->*/
                }

                //Sync Player Properties
                PacketHandler.sendToPlayer(player,
                        new SyncPlayerFormS2C(
                                playerSonicForm.getCurrentForm(),
                                playerSonicForm.getFormProperties()
                        ));
            });
        }

        /** Player: Attacker*/
        try{
            if(event.getSource().getEntity() instanceof ServerPlayer player)
            {
                player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
                    switch(playerSonicForm.getCurrentForm())
                    {
                        case BASEFORM -> BaseformHandler.dealDamage(event,(BaseformProperties)playerSonicForm.getFormProperties());
                        /*case SUPERFORM ->
                        case STARFALLFORM ->
                        case HYPERFORM ->*/
                    }

                    //Sync Player Properties
                    PacketHandler.sendToPlayer(player,
                            new SyncPlayerFormS2C(
                                    playerSonicForm.getCurrentForm(),
                                    playerSonicForm.getFormProperties()
                            ));
                });
            }
        }catch(NullPointerException ignored){}
    }
}
