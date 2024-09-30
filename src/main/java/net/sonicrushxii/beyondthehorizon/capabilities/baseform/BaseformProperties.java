package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import net.minecraft.nbt.CompoundTag;
import net.sonicrushxii.beyondthehorizon.capabilities.all.FormProperties;

public class BaseformProperties extends FormProperties {
    private byte[] abilityCooldowns;

    public boolean hasDoubleJump;
    public boolean sprintFlag;
    public boolean dangerSensePlaying;

    public BaseformProperties()
    {
        abilityCooldowns = new byte[BaseformActiveAbility.values().length];
        hasDoubleJump = true;
        sprintFlag = false;
        dangerSensePlaying = false;
    }

    public BaseformProperties(CompoundTag nbt)
    {
        //Cooldowns
        abilityCooldowns = nbt.getByteArray("AbilityCooldowns");

        //Sonic Info
        hasDoubleJump = nbt.getBoolean("hasDoubleJump");
        sprintFlag = nbt.getBoolean("isSprinting");
        dangerSensePlaying = nbt.getBoolean("dangerSensePlaying");
    }

    @Override
    public CompoundTag serialize()
    {
        CompoundTag nbt = new CompoundTag();

        //Cooldowns
        nbt.putByteArray("AbilityCooldowns",abilityCooldowns);

        //Sonic Info
        nbt.putBoolean("hasDoubleJump",hasDoubleJump);
        nbt.putBoolean("isSprinting",sprintFlag);
        nbt.putBoolean("dangerSensePlaying",dangerSensePlaying);

        return nbt;
    }

    //Get Cooldowns
    public byte[] getAllCooldowns() {return abilityCooldowns;}



}