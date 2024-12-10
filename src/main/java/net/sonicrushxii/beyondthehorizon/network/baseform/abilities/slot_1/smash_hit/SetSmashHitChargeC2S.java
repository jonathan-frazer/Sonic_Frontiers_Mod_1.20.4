package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.smash_hit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;


public class SetSmashHitChargeC2S {

    private final byte newChargeAmt;

    public SetSmashHitChargeC2S(byte newChargeAmt) {
        this.newChargeAmt = newChargeAmt;
    }

    public SetSmashHitChargeC2S(FriendlyByteBuf buffer) {
        this.newChargeAmt = buffer.readByte();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeByte(this.newChargeAmt);
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                    {
                        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
                            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                            if(this.newChargeAmt == 0 && player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.SMASH_HIT))
                                player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.SMASH_HIT.getId());
                            if(this.newChargeAmt > 0 && !player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.SMASH_HIT))
                                player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(AttributeMultipliers.SMASH_HIT);

                            baseformProperties.smashHit = this.newChargeAmt;

                            PacketHandler.sendToALLPlayers(
                                    new SyncPlayerFormS2C(
                                            player.getId(),
                                            playerSonicForm
                                    ));
                        });
                    }
                });
        ctx.setPacketHandled(true);
    }
}
