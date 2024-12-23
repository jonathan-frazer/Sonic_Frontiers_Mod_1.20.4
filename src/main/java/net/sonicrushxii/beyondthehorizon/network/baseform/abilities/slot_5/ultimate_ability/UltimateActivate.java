package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_5.ultimate_ability;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformClient;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.event_handler.EquipmentChangeHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class UltimateActivate
{
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
            AABB boundingBox = new AABB(currentPos.x() + 3, currentPos.y() + 3, currentPos.z() + 3,
                    currentPos.x() - 3, currentPos.y() - 3, currentPos.z() - 3);

            List<LivingEntity> nearbyEntities = player.level().getEntitiesOfClass(
                    LivingEntity.class, boundingBox,
                    (enemy) -> !enemy.is(player) && enemy.isAlive());

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


    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null){
                        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
                            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                            //Ultimate Target Reticle
                            if(enemyID != null)
                            {
                                //Changed Data
                                baseformProperties.ultimateUse = 1;
                                baseformProperties.ultReady = false;
                                baseformProperties.ultimateAtkMeter = 0.0;
                                baseformProperties.ultTarget = enemyID;

                                //Attributes
                                player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.0);
                                player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);

                                //Deactivate PowerBoost
                                {
                                    //Dequip Head
                                    if(baseformProperties.lightSpeedState != (byte)2)
                                    {
                                        Iterator<ItemStack> armorItems = player.getArmorSlots().iterator();
                                        armorItems.next(); armorItems.next(); armorItems.next();
                                        try{
                                            if(armorItems.next().getTag().getByte("BeyondTheHorizon") == (byte) 2){
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
                        });
                    }
                });
        ctx.setPacketHandled(true);
    }
}
