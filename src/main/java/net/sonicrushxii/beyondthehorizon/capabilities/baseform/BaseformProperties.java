package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import net.minecraft.nbt.CompoundTag;
import net.sonicrushxii.beyondthehorizon.capabilities.all.FormProperties;

public class BaseformProperties extends FormProperties {
    private final byte MAX_SLOT_COUNT = (byte)10;
    private byte currAbilitySlot;

    private boolean hasDoubleJump;

    public BaseformProperties()
    {
        currAbilitySlot = 0;
        hasDoubleJump = true;
    }

    public BaseformProperties(CompoundTag nbt)
    {
        currAbilitySlot = nbt.getByte("currAbilitySlot");
        hasDoubleJump = nbt.getBoolean("hasDoubleJump");
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = new CompoundTag();
        //Current Slot
        nbt.putByte("currAbilitySlot",currAbilitySlot);

        nbt.putBoolean("hasDoubleJump",hasDoubleJump);
        return nbt;
    }

    //Virtual Slot Handling
    public byte getCurrAbilitySlot(){return currAbilitySlot;}
    public void incrementAbilitySlot(){
        currAbilitySlot = (byte) ((currAbilitySlot+1)%MAX_SLOT_COUNT);
    }
    public void decrementAbilitySlot(){
        if(currAbilitySlot == 0)  currAbilitySlot = (byte)(MAX_SLOT_COUNT-1);
        else currAbilitySlot = (byte)(currAbilitySlot-1);
    }

    //Double Jump
    public boolean hasDoubleJump() {return hasDoubleJump;}
    public void consumeDoubleJump() {hasDoubleJump=false;}
    public void restoreDoubleJump() {hasDoubleJump=true;}

}