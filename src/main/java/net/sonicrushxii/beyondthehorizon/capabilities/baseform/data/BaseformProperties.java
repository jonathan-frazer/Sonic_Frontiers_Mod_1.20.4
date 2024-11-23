package net.sonicrushxii.beyondthehorizon.capabilities.baseform.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sonicrushxii.beyondthehorizon.capabilities.all.FormProperties;

import java.util.UUID;

public class BaseformProperties extends FormProperties {
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

    private final byte[] abilityCooldowns;
    public float atkRotPhase;

    //Passives
    public boolean hasDoubleJump;
    public boolean sprintFlag;
    public boolean dangerSenseActive;
    public boolean dangerSensePlaying;
    public byte hitCount;

    //Slot 1
    public byte airBoosts;
    public byte boostLvl;
    public boolean boosted;
    public boolean wallBoosting;
    public boolean isWaterBoosting;
    public byte lightSpeedState;
    public boolean powerBoost;
    public boolean cylooping;
    public byte quickCyloop;
    public UUID qkCyloopTarget;
    public double qkCyloopMeter;

    //Slot 2
    public byte ballFormState;
    public short spinDashChargeTime;
    public UUID homingTarget;
    public byte homingAttackAirTime;
    public boolean dodgeInvul;
    public byte hummingTop;
    public boolean speedBlitz;
    public byte speedBlitzDashTimer;
    public byte speedBlitzDashes;
    public byte smashHit;
    public byte stomp;

    //Slot 3
    public byte tornadoJump;
    public short mirageTimer;
    public byte lightSpeedAssault;
    public UUID meleeTarget;
    public byte spinSlash;
    public byte cycloneKick;
    public byte wildRushTime;
    public byte wildRushPtr;
    public int[] wildRushPX;
    public int[] wildRushPY;
    public int[] wildRushPZ;
    public byte loopKick;

    //Slot 4
    public UUID rangedTarget;
    public byte crossSlash;
    public byte homingShot;
    public byte sonicBoom;
    public byte sonicWind;
    public byte profanedWind;
    public int[] profanedWindCoords;

    //Slot 5
    public byte parryTime;
    public boolean parryTimeSlow;

    public BaseformProperties()
    {
        abilityCooldowns = new byte[BaseformActiveAbility.values().length];
        hitCount = 0;
        atkRotPhase = 0.0f;

        //Passives
        hasDoubleJump = true;
        sprintFlag = false;
        dangerSenseActive = true;
        dangerSensePlaying = false;

        //Slot 1
        airBoosts = 3;
        boostLvl = 0;
        boosted = false;
        wallBoosting = false;
        isWaterBoosting = false;
        lightSpeedState = 0;
        powerBoost = false;
        cylooping = false;
        quickCyloop = 0;
        qkCyloopTarget = new UUID(0L,0L);
        qkCyloopMeter = 0.0;

        //Slot 2
        ballFormState = (byte)0;
        spinDashChargeTime = 0;
        homingTarget = new UUID(0L,0L);
        homingAttackAirTime = 0;
        dodgeInvul = false;
        hummingTop = 0;
        speedBlitz = false;
        speedBlitzDashTimer = (byte)0;
        speedBlitzDashes = 4;
        smashHit = (byte)0;
        stomp = (byte)0;

        //Slot 3
        tornadoJump = (byte)0;
        mirageTimer = (short)0;
        lightSpeedAssault = (byte)0;
        meleeTarget = new UUID(0L,0L);
        spinSlash = (byte)0;
        cycloneKick = (byte)0;
        wildRushTime = 0;
        wildRushPtr = 0;
        wildRushPX = new int[]{0,0,0,0,0};
        wildRushPY = new int[]{0,0,0,0,0};
        wildRushPZ = new int[]{0,0,0,0,0};
        loopKick = 0;

        //Slot 4
        rangedTarget = new UUID(0L,0L);
        crossSlash = (byte)0;
        homingShot = (byte)0;
        sonicBoom = (byte)0;
        sonicWind = (byte)0;
        profanedWind = (byte)0;
        profanedWindCoords = new int[]{0,0,0};

        //Slot 5
        parryTime = (byte)0;
    }

    public BaseformProperties(CompoundTag nbt)
    {
        //Common
        abilityCooldowns = nbt.getByteArray("AbilityCooldowns");
        hitCount = nbt.getByte("hitsPerformed");
        atkRotPhase = nbt.getFloat("atkRotPhase");

        //Passives
        hasDoubleJump = nbt.getBoolean("hasDoubleJump");
        sprintFlag = nbt.getBoolean("isSprinting");
        dangerSenseActive = nbt.getBoolean("dangerSenseActive");
        dangerSensePlaying = nbt.getBoolean("dangerSensePlaying");

        //Slot 1
        airBoosts = nbt.getByte("AirBoosts");
        boostLvl = nbt.getByte("BoostLvl");
        boosted = nbt.getBoolean("boosted");
        wallBoosting = nbt.getBoolean("IsWallBoosting");
        isWaterBoosting = nbt.getBoolean("IsWaterBoosting");
        lightSpeedState = nbt.getByte("LightSpeedState");
        powerBoost = nbt.getBoolean("PowerBoost");
        cylooping = nbt.getBoolean("isCylooping");
        quickCyloop = nbt.getByte("quickCyloop");
        qkCyloopTarget = nbt.getUUID("QkCyloopTarget");
        qkCyloopMeter = nbt.getDouble("QkCyloopMeter");

        //Slot 2
        ballFormState = nbt.getByte("InBallForm");
        spinDashChargeTime = nbt.getShort("Spindash");
        homingTarget = nbt.getUUID("HomingTarget");
        homingAttackAirTime = nbt.getByte("HomingTime");
        dodgeInvul = nbt.getBoolean("isDodging");
        hummingTop = nbt.getByte("hummingTop");
        speedBlitz = nbt.getBoolean("speedBlitzOn");
        speedBlitzDashTimer = nbt.getByte("speedBlitzDash");
        speedBlitzDashes = nbt.getByte("speedBlitzes");
        smashHit = nbt.getByte("smashHitTime");
        stomp = nbt.getByte("stompTime");

        //Slot 3
        tornadoJump = nbt.getByte("tornadoJump");
        mirageTimer = nbt.getShort("MirageTimer");
        lightSpeedAssault = nbt.getByte("LSRush");
        meleeTarget = nbt.getUUID("MeleeTarget");
        spinSlash = nbt.getByte("SpinSlash");
        cycloneKick = nbt.getByte("CycloneKick");
        wildRushTime = nbt.getByte("WildRushTime");
        wildRushPtr = nbt.getByte("WildRushPhase");
        wildRushPX = nbt.getIntArray("wildRushPtrsX");
        wildRushPY = nbt.getIntArray("wildRushPtrsY");
        wildRushPZ = nbt.getIntArray("wildRushPtrsZ");
        loopKick = nbt.getByte("LoopKick");

        //Slot 4
        rangedTarget = nbt.getUUID("RangedTarget");
        crossSlash = nbt.getByte("CrossSlash");
        homingShot = nbt.getByte("HomingShot");
        sonicBoom = nbt.getByte("SonicBoom");
        sonicWind = nbt.getByte("SonicWind");
        profanedWind = nbt.getByte("SonicQWind");
        profanedWindCoords = nbt.getIntArray("SonicQWindPos");

        //Slot 5
        parryTime = nbt.getByte("ParryTime");
    }

    @Override
    public CompoundTag serialize()
    {
        CompoundTag nbt = new CompoundTag();

        //Common
        nbt.putByteArray("AbilityCooldowns",abilityCooldowns);
        nbt.putByte("hitsPerformed",hitCount);
        nbt.putFloat("atkRotPhase", atkRotPhase);

        //Passives
        nbt.putBoolean("hasDoubleJump",hasDoubleJump);
        nbt.putBoolean("isSprinting",sprintFlag);
        nbt.putBoolean("dangerSenseActive",dangerSenseActive);
        nbt.putBoolean("dangerSensePlaying",dangerSensePlaying);

        //Slot 1
        nbt.putByte("AirBoosts",airBoosts);
        nbt.putByte("BoostLvl",boostLvl);
        nbt.putBoolean("boosted",boosted);
        nbt.putBoolean("IsWallBoosting",wallBoosting);
        nbt.putBoolean("IsWaterBoosting",isWaterBoosting);
        nbt.putByte("LightSpeedState",lightSpeedState);
        nbt.putBoolean("PowerBoost",powerBoost);
        nbt.putBoolean("isCylooping",cylooping);
        nbt.putByte("QuickCyloop",quickCyloop);
        nbt.putUUID("QkCyloopTarget",qkCyloopTarget);
        nbt.putDouble("QkCyloopMeter",qkCyloopMeter);

        //Slot 2
        nbt.putByte("InBallForm",ballFormState);
        nbt.putShort("Spindash", spinDashChargeTime);
        nbt.putUUID("HomingTarget",homingTarget);
        nbt.putByte("HomingTime",homingAttackAirTime);
        nbt.putBoolean("isDodging",dodgeInvul);
        nbt.putByte("hummingTop", hummingTop);
        nbt.putBoolean("speedBlitzOn",speedBlitz);
        nbt.putByte("speedBlitzDash",speedBlitzDashTimer);
        nbt.putByte("speedBlitzes",speedBlitzDashes);
        nbt.putByte("smashHitTime",smashHit);
        nbt.putByte("stompTime",stomp);

        //Slot 3
        nbt.putByte("tornadoJump",tornadoJump);
        nbt.putShort("MirageTimer",mirageTimer);
        nbt.putByte("LSRush",lightSpeedAssault);
        nbt.putUUID("MeleeTarget", meleeTarget);
        nbt.putByte("SpinSlash",spinSlash);
        nbt.putByte("CycloneKick",cycloneKick);
        nbt.putByte("WildRushTime",wildRushTime);
        nbt.putByte("WildRushPhase",wildRushPtr);
        nbt.putIntArray("wildRushPtrsX",wildRushPX);
        nbt.putIntArray("wildRushPtrsY",wildRushPY);
        nbt.putIntArray("wildRushPtrsZ",wildRushPZ);
        nbt.putByte("LoopKick",loopKick);

        //Slot 4
        nbt.putUUID("RangedTarget",rangedTarget);
        nbt.putByte("CrossSlash",crossSlash);
        nbt.putByte("HomingShot",homingShot);
        nbt.putByte("SonicBoom",sonicBoom);
        nbt.putByte("SonicWind",sonicWind);
        nbt.putByte("SonicQWind",profanedWind);
        nbt.putIntArray("SonicQWindPos",profanedWindCoords);

        //Slot 5
        nbt.putByte("ParryTime",parryTime);

        return nbt;
    }

    //Cooldown Manager
    public byte[] getAllCooldowns() {return abilityCooldowns;}
    public byte getCooldown(BaseformActiveAbility ability){return abilityCooldowns[ability.ordinal()];}
    public void setCooldown(BaseformActiveAbility ability, byte seconds){abilityCooldowns[ability.ordinal()] = seconds;}

    //Decides when to apply Selective Invulnerability
    public boolean selectiveInvul()
    {
        boolean quickCyloop = (this.quickCyloop > 0);
        boolean ballForm = ballFormState > 0;
        boolean speedBlitzDash = (this.speedBlitzDashTimer > 0);
        boolean homingAttack = (homingAttackAirTime > 0 && homingAttackAirTime < 50);
        boolean melee = hitCount > 3;
        boolean hummingTop = this.hummingTop > 0;
        boolean stomping = (this.stomp > 0);

        boolean tornadoJump = (this.tornadoJump != 0);
        boolean mirageTimer = (this.mirageTimer > 0);
        boolean lightSpeedRush = (this.lightSpeedAssault > 0);
        boolean spinSlash = (this.spinSlash != 0);
        boolean cycloneKick = (this.cycloneKick != 0);
        boolean wildRush = (this.wildRushTime != 0);
        boolean loopKick = (this.loopKick > 24);

        boolean homingShot = (this.homingShot > 0);

        return quickCyloop ||
                ballForm || homingAttack || speedBlitzDash || melee || hummingTop || stomping ||
                tornadoJump || mirageTimer || lightSpeedRush || spinSlash || cycloneKick || wildRush || loopKick ||
                homingShot;
    }

    //Checks if Player is in the middle of another attack
    public boolean isAttacking()
    {
        boolean quickCyloop = (this.quickCyloop > 0);

        boolean ballform = ballFormState == 1;
        boolean speedBlitzDash = (this.speedBlitzDashTimer > 0);
        boolean homingAttack = (homingAttackAirTime > 0 && homingAttackAirTime < 44);
        boolean hummingTop = (this.hummingTop > 0);
        boolean stomping = (this.stomp > 0);

        boolean tornadoJump = (this.tornadoJump > 0);
        boolean lightSpeedRush = (this.lightSpeedAssault > 0);
        boolean spinSlash = (this.spinSlash != 0);
        boolean cycloneKick = (this.cycloneKick != 0);
        boolean wildRush = (this.wildRushTime > 0);
        boolean loopKick = (this.loopKick > 0);

        boolean crossSlash = (this.crossSlash > 0);
        boolean homingShot = (this.homingShot > 0);
        boolean sonicBoom = (this.sonicBoom > 0);
        boolean sonicWind = (this.sonicWind > 0) && (this.profanedWind > 0);
        boolean parry = (this.parryTime > 0);

        return quickCyloop ||
                homingAttack || hummingTop || ballform || speedBlitzDash || stomping ||
                tornadoJump || lightSpeedRush || spinSlash || cycloneKick || wildRush || loopKick ||
                crossSlash || homingShot || sonicBoom || sonicWind ||
                parry;
    }

    //Ball form
    public boolean shouldBeInBallform()
    {
        boolean regular = this.ballFormState >= 1;
        boolean homingAttack = this.homingAttackAirTime > 1;
        boolean tornadoJump = this.tornadoJump > 0;
        boolean lightSpeed = this.lightSpeedState == 1 || this.lightSpeedAssault == -1;
        boolean cycloneKick = (this.cycloneKick < 0);
        boolean homingShot = (this.homingShot > 0 && this.homingShot < 20);

        return regular || homingAttack || tornadoJump || lightSpeed || cycloneKick || homingShot;
    }
}