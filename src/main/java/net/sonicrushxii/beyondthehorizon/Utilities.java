package net.sonicrushxii.beyondthehorizon;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Utilities {

    static Random random = new Random();

    static CompoundTag baseformArmorNBTTag; static {
        baseformArmorNBTTag = new CompoundTag();
        ListTag enchantmentList = new ListTag();
        CompoundTag enchantment = new CompoundTag();
        enchantment.putString("id", "minecraft:binding_curse");
        enchantment.putShort("lvl", (short) 1);
        enchantmentList.add(enchantment);
        baseformArmorNBTTag.put("Enchantments", enchantmentList);
        baseformArmorNBTTag.putInt("HideFlags", 127);
        baseformArmorNBTTag.putByte("Unbreakable", (byte) 1);
        baseformArmorNBTTag.putByte("BeyondTheHorizon", (byte) 1);
    }
    static ItemStack baseformSonicHead; static {
        baseformSonicHead = new ItemStack(Items.PLAYER_HEAD);
        CompoundTag nbt = new CompoundTag();

        // Custom NBT data
        nbt.putByte("BeyondTheHorizon", (byte) 2);

        // SkullOwner tag
        CompoundTag skullOwner = new CompoundTag();
        CompoundTag properties = new CompoundTag();
        ListTag textures = new ListTag();
        CompoundTag texture = new CompoundTag();
        texture.putString("Value", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTBjN2NlZWNjODliNTY0MjNhOWU4YWFiMTE3NjRkZTI5MDIyNjU4MzA5YTUyNjY2M2JmMzQyNGY0N2NhZDlmOCJ9fX0=");
        textures.add(texture);
        properties.put("textures", textures);
        skullOwner.put("Properties", properties);
        skullOwner.putIntArray("Id", new int[]{512370214, -95272899, -2003262887, 1067375885});
        nbt.put("SkullOwner", skullOwner);

        // Display tag
        CompoundTag display = new CompoundTag();
        ListTag lore = new ListTag();
        lore.add(StringTag.valueOf("{\"text\":\"Adapted from Sonic Frontiers\",\"color\": \"light_purple\"}"));
        display.put("Lore", lore);
        display.putString("Name", "{\"text\":\"Sonic Head\",\"color\": \"blue\",\"italic\": false}");
        nbt.put("display", display);

        baseformSonicHead.setTag(nbt);
    }

    public static final HashSet<String> passableBlocks =
            new HashSet<>(Arrays.asList(
                    "minecraft:air",
                    "minecraft:void_air",
                    "minecraft:cave_air",
                    "minecraft:oak_sapling",
                    "minecraft:spruce_sapling",
                    "minecraft:birch_sapling",
                    "minecraft:jungle_sapling",
                    "minecraft:acacia_sapling",
                    "minecraft:dark_oak_sapling",
                    "minecraft:beetroots",
                    "minecraft:water",
                    "minecraft:lava",
                    "minecraft:light",
                    "minecraft:powered_rail",
                    "minecraft:detector_rail",
                    "minecraft:seagrass",
                    "minecraft:tall_seagrass",
                    "minecraft:grass",
                    "minecraft:fern",
                    "minecraft:dead_bush",
                    "minecraft:dandelion",
                    "minecraft:poppy",
                    "minecraft:blue_orchid",
                    "minecraft:allium",
                    "minecraft:azure_bluet",
                    "minecraft:red_tulip" ,
                    "minecraft:orange_tulip" ,
                    "minecraft:white_tulip" ,
                    "minecraft:pink_tulip" ,
                    "minecraft:oxeye_daisy" ,
                    "minecraft:cornflower" ,
                    "minecraft:wither_rose",
                    "minecraft:lily_of_the_valley",
                    "minecraft:brown_mushroom",
                    "minecraft:red_mushroom",
                    "minecraft:torch",
                    "minecraft:wall_torch",
                    "minecraft:fire",
                    "minecraft:soul_fire",
                    "minecraft:wheat",
                    "minecraft:wall_torch",
                    "minecraft:oak_sign",
                    "minecraft:spruce_sign",
                    "minecraft:birch_sign",
                    "minecraft:jungle_sign",
                    "minecraft:acacia_sign",
                    "minecraft:dark_oak_sign",
                    "minecraft:oak_pressure_plate",
                    "minecraft:spruce_pressure_plate",
                    "minecraft:birch_pressure_plate",
                    "minecraft:jungle_pressure_plate",
                    "minecraft:acacia_pressure_plate",
                    "minecraft:dark_oak_pressure_plate",
                    "minecraft:redstone",
                    "minecraft:restone_torch",
                    "minecraft:redstone_wall_torch",
                    "minecraft:repeater",
                    "minecraft:stone_button",
                    "minecraft:oak_button",
                    "minecraft:spruce_button",
                    "minecraft:birch_button",
                    "minecraft:jungle_button",
                    "minecraft:acacia_button",
                    "minecraft:dark_oak_button",
                    "minecraft:candle",
                    "minecraft:white_candle",
                    "minecraft:orange_candle",
                    "minecraft:magenta_candle",
                    "minecraft:light_blue_candle",
                    "minecraft:yellow_candle",
                    "minecraft:lime_candle",
                    "minecraft:pink_candle",
                    "minecraft:gray_candle",
                    "minecraft:light_gray_candle",
                    "minecraft:cyan_candle",
                    "minecraft:purple_candle",
                    "minecraft:blue_candle",
                    "minecraft:brown_candle",
                    "minecraft:green_candle",
                    "minecraft:red_candle",
                    "minecraft:black_candle",
                    "minecraft:structure_void",
                    "minecraft:kelp",
                    "minecraft:nether_wart",
                    "minecraft:ladder",
                    "minecraft:vines",
                    "minecraft:lever",
                    "minecraft:white_carpet",
                    "minecraft:orange_carpet",
                    "minecraft:magenta_carpet",
                    "minecraft:light_blue_carpet",
                    "minecraft:yellow_carpet",
                    "minecraft:lime_carpet",
                    "minecraft:pink_carpet",
                    "minecraft:gray_carpet",
                    "minecraft:light_gray_carpet",
                    "minecraft:cyan_carpet",
                    "minecraft:purple_carpet",
                    "minecraft:blue_carpet",
                    "minecraft:brown_carpet",
                    "minecraft:green_carpet",
                    "minecraft:red_carpet",
                    "minecraft:black_carpet",
                    "minecraft:white_banner",
                    "minecraft:orange_banner",
                    "minecraft:magenta_banner",
                    "minecraft:light_blue_banner",
                    "minecraft:yellow_banner",
                    "minecraft:lime_banner",
                    "minecraft:pink_banner",
                    "minecraft:gray_banner",
                    "minecraft:light_gray_banner",
                    "minecraft:cyan_banner",
                    "minecraft:purple_banner",
                    "minecraft:blue_banner",
                    "minecraft:brown_banner",
                    "minecraft:green_banner",
                    "minecraft:red_banner",
                    "minecraft:black_banner",
                    "minecraft:white_wall_banner",
                    "minecraft:orange_wall_banner",
                    "minecraft:magenta_wall_banner",
                    "minecraft:light_blue_wall_banner",
                    "minecraft:yellow_wall_banner",
                    "minecraft:lime_wall_banner",
                    "minecraft:pink_wall_banner",
                    "minecraft:gray_wall_banner",
                    "minecraft:light_gray_wall_banner",
                    "minecraft:cyan_wall_banner",
                    "minecraft:purple_wall_banner",
                    "minecraft:blue_wall_banner",
                    "minecraft:brown_wall_banner",
                    "minecraft:green_wall_banner",
                    "minecraft:red_wall_banner",
                    "minecraft:black_wall_banner",
                    "minecraft:sunflower",
                    "minecraft:lilac",
                    "minecraft:rose_bush",
                    "minecraft:peony",
                    "minecraft:tall_grass",
                    "minecraft:large_fern")
            );

    //User Defined Functions for things that should be available
    public static Vec3 calculateViewVector(float pXRot, float pYRot)
    {
        float f = pXRot * ((float)Math.PI / 180F);
        float f1 = -pYRot * ((float)Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return new Vec3((f3 * f4), (-f5), (f2 * f4));
    }

    public static void displayParticle(Player player , ParticleOptions particleType,
                                       double absX, double absY, double absZ,
                                       float radiusX, float radiusY, float radiusZ,
                                       double speedX, double speedY, double speedZ,
                                       int count, boolean force) {
        Level world = player.level();

        for (int i = 0; i < count; i++)
        {
            double x = random.nextGaussian();
            double y = random.nextGaussian();
            double z = random.nextGaussian();

            double nf = Math.sqrt(x*x + y*y + z*z);

            x = (x/nf)*radiusX;
            y = (y/nf)*radiusY;
            z = (z/nf)*radiusZ;

            // Spawn the particle effect
            world.addParticle(particleType, force,
                    x + (absX),
                    y + (absY),
                    z + (absZ),
                    speedX, speedY, speedZ);
        }
    }

    public static void displayParticle(Player player , ParticleOptions particleType,
                                       double absX, double absY, double absZ,
                                       float radiusX, float radiusY, float radiusZ,
                                       double speed,
                                       int count, boolean force) {
        Level world = player.level();

        for (int i = 0; i < count; i++) {

            double x = random.nextGaussian();
            double y = random.nextGaussian();
            double z = random.nextGaussian();

            double nf = Math.sqrt(x*x + y*y + z*z);

            x = (x/nf)*radiusX;
            y = (y/nf)*radiusY;
            z = (z/nf)*radiusZ;

            // Calculate the particle's initial position
            double particleX = x + (absX);
            double particleY = y + (absY);
            double particleZ = z + (absZ);

            // Calculate the direction vector from the origin to the particle
            double dirX = particleX - x;
            double dirY = particleY - y;
            double dirZ = particleZ - z;

            // Normalize the direction vector
            double length = Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
            if (length != 0) {
                dirX /= length;
                dirY /= length;
                dirZ /= length;
            }

            // Scale the direction vector by the desired speed
            double speedX = dirX * speed;
            double speedY = dirY * speed;
            double speedZ = dirZ * speed;

            // Spawn the particle effect
            world.addParticle(particleType, force,
                    particleX, particleY, particleZ,
                    speedX, speedY, speedZ);
        }
    }

    public static void particleRaycast(Level world, ParticleOptions particleType,
                                        Vec3 pos1, Vec3 pos2)
    {
        // Calculate the vector from pos1 to pos2
        Vec3 direction = pos2.subtract(pos1);

        double distance = direction.length();
        Vec3 directionNormalized = direction.normalize();

        for (int i = 0; i <= (int) distance*2; i++) {
            Vec3 point = pos1.add(directionNormalized.scale((i+1)/2.0));
            world.addParticle(particleType,
                    point.x, point.y, point.z,
                    0, 0, 0);
        }
    }
}
