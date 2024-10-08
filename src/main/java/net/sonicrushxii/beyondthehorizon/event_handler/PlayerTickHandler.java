package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformHandler;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.capabilities.hyperform.HyperformHandler;
import net.sonicrushxii.beyondthehorizon.capabilities.starfall.StarfallFormHandler;
import net.sonicrushxii.beyondthehorizon.capabilities.superform.SuperformHandler;
import net.sonicrushxii.beyondthehorizon.client.ClientFormData;


public class PlayerTickHandler {

    private static int tickCounter = 0;
    private static final int TICKS_PER_SECOND = 20;


    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent.Pre event) {
        if (event.player == null) return;
        if(event.player.level().isClientSide) localPlayerTick((LocalPlayer) event.player);
        else serverPlayerTick((ServerPlayer) event.player);
    }

    private void localPlayerTick(LocalPlayer player)
    {
        if (!player.isAlive())
            return;

       CompoundTag playerNBT = player.serializeNBT();
        switch(ClientFormData.getPlayerForm())
        {
            case BASEFORM -> BaseformHandler.performBaseformClientTick(player,playerNBT);
            case SUPERFORM -> SuperformHandler.performSuperformClientTick(player);
            case STARFALLFORM -> StarfallFormHandler.performStarfallformClientTick(player);
            case HYPERFORM -> HyperformHandler.performHyperformClientTick(player);
        }

        //Play Second
        if(tickCounter == 0)
            localPlayerSecond(player,playerNBT);
    }

    private void localPlayerSecond(LocalPlayer player, CompoundTag playerNBT)
    {
        switch(ClientFormData.getPlayerForm())
        {
            case BASEFORM -> BaseformHandler.performBaseformClientSecond(player,playerNBT);
            case SUPERFORM -> SuperformHandler.performSuperformClientSecond(player);
            case STARFALLFORM -> StarfallFormHandler.performStarfallformClientSecond(player);
            case HYPERFORM -> HyperformHandler.performHyperformClientSecond(player);
        }
    }

    private void serverPlayerTick(ServerPlayer player)
    {
        if (!player.isAlive())
            return;
        CompoundTag playerNBT = player.serializeNBT();

        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            switch(playerSonicForm.getCurrentForm())
            {
                case BASEFORM -> BaseformHandler.performBaseformServerTick(player,playerNBT);
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
        switch(ClientFormData.getPlayerForm())
        {
            case BASEFORM -> BaseformHandler.performBaseformServerSecond(player,playerNBT);
            case SUPERFORM -> SuperformHandler.performSuperformServerSecond(player);
            case STARFALLFORM -> StarfallFormHandler.performStarfallformServerSecond(player);
            case HYPERFORM -> HyperformHandler.performHyperformServerSecond(player);
        }
    }
}
