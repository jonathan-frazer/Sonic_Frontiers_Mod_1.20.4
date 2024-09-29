package net.sonicrushxii.beyondthehorizon.capabilities.superform;

import net.minecraft.nbt.CompoundTag;
import net.sonicrushxii.beyondthehorizon.capabilities.all.FormProperties;

public class SuperformProperties extends FormProperties {
    public SuperformProperties(){
        super();
    }

    public SuperformProperties(CompoundTag nbt){
        super(nbt);
    }

    public CompoundTag serialize() {
        return new CompoundTag();
    }
}
