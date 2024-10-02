package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformHandler;

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

            if(ItemStack.isSameItemSameTags(event.getTo(),BaseformHandler.baseformSonicHead)) {
                BaseformHandler.performBaseformActivation(player);
            }

            if(ItemStack.isSameItemSameTags(event.getFrom(),BaseformHandler.baseformSonicHead)||
                    ItemStack.isSameItemSameTags(event.getFrom(),BaseformHandler.baseformLSSonicHead)||
                    ItemStack.isSameItemSameTags(event.getFrom(),BaseformHandler.baseformPBSonicHead)) {
                BaseformHandler.performBaseformDeactivation(player);
            }
        }
    }
}
