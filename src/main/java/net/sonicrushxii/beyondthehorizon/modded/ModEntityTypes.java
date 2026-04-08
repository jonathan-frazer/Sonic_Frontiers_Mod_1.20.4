package net.sonicrushxii.beyondthehorizon.modded;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.entities.baseform.CycloneKickCloud;
import net.sonicrushxii.beyondthehorizon.entities.baseform.phantom_rush.PhantomRushCloud;
import net.sonicrushxii.beyondthehorizon.entities.baseform.SpinSlashCloud;
import net.sonicrushxii.beyondthehorizon.entities.baseform.TornadoJumpCloud;
import net.sonicrushxii.beyondthehorizon.entities.baseform.cross_slash.CrossSlashProjectile;
import net.sonicrushxii.beyondthehorizon.entities.baseform.homing_shot.HomingShotProjectile;
import net.sonicrushxii.beyondthehorizon.entities.baseform.mirage.MirageCloud;
import net.sonicrushxii.beyondthehorizon.entities.baseform.mirage.MirageEntity;
import net.sonicrushxii.beyondthehorizon.entities.baseform.phantom_rush.PhantomRushEntity;
import net.sonicrushxii.beyondthehorizon.entities.baseform.sonic_boom.SonicBoomProjectile;
import net.sonicrushxii.beyondthehorizon.entities.baseform.sonic_wind.SonicWind;

@EventBusSubscriber(modid = BeyondTheHorizon.MOD_ID)
public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, BeyondTheHorizon.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<TornadoJumpCloud>> TORNADO_JUMP_CLOUD = ENTITY_TYPES.register("baseform/tornado_jump_cloud",
            () -> EntityType.Builder.<TornadoJumpCloud>of(TornadoJumpCloud::new, MobCategory.MISC)
                    .sized(1F, 5F)  // Define the size of the entity
                    .build("baseform/tornado_jump_cloud"));
    public static final DeferredHolder<EntityType<?>, EntityType<MirageCloud>> MIRAGE_CLOUD = ENTITY_TYPES.register("baseform/mirage_cloud",
            () -> EntityType.Builder.<MirageCloud>of(MirageCloud::new, MobCategory.MISC)
                    .sized(5F, 5F)  // Define the size of the entity
                    .build("baseform/mirage_cloud"));
    public static final DeferredHolder<EntityType<?>, EntityType<MirageEntity>> SONIC_BASEFORM_MIRAGE = ENTITY_TYPES.register("baseform/mirage",
            () -> EntityType.Builder.<MirageEntity>of(MirageEntity::new, MobCategory.MISC)
                    .sized(1F, 2F)  // Define the size of the entity
                    .build("baseform/mirage"));
    public static final DeferredHolder<EntityType<?>, EntityType<CycloneKickCloud>> BASEFORM_CYCLONE_KICK_CLOUD = ENTITY_TYPES.register("baseform/cyclone_kick_cloud",
            () -> EntityType.Builder.<CycloneKickCloud>of(CycloneKickCloud::new, MobCategory.MISC)
                    .sized(1F, 5F)  // Define the size of the entity
                    .build("baseform/cyclone_kick_cloud"));
    public static final DeferredHolder<EntityType<?>, EntityType<SpinSlashCloud>> BASEFORM_SPIN_SLASH_CLOUD = ENTITY_TYPES.register("baseform/spinslash_cloud",
            () -> EntityType.Builder.<SpinSlashCloud>of(SpinSlashCloud::new, MobCategory.MISC)
                    .sized(1F, 5F)  // Define the size of the entity
                    .build("baseform/spinslash_cloud"));
    public static final DeferredHolder<EntityType<?>, EntityType<SonicBoomProjectile>> BASEFORM_SONIC_BOOM = ENTITY_TYPES.register("baseform/sonic_boom",
            () -> EntityType.Builder.<SonicBoomProjectile>of(SonicBoomProjectile::new, MobCategory.MISC)
                    .sized(1F, 5F)  // Define the size of the entity
                    .build("baseform/sonic_boom"));
    public static final DeferredHolder<EntityType<?>, EntityType<CrossSlashProjectile>> BASEFORM_CROSS_SLASH = ENTITY_TYPES.register("baseform/cross_slash",
            () -> EntityType.Builder.<CrossSlashProjectile>of(CrossSlashProjectile::new, MobCategory.MISC)
                    .sized(1F, 5F)  // Define the size of the entity
                    .build("baseform/cross_slash"));
    public static final DeferredHolder<EntityType<?>, EntityType<SonicWind>> BASEFORM_SONIC_WIND = ENTITY_TYPES.register("baseform/sonic_wind",
            () -> EntityType.Builder.<SonicWind>of(SonicWind::new, MobCategory.MISC)
                    .sized(1F, 5F)  // Define the size of the entity
                    .build("baseform/sonic_wind"));
    public static final DeferredHolder<EntityType<?>, EntityType<HomingShotProjectile>> BASEFORM_HOMING_SHOT = ENTITY_TYPES.register("baseform/homing_shot",
            () -> EntityType.Builder.<HomingShotProjectile>of(HomingShotProjectile::new, MobCategory.MISC)
                    .sized(1F, 5F)  // Define the size of the entity
                    .build("baseform/homing_shot"));
    public static final DeferredHolder<EntityType<?>, EntityType<PhantomRushCloud>> BASEFORM_PHANTOM_RUSH_CLOUD = ENTITY_TYPES.register("baseform/phantom_rush_cloud",
            () -> EntityType.Builder.<PhantomRushCloud>of(PhantomRushCloud::new, MobCategory.MISC)
                    .sized(1F, 5F)  // Define the size of the entity
                    .build("baseform/phantom_rush_cloud"));
    public static final DeferredHolder<EntityType<?>, EntityType<PhantomRushEntity>> BASEFORM_PHANTOM_RUSH_ENTITY = ENTITY_TYPES.register("baseform/phantom_rush_entity",
            () -> EntityType.Builder.<PhantomRushEntity>of(PhantomRushEntity::new, MobCategory.MISC)
                    .sized(1F, 5F)  // Define the size of the entity
                    .build("baseform/phantom_rush_entity"));


    public static void register(IEventBus eventBus){ ENTITY_TYPES.register(eventBus);}
}
