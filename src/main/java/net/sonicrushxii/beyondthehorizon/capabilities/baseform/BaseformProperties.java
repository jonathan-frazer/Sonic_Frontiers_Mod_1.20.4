package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import net.minecraft.nbt.CompoundTag;
import net.sonicrushxii.beyondthehorizon.capabilities.all.FormProperties;

public class BaseformProperties extends FormProperties {
    private byte[] abilityCooldowns;
    private boolean hasDoubleJump;

    public BaseformProperties()
    {
        abilityCooldowns = new byte[BaseformActiveAbility.values().length];
        hasDoubleJump = true;
    }

    public BaseformProperties(CompoundTag nbt)
    {
        //Cooldowns
        abilityCooldowns = nbt.getByteArray("AbilityCooldowns");

        //Sonic Info
        hasDoubleJump = nbt.getBoolean("hasDoubleJump");
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = new CompoundTag();
        //Cooldowns
        nbt.putByteArray("AbilityCooldowns",abilityCooldowns);

        //Sonic Info
        nbt.putBoolean("hasDoubleJump",hasDoubleJump);

        return nbt;
    }

    //Get Cooldowns
    public byte[] getAllCooldowns() {return abilityCooldowns;}

    //Double Jump
    public boolean hasDoubleJump() {return hasDoubleJump;}
    public void consumeDoubleJump() {hasDoubleJump=false;}
    public void restoreDoubleJump() {hasDoubleJump=true;}


}