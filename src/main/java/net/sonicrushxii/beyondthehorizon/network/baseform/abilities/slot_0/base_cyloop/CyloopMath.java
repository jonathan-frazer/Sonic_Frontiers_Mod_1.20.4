package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.base_cyloop;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;
import net.sonicrushxii.beyondthehorizon.modded.ModEffects;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleAuraPacketS2C;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;

public class CyloopMath
{
    private static final float CYLOOP_DAMAGE = 15.0f;

    enum Direction{
        RIGHT(1.0,0),LEFT(-1.0,0),DOWN(0,-1.0),UP(0,1.0);
        private final double x;
        private final double z;

        Direction(double x, double z)
        {
            this.x = x;
            this.z = z;
        }

        Vec3 stepInDirection(Vec3 currPos)
        {
            return currPos.add(this.x,0,this.z);
        }

    }

    public static double xzDistSqr(Vec3 p1, Vec3 p2)
    {
        return Math.pow(p1.x-p2.x,2)+Math.pow(p1.z-p2.z,2);
    }

    private static boolean cyloopisOnSide(Direction dir,Vec3 currPos,
                                          Object[] cyloopPath, byte start, byte end)
    {
        assert start<end;

        byte steps=100;
        while(steps-- > 0)
        {
            for(byte i=start;i<end;++i)
                if(xzDistSqr(currPos,(Vec3)cyloopPath[i]) < 2.0)
                    return true;

            currPos = dir.stepInDirection(currPos);
        }

        return false;
    }

    public static void cyloopEffect(ServerPlayer player, Object[] cyloopPath)
    {
        final byte SKIP_THRESHOLD = 10;
        boolean regened = false;

        //Remember to cast all the Objects to Vec3's
        for(byte i=0;i<cyloopPath.length-SKIP_THRESHOLD;++i)
            for(byte j = (byte) (i+SKIP_THRESHOLD); j<cyloopPath.length; ++j)
                if(xzDistSqr((Vec3) cyloopPath[i], (Vec3) cyloopPath[j]) < 1.0)
                {
                    //Cyloop Regeneration
                    if(!regened)
                    {
                        int absorptionAmt = (int) Math.ceil(player.getAbsorptionAmount());
                        int amplifier = -1 + (1+absorptionAmt/8)*2;
                        if(amplifier != -1 && amplifier < 10) {
                            if(player.hasEffect(MobEffects.ABSORPTION))     player.removeEffect(MobEffects.ABSORPTION);
                            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, -1, amplifier, false, false));
                        }
                        regened = true;
                    }

                    //Draw a Bounding box from point
                    Vec3 stocPoint = (Vec3) cyloopPath[j-2];
                    AABB targetBox = new AABB(
                            stocPoint.x+32.0,stocPoint.y+32.0,stocPoint.z+32.0,
                            stocPoint.x-32.0,stocPoint.y-32.0,stocPoint.z-32.0
                    );

                    //Find all viable targets
                    Level world = player.level();
                    byte finalJ = j;
                    byte finalI = i;
                    for(LivingEntity enemy : world.getEntitiesOfClass(LivingEntity.class,targetBox,
                            (entity)->{
                                if(entity.is(player))   return false;
                                Vec3 entityPos = new Vec3(entity.getX(),0.0,entity.getZ());
                                return  (
                                            cyloopisOnSide(Direction.RIGHT,
                                            entityPos,
                                            cyloopPath, finalI, finalJ)
                                        )
                                        &&
                                        (
                                            cyloopisOnSide(Direction.LEFT,
                                                    entityPos,
                                            cyloopPath, finalI, finalJ)
                                        )
                                        &&
                                        (
                                            cyloopisOnSide(Direction.UP,
                                                    entityPos,
                                            cyloopPath, finalI, finalJ)
                                        )
                                        &&
                                        (
                                            cyloopisOnSide(Direction.DOWN,
                                                    entityPos,
                                            cyloopPath, finalI, finalJ)
                                        );

                            }))
                    {
                        Vec3 enemyPos = new Vec3(enemy.getX(),enemy.getY(),enemy.getZ());

                        //Play Particle
                        PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                ParticleTypes.FLASH,
                                enemyPos.x(), enemyPos.y()+player.getEyeHeight()/2, enemyPos.z(),
                                0.0, 0.2f, 0.2f, 0.2f, 1, true)
                        );
                        PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                ParticleTypes.EXPLOSION,
                                enemyPos.x(), enemyPos.y()+player.getEyeHeight()/2, enemyPos.z(),
                                0.0, 0.2f, 0.2f, 0.2f, 1, true)
                        );

                        //Sound
                        world.playSound(null,player.getX(),player.getY(),player.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.MASTER, 1.0f, 2.0f);


                        //Double Cyloop - Launch Down
                        if(enemy.hasEffect(ModEffects.CYLOOPED.get()) && enemy.getEffect(ModEffects.CYLOOPED.get()).getDuration() > 0)
                        {
                            //Launch Down
                            enemy.setDeltaMovement(0.0,-1.1,0.0);
                            player.connection.send(new ClientboundSetEntityMotionPacket(enemy));

                            //Deal Damage
                            enemy.hurt(ModDamageTypes.getDamageSource(player.level(),ModDamageTypes.SONIC_CYLOOP.getResourceKey(),player),
                                    CYLOOP_DAMAGE*1.5F);

                            //Give the Cylooped Effect
                            enemy.getEffect(ModEffects.CYLOOPED.get()).update(new MobEffectInstance(ModEffects.CYLOOPED.get(), 20, 0, false, false));
                        }
                        //Single Cyloop
                        else
                        {
                            //Damage
                            enemy.hurt(ModDamageTypes.getDamageSource(player.level(),ModDamageTypes.SONIC_CYLOOP.getResourceKey(),player),
                                    CYLOOP_DAMAGE);

                            //Launch Up
                            enemy.setDeltaMovement(0.0,1.1,0.0);
                            player.connection.send(new ClientboundSetEntityMotionPacket(enemy));

                            //Lock in MidAir for 2 sec
                            Scheduler.scheduleTask(()->{
                                //Set Movement to Zero
                                enemy.setDeltaMovement(0.0,0.02,0.0);
                                player.connection.send(new ClientboundSetEntityMotionPacket(enemy));

                                //Give the Cylooped Effect
                                if(enemy.hasEffect(ModEffects.CYLOOPED.get()))
                                    enemy.getEffect(ModEffects.CYLOOPED.get()).update(new MobEffectInstance(ModEffects.CYLOOPED.get(), 40, 0, false, false));
                                else
                                    enemy.addEffect(new MobEffectInstance(ModEffects.CYLOOPED.get(), 40, 0, false, false));

                            },10);
                        }

                    }

                    //Skip the Rest of the Comparisons
                    i = (byte) (j+1);
                }

    }
}
