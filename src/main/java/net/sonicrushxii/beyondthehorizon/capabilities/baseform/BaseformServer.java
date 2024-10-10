package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.danger_sense.DangerSenseEmit;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleAuraPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import org.joml.Vector3f;

import java.util.UUID;

public class BaseformServer {
    public static void performServerTick(ServerPlayer player, CompoundTag playerNBT)
    {
        Minecraft minecraft = Minecraft.getInstance();
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
                //Double Jump
                //Auto Step
                //Danger Sense
                //Hunger

            }

            //Active Abilities
            {
                //Slot 1
                {
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
                                            new DustParticleOptions(new Vector3f(1.000f, 0.000f, 0.000f), 2f),
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
                                player.addDeltaMovement(new Vec3(0, player.getAttribute(Attributes.MOVEMENT_SPEED).getValue() * 1.5, 0));
                                player.connection.send(new ClientboundSetEntityMotionPacket(player));
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

                            for(LivingEntity nearbyEntity : level.getEntitiesOfClass(LivingEntity.class,
                                    new AABB(player.getX()+1.5,player.getY()+1.5,player.getZ()+1.5,
                                            player.getX()-1.5,player.getY()-1.5,player.getZ()-1.5),
                                    (nearbyEntity)->!nearbyEntity.is(player)))
                                nearbyEntity.hurt(player.damageSources().playerAttack(player),
                                        Math.min(100.0f,baseformProperties.spinDashChargeTime));

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
                                        baseformProperties.selectiveInvul = false;
                                    }

                                    //Succeed
                                    if (distanceFromEnemy < 1.5) {
                                        //Homing Attack Data
                                        baseformProperties.homingAttackAirTime = 44;

                                        //Launch Up
                                        player.setDeltaMovement(0.0, 0.8, 0.0);
                                        player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                        //Damage Enemy
                                        enemy.hurt(player.damageSources().playerAttack(player), 4.0f);
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
                                        baseformProperties.selectiveInvul = false;
                                    }
                                }
                            } catch (NullPointerException e)
                            {
                                player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                                baseformProperties.homingAttackAirTime = 0;
                                baseformProperties.homingTarget = new UUID(0L, 0L);
                                baseformProperties.selectiveInvul = false;
                            }
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

                //Subdue Hunger
                if (player.getFoodData().getFoodLevel() <= 8)
                    player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 1, 0, false, false));
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
