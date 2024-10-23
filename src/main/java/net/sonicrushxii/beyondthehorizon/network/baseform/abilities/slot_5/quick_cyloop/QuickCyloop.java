package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_5.quick_cyloop;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_5.base_cyloop.CyloopParticleS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Collections;
import java.util.List;

public class QuickCyloop {

    public QuickCyloop() {}

    public QuickCyloop(FriendlyByteBuf buffer){}

    public void encode(FriendlyByteBuf buffer){}

    //Server-Side Method
    public static LivingEntity scanFoward(ServerPlayer player)
    {
        Vec3 currentPos = player.getPosition(0).add(0.0, 1.0, 0.0);
        Vec3 lookAngle = player.getLookAngle();

        //Scan Forward for enemies
        for (int i = 0; i < 6; ++i)
        {
            //Increment Current Position Forward
            currentPos = currentPos.add(lookAngle);
            AABB boundingBox = new AABB(currentPos.x() + 3, currentPos.y() + 3, currentPos.z() + 3,
                    currentPos.x() - 3, currentPos.y() - 3, currentPos.z() - 3);

            List<LivingEntity> nearbyEntities = player.level().getEntitiesOfClass(
                    LivingEntity.class, boundingBox,
                    (enemy) -> !enemy.is(player) && enemy.isAlive());

            //If enemy is found then Target it
            if (!nearbyEntities.isEmpty())
            {
                //Select Closest target
                return Collections.min(nearbyEntities, (e1, e2) -> {
                    Vec3 e1Pos = new Vec3(e1.getX(), e1.getY(), e1.getZ());
                    Vec3 e2Pos = new Vec3(e2.getX(), e2.getY(), e2.getZ());

                    return (int) (e1Pos.distanceToSqr(player.getX(),player.getY(),player.getZ()) - e2Pos.distanceToSqr(player.getX(),player.getY(),player.getZ()));
                });
            }
        }

        return null;
    }

    public static void performQkCyloop(ServerPlayer player)
    {

        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            LivingEntity enemy = scanFoward(player);

            System.out.print("Scan Enemies: ");
            if(enemy == null)
                return;

            //Activate Cyloop
            baseformProperties.quickCyloop = 1;
            baseformProperties.qkCyloopPhase = -player.getYRot()-135f;
            System.out.println(baseformProperties.qkCyloopPhase);

            //Target Enemy
            baseformProperties.qkCyloopTarget = enemy.getUUID();

            //Get Right in Front of the enemy
            Vec3 lookAngle = Utilities.calculateViewVector(0f, player.getYRot());

            double destX, destY, destZ;
            destX = enemy.getX() - lookAngle.x * 1.1;
            destY = enemy.getY();
            destZ = enemy.getZ() - lookAngle.z * 1.1;

            player.teleportTo(destX, destY, destZ);
            PacketHandler.sendToALLPlayers(new CyloopParticleS2C(new Vec3(destX, destY, destZ)));
            player.connection.send(new ClientboundTeleportEntityPacket(player));

            System.out.println("Start Qk Cyloop");

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
                        performQkCyloop(player);
                    }
                });
        ctx.setPacketHandled(true);
    }
}


