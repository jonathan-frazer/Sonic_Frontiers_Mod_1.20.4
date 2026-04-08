package net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;

public class AutoStep {
    public static void performStepUpActivate(ServerPlayer player)
    {
        if (!player.getAttribute(Attributes.STEP_HEIGHT).hasModifier(AttributeMultipliers.STEP_UP_SPRINT.id()))
            player.getAttribute(Attributes.STEP_HEIGHT).addTransientModifier(AttributeMultipliers.STEP_UP_SPRINT);
    }

    public static void performStepUpDeactivate(ServerPlayer player)
    {
        if (player.getAttribute(Attributes.STEP_HEIGHT).hasModifier(AttributeMultipliers.STEP_UP_SPRINT.id()))
            player.getAttribute(Attributes.STEP_HEIGHT).removeModifier(AttributeMultipliers.STEP_UP_SPRINT.id());
    }
}
