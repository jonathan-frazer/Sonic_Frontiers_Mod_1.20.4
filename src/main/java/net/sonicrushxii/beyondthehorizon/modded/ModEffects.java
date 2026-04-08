package net.sonicrushxii.beyondthehorizon.modded;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.potion_effects.*;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS
            = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, BeyondTheHorizon.MOD_ID);

    public static final Holder<MobEffect> COMBO_EFFECT = MOB_EFFECTS.register(
            "baseform/combo_effect",()->((new ComboEffect(MobEffectCategory.HARMFUL,0x0011FF)).addAttributeModifier(Attributes.GRAVITY, ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "effect.combo_gravity"), -0.9, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
    );
    public static final Holder<MobEffect> INITATE_POWER_BOOST = MOB_EFFECTS.register(
            "baseform/power_boost",()->((new InitiatePowerBoostEffect(MobEffectCategory.HARMFUL,0x0000FF)).addAttributeModifier(Attributes.GRAVITY, ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "effect.power_boost_gravity"), -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
    );
    public static final Holder<MobEffect> SPEED_BLITZED = MOB_EFFECTS.register(
            "baseform/speed_blitzed",()->((new SpeedBlitzed(MobEffectCategory.HARMFUL,0x0011FF)).addAttributeModifier(Attributes.GRAVITY, ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "effect.speed_blitzed_gravity"), -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
    );
    public static final Holder<MobEffect> SPEED_BLITZING = MOB_EFFECTS.register(
            "baseform/speed_blitzing",()->((new SpeedBlitzing(MobEffectCategory.BENEFICIAL,0x0011FF)).addAttributeModifier(Attributes.GRAVITY, ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "effect.speed_blitzing_gravity"), -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
    );
    public static final Holder<MobEffect> CYLOOPED = MOB_EFFECTS.register(
            "baseform/cyloop_effect",()->((new CyloopEffect(MobEffectCategory.HARMFUL,0x00FFFF)).addAttributeModifier(Attributes.GRAVITY, ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "effect.cyloop_gravity"), -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
    );
    public static final Holder<MobEffect> MIRAGE_CONFUSE = MOB_EFFECTS.register(
            "baseform/mirage_confusion",()->((new MirageConfusion(MobEffectCategory.HARMFUL,0x00FFFF)).addAttributeModifier(Attributes.FOLLOW_RANGE, ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "effect.mirage_follow_range"), -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
    );
    public static final Holder<MobEffect> WIND_STUNNED = MOB_EFFECTS.register(
            "baseform/sonic_wind_stun",()->((new WindStunned(MobEffectCategory.HARMFUL,0xFFFFFF)).addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "effect.wind_stun_speed"), -1.0, AttributeModifier.Operation.ADD_VALUE).addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "effect.wind_stun_damage"), -1.0, AttributeModifier.Operation.ADD_VALUE))
    );


    public static void register(IEventBus eventBus)
    {
        MOB_EFFECTS.register(eventBus);
    }
}
