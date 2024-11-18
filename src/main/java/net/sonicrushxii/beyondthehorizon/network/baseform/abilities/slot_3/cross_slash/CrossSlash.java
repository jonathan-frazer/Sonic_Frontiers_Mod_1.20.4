package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.cross_slash;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleRaycastPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CrossSlash
{

    public CrossSlash() {

    }

    public CrossSlash(FriendlyByteBuf buffer){

    }

    public void encode(FriendlyByteBuf buffer){
    }

    //Server-Side Scan
    public static void scanFoward(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Get Position
            Vec3 currentPos = player.getPosition(0).add(0.0, 1.0, 0.0);
            Vec3 lookAngle = player.getLookAngle();

            baseformProperties.rangedTarget = null;

            //Scan Forward for enemies
            for (int i = 0; i < 12; ++i) {
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
                    baseformProperties.rangedTarget = Collections.min(nearbyEntities, (e1, e2) -> {
                        Vec3 e1Pos = new Vec3(e1.getX(), e1.getY(), e1.getZ());
                        Vec3 e2Pos = new Vec3(e2.getX(), e2.getY(), e2.getZ());

                        return (int) (e1Pos.distanceToSqr(player.getX(),player.getY(),player.getZ()) - e2Pos.distanceToSqr(player.getX(),player.getY(),player.getZ()));
                    }).getUUID();
                    break;
                }
            }

            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            baseformProperties
                    ));
        });
    }

    public static void tpToTarget(ServerPlayer player, UUID enemyID)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Start Cross Slash
            if(enemyID != null) {
                //Check if target is real
                Entity target = player.serverLevel().getEntity(enemyID);
                if (target == null) return;

                //Changed Data
                baseformProperties.rangedTarget = enemyID;

                //Teleport Player to Position
                Vec3 playerPos = new Vec3(player.getX(), player.getY(), player.getZ());
                Vec3 enemyPos = new Vec3(target.getX(), target.getY(), target.getZ());
                Vec3 subtraction = playerPos.subtract(enemyPos);
                Vec3 tpDir = (new Vec3(subtraction.x(), 0, subtraction.z())).normalize().scale(7.0);

                Vec3 newDir = tpDir.reverse().normalize();
                Vec3 newPlayerPosition = enemyPos.add(tpDir.add(0, 1, 0));

                float[] yawPitch = Utilities.getYawPitchFromVec(newDir);

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
                                new DustParticleOptions(new Vector3f(0f, 0f, 1f), 2.5f),
                                playerPos.add(0, 1.25, 0), newPlayerPosition.add(0, 1.25, 0)
                        )
                );
            }
        });
    }


    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null){
                        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
                            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                            //Start Cross Slash
                            scanFoward(player);

                            //Changed Data
                            baseformProperties.crossSlash = 1;
                            //If Ranged Target is there Tp to the ranged target
                            if(baseformProperties.rangedTarget != null)
                                tpToTarget(player,baseformProperties.rangedTarget);

                            //Remove Gravity
                            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.0);

                            //Play Sound
                            player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.HOMING_ATTACK.get(), SoundSource.MASTER, 1.0f, 1.0f);


                            PacketHandler.sendToPlayer(player,
                                    new SyncPlayerFormS2C(
                                            playerSonicForm.getCurrentForm(),
                                            baseformProperties
                                    ));
                        });
                    }
                });
        ctx.setPacketHandled(true);
    }
}
