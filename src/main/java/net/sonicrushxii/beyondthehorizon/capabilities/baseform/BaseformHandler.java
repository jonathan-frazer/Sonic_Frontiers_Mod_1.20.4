package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonicrushxii.beyondthehorizon.KeyBindings;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.client.ClientFormData;
import net.sonicrushxii.beyondthehorizon.client.DoubleTapHandler;
import net.sonicrushxii.beyondthehorizon.client.VirtualSlotHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModItems;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.boost.*;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack.LightspeedCancel;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack.LightspeedCharge;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack.LightspeedDecay;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack.LightspeedEffect;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StartSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StopSprint;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step.StepDown;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step.StepDownDouble;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.danger_sense.DangerSenseToggle;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.danger_sense.DangerSenseEmit;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.doublejump.DoubleJump;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.doublejump.DoubleJumpEnd;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleAuraPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.PlayerStopSoundPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.VirtualSlotSyncS2C;
import net.sonicrushxii.beyondthehorizon.scheduler.ScheduledTask;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BaseformHandler
{
    public static CompoundTag baseformArmorNBTTag; static {
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
    public static ItemStack baseformSonicHead; static {
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

    private static ScheduledTask lightSpeedCanceller = null;

    public static void performBaseformActivation(ServerPlayer player)
    {
        //Equip Armor
        //SET ARMOR NBT DATA(COMMON)
        {
            Iterator<ItemStack> armorItems = player.getArmorSlots().iterator();
            if (armorItems.next().isEmpty()) {
                ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_BOOTS.get());
                itemToPlace.setTag(baseformArmorNBTTag);
                player.setItemSlot(EquipmentSlot.FEET, itemToPlace);
            }
            if (armorItems.next().isEmpty()) {
                ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_LEGGINGS.get());
                itemToPlace.setTag(baseformArmorNBTTag);
                player.setItemSlot(EquipmentSlot.LEGS, itemToPlace);
            }
            if (armorItems.next().isEmpty()) {
                ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_CHESTPLATE.get());
                itemToPlace.setTag(baseformArmorNBTTag);
                player.setItemSlot(EquipmentSlot.CHEST, itemToPlace);
            }
        }
        //Add Data
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm->{
            playerSonicForm.activateBaseForm();
            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            playerSonicForm.getFormProperties()
                    ));
        });

        //Initialize Virtual Slot Handler
        PacketHandler.sendToPlayer(player,new VirtualSlotSyncS2C((byte)7));

        //Effects
        {
            player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, -1, 2, false, false));
        }

        //Commands
        {
            CommandSourceStack commandSourceStack = player.createCommandSourceStack().withPermission(4).withSuppressedOutput();
            MinecraftServer server = player.serverLevel().getServer();
            server.
                    getCommands().
                    performPrefixedCommand(commandSourceStack,"function beyondthehorizon:baseform/activate");
        }
    }

    public static void performBaseformClientTick(LocalPlayer player, CompoundTag playerNBT)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = player.level();

        Vec3 playerDirCentre = Utilities.calculateViewVector(0.0f, player.getViewYRot(0)).scale(0.75);
        BlockPos centrePos = player.blockPosition().offset(
                (int) Math.round(playerDirCentre.x),
                (Math.round(player.getY()) > player.getY()) ? 1 : 0,
                (int) Math.round(playerDirCentre.z)
        );

        final boolean isCtrlDown = (InputConstants.isKeyDown(minecraft.getWindow().getWindow(),InputConstants.KEY_RCONTROL)
                || InputConstants.isKeyDown(minecraft.getWindow().getWindow(),InputConstants.KEY_LCONTROL));
        final boolean isShiftDown = (InputConstants.isKeyDown(minecraft.getWindow().getWindow(),InputConstants.KEY_RSHIFT)
                || InputConstants.isKeyDown(minecraft.getWindow().getWindow(),InputConstants.KEY_LSHIFT));

        BaseformProperties baseformProperties = (BaseformProperties) ClientFormData.getPlayerFormDetails();

        //Passive Abilities
        {
            //General Sprinting
            {
                //It handles Auto Step,
                if (player.isSprinting() && baseformProperties.sprintFlag == false)
                    PacketHandler.sendToServer(new StartSprint());
                if (!player.isSprinting() && baseformProperties.sprintFlag == true)
                    PacketHandler.sendToServer(new StopSprint());

            }
            //Double Jump
            {
                if (KeyBindings.INSTANCE.doubleJump.consumeClick()
                        && !player.onGround() && !player.isSpectator()
                        && baseformProperties.hasDoubleJump
                        && playerNBT.getCompound("abilities").getByte("flying") == 0) {
                    PacketHandler.sendToServer(new DoubleJump());
                }

                if (!baseformProperties.hasDoubleJump && player.onGround()) {
                    PacketHandler.sendToServer(new DoubleJumpEnd());
                }
            }
            //Auto Step
            {
                if (player.isSprinting()) {
                    List<String> blocksinFront = new ArrayList<>();
                    blocksinFront.add(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos.offset(0, -3, 0)).getBlock()) + "");
                    blocksinFront.add(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos.offset(0, -2, 0)).getBlock()) + "");
                    blocksinFront.add(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos.offset(0, -1, 0)).getBlock()) + "");
                    blocksinFront.add(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos).getBlock()) + "");

                    if (Utilities.passableBlocks.contains(blocksinFront.get(3))
                            && Utilities.passableBlocks.contains(blocksinFront.get(2))
                            && !Utilities.passableBlocks.contains(blocksinFront.get(1))
                            && player.onGround())
                        PacketHandler.sendToServer(new StepDown());

                    if (Utilities.passableBlocks.contains(blocksinFront.get(3))
                            && Utilities.passableBlocks.contains(blocksinFront.get(2))
                            && Utilities.passableBlocks.contains(blocksinFront.get(1))
                            && !Utilities.passableBlocks.contains(blocksinFront.get(0))
                            && player.onGround())
                        PacketHandler.sendToServer(new StepDownDouble());
                }
            }

            //Danger Sense
            {
                //Danger Sense Toggle
                if(KeyBindings.INSTANCE.toggleDangerSense.consumeClick() && isShiftDown && isCtrlDown)
                {
                    player.displayClientMessage(
                            Component.nullToEmpty(
                                    (baseformProperties.dangerSenseActive)
                                            ?"Danger Sense Inhibited":"Danger Sense Activated"
                            ),true);
                    PacketHandler.sendToServer(new DangerSenseToggle());
                }
                //Danger Sense Emit-Server Second
            }

            //Hunger
            //Server Second

            //Can't swim
            if(player.isInWater()) {
                player.setSprinting(false);
            }

        }

        //Slot 1
        {
            //Air Boosts
            if(baseformProperties.airBoosts < 3 && player.onGround()) {
                PacketHandler.sendToServer(new ResetAirBoost());
            }

            if(KeyBindings.INSTANCE.useAbility1.consumeClick() &&
                    VirtualSlotHandler.getCurrAbility() == 0) {
                //Boost
                if (player.onGround())
                    PacketHandler.sendToServer(new Boost());
                //Air Boost
                else
                    PacketHandler.sendToServer(new AirBoost());
            }

            //Quickstep
            //Double Tap Left
            if(InputConstants.isKeyDown(minecraft.getWindow().getWindow(),InputConstants.KEY_A) &&
                    !DoubleTapHandler.pressedLeft && !DoubleTapHandler.releasedLeft)
                DoubleTapHandler.pressedLeft = true;
            if(!InputConstants.isKeyDown(minecraft.getWindow().getWindow(),InputConstants.KEY_A) &&
                    DoubleTapHandler.pressedLeft && !DoubleTapHandler.releasedLeft) {
                DoubleTapHandler.releasedLeft = true;
                DoubleTapHandler.scheduleResetLeftPress();
            }
            if(InputConstants.isKeyDown(minecraft.getWindow().getWindow(),InputConstants.KEY_A) &&
                    DoubleTapHandler.pressedLeft && DoubleTapHandler.releasedLeft)
            {
                if(baseformProperties.boostLvl >= 1 && player.isSprinting())
                    PacketHandler.sendToServer(new SidestepLeft());
                DoubleTapHandler.markDoubleLeftPress();
            }

            //Double Tap Right
            if(InputConstants.isKeyDown(minecraft.getWindow().getWindow(),InputConstants.KEY_D) &&
                    !DoubleTapHandler.pressedRight && !DoubleTapHandler.releasedRight)
                DoubleTapHandler.pressedRight = true;
            if(!InputConstants.isKeyDown(minecraft.getWindow().getWindow(),InputConstants.KEY_D) &&
                    DoubleTapHandler.pressedRight && !DoubleTapHandler.releasedRight) {
                DoubleTapHandler.releasedRight = true;
                DoubleTapHandler.scheduleResetRightPress();
            }
            if(InputConstants.isKeyDown(minecraft.getWindow().getWindow(),InputConstants.KEY_D) &&
                    DoubleTapHandler.pressedRight && DoubleTapHandler.releasedRight)
            {
                if(baseformProperties.boostLvl >= 1 && player.isSprinting())
                    PacketHandler.sendToServer(new SidestepRight());
                DoubleTapHandler.markDoubleRightPress();
            }
        }

        //Light Speed Attack
        {
            //Activate if Player Presses X when Sneaking
            if(KeyBindings.INSTANCE.useAbility2.consumeClick() &&
                    VirtualSlotHandler.getCurrAbility() == 0 &&
                    player.isShiftKeyDown() &&
                    baseformProperties.lightSpeedState == (byte)0 &&
                    baseformProperties.getCooldown(BaseformActiveAbility.LIGHT_SPEED_ATTACK) == (byte)0)
            {
                PacketHandler.sendToServer(new LightspeedCharge());

                lightSpeedCanceller = Scheduler.scheduleTask(()->{
                    PacketHandler.sendToServer(new LightspeedEffect());
                    Scheduler.scheduleTask(()->{
                        PacketHandler.sendToServer(new LightspeedDecay());
                    },300);
                },66);
            }

            //Cancel Light Speed Attack
            if(baseformProperties.lightSpeedState == (byte)1 &&
                    lightSpeedCanceller != null &&
                    !player.isShiftKeyDown()) {
                lightSpeedCanceller.cancel();
                PacketHandler.sendToServer(new LightspeedCancel());
            }
        }
    }

    public static void performBaseformClientSecond(LocalPlayer player, CompoundTag playerNBT)
    {

    }


    public static void performBaseformServerTick(ServerPlayer player, CompoundTag playerNBT)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = player.level();

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
                //Boost
                {

                    //Water Boost
                    if(player.isSprinting() && !player.isInWater() &&
                            baseformProperties.boostLvl>=1 && baseformProperties.boostLvl<=3)
                    {
                        try {
                            if (ForgeRegistries.BLOCKS.getKey(level.getBlockState(player.blockPosition().offset(0, -1, 0)).getBlock())
                                    .equals(ForgeRegistries.BLOCKS.getKey(Blocks.WATER)))
                            {
                                //Get Motion
                                Vec3 lookAngle = player.getLookAngle();
                                Vec3 playerDirection = new Vec3(lookAngle.x(),0,lookAngle.z());

                                if(baseformProperties.isWaterBoosting == false){
                                    player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.0);
                                    baseformProperties.isWaterBoosting = true;

                                    //Slight upward
                                    playerDirection = new Vec3(lookAngle.x(), 0.01, lookAngle.z());
                                }

                                //Move Forward
                                player.setDeltaMovement(playerDirection.scale(2));
                                player.connection.send(new ClientboundSetEntityMotionPacket(player));
                            }
                        }
                        catch(NullPointerException ignored) {}
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
                    }
                    catch (NullPointerException ignored) {}

                    if(player.isSprinting())
                    {
                        //Particles
                        switch (baseformProperties.boostLvl) {
                            case 1:
                                PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                        ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                        0.00, 0.05, 0.00,
                                        0.001, 0.00f, 0.00f, 0.00f, 1,
                                        true));
                                break;
                            case 2:
                                PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                        new DustParticleOptions(new Vector3f(1.000f,0.000f,0.000f), 2f),
                                        0.00, 0.35, 0.00,
                                        0.001, 0.25f, 0.25f, 0.25f, 4,
                                        true)
                                );
                                break;
                            case 3:
                                PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                        new DustParticleOptions(new Vector3f(0.0f,0.89f,1.00f),1),
                                        0.00, 1.0, 0.00,
                                        0.001, 0.35f,1f, 0.35f, 12,
                                        true)
                                );
                                break;
                            default:
                        }
                        if (ForgeRegistries.BLOCKS.getKey(level.getBlockState(player.blockPosition().offset(0, -1, 0)).getBlock()).equals(ForgeRegistries.BLOCKS.getKey(Blocks.WATER)))
                            PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                    ParticleTypes.FALLING_WATER,
                                    0.00, 1.0, 0.00,
                                    0.001, 0.35f,1f, 0.35f, 12,
                                    true)
                            );

                        //Wall Boost
                        if(!Utilities.passableBlocks.contains(ForgeRegistries.BLOCKS.getKey(level.getBlockState(centrePos.offset(0, 1, 0)).getBlock()) + "")
                                && player.getXRot() < -80.0
                                && baseformProperties.boostLvl>=1 && baseformProperties.boostLvl<=3)
                        {
                            //Move Upward
                            player.setSprinting(false);
                            player.addDeltaMovement(new Vec3(0,player.getAttribute(Attributes.MOVEMENT_SPEED).getValue()*1.5,0));
                            player.connection.send(new ClientboundSetEntityMotionPacket(player));
                        }
                    }
                }

                //Light Speed Attack
                //Particles
                if(baseformProperties.lightSpeedState == (byte)1)
                    PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                            new DustParticleOptions(new Vector3f(0.0f,1.2f,1.0f),1),
                            0.00, 0.85, 0.00,
                            1.0, 1.40f, 1.00f, 1.00f, 10,
                            true)
                    );
            }


            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            baseformProperties
                    ));
        });


    }

    public static void performBaseformServerSecond(ServerPlayer player, CompoundTag playerNBT)
    {
        //Get Data From the Player
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            //Get Data From the Player
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();
            //Cooldowns
            {
                byte[] allCooldowns = baseformProperties.getAllCooldowns();
                for (int i = 0; i < allCooldowns.length; ++i)
                    allCooldowns[i] = (byte) Math.max(0, allCooldowns[i] - 1);
            }

            //Passive Abilities
            {
                //Danger Sense
                if (baseformProperties.dangerSenseActive) {
                    DangerSenseEmit.performDangerSenseEmit(player);
                }

                //Subdue Hunger
                if (player.getFoodData().getFoodLevel() <= 8)
                    player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 1, 0, false, false));
            }


            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            baseformProperties
                    ));
        });
    }

    public static void performBaseformDeactivation(ServerPlayer player)
    {
        //Remove Armor
        {
            //Get Armor Items
            Iterator<ItemStack> armorItems = player.getArmorSlots().iterator();

            //Delete Boots
            ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_BOOTS.get());
            itemToPlace.setTag(baseformArmorNBTTag);
            if(ItemStack.isSameItemSameTags(armorItems.next(),itemToPlace))
                player.setItemSlot(EquipmentSlot.FEET,ItemStack.EMPTY);

            //Delete Leggings
            itemToPlace = new ItemStack(ModItems.BASEFORM_LEGGINGS.get());
            itemToPlace.setTag(baseformArmorNBTTag);
            if(ItemStack.isSameItemSameTags(armorItems.next(),itemToPlace))
                player.setItemSlot(EquipmentSlot.LEGS,ItemStack.EMPTY);

            //Delete Chestplate
            itemToPlace = new ItemStack(ModItems.BASEFORM_CHESTPLATE.get());
            itemToPlace.setTag(baseformArmorNBTTag);
            if(ItemStack.isSameItemSameTags(armorItems.next(),itemToPlace))
                player.setItemSlot(EquipmentSlot.CHEST,ItemStack.EMPTY);
        }

        //Clear Each Ability's Effects
        {
            //Passives
            {
                //Double Jump

                //Auto Step
                player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).setBaseValue(0.0);

                //Danger Sense
                PacketHandler.sendToPlayer(player, new PlayerStopSoundPacketS2C(
                                ModSounds.DANGER_SENSE.get().getLocation()
                        )
                );

                //Hunger
            }

            //Slot 1
            {
                //Reset Boost and Water Boost
                player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.50);
                player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);

            }

        }
        //Remove Data
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm->{
            playerSonicForm.deactivateBaseForm();
            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            playerSonicForm.getFormProperties()
                    ));
        });

        //Deinitialize Virtual Slot Handler
        PacketHandler.sendToPlayer(player,new VirtualSlotSyncS2C((byte)0));

        //Effects
        player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.10000000149011612);
        player.removeEffect(MobEffects.JUMP);

        //Commands
        CommandSourceStack commandSourceStack = player.createCommandSourceStack().withPermission(4).withSuppressedOutput();
        MinecraftServer server = player.serverLevel().getServer();

        server.
                getCommands().
                performPrefixedCommand(commandSourceStack,"function beyondthehorizon:baseform/deactivate");
    }
}
