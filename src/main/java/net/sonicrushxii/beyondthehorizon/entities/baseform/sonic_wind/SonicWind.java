package net.sonicrushxii.beyondthehorizon.entities.baseform.sonic_wind;

import net.minecraft.core.particles.DustParticleOptions;
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
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.entities.all.LinearMovingEntity;
import net.sonicrushxii.beyondthehorizon.entities.all.PointEntity;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SonicWind extends LinearMovingEntity {
    public static final EntityDataAccessor<Boolean> DESTROY_BLOCKS = SynchedEntityData.defineId(SonicWind.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(SonicWind.class, EntityDataSerializers.OPTIONAL_UUID);
    private int MAX_DURATION = 160;
    private static float STRENGTH = 3.0f;
    private static float SONIC_WIND_DAMAGE = 18.0f;

    public SonicWind(EntityType<? extends PointEntity> type, Level world) {
        super(type, world);
    }

    @Override
    public void setDuration(int duration) {
        super.setDuration(duration);
        MAX_DURATION = duration;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DESTROY_BLOCKS, true);
        this.entityData.define(OWNER,Optional.empty());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        // Destroy Blocks
        if(tag.contains("DestroyBlocks")) setDestroyBlocks(tag.getBoolean("DestroyBlocks"));
        // Load the owner's UUID
        if (tag.hasUUID("OwnerUUID")) this.setOwner(tag.getUUID("OwnerUUID"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        // Save the owner's UUID
        super.addAdditionalSaveData(tag);
        UUID ownerUuid = getOwnerUUID();
        if (ownerUuid != null) tag.putUUID("OwnerUUID", ownerUuid);
        tag.putBoolean("DestroyBlocks", isDestroyBlocks());
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

    public boolean isDestroyBlocks() {
        return this.entityData.get(DESTROY_BLOCKS);
    }

    public void setDestroyBlocks(boolean destroyBlocks) {
        this.entityData.set(DESTROY_BLOCKS, destroyBlocks);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide) {
           for(float p = 0; p<= 10.0F ; p += 0.8F)
           {
               for(double offset = 0; offset <= 2*Math.PI; offset += Math.PI/2)
               {
                   double particleX = (0.2)*p*Math.sin(offset+p/10);
                   double particleZ = (0.2)*p*Math.cos(offset+p/10);

                   Utilities.displayParticle(this.level(),
                           new DustParticleOptions(new Vector3f(0.00f,0.0f,1f),1f),
                           this.getX()+particleX,this.getY(),this.getZ()+particleZ,
                           0.25f-(0.025f)*p,0.25f-(0.025f)*p,0.25f-(0.025f)*p,
                           0.001, 2, false
                   );
                   Utilities.displayParticle(this.level(),
                           new DustParticleOptions(new Vector3f(0.00f,1f,1f),1f),
                           this.getX()+particleX,this.getY(),this.getZ()+particleZ,
                           0.25f-(0.025f)*p,0.25f-(0.025f)*p,0.25f-(0.025f)*p,
                           0.001, 3, false
                   );
               }
           }
        }
        else{
            // Check for collision
            if(this.onGround() || this.horizontalCollision || this.verticalCollision && this.getDeltaMovement().y > 0)  explode();

            // Check for entity collisions and apply damage
            List<LivingEntity> enemies = this.level().getEntitiesOfClass(LivingEntity.class,
                    new AABB(this.getX() - 1.0, this.getY() - 1.0, this.getZ() - 1.0,
                            this.getX() + 1.0, this.getY() + 1.0, this.getZ() + 1.0),
                    enemy -> {
                        if(enemy.is(this)) return false;
                        try {
                            Player playerEntity = (Player)enemy;
                            ItemStack headItem = playerEntity.getItemBySlot(EquipmentSlot.HEAD);
                            if (headItem.getItem() == Items.PLAYER_HEAD &&
                                    headItem.getTag().getByte("BeyondTheHorizon") == (byte) 2)
                                return false;
                        }
                        catch (NullPointerException|ClassCastException ignored){}
                        return true;
                    }
            );
            if (!enemies.isEmpty() && this.getDuration() < this.MAX_DURATION-4) {
                try {// Synchronize on server only
                    for (LivingEntity enemy : enemies) {
                        enemy.hurt(
                                ModDamageTypes.getDamageSource(this.level(), ModDamageTypes.SONIC_RANGED.getResourceKey(), this.getOwner()),
                                SONIC_WIND_DAMAGE
                        );
                    }
                }catch(NullPointerException ignored){}
                explode();
            }
        }
    }

    private void explode()
    {
        this.kill();
        if(this.isDestroyBlocks() && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING))
        {
            this.level().explode(
                    getOwner(),
                    this.getX(), this.getY(), this.getZ(),
                    STRENGTH,
                    /* Causes fire */ false,
                    Level.ExplosionInteraction.TNT
            );
        }
        else
        {
            //Deal Damage and Use Particle Effects
            this.level().explode(
                    /* Exploder (null if no specific entity causes it) */ getOwner(),
                    /* Center x, y, z positions */ this.getX(), this.getY(), this.getZ(),
                    /* Strength */ STRENGTH,
                    /* Causes fire */ false,
                    /* Block Interaction Mode */ Level.ExplosionInteraction.NONE
            );
        }
    }
}
