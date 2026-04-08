package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.spin_kick;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformClient;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SpinSlash implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SpinSlash> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "spin_slash"));

    public static final StreamCodec<FriendlyByteBuf, SpinSlash> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), SpinSlash::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    private final UUID enemyID;

    public SpinSlash(UUID enemyID) {
        this.enemyID = enemyID;
    }

    public SpinSlash(FriendlyByteBuf buffer){
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

    //Client-Side Method
    public static void scanFoward(Player player)
    {
        Vec3 currentPos = player.getPosition(0).add(0.0, 1.0, 0.0);
        Vec3 lookAngle = player.getLookAngle();

        //Scan Forward for enemies
        for (int i = 0; i < 10; ++i) {
            //Increment Current Position Forward
            currentPos = currentPos.add(lookAngle);
            AABB boundingBox = new AABB(currentPos.x() + 3, currentPos.y() + 3, currentPos.z() + 3,
                    currentPos.x() - 3, currentPos.y() - 3, currentPos.z() - 3);

            List<LivingEntity> nearbyEntities = player.level().getEntitiesOfClass(
                    LivingEntity.class, boundingBox,
                    (enemy) -> !enemy.is(player) && enemy.isAlive());

            //If enemy is found then Target it
            if (!nearbyEntities.isEmpty()) {
                //Select Closest target
                BaseformClient.ClientOnlyData.spinSlashReticle = Collections.min(nearbyEntities, (e1, e2) -> {
                    Vec3 e1Pos = new Vec3(e1.getX(), e1.getY(), e1.getZ());
                    Vec3 e2Pos = new Vec3(e2.getX(), e2.getY(), e2.getZ());

                    return (int) (e1Pos.distanceToSqr(player.getX(),player.getY(),player.getZ()) - e2Pos.distanceToSqr(player.getX(),player.getY(),player.getZ()));
                }).getUUID();
                break;
            }
        }
    }

    public static void performSpinSlash(ServerPlayer player, UUID enemyID)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

        //Check if target is real
        Entity target = player.serverLevel().getEntity(enemyID);
        if(target == null)  return;

        //Set Data
        if(player.hasEffect(MobEffects.GLOWING)) player.getEffect(MobEffects.GLOWING).update(new MobEffectInstance(MobEffects.GLOWING, 120, 1, false, false));
        else                                     player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 120, 1, false, false));
        baseformProperties.spinSlash = -60;
        baseformProperties.meleeTarget = enemyID;
        baseformProperties.atkRotPhase = -player.getYRot()-135f;

        //Reset Momentum
        player.setDeltaMovement(0,0,0);
        player.connection.send(new ClientboundSetEntityMotionPacket(player));

        //Play Sound
        player.level().playSound(null,player.getX(),player.getY(),player.getZ(), SoundEvents.BEACON_ACTIVATE, SoundSource.MASTER, 2.0f, 2.0f);


        //Remove Gravity
        player.getAttribute(NeoForgeMod.ENTITY_GRAVITY).setBaseValue(0.0);

        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));
    }

    public static void handle(SpinSlash msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null && msg.enemyID != null)
                    {
                        performSpinSlash(player,msg.enemyID);
                    }
                });
    }
}
