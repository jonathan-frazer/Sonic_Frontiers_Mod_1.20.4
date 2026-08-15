package net.sonicrushxii.beyondthehorizon.entities.baseform.mirage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sonicrushxii.beyondthehorizon.ModUtils;
import net.sonicrushxii.beyondthehorizon.entities.all.PointEntity;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class MirageEntity extends PointEntity {
    /** Ticks between the decoy's basic punches. */
    private static final int PUNCH_INTERVAL = 10;
    private static final double PUNCH_REACH = 3.0;

    public static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(MirageEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    public static final EntityDataAccessor<Optional<UUID>> TARGET =
            SynchedEntityData.defineId(MirageEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    public MirageEntity(EntityType<? extends PointEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(OWNER, Optional.empty());
        builder.define(TARGET, Optional.empty());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("OwnerUUID")) setOwner(tag.getUUID("OwnerUUID"));
        if (tag.hasUUID("TargetUUID")) setTarget(tag.getUUID("TargetUUID"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        UUID owner = getOwnerUUID();
        if (owner != null) tag.putUUID("OwnerUUID", owner);
        UUID target = getTargetUUID();
        if (target != null) tag.putUUID("TargetUUID", target);
    }

    public void setOwner(UUID uuid) { this.entityData.set(OWNER, Optional.ofNullable(uuid)); }
    public void setTarget(UUID uuid) { this.entityData.set(TARGET, Optional.ofNullable(uuid)); }

    @Nullable
    public UUID getOwnerUUID() { return this.entityData.get(OWNER).orElse(null); }

    @Nullable
    public UUID getTargetUUID() { return this.entityData.get(TARGET).orElse(null); }

    @Override
    public void tick()
    {
        super.tick();

        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        UUID targetUuid = getTargetUUID();
        if (targetUuid == null) return;

        //Decoy keeps swinging at whatever its owner was attacking, basic punches only
        if (!(serverLevel.getEntity(targetUuid) instanceof LivingEntity target) || !target.isAlive()) {
            setTarget(null);
            return;
        }

        Vec3 toTarget = target.position().subtract(this.position());
        if (toTarget.lengthSqr() > 1e-6)
            this.setYRot(ModUtils.getYawPitchFromVec(toTarget)[0]);

        if (this.tickCount % PUNCH_INTERVAL != 0) return;
        if (toTarget.length() > PUNCH_REACH) return;

        UUID ownerUuid = getOwnerUUID();
        Player owner = (ownerUuid != null) ? serverLevel.getPlayerByUUID(ownerUuid) : null;

        float punchDamage = 1.0f;
        if (owner != null && owner.getAttribute(Attributes.ATTACK_DAMAGE) != null)
            punchDamage = (float) owner.getAttribute(Attributes.ATTACK_DAMAGE).getValue();

        target.hurt(ModDamageTypes.getDamageSource(serverLevel,
                ModDamageTypes.SONIC_MELEE_COMBO_IMMUNE.getResourceKey(), owner), punchDamage);

        serverLevel.playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.MASTER, 0.6f, 1.2f);
    }
}
