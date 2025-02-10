package net.sonicrushxii.beyondthehorizon.armor.client.renderer;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.entity.EquipmentSlot;
import net.sonicrushxii.beyondthehorizon.armor.client.model.ArmorModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * the armor renderer for the {@link ArmorModel}
 */
public class ArmorRenderer<T extends ArmorModel> {
    private final T model;
    public ArmorRenderer(Supplier<LayerDefinition> supplier, Function<ModelPart, T> modelConstructor) {
        this.model = modelConstructor.apply(supplier.get().bakeRoot());
    }

    public ArmorRenderer<T> makeInvisible(boolean invisible) {
        this.model.makeInvisible(invisible);
        return this;
    }

    private static final ModelPart EMPTY_PART = new ModelPart(Collections.emptyList(), Collections.emptyMap());

    public ModelPart makeArmorParts(EquipmentSlot slot) {
        return new ModelPart(Collections.emptyList(),
                Map.of(
                        "head", slot == EquipmentSlot.HEAD ? checkNonNull(model.armorHead) : EMPTY_PART,
                        "hat", EMPTY_PART,
                        "body", slot == EquipmentSlot.CHEST ? checkNonNull(model.armorChest) : EMPTY_PART,
                        "right_arm", slot == EquipmentSlot.CHEST ? checkNonNull(model.armorRightArm) : EMPTY_PART,
                        "left_arm", slot == EquipmentSlot.CHEST ? checkNonNull(model.armorLeftArm) : EMPTY_PART,
                        "right_leg", slot == EquipmentSlot.FEET ? checkNonNull(model.armorRightBoot) :
                                slot == EquipmentSlot.LEGS ? checkNonNull(model.armorRightLeg) : EMPTY_PART,
                        "left_leg", slot == EquipmentSlot.FEET ? checkNonNull(model.armorLeftBoot) :
                                slot == EquipmentSlot.LEGS ? checkNonNull(model.armorLeftLeg) : EMPTY_PART
                )
        );
    }
    private static @NotNull ModelPart checkNonNull(@Nullable ModelPart part) {
        return part == null ? EMPTY_PART : part;
    }
}