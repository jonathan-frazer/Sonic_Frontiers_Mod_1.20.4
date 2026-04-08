package net.sonicrushxii.beyondthehorizon.armor;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.sonicrushxii.beyondthehorizon.armor.client.renderer.ArmorRenderer;
import org.jetbrains.annotations.NotNull;

public abstract class ModArmorItem extends ArmorItem {
    private static final String FULL_SET_ID = "hadFullSet";
    protected int fullSetTick = 0;

    public ModArmorItem(Holder<ArmorMaterial> pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    public boolean equals(ArmorItem item) {
        return this == item || item.getMaterial() == this.getMaterial();
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotID, boolean isSelected) {
        if (entity instanceof LivingEntity living) {
            CompoundTag tag = living.getPersistentData();
            if (this.isFullSetActive(living)) {
                if (this.getEquipmentSlot() == EquipmentSlot.CHEST) {
                    if (this.fullSetTick == 0) {
                        if (!level.isClientSide) this.initFullSetTick(stack, level, living);
                        tag.putBoolean(FULL_SET_ID, true);
                        tag.putString("lastFullSet", this.getMaterial().getRegisteredName());
                    }
                    this.fullSetTick++;
                    if (level.isClientSide) {
                        this.clientFullSetTick(stack, level, living);
                    } else {
                        this.fullSetTick(stack, level, living);
                    }
                }
            } else {
                if (living.getPersistentData().getBoolean(FULL_SET_ID)) {
                    if (!level.isClientSide) postFullSetTick(stack, level, living);
                    tag.putBoolean(FULL_SET_ID, false);
                }
                fullSetTick = 0;
            }
        }
    }

    public boolean isFullSetActive(LivingEntity living) {
        return isFullSetActive(living, this.getMaterial());
    }

    public static boolean isFullSetActive(LivingEntity living, Holder<ArmorMaterial> materials) {
        if (living == null) {
            return false;
        }
        ArmorItem head = living.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ArmorItem armorItem ? armorItem : null;
        Item chestPlate = living.getItemBySlot(EquipmentSlot.CHEST).getItem();
        ArmorItem chest;
        if (chestPlate instanceof ElytraItem || chestPlate instanceof AirItem) {
            return false;
        } else {
            chest = (ArmorItem) living.getItemBySlot(EquipmentSlot.CHEST).getItem();
        }
        ArmorItem legs = living.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ArmorItem armorItem ? armorItem : null;
        ArmorItem feet = living.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ArmorItem armorItem ? armorItem : null;
        return (head != null && legs != null && feet != null) && (head.getMaterial() == materials && chest.getMaterial() == materials && legs.getMaterial() == materials && feet.getMaterial() == materials);
    }

    protected void fullSetTick(ItemStack stack, Level level, LivingEntity living) {}
    protected void initFullSetTick(ItemStack stack, Level level, LivingEntity living) {}
    protected void postFullSetTick(ItemStack stack, Level level, LivingEntity living) {}
    protected void clientFullSetTick(ItemStack stack, Level level, LivingEntity living) {}

    // display / model START

    protected abstract boolean withCustomModel();
    protected ArmorRenderer<?> getRenderer(LivingEntity living, ItemStack stack, EquipmentSlot slot) { return null;}

    public static String makeCustomTextureLocation(String nameSpace, String id) {
        return ResourceLocation.fromNamespaceAndPath(nameSpace, "textures/models/armor/custom/" + id + ".png").toString();
    }

    // display / model END
}
