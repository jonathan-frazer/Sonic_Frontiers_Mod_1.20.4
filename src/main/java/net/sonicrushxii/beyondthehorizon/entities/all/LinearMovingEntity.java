package net.sonicrushxii.beyondthehorizon.entities.all;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class LinearMovingEntity extends PointEntity {
    public static final EntityDataAccessor<Vector3f> MOVEMENT_DIR = SynchedEntityData.defineId(LinearMovingEntity.class, EntityDataSerializers.VECTOR3);


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MOVEMENT_DIR,new Vector3f(0f,0f,0f));
    }
    public void setMovementDirection(Vector3f movementDirection) {
        this.entityData.set(MOVEMENT_DIR,movementDirection);
    }
    public Vector3f getMovementDirection() {
        return this.entityData.get(MOVEMENT_DIR);
    }

    public LinearMovingEntity(EntityType<? extends PointEntity> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(new Vec3(this.entityData.get(MOVEMENT_DIR)));
        if(this.level().isClientSide)
            this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0, 0, 0);
    }
}
