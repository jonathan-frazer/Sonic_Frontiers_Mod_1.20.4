package net.sonicrushxii.beyondthehorizon.entities.baseform.sonic_wave;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformServer;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.entities.all.LinearMovingEntity;
import net.sonicrushxii.beyondthehorizon.entities.all.PointEntity;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class SonicWaveEntity extends LinearMovingEntity {
    public static final EntityDataAccessor<Boolean> IS_STORM =
            SynchedEntityData.defineId(SonicWaveEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(SonicWaveEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    public SonicWaveEntity(EntityType<? extends PointEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_STORM, false);
        builder.define(OWNER, Optional.empty());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("IsStorm")) setIsStorm(tag.getBoolean("IsStorm"));
        if (tag.hasUUID("OwnerUUID")) setOwner(tag.getUUID("OwnerUUID"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsStorm", isStorm());
        UUID ownerUuid = getOwnerUUID();
        if (ownerUuid != null) tag.putUUID("OwnerUUID", ownerUuid);
    }

    public void setIsStorm(boolean storm) { this.entityData.set(IS_STORM, storm); }
    public boolean isStorm() { return this.entityData.get(IS_STORM); }

    public void setOwner(UUID uuid) { this.entityData.set(OWNER, Optional.of(uuid)); }

    @Nullable
    public UUID getOwnerUUID() { return this.entityData.get(OWNER).orElse(null); }

    @Nullable
    public LivingEntity getOwner() {
        UUID uuid = getOwnerUUID();
        if (uuid != null) return this.level().getPlayerByUUID(uuid);
        return null;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) return;

        // Storm mode: transition to wave when ground is reached
        if (isStorm() && (this.onGround() || this.verticalCollision)) {
            setIsStorm(false);
            setDuration(40); // 2 more seconds on ground
            Vec3 flat = new Vec3(movementDirection.x, 0, movementDirection.z).normalize().scale(1.5);
            setMovementDirection(flat);
            return;
        }

        // Wave mode: dissolve on wall collision
        if (!isStorm() && this.horizontalCollision) {
            this.kill();
            return;
        }

        // Damage + pull every 3 ticks
        if (this.tickCount % 3 == 0) {
            double hw = isStorm() ? 2.0 : 4.0; // wave has wider cone
            double hh = 2.0;
            float dmg = BaseformServer.SONIC_WIND_DAMAGE;
            LivingEntity owner = getOwner();
            if (owner instanceof ServerPlayer sp) {
                PlayerSonicForm psf = sp.getData(ModAttachments.PLAYER_SONIC_FORM);
                dmg += ((BaseformProperties) psf.getFormProperties()).boostLvl * 10.0f;
            }
            Vec3 pull = movementDirection.normalize().scale(1.2);
            for (LivingEntity enemy : this.level().getEntitiesOfClass(LivingEntity.class,
                    new AABB(getX() + hw, getY() + hh, getZ() + hw,
                             getX() - hw, getY() - hh, getZ() - hw),
                    e -> !e.is(this) && !e.is(owner))) {
                enemy.hurt(ModDamageTypes.getDamageSource(this.level(),
                        ModDamageTypes.SONIC_RANGED.getResourceKey(), owner), dmg);
                // Pull in wave direction
                enemy.setDeltaMovement(enemy.getDeltaMovement().add(pull));
            }
        }
    }
}
