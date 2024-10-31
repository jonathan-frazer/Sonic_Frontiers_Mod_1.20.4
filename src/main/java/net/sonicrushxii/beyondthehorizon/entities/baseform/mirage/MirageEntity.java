package net.sonicrushxii.beyondthehorizon.entities.baseform.mirage;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.sonicrushxii.beyondthehorizon.entities.all.PointEntity;

public class MirageEntity extends PointEntity {
    public MirageEntity(EntityType<? extends PointEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public void tick()
    {
        super.tick();
    }
}
