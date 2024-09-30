package net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeMod;

public class StepUp {
    public static void performStepUpActivate(ServerPlayer player) {
        player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).setBaseValue(1.5);
    }

    public static void performStepUpDeactivate(ServerPlayer player) {
        player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).setBaseValue(0.0);
    }


}
