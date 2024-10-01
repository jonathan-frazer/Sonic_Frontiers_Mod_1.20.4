package net.sonicrushxii.beyondthehorizon.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/*
* Example Usage(Right Press):-
            if(InputConstants.isKeyDown(minecraft.getWindow().getWindow(),InputConstants.KEY_D) &&
                    !DoubleTapHandler.pressedRight && !DoubleTapHandler.releasedRight)
                DoubleTapHandler.pressedRight = true;
            if(!InputConstants.isKeyDown(minecraft.getWindow().getWindow(),InputConstants.KEY_D) &&
                    DoubleTapHandler.pressedRight && !DoubleTapHandler.releasedRight) {
                DoubleTapHandler.releasedRight = true;
                DoubleTapHandler.scheduleResetRightPress();
            }
            if(InputConstants.isKeyDown(minecraft.getWindow().getWindow(),InputConstants.KEY_D) &&
                    DoubleTapHandler.pressedRight && DoubleTapHandler.releasedRight)
            {
                DoubleTapHandler.markDoubleRightPress();
            }
* */


public class DoubleTapHandler
{
    private static ScheduledExecutorService leftExecutor = Executors.newScheduledThreadPool(1);
    private static ScheduledFuture<?> leftFuture=null;
    private static ScheduledExecutorService rightExecutor = Executors.newScheduledThreadPool(1);
    private static ScheduledFuture<?> rightFuture=null;

    public static void scheduleResetLeftPress(){
        leftFuture = leftExecutor.schedule(() -> {
            pressedLeft = false; releasedLeft = false;
        }, 400, TimeUnit.MILLISECONDS);
    }

    public static void markDoubleLeftPress(){
        pressedLeft = false;
        releasedLeft = false;
        if(leftFuture != null) leftFuture.cancel(false);
    }

    public static void scheduleResetRightPress(){
        rightFuture = rightExecutor.schedule(() -> {
            pressedRight = false; releasedRight = false;
        }, 400, TimeUnit.MILLISECONDS);
    }

    public static void markDoubleRightPress(){
        pressedRight = false;
        releasedRight = false;
        if(rightFuture != null) rightFuture.cancel(false);
    }

    public static boolean pressedRight = false;
    public static boolean pressedLeft = false;
    public static boolean releasedRight  = false;
    public static boolean releasedLeft = false;
}

