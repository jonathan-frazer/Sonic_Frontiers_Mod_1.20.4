package net.sonicrushxii.beyondthehorizon.potion_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class InitiatePowerBoostEffect extends MobEffect {
    public InitiatePowerBoostEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory,color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        entity.setDeltaMovement(0.0,0.02,0.0);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int p_297908_, int p_301085_) {
        return true;
    }
}
