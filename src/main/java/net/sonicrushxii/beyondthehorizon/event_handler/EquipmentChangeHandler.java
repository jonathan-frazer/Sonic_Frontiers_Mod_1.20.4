package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.SonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformTransform;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.HashMap;
import java.util.UUID;

public class EquipmentChangeHandler {

    public static HashMap<UUID,Boolean> playerHeadEquipmentLock = new HashMap<>();

    @SubscribeEvent
    public void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        //Player
        if(event.getEntity() instanceof Player player)
        {
            if(player.level().isClientSide()) return;
            onServerPlayerEquipmentChange((ServerPlayer)player, event);
        }
    }

    private void onServerPlayerEquipmentChange(ServerPlayer player, LivingEquipmentChangeEvent event)
    {
        if(event.getSlot() == EquipmentSlot.HEAD)
        {
            //If there is a Lock, the lock prevent further execution and then will be broken.
            //Sometimes we want to change the head without triggerring any transformation
            if(playerHeadEquipmentLock.getOrDefault(player.getUUID(),false)){
                playerHeadEquipmentLock.put(player.getUUID(),false);
                return;
            }

            player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm->{
                ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);
                try {
                    if (playerSonicForm.getCurrentForm() == SonicForm.PLAYER &&
                            headItem.getItem() == Items.PLAYER_HEAD &&
                            headItem.getTag().getByte("BeyondTheHorizon") == (byte) 2) {
                        BaseformTransform.performActivation(player);
                    }
                }catch(NullPointerException ignored){}

                try {
                    if (playerSonicForm.getCurrentForm() == SonicForm.BASEFORM &&
                            (headItem.getTag().getByte("BeyondTheHorizon") != (byte) 2)) {
                        BaseformTransform.performActivation(player);
                    }
                }catch(NullPointerException ignored){
                    BaseformTransform.performDeactivation(player);
                }

                PacketHandler.sendToPlayer(player,
                        new SyncPlayerFormS2C(
                                playerSonicForm.getCurrentForm(),
                                playerSonicForm.getFormProperties()
                        ));
            });

        }
    }
}
