package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeMod;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModEffects;
import net.sonicrushxii.beyondthehorizon.modded.ModItems;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
import net.sonicrushxii.beyondthehorizon.network.sync.PlayerStopSoundPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.VirtualSlotSyncS2C;

import java.util.Iterator;

public class BaseformTransformer {

    public static void performActivation(ServerPlayer player)
    {
        //Equip Armor
        //SET ARMOR NBT DATA(COMMON)
        {
            Iterator<ItemStack> armorItems = player.getArmorSlots().iterator();
            if (armorItems.next().isEmpty()) {
                ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_BOOTS.get());
                itemToPlace.setTag(BaseformProperties.baseformArmorNBTTag);
                player.setItemSlot(EquipmentSlot.FEET, itemToPlace);
            }
            if (armorItems.next().isEmpty()) {
                ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_LEGGINGS.get());
                itemToPlace.setTag(BaseformProperties.baseformArmorNBTTag);
                player.setItemSlot(EquipmentSlot.LEGS, itemToPlace);
            }
            if (armorItems.next().isEmpty()) {
                ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_CHESTPLATE.get());
                itemToPlace.setTag(BaseformProperties.baseformArmorNBTTag);
                player.setItemSlot(EquipmentSlot.CHEST, itemToPlace);
            }
        }

        //Effects
        {
            //Speed
            player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);

            //Step Height
            if (!player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).hasModifier(AttributeMultipliers.STEP_UP_BASE))
                player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).addTransientModifier(AttributeMultipliers.STEP_UP_BASE);

            //Jump
            if(!player.hasEffect(MobEffects.JUMP)) player.addEffect(new MobEffectInstance(MobEffects.JUMP, -1, 2, false, false));
            else player.getEffect(MobEffects.JUMP).update(new MobEffectInstance(MobEffects.JUMP, -1, 2, false, false));

            //Jump
            if(!player.hasEffect(MobEffects.DAMAGE_RESISTANCE)) player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, 3, false, false));
            else player.getEffect(MobEffects.DAMAGE_RESISTANCE).update(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, 3, false, false));

            //Strength
            if(!player.hasEffect(MobEffects.DAMAGE_BOOST)) player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 1, false, false));
            else player.getEffect(MobEffects.DAMAGE_BOOST).update(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 1, false, false));

            //Haste
            if(!player.hasEffect(MobEffects.DIG_SPEED)) player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, -1, 1, false, false));
            else player.getEffect(MobEffects.DIG_SPEED).update(new MobEffectInstance(MobEffects.DIG_SPEED, -1, 1, false, false));
        }


        //Add Data
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm->{
            playerSonicForm.activateBaseForm();
            PacketHandler.sendToALLPlayers(
                    new SyncPlayerFormS2C(
                            player.getId(),
                            playerSonicForm
                    ));
        });

        //Initialize Virtual Slot Handler
        PacketHandler.sendToPlayer(player,new VirtualSlotSyncS2C((byte)6));

        //Commands
        {
            CommandSourceStack commandSourceStack = player.createCommandSourceStack().withPermission(4).withSuppressedOutput();
            MinecraftServer server = player.serverLevel().getServer();
            server.
                    getCommands().
                    performPrefixedCommand(commandSourceStack,"function beyondthehorizon:baseform/activate");
        }
    }

    public static void performDeactivation(ServerPlayer player)
    {
        //Kill Head
        Level world = player.level();
        if(!player.isAlive() && !world.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY))
        {
            for (ItemEntity itemEntity : world.getEntitiesOfClass(ItemEntity.class,
                    new AABB(player.getX() + 2, player.getY() + 2, player.getZ() + 2,
                            player.getX() - 2, player.getY() - 2, player.getZ() - 2),
                    (itemEntity) -> {
                        try {
                            byte bthTagVal = itemEntity.getItem().getTag().getByte("BeyondTheHorizon");
                            return (bthTagVal == (byte)1 || bthTagVal == (byte)2);
                        }catch(NullPointerException ignored){
                            return false;
                        }
                    }))
                itemEntity.remove(Entity.RemovalReason.KILLED);
        }

        //Add Item on the Ground
        ItemEntity sonicHeadItem = new ItemEntity(player.level(),
                player.getX(),player.getY(),player.getZ(),BaseformProperties.baseformSonicHead);
        sonicHeadItem.setNoPickUpDelay();
        player.level().addFreshEntity(sonicHeadItem);

        //Remove Armor
        {
            //Get Armor Items
            Iterator<ItemStack> armorItems = player.getArmorSlots().iterator();

            //Delete Boots
            ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_BOOTS.get());
            itemToPlace.setTag(BaseformProperties.baseformArmorNBTTag);
            if(ItemStack.isSameItemSameTags(armorItems.next(),itemToPlace))
                player.setItemSlot(EquipmentSlot.FEET,ItemStack.EMPTY);

            //Delete Leggings
            itemToPlace = new ItemStack(ModItems.BASEFORM_LEGGINGS.get());
            itemToPlace.setTag(BaseformProperties.baseformArmorNBTTag);
            if(ItemStack.isSameItemSameTags(armorItems.next(),itemToPlace))
                player.setItemSlot(EquipmentSlot.LEGS,ItemStack.EMPTY);

            //Delete Chestplate
            itemToPlace = new ItemStack(ModItems.BASEFORM_CHESTPLATE.get());
            itemToPlace.setTag(BaseformProperties.baseformArmorNBTTag);
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

                //Danger Sense
                PacketHandler.sendToPlayer(player, new PlayerStopSoundPacketS2C(
                                ModSounds.DANGER_SENSE.get().getLocation()
                        )
                );

                //Hunger
            }

            //Slot 1
            {
                //Reset Water Boost
                player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);

                //Reset Light Speed Attack
                if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.LIGHTSPEED_MODE))
                    player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.LIGHTSPEED_MODE.getId());

                //Reset Power Boost
                if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.POWERBOOST_SPEED))
                    player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.POWERBOOST_SPEED.getId());
                if(player.getAttribute(Attributes.ARMOR).hasModifier(AttributeMultipliers.POWERBOOST_ARMOR))
                    player.getAttribute(Attributes.ARMOR).removeModifier(AttributeMultipliers.POWERBOOST_ARMOR.getId());

            }

            //Slot 2
            {
                //Homing Attack
                //Gravity Removal

                //Melee Swipes
                //Gravity Removal

                //Speed Blitz
                player.removeEffect(ModEffects.SPEED_BLITZING.get());

                //Smash HIt
                if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.SMASH_HIT))
                    player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.SMASH_HIT.getId());
            }

            //Slot 6
            {

            }

        }
        //Remove Data
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm->{
            playerSonicForm.deactivateBaseForm();
            PacketHandler.sendToALLPlayers(
                    new SyncPlayerFormS2C(
                            player.getId(),
                            playerSonicForm
                    ));
        });

        //Deinitialize Virtual Slot Handler
        PacketHandler.sendToPlayer(player,new VirtualSlotSyncS2C((byte)0));

        //Remove Effects
        if (player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).hasModifier(AttributeMultipliers.STEP_UP_BASE)) player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).removeModifier(AttributeMultipliers.STEP_UP_BASE.getId());
        player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.10000000149011612);
        player.removeEffect(MobEffects.JUMP);
        player.removeEffect(MobEffects.DAMAGE_RESISTANCE);
        player.removeEffect(MobEffects.DAMAGE_BOOST);
        player.removeEffect(MobEffects.DIG_SPEED);
        player.removeEffect(MobEffects.ABSORPTION);

        //Commands
        CommandSourceStack commandSourceStack = player.createCommandSourceStack().withPermission(4).withSuppressedOutput();
        MinecraftServer server = player.serverLevel().getServer();

        server.
                getCommands().
                performPrefixedCommand(commandSourceStack,"function beyondthehorizon:baseform/deactivate");
    }
}
