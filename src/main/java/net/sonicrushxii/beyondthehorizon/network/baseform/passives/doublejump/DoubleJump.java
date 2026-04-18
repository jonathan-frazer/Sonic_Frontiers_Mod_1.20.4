package net.sonicrushxii.beyondthehorizon.network.baseform.passives.doublejump;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleAuraPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import org.joml.Vector3f;

public class DoubleJump implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DoubleJump> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "double_jump"));

    public static final StreamCodec<FriendlyByteBuf, DoubleJump> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), DoubleJump::new);

    public DoubleJump() {}

    public DoubleJump(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static void handle(DoubleJump msg, IPayloadContext ctx){
        ctx.enqueueWork(()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null) {
                        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                        //Modify Tags
                        baseformProperties.hasDoubleJump = false;
                        PacketHandler.sendToALLPlayers(
                                new SyncPlayerFormS2C(
                                        player.getId(),
                                        playerSonicForm
                                ));

                        //Thrust
                        if (player.isSprinting()) {
                            // Directional dash in look direction when sprinting
                            Vec3 look = player.getLookAngle();
                            player.setDeltaMovement(look.x * 2.0, 0.3, look.z * 2.0);
                        } else {
                            player.jumpFromGround();
                            player.addDeltaMovement(new Vec3(0, 0.135, 0));
                        }
                        player.connection.send(new ClientboundSetEntityMotionPacket(player));

                        //Particle
                        PacketHandler.sendToPlayer(player,new ParticleAuraPacketS2C(
                                new DustParticleOptions(new Vector3f(0.000f,0.969f,1.000f), 1.5f),
                                player.getX(),player.getY(),player.getZ(),
                                0.0 ,0.2f,0.01f, 0.2f,20,true)
                        );

                        //PlaySound
                        Level world = player.level();
                        world.playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.DOUBLE_JUMP.get(), SoundSource.MASTER, 1.0f, 1.0f);

                    }
                });
    }
}