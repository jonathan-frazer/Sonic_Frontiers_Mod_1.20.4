package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.homing_shot;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class HomingShot implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<HomingShot> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "homing_shot"));

    public static final StreamCodec<FriendlyByteBuf, HomingShot> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), HomingShot::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public HomingShot() {

    }

    public HomingShot(FriendlyByteBuf buffer){

    }

    public void encode(FriendlyByteBuf buffer){

    }

    //Server-Side Scan
    public static void scanFoward(ServerPlayer player)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        if (!(playerSonicForm.getFormProperties() instanceof BaseformProperties baseformProperties)) return;

        //Get Position
        Vec3 currentPos = player.getPosition(0).add(0.0, 1.0, 0.0);
        Vec3 lookAngle = player.getLookAngle().scale(2.0);

        baseformProperties.rangedTarget = new UUID(0L,0L);

        //Scan Forward for enemies
        for (int i = 0; i < 12; ++i)
        {
            //Increment Current Position Forward
            currentPos = currentPos.add(lookAngle);
            AABB boundingBox = new AABB(currentPos.x() - 5, currentPos.y() - 5, currentPos.z() - 5,
                                        currentPos.x() + 5, currentPos.y() + 5, currentPos.z() + 5);

            List<LivingEntity> nearbyEntities = player.level().getEntitiesOfClass(
                    LivingEntity.class, boundingBox,
                    enemy -> {
                        if (enemy.is(player) || !enemy.isAlive()) return false;
                        return player.level().clip(new ClipContext(
                            player.getEyePosition(), enemy.getEyePosition(),
                            ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)
                        ).getType() != HitResult.Type.BLOCK;
                    });

            //If enemy is found then Target it
            if (!nearbyEntities.isEmpty()) {
                //Select Closest target
                baseformProperties.rangedTarget = Collections.min(
                        nearbyEntities, (e1, e2) -> {
                    Vec3 e1Pos = new Vec3(e1.getX(), e1.getY(), e1.getZ());
                    Vec3 e2Pos = new Vec3(e2.getX(), e2.getY(), e2.getZ());

                    return (int) (e1Pos.distanceToSqr(player.getX(),player.getY(),player.getZ()) - e2Pos.distanceToSqr(player.getX(),player.getY(),player.getZ()));
                }).getUUID();
                break;
            }
        }

        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));
    }

    public static void performHomingShot(ServerPlayer player)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        if (!(playerSonicForm.getFormProperties() instanceof BaseformProperties baseformProperties)) return;

        //Set Motion
        player.setDeltaMovement(new Vec3(0,0,0));
        player.connection.send(new ClientboundSetEntityMotionPacket(player));

        //Teleport up by one if on Ground
        if(player.onGround()) {
            player.teleportTo(player.serverLevel(),
                    player.getX(),
                    player.getY()+1,
                    player.getZ(),
                    Collections.emptySet(),
                    player.getYRot(), player.getXRot());
            player.connection.send(new ClientboundTeleportEntityPacket(player));
        }
        //Modify Data
        baseformProperties.homingShot = 1;

        //Set Phase
        baseformProperties.atkRotPhase = player.getYRot();

        //Remove Gravity
        var gravAttr = player.getAttribute(Attributes.GRAVITY);
        if (gravAttr != null) gravAttr.setBaseValue(0.0);

        //Play Sound
        player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.HOMING_SHOT.get(), SoundSource.MASTER, 0.75f, 1.0f);

        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));
    }

    public static void handle(HomingShot msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null)
                    {
                        performHomingShot(player);
                    }
                });
    }
}
