package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.tornado_jump;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.ModUtils;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModEntityTypes;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;

import java.util.Objects;

public class TornadoJump implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<TornadoJump> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "tornado_jump"));

    public static final StreamCodec<FriendlyByteBuf, TornadoJump> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), TornadoJump::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public TornadoJump() {

    }

    public TornadoJump(FriendlyByteBuf buffer){

    }

    public void encode(FriendlyByteBuf buffer){

    }

    public static void performTornadoJump(ServerPlayer player)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

        //Set Motion
        player.setDeltaMovement(new Vec3(0,0,0));
        player.connection.send(new ClientboundSetEntityMotionPacket(player));

        //Modify Data
        baseformProperties.tornadoJump = 1;

        //Set Phase
        baseformProperties.atkRotPhase = -player.getYRot()-135f;
        final Vec3 playerPos = new Vec3(player.getX(),player.getY()+1,player.getZ());
        Scheduler.scheduleTask(()-> ModUtils.summonEntity(ModEntityTypes.TORNADO_JUMP_CLOUD.get(),
                player.serverLevel(),
                playerPos.add
                        (ModUtils.calculateViewVector(0,-baseformProperties.atkRotPhase+180).scale(1.4)),
                (aoeCloud) -> {
                    aoeCloud.setDuration(195);
                    aoeCloud.setOwner(player.getUUID());
                }),5);

        //Remove Gravity
        Objects.requireNonNull(player.getAttribute(Attributes.GRAVITY)).setBaseValue(0.0);

        //Play Sound
        player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.TORNADO.get(), SoundSource.MASTER, 0.75f, 1.0f);

        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));
    }

    public static void handle(TornadoJump msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null)
                    {
                        performTornadoJump(player);
                    }
                });
    }
}
