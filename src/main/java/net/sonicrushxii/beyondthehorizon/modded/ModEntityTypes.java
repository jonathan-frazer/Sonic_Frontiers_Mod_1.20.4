package net.sonicrushxii.beyondthehorizon.modded;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.entities.baseform.TornadoJumpCloud;

@Mod.EventBusSubscriber(modid = BeyondTheHorizon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BeyondTheHorizon.MOD_ID);

    public static final RegistryObject<EntityType<TornadoJumpCloud>> TORNADO_JUMP_CLOUD = ENTITY_TYPES.register("tornado_jump_cloud",
            () -> EntityType.Builder.<TornadoJumpCloud>of(TornadoJumpCloud::new, MobCategory.MISC)
                    .sized(3.0F, 0.5F)  // Define the size of the entity
                    .build("tornado_jump_cloud"));

    public static void register(IEventBus eventBus){ ENTITY_TYPES.register(eventBus);}
}
