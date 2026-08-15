package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_wind;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class QuickSonicWind implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<QuickSonicWind> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "quick_sonic_wind"));

    public static final StreamCodec<FriendlyByteBuf, QuickSonicWind> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), QuickSonicWind::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public QuickSonicWind() {}

    public QuickSonicWind(FriendlyByteBuf buffer){}

    public void encode(FriendlyByteBuf buffer){}

    public static void scanFoward(ServerPlayer player)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        if (!(playerSonicForm.getFormProperties() instanceof BaseformProperties baseformProperties)) return;

        //Get Position
        Vec3 currentPos = player.getPosition(0).add(0.0, 1.0, 0.0);
        Vec3 lookAngle = player.getLookAngle();

        baseformProperties.rangedTarget = new UUID(0L,0L);

        //Scan Forward for enemies
        for (int i = 0; i < 12; ++i) {
            //Increment Current Position Forward
            currentPos = currentPos.add(lookAngle);
            AABB boundingBox = new AABB(currentPos.x() - 4, currentPos.y() - 4, currentPos.z() - 4,
                    currentPos.x() + 4, currentPos.y() + 4, currentPos.z() + 4);

            List<LivingEntity> nearbyEntities = player.level().getEntitiesOfClass(
                    LivingEntity.class, boundingBox,
                    enemy -> {
                        if (enemy.is(player) || !enemy.isAlive()) return false;
                        return player.level().clip(new ClipContext(
                            player.getEyePosition(), enemy.getEyePosition(),
                            ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)
                        ).getType() != HitResult.Type.BLOCK;
                    });

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
    }

    public static void handle(QuickSonicWind msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null){
                        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                        if (!(playerSonicForm.getFormProperties() instanceof BaseformProperties baseformProperties)) return;

                        //Scan for targets
                        scanFoward(player);

                        //Cancel The Selection
                        if(baseformProperties.rangedTarget.equals(new UUID(0L,0L)))
                        {
                            Vec3 qSonicWindPos = player.getLookAngle().scale(10.0).add(new Vec3(player.getX(),player.getY(),player.getZ()));
                            baseformProperties.profanedWindCoords = new int[]{(int)qSonicWindPos.x(),(int)qSonicWindPos.y(),(int)qSonicWindPos.z()};
                        }
                        //If Target exists
                        else
                        {
                            //Get Target
                            LivingEntity qSonicWindTarget = (LivingEntity) player.serverLevel().getEntity(baseformProperties.rangedTarget);
                            //Reset target regardless — entity may have despawned between scan and handle
                            baseformProperties.rangedTarget = new UUID(0L, 0L);
                            if (qSonicWindTarget != null) {
                                baseformProperties.profanedWindCoords = new int[]{(int) qSonicWindTarget.getX(), (int) (qSonicWindTarget.getY()+qSonicWindTarget.getEyeHeight()/2), (int) qSonicWindTarget.getZ()};
                            } else {
                                Vec3 fallback = player.getLookAngle().scale(10.0).add(new Vec3(player.getX(),player.getY(),player.getZ()));
                                baseformProperties.profanedWindCoords = new int[]{(int)fallback.x(),(int)fallback.y(),(int)fallback.z()};
                            }
                        }

                        //Changed Data
                        baseformProperties.profanedWind = 1;

                        //Remove Gravity
                        var gravAttr = player.getAttribute(Attributes.GRAVITY);
                        if (gravAttr != null) gravAttr.setBaseValue(0.0);

                        //Set Motion to Zero
                        player.setDeltaMovement(0,0,0);
                        player.connection.send(new ClientboundSetEntityMotionPacket(player));

                        //Play Sound
                        player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.HOMING_ATTACK.get(), SoundSource.MASTER, 1.0f, 1.0f);

                        PacketHandler.sendToALLPlayers(
                                new SyncPlayerFormS2C(
                                        player.getId(),
                                        playerSonicForm
                                ));
                    }
                });
    }
}
