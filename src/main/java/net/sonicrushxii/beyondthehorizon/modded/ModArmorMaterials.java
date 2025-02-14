package net.sonicrushxii.beyondthehorizon.modded;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;

import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {

    BASEFORM_SONIC("baseform",134217727,new int[]{1,1,1,1}, 26,
            SoundEvents.ARMOR_EQUIP_LEATHER,1f,0f, null),
    BASEFORM_LIGHTSPEED_SONIC("baseform_lightspeed",134217727,new int[]{1,1,1,1}, 26,
            SoundEvents.ARMOR_EQUIP_LEATHER,1f,0f, null),
    SUPERFORM_SONIC("superform",134217727,new int[]{5,7,5,4}, 26,
            SoundEvents.ARMOR_EQUIP_LEATHER,1f,0f, null),
    STARFALL_SONIC("starfall",134217727,new int[]{5,7,5,4}, 26,
            SoundEvents.ARMOR_EQUIP_LEATHER,1f,0f, null),
    HYPERFORM_SONIC("hyperform",134217727,new int[]{5,7,5,4}, 26,
            SoundEvents.ARMOR_EQUIP_LEATHER,1f,0f, null);

    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    private static final int[] BASE_DURABILITY = {11,16,16,13};

    ModArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantmentValue, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return BASE_DURABILITY[type.ordinal()]*this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return this.protectionAmounts[type.ordinal()];
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return (this.repairIngredient==null)?null:this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return BeyondTheHorizon.MOD_ID+":"+this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}

