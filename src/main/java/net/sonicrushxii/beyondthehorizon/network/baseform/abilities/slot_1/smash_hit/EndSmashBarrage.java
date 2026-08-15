package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.smash_hit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
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

public class EndSmashBarrage implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<EndSmashBarrage> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "end_smash_barrage"));

    public static final StreamCodec<FriendlyByteBuf, EndSmashBarrage> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), EndSmashBarrage::new);

    public EndSmashBarrage() {}
    public EndSmashBarrage(FriendlyByteBuf buffer) {}
    public void encode(FriendlyByteBuf buffer) {}

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void finishSmashBarrage(ServerPlayer player) {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        if (!(playerSonicForm.getFormProperties() instanceof BaseformProperties baseformProperties)) return;

        baseformProperties.smashBarrage = 0;
        baseformProperties.setCooldown(BaseformActiveAbility.SMASH_BARRAGE, (byte) 60);

        // Knockback burst on release
        for (LivingEntity enemy : player.level().getEntitiesOfClass(LivingEntity.class,
                new AABB(player.getX() - 3.5, player.getY() - 1.0, player.getZ() - 3.5,
                         player.getX() + 3.5, player.getY() + 2.0, player.getZ() + 3.5),
                target -> !target.is(player))) {
            Vec3 knockDir = enemy.position().subtract(player.position()).normalize();
            enemy.hurt(ModDamageTypes.getDamageSource(player.level(),
                    ModDamageTypes.SONIC_MELEE.getResourceKey(), player), 12.0f);
            enemy.setDeltaMovement(knockDir.scale(3.0).add(0, 0.5, 0));
            player.connection.send(new ClientboundSetEntityMotionPacket(enemy));
        }

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.MASTER, 1.2f, 0.9f);

        PacketHandler.sendToALLPlayers(new SyncPlayerFormS2C(player.getId(), playerSonicForm));
    }

    public static void handle(EndSmashBarrage msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (player != null) {
                finishSmashBarrage(player);
            }
        });
    }
}
