package net.sonicrushxii.beyondthehorizon.modded;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BeyondTheHorizon.MOD_ID);

    public static final RegistryObject<SoundEvent> AIR_BOOST =
            registerSoundEvents("air_boost");
    public static final RegistryObject<SoundEvent> BLITZ =
            registerSoundEvents("blitz");
    public static final RegistryObject<SoundEvent> CROSS_SLASH =
            registerSoundEvents("cross_slash");
    public static final RegistryObject<SoundEvent> CYLOOP_RINGS =
            registerSoundEvents("cyloop_rings");
    public static final RegistryObject<SoundEvent> CYLOOP =
            registerSoundEvents("cyloop");
    public static final RegistryObject<SoundEvent> CYLOOP_SUCCESS =
            registerSoundEvents("cyloop_success");
    public static final RegistryObject<SoundEvent> DANGER_SENSE =
            registerSoundEvents("danger_sense");
    public static final RegistryObject<SoundEvent> DEPOWER_BOOST =
            registerSoundEvents("depower_boost");
    public static final RegistryObject<SoundEvent> DOUBLE_JUMP =
            registerSoundEvents("double_jump");
    public static final RegistryObject<SoundEvent> GRAND_SLAM =
            registerSoundEvents("grand_slam");
    public static final RegistryObject<SoundEvent> HOMING_ATTACK =
            registerSoundEvents("homing_attack");
    public static final RegistryObject<SoundEvent> HOMING_SHOT =
            registerSoundEvents("homing_shot");
    public static final RegistryObject<SoundEvent> LIGHT_SPEED_CHARGE =
            registerSoundEvents("light_speed_charge");
    public static final RegistryObject<SoundEvent> LIGHT_SPEED_IDLE =
            registerSoundEvents("light_speed_idle");
    public static final RegistryObject<SoundEvent> LOOP_KICK =
            registerSoundEvents("loop_kick");
    public static final RegistryObject<SoundEvent> MAX_BOOST =
            registerSoundEvents("max_boost");
    public static final RegistryObject<SoundEvent> MIRAGE =
            registerSoundEvents("mirage");
    public static final RegistryObject<SoundEvent> PARRY =
            registerSoundEvents("parry");
    public static final RegistryObject<SoundEvent> POWER_BOOST_IDLE =
            registerSoundEvents("power_boost_idle");
    public static final RegistryObject<SoundEvent> POWER_BOOST =
            registerSoundEvents("power_boost");
    public static final RegistryObject<SoundEvent> SMASH_CHARGE =
            registerSoundEvents("smash_charge");
    public static final RegistryObject<SoundEvent> SMASH_HIT =
            registerSoundEvents("smash_hit");
    public static final RegistryObject<SoundEvent> SONIC_BOOM =
            registerSoundEvents("sonic_boom");
    public static final RegistryObject<SoundEvent> SONIC_WIND_SHOOT =
            registerSoundEvents("sonic_wind_shoot");
    public static final RegistryObject<SoundEvent> SONIC_WIND_STUN =
            registerSoundEvents("sonic_wind_stun");
    public static final RegistryObject<SoundEvent> SPIN_SLASH =
            registerSoundEvents("spin_slash");
    public static final RegistryObject<SoundEvent> SPINDASH_CHARGE =
            registerSoundEvents("spindash_charge");
    public static final RegistryObject<SoundEvent> SPINDASH_RELEASE =
            registerSoundEvents("spindash_release");
    public static final RegistryObject<SoundEvent> STOMP =
            registerSoundEvents("stomp");
    public static final RegistryObject<SoundEvent> TORNADO =
            registerSoundEvents("tornado");
    public static final RegistryObject<SoundEvent> ULTIMATE_MUSIC =
            registerSoundEvents("ultimate_music");

    private static RegistryObject<SoundEvent> registerSoundEvents(String soundName){
        return SOUND_EVENTS.register(soundName,
                ()->SoundEvent.createVariableRangeEvent(
                        new ResourceLocation(BeyondTheHorizon.MOD_ID,soundName)
                ));
    }

    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }
}
