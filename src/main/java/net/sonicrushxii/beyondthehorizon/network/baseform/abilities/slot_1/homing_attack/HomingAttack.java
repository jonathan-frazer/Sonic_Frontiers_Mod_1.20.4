package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.homing_attack;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.List;
import java.util.UUID;

public class HomingAttack
{
    public HomingAttack() {    }

    public HomingAttack(FriendlyByteBuf buffer){    }

    public void encode(FriendlyByteBuf buffer){    }

    public static void performHomingAttack(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            Vec3 currentPos = player.getPosition(0).add(0.0,1.0,0.0);
            Vec3 lookAngle = player.getLookAngle();

            UUID enemyID = null;

            //Scan Forward for enemies
            for(int i=0;i<10;++i)
            {
                //Increment Current Position Forward
                currentPos = currentPos.add(lookAngle);
                AABB boundingBox = new AABB(currentPos.x()+3,currentPos.y()+3,currentPos.z()+3,
                        currentPos.x()-3,currentPos.y()-3,currentPos.z()-3);

                List<LivingEntity> nearbyEntities = player.level().getEntitiesOfClass(
                        LivingEntity.class, boundingBox,
                        (enemy) -> !enemy.is(player));

                //If enemy is found then Target it
                if(!nearbyEntities.isEmpty())
                {
                    //Target Acquired
                    enemyID = nearbyEntities.get(0).getUUID();
                    break;
                }
            }

            //Start Homing Attack
            if(enemyID != null)
            {
                //Homing Attack Data
                baseformProperties.homingAttackAirTime = 1;
                baseformProperties.selectiveInvul = true;
                baseformProperties.homingTarget = enemyID;

                //Remove Gravity
                player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.0);

                //Play Sound
                player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.HOMING_ATTACK.get(), SoundSource.MASTER, 1.0f, 1.0f);
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
                    if(player != null){
                        performHomingAttack(player);
                    }
                });
        ctx.setPacketHandled(true);
    }
}
