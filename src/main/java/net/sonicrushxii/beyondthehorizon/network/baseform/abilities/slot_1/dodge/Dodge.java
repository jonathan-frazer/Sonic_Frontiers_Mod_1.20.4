package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.dodge;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;

public class Dodge implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<Dodge> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "dodge"));

    public static final StreamCodec<FriendlyByteBuf, Dodge> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), Dodge::new);

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

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void performDodge(ServerPlayer player, boolean dodgingRight)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

        //Get Invul Frames
        baseformProperties.dodgeInvul = true;
        player.getAttribute(Attributes.GRAVITY).setBaseValue(0.0);

        Scheduler.scheduleTask(()->{
            baseformProperties.dodgeInvul = false;
            player.getAttribute(Attributes.GRAVITY).setBaseValue(0.08);
            PacketHandler.sendToALLPlayers(
                    new SyncPlayerFormS2C(
                            player.getId(),
                            playerSonicForm
                    ));
        },5);

        //Playsound
        player.level().playSound(null,player.getX(),player.getY(),player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.MASTER, 0.5f, 1f);

        //Delta Movement
        Vec3 directionVector = player.getLookAngle().cross(new Vec3(0, (dodgingRight)?1:-1, 0));
        player.setDeltaMovement(directionVector.scale(2.0));
        player.connection.send(new ClientboundSetEntityMotionPacket(player));

        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));
    }

    public static void handle(Dodge msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null)
                    {
                        performDodge(player,msg.dodgingRight);
                    }
                });
    }
}


