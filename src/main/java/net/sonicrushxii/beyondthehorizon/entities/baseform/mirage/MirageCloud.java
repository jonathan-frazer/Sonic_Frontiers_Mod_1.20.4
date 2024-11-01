package net.sonicrushxii.beyondthehorizon.entities.baseform.mirage;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.entities.all.PointEntity;
import net.sonicrushxii.beyondthehorizon.modded.ModEntityTypes;
import org.joml.Vector3f;

public class MirageCloud extends PointEntity
{
    private static final double RADIUS = 6.0;

    public MirageCloud(EntityType<? extends PointEntity> entityType, Level world) {
        super(entityType, world);
    }

    public static Vector3f colorSelect(int t)
    {
        return switch (t % 3) {
            case 0 -> new Vector3f(0.047f, 0.306f, 0.788f);
            case 1 -> new Vector3f(0.0f, 0.663f, 0.969f);
            default -> new Vector3f(0.0f, 0.963f, 1.0f);
        };
    }

    @Override
    public void tick()
    {
        super.tick();
        Vec3 currentPos = new Vec3(this.getX(),this.getY(),this.getZ());

        if(this.level().isClientSide)
        {
            int color = 0;
            Vec3[] positions = {new Vec3(-3,0,8),
                                new Vec3(4,5,9),
                                new Vec3(11,1,3),
                                new Vec3(-7,5,4),
                                new Vec3(7,7,9),
                                new Vec3(-9,4,-3),
                                new Vec3(-7,1,-3),
                                new Vec3(-3,4,8),
                                new Vec3(4,6,-5),
                                new Vec3(5,1,-6)};

            for(int i=0;i<positions.length;++i)
            {
                //Display to all other points
                for(int j=0;j<positions.length;++j)
                {
                    if(i!=j) {
                        //Display Particle
                        Utilities.particleRaycast(this.level(),
                                new DustParticleOptions(colorSelect(i),1.15f),
                                currentPos.add(positions[i]),currentPos.add(positions[j]));
                    }
                }
            }
        }
        else if(this.getDuration()%3 == 0)
        {
            //Set X,Y,Z Positions
            double theta = Utilities.random.nextDouble(0,2*Math.PI);
            double x = RADIUS*Math.sin(theta);
            double y = theta/2.0;
            double z = RADIUS*Math.cos(theta);

            //Spawn AfterImages
            MirageEntity sonicMirage = new MirageEntity(ModEntityTypes.SONIC_BASEFORM_MIRAGE.get(),this.level());
            sonicMirage.setPos(this.getX()+x, this.getY()+y, this.getZ()+z);
            sonicMirage.setDuration(10);
            sonicMirage.setYRot(Utilities.getYawPitchFromVec( (new Vec3(x,y,z)).reverse() )[0]);
            this.level().addFreshEntity(sonicMirage);
        }
    }

    @Override
    public void kill() {
        super.kill();
        //Kill All the Mirages Around
        for(MirageEntity sonicMirage : this.level().getEntitiesOfClass(MirageEntity.class, new AABB(
                this.getX()+16,this.getY()+16,this.getZ()+16,
                this.getX()-16,this.getY()-16,this.getZ()-16)))
            sonicMirage.remove(RemovalReason.KILLED);
    }
}
