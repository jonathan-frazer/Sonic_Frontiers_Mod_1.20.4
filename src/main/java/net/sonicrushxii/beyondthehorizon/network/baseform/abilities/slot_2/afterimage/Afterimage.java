package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.afterimage;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.entities.baseform.mirage.MirageEntity;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModEffects;
import net.sonicrushxii.beyondthehorizon.modded.ModEntityTypes;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Objects;

public class Afterimage implements CustomPacketPayload {
    /** Three seconds, matching the invisibility window and the decoy's lifetime. */
    public static final int AFTERIMAGE_DURATION = 60;

    public static final CustomPacketPayload.Type<Afterimage> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "afterimage"));

    public static final StreamCodec<FriendlyByteBuf, Afterimage> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), Afterimage::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public Afterimage() {}
    public Afterimage(FriendlyByteBuf buffer) {}
    public void encode(FriendlyByteBuf buffer) {}

    /**
     * Drops the afterimage: the player becomes visible again and the decoy it left behind
     * is dismissed. Called when the three seconds run out and when another ability is used.
     */
    public static void endAfterimage(ServerPlayer player, BaseformProperties baseformProperties) {
        baseformProperties.afterimage = 0;

        if (player.hasEffect(MobEffects.INVISIBILITY))
            player.removeEffect(MobEffects.INVISIBILITY);

        for (MirageEntity decoy : player.level().getEntitiesOfClass(MirageEntity.class,
                new AABB(player.getX() - 32, player.getY() - 32, player.getZ() - 32,
                         player.getX() + 32, player.getY() + 32, player.getZ() + 32),
                d -> player.getUUID().equals(d.getOwnerUUID())))
            decoy.discard();
    }

    public static void handle(Afterimage msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (player == null) return;
            PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            if (baseformProperties.afterimage > 0) return;

            baseformProperties.afterimage = 1;

            //Leave a decoy behind: it stands where the ability was used, or keeps punching
            //whatever the player was attacking, and fades once the afterimage runs out.
            {
                MirageEntity decoy = new MirageEntity(ModEntityTypes.SONIC_BASEFORM_MIRAGE.get(), player.level());
                decoy.setPos(player.getX(), player.getY(), player.getZ());
                decoy.setYRot(player.getYRot());
                decoy.setDuration(AFTERIMAGE_DURATION);
                decoy.setOwner(player.getUUID());

                LivingEntity lastAttacked = player.getLastHurtMob();
                if (lastAttacked != null && lastAttacked.isAlive())
                    decoy.setTarget(lastAttacked.getUUID());

                player.level().addFreshEntity(decoy);
            }

            if (player.hasEffect(MobEffects.INVISIBILITY))
                Objects.requireNonNull(player.getEffect(MobEffects.INVISIBILITY))
                        .update(new MobEffectInstance(MobEffects.INVISIBILITY, 60, 2, false, false));
            else
                player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 60, 2, false, false));

            for (LivingEntity mob : player.level().getEntitiesOfClass(LivingEntity.class,
                    new AABB(player.getX()+8, player.getY()+8, player.getZ()+8,
                             player.getX()-8, player.getY()-8, player.getZ()-8),
                    e -> !(e instanceof Player))) {
                if (mob.hasEffect(ModEffects.MIRAGE_CONFUSE))
                    Objects.requireNonNull(mob.getEffect(ModEffects.MIRAGE_CONFUSE))
                            .update(new MobEffectInstance(ModEffects.MIRAGE_CONFUSE, 60, 2, false, false));
                else
                    mob.addEffect(new MobEffectInstance(ModEffects.MIRAGE_CONFUSE, 60, 2, false, false));
            }

            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    ModSounds.MIRAGE.get(), SoundSource.MASTER, 0.5f, 1.5f);

            PacketHandler.sendToALLPlayers(new SyncPlayerFormS2C(player.getId(), playerSonicForm));
        });
    }
}
