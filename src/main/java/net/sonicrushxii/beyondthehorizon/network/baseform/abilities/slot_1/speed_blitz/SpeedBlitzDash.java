package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.speed_blitz;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;


public class SpeedBlitzDash {

    public SpeedBlitzDash() {}

    public SpeedBlitzDash(FriendlyByteBuf buffer) {}

    public void encode(FriendlyByteBuf buffer) {}

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                    {
                        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
                            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                            //Speed Dash Timer
                            baseformProperties.speedBlitzDashTimer = 1;

                            //Sound
                            player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.BLITZ.get(), SoundSource.MASTER, 0.1f, 1.0f);

                            //Thrust
                            player.setDeltaMovement(player.getLookAngle().scale(5.5));
                            player.connection.send(new ClientboundSetEntityMotionPacket(player));

                            //Remove Gravity
                            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.0);

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
