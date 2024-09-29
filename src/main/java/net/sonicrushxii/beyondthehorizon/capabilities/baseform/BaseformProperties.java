package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import net.minecraft.nbt.CompoundTag;
import net.sonicrushxii.beyondthehorizon.capabilities.all.FormProperties;

public class BaseformProperties extends FormProperties {
    public boolean hasDoubleJump;

    public BaseformProperties()
    {
        super();
        hasDoubleJump = false;
    }

    public BaseformProperties(CompoundTag nbt)
    {
        hasDoubleJump = nbt.getBoolean("hasDoubleJump");
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("hasDoubleJump",hasDoubleJump);
        return nbt;
    }
}