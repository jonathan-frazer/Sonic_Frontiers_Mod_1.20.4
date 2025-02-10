package net.sonicrushxii.beyondthehorizon.modded;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.armor.SonicChestplateItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BeyondTheHorizon.MOD_ID);

    public static final RegistryObject<Item> BASEFORM_CHESTPLATE = ITEMS.register("sonic_baseform_armor_chestplate",
            ()-> new SonicChestplateItem(ModArmorMaterials.BASEFORM_SONIC, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> BASEFORM_LEGGINGS = ITEMS.register("sonic_baseform_armor_leggings",
            ()-> new ArmorItem(ModArmorMaterials.BASEFORM_SONIC, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> BASEFORM_BOOTS = ITEMS.register("sonic_baseform_armor_boots",
            ()-> new ArmorItem(ModArmorMaterials.BASEFORM_SONIC, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> BASEFORM_LIGHTSPEED_CHESTPLATE = ITEMS.register("sonic_baseform_lightspeed_chestplate",
            ()-> new SonicChestplateItem(ModArmorMaterials.BASEFORM_LIGHTSPEED_SONIC, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> BASEFORM_LIGHTSPEED_LEGGINGS = ITEMS.register("sonic_baseform_lightspeed_leggings",
            ()-> new ArmorItem(ModArmorMaterials.BASEFORM_LIGHTSPEED_SONIC, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> BASEFORM_LIGHTSPEED_BOOTS = ITEMS.register("sonic_baseform_lightspeed_boots",
            ()-> new ArmorItem(ModArmorMaterials.BASEFORM_LIGHTSPEED_SONIC, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> SUPERFORM_CHESTPLATE = ITEMS.register("sonic_superform_armor_chestplate",
            ()-> new ArmorItem(ModArmorMaterials.SUPERFORM_SONIC, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> SUPERFORM_LEGGINGS = ITEMS.register("sonic_superform_armor_leggings",
            ()-> new ArmorItem(ModArmorMaterials.SUPERFORM_SONIC, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> SUPERFORM_BOOTS = ITEMS.register("sonic_superform_armor_boots",
            ()-> new ArmorItem(ModArmorMaterials.SUPERFORM_SONIC, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> STARFALL_CHESTPLATE = ITEMS.register("sonic_starfall_armor_chestplate",
            ()-> new ArmorItem(ModArmorMaterials.STARFALL_SONIC, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> STARFALL_LEGGINGS = ITEMS.register("sonic_starfall_armor_leggings",
            ()-> new ArmorItem(ModArmorMaterials.STARFALL_SONIC, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> STARFALL_BOOTS = ITEMS.register("sonic_starfall_armor_boots",
            ()-> new ArmorItem(ModArmorMaterials.STARFALL_SONIC, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> HYPERFORM_CHESTPLATE = ITEMS.register("sonic_hyperform_armor_chestplate",
            ()-> new ArmorItem(ModArmorMaterials.HYPERFORM_SONIC, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> HYPERFORM_LEGGINGS = ITEMS.register("sonic_hyperform_armor_leggings",
            ()-> new ArmorItem(ModArmorMaterials.HYPERFORM_SONIC, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> HYPERFORM_BOOTS = ITEMS.register("sonic_hyperform_armor_boots",
            ()-> new ArmorItem(ModArmorMaterials.HYPERFORM_SONIC, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static void register(IEventBus eventBus){ ITEMS.register(eventBus);}

}
