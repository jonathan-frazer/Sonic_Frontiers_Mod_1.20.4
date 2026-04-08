package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class ChargeSpindash implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ChargeSpindash> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "charge_spindash"));

    public static final StreamCodec<FriendlyByteBuf, ChargeSpindash> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), ChargeSpindash::new);

    public ChargeSpindash() {    }

    public ChargeSpindash(FriendlyByteBuf buffer){    }

    public void encode(FriendlyByteBuf buffer){    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ChargeSpindash msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null){
                        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                        //Initialize Counter
                        baseformProperties.spinDashChargeTime = 0;

                        //Set Data -> Charging
                        baseformProperties.ballFormState = (byte)1;

                        //Lock Player in Position
                        player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0);
                        player.getAttribute(NeoForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.80);

                        //PlaySound
                        Level world = player.level();
                        world.playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.SPINDASH_CHARGE.get(), SoundSource.MASTER, 1.0f, 1.0f);

                        PacketHandler.sendToALLPlayers(
                                new SyncPlayerFormS2C(
                                        player.getId(),
                                        playerSonicForm
                                ));
                    }
                });
    }
}
