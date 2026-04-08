package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.PlayerStopSoundPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class LightspeedCancel implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<LightspeedCancel> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "lightspeed_cancel"));

    public static final StreamCodec<FriendlyByteBuf, LightspeedCancel> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), LightspeedCancel::new);

    public LightspeedCancel() {}

    public LightspeedCancel(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void performLightspeedCancel(ServerPlayer player)
    {
        Level world = player.level();

        //Remove Slowdown
        player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);

        //Cancel the Sound
        PacketHandler.sendToALLPlayers(new PlayerStopSoundPacketS2C(ModSounds.LIGHT_SPEED_CHARGE.get().getLocation()));
        world.playSound(null,player.getX(),player.getY(),player.getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.MASTER, 1.0f, 1.0f);

        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();
        baseformProperties.lightSpeedState = (byte)0;

        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));
    }

    public static void handle(LightspeedCancel msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null)
                        performLightspeedCancel(player);
                });
    }
}
