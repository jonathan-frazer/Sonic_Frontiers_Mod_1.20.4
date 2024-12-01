package net.sonicrushxii.beyondthehorizon.entities.baseform.phantom_rush;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformServer;
import net.sonicrushxii.beyondthehorizon.entities.all.PointEntity;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;
import net.sonicrushxii.beyondthehorizon.modded.ModEntityTypes;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class PhantomRushCloud extends PointEntity {
    public static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(PhantomRushCloud.class, EntityDataSerializers.OPTIONAL_UUID);
    public static final EntityDataAccessor<Byte> PLAYER_TEXTURE_TYPE = SynchedEntityData.defineId(PhantomRushCloud.class, EntityDataSerializers.BYTE);
    private static final float RADIUS = 2.5F;

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER,Optional.empty());
        this.entityData.define(PLAYER_TEXTURE_TYPE,(byte)0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        // Load the owner's UUID
        if (tag.hasUUID("OwnerUUID")) this.setOwner(tag.getUUID("OwnerUUID"));
        if(tag.contains("PlayerTextureType")) setPlayerTextureType(tag.getByte("PlayerTextureType"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        // Save the owner's UUID
        super.addAdditionalSaveData(tag);
        UUID ownerUuid = getOwnerUUID();
        if (ownerUuid != null) tag.putUUID("OwnerUUID", ownerUuid);
        tag.putByte("PlayerTextureType", getPlayerTextureType());
    }

    // Sets the owner by UUID
    public void setOwner(UUID ownerUuid) {
        this.entityData.set(OWNER, Optional.of(ownerUuid));
    }

    // Retrieves the owner's UUID, if it exists
    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER).orElse(null);
    }

    // Gets the actual owner Entity, if they are loaded in the world
    @Nullable
    public LivingEntity getOwner() {
        UUID ownerUuid = getOwnerUUID();
        if (ownerUuid != null) return this.level().getPlayerByUUID(ownerUuid);
        return null;
    }

    public byte getPlayerTextureType() { return this.entityData.get(PLAYER_TEXTURE_TYPE); }

    public void setPlayerTextureType(byte playerTextureType) {
        this.entityData.set(PLAYER_TEXTURE_TYPE,playerTextureType );
    }

    public PhantomRushCloud(EntityType<? extends PointEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public void tick()
    {
        super.tick();

        Vec3 currentPos = new Vec3(this.getX(),this.getY(),this.getZ());

        if(this.level().isClientSide) {
            Utilities.displayParticle(this.level(),new DustParticleOptions(new Vector3f(0.4667F, 0.0F, 0.9961F), 2f),
                    this.getX(), this.getY()+1.80, this.getZ(),
                    RADIUS+2, 2.40f, RADIUS+2,
                    0,10,false
                    );
            Utilities.displayParticle(this.level(),new DustParticleOptions(new Vector3f(0.05F, 0.05F, 1.0F), 2f),
                    this.getX(), this.getY()+1.80, this.getZ(),
                    RADIUS+2, 2.40f, RADIUS+2,
                    0,10,false
            );
            Utilities.displayParticle(this.level(), ParticleTypes.CRIT,
                    this.getX(), this.getY()+1.80, this.getZ(),
                    RADIUS+2, 2.40f, RADIUS+2,
                    0,10,false
            );
        }
        else
        {
            //Set X,Y,Z Positions
            double theta = Utilities.random.nextDouble(0,2*Math.PI);
            double x = Utilities.random.nextDouble(RADIUS/2,RADIUS)*Math.sin(theta);
            double y = theta/2.0;
            double z = Utilities.random.nextDouble(RADIUS/2,RADIUS)*Math.cos(theta);

            //Spawn AfterImages
            PhantomRushEntity phantomRushEntity = new PhantomRushEntity(ModEntityTypes.BASEFORM_PHANTOM_RUSH_ENTITY.get(),this.level());
            phantomRushEntity.setPos(this.getX()+x, this.getY()+y, this.getZ()+z);
            phantomRushEntity.setDuration(3);
            phantomRushEntity.setPoseType((byte)(getPlayerTextureType()*10+this.getDuration()%5));
            phantomRushEntity.setYRot(Utilities.getYawPitchFromVec( (new Vec3(x,y,z)).reverse() )[0]);
            this.level().addFreshEntity(phantomRushEntity);
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
            //Enemy Positions
            Vec3 enemyPos = new Vec3(enemy.getX(),enemy.getY(),enemy.getZ());

            //Damage Enemy
            if(!enemy.isInvulnerable())
            enemy.hurt(
                        ModDamageTypes.getDamageSource(this.level(), ModDamageTypes.SONIC_ULTIMATE.getResourceKey(),this.getOwner()),
                        BaseformServer.PHANTOM_RUSH_DAMAGE
                );

            //Suck Enemies inward
            Vec3 motionDir = currentPos.subtract(enemyPos)
                    .normalize()
                    .scale(0.001);

            enemy.setDeltaMovement(new Vec3(motionDir.x(),0,motionDir.z()));
        }
    }
}
