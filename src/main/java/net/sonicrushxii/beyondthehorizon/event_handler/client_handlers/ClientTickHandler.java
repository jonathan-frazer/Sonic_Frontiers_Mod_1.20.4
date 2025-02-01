package net.sonicrushxii.beyondthehorizon.event_handler.client_handlers;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformClient;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.capabilities.hyperform.HyperformHandler;
import net.sonicrushxii.beyondthehorizon.capabilities.starfall.StarfallFormHandler;
import net.sonicrushxii.beyondthehorizon.capabilities.superform.SuperformHandler;
import net.sonicrushxii.beyondthehorizon.client.DoubleTapDirection;
import net.sonicrushxii.beyondthehorizon.client.DoubleTapHandler;
import net.sonicrushxii.beyondthehorizon.event_handler.PlayerTickHandler;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;

public class ClientTickHandler {


    public static void localPlayerTick(Player pPlayer)
    {
        if(pPlayer instanceof AbstractClientPlayer player)
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
            if(PlayerTickHandler.tickCounter == 0)
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
    }

    public static void localPlayerSecond(Player pPlayer, CompoundTag playerNBT)
    {
        if(pPlayer instanceof AbstractClientPlayer player)
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
}
