package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.dodge;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;

public class Dodge {

    private final boolean dodgingRight;

    public Dodge(boolean dodgingRight) {
        this.dodgingRight = dodgingRight;
    }

    public Dodge(FriendlyByteBuf buffer){
        this.dodgingRight = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeBoolean(this.dodgingRight);
    }

    public static void performDodge(ServerPlayer player, boolean dodgingRight)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Get Invul Frames
            baseformProperties.dodgeInvul = true;
            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.0);

            Scheduler.scheduleTask(()->{
                baseformProperties.dodgeInvul = false;
                player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                PacketHandler.sendToPlayer(player,
                        new SyncPlayerFormS2C(
                                playerSonicForm.getCurrentForm(),
                                baseformProperties
                        ));
            },7);

            //Delta Movement
            player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
            Vec3 directionVector = player.getLookAngle().cross(new Vec3(0, (dodgingRight)?1:-1, 0));
            player.addDeltaMovement(directionVector.scale(2.0));
            player.connection.send(new ClientboundSetEntityMotionPacket(player));

            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            baseformProperties
                    ));
        });
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                    {
                        performDodge(player,dodgingRight);
                    }
                });
        ctx.setPacketHandled(true);
    }
}


