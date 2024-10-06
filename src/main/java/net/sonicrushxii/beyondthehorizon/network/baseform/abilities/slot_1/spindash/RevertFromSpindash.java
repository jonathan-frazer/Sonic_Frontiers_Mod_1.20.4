package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformHandler;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.event_handler.EquipmentChangeHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModItems;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.UUID;

public class RevertFromSpindash {
    public RevertFromSpindash() {    }

    public RevertFromSpindash(FriendlyByteBuf buffer){    }

    public void encode(FriendlyByteBuf buffer){    }

    public static void performRevertSpindash(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Remove Ballform
            baseformProperties.ballFormState = (byte)0;
            baseformProperties.selectiveInvul = false;

            //Return to normal
            {
                //Feet
                ItemStack armorItem = (baseformProperties.lightSpeedState == (byte) 2) ? new ItemStack(ModItems.BASEFORM_LIGHTSPEED_BOOTS.get()) : new ItemStack(ModItems.BASEFORM_BOOTS.get());
                armorItem.setTag(BaseformHandler.baseformArmorNBTTag);
                player.setItemSlot(EquipmentSlot.FEET, armorItem);

                //Legs
                armorItem = (baseformProperties.lightSpeedState == (byte) 2) ? new ItemStack(ModItems.BASEFORM_LIGHTSPEED_LEGGINGS.get()) : new ItemStack(ModItems.BASEFORM_LEGGINGS.get());
                armorItem.setTag(BaseformHandler.baseformArmorNBTTag);
                player.setItemSlot(EquipmentSlot.LEGS, armorItem);

                //Chest
                armorItem = (baseformProperties.lightSpeedState == (byte) 2) ? new ItemStack(ModItems.BASEFORM_LIGHTSPEED_CHESTPLATE.get()) : new ItemStack(ModItems.BASEFORM_CHESTPLATE.get());
                armorItem.setTag(BaseformHandler.baseformArmorNBTTag);
                player.setItemSlot(EquipmentSlot.CHEST, armorItem);

                //Head
                EquipmentChangeHandler.playerHeadEquipmentLock.put(player.getUUID(),true);
                if (baseformProperties.lightSpeedState == (byte) 2)
                    player.setItemSlot(EquipmentSlot.HEAD, BaseformHandler.baseformLSSonicHead);
                else if (baseformProperties.powerBoost)
                    player.setItemSlot(EquipmentSlot.HEAD, BaseformHandler.baseformPBSonicHead);
                else
                    player.setItemSlot(EquipmentSlot.HEAD, BaseformHandler.baseformSonicHead);
            }

            //Normal Speed
            //Launch
            if (player.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(new UUID(0x1234767890AB9DEFL, 0xFEBCBA09F7654C21L)) != null)
                player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(new UUID(0x1234767890AB9DEFL, 0xFEBCBA09F7654C21L));

            if(baseformProperties.boostLvl == 0 && !player.isSprinting())
                player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).setBaseValue(0.0);

            //Set Invisibility
            player.setInvisible(false);

            //PlaySound


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
                    if(player != null){
                        performRevertSpindash(player);
                    }
                });
        ctx.setPacketHandled(true);
    }
}

