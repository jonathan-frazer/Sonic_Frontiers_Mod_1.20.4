package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.event_handler.EquipmentChangeHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModItems;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
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
        {
            Iterator<ItemStack> armorItems = player.getArmorSlots().iterator();
            try {
                if (armorItems.next().getTag().getByte("BeyondTheHorizon") == (byte) 1) {
                    ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_LIGHTSPEED_BOOTS.get());
                    itemToPlace.setTag(BaseformProperties.baseformArmorNBTTag);
                    player.setItemSlot(EquipmentSlot.FEET, itemToPlace);
                }
            }
            catch(NullPointerException ignored){}

            try {
                if (armorItems.next().getTag().getByte("BeyondTheHorizon") == (byte) 1) {
                    ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_LIGHTSPEED_LEGGINGS.get());
                    itemToPlace.setTag(BaseformProperties.baseformArmorNBTTag);
                    player.setItemSlot(EquipmentSlot.LEGS, itemToPlace);
                }
            }
            catch(NullPointerException ignored){}

            try {
                if (armorItems.next().getTag().getByte("BeyondTheHorizon") == (byte) 1) {
                    ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_LIGHTSPEED_CHESTPLATE.get());
                    itemToPlace.setTag(BaseformProperties.baseformArmorNBTTag);
                    player.setItemSlot(EquipmentSlot.CHEST, itemToPlace);
                }
            }
            catch(NullPointerException ignored){}

            try{
                if(armorItems.next().getTag().getByte("BeyondTheHorizon") == (byte) 2){
                    EquipmentChangeHandler.playerHeadEquipmentLock.put(player.getUUID(),true);
                    player.setItemSlot(EquipmentSlot.HEAD, BaseformProperties.baseformLSSonicHead);
                }
            }
            catch(NullPointerException ignored){}
        }
        //Remove Slowdown
        player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        //Add Speed Boost
        if (!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.LIGHTSPEED_MODE))
            player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(AttributeMultipliers.LIGHTSPEED_MODE);

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

