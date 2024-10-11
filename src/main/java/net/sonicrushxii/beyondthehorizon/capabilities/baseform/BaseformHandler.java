package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;

public class BaseformHandler {

    public static void takeDamage(LivingAttackEvent event, BaseformProperties baseformProperties)
    {
        try {
            ServerPlayer receiver = (ServerPlayer) event.getEntity();
            Entity damageGiver = event.getSource().getEntity();

            System.out.println((damageGiver==null)?"Nothing":damageGiver.getName().getString()+", Damaged: "+receiver.getName().getString());

            // Makes you only invulnerable to Direct mob attacks when using this ability. Like weakness but better
            if (baseformProperties.selectiveInvul && !(damageGiver instanceof Player) && !event.getSource().isIndirect())
                event.setCanceled(true);

            if (baseformProperties.dodgeInvul)
                event.setCanceled(true);

        }catch(NullPointerException ignored){}
    }

    public static void dealDamage(LivingAttackEvent event, BaseformProperties baseformProperties)
    {
        try {
            ServerPlayer damageGiver = (ServerPlayer) event.getSource().getEntity();
            Entity receiver = event.getEntity();

            System.out.println(damageGiver.getName().getString()+", Damaged: "+receiver.getName().getString());

        }catch(NullPointerException ignored){}
    }
}
