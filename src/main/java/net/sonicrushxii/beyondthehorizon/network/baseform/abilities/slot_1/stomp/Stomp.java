package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.stomp;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformServer;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleAuraPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import org.joml.Vector3f;

public class Stomp {

    public Stomp() {}

    public Stomp(FriendlyByteBuf buffer) {}

    public void encode(FriendlyByteBuf buffer) {}

    public static void performEndStomp(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Add Data
            baseformProperties.stomp = 0;

            //Deal Damage
            Level world = player.level();
            for(LivingEntity enemy: world.getEntitiesOfClass(LivingEntity.class,
                    new AABB(player.getX()+3.0,player.getY()+1.0,player.getZ()+3.0,
                            player.getX()-3.0,player.getY()-4.0,player.getZ()+-3.0),
                    (target)->!target.is(player)))
            {
                //Damage Enemy
                enemy.hurt(ModDamageTypes.getDamageSource(player.level(),ModDamageTypes.SONIC_BALL.getResourceKey(),player),
                        BaseformServer.STOMP_DAMAGE);
            }

            //Particle
            PacketHandler.sendToPlayer(player,new ParticleAuraPacketS2C(
                    new DustParticleOptions(new Vector3f(0.000f,0.969f,1.000f), 1.5f),
                    player.getX(),player.getY()+0.2,player.getZ(),
                    0.0 ,3.0f,1.0f, 3.0f,100,true)
            );

            //Sound
            player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.STOMP.get(), SoundSource.MASTER, 1.0f, 1.0f);

            PacketHandler.sendToALLPlayers(
                    new SyncPlayerFormS2C(
                            player.getId(),
                            playerSonicForm
                    ));
        });
    }

    public static void performActivateStomp(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Add Data
            baseformProperties.stomp = 1;
            player.setDeltaMovement(0.0,-2.0,0.0);
            player.connection.send(new ClientboundSetEntityMotionPacket(player));

            //Bring Enemies Down with you
            for(LivingEntity enemy: player.level().getEntitiesOfClass(LivingEntity.class,
                    new AABB(player.getX()+2.5,player.getY()+1.0,player.getZ()+2.5,
                            player.getX()-2.5,player.getY()-4.0,player.getZ()-2.5),
                    (target)->!target.is(player)))
            {
                //Damage Enemy
                enemy.setDeltaMovement(0,-5.0,0);
                player.connection.send(new ClientboundSetEntityMotionPacket(enemy));
            }

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
                        performActivateStomp(player);
                    }
                });
        ctx.setPacketHandled(true);
    }
}
