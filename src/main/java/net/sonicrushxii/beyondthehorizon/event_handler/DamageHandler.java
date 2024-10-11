package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformHandler;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DamageHandler {
    @SubscribeEvent
    public void onPlayerDamaged(LivingAttackEvent event)
    {
        /** Player Attacked*/
        if(event.getEntity() instanceof ServerPlayer player)
        {
            player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
                switch(playerSonicForm.getCurrentForm())
                {
                    case BASEFORM -> BaseformHandler.takeDamage(event,(BaseformProperties)playerSonicForm.getFormProperties());
                    /*case SUPERFORM ->
                    case STARFALLFORM ->
                    case HYPERFORM ->*/
                }

                //Sync Player Properties
                PacketHandler.sendToPlayer(player,
                        new SyncPlayerFormS2C(
                                playerSonicForm.getCurrentForm(),
                                playerSonicForm.getFormProperties()
                        ));
            });
        }

        /** Player: Attacker*/
        try{
            if(event.getSource().getEntity() instanceof ServerPlayer player)
            {
                player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
                    switch(playerSonicForm.getCurrentForm())
                    {
                        case BASEFORM -> BaseformHandler.dealDamage(event,(BaseformProperties)playerSonicForm.getFormProperties());
                        /*case SUPERFORM ->
                        case STARFALLFORM ->
                        case HYPERFORM ->*/
                    }

                    //Sync Player Properties
                    PacketHandler.sendToPlayer(player,
                            new SyncPlayerFormS2C(
                                    playerSonicForm.getCurrentForm(),
                                    playerSonicForm.getFormProperties()
                            ));
                });
            }
        }catch(NullPointerException ignored){}
    }

    public static DamageSource getCustomDamageSrc(ResourceKey<DamageType> customDamageType, Entity sourceEntity, Entity responsibleFor)
    {
        DamageSource customDmgSrc = null;
        try {
            Method privateField = sourceEntity.damageSources().getClass().getDeclaredMethod("source",ResourceKey.class, Entity.class);
            privateField.setAccessible(true);
            customDmgSrc = (DamageSource) privateField.invoke(sourceEntity.damageSources(),customDamageType, responsibleFor);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassCastException e) {
            e.printStackTrace();
        }

        return customDmgSrc;
    }

    public static DamageSource getCustomDamageSrc(ResourceKey<DamageType> customDamageType, Entity sourceEntity)
    {
        DamageSource customDmgSrc = null;
        try {
            Method privateField = sourceEntity.damageSources().getClass().getDeclaredMethod("source",ResourceKey.class);
            privateField.setAccessible(true);
            customDmgSrc = (DamageSource) privateField.invoke(sourceEntity.damageSources(),customDamageType);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassCastException e) {
            e.printStackTrace();
        }

        return customDmgSrc;
    }

    public static boolean isDamageSourceModded(DamageSource damageSource)
    {
        //Checks all the ModDamageTypes
        for(ModDamageTypes modDamageType : ModDamageTypes.values())
            return damageSource.is(modDamageType.get());

        return false;
    }
}
