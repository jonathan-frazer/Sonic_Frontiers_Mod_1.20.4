package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.event_handler.EquipmentChangeHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModItems;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.SpeedMultipliers;
import net.sonicrushxii.beyondthehorizon.network.sync.PlayerStopSoundPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Iterator;

public class LightspeedEffect {

    public LightspeedEffect() {}

    public LightspeedEffect(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    public static void performLightspeedEffect(ServerPlayer player)
    {
        //Equip Armor
        //SET ARMOR NBT DATA(COMMON)
        {
            CompoundTag nbtData = new CompoundTag();
            ListTag enchantmentList = new ListTag();
            CompoundTag enchantment = new CompoundTag();
            enchantment.putString("id", "minecraft:binding_curse");
            enchantment.putShort("lvl", (short) 1);
            enchantmentList.add(enchantment);
            nbtData.put("Enchantments", enchantmentList);
            nbtData.putInt("HideFlags", 127);
            nbtData.putByte("Unbreakable", (byte) 1);
            nbtData.putByte("BeyondTheHorizon", (byte) 1);

            Iterator<ItemStack> armorItems = player.getArmorSlots().iterator();
            try {
                if (armorItems.next().getTag().getByte("BeyondTheHorizon") == (byte) 1) {
                    ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_LIGHTSPEED_BOOTS.get());
                    itemToPlace.setTag(nbtData);
                    player.setItemSlot(EquipmentSlot.FEET, itemToPlace);
                }
            }
            catch(NullPointerException ignored){}

            try {
                if (armorItems.next().getTag().getByte("BeyondTheHorizon") == (byte) 1) {
                    ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_LIGHTSPEED_LEGGINGS.get());
                    itemToPlace.setTag(nbtData);
                    player.setItemSlot(EquipmentSlot.LEGS, itemToPlace);
                }
            }
            catch(NullPointerException ignored){}

            try {
                if (armorItems.next().getTag().getByte("BeyondTheHorizon") == (byte) 1) {
                    ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_LIGHTSPEED_CHESTPLATE.get());
                    itemToPlace.setTag(nbtData);
                    player.setItemSlot(EquipmentSlot.CHEST, itemToPlace);
                }
            }
            catch(NullPointerException ignored){}

            try{
                if(armorItems.next().getTag().getByte("BeyondTheHorizon") == (byte) 2){

                    ItemStack sonicHead = new ItemStack(Items.PLAYER_HEAD);
                    CompoundTag nbt = new CompoundTag();

                    // Custom NBT data
                    nbt.putByte("BeyondTheHorizon", (byte) 2);

                    // SkullOwner tag
                    CompoundTag skullOwner = new CompoundTag();
                    CompoundTag properties = new CompoundTag();
                    ListTag textures = new ListTag();
                    CompoundTag texture = new CompoundTag();
                    texture.putString("Value", "ewogICJ0aW1lc3RhbXAiIDogMTcyNjkyNzYxNjIxNSwKICAicHJvZmlsZUlkIiA6ICI2OTBmOTAwMTczZmQ0MDA5OGE2ZDc3Nzc2MWUwY2U4YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTb25pY1J1c2hYMTIiLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2QyNzZmMGExMTBkMGEzNzhiNzdlNzk3OTBiZDc0ZjNiOWEzMmNhNzgyYWQ2MTQ2NjhhYWE1ZmM4MDg5MWIwMCIKICAgIH0KICB9Cn0=");
                    textures.add(texture);
                    properties.put("textures", textures);
                    skullOwner.put("Properties", properties);
                    skullOwner.putIntArray("Id", new int[]{1762627585, 1945976841, -1972537481, 1642122891});
                    nbt.put("SkullOwner", skullOwner);

                    // Display tag
                    CompoundTag display = new CompoundTag();
                    ListTag lore = new ListTag();
                    lore.add(StringTag.valueOf("{\"text\":\"Light Speed Mode\",\"color\": \"aqua\"}"));
                    display.put("Lore", lore);
                    display.putString("Name", "{\"text\":\"Sonic Head\",\"color\": \"blue\",\"italic\": false}");
                    nbt.put("display", display);

                    sonicHead.setTag(nbt);

                    EquipmentChangeHandler.playerHeadEquipmentLock.put(player.getUUID(),true);
                    player.setItemSlot(EquipmentSlot.HEAD, sonicHead);
                }
            }
            catch(NullPointerException ignored){}
        }
        //Remove Slowdown
        player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        //Add Speed Boost
        if (!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(SpeedMultipliers.LIGHTSPEED_MODE))
            player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(SpeedMultipliers.LIGHTSPEED_MODE);

        //Sound
        PacketHandler.sendToALLPlayers(new PlayerStopSoundPacketS2C(ModSounds.LIGHT_SPEED_CHARGE.get().getLocation()));
        player.level().playSound(null,player.getX(),player.getY(),player.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.MASTER, 1.0f, 1.0f);

        //Add Tag
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();
            baseformProperties.lightSpeedState = (byte)2;

            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            baseformProperties
                    ));
        });
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                        performLightspeedEffect(player);

                });
        ctx.setPacketHandled(true);
    }
}

