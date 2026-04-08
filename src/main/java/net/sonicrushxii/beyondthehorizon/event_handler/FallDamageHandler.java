package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.SonicForm;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;

public class FallDamageHandler {

    @SubscribeEvent
    public void onLivingFall(LivingFallEvent event) {
        if(event.getEntity() instanceof ServerPlayer player)
        {
            PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
            if(playerSonicForm.getCurrentForm() == SonicForm.BASEFORM)
                event.setDistance(0.0f);
        }
    }
}
