package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.stomp;

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

public class BounceJump implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BounceJump> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "bounce_jump"));

    public static final StreamCodec<FriendlyByteBuf, BounceJump> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> {}, BounceJump::new);

    public BounceJump() {}
    public BounceJump(FriendlyByteBuf buffer) {}

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(BounceJump msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if(player == null) return;

            PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            if(baseformProperties.bounceWindowTimer > 0 && baseformProperties.stomp > 0
                    && baseformProperties.ballFormState > 0 && baseformProperties.bounceCount < 3) {

                baseformProperties.bounceCount++;
                baseformProperties.bounceWindowTimer = 0;

                double[] vels = {1.3, 1.85, 2.3};
                double bounceVel = vels[baseformProperties.bounceCount - 1];

                player.getAttribute(Attributes.GRAVITY).setBaseValue(0.08);
                player.setDeltaMovement(player.getDeltaMovement().x, bounceVel, player.getDeltaMovement().z);
                player.connection.send(new ClientboundSetEntityMotionPacket(player));

                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        ModSounds.STOMP.get(), SoundSource.MASTER, 1.0f, 1.0f + 0.2f * baseformProperties.bounceCount);

                baseformProperties.stomp = 1;
                PacketHandler.sendToALLPlayers(new SyncPlayerFormS2C(player.getId(), playerSonicForm));
            }
        });
    }
}
