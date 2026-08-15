package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import net.minecraft.core.particles.DustParticleOptions;
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
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.sonicrushxii.beyondthehorizon.ModUtils;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.event_handler.DamageHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;
import net.sonicrushxii.beyondthehorizon.modded.ModEffects;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleDirPacketS2C;
import net.sonicrushxii.beyondthehorizon.scheduler.ScheduledTask;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public class BaseformHandler {
    private static final HashMap<UUID,ScheduledTask> hitSchedule = new HashMap<>();

    public static void takeDamage(LivingIncomingDamageEvent event, BaseformProperties baseformProperties)
    {
        try {
            ServerPlayer receiver = (ServerPlayer) event.getEntity();
            Entity damageGiver = event.getSource().getEntity();

            //End Mirage Timer if Damage is taken
            {
                PlayerSonicForm playerSonicForm = receiver.getData(ModAttachments.PLAYER_SONIC_FORM);
                if (baseformProperties.mirageTimer > 1)
                    baseformProperties.mirageTimer = 141;
            }

            boolean isFireworkDmg = "fireworks".equals(event.getSource().getMsgId());

            //Prevent Fireworks from damaging you when Turning into Power Boost
            if(baseformProperties.powerBoost && isFireworkDmg)
                event.setCanceled(true);

            //Prevent Fireworks from damaging you when using WildRush
            if(baseformProperties.wildRushTime != 0 && isFireworkDmg)
                event.setCanceled(true);

            //Spin Slash
            if(baseformProperties.spinSlash != 0 && !event.getSource().is(DamageTypes.MAGIC) && !event.getSource().is(DamageTypes.EXPLOSION))
                event.setCanceled(true);

            // Makes you only invulnerable to Direct mob attacks when using this ability. Like weakness but better
            if (baseformProperties.dodgeInvul)
                event.setCanceled(true);
            if ((baseformProperties.selectiveInvul() || receiver.hasEffect(ModEffects.SPEED_BLITZING)) &&
                    !(damageGiver instanceof Player) && (event.getSource().getDirectEntity() == event.getSource().getEntity()))
                event.setCanceled(true);

            if (baseformProperties.afterimageCounter > 0 && damageGiver != null && !event.isCanceled()) {
                Vec3 attackerLook = damageGiver.getLookAngle();
                double behindX = damageGiver.getX() - attackerLook.x * 3;
                double behindZ = damageGiver.getZ() - attackerLook.z * 3;
                float facingYaw = (float)(Math.atan2(-attackerLook.x, attackerLook.z) * 180.0 / Math.PI);
                receiver.teleportTo(receiver.serverLevel(), behindX, damageGiver.getY(), behindZ,
                        Collections.emptySet(), facingYaw, 0);
                baseformProperties.afterimageCounter = 0;
                baseformProperties.afterimage = 0;
                PlayerSonicForm playerSonicForm = receiver.getData(ModAttachments.PLAYER_SONIC_FORM);
                receiver.level().playSound(null, receiver.getX(), receiver.getY(), receiver.getZ(),
                        SoundEvents.ENDERMAN_TELEPORT, SoundSource.MASTER, 1.0f, 1.0f);
                PacketHandler.sendToALLPlayers(new SyncPlayerFormS2C(receiver.getId(), playerSonicForm));
                event.setCanceled(true);
            }

            // Parry success removed — block-only after 0.5s hold (isBlocking handles damage reduction below)
            // Counter mechanic moved to Afterimage (C key in Melee slot)

            if(baseformProperties.isBlocking && !event.isCanceled())
            {
                event.setAmount(event.getAmount() * 0.5f);
                receiver.level().playSound(null, receiver.getX(), receiver.getY(), receiver.getZ(),
                        SoundEvents.SHIELD_BLOCK, SoundSource.MASTER, 1.0f, 1.0f);
            }

            //Grand Slam
            if(baseformProperties.grandSlamTime > 0)
                event.setCanceled(true);

            if(baseformProperties.ultimateUse > 1)
                event.setCanceled(true);

        }catch(NullPointerException ignored){}
    }

    public static void dealDamage(LivingIncomingDamageEvent event, BaseformProperties baseformProperties)
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

                    if (!damageGiver.onGround() && !damageTaker.onGround())
                    {
                        damageTaker.setDeltaMovement(Vec3.ZERO);
                        damageGiver.connection.send(new ClientboundSetEntityMotionPacket(damageTaker));
                        damageGiver.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, COMBO_TIME + 10, 0, false, false));
                        baseformProperties.airComboHoverTimer = 10; // ~0.5s hover per hit
                    }

                    if (baseformProperties.meleeHitCount == 2)
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
                    if (baseformProperties.meleeHitCount == 3)
                    {
                        damageTaker.hurt(ModDamageTypes.getDamageSource(damageGiver.level(), ModDamageTypes.SONIC_MELEE.getResourceKey(), damageGiver), 8.0f);
                        Vec3 knockDir = damageGiver.getLookAngle();
                        damageTaker.addDeltaMovement(new Vec3(knockDir.x * 0.8, 0.3, knockDir.z * 0.8));
                        damageGiver.connection.send(new ClientboundSetEntityMotionPacket(damageTaker));
                    }

                    //Increase Count
                    baseformProperties.meleeHitCount = (byte) ((baseformProperties.meleeHitCount + 1) % 4);

                    //Cancel the Current Combo schedule
                    ScheduledTask currentSchedule = hitSchedule.get(damageGiver.getUUID());
                    if (currentSchedule != null && !currentSchedule.isCancelled()) currentSchedule.cancel();

                    //Add another Schedule to reset counter After 2 seconds
                    hitSchedule.put(damageGiver.getUUID(), Scheduler.scheduleTask(() -> {
                                baseformProperties.meleeHitCount = 0;
                                damageGiver.removeEffect(MobEffects.SLOW_FALLING);
                            }, COMBO_TIME)
                    );
                }
                else if (DamageHandler.isDamageSourceModded(event.getSource())) {
                    //Cancel Combo, if you do any other modded Attack
                    assert damageGiver != null;
                    ScheduledTask currentSchedule = hitSchedule.get(damageGiver.getUUID());
                    if (currentSchedule != null && !currentSchedule.isCancelled()) {
                        currentSchedule.cancel();
                        damageGiver.removeEffect(MobEffects.SLOW_FALLING);
                        baseformProperties.meleeHitCount = 0;
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
                assert damageGiver != null;
                Vec3 currentPlayerMovement = damageGiver.getDeltaMovement();
                damageGiver.setDeltaMovement(currentPlayerMovement.x(), 0.0, currentPlayerMovement.z());
                damageGiver.connection.send(new ClientboundSetEntityMotionPacket(damageGiver));
            }

            //Smash Hit
            if(baseformProperties.smashHit > 0 && event.getSource().is(DamageTypes.PLAYER_ATTACK))
            {
                //Knockback
                assert damageGiver != null;
                damageTaker.setDeltaMovement(damageGiver.getLookAngle().scale(baseformProperties.smashHit/20.0f));
                //Damage Enemy
                damageTaker.hurt(ModDamageTypes.getDamageSource(damageGiver.level(),ModDamageTypes.SONIC_MELEE.getResourceKey(),damageGiver),
                        baseformProperties.smashHit*0.65f);

                //Sound
                damageGiver.level().playSound(null,damageGiver.getX(),damageGiver.getY(),damageGiver.getZ(), ModSounds.SMASH_HIT.get(), SoundSource.MASTER, 1.0f, 1.0f);

                {
                    PlayerSonicForm playerSonicForm = damageGiver.getData(ModAttachments.PLAYER_SONIC_FORM);
                    //Get Data From the Player
                    BaseformProperties updatedBaseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();
                    updatedBaseformProperties.smashHit = 0;
                }
            }//Speed Blitz
            else if(baseformProperties.speedBlitz && event.getSource().is(DamageTypes.PLAYER_ATTACK))
            {
                //Recover Speed Blitz Dashes
                assert damageGiver != null;
                if(!damageGiver.hasEffect(ModEffects.SPEED_BLITZING) && damageGiver.onGround())
                    baseformProperties.speedBlitzDashes = 4;

                //Set Speed Blitz damage (8 base + 10 per boost level)
                event.setAmount(8.0f + baseformProperties.boostLvl * 10.0f);

                //Current Combo Duration
                MobEffectInstance currComboEffect = damageTaker.getEffect(ModEffects.SPEED_BLITZED);
                if(currComboEffect == null)
                    damageTaker.addEffect(new MobEffectInstance(ModEffects.SPEED_BLITZED, 20, 0, false, false));
                else
                    currComboEffect.update(new MobEffectInstance(ModEffects.SPEED_BLITZED, 20, 0, false, false));

                //Increment Meter
                baseformProperties.ultimateAtkMeter = baseformProperties.ultimateAtkMeter + event.getAmount();

                Scheduler.scheduleTask(()->{
                    damageTaker.setDeltaMovement(ModUtils.calculateViewVector(damageGiver.getXRot(),damageGiver.getYRot()).scale(0.85));
                    damageGiver.connection.send(new ClientboundSetEntityMotionPacket(damageTaker));
                },3);
            }

            //Prevent Spam punching
            if(damageTaker.hurtTime != 0)
                throw new NullPointerException("Mob is already being hurt");

            //Meter increase
            if(!event.getSource().is(ModDamageTypes.SONIC_ULTIMATE.getResourceKey()))
            {
                baseformProperties.qkCyloopMeter = Math.min(100.0,baseformProperties.qkCyloopMeter+event.getAmount()/5.0);
                baseformProperties.ultimateAtkMeter = baseformProperties.ultimateAtkMeter + (
                        ((event.getSource().is(ModDamageTypes.SONIC_RANGED.getResourceKey())||event.getSource().is(ModDamageTypes.SONIC_RANGED_COMBO_IMMUNE.getResourceKey())))?event.getAmount()/1.5:
                                ((event.getSource().is(ModDamageTypes.SONIC_MELEE.getResourceKey()))||event.getSource().is(ModDamageTypes.SONIC_MELEE_COMBO_IMMUNE.getResourceKey()))?event.getAmount()*1.25:
                                        (event.getSource().is(ModDamageTypes.SONIC_CYLOOP.getResourceKey()))?event.getAmount()*2.0:event.getAmount()/1.3);

                if(baseformProperties.ultimateAtkMeter > 100.0)
                    baseformProperties.ultimateAtkMeter = 100.0;
            }

            //Combo Display Increase
            if(!event.getSource().is(ModDamageTypes.SONIC_CYLOOP.getResourceKey()) &&
                    !event.getSource().is(ModDamageTypes.SONIC_RANGED_COMBO_IMMUNE.getResourceKey()) &&
                    !event.getSource().is(ModDamageTypes.SONIC_MELEE_COMBO_IMMUNE.getResourceKey()) &&
                    !event.getSource().is(ModDamageTypes.SONIC_BALL_COMBO_IMMUNE.getResourceKey()) &&
                    !event.getSource().is(ModDamageTypes.SONIC_ULTIMATE.getResourceKey())
            //&&     (damageTaker.getHealth() < 3.0f || damageTaker.getMaxHealth() > 30.0f)
            )
            {
                //Increase Counter
                if(!event.getSource().is(ModDamageTypes.SONIC_RANGED.getResourceKey()) || baseformProperties.rangedComboTrip <= 0) {
                    if (!event.getSource().is(DamageTypes.PLAYER_ATTACK) || baseformProperties.meleeHitCount % 2 == 0)
                        baseformProperties.comboPointCount += 1;
                }

                //Gives Ranged attack is a ~1 sec Delay
                if(event.getSource().is(ModDamageTypes.SONIC_RANGED.getResourceKey()) && baseformProperties.rangedComboTrip <= 0)
                    baseformProperties.rangedComboTrip = 15;

                assert damageGiver != null;
            }



        }catch(NullPointerException|ClassCastException ignored){}
    }
}
