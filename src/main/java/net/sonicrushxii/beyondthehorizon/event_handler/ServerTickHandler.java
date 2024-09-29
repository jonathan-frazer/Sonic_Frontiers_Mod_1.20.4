package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformHandler;

import java.lang.reflect.Field;

public class ServerTickHandler {
    private static int tickCounter = 0;
    private static final int TICKS_PER_SECOND = 20;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent.Post event)
    {
        // Get the server instance
        MinecraftServer server = event.getServer(); // Get the level (world) you want to execute the command in
        ServerLevel serverLevel = server.getLevel(Level.OVERWORLD); // or any other dimension

        if(serverLevel != null)
        {
            //Helps Run OnServerSecond
            ++tickCounter;
            if(tickCounter >= TICKS_PER_SECOND){
                onServerSecond(server);
                tickCounter=0;
            }
        }
    }

    public void onServerSecond(MinecraftServer server)
    {
        //Floor Crafting
        {
            for (ServerLevel world : server.getAllLevels()) {
                for (Entity entity : world.getEntities().getAll()) {
                    if (entity instanceof ItemEntity itemEntity) {
                        ItemStack itemInfo = itemEntity.getItem();
                        if (itemInfo.getItem() == Items.BEACON && itemInfo.getCount() == 1) {
                            boolean crafted = false;
                            for (ItemEntity itemEntity1 : world.getEntitiesOfClass(ItemEntity.class,
                                    new AABB(itemEntity.getX() + 1, itemEntity.getY() + 1, itemEntity.getZ() + 1,
                                            itemEntity.getX() - 1, itemEntity.getY() - 1, itemEntity.getZ() - 1),
                                    (itemEntity1) -> {
                                        ItemStack itemInfo1 = itemEntity1.getItem();
                                        if (itemInfo1.getItem() != Items.POTION) return false;
                                        if (itemInfo1.getCount() != 1) return false;
                                        CompoundTag itemNBT1 = itemInfo1.getTag();
                                        if (itemNBT1 == null) return false;
                                        if (!itemNBT1.contains("Potion")) return false;
                                        return itemNBT1.getString("Potion").equals("minecraft:strong_swiftness");
                                    })) {
                                for (ItemEntity itemEntity2 : world.getEntitiesOfClass(ItemEntity.class,
                                        new AABB(itemEntity1.getX() + 1, itemEntity1.getY() + 1, itemEntity1.getZ() + 1,
                                                itemEntity1.getX() - 1, itemEntity1.getY() - 1, itemEntity1.getZ() - 1),
                                        (itemEntity2) -> {
                                            ItemStack itemInfo2 = itemEntity2.getItem();
                                            if (itemInfo2.getItem() != Items.AMETHYST_SHARD) return false;
                                            return itemInfo2.getCount() == 7;
                                        })) {
                                    for (ItemEntity itemEntity3 : world.getEntitiesOfClass(ItemEntity.class,
                                            new AABB(itemEntity2.getX() + 1, itemEntity2.getY() + 1, itemEntity2.getZ() + 1,
                                                    itemEntity2.getX() - 1, itemEntity2.getY() - 1, itemEntity2.getZ() - 1),
                                            (itemEntity3) -> {
                                                ItemStack itemInfo3 = itemEntity3.getItem();
                                                if (itemInfo3.getItem() != Items.BLUE_ICE) return false;
                                                return itemInfo3.getCount() == 15;
                                            })) {
                                        for (ItemEntity itemEntity4 : world.getEntitiesOfClass(ItemEntity.class,
                                                new AABB(itemEntity3.getX() + 1, itemEntity3.getY() + 1, itemEntity3.getZ() + 1,
                                                        itemEntity3.getX() - 1, itemEntity3.getY() - 1, itemEntity3.getZ() - 1),
                                                (itemEntity4) -> {
                                                    ItemStack itemInfo4 = itemEntity4.getItem();
                                                    if (itemInfo4.getItem() != Items.LAPIS_BLOCK) return false;
                                                    return itemInfo4.getCount() == 30;
                                                })) {
                                            //Set Crafting to Be True
                                            crafted = true;
                                            //Spawn Head
                                            {
                                                //Instead of Killing it, Transform the Beacon into the sonic head
                                                itemEntity.setItem(BaseformHandler.baseformSonicHead);
                                                itemEntity.setDeltaMovement(0,0.1,0);
                                            }
                                            //Spawn Fireworks
                                            {
                                                // Create the ItemStack for the firework rocket
                                                ItemStack fireworkStack = new ItemStack(Items.FIREWORK_ROCKET, 1);

                                                // Create the NBT data for the firework rocket
                                                CompoundTag fireworkTag = new CompoundTag();
                                                ListTag explosions = new ListTag();

                                                CompoundTag explosion = new CompoundTag();
                                                explosion.putByte("Type", (byte) 4); // Star-shaped
                                                explosion.put("Colors", new IntArrayTag(new int[]{65501, 16711918, 1966335}));
                                                explosion.put("FadeColors", new IntArrayTag(new int[]{1966335, 65501}));
                                                explosions.add(explosion);

                                                CompoundTag fireworks = new CompoundTag();
                                                fireworks.put("Explosions", explosions);
                                                fireworkTag.put("Fireworks", fireworks);

                                                // Add the NBT data to the ItemStack
                                                fireworkStack.setTag(fireworkTag);

                                                // Create an ItemEntity to represent the firework rocket in the world
                                                FireworkRocketEntity fireworkEntity = new FireworkRocketEntity(world,
                                                        itemEntity.getX(), itemEntity.getY()+0.1, itemEntity.getZ(), fireworkStack);

                                                try {
                                                    Field privateField = FireworkRocketEntity.class.getDeclaredField("lifetime");
                                                    privateField.setAccessible(true);
                                                    privateField.set(fireworkEntity,0);
                                                    privateField.setAccessible(false);
                                                } catch (NoSuchFieldException | IllegalAccessException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                // Add the ItemEntity to the world
                                                world.addFreshEntity(fireworkEntity);
                                            }
                                            //Playsound
                                            world.playSound(null,itemEntity.getX(),itemEntity.getY(),itemEntity.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.MASTER, 1.0f, 1.0f);
                                            itemEntity4.remove(Entity.RemovalReason.KILLED);
                                        }
                                        if (crafted) itemEntity3.remove(Entity.RemovalReason.KILLED);
                                    }
                                    if (crafted) itemEntity2.remove(Entity.RemovalReason.KILLED);
                                }
                                if (crafted) itemEntity1.remove(Entity.RemovalReason.KILLED);
                            }
                        }
                    }
                }
            }
        }

    }
}
