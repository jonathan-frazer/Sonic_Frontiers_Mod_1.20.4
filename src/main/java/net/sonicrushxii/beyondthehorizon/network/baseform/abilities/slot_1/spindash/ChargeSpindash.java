package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
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
                            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.80);

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
