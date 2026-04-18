package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.boost;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.List;

public class UniversalDash implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UniversalDash> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "universal_dash"));

    public static final StreamCodec<FriendlyByteBuf, UniversalDash> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), UniversalDash::new);

    public UniversalDash() {}
    public UniversalDash(FriendlyByteBuf buf) {}
    public void encode(FriendlyByteBuf buf) {}

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(UniversalDash msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (player == null) return;

            PlayerSonicForm psf = player.getData(ModAttachments.PLAYER_SONIC_FORM);
            BaseformProperties props = (BaseformProperties) psf.getFormProperties();

            if (props.getCooldown(BaseformActiveAbility.UNIVERSAL_DASH) != 0) return;

            Vec3 look = player.getLookAngle();
            double speed = player.getAttribute(Attributes.MOVEMENT_SPEED).getValue();

            // Dash trajectory — no ball curl (unlike AirBoost)
            player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
            player.addDeltaMovement(look.scale(2 * speed));
            player.connection.send(new ClientboundSetEntityMotionPacket(player));

            // Damage aura: hit enemies in dash radius with boost-scaled damage
            float dashDmg = 6.0f + props.boostLvl * 10.0f;
            Vec3 playerPos = player.position().add(0, 1, 0);
            AABB dashBox = new AABB(
                playerPos.x - 2.5, playerPos.y - 1.5, playerPos.z - 2.5,
                playerPos.x + 2.5, playerPos.y + 1.5, playerPos.z + 2.5
            );
            List<LivingEntity> nearby = player.level().getEntitiesOfClass(
                LivingEntity.class, dashBox,
                e -> !e.is(player) && e.isAlive()
            );
            for (LivingEntity target : nearby) {
                target.hurt(ModDamageTypes.getDamageSource(player.level(),
                    ModDamageTypes.SONIC_MELEE.getResourceKey(), player), dashDmg);
                target.addDeltaMovement(look.scale(1.5));
                player.connection.send(new ClientboundSetEntityMotionPacket(target));
            }

            props.setCooldown(BaseformActiveAbility.UNIVERSAL_DASH, (byte) 20);

            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.AIR_BOOST.get(), SoundSource.MASTER, 1.0f, 1.2f);

            PacketHandler.sendToALLPlayers(new SyncPlayerFormS2C(player.getId(), psf));
        });
    }
}
