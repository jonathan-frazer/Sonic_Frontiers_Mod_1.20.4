package net.sonicrushxii.beyondthehorizon.network.baseform.passives;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AttributeMultipliers {
    public static final AttributeModifier STEP_UP_SPRINT = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "step_up_sprint"), 1.00F, AttributeModifier.Operation.ADD_VALUE);
    public static final AttributeModifier STEP_UP_BASE = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "step_up_base"), 0.50F, AttributeModifier.Operation.ADD_VALUE);
    public static final AttributeModifier LIGHTSPEED_MODE = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "lightspeed_mode"), 1.0F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    public static final AttributeModifier POWERBOOST_SPEED = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "power_boost_speed"), 0.60F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    public static final AttributeModifier POWERBOOST_ARMOR = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "power_boost_armor"), 3.67F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    public static final AttributeModifier SMASH_HIT = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "smash_hit"), -1.0F, AttributeModifier.Operation.ADD_VALUE);
    public static final AttributeModifier PARRY_HOLD = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "parry_hold"), -1.5F, AttributeModifier.Operation.ADD_VALUE);
    public static final AttributeModifier PARRY_SPEED = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "parry_speed"), 0.60F, AttributeModifier.Operation.ADD_VALUE);
}
