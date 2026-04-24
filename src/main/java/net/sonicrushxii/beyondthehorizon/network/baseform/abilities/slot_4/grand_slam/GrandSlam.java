package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_4.grand_slam;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class GrandSlam implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<GrandSlam> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "grand_slam"));

    public static final StreamCodec<FriendlyByteBuf, GrandSlam> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), GrandSlam::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public GrandSlam() {}

    public GrandSlam(FriendlyByteBuf buffer){}

    public void encode(FriendlyByteBuf buffer){}


    public static void handle(GrandSlam msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null){
                        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                        //Activate Grandslam

                        if(baseformProperties.grandSlamTime == 0)
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
                            if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.PARRY_SPEED.id()))
                                player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.PARRY_SPEED.id());

                            baseformProperties.grandSlamTime = 1;
                        }

                        PacketHandler.sendToALLPlayers(
                                new SyncPlayerFormS2C(
                                        player.getId(),
                                        playerSonicForm
                                ));
                    }
                });
    }
}
