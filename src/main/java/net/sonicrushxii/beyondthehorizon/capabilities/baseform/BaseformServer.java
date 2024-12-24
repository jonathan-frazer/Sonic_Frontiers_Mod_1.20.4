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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.sonicrushxii.beyondthehorizon.entities.baseform.SpinSlashCloud;
import net.sonicrushxii.beyondthehorizon.entities.baseform.cross_slash.CrossSlashProjectile;
import net.sonicrushxii.beyondthehorizon.entities.baseform.homing_shot.HomingShotProjectile;
import net.sonicrushxii.beyondthehorizon.entities.baseform.mirage.MirageCloud;
import net.sonicrushxii.beyondthehorizon.entities.baseform.sonic_boom.SonicBoomProjectile;
import net.sonicrushxii.beyondthehorizon.entities.baseform.sonic_wind.SonicWind;
import net.sonicrushxii.beyondthehorizon.modded.ModDamageTypes;
import net.sonicrushxii.beyondthehorizon.modded.ModEffects;
import net.sonicrushxii.beyondthehorizon.modded.ModEntityTypes;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.base_cyloop.Cyloop;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.base_cyloop.CyloopParticleS2C;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.stomp.Stomp;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.loop_kick.LoopKick;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.wild_rush.WildRushParticleS2C;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.wild_rush.WildRushRotationSyncS2C;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.cross_slash.EndCrossSlash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.homing_shot.HomingShot;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_boom.EndSonicBoom;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_wind.SonicWindParticleS2C;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_4.parry.StopParry;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StartSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StopSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step.AutoStep;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.danger_sense.DangerSenseEmit;
import net.sonicrushxii.beyondthehorizon.network.sync.*;
import net.sonicrushxii.beyondthehorizon.scheduler.ScheduledTask;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;
import org.joml.Vector3f;

import java.util.*;

public class BaseformServer {

    //Combo
    private static final float HOMING_ATTACK_DAMAGE = 9.0f;
    private static final float BALLFORM_DAMAGE = 6.0f;
    private static final float HUMMING_TOP_DAMAGE = 3.0f;
    public static final float STOMP_DAMAGE = 12.0f;
    private static final float LIGHT_SPEED_ASSAULT = 20.0f;

    //Melee
    public static final float TORNADO_JUMP_DMG = 1.0f;
    public static final float DASH_DAMAGE = 1.0f;
    private static final float WILDRUSH_DAMAGE = 10.0f;
    private static final float LOOPKICK_DAMAGE = 12.0f;
    public static final float SPINSLASH_DAMAGE = 3.0f;
    public static final float CYCLONE_KICK_DAMAGE = 3.0f;

    //Ranged
    public static final float SONIC_BOOM_DAMAGE = 6.0f;
    public static final float SONIC_WIND_DAMAGE = 18.0f;
    public static final float HOMING_SHOT_DAMAGE = 35.0f;
    public static final float CROSS_SLASH_DAMAGE = 12.0f;

    //Counter
    private static final float GRAND_SLAM_DMG = 17.0f;

    //Ultimate
    private static final double ULT_DECAY_RATE = 0.15;
    public static final float PHANTOM_RUSH_DAMAGE = 10.0f;
    public static final float ULTIMATE_DAMAGE = 100.0f;

    public static final Map<UUID,Deque<Vec3>> cyloopCoords = new HashMap<>();
    public static final HashMap<UUID,ScheduledTask> cyloopSoundEmitter = new HashMap<>();

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

                                    if (baseformProperties.isWaterBoosting == false)
                                    {
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
                                        player.isInWater())
                                {
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
                                                new DustParticleOptions(new Vector3f(0.0f, 0.89f, 1.00f), 1.2F),
                                                player.getX()+0.00, player.getY()+1.0, player.getZ()+0.00,
                                                0.001, 0.35f, 1f, 0.35f, 21,
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
                                false)
                        );
                        PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                new DustParticleOptions(new Vector3f(0.000f,0.969f,1.000f), 1.5f),
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

                                //Stopsound in Minecraft
                                PacketHandler.sendToALLPlayers(new PlayerStopSoundPacketS2C(
                                        ModSounds.CYLOOP.get().getLocation())
                                );

                                //Sound
                                level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.CYLOOP_SUCCESS.get(), SoundSource.MASTER, 1.0f, 1.0f);

                                //Damage
                                //Double Cyloop - Launch Down
                                if(enemy.hasEffect(ModEffects.CYLOOPED.get()) && enemy.getEffect(ModEffects.CYLOOPED.get()).getDuration() > 0)
                                {
                                    //Launch Down
                                    enemy.setDeltaMovement(0.0,-1.1,0.0);
                                    player.connection.send(new ClientboundSetEntityMotionPacket(enemy));

                                    //Deal Damage
                                    enemy.hurt(ModDamageTypes.getDamageSource(player.level(),ModDamageTypes.SONIC_CYLOOP.getResourceKey(),player),
                                            QK_CYLOOP_DAMAGE*1.5F);

                                    //Give the Cylooped Effect
                                    enemy.getEffect(ModEffects.CYLOOPED.get()).update(new MobEffectInstance(ModEffects.CYLOOPED.get(), 20, 0, false, false));
                                }
                                //Single Cyloop
                                else
                                {
                                    //Damage
                                    enemy.hurt(ModDamageTypes.getDamageSource(player.level(),ModDamageTypes.SONIC_CYLOOP.getResourceKey(),player),
                                            QK_CYLOOP_DAMAGE);

                                    //Launch Up
                                    enemy.setDeltaMovement(0.0,1.1,0.0);
                                    player.connection.send(new ClientboundSetEntityMotionPacket(enemy));

                                    //Lock in MidAir for 2 sec
                                    Scheduler.scheduleTask(()->{
                                        //Set Movement to Zero
                                        enemy.setDeltaMovement(0.0,0.02,0.0);
                                        player.connection.send(new ClientboundSetEntityMotionPacket(enemy));

                                        //Give the Cylooped Effect
                                        if(enemy.hasEffect(ModEffects.CYLOOPED.get()))
                                            enemy.getEffect(ModEffects.CYLOOPED.get()).update(new MobEffectInstance(ModEffects.CYLOOPED.get(), 40, 0, false, false));
                                        else
                                            enemy.addEffect(new MobEffectInstance(ModEffects.CYLOOPED.get(), 40, 0, false, false));

                                    },10);
                                }

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
                                enemy.hurt(ModDamageTypes.getDamageSource(player.level(), ModDamageTypes.SONIC_RANGED.getResourceKey(), player),
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
                    {
                        if(baseformProperties.speedBlitzDashTimer > 0)
                        {
                            baseformProperties.speedBlitzDashTimer += 1;

                            if(baseformProperties.speedBlitzDashTimer <= 4)
                                PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                    new DustParticleOptions(new Vector3f(0f,1f,1f),2f),
                                    player.getX()+0.00, player.getY()+1.05, player.getZ()+0.00,
                                    0.001, 0.15f, 1.05f, 0.15f, 9,
                                    true));

                            for(LivingEntity enemy : level.getEntitiesOfClass(LivingEntity.class,new AABB(
                                    player.getX()+1.5, player.getY()+2.0, player.getZ()+1.5,
                                    player.getX()-1.5, player.getY()-1, player.getZ()-1.5
                            ),(enemy)->!enemy.is(player)))
                            {
                                enemy.hurt(ModDamageTypes.getDamageSource(player.level(), ModDamageTypes.SONIC_BALL.getResourceKey(), player),
                                        DASH_DAMAGE);
                            }
                        }
                        if(baseformProperties.speedBlitzDashTimer == 5)
                        {
                            //Set Delta Movement
                            player.setDeltaMovement(0,0,0);
                            //Remove Gravity
                            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                            player.connection.send(new ClientboundSetEntityMotionPacket(player));

                            Vec3 currPos = new Vec3(player.getX(),player.getY(),player.getZ()).add(Utilities.calculateViewVector(0,baseformProperties.atkRotPhase).scale(-4.0));
                            Vec3 playerPos = new Vec3(player.getX(),player.getY(),player.getZ());
                            for(LivingEntity enemy : level.getEntitiesOfClass(LivingEntity.class,new AABB(
                                    currPos.x+4.0, currPos.y+2.0, currPos.z+4.0,
                                    currPos.x-4.0, currPos.y-2.0, currPos.z-4.0
                            ),(enemy)->!enemy.is(player)))
                            {
                                Vec3 enemyPos = new Vec3(enemy.getX(),enemy.getY(),enemy.getZ());
                                float[] yawPitch = Utilities.getYawPitchFromVec(enemyPos.subtract(playerPos));
                                player.teleportTo(player.serverLevel(), player.getX(), player.getY(), player.getZ(),
                                        EnumSet.of(RelativeMovement.X,RelativeMovement.Y,RelativeMovement.Z), yawPitch[0], yawPitch[1]);
                                player.connection.send(new ClientboundTeleportEntityPacket(player));
                                break;
                            }
                        }
                        if (baseformProperties.speedBlitzDashTimer > 15)
                            baseformProperties.speedBlitzDashTimer = 0;
                    }

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
                                List<MirageCloud> mirageClouds = level.getEntitiesOfClass(MirageCloud.class, new AABB(
                                        player.getX() + 6, player.getY() + 6, player.getZ() + 6,
                                        player.getX() - 6, player.getY() - 6, player.getZ() - 6));
                                if (mirageClouds.isEmpty()) throw new NullPointerException("No Cloud");
                                MirageCloud mirageCloud = mirageClouds.get(0);
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
                                            float[] yawPitch = Utilities.getYawPitchFromVec(tpDir.reverse());

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
                                            float[] yawPitch = Utilities.getYawPitchFromVec(enemyPos.subtract(tpLocation));

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
                                            Vec3 tpDir = playerPos.subtract(enemyPos).normalize().scale(2);
                                            float[] yawPitch = Utilities.getYawPitchFromVec(tpDir.reverse());

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
                                            enemy.hurt(ModDamageTypes.getDamageSource(player.level(), ModDamageTypes.SONIC_RANGED.getResourceKey(), player),
                                                    1.0f);
                                            enemy.setDeltaMovement(player.getLookAngle().scale(2.4));
                                            player.connection.send(new ClientboundSetEntityMotionPacket(enemy));
                                        }
                                    }
                                }
                                else
                                {
                                    //Damage Enemy
                                    Vec3 tpDir = playerPos.subtract(enemyPos).normalize().scale(2);
                                    float[] yawPitch = Utilities.getYawPitchFromVec(tpDir.reverse());

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
                                    enemy.hurt(
                                            ModDamageTypes.getDamageSource(player.level(), ModDamageTypes.SONIC_MELEE.getResourceKey(), player),
                                            LIGHT_SPEED_ASSAULT
                                    );
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

                    //Spin Slash
                    {
                        try {
                            if (baseformProperties.spinSlash < -10)
                            {
                                baseformProperties.spinSlash += 1;

                                //Find Target
                                LivingEntity spinTarget = (LivingEntity)serverLevel.getEntity(baseformProperties.meleeTarget);
                                Vec3 playerPos = new Vec3(player.getX(),player.getY(),player.getZ());
                                Vec3 spinTargetPos = new Vec3(spinTarget.getX(),spinTarget.getY()+spinTarget.getEyeHeight()/2,spinTarget.getZ());

                                //Find Motion Direction
                                Vec3 motionDirection = spinTargetPos.subtract(playerPos).normalize();
                                //Move in the Direction
                                player.setDeltaMovement(motionDirection.scale((baseformProperties.spinSlash<-52)?0.0:1.0));
                                player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                //Reach Enemy
                                if(player.distanceToSqr(spinTargetPos) < 4)
                                {
                                    //Spin Target, Teleport
                                    Vec3 bounceDir = (new Vec3(motionDirection.x(), -0.75, motionDirection.z())).scale(-0.35);
                                    player.teleportTo(spinTargetPos.x()+bounceDir.x(),
                                            spinTargetPos.y()+bounceDir.y(),
                                            spinTargetPos.z()+bounceDir.z());
                                    player.connection.send(new ClientboundTeleportEntityPacket(player));

                                    //Bounce
                                    player.setDeltaMovement(bounceDir);
                                    player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                    baseformProperties.spinSlash = -10;
                                    player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);

                                    //Spawn Spin Particles
                                    Scheduler.scheduleTask(()->{
                                        Vec3 cloudSpawn = new Vec3(-motionDirection.x(),0,-motionDirection.z());

                                        Utilities.summonEntity(ModEntityTypes.BASEFORM_SPIN_SLASH_CLOUD.get(),
                                                player.serverLevel(),
                                                spinTargetPos.add(cloudSpawn.scale(0.75)),
                                                (aoeCloud) -> {
                                                    aoeCloud.setDuration(58);
                                                    aoeCloud.setOwner(player.getUUID());
                                                });

                                    },7);

                                    //Continue to Next Attack
                                    Scheduler.scheduleTask(()->{
                                        //Remove Gravity
                                        player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.0);

                                        //Set Attack Rotation Phase
                                        baseformProperties.atkRotPhase = -player.getYRot()-135f;

                                        //Set Motion to Zero
                                        player.setDeltaMovement(new Vec3(0,0,0));
                                        player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                        baseformProperties.spinSlash = 1;
                                    },11);
                                }
                            }

                            if(baseformProperties.spinSlash >= -10 && baseformProperties.spinSlash < 0 && player.onGround())
                                baseformProperties.spinSlash = 0;

                        }catch(NullPointerException|ClassCastException e)
                        {
                            baseformProperties.spinSlash = 0;
                            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                            player.removeEffect(MobEffects.GLOWING);
                        }

                        try {
                            if(baseformProperties.spinSlash == 1)
                                player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.SPIN_SLASH.get(), SoundSource.MASTER, 0.65f, 1.0f);

                            if (baseformProperties.spinSlash > 0)
                            {
                                baseformProperties.spinSlash += 1;

                                //Stun Target
                                try {
                                    LivingEntity spinTarget = (LivingEntity) serverLevel.getEntity(baseformProperties.meleeTarget);
                                    spinTarget.setDeltaMovement(0,0,0);
                                    player.connection.send(new ClientboundSetEntityMotionPacket(spinTarget));
                                }catch(NullPointerException|ClassCastException ignored){}


                                Vec3 jumpBackPos = new Vec3(0,0,0);

                                //Teleport to the crux of the spinSlash
                                List<SpinSlashCloud> spinSlashClouds = serverLevel.getEntitiesOfClass(SpinSlashCloud.class, new AABB(
                                        player.getX() + 8, player.getY() + 6, player.getZ() + 8,
                                        player.getX() - 8, player.getY() - 6, player.getZ() - 8));
                                if(!spinSlashClouds.isEmpty())
                                {
                                    SpinSlashCloud spinSlashCloud = spinSlashClouds.get(0);

                                    player.teleportTo(
                                            spinSlashCloud.getX()+Math.sin(baseformProperties.spinSlash*(Math.PI/4))*2.75,
                                            spinSlashCloud.getY(),
                                            spinSlashCloud.getZ()+Math.cos(baseformProperties.spinSlash*(Math.PI/4))*2.75
                                    );
                                    if(baseformProperties.spinSlash >= 53){
                                        jumpBackPos = (new Vec3(player.getX(),player.getY(),player.getZ())).subtract(new Vec3(spinSlashCloud.getX(),spinSlashCloud.getY(),spinSlashCloud.getZ())).normalize();
                                    }
                                    player.connection.send(new ClientboundTeleportEntityPacket(player));
                                }

                                //End the Move
                                if(baseformProperties.spinSlash >= 53) {
                                    player.setDeltaMovement( (new Vec3(0,0.35,0)).add(jumpBackPos.scale(0.35)) );
                                    player.connection.send(new ClientboundSetEntityMotionPacket(player));
                                    throw new NullPointerException("Move Completed Successfully");
                                }
                            }
                        }catch(NullPointerException|ClassCastException e)
                        {
                            baseformProperties.spinSlash = 0;
                            //Cooldown
                            baseformProperties.setCooldown(BaseformActiveAbility.SPINSLASH,(byte)5);
                            //Return Gravity
                            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                            //Remove Glowing Effect
                            player.removeEffect(MobEffects.GLOWING);
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
                                Vec3 cycloneTargetPos = new Vec3(cycloneTarget.getX(),cycloneTarget.getY()+cycloneTarget.getEyeHeight()/2,cycloneTarget.getZ());

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

                                    //Continue to Next Attack
                                    Scheduler.scheduleTask(()->{
                                        //Remove Gravity
                                        player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.0);

                                        //Set Motion to Zero
                                        player.setDeltaMovement(new Vec3(0,0,0));
                                        player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                        baseformProperties.cycloneKick = 1;
                                    },3);

                                    //Spawn Cyclone Kick Cloud
                                    Scheduler.scheduleTask(()-> Utilities.summonEntity(ModEntityTypes.BASEFORM_CYCLONE_KICK_CLOUD.get(),
                                            player.serverLevel(),
                                            cycloneTargetPos.add(0,-cycloneTarget.getEyeHeight()/2,0),
                                            (aoeCloud) -> {
                                                aoeCloud.setDuration(60);
                                                aoeCloud.setOwner(player.getUUID());
                                            }),12);
                                }
                            }
                        }catch(NullPointerException|ClassCastException e)
                        {
                            baseformProperties.cycloneKick = 0;
                            //Return Gravity
                            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                        }

                        //Cyclone Part
                        try{
                            if(baseformProperties.cycloneKick > 0 && baseformProperties.cycloneKick <= 3)
                                baseformProperties.cycloneKick += 1;

                            else if(baseformProperties.cycloneKick > 3)
                            {
                                baseformProperties.cycloneKick += 1;

                                byte offSetCycloneKick = (byte) (baseformProperties.cycloneKick-3);

                                //Motion
                                if(baseformProperties.cycloneKick <= 63)
                                {
                                    Vec3 motionDirection = new Vec3(
                                            // sin(wt + T)
                                            Math.sin((Math.PI / 6) * offSetCycloneKick + baseformProperties.atkRotPhase * (Math.PI / 180)),
                                            0.10,
                                            // cos(wt + T)
                                            Math.cos((Math.PI / 6) * offSetCycloneKick + baseformProperties.atkRotPhase * (Math.PI / 180))
                                    );
                                    player.setDeltaMovement(motionDirection.scale(1.0 + ((offSetCycloneKick < 36) ? 0.01 : -0.01) * offSetCycloneKick));
                                    player.connection.send(new ClientboundSetEntityMotionPacket(player));
                                }
                                if(baseformProperties.cycloneKick == 64)
                                {
                                    player.setDeltaMovement(0,1.0,0);
                                    player.connection.send(new ClientboundSetEntityMotionPacket(player));
                                }

                                //End the Move
                                if(baseformProperties.cycloneKick >= 73)
                                    throw new NullPointerException("Move Completed Successfully");
                            }
                        }catch(NullPointerException|ClassCastException e)
                        {
                            baseformProperties.cycloneKick = 0;
                            //Cooldown
                            baseformProperties.setCooldown(BaseformActiveAbility.CYCLONE_KICK,(byte)5);
                            //Return Gravity
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
                                    if(lightningPos != null && lightningPos.distanceToSqr(playerPos) < 4) {
                                        baseformProperties.wildRushPtr += 1;
                                        //Play Sound
                                        player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.HOMING_ATTACK.get(), SoundSource.MASTER, 0.75f, 1.0f);
                                    }


                                    //Find Motion Direction
                                    Vec3 motionDirection;
                                    if(playerPos.distanceToSqr(wildRushTargetPos) < 50)     motionDirection = wildRushTargetPos.subtract(playerPos).normalize();
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

                                        //Play Sound
                                        player.level().playSound(null,player.getX(),player.getY(),player.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.MASTER, 0.75f, 1.0f);

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
                            baseformProperties.loopKick = -40;
                            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                            baseformProperties.meleeTarget = new UUID(0L, 0L);
                            //Cooldown
                            baseformProperties.setCooldown(BaseformActiveAbility.LOOPKICK,(byte)5);
                        }
                        //Return to normal animation when on Ground or when it times out
                        if (baseformProperties.loopKick < 0) {
                            baseformProperties.loopKick += 1;
                            if(player.onGround())  baseformProperties.loopKick = 0;
                        }
                    }

                }

                //Slot 4
                {
                    //Sonic Boom
                    {
                        try {
                            if (baseformProperties.sonicBoom > 0) {
                                baseformProperties.sonicBoom += 1;
                                player.setDeltaMovement(0, -0.01, 0);
                                player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                if (baseformProperties.sonicBoom % 3 == 0) {
                                    //Motion Dir
                                    Vec3 motionDir = player.getLookAngle().scale(0.75);

                                    //Set Spawn position
                                    Vec3 spawnPos = new Vec3(player.getX() + motionDir.x,
                                            player.getY() + motionDir.y + player.getEyeHeight()/3,
                                            player.getZ() + motionDir.z);

                                    SonicBoomProjectile sonicBoomProjectile = new SonicBoomProjectile(ModEntityTypes.BASEFORM_SONIC_BOOM.get(), level);

                                    sonicBoomProjectile.setPos(spawnPos);
                                    sonicBoomProjectile.setDuration(120);
                                    sonicBoomProjectile.setXRot(player.getXRot());
                                    sonicBoomProjectile.setYRot(player.getYRot());
                                    sonicBoomProjectile.setMovementDirection(player.getLookAngle());
                                    sonicBoomProjectile.setDestroyBlocks(player.isShiftKeyDown());
                                    sonicBoomProjectile.setOwner(player.getUUID());

                                    // Add the entity to the world
                                    level.addFreshEntity(sonicBoomProjectile);
                                }

                                //End Ability
                                if (baseformProperties.sonicBoom > 120) {
                                    throw new NullPointerException("Duration End");

                                }
                            }
                        }catch (NullPointerException|ClassCastException e)
                        {
                            EndSonicBoom.performEndSonicBoom(player);
                        }
                    }

                    //Cross Slash
                    {
                        try {
                            if (baseformProperties.crossSlash > 0)
                            {
                                baseformProperties.crossSlash += 1;
                                player.setDeltaMovement(0.0, 0.0, 0.0);
                                player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                if (baseformProperties.crossSlash % 3 == 0) {
                                    //Motion Dir
                                    Vec3 motionDir = player.getLookAngle().scale(0.75);

                                    //Set Spawn position
                                    Vec3 spawnPos = new Vec3(player.getX() + motionDir.x,
                                            player.getY() + motionDir.y + (player.getEyeHeight()-player.getEyeHeight()/3),
                                            player.getZ() + motionDir.z);
                                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                                            SoundEvents.EGG_THROW, SoundSource.MASTER, 1.0f, 1.0f);

                                    CrossSlashProjectile crossSlashProjectile = new CrossSlashProjectile(ModEntityTypes.BASEFORM_CROSS_SLASH.get(), level);

                                    crossSlashProjectile.setPos(spawnPos);
                                    crossSlashProjectile.setDuration(120);
                                    crossSlashProjectile.setXRot(player.getXRot());
                                    crossSlashProjectile.setYRot(player.getYRot());
                                    crossSlashProjectile.setMovementDirection(player.getLookAngle());
                                    crossSlashProjectile.setDestroyBlocks(player.isShiftKeyDown());
                                    crossSlashProjectile.setOwner(player.getUUID());

                                    //Play Sound
                                    level.playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.CROSS_SLASH.get(), SoundSource.MASTER, 0.75f, 1.0f);

                                    // Add the entity to the world
                                    level.addFreshEntity(crossSlashProjectile);
                                }

                                //End Ability
                                if (baseformProperties.crossSlash > 120) {
                                    throw new NullPointerException("Duration End");
                                }
                            }
                        }catch (NullPointerException|ClassCastException e)
                        {
                            EndCrossSlash.performEndCrossSlash(player);
                        }
                    }

                    //Sonic Wind
                    {
                        //Normal Sonic Wind
                        if(baseformProperties.sonicWind > 0)
                        {
                            baseformProperties.sonicWind += 1;

                            //Add Particle
                            if(baseformProperties.sonicWind < 10)
                            {
                                PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                        new DustParticleOptions(new Vector3f(0f, 1f, 1f), 2f),
                                        player.getX(), player.getY() + 1.05, player.getZ(),
                                        0.001, 0.55f, 1.05f, 0.55f, 2,
                                        true));
                            }

                            //Throw Sonic Wind
                            if(baseformProperties.sonicWind == 10)
                            {
                                Vec3 spawnPos = new Vec3(player.getX()+player.getLookAngle().x,
                                        player.getY()+player.getLookAngle().y+1.0,
                                        player.getZ()+player.getLookAngle().z);
                                level.playSound(null,player.getX(),player.getY(),player.getZ(),
                                        SoundEvents.EGG_THROW, SoundSource.MASTER, 1.0f, 1.0f);
                                SonicWind sonicWind = new SonicWind(ModEntityTypes.BASEFORM_SONIC_WIND.get(), level);

                                sonicWind.setPos(spawnPos);
                                sonicWind.setDuration(120);
                                sonicWind.setMovementDirection(player.getLookAngle());
                                sonicWind.setDestroyBlocks(player.isShiftKeyDown());
                                sonicWind.setOwner(player.getUUID());

                                //Play Sound
                                level.playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.SONIC_WIND_SHOOT.get(), SoundSource.MASTER, 0.75f, 1.0f);

                                // Add the entity to the world
                                level.addFreshEntity(sonicWind);
                            }

                            //End Ability
                            if(baseformProperties.sonicWind > 20)
                            {
                                //Reset Counter to 0
                                baseformProperties.sonicWind = 0;
                                //Return Gravity
                                player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                                //Cooldown
                                baseformProperties.setCooldown(BaseformActiveAbility.SONIC_WIND,(byte)5);

                            }
                        }

                        //Quick Sonic Wind
                        if(baseformProperties.profanedWind > 0)
                        {
                            //Increment Tick
                            baseformProperties.profanedWind += 1;

                            //Display Player Particles
                            PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                    new DustParticleOptions(new Vector3f(0f, 1f, 1f), 2f),
                                    player.getX(), player.getY() + 1.05, player.getZ(),
                                    0.001, 0.55f, 1.05f, 0.55f, 2,
                                    true));

                            //Display Swirling Particles
                            PacketHandler.sendToALLPlayers(new SonicWindParticleS2C(
                                    baseformProperties.profanedWindCoords[0],
                                    baseformProperties.profanedWindCoords[1],
                                    baseformProperties.profanedWindCoords[2],
                                    baseformProperties.profanedWind
                            ));

                            if(baseformProperties.profanedWind == 9)
                            {
                                //Particle
                                PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                        new DustParticleOptions(new Vector3f(1F,1F,1F),1.5F),
                                        baseformProperties.profanedWindCoords[0],
                                        baseformProperties.profanedWindCoords[1],
                                        baseformProperties.profanedWindCoords[2],
                                        0.01, 1F, 20, true
                                ));
                                PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                        ParticleTypes.FLASH,
                                        baseformProperties.profanedWindCoords[0],
                                        baseformProperties.profanedWindCoords[1],
                                        baseformProperties.profanedWindCoords[2],
                                        0.01, 1F, 2, true
                                ));

                                //Sound
                                level.playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.SONIC_WIND_STUN.get(), SoundSource.MASTER, 0.75f, 1.0f);

                                //Blast
                                for(LivingEntity enemy : serverLevel.getEntitiesOfClass(LivingEntity.class,
                                        new AABB(baseformProperties.profanedWindCoords[0]+2.5,baseformProperties.profanedWindCoords[1]+2.5, baseformProperties.profanedWindCoords[2]+2.5,
                                                baseformProperties.profanedWindCoords[0]-2.5,baseformProperties.profanedWindCoords[1]-2.5, baseformProperties.profanedWindCoords[2]-2.5),
                                       enemy -> {
                                        try {
                                            Player playerEntity = (Player)enemy;
                                            ItemStack headItem = playerEntity.getItemBySlot(EquipmentSlot.HEAD);
                                            if (headItem.getItem() == Items.PLAYER_HEAD &&
                                                    headItem.getTag().getByte("BeyondTheHorizon") == (byte) 2)
                                                return false;
                                        }
                                        catch (NullPointerException|ClassCastException ignored){}
                                        return true;
                                    }))
                                {
                                    if(enemy.hasEffect(ModEffects.WIND_STUNNED.get()))
                                        enemy.getEffect(ModEffects.WIND_STUNNED.get()).update(new MobEffectInstance(ModEffects.WIND_STUNNED.get(), 80, 0, false, false));
                                    else
                                        enemy.addEffect(new MobEffectInstance(ModEffects.WIND_STUNNED.get(), 80, 0, false, false));
                                }
                            }

                            //End Ability
                            if(baseformProperties.profanedWind > 9)
                            {
                                //Reset Counter to 0
                                baseformProperties.profanedWind = 0;
                                //Return Gravity
                                player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                                //Cooldown
                                baseformProperties.setCooldown(BaseformActiveAbility.SONIC_WIND,(byte)5);
                            }
                        }
                    }

                    //Homing Shot
                    {
                        try {
                            if (baseformProperties.homingShot > 0) {
                                baseformProperties.homingShot += 1;

                                //Ball form
                                if (baseformProperties.homingShot < 20) {
                                    player.setDeltaMovement(0, 0, 0);
                                    player.connection.send(new ClientboundSetEntityMotionPacket(player));
                                }

                                //Spawn the Homing Shots
                                if (baseformProperties.homingShot == 10)
                                {
                                    for(double theta = 0.0; theta <= 2*Math.PI; theta += Math.PI/5.5)
                                    {
                                        double xComponent = Math.sin(-(baseformProperties.atkRotPhase+90) * (Math.PI / 180)) * 0.707;
                                        double zComponent = Math.cos((baseformProperties.atkRotPhase+90) * (Math.PI / 180)) * 0.707;

                                        //Motion
                                        Utilities.summonEntity(ModEntityTypes.BASEFORM_HOMING_SHOT.get(),
                                                player.serverLevel(),
                                                new Vec3(
                                                        // sin(wt + T)
                                                        player.getX()+ 0.2*xComponent*Math.cos(theta),
                                                        player.getY()+0.25 + 0.2*Math.sin(theta),
                                                        // cos(wt + T)
                                                        player.getZ()+ 0.2*zComponent*Math.cos(theta)
                                                ),(homingShotProjectile -> {
                                                    homingShotProjectile.setXRot(player.getXRot());
                                                    homingShotProjectile.setYRot(baseformProperties.atkRotPhase);
                                                    homingShotProjectile.setOwner(player.getUUID());
                                                }));
                                    }
                                }

                                //Aim the Homing Shots
                                try {
                                    if (baseformProperties.homingShot == 21) {
                                        HomingShot.scanFoward(player);
                                        for (HomingShotProjectile homingShotProjectile : level.getEntitiesOfClass(HomingShotProjectile.class,
                                                new AABB(player.getX() + 6.0, player.getY() + 6.0, player.getZ() + 6.0,
                                                        player.getX() - 6.0, player.getY() - 6.0, player.getZ() - 6.0))) {
                                            homingShotProjectile.setTarget(serverLevel.getEntity(baseformProperties.rangedTarget).getId());
                                        }
                                    }
                                }catch(NullPointerException ignored){}

                            }

                            //End Move
                            if (baseformProperties.homingShot > 50) {
                                throw new InterruptedException("Move Successful");
                            }
                        }catch (InterruptedException|NullPointerException|ClassCastException ignored)
                        {
                            //Homing Shot
                            baseformProperties.homingShot = 0;
                            //Return Gravity
                            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                            //Cooldown
                            baseformProperties.setCooldown(BaseformActiveAbility.HOMING_SHOT,(byte)5);
                        }
                    }
                }

                //Slot 5
                {
                    //Parry
                    {
                        try {
                            if (baseformProperties.parryTime > 0)
                            {
                                //
                                baseformProperties.parryTime += 1;
                                player.setDeltaMovement(0.0, 0.0, 0.0);
                                player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                // End Ability
                                if (baseformProperties.parryTime == Byte.MAX_VALUE) {
                                    throw new InterruptedException("Duration End");
                                }
                            }
                        } catch(NullPointerException|ClassCastException|InterruptedException e)
                        {
                            StopParry.performStopParry(player);
                        }

                        //Parry Cooldown Handling
                        if(baseformProperties.parryTime < 0)
                        {
                            baseformProperties.parryTime += 1;

                            //Reset The Parry Cooldown
                            if(player.onGround() && baseformProperties.parryTime > -50)
                                baseformProperties.parryTime = 0;
                        }

                        //Slow Down Time for 1.5sec
                        if(baseformProperties.parryTimeSlow > 0)    baseformProperties.parryTimeSlow += 1;
                        if(baseformProperties.parryTimeSlow > 30)   StopParry.returnFromParryTime(player);
                    }

                    //Grand Slam
                    {
                        try
                        {
                            LivingEntity counterTarget = (LivingEntity) serverLevel.getEntity(baseformProperties.counteredEntity);
                            Vec3 counterTargetPos = new Vec3(counterTarget.getX(),counterTarget.getY()+counterTarget.getEyeHeight()/3,counterTarget.getZ());
                            Vec3 playerPos = new Vec3(player.getX(),player.getY(),player.getZ());
                            Vec3 tpDir = playerPos.subtract(counterTargetPos).normalize();

                            //Make Target Glow
                            if (!counterTarget.hasEffect(MobEffects.GLOWING) && baseformProperties.parryTimeSlow > 0)
                                counterTarget.addEffect(new MobEffectInstance(MobEffects.GLOWING, 5, 13, false, false));

                            //Execute GrandSlam
                            if(baseformProperties.grandSlamTime > 0)
                                baseformProperties.grandSlamTime += 1;

                            if(baseformProperties.grandSlamTime == 2)
                            {
                                float[] yawPitch = Utilities.getYawPitchFromVec(tpDir.reverse());
                                baseformProperties.atkRotPhase = yawPitch[0];
                                player.teleportTo(player.serverLevel(),
                                        counterTargetPos.x() + tpDir.x(),
                                        counterTargetPos.y() + tpDir.y(),
                                        counterTargetPos.z() + tpDir.z(),
                                        Collections.emptySet(),
                                        yawPitch[0], yawPitch[1]);
                            }

                            if(baseformProperties.grandSlamTime > 1 && baseformProperties.grandSlamTime <= 40)
                            {
                                Vec3 motionDir = Utilities.calculateViewVector(-30,baseformProperties.atkRotPhase);
                                //Teleport the player
                                player.setDeltaMovement(motionDir.scale(
                                        Math.max(0.1,
                                                1.0/(Math.max(1,baseformProperties.grandSlamTime/2))
                                        )));
                                player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                //Teleport the Target in Front of you
                                counterTarget.teleportTo(player.getX() + motionDir.x()*2,
                                                        player.getY() + motionDir.y(),
                                                        player.getZ() + motionDir.z()*2);
                                player.connection.send(new ClientboundTeleportEntityPacket(counterTarget));

                                //Damage Enemy
                                counterTarget.hurt(ModDamageTypes.getDamageSource(player.level(),ModDamageTypes.SONIC_BALL.getResourceKey(),player),
                                        1.0F);
                            }

                            if(baseformProperties.grandSlamTime == 40)
                            {
                                //Damage Target
                                counterTarget.hurt(ModDamageTypes.getDamageSource(player.level(),ModDamageTypes.SONIC_BALL.getResourceKey(),player),
                                        GRAND_SLAM_DMG);

                                //Knock Counter Target away
                                counterTarget.setDeltaMovement(
                                        Utilities.calculateViewVector(
                                                Math.min(90.0F,player.getXRot()+45.0F),
                                                player.getYRot()
                                        ).scale(2.0)
                                );
                                //Knock yourself back
                                player.setDeltaMovement(
                                        Utilities.calculateViewVector(
                                                Math.min(90.0F,player.getXRot()+45.0F),
                                                player.getYRot()
                                        ).scale(-0.75)
                                );
                                //Particle
                                PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                        ParticleTypes.FLASH,
                                        player.getX(),player.getY()+player.getEyeHeight()/2,player.getZ(),
                                        0.001,0.01F,player.getEyeHeight()/2,0.01F,
                                        1,true));
                                //Throw the Entity in our desired direction
                                player.connection.send(new ClientboundSetEntityMotionPacket(counterTarget));
                                player.connection.send(new ClientboundSetEntityMotionPacket(player));
                            }

                            if(baseformProperties.grandSlamTime > 40)
                            {
                                //If Timer Expires remove the slam time and Countered Tag
                                baseformProperties.grandSlamTime = 0;
                                baseformProperties.counteredEntity = new UUID(0L,0L);
                            }
                        }
                        catch (NullPointerException|ClassCastException ignored)
                        {
                            baseformProperties.grandSlamTime = 0;
                        }
                    }
                }

                //Slot 6
                {
                    //Meter Decay
                    baseformProperties.ultimateAtkMeter = baseformProperties.ultimateAtkMeter - ULT_DECAY_RATE;

                    //Ultimate Ready
                    if(baseformProperties.ultReady) {
                        PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                new DustParticleOptions(new Vector3f(0.4667F, 0F, 0.9961F), 1f),
                                player.getX() + 0.00, player.getY() + 0.85, player.getZ() + 0.00,
                                0.0, 0.80f, 1.00f, 0.80f, 1,
                                false)
                        );
                        PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                new DustParticleOptions(new Vector3f(0.05F, 0.05F, 1.0F), 1f),
                                player.getX() + 0.00, player.getY() + 0.85, player.getZ() + 0.00,
                                0.0, 0.80f, 1.00f, 0.80f, 1,
                                false)
                        );
                    }


                    if(baseformProperties.ultimateAtkMeter < 0.0)
                    {
                        if(baseformProperties.ultReady)
                            level.playSound(null,player.getX(),player.getY(),player.getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.MASTER, 1.0f, 2.0f);

                        baseformProperties.ultReady = false;
                        baseformProperties.ultimateAtkMeter = 0.0;
                    }

                    try
                    {
                        //Ultimate
                        if(baseformProperties.ultimateUse > 0)
                        {
                            baseformProperties.ultimateUse += 1;

                            //Get Target
                            if(baseformProperties.ultTarget == null) throw new NullPointerException("Null Enemy ID");
                            LivingEntity enemy = (LivingEntity) serverLevel.getEntity(baseformProperties.ultTarget);

                            if(enemy == null)  throw new NullPointerException("Enemy died/doesn't exist anymore: "+baseformProperties.ultTarget);


                            Vec3 playerPos = new Vec3(player.getX(),player.getY(),player.getZ());
                            Vec3 enemyPos = new Vec3(enemy.getX(),enemy.getY(),enemy.getZ());
                            double distanceFromEnemy = playerPos.distanceTo(enemyPos);

                            //Light Speed Dash
                            if(baseformProperties.ultimateUse == 2)
                            {
                                //Get Position of in front of enemy
                                Vec3 tpDir = playerPos.subtract(enemyPos).normalize().scale(2);
                                float[] yawPitch = Utilities.getYawPitchFromVec(tpDir.reverse());

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
                                enemy.setDeltaMovement(player.getLookAngle().scale(0.0));
                                player.connection.send(new ClientboundSetEntityMotionPacket(enemy));

                                //Make enemy invulnerable for the rest of the attack
                                enemy.setInvulnerable(true);

                                //Spawn Cloud
                                Utilities.summonEntity(
                                        ModEntityTypes.BASEFORM_PHANTOM_RUSH_CLOUD.get(), serverLevel,
                                        enemyPos,
                                        (phantomRushCloud) ->{
                                            phantomRushCloud.setDuration(50);
                                            if (baseformProperties.lightSpeedState == 2)    phantomRushCloud.setPlayerTextureType((byte)2);
                                            else if (baseformProperties.powerBoost)         phantomRushCloud.setPlayerTextureType((byte)1);
                                            else                                            phantomRushCloud.setPlayerTextureType((byte)0);
                                            phantomRushCloud.setOwner(player.getUUID());
                                        }
                                );
                            }

                            //Phantom Rush
                            else if(baseformProperties.ultimateUse < 50)
                            {
                                if(distanceFromEnemy > 4.0) throw new InterruptedException("Moved too Far from enemy");
                                if(baseformProperties.ultimateUse == 49) baseformProperties.atkRotPhase = player.getYRot();
                            }
                            //Uppercut
                            else if(baseformProperties.ultimateUse < 65)
                            {
                                Vec3 motionDir = Utilities.calculateViewVector(-30,baseformProperties.atkRotPhase);
                                //Teleport the player
                                player.setDeltaMovement(motionDir.scale(
                                        Math.max(0.1,
                                                1.0/(Math.max(1,(baseformProperties.ultimateUse-49)/2))
                                        )));
                                player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                //Teleport the Target in Front of you
                                enemy.teleportTo(player.getX() + motionDir.x()*2,
                                        player.getY() + motionDir.y(),
                                        player.getZ() + motionDir.z()*2);
                                player.connection.send(new ClientboundTeleportEntityPacket(enemy));

                                //Damage Enemy
                                enemy.hurt(ModDamageTypes.getDamageSource(player.level(),ModDamageTypes.SONIC_BALL.getResourceKey(),player),
                                        1.0F);
                            }

                            //Coriolis Punch
                            else if(baseformProperties.ultimateUse < 200)
                            {
                                //Hold Enemy in Place
                                enemy.setDeltaMovement(0,0,0);
                                player.connection.send(new ClientboundSetEntityMotionPacket(enemy));

                                Vec3 motionDir = Utilities.calculateViewVector(0,baseformProperties.atkRotPhase);
                                player.setDeltaMovement(motionDir.scale(3.0));
                                player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                //Boost Around the World
                                if(baseformProperties.ultimateUse == 85 ||
                                        baseformProperties.ultimateUse == 105 ||
                                        baseformProperties.ultimateUse == 120 ||
                                        baseformProperties.ultimateUse == 135 ||
                                        baseformProperties.ultimateUse == 150 ||
                                        baseformProperties.ultimateUse == 160 ||
                                        baseformProperties.ultimateUse == 170 ||
                                        baseformProperties.ultimateUse == 180)
                                {
                                    //Play sound
                                    level.playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.AIR_BOOST.get(), SoundSource.MASTER, 1.0f, 1.0f);

                                    //Particle
                                    Vec3 playerPosition = player.getPosition(0).add(new Vec3(0,0.75,0));
                                    PacketHandler.sendToALLPlayers(new ParticleRaycastPacketS2C(
                                            new DustParticleOptions(new Vector3f(0.000f,0.000f,1.000f), 2.0f),
                                            playerPosition,
                                            playerPosition.add(player.getLookAngle().scale(10))
                                    ));
                                    PacketHandler.sendToPlayer(player,new ParticleAuraPacketS2C(
                                            ParticleTypes.SONIC_BOOM,
                                            player.getX(),player.getY()+1.0,player.getZ(),
                                            0.0 ,0.0f,0.0f, 0.2f,1,true)
                                    );

                                    Vec3 destinationVector = enemyPos.add(motionDir.scale(-20));
                                    player.teleportTo(player.serverLevel(),
                                            destinationVector.x(),
                                            destinationVector.y(),
                                            destinationVector.z(),
                                            Collections.emptySet(),
                                            baseformProperties.atkRotPhase,0);

                                    //Boost Around the world
                                    PacketHandler.sendToALLPlayers(new ParticleRaycastPacketS2C(
                                            new DustParticleOptions(new Vector3f(0.000f,0.000f,1.000f), 2.0f),
                                            destinationVector,
                                            destinationVector.add(motionDir.scale(-10))
                                    ));
                                    PacketHandler.sendToPlayer(player,new ParticleAuraPacketS2C(
                                            ParticleTypes.SONIC_BOOM,
                                            destinationVector.x(),destinationVector.y()+player.getEyeHeight()/2,destinationVector.z(),
                                            0.0 ,0.0f,0.0f, 0.2f,1,true)
                                    );
                                    player.connection.send(new ClientboundTeleportEntityPacket(player));
                                }

                                //Normal Strike
                                if(baseformProperties.ultimateUse < 180 && distanceFromEnemy < 2.0)
                                {
                                    //Flash Particle
                                    PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                            ParticleTypes.FLASH,
                                            player.getX(),player.getY()+player.getEyeHeight()/2,player.getZ(),
                                            0.001,0.01F,player.getEyeHeight()/2,0.01F,
                                            1,true));

                                    //Play sound
                                    player.level().playSound(null,player.getX(),player.getY(),player.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.MASTER, 1.0f, 1.0f);
                                }

                                //Final Strike
                                else if(baseformProperties.ultimateUse >= 180 && distanceFromEnemy < 4.0)
                                {
                                    //Throw the Entity in our desired direction
                                    player.connection.send(new ClientboundSetEntityMotionPacket(enemy));
                                    player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                    //Particle
                                    PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                            ParticleTypes.FLASH,
                                            player.getX(),player.getY()+player.getEyeHeight()/2,player.getZ(),
                                            0.001,0.01F,player.getEyeHeight()/2,0.01F,
                                            1,true));

                                    //Teleport to the Position
                                    Vec3 tpDir = Utilities.calculateViewVector(0,baseformProperties.atkRotPhase);
                                    player.teleportTo(player.serverLevel(),
                                            enemyPos.x() - tpDir.x(),
                                            enemyPos.y() - tpDir.y(),
                                            enemyPos.z() - tpDir.z(),
                                            Collections.emptySet(),
                                            baseformProperties.atkRotPhase, 0);
                                    player.connection.send(new ClientboundTeleportEntityPacket(player));

                                    //Play sound
                                    player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.SMASH_HIT.get(), SoundSource.MASTER, 1.0f, 1.0f);

                                    baseformProperties.ultimateUse = 200;
                                }
                            }

                            //Coriolis Kick
                            else if(baseformProperties.ultimateUse >= 200)
                            {
                                if(baseformProperties.ultimateUse > 207)
                                {
                                    enemy.setInvulnerable(false);

                                    //Knock Counter Target away
                                    enemy.setDeltaMovement(
                                            Utilities.calculateViewVector(
                                                    Math.min(90.0F,player.getXRot()+45.0F),
                                                    player.getYRot()
                                            ).scale(4.0)
                                    );
                                    //Knock yourself back
                                    player.setDeltaMovement(
                                            Utilities.calculateViewVector(
                                                    Math.min(90.0F,player.getXRot()+45.0F),
                                                    player.getYRot()
                                            ).scale(-0.75)
                                    );

                                    //Flash Particle
                                    PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                            ParticleTypes.FLASH,
                                            player.getX(),player.getY()+player.getEyeHeight()/2,player.getZ(),
                                            0.001,0.01F,player.getEyeHeight()/2,0.01F,
                                            1,true));

                                    //Update Motion
                                    player.connection.send(new ClientboundSetEntityMotionPacket(enemy));
                                    player.connection.send(new ClientboundSetEntityMotionPacket(player));

                                    //Final Dmg
                                    enemy.hurt(ModDamageTypes.getDamageSource(player.level(),ModDamageTypes.SONIC_ULTIMATE.getResourceKey(),player),
                                            ULTIMATE_DAMAGE);
                                }
                                else{
                                    //Freeze them in place
                                    player.setDeltaMovement(0,0,0);
                                    enemy.setDeltaMovement(0,0,0);

                                    //Update Motion
                                    player.connection.send(new ClientboundSetEntityMotionPacket(enemy));
                                    player.connection.send(new ClientboundSetEntityMotionPacket(player));
                                }


                            }
                        }

                        //Stop Ultimate
                        if(baseformProperties.ultimateUse > 207)
                        {
                            throw new InterruptedException("Move Timed out");
                        }
                    }
                    catch(NullPointerException|InterruptedException|ClassCastException e)
                    {
                        if(baseformProperties.ultTarget != null) {
                            LivingEntity enemy = (LivingEntity) serverLevel.getEntity(baseformProperties.ultTarget);
                            if(enemy != null)  enemy.setInvulnerable(false);
                        }
                        baseformProperties.ultimateUse = 0;
                        player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.0);
                        player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);
                    }
                }
            }

            PacketHandler.sendToALLPlayers(
                    new SyncPlayerFormS2C(
                            player.getId(),
                            playerSonicForm
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

            //Data
            //Effects
            {
                //Speed
                if(baseformProperties.boostLvl == 0)
                    player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);

                //Jump
                if(!player.hasEffect(MobEffects.JUMP)) player.addEffect(new MobEffectInstance(MobEffects.JUMP, -1, 2, false, false));
                else player.getEffect(MobEffects.JUMP).update(new MobEffectInstance(MobEffects.JUMP, -1, 2, false, false));

                //Resistance
                if(!player.hasEffect(MobEffects.DAMAGE_RESISTANCE)) player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, 3, false, false));
                else player.getEffect(MobEffects.DAMAGE_RESISTANCE).update(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, 3, false, false));

                //Strength
                if(!player.hasEffect(MobEffects.DAMAGE_BOOST)) player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 1, false, false));
                else player.getEffect(MobEffects.DAMAGE_BOOST).update(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 1, false, false));

                //Haste
                if(!player.hasEffect(MobEffects.DIG_SPEED)) player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, -1, 1, false, false));
                else player.getEffect(MobEffects.DIG_SPEED).update(new MobEffectInstance(MobEffects.DIG_SPEED, -1, 1, false, false));

                //Immunities: Slowdown
                if(player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);

                //Immunities: Mining Fatigue
                if(player.hasEffect(MobEffects.DIG_SLOWDOWN))      player.removeEffect(MobEffects.DIG_SPEED);
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
            {}
            //Slot 4
            {}
            //Slot 5
            {
                //Movement Speed Removal, In the case that it trips
                if(baseformProperties.parryTime <= 0 && player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.PARRY_HOLD))
                    player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.PARRY_HOLD.getId());
            }


            PacketHandler.sendToALLPlayers(
                    new SyncPlayerFormS2C(
                            player.getId(),
                            playerSonicForm
                    ));
        });
    }
}
