package net.sonicrushxii.beyondthehorizon.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.sonicrushxii.beyondthehorizon.capabilities.all.FormProperties;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.capabilities.hyperform.HyperformProperties;
import net.sonicrushxii.beyondthehorizon.capabilities.starfall.StarfallFormProperties;
import net.sonicrushxii.beyondthehorizon.capabilities.superform.SuperformProperties;

import java.util.Objects;

public class PlayerSonicForm {
    private SonicForm currentForm;
    private FormProperties formProperties;

    public PlayerSonicForm()
    {
        currentForm = SonicForm.PLAYER;
        formProperties = new FormProperties();
    }

    public SonicForm getCurrentForm(){
        return currentForm;
    }

    public FormProperties getFormProperties() { return formProperties; }

    public void activateBaseForm(){
        if(currentForm == SonicForm.BASEFORM)
            return;
        currentForm = SonicForm.BASEFORM;
        formProperties = new BaseformProperties();
    }

    public void activateSuperForm(){
        if(currentForm == SonicForm.SUPERFORM)
            return;
        currentForm = SonicForm.SUPERFORM;
        formProperties = new SuperformProperties();
    }

    public void activateStarfallForm(){
        if(currentForm == SonicForm.STARFALLFORM)
            return;
        currentForm = SonicForm.STARFALLFORM;
        formProperties = new StarfallFormProperties();
    }

    public void activateHyperForm(){
        if(currentForm == SonicForm.HYPERFORM)
            return;
        currentForm = SonicForm.HYPERFORM;
        formProperties = new HyperformProperties();
    }

    public void deactivateBaseForm(){
        if(currentForm != SonicForm.BASEFORM)
            return;
        currentForm = SonicForm.PLAYER;
        formProperties = new FormProperties();
    }

    public void deactivateSuperForm(){
        if(currentForm != SonicForm.SUPERFORM)
            return;
        currentForm = SonicForm.PLAYER;
        formProperties = new FormProperties();;
    }

    public void deactivateStarfallForm(){
        if(currentForm != SonicForm.STARFALLFORM)
            return;
        currentForm = SonicForm.PLAYER;
        formProperties = new FormProperties();;
    }

    public void deactivateHyperForm(){
        if(currentForm != SonicForm.HYPERFORM)
            return;
        currentForm = SonicForm.PLAYER;
        formProperties = new FormProperties();;
    }

    public void copyFrom(PlayerSonicForm source){
        this.currentForm = source.currentForm;
        this.formProperties = source.formProperties;
    }

    public void saveNBTData(CompoundTag nbt){
        //Copy Current Form
        nbt.putByte("currentForm", (byte)currentForm.ordinal());
        nbt.put("sonicProperties", formProperties.serialize());
    }

    public void loadNBTData(CompoundTag nbt){
        //Copy Current Form
        currentForm = SonicForm.values()[nbt.getByte("currentForm")];

        CompoundTag formDetails = nbt.getCompound("sonicProperties");

        formProperties = switch(currentForm){
            case PLAYER -> new FormProperties(formDetails);
            case BASEFORM -> new BaseformProperties(formDetails);
            case SUPERFORM -> new SuperformProperties(formDetails);
            case STARFALLFORM -> new StarfallFormProperties(formDetails);
            case HYPERFORM -> new HyperformProperties(formDetails);
        };
    }
}
