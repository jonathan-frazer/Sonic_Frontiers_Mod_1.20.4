package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.event_handler.EquipmentChangeHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
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

                            //Set Data -> Charging
                            baseformProperties.ballFormState = (byte)1;

                            //Lock Player in Position
                            player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0);

                            //PlaySound
                            Level world = player.level();
                            world.playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.SPINDASH_CHARGE.get(), SoundSource.MASTER, 1.0f, 1.0f);

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
