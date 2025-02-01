package net.sonicrushxii.beyondthehorizon.event_handler.client_handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import org.joml.Vector3f;

import java.util.Objects;

public class ClientPacketHandler
{
    public static void playerFormSync(int playerId, PlayerSonicForm player_SonicForm)
    {
        // This code is run on the client side
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        // Get the player entity by ID
        Player player = (Player) mc.level.getEntity(playerId);
        if (player == null) return;

        // Update the player's capability data on the client side
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
            playerSonicForm.copyFrom(player_SonicForm);
        });
    }

    public static void clientParticleAura(String particle_Type, double absX, double absY, double absZ, double speed, float radiusX, float radiusY, float radiusZ, short count, boolean force, float red, float green, float blue, float scale) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel world = mc.level;
        AbstractClientPlayer player = mc.player;

        if (player != null && world != null) {
            ParticleType<?> particleType = ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(particle_Type));
            ParticleOptions particleOptions;

            if (particleType == ParticleTypes.DUST) {
                particleOptions = new DustParticleOptions(new Vector3f(red, green, blue), scale);
            } else {
                particleOptions = (ParticleOptions) particleType;
            }

            assert particleOptions != null;
            Utilities.displayParticle(player.level(), particleOptions, absX, absY, absZ,
                    radiusX, radiusY, radiusZ,
                    speed, count, force);
        }
    }

    public static void clientParticleDir(String particle_Type, double absX, double absY, double absZ, double speedX,double speedY,double speedZ, float radiusX, float radiusY, float radiusZ, short count, boolean force, float red, float green, float blue, float scale)
    {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel world = mc.level;
        AbstractClientPlayer player = mc.player;

        if (player != null && world != null) {
            ParticleType<?> particleType = ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(particle_Type));
            ParticleOptions particleOptions;

            if (particleType == ParticleTypes.DUST) {
                particleOptions = new DustParticleOptions(new Vector3f(red, green, blue), scale);
            } else {
                particleOptions = (ParticleOptions) particleType;
            }

            assert particleOptions != null;
            Utilities.displayParticle(player.level(), particleOptions, absX,absY,absZ,
                    radiusX,radiusY,radiusZ,
                    speedX, speedY,speedZ,
                    count, force);
        }
    }

    public static void clientParticleRaycast(Vector3f pos1, Vector3f pos2, String particle_Type, float red, float green, float blue, float scale)
    {
        ClientLevel world = Minecraft.getInstance().level;
        if (world != null)
        {
            ParticleType<?> particleType = ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(particle_Type));
            ParticleOptions particleOptions;

            if (particleType == ParticleTypes.DUST) {
                particleOptions = new DustParticleOptions(new Vector3f(red, green, blue), scale);
            } else {
                particleOptions = (ParticleOptions) particleType;
            }

            Utilities.particleRaycast(
                    world, particleOptions,
                    new Vec3(pos1.x(), pos1.y(), pos1.z()),
                    new Vec3(pos2.x(), pos2.y(), pos2.z())
            );
        }
    }

    public static void clientPlaysound(ResourceLocation soundLocation, BlockPos emitterPosition, float volume, float pitch)
    {
        // This code is run on the client side
        Minecraft mc = Minecraft.getInstance();
        ClientLevel world = mc.level;
        AbstractClientPlayer player = mc.player;

        if(player != null && world != null) {
            if(player.blockPosition().distSqr(emitterPosition) < 576)
                world.playLocalSound(emitterPosition.getX(),emitterPosition.getY(),emitterPosition.getZ(),
                        Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(soundLocation)),
                        SoundSource.MASTER, volume, pitch, true);
        }
    }

    public static void clientStopSound(ResourceLocation soundLocation)
    {
        // This code is run on the client side
        Minecraft mc = Minecraft.getInstance();
        ClientLevel world = mc.level;
        AbstractClientPlayer player = mc.player;

        if(player != null && world != null) {
            for(SoundSource soundSource : SoundSource.values())
                mc.getSoundManager().stop(soundLocation, soundSource);
        }
    }

    public static void clientProjSync(int entityId, boolean noGravity, Vec3 deltaMovement)
    {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel world = mc.level;
        try {
            Projectile projectile = (Projectile) world.getEntity(entityId);
            projectile.setNoGravity(noGravity);
            projectile.setDeltaMovement(deltaMovement);
        } catch(NullPointerException|ClassCastException ignored) {}
    }

    public static void cyloopParticle(Vec3 position)
    {
        AbstractClientPlayer player = Minecraft.getInstance().player;
        Utilities.displayParticle(player, (new DustParticleOptions(new Vector3f(0.000f, 1.0f, 1.000f), 2f)),
                position.x, position.y + 0.5, position.z,
                0.55f, 0.55f, 0.55f,
                0.01, 3, true);
        Utilities.displayParticle(player, (new DustParticleOptions(new Vector3f(0.000f, 0.11f, 1.000f), 2f)),
                position.x, position.y + 0.5, position.z,
                0.65f, 0.65f, 0.65f,
                0.01, 2, false);
        Utilities.displayParticle(player, (new DustParticleOptions(new Vector3f(1.000f, 0.0f, 0.890f), 2f)),
                position.x,position.y + 0.5, position.z,
                0.55f, 0.55f, 0.55f,
                0.01, 2, false);
        Utilities.displayParticle(player, (ParticleTypes.FIREWORK),
                position.x, position.y + 0.5, position.z,
                0.55f, 0.55f, 0.55f,
                0.01, 1, false);
    }

    public static void wildRushParticle(double absX, double absY, double absZ)
    {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel world = mc.level;
        AbstractClientPlayer player = mc.player;

        if (player != null && world != null)
        {
            Utilities.displayParticle(player,
                    ParticleTypes.FIREWORK,
                    absX, absY, absZ,
                    0.5f, 0.5f, 0.5f,
                    0.01, 10, false);
            Utilities.displayParticle(player,
                    new DustParticleOptions(new Vector3f(0f,0f,1f),1.5f),
                    absX, absY, absZ,
                    0.5f, 0.5f, 0.5f,
                    0.01, 10, false);
        }
    }

    public static void sonicWindParticle(double absX, double absY, double absZ, byte phase)
    {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel world = mc.level;
        AbstractClientPlayer player = mc.player;

        if (player != null && world != null)
        {
            float p = 10.0F-phase*1.25F;
            for(double offset = 0; offset <= 2*Math.PI; offset += Math.PI/2)
            {
                //Get Vertical Component
                double particleY = (0.4)*p*Math.sin(phase/2.0F+offset+p/2);

                //Get Horizontal Component
                double horizontalPart = (0.4)*p*Math.cos(phase/2.0F+offset+p/2);
                double particleX = Math.sin((-player.getYRot()+90) * (Math.PI / 180)) * 0.707 * horizontalPart;
                double particleZ = Math.cos((-player.getYRot()+90) * (Math.PI / 180)) * 0.707 * horizontalPart;

                Utilities.displayParticle(world,
                        new DustParticleOptions(new Vector3f(0.00f,0.0f,1f),1f),
                        absX+particleX,absY+particleY,absZ+particleZ,
                        0.35f-(0.035f)*p,0.25f-(0.035f)*p,0.25f-(0.035f)*p,
                        0.001, 2, false
                );
                Utilities.displayParticle(world,
                        new DustParticleOptions(new Vector3f(0.00f,1f,1f),1f),
                        absX+particleX,absY+particleY,absZ+particleZ,
                        0.25f-(0.025f)*p,0.25f-(0.025f)*p,0.25f-(0.025f)*p,
                        0.001, 3, false
                );
            }
        }
    }
}
