package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.sonicrushxii.beyondthehorizon.KeyBindings;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.client.ClientFormData;
import net.sonicrushxii.beyondthehorizon.modded.ModItems;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BaseformHandler
{
    public static void performBaseformActivation(ServerPlayer player)
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
            if (armorItems.next().isEmpty()) {
                ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_BOOTS.get());
                itemToPlace.setTag(nbtData);
                player.setItemSlot(EquipmentSlot.FEET, itemToPlace);
            }
            if (armorItems.next().isEmpty()) {
                ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_LEGGINGS.get());
                itemToPlace.setTag(nbtData);
                player.setItemSlot(EquipmentSlot.LEGS, itemToPlace);
            }
            if (armorItems.next().isEmpty()) {
                ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_CHESTPLATE.get());
                itemToPlace.setTag(nbtData);
                player.setItemSlot(EquipmentSlot.CHEST, itemToPlace);
            }
        }
        //Add Tag
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm->{
            playerSonicForm.activateBaseForm();
            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            playerSonicForm.getFormProperties()
                    ));
        });

        //Effects
        {
            player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, -1, 2, false, false));
        }

        //Commands
        {
            CommandSourceStack commandSourceStack = player.createCommandSourceStack().withPermission(4).withSuppressedOutput();
            MinecraftServer server = player.serverLevel().getServer();
            server.
                    getCommands().
                    performPrefixedCommand(commandSourceStack,"function beyondthehorizon:baseform/activate");
        }
    }

    public static void performBaseformClientTick(LocalPlayer player)
    {if(ClientFormData.getPlayerFormDetails() instanceof BaseformProperties baseformProperties)
    {
        //Test KeyPress
        if(KeyBindings.INSTANCE.useAbility1.consumeClick())
        {
            System.out.println(baseformProperties.hasDoubleJump);
        }
    }}

    public static void performBaseformClientSecond(LocalPlayer player) {}


    public static void performBaseformServerTick(ServerPlayer player)
    {

    }

    public static void performBaseformServerSecond(ServerPlayer player) {}

    public static void performBaseformDeactivation(ServerPlayer player)
    {
        //Remove Armor
        {
            //SET ARMOR NBT DATA(COMMON)
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

            //Get Armor Items
            Iterator<ItemStack> armorItems = player.getArmorSlots().iterator();

            //Delete Boots
            ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_BOOTS.get());
            itemToPlace.setTag(nbtData);
            if(ItemStack.isSameItemSameTags(armorItems.next(),itemToPlace))
                player.setItemSlot(EquipmentSlot.FEET,ItemStack.EMPTY);

            //Delete Leggings
            itemToPlace = new ItemStack(ModItems.BASEFORM_LEGGINGS.get());
            itemToPlace.setTag(nbtData);
            if(ItemStack.isSameItemSameTags(armorItems.next(),itemToPlace))
                player.setItemSlot(EquipmentSlot.LEGS,ItemStack.EMPTY);

            //Delete Chestplate
            itemToPlace = new ItemStack(ModItems.BASEFORM_CHESTPLATE.get());
            itemToPlace.setTag(nbtData);
            if(ItemStack.isSameItemSameTags(armorItems.next(),itemToPlace))
                player.setItemSlot(EquipmentSlot.CHEST,ItemStack.EMPTY);

        }

        //Remove Tags
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm->{
            playerSonicForm.deactivateBaseForm();
            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            playerSonicForm.getFormProperties()
                    ));
        });

        //Effects
        player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.10000000149011612);
        player.removeEffect(MobEffects.JUMP);

        //Commands
        CommandSourceStack commandSourceStack = player.createCommandSourceStack().withPermission(4).withSuppressedOutput();
        MinecraftServer server = player.serverLevel().getServer();

        server.
                getCommands().
                performPrefixedCommand(commandSourceStack,"function beyondthehorizon:baseform/deactivate");
    }
}
