package net.sonicrushxii.beyondthehorizon.modded;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;

public enum ModDamageTypes
{
    SONIC_BALL("sonic_ball"),
    SONIC_MELEE("sonic_melee");

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
