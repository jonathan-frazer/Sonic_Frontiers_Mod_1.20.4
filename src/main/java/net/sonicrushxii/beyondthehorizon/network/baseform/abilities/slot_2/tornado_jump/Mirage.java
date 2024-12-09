package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.tornado_jump;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModEffects;
import net.sonicrushxii.beyondthehorizon.modded.ModEntityTypes;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;

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

            //Add Invisibility
            if(player.hasEffect(MobEffects.INVISIBILITY))
                player.getEffect(MobEffects.INVISIBILITY).update(new MobEffectInstance(MobEffects.INVISIBILITY, 140, 2, false, false));
            else
                player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 140, 2, false, false));

            //Confuse All Surrounding Entities
            for(LivingEntity mob : player.level().getEntitiesOfClass(LivingEntity.class,
                    new AABB(player.getX()+6.5,player.getY()+6.5,player.getZ()+6.5,
                            player.getX()-6.5,player.getY()-6.5,player.getZ()-6.5),
                    (entity)->!(entity instanceof Player)))
            {
                if(mob.hasEffect(ModEffects.MIRAGE_CONFUSE.get()))
                    mob.getEffect(ModEffects.MIRAGE_CONFUSE.get()).update(new MobEffectInstance(ModEffects.MIRAGE_CONFUSE.get(),140,2,false,false));
                else
                    mob.addEffect(new MobEffectInstance(ModEffects.MIRAGE_CONFUSE.get(),140,2,false,false));
            }

            //Set Phase
            baseformProperties.atkRotPhase = -player.getYRot()-135f;
            final Vec3 playerPos = new Vec3(player.getX(),player.getY()+1,player.getZ());
            Scheduler.scheduleTask(()->{
                Utilities.summonEntity(ModEntityTypes.MIRAGE_CLOUD.get(),
                        player.serverLevel(),
                        playerPos.add
                                (Utilities.calculateViewVector(0,-baseformProperties.atkRotPhase+240).scale(2.75)),
                        (mirageCloud) -> {
                            mirageCloud.setDuration(140);
                        });
            },7);

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
                        performMirageActivate(player);
                    }
                });
        ctx.setPacketHandled(true);
    }
}
