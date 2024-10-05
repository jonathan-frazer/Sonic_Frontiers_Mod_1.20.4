package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.CameraType;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
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
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.power_boost.PowerBoostActivate;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.power_boost.PowerBoostDeactivate;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.homing_attack.HomingAttack;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash.ChargeSpindash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash.LaunchSpindash;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash.RevertFromSpindash;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
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
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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
    public static ItemStack baseformLSSonicHead; static {
        baseformLSSonicHead = new ItemStack(Items.PLAYER_HEAD);
        CompoundTag nbt = new CompoundTag();

        // Custom NBT data
        nbt.putByte("BeyondTheHorizon", (byte) 2);

        // SkullOwner tag
        CompoundTag skullOwner = new CompoundTag();
        CompoundTag properties = new CompoundTag();
        ListTag textures = new ListTag();
        CompoundTag texture = new CompoundTag();
        texture.putString("Value", "ewogICJ0aW1lc3RhbXAiIDogMTcyNjkyNzYxNjIxNSwKICAicHJvZmlsZUlkIiA6ICI2OTBmOTAwMTczZmQ0MDA5OGE2ZDc3Nzc2MWUwY2U4YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTb25pY1J1c2hYMTIiLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2QyNzZmMGExMTBkMGEzNzhiNzdlNzk3OTBiZDc0ZjNiOWEzMmNhNzgyYWQ2MTQ2NjhhYWE1ZmM4MDg5MWIwMCIKICAgIH0KICB9Cn0=");
        textures.add(texture);
        properties.put("textures", textures);
        skullOwner.put("Properties", properties);
        skullOwner.putIntArray("Id", new int[]{1762627585, 1945976841, -1972537481, 1642122891});
        nbt.put("SkullOwner", skullOwner);

        // Display tag
        CompoundTag display = new CompoundTag();
        ListTag lore = new ListTag();
        lore.add(StringTag.valueOf("{\"text\":\"Light Speed Mode\",\"color\": \"aqua\"}"));
        display.put("Lore", lore);
        display.putString("Name", "{\"text\":\"Sonic Head\",\"color\": \"blue\",\"italic\": false}");
        nbt.put("display", display);

        baseformLSSonicHead.setTag(nbt);
    }
    public static ItemStack baseformPBSonicHead; static {
    baseformPBSonicHead = new ItemStack(Items.PLAYER_HEAD);
    CompoundTag nbt = new CompoundTag();

    // Custom NBT data
    nbt.putByte("BeyondTheHorizon", (byte) 2);

    // SkullOwner tag
    CompoundTag skullOwner = new CompoundTag();
    CompoundTag properties = new CompoundTag();
    ListTag textures = new ListTag();
    CompoundTag texture = new CompoundTag();
    texture.putString("Value", "ewogICJ0aW1lc3RhbXAiIDogMTcyNzg5MTc2MDg1NywKICAicHJvZmlsZUlkIiA6ICI2OTBmOTAwMTczZmQ0MDA5OGE2ZDc3Nzc2MWUwY2U4YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTb25pY1J1c2hYMTIiLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjdlMDM2OWQxYzQ3ZWYwZmFjMmVjNGE2MmI5NzgxZDZjOGE0NTRlOGNiYjBkOTg5ODgxMWVkNjlhZjhhOWJiZCIKICAgIH0KICB9Cn0=");
    textures.add(texture);
    properties.put("textures", textures);
    skullOwner.put("Properties", properties);
    skullOwner.putIntArray("Id", new int[]{1762627585, 1945976841, -1972537481, 1642122891});
    nbt.put("SkullOwner", skullOwner);

    // Display tag
    CompoundTag display = new CompoundTag();
    ListTag lore = new ListTag();
    lore.add(StringTag.valueOf("{\"text\":\"Power Boost\",\"color\": \"dark_blue\"}"));
    display.put("Lore", lore);
    display.putString("Name", "{\"text\":\"Sonic Head\",\"color\": \"blue\",\"italic\": false}");
    nbt.put("display", display);

    baseformPBSonicHead.setTag(nbt);
}
    private static boolean cinematicCamera = false;

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
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, 3, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 1, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, -1, 2, false, false));
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
            //Boost
            {
                //Air Boosts
                if (baseformProperties.airBoosts < 3 && player.onGround()) {
                    PacketHandler.sendToServer(new ResetAirBoost());
                }

                if ( VirtualSlotHandler.getCurrAbility() == 0 &&
                        KeyBindings.INSTANCE.useAbility1.consumeClick()) {
                    //Boost
                    if (player.onGround())
                        PacketHandler.sendToServer(new Boost());
                        //Air Boost
                    else
                        PacketHandler.sendToServer(new AirBoost());
                }

                //Quickstep
                //Double Tap Left
                if (InputConstants.isKeyDown(minecraft.getWindow().getWindow(), InputConstants.KEY_A) &&
                        !DoubleTapHandler.pressedLeft && !DoubleTapHandler.releasedLeft)
                    DoubleTapHandler.pressedLeft = true;
                if (!InputConstants.isKeyDown(minecraft.getWindow().getWindow(), InputConstants.KEY_A) &&
                        DoubleTapHandler.pressedLeft && !DoubleTapHandler.releasedLeft) {
                    DoubleTapHandler.releasedLeft = true;
                    DoubleTapHandler.scheduleResetLeftPress();
                }
                if (InputConstants.isKeyDown(minecraft.getWindow().getWindow(), InputConstants.KEY_A) &&
                        DoubleTapHandler.pressedLeft && DoubleTapHandler.releasedLeft) {
                    if (baseformProperties.boostLvl >= 1 && player.isSprinting())
                        PacketHandler.sendToServer(new SidestepLeft());
                    DoubleTapHandler.markDoubleLeftPress();
                }

                //Double Tap Right
                if (InputConstants.isKeyDown(minecraft.getWindow().getWindow(), InputConstants.KEY_D) &&
                        !DoubleTapHandler.pressedRight && !DoubleTapHandler.releasedRight)
                    DoubleTapHandler.pressedRight = true;
                if (!InputConstants.isKeyDown(minecraft.getWindow().getWindow(), InputConstants.KEY_D) &&
                        DoubleTapHandler.pressedRight && !DoubleTapHandler.releasedRight) {
                    DoubleTapHandler.releasedRight = true;
                    DoubleTapHandler.scheduleResetRightPress();
                }
                if (InputConstants.isKeyDown(minecraft.getWindow().getWindow(), InputConstants.KEY_D) &&
                        DoubleTapHandler.pressedRight && DoubleTapHandler.releasedRight) {
                    if (baseformProperties.boostLvl >= 1 && player.isSprinting())
                        PacketHandler.sendToServer(new SidestepRight());
                    DoubleTapHandler.markDoubleRightPress();
                }
            }
            //Light Speed Attack
            {
                //Activate if Player Presses X when Sneaking
                if ( VirtualSlotHandler.getCurrAbility() == 0 &&
                        player.isShiftKeyDown() &&
                        baseformProperties.lightSpeedState == (byte) 0 &&
                        baseformProperties.getCooldown(BaseformActiveAbility.LIGHT_SPEED_ATTACK) == (byte) 0 &&
                        KeyBindings.INSTANCE.useAbility2.consumeClick()) {
                    PacketHandler.sendToServer(new LightspeedCharge());

                    lightSpeedCanceller = Scheduler.scheduleTask(() -> {
                        PacketHandler.sendToServer(new LightspeedEffect());
                        Scheduler.scheduleTask(() -> PacketHandler.sendToServer(new LightspeedDecay()), 300);
                    }, 66);
                }

                //Cancel Light Speed Attack
                if (baseformProperties.lightSpeedState == (byte) 1 &&
                        lightSpeedCanceller != null &&
                        !player.isShiftKeyDown()) {
                    lightSpeedCanceller.cancel();
                    PacketHandler.sendToServer(new LightspeedCancel());
                }
            }
            //Power Boost
            {
                //Activate if Player Presses Z
                if(VirtualSlotHandler.getCurrAbility() == 0 && baseformProperties.getCooldown(BaseformActiveAbility.HOMING_ATTACK) == 0 &&
                baseformProperties.getCooldown(BaseformActiveAbility.POWER_BOOST) == (byte) 0 &&
                KeyBindings.INSTANCE.useAbility3.consumeClick())
                {
                    if(baseformProperties.powerBoost)   PacketHandler.sendToServer(new PowerBoostDeactivate());
                    else                                PacketHandler.sendToServer(new PowerBoostActivate());
                }
            }
        }

        //Slot 2
        {
            //Spin Dash
            {
                //Charge Spindash
                if (VirtualSlotHandler.getCurrAbility() == 1 && player.isShiftKeyDown() &&
                        player.getXRot() > 80.0 && baseformProperties.ballFormState == (byte) 0 &&
                        KeyBindings.INSTANCE.useAbility1.isDown()) {
                    //Set Camera
                    player.setXRot(0.0f);
                    minecraft.options.setCameraType(CameraType.THIRD_PERSON_BACK);

                    //Let go of Z
                    minecraft.keyboardHandler.keyPress(minecraft.getWindow().getWindow(), KeyBindings.INSTANCE.useAbility1.getKey().getValue(), 0, GLFW.GLFW_RELEASE, 0);

                    //Send Packet
                    PacketHandler.sendToServer(new ChargeSpindash());
                }

                //Launch Spindash
                if (!player.isShiftKeyDown() && baseformProperties.ballFormState == (byte)1)
                {
                    //Force W Presses and lower Mouse Sens
                    double currentSens = minecraft.options.sensitivity().get();
                    minecraft.options.sensitivity().set(currentSens/2.5f);
                    PacketHandler.sendToServer(new LaunchSpindash());
                    Scheduler.scheduleTask(()->
                    {
                        Minecraft mc = Minecraft.getInstance();
                        if(!InputConstants.isKeyDown(mc.getWindow().getWindow(),InputConstants.KEY_R))
                            mc.keyboardHandler.keyPress(mc.getWindow().getWindow(), InputConstants.KEY_W, 0, GLFW.GLFW_RELEASE, 0);
                        mc.options.sensitivity().set(currentSens);
                        PacketHandler.sendToServer(new RevertFromSpindash());

                    },Math.min(baseformProperties.spinDashChargeTime/2, 100));
                }
                //Keep going forward
                if(baseformProperties.ballFormState == (byte)2) {
                    minecraft.keyboardHandler.keyPress(minecraft.getWindow().getWindow(), InputConstants.KEY_W, 0, GLFW.GLFW_PRESS, 0);
                }
            }

            //Homing Attack
            {
                if (VirtualSlotHandler.getCurrAbility() == 1 && (player.getXRot() <= 80.0 || !player.isShiftKeyDown()) &&
                        KeyBindings.INSTANCE.useAbility1.consumeClick() && baseformProperties.ballFormState != (byte)1 && baseformProperties.homingAttackAirTime == 0)
                {
                    System.out.println("Send Homing attack Packet");
                    PacketHandler.sendToServer(new HomingAttack());
                }
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
                                    new DustParticleOptions(new Vector3f(0.0f, 0.2f, 1.0f), 1.5f),
                                    player.getX(), player.getY()+0.55, player.getZ(),
                                    1.0, 0.65f, 0.65f, 0.65f, 75,
                                    true)
                            );
                        }
                        if (baseformProperties.ballFormState == (byte) 2) {

                            for(LivingEntity nearbyEntity : level.getEntitiesOfClass(LivingEntity.class,
                                    new AABB(player.getX()+1.5,player.getY()+1.5,player.getZ()+1.5,
                                            player.getX()-1.5,player.getY()-1.5,player.getZ()-1.5),
                                    (nearbyEntity)->!nearbyEntity.is(player)))
                                nearbyEntity.hurt(player.damageSources().playerAttack(player),
                                        Math.min(22.0f,baseformProperties.spinDashChargeTime/4.0f));

                            PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                                    new DustParticleOptions(new Vector3f(0.0f, 0.2f, 1.0f), 1.5f),
                                    player.getX(), player.getY()+0.55, player.getZ(),
                                    1.0, 0.65f, 0.65f, 0.65f, 75,
                                    true)
                            );
                        }
                    }

                    //Homing Attack
                    {
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

    public static void performBaseformServerSecond(ServerPlayer player, CompoundTag playerNBT)
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

    public static void performBaseformDeactivation(ServerPlayer player)
    {
        //Kill Head
        Level world = player.level();
        if(!player.isAlive() && !world.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY))
        {
            for (ItemEntity itemEntity : world.getEntitiesOfClass(ItemEntity.class,
                    new AABB(player.getX() + 2, player.getX() + 2, player.getX() + 2,
                            player.getX() - 2, player.getX() - 2, player.getX() - 2),
                    (itemEntity) -> {
                        try {
                            byte bthTagVal = itemEntity.getItem().getTag().getByte("BeyondTheHorizon");
                            return (bthTagVal == (byte)1 || bthTagVal == (byte)2);
                        }catch(NullPointerException ignored){
                            return false;
                        }
                    }))
                itemEntity.remove(Entity.RemovalReason.KILLED);
        }

        //Add Item on the Ground
        ItemEntity sonicHeadItem = new ItemEntity(player.level(),
                player.getX(),player.getY(),player.getZ(),BaseformHandler.baseformSonicHead);
        sonicHeadItem.setNoPickUpDelay();
        player.level().addFreshEntity(sonicHeadItem);

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
                //Reset Water Boost
                player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);

                //Reset Light Speed Attack
                if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.LIGHTSPEED_MODE))
                    player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.LIGHTSPEED_MODE.getId());

                //Reset Power Boost
                if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.POWERBOOST_SPEED))
                    player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.POWERBOOST_SPEED.getId());
                if(player.getAttribute(Attributes.ARMOR).hasModifier(AttributeMultipliers.POWERBOOST_ARMOR))
                    player.getAttribute(Attributes.ARMOR).removeModifier(AttributeMultipliers.POWERBOOST_ARMOR.getId());

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

        //Remove Effects
        player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.10000000149011612);
        player.removeEffect(MobEffects.JUMP);
        player.removeEffect(MobEffects.DAMAGE_RESISTANCE);
        player.removeEffect(MobEffects.DAMAGE_BOOST);
        player.removeEffect(MobEffects.DIG_SPEED);
        player.removeEffect(MobEffects.INVISIBILITY);

        //Commands
        CommandSourceStack commandSourceStack = player.createCommandSourceStack().withPermission(4).withSuppressedOutput();
        MinecraftServer server = player.serverLevel().getServer();

        server.
                getCommands().
                performPrefixedCommand(commandSourceStack,"function beyondthehorizon:baseform/deactivate");
    }
}
