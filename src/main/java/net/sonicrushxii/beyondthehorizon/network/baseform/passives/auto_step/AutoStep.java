package net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;

public class AutoStep {
    public static void performStepUpActivate(ServerPlayer player)
    {
        if (!player.getAttribute(NeoForgeMod.STEP_HEIGHT.get()).hasModifier(AttributeMultipliers.STEP_UP_SPRINT))
            player.getAttribute(NeoForgeMod.STEP_HEIGHT.get()).addTransientModifier(AttributeMultipliers.STEP_UP_SPRINT);
    }

    public static void performStepUpDeactivate(ServerPlayer player)
    {
        if (player.getAttribute(NeoForgeMod.STEP_HEIGHT.get()).hasModifier(AttributeMultipliers.STEP_UP_SPRINT))
            player.getAttribute(NeoForgeMod.STEP_HEIGHT.get()).removeModifier(AttributeMultipliers.STEP_UP_SPRINT.getId());
    }
}
