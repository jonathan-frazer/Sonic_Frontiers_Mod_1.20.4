package net.sonicrushxii.beyondthehorizon.entities.baseform;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.sonicrushxii.beyondthehorizon.ModUtils;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformServer;
import net.sonicrushxii.beyondthehorizon.entities.all.PointEntity;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpinSlashCloud extends PointEntity {
    public SpinSlashCloud(EntityType<? extends PointEntity> entityType, Level world) {
        super(entityType, world);
    }

    public static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(SpinSlashCloud.class, EntityDataSerializers.OPTIONAL_UUID);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER,Optional.empty());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        // Load the owner's UUID
        if (tag.hasUUID("OwnerUUID")) this.setOwner(tag.getUUID("OwnerUUID"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        // Save the owner's UUID
        super.addAdditionalSaveData(tag);
        UUID ownerUuid = getOwnerUUID();
        if (ownerUuid != null) tag.putUUID("OwnerUUID", ownerUuid);
    }

    // Sets the owner by UUID
    public void setOwner(UUID ownerUuid) {
        this.entityData.set(OWNER, Optional.of(ownerUuid));
    }

    // Retrieves the owner's UUID, if it exists
    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER).orElse(null);
    }

    // Gets the actual owner Entity, if they are loaded in the world
    @Nullable
    public LivingEntity getOwner() {
        UUID ownerUuid = getOwnerUUID();
        if (ownerUuid != null) return this.level().getPlayerByUUID(ownerUuid);
        return null;
    }

    @Override
    public void tick()
    {
        super.tick();
        if(this.level().isClientSide) {
            for (double theta = 0; theta <= 2*Math.PI; theta += Math.PI/24)
            {
                double particleX = this.getX() + Math.sin(theta)*3.0;
                double particleY = this.getY() + 1.0;
                double particleZ = this.getZ() + Math.cos(theta)*3.0;

                ModUtils.displayParticle(this.level(),
                        ParticleTypes.SWEEP_ATTACK,
                        particleX,particleY,particleZ,
                        1.0f,0.1f,1.0f,
                        0.001, 1, false
                );
                ModUtils.displayParticle(this.level(),
                        new DustParticleOptions(new Vector3f(0.259f,0.387f,1.00f),2f),
                        particleX,particleY,particleZ,
                        1.0f,0.1f,1.0f,
                        0.001, 2, false
                );

            }
        }

        //It requires a Baseform Sonic player to be next to the cloud or else it instantly disappears
        List<Player> sonicPlayers = this.level().getEntitiesOfClass(Player.class, new AABB(
                this.getX() + 8, this.getY() + 6, this.getZ() + 8,
                this.getX() - 8, this.getY() - 6, this.getZ() - 8),
                (playerEntity)->{
                    try {
                        ItemStack headItem = playerEntity.getItemBySlot(EquipmentSlot.HEAD);
                        if (headItem.getItem() == Items.PLAYER_HEAD) {
                            assert headItem.getTag() != null;
                            if (headItem.getTag().getByte("BeyondTheHorizon") == (byte) 2) return true;
                        }
                    }
                    catch (NullPointerException|ClassCastException ignored){}
                    return false;
                });
        if (sonicPlayers.isEmpty()) this.kill();


        for(LivingEntity enemy : this.level().getEntitiesOfClass(LivingEntity.class,
                new AABB(this.getX()+3.5,this.getY()+3.5,this.getZ()+3.5,
                        this.getX()-3.5,this.getY()-3.5,this.getZ()-3.5),
                (entity)->{
                    try {
                        Player playerEntity = (Player)entity;
                        ItemStack headItem = playerEntity.getItemBySlot(EquipmentSlot.HEAD);
                        if (headItem.getItem() == Items.PLAYER_HEAD) {
                            assert headItem.getTag() != null;
                            if (headItem.getTag().getByte("BeyondTheHorizon") == (byte) 2) return false;
                        }
                    }
                    catch (NullPointerException|ClassCastException ignored){}
                    return true;
                }))
        {
            //Damage Enemy
            enemy.hurt(
                    ModDamageTypes.getDamageSource(this.level(), ModDamageTypes.SONIC_RANGED.getResourceKey(),this.getOwner()),
                    BaseformServer.SPINSLASH_DAMAGE
            );
        }
    }
}