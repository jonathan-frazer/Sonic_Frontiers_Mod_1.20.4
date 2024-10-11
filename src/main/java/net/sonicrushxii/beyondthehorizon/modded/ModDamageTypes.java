package net.sonicrushxii.beyondthehorizon.modded;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public enum ModDamageTypes
{
    SONIC_BALL("sonic_ball");

    private final ResourceKey<DamageType> dmgSrc;

    ModDamageTypes(String jsonFilename)
    {
        dmgSrc = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(BeyondTheHorizon.MOD_ID, jsonFilename));
    }

    public ResourceKey<DamageType> get()
    {
        return dmgSrc;
    }
}
