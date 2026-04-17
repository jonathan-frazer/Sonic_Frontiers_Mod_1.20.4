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

    // direction: 0 = left, 1 = right, 2 = backward
    private final byte direction;

    public Dodge(boolean dodgingRight) {
        this.direction = dodgingRight ? (byte)1 : (byte)0;
    }

    // Backward dodge constructor
    public Dodge(Object ignored) {
        this.direction = 2;
    }

    public Dodge(FriendlyByteBuf buffer){
        this.direction = buffer.readByte();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeByte(this.direction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void performDodge(ServerPlayer player, byte direction)
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
        Vec3 dodgeVector;
        if (direction == 2) {
            // Backward dodge: opposite of look direction
            Vec3 look = player.getLookAngle();
            dodgeVector = new Vec3(-look.x, 0, -look.z).normalize().scale(2.0);
        } else {
            dodgeVector = player.getLookAngle().cross(new Vec3(0, (direction == 1) ? 1 : -1, 0)).scale(2.0);
        }
        player.setDeltaMovement(dodgeVector);
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
                        performDodge(player, msg.direction);
                    }
                });
    }
}


