package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.speed_blitz;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
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
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;


public class SpeedBlitzDash implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SpeedBlitzDash> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "speed_blitz_dash"));

    public static final StreamCodec<FriendlyByteBuf, SpeedBlitzDash> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), SpeedBlitzDash::new);

    public SpeedBlitzDash() {}

    public SpeedBlitzDash(FriendlyByteBuf buffer) {}

    public void encode(FriendlyByteBuf buffer) {}

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SpeedBlitzDash msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null)
                    {
                        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                        //Speed Dash Timer
                        baseformProperties.speedBlitzDashTimer = 1;
                        baseformProperties.atkRotPhase = player.getYRot();

                        //Sound
                        player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.BLITZ.get(), SoundSource.MASTER, 0.1f, 1.0f);

                        //Thrust
                        player.setDeltaMovement(player.getLookAngle().scale(5.5));
                        player.connection.send(new ClientboundSetEntityMotionPacket(player));

                        //Remove Gravity
                        player.getAttribute(Attributes.GRAVITY).setBaseValue(0.0);

                        PacketHandler.sendToALLPlayers(
                                new SyncPlayerFormS2C(
                                        player.getId(),
                                        playerSonicForm
                                ));
                    }
                });
    }
}
