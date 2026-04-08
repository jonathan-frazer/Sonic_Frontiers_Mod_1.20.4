package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class LightspeedCharge implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<LightspeedCharge> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "lightspeed_charge"));

    public static final StreamCodec<FriendlyByteBuf, LightspeedCharge> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), LightspeedCharge::new);

    public LightspeedCharge() {}

    public LightspeedCharge(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void performLightspeedCharge(ServerPlayer player)
    {
        //Add Tag
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();
        baseformProperties.lightSpeedState = (byte)1;

        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));

        //Slow Down
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 66, 22, false, false));

        //Sound
        Level world = player.level();
        world.playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.LIGHT_SPEED_CHARGE.get(), SoundSource.MASTER, 1.0f, 1.0f);
    }

    public static void handle(LightspeedCharge msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null)
                        performLightspeedCharge(player);

                });
    }
}

