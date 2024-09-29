package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
        ItemStack baseformSonicHead = new ItemStack(Items.PLAYER_HEAD);
        {
            CompoundTag nbt = new CompoundTag();

            // Custom NBT data
            nbt.putByte("BeyondTheHorizon", (byte) 2);

            // SkullOwner tag
            CompoundTag skullOwner = new CompoundTag();
            CompoundTag properties = new CompoundTag();
            ListTag textures = new ListTag();
            CompoundTag texture = new CompoundTag();
            texture.putString("Value", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTBjN2NlZWNjODliNTY0MjNhOWU4YWFiMTE3NjRkZTI5MDIyNjU4MzA5YTUyNjY2M2JmMzQyNGY0N2NhZDlmOCJ9fX0=");
            textures.add(texture);
            properties.put("textures", textures);
            skullOwner.put("Properties", properties);
            skullOwner.putIntArray("Id", new int[]{512370214, -95272899, -2003262887, 1067375885});
            nbt.put("SkullOwner", skullOwner);

            // Display tag
            CompoundTag display = new CompoundTag();
            ListTag lore = new ListTag();
            lore.add(StringTag.valueOf("{\"text\":\"Adapted from Sonic Frontiers\",\"color\": \"light_purple\"}"));
            display.put("Lore", lore);
            display.putString("Name", "{\"text\":\"Sonic Head\",\"color\": \"blue\",\"italic\": false}");
            nbt.put("display", display);

            baseformSonicHead.setTag(nbt);
        }
        if(event.getSlot() == EquipmentSlot.HEAD)
        {
            //If there is a Lock, the lock will be broken. Sometimes we want to change the head without triggerring any transformation
            if(playerHeadEquipmentLock.getOrDefault(player.getUUID(),false)){
                playerHeadEquipmentLock.put(player.getUUID(),false);
                return;
            }

            if(ItemStack.isSameItemSameTags(event.getTo(),baseformSonicHead)) {
                BaseformHandler.performBaseformActivation(player);
            }

            if(ItemStack.isSameItemSameTags(event.getFrom(),baseformSonicHead)) {
                BaseformHandler.performBaseformDeactivation(player);
            }
        }
    }
}
