package net.sonicrushxii.beyondthehorizon.armor;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.sonicrushxii.beyondthehorizon.armor.client.renderer.ArmorRenderer;

public class SonicChestplateItem extends ModArmorItem{

    public SonicChestplateItem(Holder<ArmorMaterial> pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    protected boolean withCustomModel() {
        return true;
    }

    @Override
    protected ArmorRenderer<?> getRenderer(LivingEntity living, ItemStack stack, EquipmentSlot slot) {
        return new ArmorRenderer<>(net.sonicrushxii.beyondthehorizon.armor.client.model.SonicArmorModel::createBodyLayer, net.sonicrushxii.beyondthehorizon.armor.client.model.SonicArmorModel::new);
    }

    // TODO: In NeoForge 1.21.1, armor textures are resolved via ArmorMaterial.Layer instead of getArmorTexture().
    // Custom per-item textures (baseform vs powerboost vs lightspeed) need to use separate ArmorMaterial registrations
    // or a custom armor rendering layer. The texture paths are defined in ModArmorMaterials via Layer.
}
