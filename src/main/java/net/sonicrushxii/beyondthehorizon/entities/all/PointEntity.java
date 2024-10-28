package net.sonicrushxii.beyondthehorizon.entities.all;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;

public class PointEntity extends Entity {
    protected int duration; // Time in ticks after which the entity disappears

    public PointEntity(EntityType<? extends PointEntity> type, Level world) {
        super(type, world);
        this.noCulling = true; // Prevents entity from being rendered (invisible)
        this.duration = 200; // Default duration (10 seconds at 20 ticks per second)
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Duration")) {
            this.duration = tag.getInt("Duration");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Duration", this.duration);
    }

    @Override
    public void tick() {
        super.tick();

        this.move(MoverType.SELF, this.getDeltaMovement());

        // Custom tick logic - Here, it just counts down the duration and removes the entity
        if (this.duration > 0) {
            this.duration--;
        } else {
            this.discard(); // Removes the entity from the world
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
