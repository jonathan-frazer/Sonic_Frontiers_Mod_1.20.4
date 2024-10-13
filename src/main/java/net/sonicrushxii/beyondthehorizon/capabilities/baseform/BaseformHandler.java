package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

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
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.event_handler.DamageHandler;
import net.sonicrushxii.beyondthehorizon.scheduler.ScheduledTask;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;

import java.util.HashMap;
import java.util.UUID;

public class BaseformHandler {
    private static HashMap<UUID,ScheduledTask> hitSchedule = new HashMap<>();

    public static void takeDamage(LivingAttackEvent event, BaseformProperties baseformProperties)
    {
        try {
            ServerPlayer receiver = (ServerPlayer) event.getEntity();
            Entity damageGiver = event.getSource().getEntity();

            // Makes you only invulnerable to Direct mob attacks when using this ability. Like weakness but better
            if (baseformProperties.dodgeInvul)
                event.setCanceled(true);
            if (baseformProperties.selectiveInvul() && !(damageGiver instanceof Player) && !event.getSource().isIndirect())
                event.setCanceled(true);

        }catch(NullPointerException ignored){}
    }

    public static void dealDamage(LivingAttackEvent event, BaseformProperties baseformProperties)
    {
        try {
            ServerPlayer damageGiver = (ServerPlayer) event.getSource().getEntity();
            Entity receiver = event.getEntity();

            //Melee Attack
            {
                final int COMBO_TIME = 40;

                if (event.getSource().is(DamageTypes.PLAYER_ATTACK)) {
                    //Perform Knockup/Knockdown
                    assert damageGiver != null;

                    if (!damageGiver.onGround()) {
                        Vec3 currentPlayerMovement = damageGiver.getDeltaMovement();
                        damageGiver.setDeltaMovement(currentPlayerMovement.x(), 0.0, currentPlayerMovement.z());
                        damageGiver.connection.send(new ClientboundSetEntityMotionPacket(damageGiver));
                        if (receiver instanceof LivingEntity damageTaker) {
                            damageTaker.setDeltaMovement(Vec3.ZERO);
                            damageGiver.connection.send(new ClientboundSetEntityMotionPacket(damageTaker));
                        }
                    }

                    if (baseformProperties.hitCount == 3) {
                        Level world = damageGiver.level();
                        world.playSound(null, damageGiver.getX(), damageGiver.getY(), damageGiver.getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.MASTER, 1.0f, 1.0f);
                    }
                    if (baseformProperties.hitCount == 4) {
                        if (damageGiver.isShiftKeyDown()) {
                            damageGiver.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 40, 10, false, false));
                            if (receiver instanceof LivingEntity damageTaker) {
                                damageTaker.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 40, 10, false, false));
                                damageTaker.moveTo(damageTaker.getX(), damageTaker.getY() + 5.0, damageTaker.getZ());
                            }
                        } else if (!damageGiver.onGround()) {
                            damageGiver.removeEffect(MobEffects.SLOW_FALLING);
                            if (receiver instanceof LivingEntity damageTaker) {
                                damageTaker.removeEffect(MobEffects.SLOW_FALLING);
                                damageTaker.addDeltaMovement(new Vec3(0.0, -0.85, 0.0));
                                damageGiver.connection.send(new ClientboundSetEntityMotionPacket(damageTaker));
                            }
                        } else {
                            if (receiver instanceof LivingEntity damageTaker) {
                                damageTaker.addDeltaMovement(damageGiver.getLookAngle());

                            }
                        }
                    }

                    //Increase Count
                    baseformProperties.hitCount = (byte) ((baseformProperties.hitCount + 1) % 5);

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

        }catch(NullPointerException e){}
    }
}
