package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.event_handler.EquipmentChangeHandler;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class ChargeSpindash {
    public ChargeSpindash() {    }

    public ChargeSpindash(FriendlyByteBuf buffer){    }

    public void encode(FriendlyByteBuf buffer){    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null){
                        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
                            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                            //Initialize Counter
                            baseformProperties.spinDashChargeTime = 0;
                            baseformProperties.selectiveInvul = true;

                            //Remove all Armor
                            EquipmentChangeHandler.playerHeadEquipmentLock.put(player.getUUID(),true);
                            player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                            player.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
                            player.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
                            player.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);

                            //Set Invisibility
                            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, -1, 2, false, false));

                            //Set Data -> Charging
                            baseformProperties.ballFormState = (byte)1;

                            //Lock Player in Position
                            player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0);

                            //PlaySound

                            PacketHandler.sendToPlayer(player,
                                    new SyncPlayerFormS2C(
                                            playerSonicForm.getCurrentForm(),
                                            baseformProperties
                                    ));
                        });
                    }
                });
        ctx.setPacketHandled(true);
    }
}
