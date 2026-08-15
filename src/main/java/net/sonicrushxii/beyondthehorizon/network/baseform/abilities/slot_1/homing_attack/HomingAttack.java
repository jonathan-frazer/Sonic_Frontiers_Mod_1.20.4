package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.homing_attack;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformClient;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.List;
import java.util.UUID;

public class HomingAttack implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<HomingAttack> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "homing_attack"));

    public static final StreamCodec<FriendlyByteBuf, HomingAttack> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), HomingAttack::new);

    private final UUID enemyID;

    public HomingAttack(UUID enemyID) {
        this.enemyID = enemyID;
    }

    public HomingAttack(FriendlyByteBuf buffer){
        UUID enemyID1;
        enemyID1 = buffer.readUUID();
        if(enemyID1.equals(new UUID(0L,0L)))
            enemyID1 = null;
        this.enemyID = enemyID1;
    }

    public void encode(FriendlyByteBuf buffer){
        if(enemyID==null) buffer.writeUUID(new UUID(0L,0L));
        else buffer.writeUUID(enemyID);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    //Client-Side Method
    public static void scanFoward(Player player)
    {
        final double RANGE = 12.0;
        // cos(60°) — targets must be within 60° of the look direction
        final double COS_THRESHOLD = 0.5;

        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle(); // already a unit vector

        List<LivingEntity> candidates = player.level().getEntitiesOfClass(
            LivingEntity.class,
            new AABB(eye.x - RANGE, eye.y - RANGE, eye.z - RANGE,
                     eye.x + RANGE, eye.y + RANGE, eye.z + RANGE),
            e -> !e.is(player) && e.isAlive()
        );

        LivingEntity best = null;
        double bestDist = Double.MAX_VALUE;

        for (LivingEntity candidate : candidates) {
            Vec3 toTarget = candidate.getEyePosition().subtract(eye);
            double distSq = toTarget.lengthSqr();
            if (distSq > RANGE * RANGE || distSq < 1e-8) continue;

            double dist = Math.sqrt(distSq);

            // Cone check: dot product of normalized direction with look vector
            if (toTarget.scale(1.0 / dist).dot(look) < COS_THRESHOLD) continue;

            // Line-of-sight check: skip if a solid block is between player and target
            if (player.level().clip(new ClipContext(
                    eye, candidate.getEyePosition(),
                    ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)
            ).getType() == HitResult.Type.BLOCK) continue;

            if (dist < bestDist) {
                bestDist = dist;
                best = candidate;
            }
        }

        if (best != null)
            BaseformClient.ClientOnlyData.homingAttackReticle = best.getUUID();
    }


    public static void handle(HomingAttack msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null){
                        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                        if (!(playerSonicForm.getFormProperties() instanceof BaseformProperties baseformProperties)) return;

                        //Start Homing Attack
                        if(msg.enemyID != null)
                        {
                            //Homing Attack Data
                            baseformProperties.airBoosts = 3;
                            baseformProperties.homingAttackAirTime = 1;
                            baseformProperties.homingTarget = msg.enemyID;

                            //Remove Gravity
                            var gravAttr = player.getAttribute(Attributes.GRAVITY);
                            if (gravAttr != null) gravAttr.setBaseValue(0.0);

                            //Play Sound
                            player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.HOMING_ATTACK.get(), SoundSource.MASTER, 1.0f, 1.0f);
                        }

                        PacketHandler.sendToALLPlayers(
                                new SyncPlayerFormS2C(
                                        player.getId(),
                                        playerSonicForm
                                ));
                    }
                });
    }
}
