package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.speed_blitz;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonicrushxii.beyondthehorizon.ModUtils;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleRaycastPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import net.sonicrushxii.beyondthehorizon.modded.ModEffects;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.List;


public class SpeedBlitz {

    public SpeedBlitz() {}

    public SpeedBlitz(FriendlyByteBuf buffer) {}

    public void encode(FriendlyByteBuf buffer) {}

    public static Vec3 scanFoward(Player player)
    {
        Vec3 currentPos = player.getPosition(0).add(0.0, 1.0, 0.0);
        Vec3 lookAngle = player.getLookAngle();

        //Scan Forward for enemies
        for (int i = 0; i < 10; ++i)
        {
            //Increment Current Position Forward
            currentPos = currentPos.add(lookAngle);
            AABB boundingBox = new AABB(currentPos.x() + 3, currentPos.y() + 3, currentPos.z() + 3,
                    currentPos.x() - 3, currentPos.y() - 3, currentPos.z() - 3);

            List<LivingEntity> nearbyEntities = player.level().getEntitiesOfClass(
                    LivingEntity.class, boundingBox,
                    (enemy) -> !enemy.is(player) && enemy.isAlive() && enemy.hasEffect(ModEffects.SPEED_BLITZED.get()));

            //If enemy is found then Target it
            if (!nearbyEntities.isEmpty())
            {
                //Select Closest target
                LivingEntity target = Collections.min(nearbyEntities, (e1, e2) -> {
                    Vec3 e1Pos = new Vec3(e1.getX(), e1.getY(), e1.getZ());
                    Vec3 e2Pos = new Vec3(e2.getX(), e2.getY(), e2.getZ());

                    return (int) (e1Pos.distanceToSqr(player.getX(),player.getY(),player.getZ()) - e2Pos.distanceToSqr(player.getX(),player.getY(),player.getZ()));
                });

                return new Vec3(target.getX(),target.getY(),target.getZ());
            }
        }

        return null;
    }

    public static void performToggleSpeedBlitz(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Add Data
            baseformProperties.speedBlitz = !baseformProperties.speedBlitz;

            PacketHandler.sendToALLPlayers(
                    new SyncPlayerFormS2C(
                            player.getId(),
                            playerSonicForm
                    ));
        });
    }

    public static void performSpeedDash(ServerPlayer player, Vec3 enemyPos)
    {
        //Calculate new Position
        Vec3 playerPos = new Vec3(player.getX(),player.getY(),player.getZ());
        Vec3 tpDirection = enemyPos.subtract(playerPos).normalize();

        Vec3 newPlayerPos = enemyPos.add(tpDirection.scale(3));
        float[] yawPitch = ModUtils.calculateFacing(newPlayerPos,enemyPos);

        //Display Raycast Particle
        PacketHandler.sendToPlayer(player, new ParticleRaycastPacketS2C(
                new DustParticleOptions(new Vector3f(0.000f,0.969f,1.000f), 1.5f),
                playerPos.add(0,0.75,0),
                newPlayerPos.add(0,0.75,0)
        ));
        //Sound
        player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.BLITZ.get(), SoundSource.MASTER, 0.5f, 1.0f);

        //Current Combo Duration
        MobEffectInstance currComboEffect = player.getEffect(ModEffects.SPEED_BLITZING.get());
        if(currComboEffect == null)
            player.addEffect(new MobEffectInstance(ModEffects.SPEED_BLITZING.get(), 20, 0, false, false));
        else
            currComboEffect.update(new MobEffectInstance(ModEffects.SPEED_BLITZING.get(), 20, 0, false, false));

        //Consume Speed Blitz Dash
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();
            baseformProperties.speedBlitzDashes -= 1;
            PacketHandler.sendToALLPlayers(
                    new SyncPlayerFormS2C(
                            player.getId(),
                            playerSonicForm
                    ));
        });

        //Check if position is Safe
        Level world = player.level();
        String destinationBlockName = ForgeRegistries.BLOCKS.getKey(
                world.getBlockState(
                        new BlockPos(
                                (int)Math.round(newPlayerPos.x),
                                (int)Math.round(newPlayerPos.y+1),
                                (int)Math.round(newPlayerPos.z)
                        )
                ).getBlock())+"";
        //Set New Position
        if(ModUtils.passableBlocks.contains(destinationBlockName))
        {
            player.teleportTo(player.serverLevel(), newPlayerPos.x, newPlayerPos.y, newPlayerPos.z,
                    Collections.emptySet(), yawPitch[0], yawPitch[1]);
            player.connection.send(new ClientboundPlayerPositionPacket(newPlayerPos.x, newPlayerPos.y, newPlayerPos.z,
                    yawPitch[0], yawPitch[1], Collections.emptySet(), 0));
        }

        //Set Speed to Zero
        player.setDeltaMovement(new Vec3(0.0,0.0,0.0));
        player.connection.send(new ClientboundSetEntityMotionPacket(player));
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                    {
                        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
                            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                            Vec3 enemyPos = scanFoward(player);
                            if(enemyPos == null)
                                performToggleSpeedBlitz(player);
                            else if(baseformProperties.speedBlitzDashes > 0)
                                performSpeedDash(player,enemyPos);

                            PacketHandler.sendToALLPlayers(
                                    new SyncPlayerFormS2C(
                                            player.getId(),
                                            playerSonicForm
                                    ));
                        });
                    }
                });
        ctx.setPacketHandled(true);
    }
}
