package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.tornado_jump;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModEntityTypes;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleAuraPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;
import org.joml.Vector3f;

public class Mirage {
    public Mirage() {

    }

    public Mirage(FriendlyByteBuf buffer){

    }

    public void encode(FriendlyByteBuf buffer){

    }

    public static void performMirageActivate(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            if(baseformProperties.mirageTimer > 0)
                return;

            //Set Motion
            player.setDeltaMovement(new Vec3(0,0,0));
            player.setPos(player.getX(),player.getY(),player.getZ());
            player.connection.send(new ClientboundSetEntityMotionPacket(player));

            //Modify Data
            baseformProperties.mirageTimer = 1;

            //Set Phase
            baseformProperties.atkRotPhase = -player.getYRot()-135f;
            final Vec3 playerPos = new Vec3(player.getX(),player.getY()+1,player.getZ());
            Scheduler.scheduleTask(()->{
                PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                        new DustParticleOptions(new Vector3f(1.0f, 0.0f, 0.0f), 1.5f),
                        playerPos.x(),
                        playerPos.y(),
                        playerPos.z(),
                        0.0, 1.55f, 1.55f, 1.55f, 10,
                        true)
                );
                Utilities.summonEntity(ModEntityTypes.MIRAGE_CLOUD.get(),
                        player.serverLevel(),
                        playerPos.add
                                (Utilities.calculateViewVector(0,-baseformProperties.atkRotPhase+240).scale(2.75)),
                        (mirageCloud) -> {
                            mirageCloud.setDuration(140);
                        });
            },7);


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
                        performMirageActivate(player);
                    }
                });
        ctx.setPacketHandled(true);
    }
}
