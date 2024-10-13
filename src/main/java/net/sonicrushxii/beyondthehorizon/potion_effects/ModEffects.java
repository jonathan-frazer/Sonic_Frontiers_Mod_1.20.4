package net.sonicrushxii.beyondthehorizon.potion_effects;

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

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS
            = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BeyondTheHorizon.MOD_ID);
    public static final RegistryObject<MobEffect> COMBO = MOB_EFFECTS.register(
            "combo_effect",()->((new ComboEffect(MobEffectCategory.HARMFUL,3215315)).addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "12AEAA34-359B-1198-935C-2E7E61020331", -1.0, AttributeModifier.Operation.MULTIPLY_TOTAL))
    );

    public static void register(IEventBus eventBus)
    {
        MOB_EFFECTS.register(eventBus);
    }
}
