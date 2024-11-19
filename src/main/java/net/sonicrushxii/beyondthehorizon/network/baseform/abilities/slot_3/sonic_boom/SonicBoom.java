package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_boom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Collections;
import java.util.List;

public class SonicBoom
{

    public SonicBoom() {

    }

    public SonicBoom(FriendlyByteBuf buffer){

    }

    public void encode(FriendlyByteBuf buffer){
    }

    //Server-Side Scan
    public static void scanFoward(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Get Position
            Vec3 currentPos = player.getPosition(0).add(0.0, 1.0, 0.0);
            Vec3 lookAngle = player.getLookAngle();

            baseformProperties.rangedTarget = null;

            //Scan Forward for enemies
            for (int i = 0; i < 12; ++i) {
                //Increment Current Position Forward
                currentPos = currentPos.add(lookAngle);
                AABB boundingBox = new AABB(currentPos.x() + 4, currentPos.y() + 4, currentPos.z() + 4,
                        currentPos.x() - 4, currentPos.y() - 4, currentPos.z() - 4);

                List<LivingEntity> nearbyEntities = player.level().getEntitiesOfClass(
                        LivingEntity.class, boundingBox,
                        (enemy) -> !enemy.is(player) && enemy.isAlive());

                //If enemy is found then Target it
                if (!nearbyEntities.isEmpty()) {
                    //Select Closest target
                    baseformProperties.rangedTarget = Collections.min(nearbyEntities, (e1, e2) -> {
                        Vec3 e1Pos = new Vec3(e1.getX(), e1.getY(), e1.getZ());
                        Vec3 e2Pos = new Vec3(e2.getX(), e2.getY(), e2.getZ());

                        return (int) (e1Pos.distanceToSqr(player.getX(),player.getY(),player.getZ()) - e2Pos.distanceToSqr(player.getX(),player.getY(),player.getZ()));
                    }).getUUID();
                    break;
                }
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
                        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
                            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                            //Start Cross Slash
                            scanFoward(player);

                            //Changed Data
                            baseformProperties.sonicBoom = 1;

                            //Remove Gravity
                            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.0);

                            //Play Sound
                            player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.HOMING_ATTACK.get(), SoundSource.MASTER, 1.0f, 1.0f);

                            PacketHandler.sendToPlayer(player,
                                    new SyncPlayerFormS2C(
                                            playerSonicForm.getCurrentForm(),
                                            baseformProperties
                                    ));
                        });
                    }
                });
        ctx.setPacketHandled(true);
    }
}