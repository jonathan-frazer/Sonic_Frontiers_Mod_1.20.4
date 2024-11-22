package net.sonicrushxii.beyondthehorizon.modded;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.potion_effects.*;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS
            = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BeyondTheHorizon.MOD_ID);

    public static final RegistryObject<MobEffect> COMBO_EFFECT = MOB_EFFECTS.register(
            "combo_effect",()->((new ComboEffect(MobEffectCategory.HARMFUL,0x0011FF)).addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "12AEAA34-359B-1198-935C-2E7E61020331", -0.9, AttributeModifier.Operation.MULTIPLY_TOTAL))
    );
    public static final RegistryObject<MobEffect> INITATE_POWER_BOOST = MOB_EFFECTS.register(
            "initiate_power_boost",()->((new InitiatePowerBoostEffect(MobEffectCategory.HARMFUL,0x0000FF)).addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "12AEAA34-359B-1198-935C-2E7E61020332", -1.0, AttributeModifier.Operation.MULTIPLY_TOTAL))
    );
    public static final RegistryObject<MobEffect> SPEED_BLITZED = MOB_EFFECTS.register(
            "speed_blitzed",()->((new SpeedBlitzed(MobEffectCategory.HARMFUL,0x0011FF)).addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "12AEAA34-359B-1198-935C-2E7E61020333", -1.0, AttributeModifier.Operation.MULTIPLY_TOTAL))
    );
    public static final RegistryObject<MobEffect> SPEED_BLITZING = MOB_EFFECTS.register(
            "speed_blitzing",()->((new SpeedBlitzing(MobEffectCategory.BENEFICIAL,0x0011FF)).addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "12AEAA34-359B-1198-935C-2E7E61020334", -1.0, AttributeModifier.Operation.MULTIPLY_TOTAL))
    );
    public static final RegistryObject<MobEffect> CYLOOPED = MOB_EFFECTS.register(
            "cyloop_effect",()->((new CyloopEffect(MobEffectCategory.HARMFUL,0x00FFFF)).addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "12AEAA34-359B-1198-935C-2E7E61020335", -1.0, AttributeModifier.Operation.MULTIPLY_TOTAL))
    );
    public static final RegistryObject<MobEffect> MIRAGE_CONFUSE = MOB_EFFECTS.register(
            "mirage_confusion",()->((new MirageConfusion(MobEffectCategory.HARMFUL,0x00FFFF)).addAttributeModifier(Attributes.FOLLOW_RANGE, "12AEAA34-359B-1198-935C-2E7E61020336", -1.0, AttributeModifier.Operation.MULTIPLY_TOTAL))
    );
    public static final RegistryObject<MobEffect> WIND_STUNNED = MOB_EFFECTS.register(
            "sonic_wind_stun",()->((new WindStunned(MobEffectCategory.HARMFUL,0xFFFFFF)).addAttributeModifier(Attributes.MOVEMENT_SPEED, "12AEAA34-359B-1198-935C-2E7E61020337", -1.0, AttributeModifier.Operation.ADDITION).addAttributeModifier(Attributes.ATTACK_DAMAGE, "12AEAA34-359B-1198-935C-2E7E61020338", -1.0, AttributeModifier.Operation.ADDITION))
    );


    public static void register(IEventBus eventBus)
    {
        MOB_EFFECTS.register(eventBus);
    }
}
