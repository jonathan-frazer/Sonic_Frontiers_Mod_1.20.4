package net.sonicrushxii.beyondthehorizon.capabilities.hyperform;

import net.minecraft.nbt.CompoundTag;
import net.sonicrushxii.beyondthehorizon.capabilities.all.FormProperties;

public class HyperformProperties extends FormProperties {
    public HyperformProperties(){
        super();
    }

    public HyperformProperties(CompoundTag nbt){
        super(nbt);
    }

    public CompoundTag serialize() {
        return new CompoundTag();
    }
}
