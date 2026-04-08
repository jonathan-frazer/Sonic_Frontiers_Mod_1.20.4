package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;

@EventBusSubscriber(modid = BeyondTheHorizon.MOD_ID)
public class ModEventHandler {
    // In NeoForge 1.21.1, capabilities are replaced by data attachments.
    // The onAttachCapabilitiesPlayer method is no longer needed as ModAttachments handles registration.

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event){
        if(event.isWasDeath()){
            PlayerSonicForm oldStore = event.getOriginal().getData(ModAttachments.PLAYER_SONIC_FORM);
            PlayerSonicForm newStore = event.getEntity().getData(ModAttachments.PLAYER_SONIC_FORM);
            newStore.copyFrom(oldStore);
        }
    }
}
