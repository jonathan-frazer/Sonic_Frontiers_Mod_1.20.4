package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.boost;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleAuraPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleRaycastPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import org.joml.Vector3f;

public class AirBoost {

    public AirBoost() {}

    public AirBoost(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    public static void performAirBoost(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm->{
            BaseformProperties baseformProperties =  (BaseformProperties) playerSonicForm.getFormProperties();
            if(baseformProperties.airBoosts > 0)
            {
                baseformProperties.airBoosts -= 1;
                //PlaySound
                Level world = player.level();
                world.playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.AIR_BOOST.get(), SoundSource.MASTER, 1.0f, 1.0f);


                //Particle
                Vec3 playerPosition = player.getPosition(0).add(new Vec3(0,0.75,0));
                PacketHandler.sendToALLPlayers(new ParticleRaycastPacketS2C(
                        new DustParticleOptions(new Vector3f(0.000f,0.000f,1.000f), 2.0f),
                        playerPosition,
                        playerPosition.add(player.getLookAngle().scale(2*player.getAttribute(Attributes.MOVEMENT_SPEED).getValue()))
                ));
                PacketHandler.sendToPlayer(player,new ParticleAuraPacketS2C(
                        ParticleTypes.SONIC_BOOM,
                        0.0,1.0,0.0,
                        0.0 ,0.0f,0.0f, 0.2f,1,true)
                );

                //Add Trajectory
                player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
                player.addDeltaMovement(player.getLookAngle());
                player.connection.send(new ClientboundSetEntityMotionPacket(player));
            }

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
                        performAirBoost(player);
                });
        ctx.setPacketHandled(true);
    }
}

