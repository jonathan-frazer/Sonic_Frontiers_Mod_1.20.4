package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.spin_kick;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformClient;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModEntityTypes;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CycloneKick {

    private final UUID enemyID;

    public CycloneKick(UUID enemyID) {
        this.enemyID = enemyID;
    }

    public CycloneKick(FriendlyByteBuf buffer){
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
                BaseformClient.ClientOnlyData.cycloneReticle = Collections.min(nearbyEntities, (e1, e2) -> {
                    Vec3 e1Pos = new Vec3(e1.getX(), e1.getY(), e1.getZ());
                    Vec3 e2Pos = new Vec3(e2.getX(), e2.getY(), e2.getZ());

                    return (int) (e1Pos.distanceToSqr(player.getX(),player.getY(),player.getZ()) - e2Pos.distanceToSqr(player.getX(),player.getY(),player.getZ()));
                }).getUUID();
                break;
            }
        }
    }

    public static void performCycloneKick(ServerPlayer player, UUID enemyID)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            boolean instantCyclone = (enemyID == null);

            if(!instantCyclone)
            {
                //Check if target is real
                Entity target = player.serverLevel().getEntity(enemyID);
                if(target == null)  instantCyclone = true;
            }

            if(!instantCyclone)
            {
                //Set Data
                baseformProperties.cycloneKick = -60;
                baseformProperties.meleeTarget = enemyID;
            }
            else
            {
                //Set Data
                baseformProperties.cycloneKick = 1;
                Vec3 playerPos = new Vec3(player.getX(),player.getY(),player.getZ());
                Scheduler.scheduleTask(()->{
                    Utilities.summonEntity(ModEntityTypes.BASEFORM_CYCLONE_KICK_CLOUD.get(),
                        player.serverLevel(),
                        playerPos.add
                                (Utilities.calculateViewVector(0,player.getYRot()).scale(1.4)),
                        (aoeCloud) -> {
                            aoeCloud.setDuration(60);
                        });
                },5);
                baseformProperties.meleeTarget = new UUID(0L,0L);
            }

            baseformProperties.atkRotPhase = -player.getYRot()-135f;

            //Remove Gravity
            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.0);

            //Play Sound
            player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.DOUBLE_JUMP.get(), SoundSource.MASTER, 0.75f, 1.0f);

            PacketHandler.sendToALLPlayers(
                    new SyncPlayerFormS2C(
                            player.getId(),
                            playerSonicForm
                    ));
        });
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                    {
                        performCycloneKick(player,enemyID);
                    }
                });
        ctx.setPacketHandled(true);
    }
}

