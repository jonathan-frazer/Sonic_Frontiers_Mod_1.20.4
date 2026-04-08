package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.loop_kick;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Collections;
import java.util.List;

public class LoopKick implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LoopKick> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "loop_kick"));

    public static final StreamCodec<FriendlyByteBuf, LoopKick> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), LoopKick::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public LoopKick() {

    }

    public LoopKick(FriendlyByteBuf buffer){

    }

    public void encode(FriendlyByteBuf buffer){

    }

    //Server-Side Scan
    public static void scanFoward(ServerPlayer player)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

        //Get Position
        Vec3 currentPos = player.getPosition(0).add(0.0, 1.0, 0.0);
        Vec3 lookAngle = player.getLookAngle();

        baseformProperties.meleeTarget = null;

        //Scan Forward for enemies
        for (int i = 0; i < 16; ++i) {
            //Increment Current Position Forward
            currentPos = currentPos.add(lookAngle);
            AABB boundingBox = new AABB(currentPos.x() + 4, currentPos.y() + 4, currentPos.z() + 4,
                    currentPos.x() - 4, currentPos.y() - 4, currentPos.z() - 4);

            List<LivingEntity> nearbyEntities = player.level().getEntitiesOfClass(
                    LivingEntity.class, boundingBox,
                    (enemy) -> !enemy.is(player) && enemy.isAlive());

            //If enemy is found then Target it
            if (!nearbyEntities.isEmpty()) {
                //Select Closest target
                baseformProperties.meleeTarget = Collections.min(nearbyEntities, (e1, e2) -> {
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

    public static void performLoopKick(ServerPlayer player)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

        //Set Motion
        player.setDeltaMovement(new Vec3(0,0,0));
        player.connection.send(new ClientboundSetEntityMotionPacket(player));

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
        baseformProperties.loopKick = 1;

        //Set Phase
        baseformProperties.atkRotPhase = player.getYRot();

        //PlaySound
        player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.LOOP_KICK.get(), SoundSource.MASTER, 0.75f, 1.0f);

        //Remove Gravity
        player.getAttribute(NeoForgeMod.ENTITY_GRAVITY).setBaseValue(0.0);


        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));
    }

    public static void handle(LoopKick msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null)
                    {
                        performLoopKick(player);
                    }
                });
    }
}
