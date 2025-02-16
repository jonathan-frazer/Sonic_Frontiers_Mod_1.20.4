package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.wild_rush;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.ModUtils;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformClient;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleRaycastPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class WildRush
{
    private final UUID enemyID;

    public WildRush(UUID enemyID) {
        this.enemyID = enemyID;
    }

    public WildRush(FriendlyByteBuf buffer){
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
        for (int i = 0; i < 12; ++i) {
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
                BaseformClient.ClientOnlyData.wildRushReticle = Collections.min(nearbyEntities, (e1, e2) -> {
                    Vec3 e1Pos = new Vec3(e1.getX(), e1.getY(), e1.getZ());
                    Vec3 e2Pos = new Vec3(e2.getX(), e2.getY(), e2.getZ());

                    return (int) (e1Pos.distanceToSqr(player.getX(),player.getY(),player.getZ()) - e2Pos.distanceToSqr(player.getX(),player.getY(),player.getZ()));
                }).getUUID();
                break;
            }
        }
    }


    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null){
                        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
                            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                            //Start Wild Rush
                            if(enemyID != null)
                            {
                                //Check if target is real
                                Entity target = player.serverLevel().getEntity(enemyID);
                                if(target == null) return;

                                //Changed Data
                                baseformProperties.wildRushTime = 1;
                                baseformProperties.wildRushPtr = 0;
                                baseformProperties.meleeTarget = enemyID;

                                //Teleport Player to Position
                                Vec3 playerPos = new Vec3(player.getX(),player.getY(),player.getZ());
                                Vec3 enemyPos = new Vec3(target.getX(),target.getY(),target.getZ());
                                Vec3 subtraction = playerPos.subtract(enemyPos);
                                Vec3 tpDir = (new Vec3(subtraction.x(),0,subtraction.z())).normalize().scale(16.0);

                                Vec3 newDir = tpDir.reverse().normalize();
                                Vec3 newPlayerPosition = enemyPos.add(tpDir.add(0,2,0));

                                float[] yawPitch = ModUtils.getYawPitchFromVec(newDir);

                                player.teleportTo(player.serverLevel(),
                                        newPlayerPosition.x(),
                                        newPlayerPosition.y(),
                                        newPlayerPosition.z(),
                                        Collections.emptySet(),
                                        yawPitch[0], yawPitch[1]);
                                player.connection.send(new ClientboundTeleportEntityPacket(player));

                                //Draw Line
                                PacketHandler.sendToALLPlayers(
                                        new ParticleRaycastPacketS2C(
                                                new DustParticleOptions(new Vector3f(0f,0f,1f),2.5f),
                                                playerPos.add(0,1.25,0),newPlayerPosition.add(0,1.25,0)
                                        )
                                );

                                //Store all the new Positions
                                //                                                      Z                           Y                                        X
                                Vec3 pos0 = newPlayerPosition.add(newDir.scale(4).add(0,1.5,0).add(newDir.scale(2.9).cross(new Vec3(0, 1, 0))));
                                Vec3 pos1 = newPlayerPosition.add(newDir.scale(8).add(0,0.75,0).add(newDir.scale(2.9).cross(new Vec3(0, -1, 0))));
                                Vec3 pos2 = newPlayerPosition.add(newDir.scale(12).add(0,2.0,0).add(newDir.scale(0.7).cross(new Vec3(0, 1, 0))));
                                Vec3 pos3 = newPlayerPosition.add(newDir.scale(16).add(0,0,0).add(newDir.scale(1.6).cross(new Vec3(0, 1, 0))));
                                Vec3 pos4 = newPlayerPosition.add(newDir.scale(20).add(0,1,0).add(newDir.scale(2.9).cross(new Vec3(0, -1, 0))));
                                baseformProperties.wildRushPX[0] = (int)pos0.x();   baseformProperties.wildRushPY[0] = (int)pos0.y();   baseformProperties.wildRushPZ[0] = (int)pos0.z();
                                baseformProperties.wildRushPX[1] = (int)pos1.x();   baseformProperties.wildRushPY[1] = (int)pos1.y();   baseformProperties.wildRushPZ[1] = (int)pos1.z();
                                baseformProperties.wildRushPX[2] = (int)pos2.x();   baseformProperties.wildRushPY[2] = (int)pos2.y();   baseformProperties.wildRushPZ[2] = (int)pos2.z();
                                baseformProperties.wildRushPX[3] = (int)pos3.x();   baseformProperties.wildRushPY[3] = (int)pos3.y();   baseformProperties.wildRushPZ[3] = (int)pos3.z();
                                baseformProperties.wildRushPX[4] = (int)pos4.x();   baseformProperties.wildRushPY[4] = (int)pos4.y();   baseformProperties.wildRushPZ[4] = (int)pos4.z();

                                //Remove Gravity
                                Objects.requireNonNull(player.getAttribute(ForgeMod.ENTITY_GRAVITY.get())).setBaseValue(0.0);

                                //Play Sound
                                player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.HOMING_ATTACK.get(), SoundSource.MASTER, 1.0f, 1.0f);
                            }

                            PacketHandler.sendToALLPlayers(
                                    new SyncPlayerFormS2C(
                                            player.getId(),
                                            playerSonicForm
                                    ));
                        });
                    }
                });
        ctx.setPacketHandled(true);
    }
}
