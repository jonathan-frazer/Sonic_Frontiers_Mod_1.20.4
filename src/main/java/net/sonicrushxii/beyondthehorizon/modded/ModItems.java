package net.sonicrushxii.beyondthehorizon.modded;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.armor.SonicChestplateItem;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, BeyondTheHorizon.MOD_ID);

    public static final Supplier<Item> BASEFORM_CHESTPLATE = ITEMS.register("sonic_baseform_armor_chestplate",
            ()-> new SonicChestplateItem(ModArmorMaterials.BASEFORM_SONIC, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final Supplier<Item> BASEFORM_POWERBOOST_CHESTPLATE = ITEMS.register("sonic_baseform_powerboost_chestplate",
            ()-> new SonicChestplateItem(ModArmorMaterials.BASEFORM_SONIC, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final Supplier<Item> BASEFORM_LEGGINGS = ITEMS.register("sonic_baseform_armor_leggings",
            ()-> new ArmorItem(ModArmorMaterials.BASEFORM_SONIC, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final Supplier<Item> BASEFORM_BOOTS = ITEMS.register("sonic_baseform_armor_boots",
            ()-> new ArmorItem(ModArmorMaterials.BASEFORM_SONIC, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final Supplier<Item> BASEFORM_LIGHTSPEED_CHESTPLATE = ITEMS.register("sonic_baseform_lightspeed_chestplate",
            ()-> new SonicChestplateItem(ModArmorMaterials.BASEFORM_LIGHTSPEED_SONIC, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final Supplier<Item> BASEFORM_LIGHTSPEED_LEGGINGS = ITEMS.register("sonic_baseform_lightspeed_leggings",
            ()-> new ArmorItem(ModArmorMaterials.BASEFORM_LIGHTSPEED_SONIC, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final Supplier<Item> BASEFORM_LIGHTSPEED_BOOTS = ITEMS.register("sonic_baseform_lightspeed_boots",
            ()-> new ArmorItem(ModArmorMaterials.BASEFORM_LIGHTSPEED_SONIC, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final Supplier<Item> SUPERFORM_CHESTPLATE = ITEMS.register("sonic_superform_armor_chestplate",
            ()-> new ArmorItem(ModArmorMaterials.SUPERFORM_SONIC, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final Supplier<Item> SUPERFORM_LEGGINGS = ITEMS.register("sonic_superform_armor_leggings",
            ()-> new ArmorItem(ModArmorMaterials.SUPERFORM_SONIC, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final Supplier<Item> SUPERFORM_BOOTS = ITEMS.register("sonic_superform_armor_boots",
            ()-> new ArmorItem(ModArmorMaterials.SUPERFORM_SONIC, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final Supplier<Item> STARFALL_CHESTPLATE = ITEMS.register("sonic_starfall_armor_chestplate",
            ()-> new ArmorItem(ModArmorMaterials.STARFALL_SONIC, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final Supplier<Item> STARFALL_LEGGINGS = ITEMS.register("sonic_starfall_armor_leggings",
            ()-> new ArmorItem(ModArmorMaterials.STARFALL_SONIC, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final Supplier<Item> STARFALL_BOOTS = ITEMS.register("sonic_starfall_armor_boots",
            ()-> new ArmorItem(ModArmorMaterials.STARFALL_SONIC, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final Supplier<Item> HYPERFORM_CHESTPLATE = ITEMS.register("sonic_hyperform_armor_chestplate",
            ()-> new ArmorItem(ModArmorMaterials.HYPERFORM_SONIC, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final Supplier<Item> HYPERFORM_LEGGINGS = ITEMS.register("sonic_hyperform_armor_leggings",
            ()-> new ArmorItem(ModArmorMaterials.HYPERFORM_SONIC, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final Supplier<Item> HYPERFORM_BOOTS = ITEMS.register("sonic_hyperform_armor_boots",
            ()-> new ArmorItem(ModArmorMaterials.HYPERFORM_SONIC, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static void register(IEventBus eventBus){ ITEMS.register(eventBus);}

}
