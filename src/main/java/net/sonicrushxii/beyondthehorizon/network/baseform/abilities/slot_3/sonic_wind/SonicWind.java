package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_wind;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class SonicWind implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<SonicWind> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "sonic_wind"));

    public static final StreamCodec<FriendlyByteBuf, SonicWind> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), SonicWind::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public SonicWind() {

    }

    public SonicWind(FriendlyByteBuf buffer){

    }

    public void encode(FriendlyByteBuf buffer){
    }

    public static void handle(SonicWind msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null){
                        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                        //Changed Data
                        baseformProperties.sonicWind = 1;

                        //Remove Gravity
                        player.getAttribute(NeoForgeMod.ENTITY_GRAVITY).setBaseValue(0.0);

                        //Set Motion to Zero
                        player.setDeltaMovement(0,0,0);
                        player.connection.send(new ClientboundSetEntityMotionPacket(player));

                        //Play Sound
                        player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.HOMING_ATTACK.get(), SoundSource.MASTER, 1.0f, 1.0f);

                        PacketHandler.sendToALLPlayers(
                                new SyncPlayerFormS2C(
                                        player.getId(),
                                        playerSonicForm
                                ));
                    }
                });
    }
}
