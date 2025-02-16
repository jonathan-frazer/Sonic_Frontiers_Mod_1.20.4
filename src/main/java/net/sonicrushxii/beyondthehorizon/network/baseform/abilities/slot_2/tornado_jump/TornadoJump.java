package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.tornado_jump;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.ModUtils;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModEntityTypes;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;

import java.util.Objects;

public class TornadoJump {

    public TornadoJump() {

    }

    public TornadoJump(FriendlyByteBuf buffer){

    }

    public void encode(FriendlyByteBuf buffer){

    }

    public static void performTornadoJump(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
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
            Objects.requireNonNull(player.getAttribute(ForgeMod.ENTITY_GRAVITY.get())).setBaseValue(0.0);

            //Play Sound
            player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.TORNADO.get(), SoundSource.MASTER, 0.75f, 1.0f);

            PacketHandler.sendToALLPlayers(
                    new SyncPlayerFormS2C(
                            player.getId(),
                            playerSonicForm
                    ));
        });
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                    {
                        performTornadoJump(player);
                    }
                });
        ctx.setPacketHandled(true);
    }
}

