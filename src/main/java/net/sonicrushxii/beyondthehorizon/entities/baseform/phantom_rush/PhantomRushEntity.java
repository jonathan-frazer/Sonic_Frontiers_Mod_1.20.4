package net.sonicrushxii.beyondthehorizon.entities.baseform.phantom_rush;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.sonicrushxii.beyondthehorizon.entities.all.PointEntity;

public class PhantomRushEntity extends PointEntity {
    public static final EntityDataAccessor<Byte> POSE_TYPE = SynchedEntityData.defineId(PhantomRushEntity.class, EntityDataSerializers.BYTE);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(POSE_TYPE, (byte)0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("PoseType")) setPoseType(tag.getByte("PoseType"));

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        // Save the owner's UUID
        super.addAdditionalSaveData(tag);
        tag.putByte("PoseType", getPoseType());
    }

    public byte getPoseType() { return this.entityData.get(POSE_TYPE); }

    public void setPoseType(byte poseType) {
        this.entityData.set(POSE_TYPE, poseType);
    }

    public PhantomRushEntity(EntityType<? extends PointEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public void tick()
    {
        super.tick();
    }
    
}
