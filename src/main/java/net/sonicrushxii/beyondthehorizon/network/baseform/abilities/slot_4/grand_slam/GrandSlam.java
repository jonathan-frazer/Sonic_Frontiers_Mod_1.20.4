package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_4.grand_slam;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class GrandSlam
{
    public GrandSlam() {}

    public GrandSlam(FriendlyByteBuf buffer){}

    public void encode(FriendlyByteBuf buffer){}


    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null){
                        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
                            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                            //Activate Grandslam

                            if(baseformProperties.grandSlamTime == 0 && baseformProperties.parryTimeSlow > 0)
                            {
                                //Reset Data
                                baseformProperties.parryTimeSlow = 0;
                                baseformProperties.parryTime = 0;

                                //Stop Timeslow
                                baseformProperties.counterReady = false;

                                //baseformProperties.counteredEntity = new UUID(0L,0L); We still wanna store the counteredEntity

                                //Play Sound
                                player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.GRAND_SLAM.get(), SoundSource.MASTER, 0.85f, 1.0f);

                                //Remove Attributes
                                if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.PARRY_SPEED))
                                    player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.PARRY_SPEED.getId());

                                baseformProperties.grandSlamTime = 1;
                            }

                            PacketHandler.sendToALLPlayers(
                                    new SyncPlayerFormS2C(
                                            player.getId(),
                                            playerSonicForm
                                    ));
                        });
                    }
                });
        ctx.setPacketHandled(true);
    }
}
