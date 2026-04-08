package net.sonicrushxii.beyondthehorizon;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.models.*;
import net.sonicrushxii.beyondthehorizon.client.VirtualSlotOverlay;
import net.sonicrushxii.beyondthehorizon.entities.all.PointRenderer;
import net.sonicrushxii.beyondthehorizon.entities.baseform.cross_slash.CrossSlashRenderer;
import net.sonicrushxii.beyondthehorizon.entities.baseform.homing_shot.HomingShotRenderer;
import net.sonicrushxii.beyondthehorizon.entities.baseform.mirage.MirageRenderer;
import net.sonicrushxii.beyondthehorizon.entities.baseform.phantom_rush.PhantomRushRenderer;
import net.sonicrushxii.beyondthehorizon.entities.baseform.sonic_boom.SonicBoomRenderer;
import net.sonicrushxii.beyondthehorizon.entities.baseform.sonic_wind.SonicWindRenderer;
import net.sonicrushxii.beyondthehorizon.event_handler.*;
import net.sonicrushxii.beyondthehorizon.modded.*;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;
import net.sonicrushxii.beyondthehorizon.timehandler.TimeHandler;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(BeyondTheHorizon.MOD_ID)
public class BeyondTheHorizon
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "beyondthehorizon";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void initializeMod(BeyondTheHorizon thisMod, ModContainer container)
    {
        IEventBus modEventBus = container.getEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(thisMod::commonSetup);

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(thisMod);
        NeoForge.EVENT_BUS.register(new LoginHandler());
        NeoForge.EVENT_BUS.register(new FallDamageHandler());
        NeoForge.EVENT_BUS.register(new ServerTickHandler());
        NeoForge.EVENT_BUS.register(new PlayerTickHandler());
        NeoForge.EVENT_BUS.register(new Scheduler());
        NeoForge.EVENT_BUS.register(new TimeHandler());
        NeoForge.EVENT_BUS.register(new DamageHandler());
        NeoForge.EVENT_BUS.register(new EquipmentChangeHandler());

        // Register the item to a creative tab
        modEventBus.addListener(thisMod::addCreative);

        // Register payload handlers for networking
        modEventBus.addListener(PacketHandler::onRegisterPayloadHandlers);

        // Register Stuff
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEffects.register(modEventBus);
        ModEntityTypes.register(modEventBus);
        ModAttachments.register(modEventBus);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        container.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public BeyondTheHorizon(ModContainer context)
    {
        initializeMod(this, context);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {}

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

            //Entities
            EntityRenderers.register(ModEntityTypes.TORNADO_JUMP_CLOUD.get(), PointRenderer::new);
            EntityRenderers.register(ModEntityTypes.MIRAGE_CLOUD.get(), PointRenderer::new);
            EntityRenderers.register(ModEntityTypes.SONIC_BASEFORM_MIRAGE.get(), MirageRenderer::new);
            EntityRenderers.register(ModEntityTypes.BASEFORM_CYCLONE_KICK_CLOUD.get(), PointRenderer::new);
            EntityRenderers.register(ModEntityTypes.BASEFORM_SPIN_SLASH_CLOUD.get(), PointRenderer::new);
            EntityRenderers.register(ModEntityTypes.BASEFORM_CROSS_SLASH.get(), CrossSlashRenderer::new);
            EntityRenderers.register(ModEntityTypes.BASEFORM_SONIC_BOOM.get(), SonicBoomRenderer::new);
            EntityRenderers.register(ModEntityTypes.BASEFORM_SONIC_WIND.get(), SonicWindRenderer::new);
            EntityRenderers.register(ModEntityTypes.BASEFORM_HOMING_SHOT.get(), HomingShotRenderer::new);
            EntityRenderers.register(ModEntityTypes.BASEFORM_PHANTOM_RUSH_CLOUD.get(), PointRenderer::new);
            EntityRenderers.register(ModEntityTypes.BASEFORM_PHANTOM_RUSH_ENTITY.get(), PhantomRushRenderer::new);

            //Menu Screens
        }

        @SubscribeEvent
        public static void registerGUIOverlays(RegisterGuiLayersEvent event) {
            event.registerBelow(VanillaGuiLayers.DEBUG_OVERLAY,
                    ResourceLocation.fromNamespaceAndPath(MOD_ID, "ability_hud"),
                    VirtualSlotOverlay.ABILITY_HUD);
        }

        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent event){
            event.register(KeyBindings.INSTANCE.doubleJump);
            event.register(KeyBindings.INSTANCE.toggleDangerSense);
            event.register(KeyBindings.INSTANCE.virtualSlotUse);
            event.register(KeyBindings.INSTANCE.useAbility1);
            event.register(KeyBindings.INSTANCE.useAbility2);
            event.register(KeyBindings.INSTANCE.useAbility3);
            event.register(KeyBindings.INSTANCE.useAbility4);
            event.register(KeyBindings.INSTANCE.useAbility5);
            event.register(KeyBindings.INSTANCE.useAbility6);
            event.register(KeyBindings.INSTANCE.parryKey);
            event.register(KeyBindings.INSTANCE.useSingleAbility);
            event.register(KeyBindings.INSTANCE.useUltimateAbility);
            event.register(KeyBindings.INSTANCE.helpButton);
        }

        @SubscribeEvent
        public static void registerModelLayer(EntityRenderersEvent.RegisterLayerDefinitions event)
        {
            //Sonic Model
            event.registerLayerDefinition(SonicFlatPlayerModel.LAYER_LOCATION, SonicFlatPlayerModel::createBodyLayer);
            //Peelout Model
            event.registerLayerDefinition(SonicPeeloutModel.LAYER_LOCATION, SonicPeeloutModel::createBodyLayer);
            //Boost Model
            event.registerLayerDefinition(SonicBoostModel.LAYER_LOCATION, SonicBoostModel::createBodyLayer);

            //Parry Model
            event.registerLayerDefinition(ParryModelPre.LAYER_LOCATION, ParryModelPre::createBodyLayer);
            event.registerLayerDefinition(ParryModelPost.LAYER_LOCATION, ParryModelPost::createBodyLayer);

            //Spin Dash
            event.registerLayerDefinition(Spindash.LAYER_LOCATION,Spindash::createBodyLayer);

            //Homing Attack
            event.registerLayerDefinition(HomingAttack.LAYER_LOCATION,HomingAttack::createBodyLayer);

            //Humming Top
            event.registerLayerDefinition(HummingTop.LAYER_LOCATION,HummingTop::createBodyLayer);

            //Mirage
            event.registerLayerDefinition(MirageModel.LAYER_LOCATION, MirageModel::createBodyLayer);

            //Spin Slash
            event.registerLayerDefinition(Spinslash.LAYER_LOCATION,Spinslash::createBodyLayer);

            //Loop Kick
            event.registerLayerDefinition(LoopKickPlayerModel.LAYER_LOCATION, LoopKickPlayerModel::createBodyLayer);

            //Sonic Boom
            event.registerLayerDefinition(SonicBoomPlayerModel.LAYER_LOCATION, SonicBoomPlayerModel::createBodyLayer);
            event.registerLayerDefinition(SonicBoomModel.LAYER_LOCATION, SonicBoomModel::createBodyLayer);

            //Cross Slash
            event.registerLayerDefinition(CrossSlashPlayerModel.LAYER_LOCATION, CrossSlashPlayerModel::createBodyLayer);
            event.registerLayerDefinition(CrossSlashModel.LAYER_LOCATION, CrossSlashModel::createBodyLayer);

            //Sonic Wind
            event.registerLayerDefinition(SonicWindModel.LAYER_LOCATION, SonicWindModel::createBodyLayer);

            //Homing Shot
            event.registerLayerDefinition(HomingShotModel.LAYER_LOCATION, HomingShotModel::createBodyLayer);

            //Grand Slam
            event.registerLayerDefinition(GrandSlamModel.LAYER_LOCATION,GrandSlamModel::createBodyLayer);

            //Phantom Rush
            event.registerLayerDefinition(PhantomRushModel_1.LAYER_LOCATION,PhantomRushModel_1::createBodyLayer);
            event.registerLayerDefinition(PhantomRushModel_2.LAYER_LOCATION,PhantomRushModel_2::createBodyLayer);
            event.registerLayerDefinition(PhantomRushModel_3.LAYER_LOCATION,PhantomRushModel_3::createBodyLayer);
            event.registerLayerDefinition(PhantomRushModel_4.LAYER_LOCATION,PhantomRushModel_4::createBodyLayer);
            event.registerLayerDefinition(PhantomRushModel_5.LAYER_LOCATION,PhantomRushModel_5::createBodyLayer);

            //Coriolis
            event.registerLayerDefinition(UltimateKickModel.LAYER_LOCATION,UltimateKickModel::createBodyLayer);
        }
    }
}
