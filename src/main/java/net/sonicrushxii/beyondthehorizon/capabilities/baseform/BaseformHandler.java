package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.event_handler.DamageHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;
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
            if (baseformProperties.selectiveInvul && !(damageGiver instanceof Player) && !event.getSource().isIndirect())
                event.setCanceled(true);

        }catch(NullPointerException ignored){}
    }

    public static void dealDamage(LivingAttackEvent event, BaseformProperties baseformProperties)
    {
        try {
            ServerPlayer damageGiver = (ServerPlayer) event.getSource().getEntity();
            Entity receiver = event.getEntity();

            //Melee Attack
            if(event.getSource().is(DamageTypes.PLAYER_ATTACK))
            {
                baseformProperties.hitCount = (byte) ((baseformProperties.hitCount + 1) % 5);

                assert damageGiver != null;

                //Cancel the Current Combo schedule
                ScheduledTask currentSchedule = hitSchedule.get(damageGiver.getUUID());
                if (currentSchedule != null && !currentSchedule.isCancelled()) currentSchedule.cancel();

                //Add another Schedule to reset counter After 2 seconds
                hitSchedule.put(damageGiver.getUUID(), Scheduler.scheduleTask(() -> {
                            baseformProperties.hitCount = 0;
                            System.out.println("Reset Combo");
                        }, 40)
                );
            }
            else if(DamageHandler.isDamageSourceModded(event.getSource())){
                //Cancel Combo, if you do any other modded Attack
                ScheduledTask currentSchedule = hitSchedule.get(damageGiver.getUUID());
                if (currentSchedule != null && !currentSchedule.isCancelled()) {
                    currentSchedule.cancel();

                    baseformProperties.hitCount = 0;
                    System.out.println("Interrupt Combo");
                }
            }

        }catch(NullPointerException e){}
    }
}
