package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;

@Mod.EventBusSubscriber(modid = BeyondTheHorizon.MOD_ID)
public class ModEventHandler {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof Player){
            //Add Other Capabilities from here
            if(!event.getObject().getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).isPresent()){
                event.addCapability(new ResourceLocation(BeyondTheHorizon.MOD_ID, "properties"), new PlayerSonicFormProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event){
        if(event.isWasDeath()){
            //Add Other Capabilities from here
            event.getOriginal().getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(oldStore->{
                event.getOriginal().getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(newStore->{
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }
}
