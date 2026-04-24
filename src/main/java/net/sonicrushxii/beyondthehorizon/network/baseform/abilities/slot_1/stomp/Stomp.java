package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.stomp;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformServer;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;
import net.sonicrushxii.beyondthehorizon.modded.ModEffects;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleAuraPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import org.joml.Vector3f;

public class Stomp implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<Stomp> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "stomp"));

    public static final StreamCodec<FriendlyByteBuf, Stomp> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), Stomp::new);

    private final byte variant;

    public Stomp() { this.variant = 0; }
    public Stomp(byte variant) { this.variant = variant; }

    public Stomp(FriendlyByteBuf buffer) { this.variant = buffer.readByte(); }

    public void encode(FriendlyByteBuf buffer) { buffer.writeByte(this.variant); }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void performEndStomp(ServerPlayer player)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
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
            enemy.hurt(ModDamageTypes.getDamageSource(player.level(),ModDamageTypes.SONIC_MELEE.getResourceKey(),player),
                    BaseformServer.STOMP_DAMAGE + baseformProperties.boostLvl * 10.0f);
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
    }

    public static void performUpdraft(ServerPlayer player) {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();
        baseformProperties.stomp = 1;

        for (LivingEntity enemy : player.level().getEntitiesOfClass(LivingEntity.class,
                new AABB(player.getX()+2.5, player.getY()+2.0, player.getZ()+2.5,
                         player.getX()-2.5, player.getY()-1.0, player.getZ()-2.5),
                target -> !target.is(player))) {
            enemy.hurt(ModDamageTypes.getDamageSource(player.level(),
                    ModDamageTypes.SONIC_MELEE.getResourceKey(), player), BaseformServer.STOMP_DAMAGE + baseformProperties.boostLvl * 10.0f);
            enemy.setDeltaMovement(0, 1.5, 0);
            player.connection.send(new ClientboundSetEntityMotionPacket(enemy));
            enemy.addEffect(new MobEffectInstance(ModEffects.COMBO_EFFECT, 30, 0, false, false));
        }

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.STOMP.get(), SoundSource.MASTER, 1.0f, 1.5f);
        PacketHandler.sendToALLPlayers(new SyncPlayerFormS2C(player.getId(), playerSonicForm));
    }

    public static void performSonicEagle(ServerPlayer player) {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();
        baseformProperties.stomp = 1;

        player.setDeltaMovement(0.0, -2.0, 0.0);
        player.connection.send(new ClientboundSetEntityMotionPacket(player));
        for (LivingEntity enemy : player.level().getEntitiesOfClass(LivingEntity.class,
                new AABB(player.getX()+2.5, player.getY()+1.0, player.getZ()+2.5,
                         player.getX()-2.5, player.getY()-4.0, player.getZ()-2.5),
                target -> !target.is(player))) {
            enemy.hurt(ModDamageTypes.getDamageSource(player.level(),
                    ModDamageTypes.SONIC_MELEE.getResourceKey(), player), BaseformServer.STOMP_DAMAGE + baseformProperties.boostLvl * 10.0f);
            enemy.setDeltaMovement(0, -5.0, 0);
            player.connection.send(new ClientboundSetEntityMotionPacket(enemy));
            enemy.addEffect(new MobEffectInstance(ModEffects.COMBO_EFFECT, 20, 0, false, false));
        }

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.STOMP.get(), SoundSource.MASTER, 1.0f, 0.8f);
        PacketHandler.sendToALLPlayers(new SyncPlayerFormS2C(player.getId(), playerSonicForm));
    }

    public static void handle(Stomp msg, IPayloadContext ctx){
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (player != null) {
                if (msg.variant == 1) performSonicEagle(player);
                else                  performUpdraft(player);
            }
        });
    }
}
