package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.SonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformTransformer;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
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

            {
                PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);
                CustomData customData = headItem.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
                CompoundTag tag = customData.copyTag();
                try {
                    if (playerSonicForm.getCurrentForm() == SonicForm.PLAYER &&
                            headItem.getItem() == Items.PLAYER_HEAD &&
                            tag.getByte("BeyondTheHorizon") == (byte) 2) {
                        BaseformTransformer.performActivation(player);
                    }
                }catch(NullPointerException ignored){}

                try {
                    if (playerSonicForm.getCurrentForm() == SonicForm.BASEFORM &&
                            (tag.getByte("BeyondTheHorizon") != (byte) 2)) {
                        BaseformTransformer.performActivation(player);
                    }
                }catch(NullPointerException ignored){
                    BaseformTransformer.performDeactivation(player);
                }

                PacketHandler.sendToALLPlayers(
                        new SyncPlayerFormS2C(
                                player.getId(),
                                playerSonicForm
                        ));
            }

        }
    }
}
