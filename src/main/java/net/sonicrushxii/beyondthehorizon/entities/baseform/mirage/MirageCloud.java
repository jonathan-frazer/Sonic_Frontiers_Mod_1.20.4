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
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.base_cyloop.CyloopMath;
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
            for (int t = 10; t < 90; ++t)
            {
                final double v0 = (t) * Math.cos(t/1.5) / 40;
                final double v1 = (double)t/32;
                final double v2 = (t) * Math.sin(t/1.5) / 40;

                double particleY = this.getY() + v1;
                double particleX = this.getX() + v0;
                double particleZ = this.getZ() + v2;

                this.level().addParticle(new DustParticleOptions(colorSelect(t),2.0f),
                        false,
                        particleX, particleY, particleZ,
                        0, 0, 0);
            }
        }

        for(LivingEntity enemy : this.level().getEntitiesOfClass(LivingEntity.class,
                new AABB(this.getX()+10.0,this.getY()+10.0,this.getZ()+10.0,
                        this.getX()-10.0,this.getY()-10.0,this.getZ()-10.0),
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

            Vec3 motionDir = currentPos.subtract(enemyPos)
                    .normalize()
                    .scale(Math.min(0.8,5/ CyloopMath.xzDistSqr(currentPos,enemyPos)));

            if(CyloopMath.xzDistSqr(currentPos,enemyPos) < 3.0) {
                //Damage Enemy
                enemy.hurt(this.level().damageSources().generic(), 2.0f);
            }

            enemy.setDeltaMovement(new Vec3(motionDir.x(),0,motionDir.z()));
        }
    }
}
