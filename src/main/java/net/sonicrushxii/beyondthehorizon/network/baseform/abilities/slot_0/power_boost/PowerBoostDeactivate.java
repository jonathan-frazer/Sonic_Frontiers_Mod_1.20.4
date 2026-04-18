package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.power_boost;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.event_handler.EquipmentChangeHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModItems;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Iterator;

public class PowerBoostDeactivate implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PowerBoostDeactivate> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "power_boost_deactivate"));

    public static final StreamCodec<FriendlyByteBuf, PowerBoostDeactivate> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), PowerBoostDeactivate::new);

    public PowerBoostDeactivate() {}

    public PowerBoostDeactivate(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void performPowerBoostDeactivate(ServerPlayer player)
    {

        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

        //Dequip Head
        if(baseformProperties.lightSpeedState != (byte)2)
        {
            Iterator<ItemStack> armorItems = player.getArmorSlots().iterator();
            armorItems.next(); armorItems.next();
            try {
                if (armorItems.next().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getByte("BeyondTheHorizon") == (byte) 1) {
                    ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_CHESTPLATE.get());
                    itemToPlace.set(DataComponents.CUSTOM_DATA, CustomData.of(BaseformProperties.baseformArmorNBTTag));
                    player.setItemSlot(EquipmentSlot.CHEST, itemToPlace);
                }
            }
            catch(NullPointerException ignored){}

            try{
                if(armorItems.next().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getByte("BeyondTheHorizon") == (byte) 2){
                    EquipmentChangeHandler.playerHeadEquipmentLock.put(player.getUUID(),true);
                    player.setItemSlot(EquipmentSlot.HEAD, BaseformProperties.baseformSonicHead);
                }
            }
            catch(NullPointerException ignored){}
        }

        //Power Boost
        baseformProperties.powerBoost = false;
        baseformProperties.powerBoostLevel = 0;

        //Restore tick rate
        CommandSourceStack cs = player.createCommandSourceStack().withPermission(4).withSuppressedOutput();
        player.serverLevel().getServer().getCommands().performPrefixedCommand(cs, "tick rate 20");

        //Remove Speed Multiplier
        if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.POWERBOOST_SPEED.id()))
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.POWERBOOST_SPEED.id());

        //Remove Armor Multiplier
        if(player.getAttribute(Attributes.ARMOR).hasModifier(AttributeMultipliers.POWERBOOST_ARMOR.id()))
            player.getAttribute(Attributes.ARMOR).removeModifier(AttributeMultipliers.POWERBOOST_ARMOR.id());

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
    }

    public static void handle(PowerBoostDeactivate msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null)
                        performPowerBoostDeactivate(player);
                });
    }
}
