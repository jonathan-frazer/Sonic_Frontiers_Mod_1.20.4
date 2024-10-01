package net.sonicrushxii.beyondthehorizon.network.baseform.passives.danger_sense;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DangerSenseEmit {

    public static HashMap<String,ScheduledFuture<?>> scheduledFutures = new HashMap<>();

    public static void stopDangerSenseSound(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm->{
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();
            baseformProperties.dangerSensePlaying = false;

            PacketHandler.sendToPlayer(player, new PlayerStopSoundPacketS2C(
                            ModSounds.DANGER_SENSE.get().getLocation()
                    )
            );

            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            baseformProperties
                    ));
        });
    }

    public static boolean nearbyAngryMobs(ServerPlayer player, Level world)
    {
        List<Entity> nearbyEntities = world.getEntities(player , player.getBoundingBox().inflate(16.0), entity -> entity instanceof Mob);

        for (Entity entity : nearbyEntities) {
            if (entity instanceof Mob mob) {
                UUID aggro = null;

                if (mob.getTarget() != null) aggro = mob.getTarget().getUUID();
                if (aggro != null && aggro.equals(player.getUUID())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void performDangerSenseEmit(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            Level world = player.level();
            List<Entity> nearbyEntities = world.getEntities(player, player.getBoundingBox().inflate(10.0), entity -> entity instanceof Mob);

            for (Entity entity : nearbyEntities) {
                if (entity instanceof Mob mob) {
                    UUID aggro = null;
                    if (mob.getTarget() != null) aggro = mob.getTarget().getUUID();
                    if (aggro != null && aggro.equals(player.getUUID())) {
                        if (!baseformProperties.dangerSensePlaying)
                        {
                            //Play Sound
                            PacketHandler.sendToPlayer(player, new PlayerPlaySoundPacketS2C(
                                    ModSounds.DANGER_SENSE.get().getLocation())
                            );

                            //Change the tag
                            baseformProperties.dangerSensePlaying = true;
                            PacketHandler.sendToPlayer(player,
                                    new SyncPlayerFormS2C(
                                            playerSonicForm.getCurrentForm(),
                                            baseformProperties
                                    ));

                            //Schedule Sound Stopping
                            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                            scheduledFutures.put(player.getStringUUID(), executor.schedule(
                                    () -> stopDangerSenseSound(player),
                                    5,
                                    TimeUnit.SECONDS
                            ));
                        }

                        // Define the vector and position
                        Vec3 vector = player.getLookAngle();
                        Vec3 position = mob.position().subtract(player.position());

                        vector.subtract(0, vector.y(), 0);
                        position.subtract(0, position.y(), 0);

                        // Calculate the vector to the position
                        Vec3 vectorToPosition = position.subtract(vector);
                        Vec3 crossProduct = vectorToPosition.cross(position);

                        Vec3 playerPos = (crossProduct.y() > 0) //If Mob is on the Right
                                ? player.getLookAngle() //Gets where player is looking
                                .cross(new Vec3(0, 1, 0)).scale(0.75) //It shifts it to the right
                                .add(player.position().add(player.getLookAngle().scale(0.5))) //Adds Players current position, offset ^ ^ ^0.5
                                .add(0, 1.5, 0) //Offsets that by Up By a Bit
                                : player.getLookAngle() //Gets where player is looking
                                .cross(new Vec3(0, 1, 0)).scale(-0.75) //It shifts it to the right
                                .add(player.position().add(player.getLookAngle().scale(0.5))) //Adds Players current position, offset ^ ^ ^0.5
                                .add(0, 1.5, 0); //Offsets that by Up By a Bit

                        PacketHandler.sendToPlayer(player, new ParticleRaycastPacketS2C(
                                ParticleTypes.ELECTRIC_SPARK,
                                playerPos,
                                mob.getPosition(0).add(0, 1.75, 0)
                        ));
                    }
                }
            }

            if (!nearbyAngryMobs(player, world) && baseformProperties.dangerSensePlaying) {
                stopDangerSenseSound(player);
                ScheduledFuture<?> scheduledFuture = scheduledFutures.getOrDefault(player.getStringUUID(), null);
                if (scheduledFuture != null)
                    scheduledFuture.cancel(false);
            }
        });
    }
}