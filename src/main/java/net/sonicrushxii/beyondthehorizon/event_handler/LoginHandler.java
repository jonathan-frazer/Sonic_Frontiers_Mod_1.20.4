package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.SonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformTransformer;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash.RevertFromSpindash;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class LoginHandler {
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        Player player = event.getEntity();
        if(player == null) return;
        if(!player.level().isClientSide()) onServerLogin((ServerPlayer) player);
    }

    private void onServerLogin(ServerPlayer player)
    {
        System.out.println("Logged In");
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
            if(playerSonicForm.getCurrentForm() == SonicForm.BASEFORM)
            {
                BaseformTransformer.performActivation(player);
                BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                //Revert Spindash
                if(baseformProperties.ballFormState != 0)
                    RevertFromSpindash.performRevertSpindash(player);

                //Revert Dodge
                if(baseformProperties.dodgeInvul) {
                    baseformProperties.dodgeInvul = false;
                    player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                }

                //Reset Combo Meter
                baseformProperties.hitCount = 0;

                PacketHandler.sendToPlayer(player,
                        new SyncPlayerFormS2C(
                                playerSonicForm.getCurrentForm(),
                                baseformProperties
                        ));
            }

            PacketHandler.sendToPlayer(player, new SyncPlayerFormS2C(
                    playerSonicForm.getCurrentForm(),
                    playerSonicForm.getFormProperties()
            ));
        });
    }
}
