package net.sonicrushxii.beyondthehorizon.capabilities.starfall;

import net.minecraft.nbt.CompoundTag;
import net.sonicrushxii.beyondthehorizon.capabilities.all.FormProperties;

public class StarfallFormProperties extends FormProperties {
    public StarfallFormProperties(){
        super();
    }

    public StarfallFormProperties(CompoundTag nbt){
        super(nbt);
    }

    public CompoundTag serialize() {
        return new CompoundTag();
    }
}
