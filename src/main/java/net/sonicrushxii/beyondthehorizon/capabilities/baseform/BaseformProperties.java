package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import net.minecraft.nbt.CompoundTag;
import net.sonicrushxii.beyondthehorizon.capabilities.all.FormProperties;

public class BaseformProperties extends FormProperties {
    private byte[] abilityCooldowns;

    //Passives
    public boolean hasDoubleJump;
    public boolean sprintFlag;
    public boolean dangerSensePlaying;

    //Slot 1
    public byte airBoosts;

    public BaseformProperties()
    {
        abilityCooldowns = new byte[BaseformActiveAbility.values().length];

        //Passives
        hasDoubleJump = true;
        sprintFlag = false;
        dangerSensePlaying = false;

        //Slot 1
        airBoosts = 3;
    }

    public BaseformProperties(CompoundTag nbt)
    {
        //Cooldowns
        abilityCooldowns = nbt.getByteArray("AbilityCooldowns");

        //Passives
        hasDoubleJump = nbt.getBoolean("hasDoubleJump");
        sprintFlag = nbt.getBoolean("isSprinting");
        dangerSensePlaying = nbt.getBoolean("dangerSensePlaying");

        //Slot 1
        airBoosts = nbt.getByte("AirBoosts");
    }

    @Override
    public CompoundTag serialize()
    {
        CompoundTag nbt = new CompoundTag();

        //Cooldowns
        nbt.putByteArray("AbilityCooldowns",abilityCooldowns);

        //Passives
        nbt.putBoolean("hasDoubleJump",hasDoubleJump);
        nbt.putBoolean("isSprinting",sprintFlag);
        nbt.putBoolean("dangerSensePlaying",dangerSensePlaying);

        //Slot 1
        nbt.putByte("AirBoosts",airBoosts);

        return nbt;
    }

    //Get Cooldowns
    public byte[] getAllCooldowns() {return abilityCooldowns;}



}