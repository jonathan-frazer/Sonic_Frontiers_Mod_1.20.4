package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformActiveAbility;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformHandler;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.event_handler.EquipmentChangeHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModItems;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Iterator;

public class LightspeedDecay {

    public LightspeedDecay() {}

    public LightspeedDecay(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    public static void performLightspeedDecay(ServerPlayer player)
    {

        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            //Add Tag
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();
            baseformProperties.lightSpeedState = (byte)0;
            baseformProperties.setCooldown(BaseformActiveAbility.LIGHT_SPEED_ATTACK, (byte) 60);

            //Requip Armor
            //SET ARMOR NBT DATA(COMMON)
            {
                Iterator<ItemStack> armorItems = player.getArmorSlots().iterator();
                try {
                    if (armorItems.next().getTag().getByte("BeyondTheHorizon") == (byte) 1) {
                        ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_BOOTS.get());
                        itemToPlace.setTag(BaseformHandler.baseformArmorNBTTag);
                        player.setItemSlot(EquipmentSlot.FEET, itemToPlace);
                    }
                }
                catch(NullPointerException ignored){}

                try {
                    if (armorItems.next().getTag().getByte("BeyondTheHorizon") == (byte) 1) {
                        ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_LEGGINGS.get());
                        itemToPlace.setTag(BaseformHandler.baseformArmorNBTTag);
                        player.setItemSlot(EquipmentSlot.LEGS, itemToPlace);
                    }
                }
                catch(NullPointerException ignored){}

                try {
                    if (armorItems.next().getTag().getByte("BeyondTheHorizon") == (byte) 1) {
                        ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_CHESTPLATE.get());
                        itemToPlace.setTag(BaseformHandler.baseformArmorNBTTag);
                        player.setItemSlot(EquipmentSlot.CHEST, itemToPlace);
                    }
                }
                catch(NullPointerException ignored){}

                try{
                    if(armorItems.next().getTag().getByte("BeyondTheHorizon") == (byte) 2)
                    {
                        EquipmentChangeHandler.playerHeadEquipmentLock.put(player.getUUID(), true);

                        if(baseformProperties.powerBoost) player.setItemSlot(EquipmentSlot.HEAD, BaseformHandler.baseformPBSonicHead);
                        else                              player.setItemSlot(EquipmentSlot.HEAD, BaseformHandler.baseformSonicHead);
                    }
                }
                catch(NullPointerException ignored){}
            }

            //Remove Speed Boost
            if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.LIGHTSPEED_MODE))
                player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.LIGHTSPEED_MODE.getId());

            //Decay Sound
            player.level().playSound(null,player.getX(),player.getY(),player.getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.MASTER, 1.0f, 1.0f);

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
                        performLightspeedDecay(player);
                });
        ctx.setPacketHandled(true);
    }
}

