package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.sonicrushxii.beyondthehorizon.ModUtils;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformServer;
import net.sonicrushxii.beyondthehorizon.capabilities.hyperform.HyperformHandler;
import net.sonicrushxii.beyondthehorizon.capabilities.starfall.StarfallFormHandler;
import net.sonicrushxii.beyondthehorizon.capabilities.superform.SuperformHandler;
import net.sonicrushxii.beyondthehorizon.event_handler.client_handlers.ClientTickHandler;

import java.util.Arrays;


public class PlayerTickHandler {
    public static int tickCounter = 0;
    private static final int TICKS_PER_SECOND = 20;

    public static boolean hasAllChaosEmeralds(Player player)
    {
        return ModUtils.playerHasAllItems
                (
                        player,
                        Arrays.asList(
                                "chaos_emerald/aqua_emerald",
                                "chaos_emerald/blue_emerald",
                                "chaos_emerald/green_emerald",
                                "chaos_emerald/grey_emerald",
                                "chaos_emerald/purple_emerald",
                                "chaos_emerald/red_emerald",
                                "chaos_emerald/yellow_emerald"
                        ), "chaos_emerald"
                );
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent.Pre event) {
        if (event.player == null) return;
        if(event.player.level().isClientSide && FMLEnvironment.dist == Dist.CLIENT) ClientTickHandler.localPlayerTick(event.player);
        else serverPlayerTick((ServerPlayer) event.player);
    }

    private void serverPlayerTick(ServerPlayer player)
    {
        if (!player.isAlive())
            return;

        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            switch(playerSonicForm.getCurrentForm())
            {
                case BASEFORM -> BaseformServer.performServerTick(player);
                case SUPERFORM -> SuperformHandler.performSuperformServerTick(player);
                case STARFALLFORM -> StarfallFormHandler.performStarfallformServerTick(player);
                case HYPERFORM -> HyperformHandler.performHyperformServerTick(player);
            }
        });

        ++tickCounter;
        if (tickCounter >= TICKS_PER_SECOND) {
            tickCounter = 0;
            serverPlayerSecond(player);
        }
    }

    private void serverPlayerSecond(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            switch(playerSonicForm.getCurrentForm())
            {
                case BASEFORM -> BaseformServer.performServerSecond(player);
                case SUPERFORM -> SuperformHandler.performSuperformServerSecond(player);
                case STARFALLFORM -> StarfallFormHandler.performStarfallformServerSecond(player);
                case HYPERFORM -> HyperformHandler.performHyperformServerSecond(player);
            }
        });
    }
}
