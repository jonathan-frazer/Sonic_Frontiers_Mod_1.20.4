package net.sonicrushxii.beyondthehorizon.entities.all;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;

public class PointEntity extends Entity {
    public static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(PointEntity.class, EntityDataSerializers.INT);
    public PointEntity(EntityType<? extends PointEntity> type, Level world) {
        super(type, world);
        this.noCulling = true; // Prevents entity from being rendered (invisible)
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DURATION, 200);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Duration")) {
            this.entityData.set(DURATION,tag.getInt("Duration"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Duration", this.entityData.get(DURATION));
    }

    @Override
    public void tick() {
        super.tick();

        this.move(MoverType.SELF, this.getDeltaMovement());

        // Custom tick logic - Here, it just counts down the duration and removes the entity
        if (this.entityData.get(DURATION) > 0) {
            this.entityData.set(DURATION,this.entityData.get(DURATION)-1);
        } else if(!this.level().isClientSide) {
            this.kill(); // Removes the entity from the world
        }
    }

    public void setDuration(int duration)
    {
        this.entityData.set(DURATION,duration);
    }

    public int getDuration()
    {
        return this.entityData.get(DURATION);
    }
}
