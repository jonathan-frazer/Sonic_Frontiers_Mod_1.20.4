package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonicrushxii.beyondthehorizon.KeyBindings;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.client.ClientFormData;
import net.sonicrushxii.beyondthehorizon.modded.ModItems;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StartSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StopSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step.StepDown;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step.StepDownDouble;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.danger_sense.DangerSenseEmit;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.doublejump.DoubleJump;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.doublejump.DoubleJumpEnd;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.VirtualSlotSyncS2C;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BaseformHandler
{
    public static CompoundTag baseformArmorNBTTag; static {
    baseformArmorNBTTag = new CompoundTag();
    ListTag enchantmentList = new ListTag();
    CompoundTag enchantment = new CompoundTag();
    enchantment.putString("id", "minecraft:binding_curse");
    enchantment.putShort("lvl", (short) 1);
    enchantmentList.add(enchantment);
    baseformArmorNBTTag.put("Enchantments", enchantmentList);
    baseformArmorNBTTag.putInt("HideFlags", 127);
    baseformArmorNBTTag.putByte("Unbreakable", (byte) 1);
    baseformArmorNBTTag.putByte("BeyondTheHorizon", (byte) 1);
}
    public static ItemStack baseformSonicHead; static {
    baseformSonicHead = new ItemStack(Items.PLAYER_HEAD);
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

    public static void performBaseformActivation(ServerPlayer player)
    {
        //Equip Armor
        //SET ARMOR NBT DATA(COMMON)
        {
            Iterator<ItemStack> armorItems = player.getArmorSlots().iterator();
            if (armorItems.next().isEmpty()) {
                ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_BOOTS.get());
                itemToPlace.setTag(baseformArmorNBTTag);
                player.setItemSlot(EquipmentSlot.FEET, itemToPlace);
            }
            if (armorItems.next().isEmpty()) {
                ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_LEGGINGS.get());
                itemToPlace.setTag(baseformArmorNBTTag);
                player.setItemSlot(EquipmentSlot.LEGS, itemToPlace);
            }
            if (armorItems.next().isEmpty()) {
                ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_CHESTPLATE.get());
                itemToPlace.setTag(baseformArmorNBTTag);
                player.setItemSlot(EquipmentSlot.CHEST, itemToPlace);
            }
        }
        //Add Data
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm->{
            playerSonicForm.activateBaseForm();
            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            playerSonicForm.getFormProperties()
                    ));
        });

        //Initialize Virtual Slot Handler
        PacketHandler.sendToPlayer(player,new VirtualSlotSyncS2C((byte)7));

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

    public static void performBaseformClientTick(LocalPlayer player, CompoundTag playerNBT)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = player.level();

        Vec3 playerDirCentre = Utilities.calculateViewVector(0.0f, player.getViewYRot(0)).scale(0.75);
        BlockPos centrePos = player.blockPosition().offset(
                (int) Math.round(playerDirCentre.x),
                (Math.round(player.getY()) > player.getY()) ? 1 : 0,
                (int) Math.round(playerDirCentre.z)
        );

        BaseformProperties baseformProperties = (BaseformProperties) ClientFormData.getPlayerFormDetails();

        //Passive Abilities
        //General Sprinting
        {
            //It handles Auto Step,
            if(player.isSprinting() && baseformProperties.sprintFlag == false)
                PacketHandler.sendToServer(new StartSprint());
            if(!player.isSprinting() && baseformProperties.sprintFlag == true)
                PacketHandler.sendToServer(new StopSprint());

        }
        //Double Jump
        {
            if (KeyBindings.INSTANCE.doubleJump.consumeClick()
                    && !player.onGround() && !player.isSpectator()
                    && baseformProperties.hasDoubleJump
                    && playerNBT.getCompound("abilities").getByte("flying") == 0) {
                PacketHandler.sendToServer(new DoubleJump());
            }

            if (!baseformProperties.hasDoubleJump && player.onGround()) {
                PacketHandler.sendToServer(new DoubleJumpEnd());
            }
        }
        //Auto Step
        {
            if(player.isSprinting())
            {
                List<String> blocksinFront = new ArrayList<>();
                blocksinFront.add(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos.offset(0, -3, 0)).getBlock()) + "");
                blocksinFront.add(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos.offset(0, -2, 0)).getBlock()) + "");
                blocksinFront.add(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos.offset(0, -1, 0)).getBlock()) + "");
                blocksinFront.add(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos).getBlock()) + "");

                if (Utilities.passableBlocks.contains(blocksinFront.get(3))
                        && Utilities.passableBlocks.contains(blocksinFront.get(2))
                        && !Utilities.passableBlocks.contains(blocksinFront.get(1))
                        && player.onGround())
                    PacketHandler.sendToServer(new StepDown());

                if (Utilities.passableBlocks.contains(blocksinFront.get(3))
                        && Utilities.passableBlocks.contains(blocksinFront.get(2))
                        && Utilities.passableBlocks.contains(blocksinFront.get(1))
                        && !Utilities.passableBlocks.contains(blocksinFront.get(0))
                        && player.onGround())
                    PacketHandler.sendToServer(new StepDownDouble());
            }
        }
        //Danger Sense


    }

    public static void performBaseformClientSecond(LocalPlayer player, CompoundTag playerNBT) {}


    public static void performBaseformServerTick(ServerPlayer player, CompoundTag playerNBT)
    {

    }

    public static void performBaseformServerSecond(ServerPlayer player, CompoundTag playerNBT) {
        DangerSenseEmit.performDangerSenseEmit(player);
    }

    public static void performBaseformDeactivation(ServerPlayer player)
    {
        //Remove Armor
        {
            //Get Armor Items
            Iterator<ItemStack> armorItems = player.getArmorSlots().iterator();

            //Delete Boots
            ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_BOOTS.get());
            itemToPlace.setTag(baseformArmorNBTTag);
            if(ItemStack.isSameItemSameTags(armorItems.next(),itemToPlace))
                player.setItemSlot(EquipmentSlot.FEET,ItemStack.EMPTY);

            //Delete Leggings
            itemToPlace = new ItemStack(ModItems.BASEFORM_LEGGINGS.get());
            itemToPlace.setTag(baseformArmorNBTTag);
            if(ItemStack.isSameItemSameTags(armorItems.next(),itemToPlace))
                player.setItemSlot(EquipmentSlot.LEGS,ItemStack.EMPTY);

            //Delete Chestplate
            itemToPlace = new ItemStack(ModItems.BASEFORM_CHESTPLATE.get());
            itemToPlace.setTag(baseformArmorNBTTag);
            if(ItemStack.isSameItemSameTags(armorItems.next(),itemToPlace))
                player.setItemSlot(EquipmentSlot.CHEST,ItemStack.EMPTY);
        }

        //Clear Each Ability's Effects
        {
            //Passives
            {
                //Double Jump

                //Auto Step
                player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).setBaseValue(0.0);

                //
            }
        }
        //Remove Data
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm->{
            playerSonicForm.deactivateBaseForm();
            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            playerSonicForm.getFormProperties()
                    ));
        });

        //Deinitialize Virtual Slot Handler
        PacketHandler.sendToPlayer(player,new VirtualSlotSyncS2C((byte)0));

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
