package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformHandler;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Arrays;
import java.util.List;

public class DamageHandler {
    @SubscribeEvent
    public void onPlayerDamaged(LivingIncomingDamageEvent event)
    {
        /** Player Attacked*/
        if(event.getEntity() instanceof ServerPlayer player)
        {
            {
                PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                switch(playerSonicForm.getCurrentForm())
                {
                    case BASEFORM -> BaseformHandler.takeDamage(event,(BaseformProperties)playerSonicForm.getFormProperties());
                    /*case SUPERFORM ->
                    case STARFALLFORM ->
                    case HYPERFORM ->*/
                }

                //Sync Player Properties
                PacketHandler.sendToALLPlayers(
                        new SyncPlayerFormS2C(
                                player.getId(),
                                playerSonicForm
                        ));
            }
        }

        /** Player: Attacker*/
        try{
            if(event.getSource().getEntity() instanceof ServerPlayer player)
            {
                {
                    PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                    switch(playerSonicForm.getCurrentForm())
                    {
                        case BASEFORM -> BaseformHandler.dealDamage(event,(BaseformProperties)playerSonicForm.getFormProperties());
                        /*case SUPERFORM ->
                        case STARFALLFORM ->
                        case HYPERFORM ->*/
                    }

                    //Sync Player Properties
                    PacketHandler.sendToALLPlayers(
                            new SyncPlayerFormS2C(
                                    player.getId(),
                                    playerSonicForm
                            ));
                }
            }
        }catch(NullPointerException ignored){}
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if(!(event.getEntity() instanceof ServerPlayer player)) return;
        if(PlayerTickHandler.hasAllChaosEmeralds(player)) {
            event.setCanceled(true);
            player.setHealth(4.0f);
            consumeChaosEmeralds(player);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.TOTEM_USE, SoundSource.MASTER, 1.0f, 1.0f);
        }
    }

    private static void consumeChaosEmeralds(Player player) {
        List<String> paths = Arrays.asList(
            "chaos_emerald/aqua_emerald", "chaos_emerald/blue_emerald",
            "chaos_emerald/green_emerald", "chaos_emerald/grey_emerald",
            "chaos_emerald/purple_emerald", "chaos_emerald/red_emerald",
            "chaos_emerald/yellow_emerald"
        );
        for(String path : paths) {
            Item target = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("chaos_emerald", path));
            for(int i = 0; i < player.getInventory().items.size(); i++) {
                ItemStack stack = player.getInventory().items.get(i);
                if(!stack.isEmpty() && stack.getItem() == target) {
                    player.getInventory().items.set(i, ItemStack.EMPTY);
                    break;
                }
            }
        }
    }

    public static boolean isDamageSourceModded(DamageSource damageSource)
    {
        //Checks all the ModDamageTypes
        for(ModDamageTypes modDamageType : ModDamageTypes.values())
            return damageSource.is(modDamageType.getResourceKey());

        return false;
    }
}
