package net.sonicrushxii.beyondthehorizon.entities.all;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;

public class ParabolicMovingEntity extends PointEntity{

    public ParabolicMovingEntity(EntityType<? extends PointEntity> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();

        // Apply movement based on delta movement
        this.move(MoverType.SELF, this.getDeltaMovement());

        // Check for wall collision
        if (this.horizontalCollision) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
        }
        // Check for vertical collision
        if (this.verticalCollision && this.getDeltaMovement().y > 0) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0, this.getDeltaMovement().z);
        }

        if (!this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.08, 0));
        }
    }
}

