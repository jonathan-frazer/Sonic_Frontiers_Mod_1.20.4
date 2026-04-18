package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformActiveAbility;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class Peelout implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<Peelout> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "peelout"));

    public static final StreamCodec<FriendlyByteBuf, Peelout> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), Peelout::new);

    public Peelout() {}
    public Peelout(FriendlyByteBuf buffer) {}
    public void encode(FriendlyByteBuf buffer) {}

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(Peelout msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (player != null) {
                PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                // Launch in look direction at max speed, no ball form
                net.minecraft.world.phys.Vec3 look = player.getLookAngle();
                player.setDeltaMovement(look.x * 10.0, 0.2, look.z * 10.0);
                player.connection.send(new ClientboundSetEntityMotionPacket(player));

                baseformProperties.setCooldown(BaseformActiveAbility.PEELOUT, (byte) 100);

                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        ModSounds.SPINDASH_RELEASE.get(), SoundSource.MASTER, 1.0f, 1.3f);

                PacketHandler.sendToALLPlayers(new SyncPlayerFormS2C(player.getId(), playerSonicForm));
            }
        });
    }
}
