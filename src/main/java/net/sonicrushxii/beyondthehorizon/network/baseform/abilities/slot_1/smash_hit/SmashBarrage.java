package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.smash_hit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class SmashBarrage implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SmashBarrage> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "smash_barrage"));

    public static final StreamCodec<FriendlyByteBuf, SmashBarrage> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), SmashBarrage::new);

    public SmashBarrage() {}
    public SmashBarrage(FriendlyByteBuf buffer) {}
    public void encode(FriendlyByteBuf buffer) {}

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(SmashBarrage msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (player != null) {
                PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                baseformProperties.smashBarrage = 1;

                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        ModSounds.SMASH_CHARGE.get(), SoundSource.MASTER, 1.0f, 1.2f);

                PacketHandler.sendToALLPlayers(new SyncPlayerFormS2C(player.getId(), playerSonicForm));
            }
        });
    }
}
