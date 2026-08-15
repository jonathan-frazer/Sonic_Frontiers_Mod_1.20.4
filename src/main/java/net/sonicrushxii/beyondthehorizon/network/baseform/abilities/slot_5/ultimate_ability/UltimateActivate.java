package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_5.ultimate_ability;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformClient;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.event_handler.EquipmentChangeHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class UltimateActivate implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<UltimateActivate> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "ultimate_activate"));

    public static final StreamCodec<FriendlyByteBuf, UltimateActivate> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), UltimateActivate::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    private final UUID enemyID;

    public UltimateActivate(UUID enemyID) {
        if(enemyID.equals(new UUID(0L,0L))) this.enemyID = null;
        else                                                     this.enemyID = enemyID;
    }

    public UltimateActivate(FriendlyByteBuf buffer){
        UUID enemyID1;
        enemyID1 = buffer.readUUID();
        if(enemyID1.equals(new UUID(0L,0L)))
            enemyID1 = null;
        this.enemyID = enemyID1;
    }

    public void encode(FriendlyByteBuf buffer){
        if(enemyID==null) buffer.writeUUID(new UUID(0L,0L));
        else buffer.writeUUID(enemyID);
    }

    //Client-Side Method
    public static void scanFoward(Player player)
    {
        Vec3 currentPos = player.getPosition(0).add(0.0, 1.0, 0.0);
        Vec3 lookAngle = player.getLookAngle();

        //Scan Forward for enemies
        for (int i = 0; i < 10; ++i) {
            //Increment Current Position Forward
            currentPos = currentPos.add(lookAngle);
            AABB boundingBox = new AABB(currentPos.x() - 3, currentPos.y() - 3, currentPos.z() - 3,
                    currentPos.x() + 3, currentPos.y() + 3, currentPos.z() + 3);

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
                BaseformClient.ClientOnlyData.ultTargetReticle = Collections.min(nearbyEntities, (e1, e2) -> {
                    Vec3 e1Pos = new Vec3(e1.getX(), e1.getY(), e1.getZ());
                    Vec3 e2Pos = new Vec3(e2.getX(), e2.getY(), e2.getZ());

                    return (int) (e1Pos.distanceToSqr(player.getX(),player.getY(),player.getZ()) - e2Pos.distanceToSqr(player.getX(),player.getY(),player.getZ()));
                }).getUUID();
                break;
            }
        }
    }


    public static void handle(UltimateActivate msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null){
                        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                        if (!(playerSonicForm.getFormProperties() instanceof BaseformProperties baseformProperties)) return;

                        //Ultimate Target Reticle
                        if(msg.enemyID != null)
                        {
                            baseformProperties.ultimateUse = 1;
                            baseformProperties.ultimateCooldown = 300;
                            baseformProperties.ultReady = false;
                            baseformProperties.ultimateAtkMeter = 0.0;
                            baseformProperties.ultTarget = msg.enemyID;

                            //Attributes
                            var gravAttr = player.getAttribute(Attributes.GRAVITY);
                            if (gravAttr != null) gravAttr.setBaseValue(0.0);
                            var krAttr = player.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
                            if (krAttr != null) krAttr.setBaseValue(1.0);

                            //Deactivate PowerBoost
                            {
                                //Dequip Head
                                if(baseformProperties.lightSpeedState != (byte)2)
                                {
                                    Iterator<ItemStack> armorItems = player.getArmorSlots().iterator();
                                    armorItems.next(); armorItems.next(); armorItems.next();
                                    try{
                                        if(armorItems.next().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getByte("BeyondTheHorizon") == (byte) 2){
                                            EquipmentChangeHandler.playerHeadEquipmentLock.put(player.getUUID(),true);
                                            player.setItemSlot(EquipmentSlot.HEAD, BaseformProperties.baseformSonicHead);
                                        }
                                    }
                                    catch(NullPointerException ignored){}
                                }

                                //Power Boost
                                baseformProperties.powerBoost = false;
                            }

                            //Play Sound
                            player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.ULTIMATE_MUSIC.get(), SoundSource.MASTER, 1.0f, 1.0f);
                        }

                        PacketHandler.sendToALLPlayers(
                                new SyncPlayerFormS2C(
                                        player.getId(),
                                        playerSonicForm
                                ));
                    }
                });
    }
}
