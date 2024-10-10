package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;

public class AttackHandler {
    @SubscribeEvent
    public void onPlayerAttack(LivingAttackEvent event)
    {
        //Trigger Selective Invulnerability
        try {
            event.getEntity().getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
                //Get Data From the Player
                BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();
                //Makes you only invulnerable to Direct mob attacks when using this ability. Like weakness but better
                if (baseformProperties.selectiveInvul && !(event.getSource().getEntity() instanceof Player) && !event.getSource().isIndirect())
                    event.setCanceled(true);
            });
        }catch(NullPointerException | ClassCastException ignored) {}
    }
}
