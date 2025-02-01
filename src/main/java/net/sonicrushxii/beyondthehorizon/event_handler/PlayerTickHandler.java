package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformServer;
import net.sonicrushxii.beyondthehorizon.capabilities.hyperform.HyperformHandler;
import net.sonicrushxii.beyondthehorizon.capabilities.starfall.StarfallFormHandler;
import net.sonicrushxii.beyondthehorizon.capabilities.superform.SuperformHandler;
import net.sonicrushxii.beyondthehorizon.event_handler.client_handlers.ClientTickHandler;


public class PlayerTickHandler {
    public static int tickCounter = 0;
    private static final int TICKS_PER_SECOND = 20;

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
        CompoundTag playerNBT = player.serializeNBT();

        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            switch(playerSonicForm.getCurrentForm())
            {
                case BASEFORM -> BaseformServer.performServerTick(player,playerNBT);
                case SUPERFORM -> SuperformHandler.performSuperformServerTick(player);
                case STARFALLFORM -> StarfallFormHandler.performStarfallformServerTick(player);
                case HYPERFORM -> HyperformHandler.performHyperformServerTick(player);
            }
        });

        ++tickCounter;
        if (tickCounter >= TICKS_PER_SECOND) {
            tickCounter = 0;
            serverPlayerSecond(player,playerNBT);
        }
    }

    private void serverPlayerSecond(ServerPlayer player, CompoundTag playerNBT)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            switch(playerSonicForm.getCurrentForm())
            {
                case BASEFORM -> BaseformServer.performServerSecond(player,playerNBT);
                case SUPERFORM -> SuperformHandler.performSuperformServerSecond(player);
                case STARFALLFORM -> StarfallFormHandler.performStarfallformServerSecond(player);
                case HYPERFORM -> HyperformHandler.performHyperformServerSecond(player);
            }
        });
    }
}
