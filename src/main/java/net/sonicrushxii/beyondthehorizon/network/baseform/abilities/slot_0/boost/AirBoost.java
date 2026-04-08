package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.boost;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleAuraPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleRaycastPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import org.joml.Vector3f;

public class AirBoost implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AirBoost> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "air_boost"));

    public static final StreamCodec<FriendlyByteBuf, AirBoost> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), AirBoost::new);

    public AirBoost() {}

    public AirBoost(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void performAirBoost(ServerPlayer player)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
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
                    playerPosition.add(player.getLookAngle().scale(7*player.getAttribute(Attributes.MOVEMENT_SPEED).getValue()))
            ));
            PacketHandler.sendToPlayer(player,new ParticleAuraPacketS2C(
                    ParticleTypes.SONIC_BOOM,
                    player.getX(),player.getY()+1.0,player.getZ(),
                    0.0 ,0.0f,0.0f, 0.2f,1,true)
            );

            //Ballform
            baseformProperties.ballFormState = (byte)3;

            //Add Trajectory
            player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
            player.addDeltaMovement(player.getLookAngle().scale(2*player.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));
            player.connection.send(new ClientboundSetEntityMotionPacket(player));
        }

        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));
    }

    public static void handle(AirBoost msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null)
                        performAirBoost(player);
                });
    }
}

