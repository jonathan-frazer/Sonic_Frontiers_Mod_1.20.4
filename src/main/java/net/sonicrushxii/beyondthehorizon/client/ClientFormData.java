package net.sonicrushxii.beyondthehorizon.client;

import net.minecraft.client.CameraType;
import net.sonicrushxii.beyondthehorizon.capabilities.SonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.all.FormProperties;

import java.util.UUID;

public class ClientFormData {
    private static SonicForm playerForm = SonicForm.PLAYER;
    private static FormProperties formData = new FormProperties();
    private static UUID homingAttackReticle = null;
    private static CameraType currentCam = null;

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

    public static void setHomingReticle(UUID enemyUUID) {homingAttackReticle = enemyUUID;}
    public static UUID hasHomingReticle() {return homingAttackReticle;}

    public static void storeCamPos(CameraType cameraType) {currentCam = cameraType;}
    public static CameraType getStoredCamPos() {return currentCam;}
    public static CameraType useStoredCamPos() {
        CameraType result = currentCam;
        currentCam = null;
        return result;
    }
}
