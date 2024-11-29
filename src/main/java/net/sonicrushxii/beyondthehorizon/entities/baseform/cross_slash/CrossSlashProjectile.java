package net.sonicrushxii.beyondthehorizon.entities.baseform.cross_slash;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformServer;
import net.sonicrushxii.beyondthehorizon.entities.all.LinearMovingEntity;
import net.sonicrushxii.beyondthehorizon.entities.all.PointEntity;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CrossSlashProjectile extends LinearMovingEntity {
    public static final EntityDataAccessor<Boolean> DESTROY_BLOCKS = SynchedEntityData.defineId(CrossSlashProjectile.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(CrossSlashProjectile.class, EntityDataSerializers.OPTIONAL_UUID);
    private int MAX_DURATION = 50;
    private static final float STRENGTH = 0.5f;

    public CrossSlashProjectile(EntityType<? extends PointEntity> type, Level world) {
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
            Vec3 lookDir = Utilities.calculateViewVector(this.getXRot(),this.getYRot());
            Utilities.displayParticle(this.level(),
                    ParticleTypes.FIREWORK,
                    this.getX()+lookDir.x(),this.getY()+lookDir.y(),this.getZ()+lookDir.z(),
                    0.1f,0.1f,0.1f,
                    0.001, 1, false
            );
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
                                BaseformServer.CROSS_SLASH_DAMAGE
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

        Vec3 blockDirection = movementDirection.scale(2);

        //Break Blocks
        BlockPos start = this.blockPosition();
        BlockPos end = this.blockPosition().offset(
                (int) Math.ceil(blockDirection.x()),
                (int) Math.ceil(blockDirection.y()),
                (int) Math.ceil(blockDirection.z())
        );

        // Use BlockPos.betweenClosed to iterate over all positions in the cube
        for (BlockPos pos : BlockPos.betweenClosed(start, end)) {
            BlockState blockState = this.level().getBlockState(pos);
            if(!Utilities.unbreakableBlocks.contains(ForgeRegistries.BLOCKS.getKey(blockState.getBlock())+""))
                this.level().destroyBlock(pos,true);
        }

        /*
        if(this.isDestroyBlocks() && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING))
        {
            this.level().explode(
                    getOwner(),
                    this.getX(), this.getY(), this.getZ(),
                    STRENGTH,
                    false,
                    Level.ExplosionInteraction.TNT
            );
        }
        else
        {
            //Deal Damage and Use Particle Effects
            this.level().explode(
                    getOwner(),
                     this.getX(), this.getY(), this.getZ(),
                    STRENGTH,
                    false,
                    Level.ExplosionInteraction.NONE
            );
        }*/
    }
}
