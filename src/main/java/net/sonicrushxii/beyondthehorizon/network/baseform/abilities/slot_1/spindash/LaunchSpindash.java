package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
import net.sonicrushxii.beyondthehorizon.network.sync.PlayerStopSoundPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.UUID;

public class LaunchSpindash {
    public LaunchSpindash() {    }

    public LaunchSpindash(FriendlyByteBuf buffer){    }

    public void encode(FriendlyByteBuf buffer){    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null){
                        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
                            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                            //Set Data -> Charging
                            baseformProperties.ballFormState = (byte)2;
                            baseformProperties.selectiveInvul = true;

                            //Launch
                            if (player.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(new UUID(0x1234767890AB9DEFL, 0xFEBCBA09F7654C21L)) == null)
                                player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(
                                        new AttributeModifier(new UUID(0x1234767890AB9DEFL, 0xFEBCBA09F7654C21L),
                                                "Spindash_Speed", Math.min(baseformProperties.spinDashChargeTime/100.0,1.0), AttributeModifier.Operation.ADDITION));
                            player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
                            player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).setBaseValue(1.5);

                            //PlaySound
                            Level world = player.level();
                            PacketHandler.sendToALLPlayers(new PlayerStopSoundPacketS2C(ModSounds.SPINDASH_CHARGE.get().getLocation()));
                            world.playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.SPINDASH_RELEASE.get(), SoundSource.MASTER, 1.0f, 1.0f);

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

