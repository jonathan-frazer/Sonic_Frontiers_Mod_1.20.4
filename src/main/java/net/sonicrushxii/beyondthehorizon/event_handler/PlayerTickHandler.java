package net.sonicrushxii.beyondthehorizon.event_handler;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformClient;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformServer;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.capabilities.hyperform.HyperformHandler;
import net.sonicrushxii.beyondthehorizon.capabilities.starfall.StarfallFormHandler;
import net.sonicrushxii.beyondthehorizon.capabilities.superform.SuperformHandler;
import net.sonicrushxii.beyondthehorizon.client.DoubleTapDirection;
import net.sonicrushxii.beyondthehorizon.client.DoubleTapHandler;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;


public class PlayerTickHandler {

    public static int clientTickCounter = 0;
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

        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
            switch(playerSonicForm.getCurrentForm())
            {
                case BASEFORM -> BaseformClient.performClientTick(player,playerNBT);
                case SUPERFORM -> SuperformHandler.performSuperformClientTick(player);
                case STARFALLFORM -> StarfallFormHandler.performStarfallformClientTick(player);
                case HYPERFORM -> HyperformHandler.performHyperformClientTick(player);
            }
        });


        //Play Second
        if(clientTickCounter == 0)
            localPlayerSecond(player,playerNBT);


        //Double Tap Handler
        Minecraft mc = Minecraft.getInstance();
        {
            final int DOUBLE_TAP_COOLDOWN = 15;//In Ticks

            if (InputConstants.isKeyDown(mc.getWindow().getWindow(), InputConstants.KEY_A) &&
                    !DoubleTapHandler.pressedLeft && !DoubleTapHandler.releasedLeft)
                DoubleTapHandler.pressedLeft = true;
            if (!InputConstants.isKeyDown(mc.getWindow().getWindow(), InputConstants.KEY_A) &&
                    DoubleTapHandler.pressedLeft && !DoubleTapHandler.releasedLeft) {
                DoubleTapHandler.releasedLeft = true;
                DoubleTapHandler.scheduleResetLeftPress();
            }
            if (InputConstants.isKeyDown(mc.getWindow().getWindow(), InputConstants.KEY_A) &&
                    DoubleTapHandler.pressedLeft && DoubleTapHandler.releasedLeft && !mc.isPaused() && mc.screen == null) {
                //Handle some internal stuff
                DoubleTapHandler.markDoubleLeftPress();

                //Perform the Function
                if(DoubleTapHandler.doubleTapLock == false){
                    player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
                        switch(playerSonicForm.getCurrentForm())
                        {
                            case BASEFORM -> BaseformClient.performDoublePress(player,(BaseformProperties) playerSonicForm.getFormProperties(), DoubleTapDirection.LEFT_PRESS);
                        /*
                        case SUPERFORM
                        case STARFALLFORM
                        case HYPERFORM
                         */
                        }
                    });

                    //Prevent it from being pressed for a lil bit
                    DoubleTapHandler.doubleTapLock = true;
                    Scheduler.scheduleTask(()->{
                        DoubleTapHandler.doubleTapLock = false;
                    },DOUBLE_TAP_COOLDOWN);
                }
            }

            if (InputConstants.isKeyDown(mc.getWindow().getWindow(), InputConstants.KEY_D) &&
                    !DoubleTapHandler.pressedRight && !DoubleTapHandler.releasedRight)
                DoubleTapHandler.pressedRight = true;
            if (!InputConstants.isKeyDown(mc.getWindow().getWindow(), InputConstants.KEY_D) &&
                    DoubleTapHandler.pressedRight && !DoubleTapHandler.releasedRight) {
                DoubleTapHandler.releasedRight = true;
                DoubleTapHandler.scheduleResetRightPress();
            }
            if (InputConstants.isKeyDown(mc.getWindow().getWindow(), InputConstants.KEY_D) &&
                    DoubleTapHandler.pressedRight && DoubleTapHandler.releasedRight && !mc.isPaused() && mc.screen == null) {
                //Handle some internal stuff
                DoubleTapHandler.markDoubleRightPress();

                //Perform the Function
                if(DoubleTapHandler.doubleTapLock == false) {
                    player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
                        switch(playerSonicForm.getCurrentForm())
                        {
                            case BASEFORM -> BaseformClient.performDoublePress(player,(BaseformProperties) playerSonicForm.getFormProperties(), DoubleTapDirection.RIGHT_PRESS);
                        /*
                        case SUPERFORM
                        case STARFALLFORM
                        case HYPERFORM
                         */
                        }
                    });
                    //Prevent it from being pressed for a lil bit
                    DoubleTapHandler.doubleTapLock = true;
                    Scheduler.scheduleTask(()->{
                        DoubleTapHandler.doubleTapLock = false;
                    },DOUBLE_TAP_COOLDOWN);
                }
            }
        }
    }

    private void localPlayerSecond(LocalPlayer player, CompoundTag playerNBT)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
            switch(playerSonicForm.getCurrentForm())
            {
                case BASEFORM -> BaseformClient.performClientSecond(player,playerNBT);
                case SUPERFORM -> SuperformHandler.performSuperformClientSecond(player);
                case STARFALLFORM -> StarfallFormHandler.performStarfallformClientSecond(player);
                case HYPERFORM -> HyperformHandler.performHyperformClientSecond(player);
            }
        });

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

        ++clientTickCounter;
        if (clientTickCounter >= TICKS_PER_SECOND) {
            clientTickCounter = 0;
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
