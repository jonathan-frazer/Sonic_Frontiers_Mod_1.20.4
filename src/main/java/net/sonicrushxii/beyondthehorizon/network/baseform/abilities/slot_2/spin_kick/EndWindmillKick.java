package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.spin_kick;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformActiveAbility;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Objects;

public class EndWindmillKick implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<EndWindmillKick> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "end_windmill_kick"));

    public static final StreamCodec<FriendlyByteBuf, EndWindmillKick> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), EndWindmillKick::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public EndWindmillKick() {}
    public EndWindmillKick(FriendlyByteBuf buffer) {}
    public void encode(FriendlyByteBuf buffer) {}

    public static void finishWindmillKick(ServerPlayer player) {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

        if (baseformProperties.windmillKick <= 0) return;

        Vec3 playerPos = new Vec3(player.getX(), player.getY(), player.getZ());
        for (LivingEntity target : player.level().getEntitiesOfClass(LivingEntity.class,
                new AABB(playerPos.x+3, playerPos.y+3, playerPos.z+3,
                         playerPos.x-3, playerPos.y-3, playerPos.z-3),
                e -> e != player && e.isAlive())) {
            Vec3 launchDir = new Vec3(target.getX()-player.getX(), 0.5, target.getZ()-player.getZ()).normalize();
            target.setDeltaMovement(launchDir.scale(2.0));
            player.connection.send(new ClientboundSetEntityMotionPacket(target));
            target.hurt(ModDamageTypes.getDamageSource(player.level(),
                    ModDamageTypes.SONIC_MELEE.getResourceKey(), player), 8.0f + baseformProperties.boostLvl * 10.0f);
        }

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.MASTER, 1.0f, 1.0f);

        baseformProperties.windmillKick = 0;
        Objects.requireNonNull(player.getAttribute(Attributes.GRAVITY)).setBaseValue(0.08);
        baseformProperties.setCooldown(BaseformActiveAbility.WINDMILL_KICK, (byte) 5);

        PacketHandler.sendToALLPlayers(new SyncPlayerFormS2C(player.getId(), playerSonicForm));
    }

    public static void handle(EndWindmillKick msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (player != null) finishWindmillKick(player);
        });
    }
}
