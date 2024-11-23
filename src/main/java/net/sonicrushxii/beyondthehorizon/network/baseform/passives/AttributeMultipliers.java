package net.sonicrushxii.beyondthehorizon.network.baseform.passives;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class AttributeMultipliers {
    public static final AttributeModifier LIGHTSPEED_MODE = new AttributeModifier(new UUID(0x1234767890AB6DEFL, 0xFEBCBA09F7654C21L),
            "Lightspeed Mode", 1.0F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    public static final AttributeModifier POWERBOOST_SPEED = new AttributeModifier(new UUID(0x1234767890AB7DEFL, 0xFEBCBA09F7654C21L),
            "Power_boost_Speed", 0.60F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    public static final AttributeModifier POWERBOOST_ARMOR = new AttributeModifier(new UUID(0x1234767890AB8DEFL, 0xFEBCBA09F7654C21L),
            "Power_boost_Armor", 3.67F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    public static final AttributeModifier SMASH_HIT = new AttributeModifier(new UUID(0x1234767890AB9DEFL, 0xFEBCBA09F7654C21L),
            "Smash_Hit", -1.0F, AttributeModifier.Operation.ADDITION);
    public static final AttributeModifier PARRY_HOLD = new AttributeModifier(new UUID(0x1234767890AC0DEFL, 0xFEBCBA09F7654C21L),
            "Parry_FOV", -0.5F, AttributeModifier.Operation.ADDITION);
    public static final AttributeModifier PARRY_SPEED = new AttributeModifier(new UUID(0x1234767890AC1DEFL, 0xFEBCBA09F7654C21L),
            "Parry_Speed", 0.60F, AttributeModifier.Operation.ADDITION);
}