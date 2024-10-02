package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import net.minecraft.nbt.CompoundTag;
import net.sonicrushxii.beyondthehorizon.capabilities.all.FormProperties;

public class BaseformProperties extends FormProperties {
    private final byte[] abilityCooldowns;

    //Passives
    public boolean hasDoubleJump;
    public boolean sprintFlag;
    public boolean dangerSenseActive;
    public boolean dangerSensePlaying;

    //Slot 1
    public byte airBoosts;
    public byte boostLvl;
    public boolean isWaterBoosting;
    public byte lightSpeedState;
    public boolean powerBoost;

    public BaseformProperties()
    {
        abilityCooldowns = new byte[BaseformActiveAbility.values().length];

        //Passives
        hasDoubleJump = true;
        sprintFlag = false;
        dangerSenseActive = true;
        dangerSensePlaying = false;

        //Slot 1
        airBoosts = 3;
        boostLvl = 0;
        isWaterBoosting = false;
        lightSpeedState = 0;
        powerBoost = false;
    }

    public BaseformProperties(CompoundTag nbt)
    {
        //Cooldowns
        abilityCooldowns = nbt.getByteArray("AbilityCooldowns");

        //Passives
        hasDoubleJump = nbt.getBoolean("hasDoubleJump");
        sprintFlag = nbt.getBoolean("isSprinting");
        dangerSenseActive = nbt.getBoolean("dangerSenseActive");
        dangerSensePlaying = nbt.getBoolean("dangerSensePlaying");

        //Slot 1
        airBoosts = nbt.getByte("AirBoosts");
        boostLvl = nbt.getByte("BoostLvl");
        isWaterBoosting = nbt.getBoolean("IsWaterBoosting");
        lightSpeedState = nbt.getByte("LightSpeedState");
        powerBoost = nbt.getBoolean("PowerBoost");
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
        nbt.putBoolean("dangerSenseActive",dangerSenseActive);
        nbt.putBoolean("dangerSensePlaying",dangerSensePlaying);

        //Slot 1
        nbt.putByte("AirBoosts",airBoosts);
        nbt.putByte("BoostLvl",boostLvl);
        nbt.putBoolean("IsWaterBoosting",isWaterBoosting);
        nbt.putByte("LightSpeedState",lightSpeedState);
        nbt.putBoolean("PowerBoost",powerBoost);

        return nbt;
    }

    //Get Cooldowns
    public byte[] getAllCooldowns() {return abilityCooldowns;}
    public byte getCooldown(BaseformActiveAbility ability){return abilityCooldowns[ability.ordinal()];}
    public void setCooldown(BaseformActiveAbility ability, byte seconds){abilityCooldowns[ability.ordinal()] = seconds;}
}