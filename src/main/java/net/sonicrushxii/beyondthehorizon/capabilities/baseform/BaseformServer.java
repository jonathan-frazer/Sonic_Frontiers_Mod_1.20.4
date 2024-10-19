package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.stomp.Stomp;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_5.Cyloop;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StartSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StopSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step.AutoStep;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.danger_sense.DangerSenseEmit;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleAuraPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import org.joml.Vector3f;

import java.util.*;

public class BaseformServer {
    private static final float HOMING_ATTACK_DAMAGE = 12.0f;
    private static final float BALLFORM_DAMAGE = 6.0f;
    private static final float HUMMING_TOP_DAMAGE = 3.0f;

    public static final Map<UUID,Deque<Vec3i>> cyloopCoords = new HashMap<>();

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
                                player.getX()+0.00, player.getY()+0.85, player.getZ()+0.00,
                                1.0, 1.40f, 1.00f, 1.00f, 10,
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

                                assert enemy != null;
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
                                enemy.setDeltaMovement(new Vec3(lookAngle.x,-0.1,lookAngle.z).scale(1.4));
                                player.connection.send(new ClientboundSetEntityMotionPacket(enemy));
                            }

                        }
                        if(baseformProperties.hummingTop == 40) {
                            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                        }
                        //Ability End
                        if(baseformProperties.hummingTop >= 50 || (baseformProperties.hummingTop > 0 && player.onGround()))
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

                //Slot 6
                {
                    if(baseformProperties.cylooping)
                    {
                        //Get a Deque of current Coordinates
                        Deque<Vec3i> currCoords = cyloopCoords.get(player.getUUID());
                        assert currCoords != null;

                        //Add points to list
                        Vec3i currPoint = new Vec3i((int)player.getX(),(int)player.getY(),(int)player.getZ());
                        Cyloop.addToList(currCoords,currPoint);


                        //Display all points in the list
                        for(Vec3i coord: currCoords)
                        {
                            PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                    new DustParticleOptions(new Vector3f(0.0f, 1.00f, 1.00f), 2f),
                                    coord.getX()+0.00, coord.getY()+0.5, coord.getZ()+0.00,
                                    0.001, 0.55f, 0.55f, 0.55f, 3,
                                    true)
                            );
                            PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                    new DustParticleOptions(new Vector3f(0.00f, 0.11f, 1.00f), 1.5f),
                                    coord.getX()+0.00, coord.getY()+0.5, coord.getZ()+0.00,
                                    0.001, 0.65f, 0.65f, 0.65f, 2,
                                    true)
                            );
                            PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                    new DustParticleOptions(new Vector3f(1.00f, 0.00f, 0.89f), 1.5f),
                                    coord.getX()+0.00, coord.getY()+0.5, coord.getZ()+0.00,
                                    0.001, 0.55f, 0.55f, 0.55f, 2,
                                    true)
                            );
                            PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                    ParticleTypes.FIREWORK,
                                    coord.getX()+0.00, coord.getY()+0.5, coord.getZ()+0.00,
                                    0.001, 0.55f, 0.55f, 0.55f, 1,
                                    true)
                            );
                        }
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
            {

            }


            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            baseformProperties
                    ));
        });
    }
}
