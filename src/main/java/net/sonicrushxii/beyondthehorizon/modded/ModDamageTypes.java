package net.sonicrushxii.beyondthehorizon.modded;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;

public enum ModDamageTypes
{
    SONIC_BALL("sonic_ball"),
    SONIC_MELEE("sonic_melee"),   //Deals More Charge
    SONIC_RANGED("sonic_ranged"), //Deals less Charge
    SONIC_CYLOOP("sonic_cyloop"),

    SONIC_BALL_COMBO_IMMUNE("sonic_ball_combo_immune"), //Does not Range Combo Meter
    SONIC_MELEE_COMBO_IMMUNE("sonic_melee_combo_immune"), //Deals less Charge, Does not raise the combo meter
    SONIC_RANGED_COMBO_IMMUNE("sonic_ranged_combo_immune"), //Deals less Charge, Does not raise the combo meter

    SONIC_ULTIMATE("sonic_ultimate");//Deals no Charge

    private final ResourceKey<DamageType> dmgResourceKey;

    ModDamageTypes(String jsonFilename)
    {
        dmgResourceKey = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(BeyondTheHorizon.MOD_ID, jsonFilename));
    }
    public ResourceKey<DamageType> getResourceKey()
    {
        return dmgResourceKey;
    }

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> type)
    {
        return new DamageSource(
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type)
        );
    }

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> type, LivingEntity causer)
    {
        return new DamageSource(
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type),
                causer
        );
    }

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> type, Vec3 knockbackDir)
    {
        return new DamageSource(
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type),
                knockbackDir
        );
    }

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> type, LivingEntity causer, LivingEntity victim)
    {
        return new DamageSource(
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type),
                causer, victim
        );
    }

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> type, LivingEntity causer, LivingEntity victim, Vec3 knockbackDir)
    {
        return new DamageSource(
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type),
                causer, victim, knockbackDir
        );
    }

}
