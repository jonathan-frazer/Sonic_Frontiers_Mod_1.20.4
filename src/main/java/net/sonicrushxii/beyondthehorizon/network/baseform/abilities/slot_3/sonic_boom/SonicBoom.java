package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_boom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.PlayerPlaySoundPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class SonicBoom implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<SonicBoom> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "sonic_boom"));

    public static final StreamCodec<FriendlyByteBuf, SonicBoom> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), SonicBoom::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public SonicBoom() {}

    public SonicBoom(FriendlyByteBuf buffer){}

    public void encode(FriendlyByteBuf buffer){}
/*
    //Server-Side Scan
    public static void scanFoward(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Get Position
            Vec3 currentPos = player.getPosition(0).add(0.0, 1.0, 0.0);
            Vec3 lookAngle = player.getLookAngle();

            baseformProperties.rangedTarget = new UUID(0L,0L);

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
*/


    public static void handle(SonicBoom msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null){
                        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                        //Changed Data
                        baseformProperties.sonicBoom = 1;

                        //Remove Gravity
                        player.getAttribute(NeoForgeMod.ENTITY_GRAVITY).setBaseValue(0.0);

                        //Play Sound
                        PacketHandler.sendToALLPlayers(new PlayerPlaySoundPacketS2C(
                                player.blockPosition(),
                                ModSounds.SONIC_BOOM.get().getLocation())
                        );

                        PacketHandler.sendToALLPlayers(
                                new SyncPlayerFormS2C(
                                        player.getId(),
                                        playerSonicForm
                                ));
                    }
                });
    }
}
