package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_5.ultimate_ability;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformClient;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformActiveAbility;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PhantomRushActivate implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PhantomRushActivate> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "phantom_rush_activate"));

    public static final StreamCodec<FriendlyByteBuf, PhantomRushActivate> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), PhantomRushActivate::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    private final UUID enemyID;

    public PhantomRushActivate(UUID enemyID) {
        this.enemyID = enemyID.equals(new UUID(0L, 0L)) ? null : enemyID;
    }

    public PhantomRushActivate(FriendlyByteBuf buffer) {
        UUID id = buffer.readUUID();
        this.enemyID = id.equals(new UUID(0L, 0L)) ? null : id;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUUID(enemyID != null ? enemyID : new UUID(0L, 0L));
    }

    // Client-side target scan (shared with UltimateActivate)
    public static void scanForward(Player player) {
        UltimateActivate.scanFoward(player);
    }

    public static void handle(PhantomRushActivate msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (player == null) return;

            PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            if (msg.enemyID != null && baseformProperties.getCooldown(BaseformActiveAbility.PHANTOM_RUSH) == 0) {
                baseformProperties.ultimateUse = 1;
                baseformProperties.phantomRushOnly = true;
                baseformProperties.ultTarget = msg.enemyID;
                baseformProperties.setCooldown(BaseformActiveAbility.PHANTOM_RUSH, (byte) 100);

                player.getAttribute(Attributes.GRAVITY).setBaseValue(0.0);
                player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);

                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        ModSounds.ULTIMATE_MUSIC.get(), SoundSource.MASTER, 1.0f, 1.0f);
            }

            PacketHandler.sendToALLPlayers(new SyncPlayerFormS2C(player.getId(), playerSonicForm));
        });
    }
}
