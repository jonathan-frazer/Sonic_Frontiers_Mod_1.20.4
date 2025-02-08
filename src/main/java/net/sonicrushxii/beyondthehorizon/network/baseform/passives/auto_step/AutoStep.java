package net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeMod;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;

public class AutoStep {
    public static void performStepUpActivate(ServerPlayer player)
    {
        if (!player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).hasModifier(AttributeMultipliers.STEP_UP_SPRINT))
            player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).addTransientModifier(AttributeMultipliers.STEP_UP_SPRINT);
    }

    public static void performStepUpDeactivate(ServerPlayer player)
    {
        if (player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).hasModifier(AttributeMultipliers.STEP_UP_SPRINT))
            player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).removeModifier(AttributeMultipliers.STEP_UP_SPRINT.getId());
    }
}
