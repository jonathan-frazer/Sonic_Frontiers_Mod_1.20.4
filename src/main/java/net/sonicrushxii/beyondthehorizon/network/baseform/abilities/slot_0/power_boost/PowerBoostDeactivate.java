package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.power_boost;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
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
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Iterator;

public class PowerBoostDeactivate {
    public PowerBoostDeactivate() {}

    public PowerBoostDeactivate(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    public static void performPowerBoostDeactivate(ServerPlayer player)
    {

        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Dequip Head
            if(baseformProperties.lightSpeedState != (byte)2)
            {
                Iterator<ItemStack> armorItems = player.getArmorSlots().iterator();
                armorItems.next(); armorItems.next();
                try {
                    if (armorItems.next().getTag().getByte("BeyondTheHorizon") == (byte) 1) {
                        ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_CHESTPLATE.get());
                        itemToPlace.setTag(BaseformProperties.baseformArmorNBTTag);
                        player.setItemSlot(EquipmentSlot.CHEST, itemToPlace);
                    }
                }
                catch(NullPointerException ignored){}

                try{
                    if(armorItems.next().getTag().getByte("BeyondTheHorizon") == (byte) 2){
                        EquipmentChangeHandler.playerHeadEquipmentLock.put(player.getUUID(),true);
                        player.setItemSlot(EquipmentSlot.HEAD, BaseformProperties.baseformSonicHead);
                    }
                }
                catch(NullPointerException ignored){}
            }

            //Power Boost
            baseformProperties.powerBoost = false;

            //Remove Speed Multiplier
            if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.POWERBOOST_SPEED))
                player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.POWERBOOST_SPEED.getId());

            //Remove Armor Multiplier
            if(player.getAttribute(Attributes.ARMOR).hasModifier(AttributeMultipliers.POWERBOOST_ARMOR))
                player.getAttribute(Attributes.ARMOR).removeModifier(AttributeMultipliers.POWERBOOST_ARMOR.getId());

            /*player.removeEffect(MobEffects.JUMP);
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, -1, 2, false, false));*/

            /*player.removeEffect(MobEffects.DAMAGE_RESISTANCE);
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, 3, false, false));*/

            //Decrease Strength
            player.removeEffect(MobEffects.DAMAGE_BOOST);
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 1, false, false));

            //Decrease Haste
            player.removeEffect(MobEffects.DIG_SPEED);
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, -1, 2, false, false));

            //Decay Sound
            player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.DEPOWER_BOOST.get(), SoundSource.MASTER, 0.75f, 0.75f);

            PacketHandler.sendToALLPlayers(
                    new SyncPlayerFormS2C(
                            player.getId(),
                            playerSonicForm
                    ));
        });
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                        performPowerBoostDeactivate(player);
                });
        ctx.setPacketHandled(true);
    }
}
