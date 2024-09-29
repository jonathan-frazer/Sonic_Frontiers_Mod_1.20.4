package net.sonicrushxii.beyondthehorizon.network.sync;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.SonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.all.FormProperties;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.capabilities.hyperform.HyperformProperties;
import net.sonicrushxii.beyondthehorizon.capabilities.starfall.StarfallFormProperties;
import net.sonicrushxii.beyondthehorizon.capabilities.superform.SuperformProperties;
import net.sonicrushxii.beyondthehorizon.client.ClientFormData;

public class SyncPlayerFormS2C {
    private final SonicForm playerForm;
    private final FormProperties formData;

    public SyncPlayerFormS2C(SonicForm playerForm, FormProperties formData) {
        this.playerForm = playerForm;
        this.formData = formData;
    }

    public SyncPlayerFormS2C(FriendlyByteBuf buffer) {
        this.playerForm = buffer.readEnum(SonicForm.class);
        CompoundTag nbtData = buffer.readNbt();
        assert nbtData != null;
        this.formData = switch(this.playerForm){
            case PLAYER -> new FormProperties(nbtData);
            case BASEFORM -> new BaseformProperties(nbtData);
            case SUPERFORM -> new SuperformProperties(nbtData);
            case STARFALLFORM -> new StarfallFormProperties(nbtData);
            case HYPERFORM -> new HyperformProperties(nbtData);
        };
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeEnum(this.playerForm);
        buffer.writeNbt(this.formData.serialize());
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(()->{
            //On Client Side
            ClientFormData.setPlayerForm(this.playerForm,this.formData);
        });
        ctx.setPacketHandled(true);
    }
}

