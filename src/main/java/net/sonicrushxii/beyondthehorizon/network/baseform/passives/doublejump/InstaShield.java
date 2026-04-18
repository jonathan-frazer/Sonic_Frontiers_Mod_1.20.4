package net.sonicrushxii.beyondthehorizon.network.baseform.passives.doublejump;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;

public class InstaShield implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<InstaShield> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "insta_shield"));

    public static final StreamCodec<FriendlyByteBuf, InstaShield> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), InstaShield::new);

    public InstaShield() {}
    public InstaShield(FriendlyByteBuf buf) {}
    public void encode(FriendlyByteBuf buf) {}

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(InstaShield msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (player == null) return;

            PlayerSonicForm psf = player.getData(ModAttachments.PLAYER_SONIC_FORM);
            BaseformProperties props = (BaseformProperties) psf.getFormProperties();

            // 0.5s (10 ticks) of invulnerability
            props.dodgeInvul = true;
            PacketHandler.sendToALLPlayers(new SyncPlayerFormS2C(player.getId(), psf));

            Scheduler.scheduleTask(() -> {
                props.dodgeInvul = false;
                PacketHandler.sendToALLPlayers(new SyncPlayerFormS2C(player.getId(), psf));
            }, 10);

            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SHIELD_BLOCK, SoundSource.MASTER, 1.0f, 1.8f);
        });
    }
}
