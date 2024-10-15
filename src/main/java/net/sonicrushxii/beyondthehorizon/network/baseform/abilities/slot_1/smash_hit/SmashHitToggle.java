package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.smash_hit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;


public class SmashHitToggle {

    private boolean keyDown;
    public SmashHitToggle(boolean keyDown) {
        this.keyDown = keyDown;
    }

    public SmashHitToggle(FriendlyByteBuf buffer) {
        this.keyDown = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.keyDown);
    }

    public static void performStartSmashHit(ServerPlayer player)
    {
        if (!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.SMASH_HIT))
            player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(AttributeMultipliers.SMASH_HIT);

    }
    public static void performStopSmashHit(ServerPlayer player)
    {
        if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.SMASH_HIT))
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.SMASH_HIT.getId());

    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                    {
                        if(keyDown) performStartSmashHit(player);
                        else    performStopSmashHit(player);
                    }
                });
        ctx.setPacketHandled(true);
    }
}
