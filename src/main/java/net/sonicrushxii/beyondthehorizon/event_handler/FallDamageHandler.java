package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.SonicForm;

public class FallDamageHandler {

    @SubscribeEvent
    public void onLivingFall(LivingFallEvent event) {
        if(event.getEntity() instanceof ServerPlayer player)
        {
            player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm->{
                if(playerSonicForm.getCurrentForm() == SonicForm.BASEFORM)
                    event.setDistance(0.0f);
            });
        }
    }
}
