package net.sonicrushxii.beyondthehorizon.client;

import net.sonicrushxii.beyondthehorizon.capabilities.SonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.all.FormProperties;

import java.util.UUID;

public class ClientFormData {
    private static SonicForm playerForm = SonicForm.PLAYER;
    private static FormProperties formData = new FormProperties();

    public static void setPlayerForm(SonicForm playerForm, FormProperties formData){
        ClientFormData.playerForm = playerForm;
        ClientFormData.formData = formData;
    }

    public static SonicForm getPlayerForm(){
        return playerForm;
    }

    public static FormProperties getPlayerFormDetails(){
        return formData;
    }
}
