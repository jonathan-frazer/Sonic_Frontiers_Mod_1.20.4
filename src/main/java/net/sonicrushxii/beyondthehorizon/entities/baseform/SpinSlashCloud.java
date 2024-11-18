package net.sonicrushxii.beyondthehorizon.entities.baseform;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.entities.all.PointEntity;
import org.joml.Vector3f;

import java.util.List;

public class SpinSlashCloud extends PointEntity {
    public SpinSlashCloud(EntityType<? extends PointEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public void tick()
    {
        super.tick();
        if(this.level().isClientSide) {
            for (double theta = 0; theta <= 2*Math.PI; theta += Math.PI/24)
            {
                double particleX = this.getX() + Math.sin(theta)*3.0;
                double particleY = this.getY() + 1.0;
                double particleZ = this.getZ() + Math.cos(theta)*3.0;

                Utilities.displayParticle(this.level(),
                        ParticleTypes.SWEEP_ATTACK,
                        particleX,particleY,particleZ,
                        1.0f,0.1f,1.0f,
                        0.001, 1, false
                );
                Utilities.displayParticle(this.level(),
                        new DustParticleOptions(new Vector3f(0.259f,0.387f,1.00f),2f),
                        particleX,particleY,particleZ,
                        1.0f,0.1f,1.0f,
                        0.001, 2, false
                );

            }
        }

        //It requires a Baseform Sonic player to be next to the cloud or else it instantly disappears
        List<Player> sonicPlayers = this.level().getEntitiesOfClass(Player.class, new AABB(
                this.getX() + 8, this.getY() + 6, this.getZ() + 8,
                this.getX() - 8, this.getY() - 6, this.getZ() - 8),
                (playerEntity)->{
                    try {
                        ItemStack headItem = playerEntity.getItemBySlot(EquipmentSlot.HEAD);
                        if (headItem.getItem() == Items.PLAYER_HEAD &&
                                headItem.getTag().getByte("BeyondTheHorizon") == (byte) 2)
                            return true;
                    }
                    catch (NullPointerException|ClassCastException ignored){}
                    return false;
                });
        if (sonicPlayers.isEmpty()) this.kill();


        for(LivingEntity enemy : this.level().getEntitiesOfClass(LivingEntity.class,
                new AABB(this.getX()+3.5,this.getY()+3.5,this.getZ()+3.5,
                        this.getX()-3.5,this.getY()-3.5,this.getZ()-3.5),
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
            //Damage Enemy
            enemy.hurt(this.level().damageSources().generic(), 6.0f);
        }
    }
}