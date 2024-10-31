package net.sonicrushxii.beyondthehorizon.entities.baseform.mirage;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sonicrushxii.beyondthehorizon.entities.all.PointEntity;
import org.joml.Vector3f;

public class MirageCloud extends PointEntity
{
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

        if(this.level().isClientSide) {
            int color = 0;
            for (double t = 0.0; t <= 12.0; t+=1.0/19)
            {
                final double v0 = Math.sin((t%10)*Math.PI)*Math.cos(t/10*Math.PI)*4.5;
                final double v1 = Math.sin(t/10*Math.PI)*3;
                final double v2 = Math.cos((t%10)*Math.PI)*Math.cos(t/10*Math.PI)*4.5;

                double particleX = this.getX() + v0;
                double particleY = this.getY() + v1;
                double particleZ = this.getZ() + v2;

                this.level().addParticle(new DustParticleOptions(MirageCloud.colorSelect(color),2.0f),
                        false,
                        particleX, particleY, particleZ,
                        0, 0, 0);
                color = (color+1)%3;
            }
        }

        for(LivingEntity enemy : this.level().getEntitiesOfClass(LivingEntity.class,
                new AABB(this.getX()+7.0,this.getY()+7.0,this.getZ()+7.0,
                        this.getX()-7.0,this.getY()-7.0,this.getZ()-7.0),
                (entity)->{
                    try {
                        Player playerEntity = (Player)entity;
                        ItemStack headItem = playerEntity.getItemBySlot(EquipmentSlot.HEAD);
                        if (headItem.getItem() == Items.PLAYER_HEAD &&
                                headItem.getTag().getByte("BeyondTheHorizon") == (byte) 2)
                            return false;
                    }
                    catch (NullPointerException|ClassCastException ignored){}
                    return true;
                }))
        {
            Vec3 enemyPos = new Vec3(enemy.getX(),enemy.getY(),enemy.getZ());

            if(currentPos.distanceToSqr(enemyPos) < 6.0)
                return;

            Vec3 motionDir = currentPos.subtract(enemyPos)
                    .normalize()
                    .scale(Math.min(0.6, currentPos.distanceToSqr(enemyPos)));

            enemy.setDeltaMovement(motionDir);
        }
    }
}
