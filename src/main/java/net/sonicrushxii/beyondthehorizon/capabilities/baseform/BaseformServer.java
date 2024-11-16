package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformActiveAbility;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.entities.baseform.mirage.MirageCloud;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;
import net.sonicrushxii.beyondthehorizon.modded.ModEffects;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.base_cyloop.Cyloop;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.base_cyloop.CyloopParticleS2C;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.stomp.Stomp;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.loop_kick.LoopKick;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.wild_rush.WildRushParticleS2C;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.wild_rush.WildRushRotationSyncS2C;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StartSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StopSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step.AutoStep;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.danger_sense.DangerSenseEmit;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleAuraPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleRaycastPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;
import org.joml.Vector3f;

import java.util.*;

public class BaseformServer {
    private static final float HOMING_ATTACK_DAMAGE = 12.0f;
    private static final float BALLFORM_DAMAGE = 6.0f;
    private static final float HUMMING_TOP_DAMAGE = 3.0f;
    private static final float WILDRUSH_DAMAGE = 50.0f;
    private static final float LOOPKICK_DAMAGE = 40.0f;

    public static final Map<UUID,Deque<Vec3>> cyloopCoords = new HashMap<>();

    public static void performServerTick(ServerPlayer player, CompoundTag playerNBT)
    {
        Minecraft mc = Minecraft.getInstance();
        Level level = player.level();
        ServerLevel serverLevel = player.serverLevel();

        Vec3 playerDirCentre = Utilities.calculateViewVector(0.0f, player.getViewYRot(0)).scale(0.75);
        BlockPos centrePos = player.blockPosition().offset(
                (int) Math.round(playerDirCentre.x),
                (Math.round(player.getY()) > player.getY()) ? 1 : 0,
                (int) Math.round(playerDirCentre.z)
        );

        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            //Get Data From the Player
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Passive Abilities
            {
                //General Sprinting, it also handles autostep
                if (player.isSprinting() && baseformProperties.sprintFlag == false)
                    StartSprint.performStartSprint(player);
                if (!player.isSprinting() && baseformProperties.sprintFlag == true)
                    StopSprint.performStopSprint(player);

                //Double Jump
                if (!baseformProperties.hasDoubleJump && player.onGround()) {
                    baseformProperties.hasDoubleJump = true;
                }

                //Auto Step
                if (player.isSprinting() && !baseformProperties.isAttacking())
                {
                    List<String> blocksinFront = new ArrayList<>();
                    blocksinFront.add(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos.offset(0, -3, 0)).getBlock()) + "");
                    blocksinFront.add(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos.offset(0, -2, 0)).getBlock()) + "");
                    blocksinFront.add(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos.offset(0, -1, 0)).getBlock()) + "");
                    blocksinFront.add(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos).getBlock()) + "");

                    if (Utilities.passableBlocks.contains(blocksinFront.get(3))
                            && Utilities.passableBlocks.contains(blocksinFront.get(2))
                            && !Utilities.passableBlocks.contains(blocksinFront.get(1))
                            && player.onGround())
                        AutoStep.performStepDown(player,0.95);

                    if (Utilities.passableBlocks.contains(blocksinFront.get(3))
                            && Utilities.passableBlocks.contains(blocksinFront.get(2))
                            && Utilities.passableBlocks.contains(blocksinFront.get(1))
                            && !Utilities.passableBlocks.contains(blocksinFront.get(0))
                            && player.onGround())
                        AutoStep.performStepDown(player,1.95);
                }
                //Danger Sense

                //Subdue Hunger
                if (player.getFoodData().getFoodLevel() <= 7)
                    player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 1, 0, false, false));
            }

            //Active Abilities
            {
                //Slot 1
                {
                    //Air Boosts
                    if (baseformProperties.airBoosts < 3 && player.onGround())
                        baseformProperties.airBoosts = 3;

                    //Boost
                    {
                        //Water Boost
                        if (player.isSprinting() && !player.isInWater() &&
                                baseformProperties.boostLvl >= 1 && baseformProperties.boostLvl <= 3) {
                            try {
                                if (ForgeRegistries.BLOCKS.getKey(level.getBlockState(player.blockPosition().offset(0, -1, 0)).getBlock())
                                        .equals(ForgeRegistries.BLOCKS.getKey(Blocks.WATER))) {
                                    //Get Motion
                                    Vec3 lookAngle = player.getLookAngle();
                                    Vec3 playerDirection = new Vec3(lookAngle.x(), 0, lookAngle.z());

                                    if (baseformProperties.isWaterBoosting == false) {
                                        player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.0);
                                        baseformProperties.isWaterBoosting = true;

                                        //Slight upward
                                        playerDirection = new Vec3(lookAngle.x(), 0.01, lookAngle.z());
                                    }

                                    //Move Forward
                                    player.setDeltaMovement(playerDirection.scale(2 * baseformProperties.boostLvl));
                                    player.connection.send(new ClientboundSetEntityMotionPacket(player));
                                }
                            } catch (NullPointerException ignored) {
                            }
                        }

                        //Undo Water Boost
                        try {
                            if (baseformProperties.isWaterBoosting)
                                if (!ForgeRegistries.BLOCKS.getKey(level.getBlockState(player.blockPosition().offset(0, -1, 0)).getBlock())
                                        .equals(ForgeRegistries.BLOCKS.getKey(Blocks.WATER))
                                        ||
                                        !(baseformProperties.boostLvl >= 1 && baseformProperties.boostLvl <= 3)
                                        ||
                                        (player.getDeltaMovement().x < 0.5 && player.getDeltaMovement().y < 0.5 && player.getDeltaMovement().z < 0.5)
                                        ||
                                        player.isInWater()) {
                                    player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                                    baseformProperties.isWaterBoosting = false;
                                }
                        } catch (NullPointerException ignored) {
                        }

                        //Sprint Effects
                        if (player.isSprinting()) {
                            //Particles
                            switch (baseformProperties.boostLvl) {
                                case 1:
                                    PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                            ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                            player.getX()+0.00, player.getY()+0.05, player.getZ()+0.00,
                                            0.001, 0.00f, 0.00f, 0.00f, 1,
                                            true));
                                    break;
                                case 2:
                                    PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                            new DustParticleOptions((baseformProperties.ballFormState==0)?(new Vector3f(1.000f, 0.000f, 0.000f)):(new Vector3f(0.000f, 0.000f, 0.800f)), 2f),
                                            player.getX()+0.00, player.getY()+0.35, player.getZ()+0.00,
                                            0.001, 0.25f, 0.25f, 0.25f, 4,
                                            true)
                                    );
                                    break;
                                case 3:
                                    PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                            new DustParticleOptions(new Vector3f(0.0f, 0.89f, 1.00f), 1),
                                            player.getX()+0.00, player.getY()+1.0, player.getZ()+0.00,
                                            0.001, 0.35f, 1f, 0.35f, 12,
                                            true)
                                    );
                                    break;
                                default:
                            }
                            if (ForgeRegistries.BLOCKS.getKey(level.getBlockState(player.blockPosition().offset(0, -1, 0)).getBlock()).equals(ForgeRegistries.BLOCKS.getKey(Blocks.WATER)))
                                PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                        ParticleTypes.FALLING_WATER,
                                        player.getX()+0.00, player.getY()+1.0, player.getZ()+0.00,
                                        0.001, 0.35f, 1f, 0.35f, 12,
                                        true)
                                );

                            //Wall Boost
                            if (!Utilities.passableBlocks.contains(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos.offset(0, 1, 0)).getBlock()) + "")
                                    && player.getXRot() < -80.0
                                    && baseformProperties.boostLvl >= 1 && baseformProperties.boostLvl <= 3) {
                                //Move Upward
                                player.setSprinting(false);
                                baseformProperties.wallBoosting = true;
                                player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.0);
                                player.setDeltaMovement(new Vec3(0, player.getAttribute(Attributes.MOVEMENT_SPEED).getValue() * 2.5, 0));
                                player.connection.send(new ClientboundSetEntityMotionPacket(player));
                            }
                        }
                        //Wall Boost
                        if(baseformProperties.wallBoosting)
                        {
                            if (!Utilities.passableBlocks.contains(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos.offset(0, 1, 0)).getBlock()) + ""))
                            {
                                //Particle
                                switch (baseformProperties.boostLvl) {
                                    case 1:
                                        PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                                player.getX()+0.00, player.getY()+0.05, player.getZ()+0.00,
                                                0.001, 0.00f, 0.00f, 0.00f, 1,
                                                true));
                                        break;
                                    case 2:
                                        PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                                new DustParticleOptions((baseformProperties.ballFormState==0)?(new Vector3f(1.000f, 0.000f, 0.000f)):(new Vector3f(0.000f, 0.000f, 0.800f)), 2f),
                                                player.getX()+0.00, player.getY()+0.35, player.getZ()+0.00,
                                                0.001, 0.25f, 0.25f, 0.25f, 4,
                                                true)
                                        );
                                        break;
                                    case 3:
                                        PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                                new DustParticleOptions(new Vector3f(0.0f, 0.89f, 1.00f), 1),
                                                player.getX()+0.00, player.getY()+1.0, player.getZ()+0.00,
                                                0.001, 0.35f, 1f, 0.35f, 12,
                                                true)
                                        );
                                        break;
                                    default:
                                }

                                //Motion
                                player.setDeltaMovement(new Vec3(0, player.getAttribute(Attributes.MOVEMENT_SPEED).getValue() * 2.5, 0));
                                player.connection.send(new ClientboundSetEntityMotionPacket(player));
                            }
                            else {
                                player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                                baseformProperties.wallBoosting = false;
                            }
                        }
                    }

                    //Light Speed Attack
                    //Particles
                    if (baseformProperties.lightSpeedState == (byte) 1)
                        PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                new DustParticleOptions(new Vector3f(0.0f, 1.2f, 1.0f), 1),
                                player.getX()+0.00, player.getY()+0.35, player.getZ()+0.00,
                                1.0, 1.00f, 1.00f, 1.00f, 10,
                                true)
                        );

                    //Power Boost
                    if (baseformProperties.powerBoost) {
                        PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                ParticleTypes.ENCHANTED_HIT,
                                player.getX()+0.00, player.getY()+0.85, player.getZ()+0.00,
                                0.0, 0.80f, 1.00f, 0.80f, 1,
                                true)
                        );
                    }

                    //Base Cyloop
                    if(baseformProperties.cylooping)
                    {
                        //Get a Deque of current Coordinates
                        Deque<Vec3> currCoords = cyloopCoords.get(player.getUUID());
                        assert currCoords != null;

                        //Add points to list
                        Vec3 currPoint = new Vec3(player.getX(),player.getY(),player.getZ());
                        Cyloop.addToList(currCoords,currPoint);

                        //Display all points in the list
                        for(Vec3 coord: currCoords) {
                            PacketHandler.sendToALLPlayers(new CyloopParticleS2C(coord));
                        }
                    }

                    //Quick Cyloop
                    {
                        final float QK_CYLOOP_DAMAGE = 15.0f;

                        //Duration
                        try {
                            if (baseformProperties.quickCyloop > 0) {
                                //Increment Counter
                                baseformProperties.quickCyloop += 1;

                                //Particle
                                PacketHandler.sendToALLPlayers(new CyloopParticleS2C(new Vec3(player.getX(), player.getY(), player.getZ())));

                                //Circular Motion
                                //Get Target
                                assert baseformProperties.qkCyloopTarget != null;
                                LivingEntity enemy = (LivingEntity) serverLevel.getEntity(baseformProperties.qkCyloopTarget);

                                //Perform technique
                                if (enemy == null) {
                                    baseformProperties.quickCyloop = 0;
                                    throw new NullPointerException("Enemy died/doesn't exist anymore");
                                }

                                //Lock enemy in place
                                enemy.setDeltaMovement(0.0,0.0,0.0);
                                player.connection.send(new ClientboundSetEntityMotionPacket(enemy));

                                //Motion
                                Vec3 motionDirection = new Vec3(
                                        // sin(wt + T)
                                        Math.sin((Math.PI / 4) * baseformProperties.quickCyloop + baseformProperties.atkRotPhase * (Math.PI / 180)),
                                        0,
                                        // cos(wt + T)
                                        Math.cos((Math.PI / 4) * baseformProperties.quickCyloop + baseformProperties.atkRotPhase * (Math.PI / 180))
                                );
                                player.setDeltaMovement(motionDirection.scale(1.1));
                                player.connection.send(new ClientboundSetEntityMotionPacket(player));
                            }
                        } catch (NullPointerException ignored) {}

                        //Ending
                        try {
                            if (baseformProperties.quickCyloop > 8) {
                                baseformProperties.quickCyloop = 0;

                                assert baseformProperties.qkCyloopTarget != null;
                                LivingEntity enemy = (LivingEntity) serverLevel.getEntity(baseformProperties.qkCyloopTarget);

                                //Exit Move
                                if (enemy == null)
                                    throw new NullPointerException("Enemy died/doesn't exist anymore");


                                baseformProperties.qkCyloopTarget = new UUID(0L, 0L);

                                //Damage
                                Vec3 enemyPos = new Vec3(enemy.getX(), enemy.getY(), enemy.getZ());

                                //Play Particle
                                PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                        ParticleTypes.FLASH,
                                        enemyPos.x(), enemyPos.y() + player.getEyeHeight() / 2, enemyPos.z(),
                                        0.0, 0.2f, 0.2f, 0.2f, 1, true)
                                );
                                PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                        ParticleTypes.EXPLOSION,
                                        enemyPos.x(), enemyPos.y() + player.getEyeHeight() / 2, enemyPos.z(),
                                        0.0, 0.2f, 0.2f, 0.2f, 1, true)
                                );

                                //Sound
                                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.MASTER, 1.0f, 2.0f);

                                //Damage
                                enemy.hurt(ModDamageTypes.getDamageSource(player.level(), ModDamageTypes.SONIC_CYLOOP.getResourceKey(), player),
                                        QK_CYLOOP_DAMAGE);

                                //Launch up
                                enemy.setDeltaMovement(0.0, 1.1, 0.0);
                                player.connection.send(new ClientboundSetEntityMotionPacket(enemy));

                                //Lock in MidAir for 2 sec
                                Scheduler.scheduleTask(() -> {
                                    //Set Movement to Zero
                                    enemy.setDeltaMovement(0.0, 0.02, 0.0);
                                    player.connection.send(new ClientboundSetEntityMotionPacket(enemy));

                                    //Give the Cylooped Effect
                                    if (enemy.hasEffect(ModEffects.CYLOOPED.get()))
                                        enemy.getEffect(ModEffects.CYLOOPED.get()).update(new MobEffectInstance(ModEffects.CYLOOPED.get(), 40, 0, false, false));
                                    else
                                        enemy.addEffect(new MobEffectInstance(ModEffects.CYLOOPED.get(), 40, 0, false, false));

                                }, 10);

                                System.out.println("End Qk Cyloop");
                            }
                        }catch(NullPointerException ignored){}
                    }
                }

                //Slot 2
                {
                    //Spindash
                    {
                        if (baseformProperties.ballFormState == (byte) 1) {
                            baseformProperties.spinDashChargeTime++;

                            Vec3 playerLookVector = player.getLookAngle();

                            PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                    player.getX()-playerLookVector.x(), player.getY()+0.05, player.getZ()-playerLookVector.z(),
                                    0.001, 0.00f, 0.00f, 0.00f, 1,
                                    true));

                            PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                    new DustParticleOptions(new Vector3f(0.0f, 0.2f, 1.0f), 1f),
                                    player.getX(), player.getY()+0.45, player.getZ(),
                                    1.0, 0.65f, 0.65f, 0.65f, 3,
                                    true)
                            );
                        }
                        if (baseformProperties.ballFormState == (byte) 2) {
                            player.setDeltaMovement((Utilities.calculateViewVector(0,player.getYRot())).scale(Math.min(10.0,baseformProperties.spinDashChargeTime/10f)));
                            player.connection.send(new ClientboundSetEntityMotionPacket(player));

                            for(LivingEntity nearbyEntity : level.getEntitiesOfClass(LivingEntity.class,
                                    new AABB(player.getX()+1.5,player.getY()+1.5,player.getZ()+1.5,
                                            player.getX()-1.5,player.getY()-1.5,player.getZ()-1.5),
                                    (nearbyEntity)->!nearbyEntity.is(player)))
                                nearbyEntity.hurt(ModDamageTypes.getDamageSource(player.level(),ModDamageTypes.SONIC_BALL.getResourceKey(),player),
                                        Math.min(50.0f,baseformProperties.spinDashChargeTime/2f));

                            PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                    new DustParticleOptions(new Vector3f(0.0f, 0.2f, 1.0f), 1f),
                                    player.getX(), player.getY()+0.45, player.getZ(),
                                    1.0, 0.65f, 0.65f, 0.65f, 3,
                                    true)
                            );
                        }
                    }

                    //Homing Attack
                    {
                        //Perform homing attack
                        if(baseformProperties.homingAttackAirTime > 0)
                        {
                            try {
                                //Increment Counter
                                baseformProperties.homingAttackAirTime += 1;

                                //Get Target
                                assert baseformProperties.homingTarget != null;
                                LivingEntity enemy = (LivingEntity) serverLevel.getEntity(baseformProperties.homingTarget);

                                if(enemy == null)
                                {
                                    baseformProperties.homingAttackAirTime = 0;
                                    throw new NullPointerException("Enemy died/doesn't exist anymore");
                                }
                                Vec3 playerPos = player.getPosition(0);
                                Vec3 enemyPos = enemy.getPosition(0);
                                double distanceFromEnemy = playerPos.distanceTo(enemyPos);


                                //Homing
                                if (baseformProperties.homingAttackAirTime < 45) {
                                    player.setDeltaMovement(enemyPos.subtract(playerPos).normalize().scale(1.5));
                                    player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                    //Fail
                                    if (distanceFromEnemy > 16.0) {
                                        //Homing Attack Data
                                        baseformProperties.homingAttackAirTime = 44;
                                    }

                                    //Succeed
                                    if (distanceFromEnemy < 1.5) {
                                        //Homing Attack Data
                                        baseformProperties.homingAttackAirTime = 44;

                                        //Launch Up
                                        player.setDeltaMovement(0.0, 0.8, 0.0);
                                        player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                        //Damage Enemy
                                        enemy.hurt(ModDamageTypes.getDamageSource(player.level(),ModDamageTypes.SONIC_BALL.getResourceKey(),player),
                                                HOMING_ATTACK_DAMAGE);
                                    }

                                }
                                //Ending Segment
                                else {
                                    //Remove Gravity at point of impact
                                    if (baseformProperties.homingAttackAirTime == 45)
                                        player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);

                                    //At the end return all data to normal
                                    if (baseformProperties.homingAttackAirTime == 55) {
                                        baseformProperties.homingAttackAirTime = 0;
                                        baseformProperties.homingTarget = new UUID(0L, 0L);
                                    }
                                }
                            } catch (NullPointerException e)
                            {
                                player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                                baseformProperties.homingAttackAirTime = 0;
                                baseformProperties.homingTarget = new UUID(0L, 0L);
                            }
                        }
                    }

                    //Air Boost
                    if(baseformProperties.ballFormState == 3) {

                        //Damage Enemy
                        for(LivingEntity enemy: level.getEntitiesOfClass(LivingEntity.class,
                                new AABB(player.getX()+0.5,player.getY()+1.0,player.getZ()+0.5,
                                        player.getX()-0.5,player.getY(),player.getZ()-0.5),(enemy)->!enemy.is(player)))
                        {
                            enemy.hurt(ModDamageTypes.getDamageSource(player.level(), ModDamageTypes.SONIC_BALL.getResourceKey(), player),
                                    BALLFORM_DAMAGE);
                            player.addDeltaMovement(new Vec3(0.0,0.3,0.0));
                            player.connection.send(new ClientboundSetEntityMotionPacket(player));
                        }

                        //Go back to normal
                        if(player.onGround())
                            baseformProperties.ballFormState = 0;
                    }

                    //Dodge
                    {
                        if (baseformProperties.dodgeInvul)
                        {
                            PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                    ParticleTypes.CRIT,
                                    player.getX(), player.getY()+1, player.getZ(),
                                    0.0, 0.3f, 0.85f, 0.3f, 3,
                                    true)
                            );
                            level.playSound(null,player.getX(),player.getY(),player.getZ(), SoundEvents.PLAYER_ATTACK_WEAK, SoundSource.MASTER, 1.0f, 1.0f);
                        }
                    }

                    //Humming Top
                    Vec3 playerInFrontOf = player.position().add(player.getLookAngle().scale(0.90));
                    {
                        //Duration
                        if(baseformProperties.hummingTop > 0)
                        {
                            //Add Time
                            baseformProperties.hummingTop += 1;

                            //Go Forward
                            Vec3 lookAngle = player.getLookAngle();
                            player.setDeltaMovement(new Vec3(lookAngle.x,-0.1,lookAngle.z).scale(1.0));
                            player.connection.send(new ClientboundSetEntityMotionPacket(player));

                            //Damage and Move Entities
                            for(LivingEntity enemy: level.getEntitiesOfClass(LivingEntity.class,
                                    new AABB(player.getX()+1.0,player.getY()+0.75,player.getZ()+1.0,
                                            player.getX()-1.0,player.getY()-0.25,player.getZ()-1.0),(enemy)->!enemy.is(player)))
                            {
                                enemy.hurt(ModDamageTypes.getDamageSource(player.level(), ModDamageTypes.SONIC_BALL.getResourceKey(), player),
                                        HUMMING_TOP_DAMAGE);
                                enemy.teleportTo(player.getX()+lookAngle.x*2.0,player.getY()+lookAngle.y*2.0,player.getZ()+lookAngle.z*2.0);
                                player.connection.send(new ClientboundTeleportEntityPacket(enemy));
                            }

                        }
                        //Ability End
                        if(baseformProperties.hummingTop > 0 && player.onGround())
                        {
                            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                            baseformProperties.hummingTop = 0;
                        }
                    }

                    //Speed Blitz

                    //Smash Hit
                    {
                        switch(baseformProperties.smashHit)
                        {
                            case 20:
                            case 31:
                            case 42:
                            case 53://Particle
                                    PacketHandler.sendToPlayer(player, new ParticleAuraPacketS2C(
                                        ParticleTypes.FLASH,
                                                playerInFrontOf.x(), playerInFrontOf.y()+player.getEyeHeight()/2, playerInFrontOf.z(),
                                        0.0, 0.2f, 0.2f, 0.2f, 1, true)
                                        );
                                    //Sound
                                    level.playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.SMASH_CHARGE.get(), SoundSource.MASTER, 1.0f, 1.0f);
                                    baseformProperties.smashHit += 1;
                                    break;
                            case 64:PacketHandler.sendToPlayer(player, new ParticleAuraPacketS2C(
                                    ParticleTypes.FLASH,
                                            playerInFrontOf.x(), playerInFrontOf.y()+player.getEyeHeight()/2, playerInFrontOf.z(),
                                    0.0, 0.2f, 0.2f, 0.2f, 3, true)
                                    );
                                    //Sound
                                    level.playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.SMASH_CHARGE.get(), SoundSource.MASTER, 1.0f, 1.0f);
                                    baseformProperties.smashHit += 1;
                                    break;
                        }
                    }

                    //Stomp
                    {
                        if(baseformProperties.stomp > 0)
                        {
                            //Increase Stomp time
                            baseformProperties.stomp += 1;

                            //Bring Enemies Down with you
                            for(LivingEntity enemy: player.level().getEntitiesOfClass(LivingEntity.class,
                                    new AABB(player.getX()+2.5,player.getY()+1.0,player.getZ()+2.5,
                                            player.getX()-2.5,player.getY()-4.0,player.getZ()-2.5),
                                    (target)->!target.is(player)))
                            {
                                //Damage Enemy
                                enemy.setDeltaMovement(0,-5.0,0);
                                player.connection.send(new ClientboundSetEntityMotionPacket(enemy));
                            }

                            //Deactivate Stomp
                            if(baseformProperties.stomp == Byte.MAX_VALUE || player.onGround() || player.isInWater())
                                Stomp.performEndStomp(player);
                        }
                    }

                }

                //Slot 3
                {
                    // Tornado Jump
                    {
                        if (baseformProperties.tornadoJump > 0) {
                            //Increase Time
                            baseformProperties.tornadoJump += 1;

                            //Particle
                            PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                    new DustParticleOptions(new Vector3f(0.0f, 1.0f, 1.0f), 1.5f),
                                    player.getX(), player.getY() + 1, player.getZ(),
                                    0.0, 0.55f, 0.55f, 0.55f, 10,
                                    true)
                            );

                            //Motion
                            Vec3 motionDirection = new Vec3(
                                    // sin(wt + T)
                                    Math.sin((Math.PI / 6) * baseformProperties.tornadoJump + baseformProperties.atkRotPhase * (Math.PI / 180)),
                                    0.10,
                                    // cos(wt + T)
                                    Math.cos((Math.PI / 6) * baseformProperties.tornadoJump + baseformProperties.atkRotPhase * (Math.PI / 180))
                            );
                            player.setDeltaMovement(motionDirection.scale(0.15 + 0.15 * Math.min(6, baseformProperties.tornadoJump)));
                            player.connection.send(new ClientboundSetEntityMotionPacket(player));
                        }

                        if (baseformProperties.tornadoJump > 30) {
                            baseformProperties.tornadoJump = -1;
                            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                            player.setDeltaMovement(new Vec3(0.0, 1.0, 0.0));
                            player.connection.send(new ClientboundSetEntityMotionPacket(player));
                        }

                        if (baseformProperties.tornadoJump == -1 && player.onGround())
                            baseformProperties.tornadoJump = 0;

                    }
                    // Mirage
                    {
                        //Increase Mirage Time
                        if (baseformProperties.mirageTimer > 0)
                            baseformProperties.mirageTimer += 1;

                        //End Ability if no Cloud is detected
                        if(baseformProperties.mirageTimer >= 8)
                        {
                            try {
                                MirageCloud mirageCloud = level.getEntitiesOfClass(MirageCloud.class, new AABB(
                                        player.getX() + 6, player.getY() + 6, player.getZ() + 6,
                                        player.getX() - 6, player.getY() - 6, player.getZ() - 6)).get(0);
                                if (mirageCloud == null) throw new NullPointerException("No Cloud");
                                double newX = mirageCloud.getX()+Utilities.random.nextDouble(-5,5);
                                double newY = mirageCloud.getY()+Utilities.random.nextDouble(0,1.5);
                                double newZ = mirageCloud.getZ()+Utilities.random.nextDouble(-5,5);

                                if(baseformProperties.mirageTimer == 9) {
                                    Vec3 lookDir = (new Vec3(mirageCloud.getX(), mirageCloud.getY(), mirageCloud.getZ()))
                                            .subtract(new Vec3(newX, newY, newZ));
                                    float[] yawPitch = Utilities.getYawPitchFromVec(lookDir);

                                    player.teleportTo(player.serverLevel(), newX, newY, newZ,
                                            Collections.emptySet(), yawPitch[0], yawPitch[1]);
                                    player.connection.send(new ClientboundTeleportEntityPacket(player));
                                }
                            }catch (NullPointerException | IndexOutOfBoundsException e) {
                                baseformProperties.mirageTimer = 141;
                            }
                        }

                        //End Mirage Ability
                        if (baseformProperties.mirageTimer > 140 || (baseformProperties.mirageTimer > 1 && baseformProperties.isAttacking()))
                        {
                            //Reset Timer
                            baseformProperties.mirageTimer = 0;

                            //Remove Effect
                            if(player.hasEffect(MobEffects.INVISIBILITY))
                                player.removeEffect(MobEffects.INVISIBILITY);

                            //Kill Any Mirage Cloud Around
                            for(MirageCloud mirageCloud : level.getEntitiesOfClass(MirageCloud.class, new AABB(
                                    player.getX()+16,player.getY()+16,player.getZ()+16,
                                    player.getX()-16,player.getY()-16,player.getZ()-16)))
                                mirageCloud.remove(Entity.RemovalReason.KILLED);

                            //Cooldown
                            baseformProperties.setCooldown(BaseformActiveAbility.MIRAGE,(byte)45);
                        }
                    }
                    // Light Speed Assault
                    {
                        //Remove Light Speed Assault
                        if(baseformProperties.lightSpeedAssault == -1 && player.onGround())
                            baseformProperties.lightSpeedAssault = 0;

                        //Perform Light Speed Assault
                        if(baseformProperties.lightSpeedAssault > 0)
                        {
                            try
                            {
                                //Increment Counter
                                baseformProperties.lightSpeedAssault += 1;

                                //Get Target
                                assert baseformProperties.meleeTarget != null;
                                LivingEntity enemy = (LivingEntity) serverLevel.getEntity(baseformProperties.meleeTarget);

                                if(enemy == null)  throw new NullPointerException("Enemy died/doesn't exist anymore");

                                Vec3 playerPos = new Vec3(player.getX(),player.getY(),player.getZ());
                                Vec3 enemyPos = new Vec3(enemy.getX(),enemy.getY(),enemy.getZ());
                                double distanceFromEnemy = playerPos.distanceTo(enemyPos);

                                //Duration Light Speed Assault
                                if (baseformProperties.lightSpeedAssault < 105)
                                {
                                    //Fail
                                    if (distanceFromEnemy > 24.0) {
                                        throw new NullPointerException("Too Far");
                                    }
                                    //Perform Surrounding Strikes
                                    else if (baseformProperties.lightSpeedAssault <= 55)
                                    {
                                        if (baseformProperties.lightSpeedAssault % 4 == 0) {
                                            //Get Position of in front of enemy
                                            Vec3 tpDir = playerPos.subtract(enemyPos).normalize().scale(2);
                                            float yawPitch[] = Utilities.getYawPitchFromVec(tpDir.reverse());

                                            //Particle Raycast
                                            PacketHandler.sendToALLPlayers(
                                                    new ParticleRaycastPacketS2C(
                                                            new DustParticleOptions(new Vector3f(0f,0f,1f),1)
                                                            ,playerPos.add(0,1.25,1),enemyPos.add(tpDir).add(0,1.25,0)
                                                    )
                                            );

                                            //Teleport to the Position
                                            player.teleportTo(player.serverLevel(),
                                                    enemyPos.x() + tpDir.x(),
                                                    enemyPos.y() + tpDir.y(),
                                                    enemyPos.z() + tpDir.z(),
                                                    Collections.emptySet(),
                                                    yawPitch[0], yawPitch[1]);
                                            player.connection.send(new ClientboundTeleportEntityPacket(player));

                                            //Attack the Enemy
                                            enemy.hurt(ModDamageTypes.getDamageSource(player.level(), ModDamageTypes.SONIC_MELEE.getResourceKey(), player),
                                                    1.0f);
                                            enemy.setDeltaMovement(player.getLookAngle().scale(0.3));
                                            player.connection.send(new ClientboundSetEntityMotionPacket(enemy));
                                        }
                                        else if(baseformProperties.lightSpeedAssault % 4 == 2)
                                        {
                                            //Find Random Position around Enemy
                                            double theta = Utilities.random.nextDouble(0,2*Math.PI);
                                            Vec3 tpLocation = new Vec3(
                                                    6*Math.cos(theta)+enemyPos.x(),
                                                    enemyPos.y()+Utilities.random.nextDouble(-1,1),
                                                    6*Math.sin(theta)+enemyPos.z()
                                            );
                                            float yawPitch[] = Utilities.getYawPitchFromVec(enemyPos.subtract(tpLocation));

                                            //Particle Raycast
                                            PacketHandler.sendToALLPlayers(
                                                    new ParticleRaycastPacketS2C(
                                                            new DustParticleOptions(new Vector3f(0f,0f,1f),1)
                                                            ,playerPos.add(0,1.25,1),tpLocation.add(0,1.25,0)
                                                    )
                                            );

                                            //Teleport to Random Position
                                            player.teleportTo(player.serverLevel(),
                                                    tpLocation.x(),
                                                    tpLocation.y(),
                                                    tpLocation.z(),
                                                    Collections.emptySet(),
                                                    yawPitch[0], yawPitch[1]);
                                            player.connection.send(new ClientboundTeleportEntityPacket(player));
                                        }
                                    }
                                    else //if (baseformProperties.lightSpeedAssault > 55)
                                    {
                                        if (baseformProperties.lightSpeedAssault % 3 == 0) {
                                            //Damage Enemy
                                            Vec3 tpDir = playerPos.subtract(enemyPos).normalize().scale(2);;
                                            float yawPitch[] = Utilities.getYawPitchFromVec(tpDir.reverse());

                                            //Particle Raycast
                                            PacketHandler.sendToALLPlayers(
                                                    new ParticleRaycastPacketS2C(
                                                            new DustParticleOptions(new Vector3f(0f,0f,1f),1)
                                                            ,playerPos.add(0,1.25,1),enemyPos.add(tpDir).add(0,1.25,0)
                                                    )
                                            );
                                            player.teleportTo(player.serverLevel(),
                                                    enemyPos.x() + tpDir.x(),
                                                    enemyPos.y() + tpDir.y(),
                                                    enemyPos.z() + tpDir.z(),
                                                    Collections.emptySet(),
                                                    yawPitch[0], yawPitch[1]);
                                            player.connection.send(new ClientboundTeleportEntityPacket(player));
                                            enemy.hurt(ModDamageTypes.getDamageSource(player.level(), ModDamageTypes.SONIC_MELEE.getResourceKey(), player),
                                                    1.0f);
                                            enemy.setDeltaMovement(player.getLookAngle().scale(1.0));
                                            player.connection.send(new ClientboundSetEntityMotionPacket(enemy));
                                        }
                                    }
                                }
                                else
                                {
                                    //Damage Enemy
                                    Vec3 tpDir = playerPos.subtract(enemyPos).normalize().scale(2);
                                    float yawPitch[] = Utilities.getYawPitchFromVec(tpDir.reverse());

                                    //Particle Raycast
                                    PacketHandler.sendToALLPlayers(
                                            new ParticleRaycastPacketS2C(
                                                    new DustParticleOptions(new Vector3f(0f,0f,1f),1),
                                                    playerPos.add(0,1.25,1),enemyPos.add(tpDir).add(0,1.25,0)
                                            )
                                    );

                                    //Teleport
                                    player.teleportTo(player.serverLevel(),
                                            enemyPos.x() + tpDir.x(),
                                            enemyPos.y() + tpDir.y(),
                                            enemyPos.z() + tpDir.z(),
                                            Collections.emptySet(),
                                            yawPitch[0], yawPitch[1]);
                                    player.connection.send(new ClientboundTeleportEntityPacket(player));

                                    //Go Up
                                    player.setDeltaMovement(0,0.95,0);
                                    player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                    //Hurt Enemy
                                    enemy.hurt(ModDamageTypes.getDamageSource(player.level(), ModDamageTypes.SONIC_MELEE.getResourceKey(), player),
                                            20.0f);
                                    enemy.setDeltaMovement(player.getLookAngle().scale(2.0));
                                    player.connection.send(new ClientboundSetEntityMotionPacket(enemy));

                                    throw new NullPointerException("Move Success");
                                }
                            }
                            catch (NullPointerException e)
                            {
                                //Light Speed Attack
                                player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                                baseformProperties.lightSpeedAssault = -1;
                                baseformProperties.meleeTarget = new UUID(0L, 0L);
                                //Cooldown
                                baseformProperties.setCooldown(BaseformActiveAbility.MIRAGE,(byte)45);
                            }
                        }
                    }

                    //Cyclone Kick
                    {
                        //Target the Enemy
                        try
                        {
                            if(baseformProperties.cycloneKick < 0)
                            {
                                baseformProperties.cycloneKick += 1;

                                //Find Target
                                LivingEntity cycloneTarget = (LivingEntity)serverLevel.getEntity(baseformProperties.meleeTarget);
                                Vec3 playerPos = new Vec3(player.getX(),player.getY(),player.getZ());
                                Vec3 cycloneTargetPos = new Vec3(cycloneTarget.getX(),cycloneTarget.getY()+cycloneTarget.getEyeHeight(),cycloneTarget.getZ());

                                //Find Motion Direction
                                Vec3 motionDirection = cycloneTargetPos.subtract(playerPos).normalize();
                                //Move in the Direction
                                player.setDeltaMovement(motionDirection.scale(1.0));
                                player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                //Reach Enemy
                                if(player.distanceToSqr(cycloneTargetPos) < 4)
                                {
                                    player.setDeltaMovement(motionDirection.add(0, -0.5, 0).scale(-0.1));
                                    player.connection.send(new ClientboundSetEntityMotionPacket(player));
                                    baseformProperties.cycloneKick = 0;
                                    player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                                    Scheduler.scheduleTask(()->{
                                        //Remove Gravity
                                        player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.0);

                                        //Set Motion to Zero
                                        player.setDeltaMovement(new Vec3(0,0,0));
                                        player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                        baseformProperties.cycloneKick = 1;
                                    },9);
                                }
                            }
                        }catch(NullPointerException|ClassCastException e)
                        {
                            baseformProperties.cycloneKick = 0;
                            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                        }

                        //Cyclone Part
                        try{
                            if(baseformProperties.cycloneKick > 0)
                            {
                                baseformProperties.cycloneKick += 1;

                                //Motion
                                Vec3 motionDirection = new Vec3(
                                        // sin(wt + T)
                                        Math.sin((Math.PI / 6) * baseformProperties.cycloneKick + baseformProperties.atkRotPhase * (Math.PI / 180)),
                                        0.10,
                                        // cos(wt + T)
                                        Math.cos((Math.PI / 6) * baseformProperties.cycloneKick + baseformProperties.atkRotPhase * (Math.PI / 180))
                                );
                                player.setDeltaMovement(motionDirection.scale(1.0 + ((baseformProperties.cycloneKick < 36)?0.01:-0.01)*baseformProperties.cycloneKick ));
                                player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                //End the Move
                                if(baseformProperties.cycloneKick >= 90)
                                    throw new NullPointerException("Move Completed Successfully");
                            }
                        }catch(NullPointerException|ClassCastException e)
                        {
                            baseformProperties.cycloneKick = 0;
                            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                        }


                    }

                    //Wild Rush
                    {
                        try{
                            if(baseformProperties.wildRushTime != 0)
                            {
                                baseformProperties.wildRushTime += 1;

                                if(baseformProperties.wildRushTime > 0 && baseformProperties.wildRushTime < 10)
                                {
                                    player.setDeltaMovement(0,0,0);
                                    player.connection.send(new ClientboundSetEntityMotionPacket(player));
                                }
                                else if(baseformProperties.wildRushTime > 0 && baseformProperties.wildRushTime < 80)
                                {
                                    //Find Target
                                    LivingEntity wildRushTarget = (LivingEntity)serverLevel.getEntity(baseformProperties.meleeTarget);
                                    Vec3 playerPos = new Vec3(player.getX(),player.getY(),player.getZ());
                                    Vec3 wildRushTargetPos = new Vec3(wildRushTarget.getX(),wildRushTarget.getY()+wildRushTarget.getEyeHeight(),wildRushTarget.getZ());

                                    //Match Lightning Bolt Position
                                    Vec3 lightningPos = null;
                                    if(baseformProperties.wildRushPtr < 5)
                                        lightningPos = new Vec3(
                                                baseformProperties.wildRushPX[baseformProperties.wildRushPtr],
                                                baseformProperties.wildRushPY[baseformProperties.wildRushPtr],
                                                baseformProperties.wildRushPZ[baseformProperties.wildRushPtr]
                                        );
                                    //Update Lightning Bolt to move to next Position
                                    if(lightningPos != null && lightningPos.distanceToSqr(playerPos) < 4)
                                        baseformProperties.wildRushPtr += 1;


                                    //Find Motion Direction
                                    Vec3 motionDirection;
                                    if(playerPos.distanceToSqr(wildRushTargetPos) < 20)     motionDirection = wildRushTargetPos.subtract(playerPos).normalize();
                                    else                                                    motionDirection = lightningPos.subtract(playerPos).normalize();
                                    //Move in the Direction
                                    player.setDeltaMovement(motionDirection.scale(1.0));
                                    player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                    //Update Player model in the direction of the vector
                                    float[] yawPitch = Utilities.getYawPitchFromVec(motionDirection);
                                    PacketHandler.sendToALLPlayers(new WildRushRotationSyncS2C(yawPitch[0],yawPitch[1]));
                                    //Particle
                                    PacketHandler.sendToALLPlayers(
                                            new WildRushParticleS2C(
                                                    player.getX()+motionDirection.x(),
                                                    player.getY()+motionDirection.y(),
                                                    player.getZ()+motionDirection.z()
                                            )
                                    );

                                    //If player is reached then, Hurt Enemy
                                    if(playerPos.distanceToSqr(wildRushTargetPos) < 2)
                                    {
                                        //Commands
                                        String command = String.format(
                                                "summon firework_rocket %.2f %.2f %.2f {Life:0,LifeTime:0,FireworksItem:{id:\"firework_rocket\",Count:1,tag:{Fireworks:{Explosions:[{Type:0,Flicker:1b,Colors:[I;255,16777215],FadeColors:[I;16777215,255]}]}}}}",
                                                player.getX() + motionDirection.x(),
                                                player.getY() + motionDirection.y(),
                                                player.getZ() + motionDirection.z()
                                        );
                                        player.setDeltaMovement(motionDirection.add(0,-0.5,0).scale(-0.3));
                                        player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                        CommandSourceStack commandSourceStack = serverLevel.getServer().createCommandSourceStack().withPermission(4).withSuppressedOutput();
                                        serverLevel.getServer().
                                                getCommands().
                                                performPrefixedCommand(commandSourceStack,command);

                                        //Hurt Enemy
                                        wildRushTarget.setDeltaMovement(motionDirection.scale(1.75f));
                                        wildRushTarget.hurt(
                                                ModDamageTypes.getDamageSource(player.level(), ModDamageTypes.SONIC_MELEE.getResourceKey(), player),
                                                WILDRUSH_DAMAGE
                                        );
                                        player.connection.send(new ClientboundSetEntityMotionPacket(wildRushTarget));
                                        throw new NullPointerException("Move Successful");
                                    }
                                }
                                else if(baseformProperties.wildRushTime > 0)
                                    throw new NullPointerException("Time out");
                            }
                        } catch(NullPointerException|ClassCastException|ArrayIndexOutOfBoundsException e)
                        {
                            //Reset Timer
                            baseformProperties.wildRushTime = -3;
                            //Cooldown
                            baseformProperties.setCooldown(BaseformActiveAbility.WILDRUSH,(byte)5);
                            //Return Gravity
                            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                        }
                    }

                    //Loop Kick
                    {
                        try {
                            if (baseformProperties.loopKick > 0) {
                                //Increase Timer
                                baseformProperties.loopKick += 1;

                                //Looping Portion
                                if (baseformProperties.loopKick < 24)
                                {
                                    //Get X and Z Components from The Player Rotation
                                    double xComponent = Math.sin(-baseformProperties.atkRotPhase * (Math.PI / 180)) * 0.707;
                                    double zComponent = Math.cos(baseformProperties.atkRotPhase * (Math.PI / 180)) * 0.707;

                                    //Particle
                                    PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                            ParticleTypes.SWEEP_ATTACK,
                                            player.getX(), player.getY() + 1, player.getZ(),
                                            0.0, 0.55f, 0.55f, 0.55f, 10,
                                            false)
                                    );

                                    //Motion
                                    Vec3 motionDirection = new Vec3(
                                            // sin(wt + T)
                                            xComponent * Math.cos((15.0F * baseformProperties.loopKick) * Math.PI / 180),
                                            Math.sin((15.0F * baseformProperties.loopKick) * Math.PI / 180),
                                            // cos(wt + T)
                                            zComponent * Math.cos((15.0F * baseformProperties.loopKick) * Math.PI / 180)
                                    );
                                    player.setDeltaMovement(motionDirection.scale(1.0));
                                    player.connection.send(new ClientboundSetEntityMotionPacket(player));
                                }
                                //Stay in Place for a bit
                                else if(baseformProperties.loopKick < 30)
                                {
                                    //Motion
                                    player.setDeltaMovement(new Vec3(0,0,0));
                                    player.connection.send(new ClientboundSetEntityMotionPacket(player));
                                }
                                //Scan for enemies
                                else if (baseformProperties.loopKick == 30)
                                {
                                    //Freeze Player
                                    player.setDeltaMovement(player.getLookAngle().scale(2.0f));
                                    player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                    //Scan for enemies
                                    LoopKick.scanFoward(player);
                                }
                                //Homing Strike
                                else if (baseformProperties.loopKick < 66)
                                {
                                    //Find Target
                                    //Move to the nearest entity
                                    LivingEntity loopKickTarget = (LivingEntity) serverLevel.getEntity(baseformProperties.meleeTarget);
                                    Vec3 targetPosition = new Vec3(loopKickTarget.getX(),loopKickTarget.getY(),loopKickTarget.getZ());
                                    double distanceFromEnemySqr = targetPosition.distanceToSqr(player.getX(),player.getY(),player.getZ());

                                    Vec3 motionVector = targetPosition.subtract(player.getX(), player.getY(), player.getZ());
                                    player.setDeltaMovement(motionVector.scale(0.35f));
                                    player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                    //Particle
                                    PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                            ParticleTypes.CRIT,
                                            player.getX(), player.getY() + 1, player.getZ(),
                                            0.0, 0.55f, 0.55f, 0.55f, 10,
                                            false)
                                    );

                                    //Out Of Range
                                    if(distanceFromEnemySqr > 900)  throw new NullPointerException("Enemy out of Range");

                                    //In Range
                                    else if(distanceFromEnemySqr < 4)
                                    {
                                        //Hurt Enemy
                                        loopKickTarget.setDeltaMovement(player.getLookAngle().scale(1.75f));
                                        loopKickTarget.hurt(
                                                ModDamageTypes.getDamageSource(player.level(), ModDamageTypes.SONIC_MELEE.getResourceKey(), player),
                                                LOOPKICK_DAMAGE
                                        );
                                        player.connection.send(new ClientboundSetEntityMotionPacket(loopKickTarget));
                                        throw new NullPointerException("Move Successful");
                                    }

                                }
                                else {
                                    throw new NullPointerException("Move Timed Out");
                                }
                            }
                        } catch(NullPointerException|ClassCastException e)
                        {
                            baseformProperties.loopKick = -1;
                            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                            baseformProperties.meleeTarget = new UUID(0L, 0L);
                            //Cooldown
                            baseformProperties.setCooldown(BaseformActiveAbility.LOOPKICK,(byte)5);
                        }
                        //Return to normal animation when on Ground
                        if (baseformProperties.loopKick == -1 && player.onGround())
                            baseformProperties.loopKick = 0;
                    }

                }
            }

            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            baseformProperties
                    ));
        });


    }

    public static void performServerSecond(ServerPlayer player, CompoundTag playerNBT)
    {
        //Get Data From the Player
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            //Get Data From the Player
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();
            //Cooldowns
            {
                byte[] allCooldowns = baseformProperties.getAllCooldowns();
                for (int i = 0; i < allCooldowns.length; ++i) {
                    if(allCooldowns[i] != (byte)-1)
                        allCooldowns[i] = (byte) Math.max(0, allCooldowns[i] - 1);
                }
            }

            //Passive Abilities
            {
                //Danger Sense
                if (baseformProperties.dangerSenseActive)
                    DangerSenseEmit.performDangerSenseEmit(player);


            }

            //Slot 1
            {}
            //Slot 2
            {}
            //Slot 3


            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            baseformProperties
                    ));
        });
    }
}
