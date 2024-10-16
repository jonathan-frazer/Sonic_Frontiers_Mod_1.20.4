package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.stomp;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleAuraPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import org.joml.Vector3f;

public class Stomp {
    private static final float STOMP_DAMAGE=30.0f;

    public Stomp() {}

    public Stomp(FriendlyByteBuf buffer) {}

    public void encode(FriendlyByteBuf buffer) {}

    public static void performEndStomp(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Add Data
            baseformProperties.stomp = 0;
            if (player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).hasModifier(AttributeMultipliers.STOMP_GRAVITY))
                player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).removeModifier(AttributeMultipliers.STOMP_GRAVITY.getId());

            //Deal Damage
            Level world = player.level();
            for(LivingEntity enemy: world.getEntitiesOfClass(LivingEntity.class,
                    new AABB(player.getX()+3.0,player.getY()+1.0,player.getZ()+3.0,
                            player.getX()-3.0,player.getY()-1.0,player.getZ()+-3.0),
                    (target)->!target.is(player)))
            {
                //Damage Enemy
                enemy.hurt(ModDamageTypes.getDamageSource(player.level(),ModDamageTypes.SONIC_MELEE.getResourceKey(),player),
                        STOMP_DAMAGE);
            }

            //Particle
            PacketHandler.sendToPlayer(player,new ParticleAuraPacketS2C(
                    new DustParticleOptions(new Vector3f(0.000f,0.969f,1.000f), 1.5f),
                    player.getX(),player.getY()+0.2,player.getZ(),
                    0.0 ,3.0f,0.10f, 3.0f,100,true)
            );

            //Sound
            player.level().playSound(null,player.getX(),player.getY(),player.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.MASTER, 0.95f, 0.5f);

            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            baseformProperties
                    ));
        });
    }

    public static void performActivateStomp(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Add Data
            baseformProperties.stomp = 1;
            if (!player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).hasModifier(AttributeMultipliers.STOMP_GRAVITY))
                player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).addTransientModifier(AttributeMultipliers.STOMP_GRAVITY);

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
                        performActivateStomp(player);
                    }
                });
        ctx.setPacketHandled(true);
    }
}
