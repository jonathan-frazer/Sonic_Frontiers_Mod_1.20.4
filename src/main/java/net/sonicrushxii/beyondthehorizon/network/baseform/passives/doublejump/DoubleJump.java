package net.sonicrushxii.beyondthehorizon.network.baseform.passives.doublejump;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleAuraPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import org.joml.Vector3f;

public class DoubleJump {
    public DoubleJump() {}

    public DoubleJump(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }


    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null) {
                        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm->{
                            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                            //Modify Tags
                            baseformProperties.consumeDoubleJump();
                            PacketHandler.sendToPlayer(player,
                                    new SyncPlayerFormS2C(
                                            playerSonicForm.getCurrentForm(),
                                            playerSonicForm.getFormProperties()
                                    ));

                            //Thrust
                            player.jumpFromGround();
                            player.addDeltaMovement(new Vec3(0,0.135,0));
                            player.connection.send(new ClientboundSetEntityMotionPacket(player));

                            //Particle
                            PacketHandler.sendToPlayer(player,new ParticleAuraPacketS2C(
                                    new DustParticleOptions(new Vector3f(0.000f,0.969f,1.000f), 1.5f),
                                    0.0,0.0,0.0,
                                    0.0 ,0.2f,0.01f, 0.2f,20,true)
                            );


                            //PlaySound
                            Level world = player.level();
                            world.playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.DOUBLE_JUMP.get(), SoundSource.MASTER, 1.0f, 1.0f);

                        });
                    }
                });
        ctx.setPacketHandled(true);
    }
}

