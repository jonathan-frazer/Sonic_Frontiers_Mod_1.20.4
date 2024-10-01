package net.sonicrushxii.beyondthehorizon.network.baseform.passives;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class SpeedMultipliers {
    public static final AttributeModifier LIGHTSPEED_MODE = new AttributeModifier(new UUID(0x3214767890AB6DEFL, 0xFEBCBA09F7654C21L),
            "Lightspeed Mode", 1.0F, AttributeModifier.Operation.MULTIPLY_TOTAL);

}