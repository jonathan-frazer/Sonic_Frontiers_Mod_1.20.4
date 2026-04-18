package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_wave;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import java.util.Objects;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformActiveAbility;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.entities.baseform.sonic_wave.SonicWaveEntity;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModEntityTypes;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class SonicWavePacket implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SonicWavePacket> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "sonic_wave"));

    public static final StreamCodec<FriendlyByteBuf, SonicWavePacket> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), SonicWavePacket::new);

    public SonicWavePacket() {}
    public SonicWavePacket(FriendlyByteBuf buf) {}
    public void encode(FriendlyByteBuf buf) {}

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(SonicWavePacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (player == null) return;

            PlayerSonicForm psf = player.getData(ModAttachments.PLAYER_SONIC_FORM);
            BaseformProperties props = (BaseformProperties) psf.getFormProperties();

            boolean isAir = !player.onGround();
            Vec3 look = player.getLookAngle();

            SonicWaveEntity wave = new SonicWaveEntity(ModEntityTypes.BASEFORM_SONIC_WAVE.get(), player.level());
            wave.setPos(player.getX(), player.getY() + 1.0, player.getZ());
            wave.setOwner(player.getUUID());

            if (isAir) {
                // Storm: diagonal downward path
                wave.setIsStorm(true);
                wave.setDuration(60); // 3 seconds in air
                Vec3 stormDir = new Vec3(look.x, -0.5, look.z).normalize().scale(1.5);
                wave.setMovementDirection(stormDir);
            } else {
                // Wave: flat ground slide
                wave.setIsStorm(false);
                wave.setDuration(40); // 2 seconds on ground
                Vec3 waveDir = new Vec3(look.x, 0, look.z).normalize().scale(1.5);
                wave.setMovementDirection(waveDir);
                // Brief spin — no gravity during cast
                Objects.requireNonNull(player.getAttribute(Attributes.GRAVITY)).setBaseValue(0.0);
                net.sonicrushxii.beyondthehorizon.scheduler.Scheduler.scheduleTask(
                        () -> player.getAttribute(Attributes.GRAVITY).setBaseValue(0.08), 10);
            }

            player.level().addFreshEntity(wave);

            props.setCooldown(BaseformActiveAbility.SONIC_WAVE, (byte) 100);

            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    ModSounds.SONIC_WIND_SHOOT.get(), SoundSource.MASTER, 1.0f, isAir ? 0.8f : 1.2f);

            PacketHandler.sendToALLPlayers(new SyncPlayerFormS2C(player.getId(), psf));
        });
    }
}
