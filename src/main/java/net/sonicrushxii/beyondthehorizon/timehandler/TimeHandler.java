package net.sonicrushxii.beyondthehorizon.timehandler;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;

import java.util.HashSet;
import java.util.UUID;

public class TimeHandler {

    private static HashSet<UUID> slowedProjectiles = new HashSet<>();

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent.Post event) {
        // Get the server instance
        MinecraftServer server = event.getServer(); // Get the level (world) you want to execute the command in

        int dimIdx;

        for(TimeAffector timeAffector : TimeAffector.values())
        {
            //Set all dimensionalUsers to false initially
            server.getAllLevels().forEach(ignored -> timeAffector.dimensionalUsers.add(false));

            dimIdx= 0;
            //Iterate through every dimension
            for(ServerLevel world : server.getAllLevels())
            {
                //Set a Flag Variable for the current tick
                timeAffector.noPlayerPresent.set(true);

                //Skip Iteration if time is stopped
                if(world.tickRateManager().isFrozen())
                    continue;

                for(Entity entity : world.getAllEntities())
                {
                    //Player Interactions
                    if (entity instanceof ServerPlayer player) {
                        final int DimIdx = dimIdx; //Effectively Final Variable
                        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
                            try
                            {
                                //If Player has the given tag, then Continue
                                if (!timeAffector.isContainedBy(playerSonicForm.getFormProperties()))
                                    return;

                                //Slow Projectiles
                                for(Projectile projectile : world.getEntitiesOfClass(Projectile.class,
                                        new AABB(player.getX()+32,player.getY()+32,player.getZ()+32,
                                                player.getX()-32,player.getY()-32,player.getZ()-32),
                                        (projectile)->!slowedProjectiles.contains(projectile.getUUID())))
                                {
                                    //Remove Gravity, Lower Speed
                                    projectile.setNoGravity(true);
                                    projectile.setDeltaMovement(projectile.getDeltaMovement().scale(0.25));

                                    PacketHandler.sendToALLPlayers(new TimeProjSync(projectile.getId(),
                                            projectile.getDeltaMovement(),projectile.isNoGravity()));

                                    slowedProjectiles.add(projectile.getUUID());
                                }

                                //Slow down Time
                                if (!timeAffector.dimensionalUsers.get(DimIdx))
                                {
                                    TickRateManager tickRateManager = world.tickRateManager();
                                    tickRateManager.setTickRate(tickRateManager.tickrate() / timeAffector.getTimeFactor());
                                }
                                //Set a Value Indicating current dimension has a user of this Time Affector
                                timeAffector.dimensionalUsers.set(DimIdx, true);
                                //Flag variable
                                timeAffector.noPlayerPresent.set(false);

                            } catch(NoSuchFieldException | IllegalAccessException ignored) {}
                        });
                    }
                }

                //If no players of the given type are found
                if (timeAffector.noPlayerPresent.get())
                {
                    //Return time to Normal
                    if (timeAffector.dimensionalUsers.get(dimIdx))
                    {
                        TickRateManager tickRateManager = world.tickRateManager();
                        tickRateManager.setTickRate(tickRateManager.tickrate() * timeAffector.getTimeFactor());

                        //Remove Slow on the Projectiles
                        for(UUID projectileUUID : slowedProjectiles)
                        {
                            try {
                                Projectile projectile = (Projectile) world.getEntity(projectileUUID);
                                projectile.setDeltaMovement(projectile.getDeltaMovement().scale(4));
                                projectile.setNoGravity(false);
                            }catch(NullPointerException|ClassCastException ignored){}
                        }

                        //Cleanup the hashset
                        slowedProjectiles.removeIf((projectileUUID)->{
                            try{
                                Projectile projectile = (Projectile) world.getEntity(projectileUUID);
                                return projectile != null;
                            }catch (ClassCastException ignored)
                            {
                                return false;
                            }
                        });

                    }
                    //Set All users of this dimension to false
                    timeAffector.dimensionalUsers.set(dimIdx,false);
                }
                dimIdx++;
            }
        }
    }
}