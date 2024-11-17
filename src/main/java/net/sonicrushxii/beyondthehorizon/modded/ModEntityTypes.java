package net.sonicrushxii.beyondthehorizon.modded;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.entities.baseform.CycloneKickCloud;
import net.sonicrushxii.beyondthehorizon.entities.baseform.TornadoJumpCloud;
import net.sonicrushxii.beyondthehorizon.entities.baseform.mirage.MirageCloud;
import net.sonicrushxii.beyondthehorizon.entities.baseform.mirage.MirageEntity;

@Mod.EventBusSubscriber(modid = BeyondTheHorizon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BeyondTheHorizon.MOD_ID);

    public static final RegistryObject<EntityType<TornadoJumpCloud>> TORNADO_JUMP_CLOUD = ENTITY_TYPES.register("tornado_jump_cloud",
            () -> EntityType.Builder.<TornadoJumpCloud>of(TornadoJumpCloud::new, MobCategory.MISC)
                    .sized(1F, 5F)  // Define the size of the entity
                    .build("tornado_jump_cloud"));
    public static final RegistryObject<EntityType<MirageCloud>> MIRAGE_CLOUD = ENTITY_TYPES.register("mirage_cloud",
            () -> EntityType.Builder.<MirageCloud>of(MirageCloud::new, MobCategory.MISC)
                    .sized(5F, 5F)  // Define the size of the entity
                    .build("mirage_cloud"));
    public static final RegistryObject<EntityType<MirageEntity>> SONIC_BASEFORM_MIRAGE = ENTITY_TYPES.register("baseform_mirage",
            () -> EntityType.Builder.<MirageEntity>of(MirageEntity::new, MobCategory.MISC)
                    .sized(1F, 2F)  // Define the size of the entity
                    .build("baseform_mirage"));
    public static final RegistryObject<EntityType<CycloneKickCloud>> CYCLONE_KICK_CLOUD = ENTITY_TYPES.register("cyclone_kick_cloud",
            () -> EntityType.Builder.<CycloneKickCloud>of(CycloneKickCloud::new, MobCategory.MISC)
                    .sized(1F, 5F)  // Define the size of the entity
                    .build("cyclone_kick_cloud"));

    public static void register(IEventBus eventBus){ ENTITY_TYPES.register(eventBus);}
}
