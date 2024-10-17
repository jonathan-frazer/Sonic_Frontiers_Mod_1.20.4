package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.event_handler.DamageHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleDirPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import net.sonicrushxii.beyondthehorizon.modded.ModEffects;
import net.sonicrushxii.beyondthehorizon.scheduler.ScheduledTask;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.UUID;

public class BaseformHandler {
    private static final HashMap<UUID,ScheduledTask> hitSchedule = new HashMap<>();

    public static void takeDamage(LivingAttackEvent event, BaseformProperties baseformProperties)
    {
        try {
            ServerPlayer receiver = (ServerPlayer) event.getEntity();
            Entity damageGiver = event.getSource().getEntity();

            // Makes you only invulnerable to Direct mob attacks when using this ability. Like weakness but better
            if (baseformProperties.dodgeInvul)
                event.setCanceled(true);
            if ((baseformProperties.selectiveInvul() || receiver.hasEffect(ModEffects.SPEED_BLITZING.get())) &&
                    !(damageGiver instanceof Player) && !event.getSource().isIndirect())
                event.setCanceled(true);

        }catch(NullPointerException ignored){}
    }

    public static void dealDamage(LivingAttackEvent event, BaseformProperties baseformProperties)
    {
        //Living Entity, Rapid Damage (Hurt Time 0 only)
        try {
            ServerPlayer damageGiver = (ServerPlayer) event.getSource().getEntity();
            LivingEntity damageTaker = event.getEntity();

            if(damageTaker.hurtTime != 0)
                throw new NullPointerException("Mob is already being hurt");

            //Melee Attack
            {
                final int COMBO_TIME = 40;

                if (event.getSource().is(DamageTypes.PLAYER_ATTACK) && !baseformProperties.speedBlitz) {

                    //Perform Knockup/Knockdown
                    assert damageGiver != null;

                    if (!damageGiver.onGround())
                    {

                        damageTaker.setDeltaMovement(Vec3.ZERO);
                        damageGiver.connection.send(new ClientboundSetEntityMotionPacket(damageTaker));
                    }

                    if (baseformProperties.hitCount == 3)
                    {
                        Level world = damageGiver.level();
                        world.playSound(null, damageGiver.getX(), damageGiver.getY(), damageGiver.getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.MASTER, 1.0f, 1.0f);

                        PacketHandler.sendToALLPlayers(new ParticleDirPacketS2C(
                                new DustParticleOptions(new Vector3f(0.0f, 0.0f, 1.0f), 1f),
                                damageTaker.getX(), damageTaker.getY()+0.45, damageTaker.getZ(),
                                0.0,2.0,0.0 ,0.65f, 0.65f, 0.65f, 30,
                                true)
                            );
                    }
                    if (baseformProperties.hitCount == 4)
                    {
                        if (damageGiver.isShiftKeyDown()||damageGiver.getY()+damageGiver.getEyeHeight() < damageTaker.getY()+damageTaker.getEyeHeight()) {
                            damageGiver.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 40, 10, false, false));
                            damageTaker.addEffect(new MobEffectInstance(ModEffects.COMBO_EFFECT.get(), 40, 0, false, false));
                            damageTaker.moveTo(damageTaker.getX(), damageTaker.getY() + 5.0, damageTaker.getZ());
                        } else {
                            //Sonic Eagle
                            PacketHandler.sendToALLPlayers(new ParticleDirPacketS2C(
                                    ParticleTypes.FLAME,
                                    damageTaker.getX(), damageTaker.getY()+damageTaker.getEyeHeight(), damageTaker.getZ(),
                                    0.0,-0.1,0.0 ,0.65f, 0.65f, 0.65f, 30,
                                    true)
                            );
                            //Sound Effect
                            damageGiver.level().playSound(null,damageGiver.getX(),damageGiver.getY(),damageGiver.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.MASTER, 1.0f, 1.0f);

                            damageGiver.removeEffect(MobEffects.SLOW_FALLING);
                            damageTaker.removeEffect(ModEffects.COMBO_EFFECT.get());
                            damageTaker.hurt(ModDamageTypes.getDamageSource(damageGiver.level(),ModDamageTypes.SONIC_MELEE.getResourceKey(),damageGiver), 6.0f);
                            damageTaker.addDeltaMovement(new Vec3(0.0, -0.85, 0.0));
                            damageGiver.connection.send(new ClientboundSetEntityMotionPacket(damageTaker));
                        }
                    }

                    //Increase Count
                    baseformProperties.hitCount = (byte) ((baseformProperties.hitCount + 1) % 6);

                    //Cancel the Current Combo schedule
                    ScheduledTask currentSchedule = hitSchedule.get(damageGiver.getUUID());
                    if (currentSchedule != null && !currentSchedule.isCancelled()) currentSchedule.cancel();

                    //Add another Schedule to reset counter After 2 seconds
                    hitSchedule.put(damageGiver.getUUID(), Scheduler.scheduleTask(() -> {
                                baseformProperties.hitCount = 0;
                                damageGiver.removeEffect(MobEffects.SLOW_FALLING);
                                System.out.println("Reset Combo");
                            }, COMBO_TIME)
                    );
                } else if (DamageHandler.isDamageSourceModded(event.getSource())) {
                    //Cancel Combo, if you do any other modded Attack
                    ScheduledTask currentSchedule = hitSchedule.get(damageGiver.getUUID());
                    if (currentSchedule != null && !currentSchedule.isCancelled()) {
                        currentSchedule.cancel();
                        damageGiver.removeEffect(MobEffects.SLOW_FALLING);
                        baseformProperties.hitCount = 0;
                        System.out.println("Interrupt Combo");
                    }
                }
            }
        }catch(NullPointerException|ClassCastException ignored){}

        //Living Entity, Instantaneous Damage(HurtTime any)
        try {
            ServerPlayer damageGiver = (ServerPlayer) event.getSource().getEntity();
            LivingEntity damageTaker = event.getEntity();

            //Reduce Vertical Movement to 0 if In Player attack
            if(event.getSource().is(DamageTypes.PLAYER_ATTACK))
            {
                Vec3 currentPlayerMovement = damageGiver.getDeltaMovement();
                damageGiver.setDeltaMovement(currentPlayerMovement.x(), 0.0, currentPlayerMovement.z());
                damageGiver.connection.send(new ClientboundSetEntityMotionPacket(damageGiver));
            }

            //Smash Hit
            if(baseformProperties.smashHit > 0 && event.getSource().is(DamageTypes.PLAYER_ATTACK))
            {
                //Knockback
                damageTaker.setDeltaMovement(damageGiver.getLookAngle().scale(baseformProperties.smashHit/20.0f));
                //Damage Enemy
                damageTaker.hurt(ModDamageTypes.getDamageSource(damageGiver.level(),ModDamageTypes.SONIC_MELEE.getResourceKey(),damageGiver),
                        baseformProperties.smashHit*0.5f);

                //Sound
                damageGiver.level().playSound(null,damageGiver.getX(),damageGiver.getY(),damageGiver.getZ(), ModSounds.SMASH_HIT.get(), SoundSource.MASTER, 1.0f, 1.0f);;

                damageGiver.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
                    //Get Data From the Player
                    BaseformProperties updatedBaseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();
                    updatedBaseformProperties.smashHit = 0;

                    //Sync Data
                    PacketHandler.sendToPlayer(damageGiver,
                            new SyncPlayerFormS2C(
                                    playerSonicForm.getCurrentForm(),
                                    baseformProperties
                            ));
                });
            }//Speed Blitz
            else if(baseformProperties.speedBlitz && event.getSource().is(DamageTypes.PLAYER_ATTACK))
            {
                //Recover Speed Blitz Dashes
                if(!damageGiver.hasEffect(ModEffects.SPEED_BLITZING.get()) && damageGiver.onGround())
                    baseformProperties.speedBlitzDashes = 4;

                //Current Combo Duration
                MobEffectInstance currComboEffect = damageTaker.getEffect(ModEffects.SPEED_BLITZED.get());
                if(currComboEffect == null)
                    damageTaker.addEffect(new MobEffectInstance(ModEffects.SPEED_BLITZED.get(), 20, 0, false, false));
                else
                    currComboEffect.update(new MobEffectInstance(ModEffects.SPEED_BLITZED.get(), 20, 0, false, false));

                Scheduler.scheduleTask(()->{
                    damageTaker.setDeltaMovement(Utilities.calculateViewVector(damageGiver.getXRot(),damageGiver.getYRot()).scale(0.85));
                    damageGiver.connection.send(new ClientboundSetEntityMotionPacket(damageTaker));
                },3);
            }


        }catch(NullPointerException|ClassCastException ignored){}
    }
}
