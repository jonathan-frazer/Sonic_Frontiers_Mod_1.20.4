package net.sonicrushxii.beyondthehorizon.modded;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.potion_effects.*;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS
            = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, BeyondTheHorizon.MOD_ID);

    public static final Holder<MobEffect> COMBO_EFFECT = MOB_EFFECTS.register(
            "baseform/combo_effect",()->((new ComboEffect(MobEffectCategory.HARMFUL,0x0011FF)).addAttributeModifier(NeoForgeMod.ENTITY_GRAVITY.get(), "12AEAA34-359B-1198-935C-2E7E61020331", -0.9, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
    );
    public static final Holder<MobEffect> INITATE_POWER_BOOST = MOB_EFFECTS.register(
            "baseform/power_boost",()->((new InitiatePowerBoostEffect(MobEffectCategory.HARMFUL,0x0000FF)).addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "12AEAA34-359B-1198-935C-2E7E61020332", -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
    );
    public static final Holder<MobEffect> SPEED_BLITZED = MOB_EFFECTS.register(
            "baseform/speed_blitzed",()->((new SpeedBlitzed(MobEffectCategory.HARMFUL,0x0011FF)).addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "12AEAA34-359B-1198-935C-2E7E61020333", -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
    );
    public static final Holder<MobEffect> SPEED_BLITZING = MOB_EFFECTS.register(
            "baseform/speed_blitzing",()->((new SpeedBlitzing(MobEffectCategory.BENEFICIAL,0x0011FF)).addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "12AEAA34-359B-1198-935C-2E7E61020334", -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
    );
    public static final Holder<MobEffect> CYLOOPED = MOB_EFFECTS.register(
            "baseform/cyloop_effect",()->((new CyloopEffect(MobEffectCategory.HARMFUL,0x00FFFF)).addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "12AEAA34-359B-1198-935C-2E7E61020335", -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
    );
    public static final Holder<MobEffect> MIRAGE_CONFUSE = MOB_EFFECTS.register(
            "baseform/mirage_confusion",()->((new MirageConfusion(MobEffectCategory.HARMFUL,0x00FFFF)).addAttributeModifier(Attributes.FOLLOW_RANGE, "12AEAA34-359B-1198-935C-2E7E61020336", -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
    );
    public static final Holder<MobEffect> WIND_STUNNED = MOB_EFFECTS.register(
            "baseform/sonic_wind_stun",()->((new WindStunned(MobEffectCategory.HARMFUL,0xFFFFFF)).addAttributeModifier(Attributes.MOVEMENT_SPEED, "12AEAA34-359B-1198-935C-2E7E61020337", -1.0, AttributeModifier.Operation.ADD_VALUE).addAttributeModifier(Attributes.ATTACK_DAMAGE, "12AEAA34-359B-1198-935C-2E7E61020338", -1.0, AttributeModifier.Operation.ADD_VALUE))
    );


    public static void register(IEventBus eventBus)
    {
        MOB_EFFECTS.register(eventBus);
    }
}
