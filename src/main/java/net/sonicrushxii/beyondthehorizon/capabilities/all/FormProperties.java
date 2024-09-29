package net.sonicrushxii.beyondthehorizon.capabilities.all;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

@NotNull
public class FormProperties {

    public FormProperties(){}

    public FormProperties(CompoundTag nbt){}

    public CompoundTag serialize()
    {
        return new CompoundTag();
    }
}
