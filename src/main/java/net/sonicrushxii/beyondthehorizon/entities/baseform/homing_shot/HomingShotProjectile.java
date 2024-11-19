package net.sonicrushxii.beyondthehorizon.entities.baseform.homing_shot;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleAuraPacketS2C;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HomingShotProjectile extends Entity {
    public static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(HomingShotProjectile.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(HomingShotProjectile.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final float STRENGTH = 1.0f;
    private static final float HOMING_SHOT_DAMAGE = 35.0f;

    public HomingShotProjectile(EntityType<? extends HomingShotProjectile> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DURATION, 150);
        this.entityData.define(OWNER,Optional.empty());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Duration")) {
            this.entityData.set(DURATION,tag.getInt("Duration"));
        }
        if (tag.hasUUID("OwnerUUID")) this.setOwner(tag.getUUID("OwnerUUID"));

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Duration", this.entityData.get(DURATION));
        UUID ownerUuid = getOwnerUUID();
        if (ownerUuid != null) tag.putUUID("OwnerUUID", ownerUuid);
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


    @Override
    public void tick() {
        super.tick();

        final int timeElapsed = 150-getDuration();

        this.move(MoverType.SELF, this.getDeltaMovement());
        final float[] yawPitch = Utilities.getYawPitchFromVec(this.getDeltaMovement());
        this.setYRot(yawPitch[0]);
        this.setXRot(yawPitch[1]);

        // Custom tick logic - Here, it just counts down the duration and removes the entity
        if (this.entityData.get(DURATION) > 0) {
            this.entityData.set(DURATION,this.entityData.get(DURATION)-1);
        } else if(!this.level().isClientSide) {
            this.kill(); // Removes the entity from the world
        }

        //Set Glowing
        if(timeElapsed == 1)
        {
            //Glowing
            this.setGlowingTag(true);

            // Get the scoreboard
            Scoreboard scoreboard = this.level().getScoreboard();

            // Check if the team already exists
            PlayerTeam team = scoreboard.getPlayerTeam("light_blue");
            if (team == null) {
                // Create a new team if it doesn't exist
                team = scoreboard.addPlayerTeam("light_blue");

                // Set the team's color
                team.setColor(ChatFormatting.BLUE); // Set team color
            }

            // Add the entity to the team
            scoreboard.addPlayerToTeam(this.getScoreboardName(), team);
        }

        //Move Homing Shot Away from Player
        if(timeElapsed > 0 && timeElapsed < 10)
        {
            //It requires a Baseform Sonic player to be next to the cloud or else it instantly disappears
            List<Player> sonicPlayers = this.level().getEntitiesOfClass(Player.class, new AABB(
                            this.getX() + 3, this.getY() + 3, this.getZ() + 3,
                            this.getX() - 3, this.getY() - 3, this.getZ() - 3),
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
            if(sonicPlayers.isEmpty()) return;

            Player sonicPlayer = Collections.min(sonicPlayers, (e1, e2) -> {
                Vec3 e1Pos = new Vec3(e1.getX(), e1.getY(), e1.getZ());
                Vec3 e2Pos = new Vec3(e2.getX(), e2.getY(), e2.getZ());

                return (int) (e1Pos.distanceToSqr(this.getX(),this.getY(),this.getZ()) - e2Pos.distanceToSqr(this.getX(),this.getY(),this.getZ()));
            });

            Vec3 sonicPos = new Vec3(sonicPlayer.getX(),sonicPlayer.getY()+0.25,sonicPlayer.getZ());
            Vec3 currentPos = new Vec3(this.getX(),this.getY(),this.getZ());

            Vec3 motionDir = currentPos.subtract(sonicPos).normalize();

            this.setDeltaMovement(motionDir.scale(0.2));
        }

        if(timeElapsed == 10)
            this.setDeltaMovement(0,0,0);

        //Particles
        if(this.level().isClientSide)
        {
            Utilities.displayParticle(this.level(),
                    new DustParticleOptions(new Vector3f(0.2f,0.2f,1f),1f),
                    this.getX(),this.getY()+1,this.getZ(),
                    0.2F,0.2F,0.2F,
                    0.001, 4, false);
        }
        //Time Elapsed
        else if(timeElapsed > 12)
        {
            // Check for collision
            if(this.onGround() || this.horizontalCollision || this.verticalCollision && this.getDeltaMovement().y > 0)  explode();

            // Check for entity collisions and apply damage
            List<LivingEntity> enemies = this.level().getEntitiesOfClass(LivingEntity.class,
                    new AABB(this.getX() - 0.5, this.getY() - 0.5, this.getZ() - 0.5,
                            this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5),
                    enemy -> {
                        if(enemy.is(this)) return false;
                        try {
                            Player playerEntity = (Player)enemy;
                            ItemStack headItem = playerEntity.getItemBySlot(EquipmentSlot.HEAD);
                            if (headItem.getItem() == Items.PLAYER_HEAD &&
                                    headItem.getTag().getByte("BeyondTheHorizon") == (byte) 2)
                                return false;
                        }
                        catch (NullPointerException|ClassCastException ignored){}
                        return true;
                    }
            );
            if (!enemies.isEmpty()) {
                try {// Synchronize on server only
                    for (LivingEntity enemy : enemies) {
                        enemy.hurt(
                                ModDamageTypes.getDamageSource(this.level(), ModDamageTypes.SONIC_RANGED.getResourceKey(), this.getOwner()),
                                HOMING_SHOT_DAMAGE
                        );
                    }
                }catch(NullPointerException ignored){}
                explode();
            }
        }

    }

    private void explode()
    {
        //Particle
        PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                new DustParticleOptions(new Vector3f(0.202F,0.202F,1F),1.5F),
                this.getX(), this.getY() + 0.5, this.getZ(),
                0.0, 0.55f, 0.55f, 0.55f, 10,
                false)
        );

        // Kill all other projectiles
        for(HomingShotProjectile siblings : this.level().getEntitiesOfClass(HomingShotProjectile.class,
                new AABB(this.getX() - 6.0, this.getY() - 6.0, this.getZ() - 6.0,
                        this.getX() + 6.0, this.getY() + 6.0, this.getZ() + 6.0)))
            siblings.kill();

        //Deal Damage and Use Particle Effects
        this.level().explode(
                /* Exploder (null if no specific entity causes it) */ getOwner(),
                /* Center x, y, z positions */ this.getX(), this.getY(), this.getZ(),
                /* Strength */ STRENGTH,
                /* Causes fire */ false,
                /* Block Interaction Mode */ Level.ExplosionInteraction.NONE
        );
    }

    public void setDuration(int duration)
    {
        this.entityData.set(DURATION,duration);
    }

    public int getDuration()
    {
        return this.entityData.get(DURATION);
    }
}