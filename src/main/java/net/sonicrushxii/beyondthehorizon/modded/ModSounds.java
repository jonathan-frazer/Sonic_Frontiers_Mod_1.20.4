package net.sonicrushxii.beyondthehorizon.modded;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, BeyondTheHorizon.MOD_ID);

    public static final Supplier<SoundEvent> AIR_BOOST =
            registerSoundEvents("air_boost");
    public static final Supplier<SoundEvent> BLITZ =
            registerSoundEvents("blitz");
    public static final Supplier<SoundEvent> CROSS_SLASH =
            registerSoundEvents("cross_slash");
    public static final Supplier<SoundEvent> CYLOOP_RINGS =
            registerSoundEvents("cyloop_rings");
    public static final Supplier<SoundEvent> CYLOOP =
            registerSoundEvents("cyloop");
    public static final Supplier<SoundEvent> CYLOOP_SUCCESS =
            registerSoundEvents("cyloop_success");
    public static final Supplier<SoundEvent> DANGER_SENSE =
            registerSoundEvents("danger_sense");
    public static final Supplier<SoundEvent> DEPOWER_BOOST =
            registerSoundEvents("depower_boost");
    public static final Supplier<SoundEvent> DOUBLE_JUMP =
            registerSoundEvents("double_jump");
    public static final Supplier<SoundEvent> GRAND_SLAM =
            registerSoundEvents("grand_slam");
    public static final Supplier<SoundEvent> HOMING_ATTACK =
            registerSoundEvents("homing_attack");
    public static final Supplier<SoundEvent> HOMING_SHOT =
            registerSoundEvents("homing_shot");
    public static final Supplier<SoundEvent> HUMMING_TOP =
            registerSoundEvents("humming_top");
    public static final Supplier<SoundEvent> LIGHT_SPEED_CHARGE =
            registerSoundEvents("light_speed_charge");
    public static final Supplier<SoundEvent> LIGHT_SPEED_IDLE =
            registerSoundEvents("light_speed_idle");
    public static final Supplier<SoundEvent> LOOP_KICK =
            registerSoundEvents("loop_kick");
    public static final Supplier<SoundEvent> MAX_BOOST =
            registerSoundEvents("max_boost");
    public static final Supplier<SoundEvent> MIRAGE =
            registerSoundEvents("mirage");
    public static final Supplier<SoundEvent> PARRY =
            registerSoundEvents("parry");
    public static final Supplier<SoundEvent> POWER_BOOST_IDLE =
            registerSoundEvents("power_boost_idle");
    public static final Supplier<SoundEvent> POWER_BOOST =
            registerSoundEvents("power_boost");
    public static final Supplier<SoundEvent> SMASH_CHARGE =
            registerSoundEvents("smash_charge");
    public static final Supplier<SoundEvent> SMASH_HIT =
            registerSoundEvents("smash_hit");
    public static final Supplier<SoundEvent> SONIC_BOOM =
            registerSoundEvents("sonic_boom");
    public static final Supplier<SoundEvent> SONIC_WIND_SHOOT =
            registerSoundEvents("sonic_wind_shoot");
    public static final Supplier<SoundEvent> SONIC_WIND_STUN =
            registerSoundEvents("sonic_wind_stun");
    public static final Supplier<SoundEvent> SPIN_SLASH =
            registerSoundEvents("spin_slash");
    public static final Supplier<SoundEvent> SPINDASH_CHARGE =
            registerSoundEvents("spindash_charge");
    public static final Supplier<SoundEvent> SPINDASH_RELEASE =
            registerSoundEvents("spindash_release");
    public static final Supplier<SoundEvent> STOMP =
            registerSoundEvents("stomp");
    public static final Supplier<SoundEvent> TORNADO =
            registerSoundEvents("tornado");
    public static final Supplier<SoundEvent> ULTIMATE_MUSIC =
            registerSoundEvents("ultimate_music");

    private static Supplier<SoundEvent> registerSoundEvents(String soundName){
        return SOUND_EVENTS.register(soundName,
                ()->SoundEvent.createVariableRangeEvent(
                        ResourceLocation.fromNamespaceAndPath(BeyondTheHorizon.MOD_ID,soundName)
                ));
    }

    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }
}
