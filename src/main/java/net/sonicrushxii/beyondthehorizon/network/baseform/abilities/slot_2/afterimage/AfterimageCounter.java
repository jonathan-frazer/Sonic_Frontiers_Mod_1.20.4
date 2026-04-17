package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.afterimage;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class AfterimageCounter implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AfterimageCounter> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "afterimage_counter"));

    public static final StreamCodec<FriendlyByteBuf, AfterimageCounter> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), AfterimageCounter::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public AfterimageCounter() {}
    public AfterimageCounter(FriendlyByteBuf buffer) {}
    public void encode(FriendlyByteBuf buffer) {}

    public static void handle(AfterimageCounter msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (player == null) return;
            PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            // Activate 2-second counter window, end active afterimage invisibility
            baseformProperties.afterimageCounter = 40;
            baseformProperties.afterimage = 0;
            if (player.hasEffect(MobEffects.INVISIBILITY))
                player.removeEffect(MobEffects.INVISIBILITY);

            PacketHandler.sendToALLPlayers(new SyncPlayerFormS2C(player.getId(), playerSonicForm));
        });
    }
}
