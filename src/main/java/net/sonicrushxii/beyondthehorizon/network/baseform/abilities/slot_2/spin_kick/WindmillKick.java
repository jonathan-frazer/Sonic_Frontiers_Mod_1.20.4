package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.spin_kick;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Objects;

public class WindmillKick implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<WindmillKick> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "windmill_kick"));

    public static final StreamCodec<FriendlyByteBuf, WindmillKick> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), WindmillKick::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public WindmillKick() {}
    public WindmillKick(FriendlyByteBuf buffer) {}
    public void encode(FriendlyByteBuf buffer) {}

    public static void handle(WindmillKick msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (player == null) return;
            PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            baseformProperties.windmillKick = 1;
            Objects.requireNonNull(player.getAttribute(Attributes.GRAVITY)).setBaseValue(0.0);

            PacketHandler.sendToALLPlayers(new SyncPlayerFormS2C(player.getId(), playerSonicForm));
        });
    }
}
