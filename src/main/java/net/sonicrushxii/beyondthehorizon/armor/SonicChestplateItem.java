package net.sonicrushxii.beyondthehorizon.armor;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.armor.client.renderer.ArmorRenderer;
import net.sonicrushxii.beyondthehorizon.modded.ModItems;
import org.jetbrains.annotations.Nullable;

public class SonicChestplateItem extends ModArmorItem{

    public SonicChestplateItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
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

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if(stack.is(ModItems.BASEFORM_CHESTPLATE.get())) return makeCustomTextureLocation(BeyondTheHorizon.MOD_ID,"baseform_sonic_armor");
        else if(stack.is(ModItems.BASEFORM_LIGHTSPEED_CHESTPLATE.get())) return makeCustomTextureLocation(BeyondTheHorizon.MOD_ID,"baseform_ls_sonic_armor");
        else return null;
    }
}
