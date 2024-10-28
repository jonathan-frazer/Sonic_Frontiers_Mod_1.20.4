package net.sonicrushxii.beyondthehorizon;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.models.HomingAttack;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.models.HummingTop;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.models.Spindash;
import net.sonicrushxii.beyondthehorizon.client.VirtualSlotOverlay;
import net.sonicrushxii.beyondthehorizon.entities.all.PointRenderer;
import net.sonicrushxii.beyondthehorizon.event_handler.*;
import net.sonicrushxii.beyondthehorizon.modded.*;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;
import net.sonicrushxii.beyondthehorizon.timehandler.TimeHandler;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BeyondTheHorizon.MOD_ID)
public class BeyondTheHorizon
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "beyondthehorizon";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();


    public BeyondTheHorizon(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new LoginHandler());
        MinecraftForge.EVENT_BUS.register(new FallDamageHandler());
        MinecraftForge.EVENT_BUS.register(new ServerTickHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerTickHandler());
        MinecraftForge.EVENT_BUS.register(new Scheduler());
        MinecraftForge.EVENT_BUS.register(new TimeHandler());
        MinecraftForge.EVENT_BUS.register(new DamageHandler());
        MinecraftForge.EVENT_BUS.register(new EquipmentChangeHandler());

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register Stuff
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEffects.register(modEventBus);
        ModEntityTypes.register(modEventBus);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerSonicForm.class);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        event.enqueueWork(PacketHandler::register);

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

            EntityRenderers.register(ModEntityTypes.TORNADO_JUMP_CLOUD.get(), PointRenderer::new);
        }

        @SubscribeEvent
        public static void registerGUIOverlays(RegisterGuiOverlaysEvent event) {
            event.registerBelow(VanillaGuiOverlay.DEBUG_TEXT.id(),
                    "ability_hud",
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
            event.register(KeyBindings.INSTANCE.useSingleAbility);
        }

        @SubscribeEvent
        public static void registerModelLayer(EntityRenderersEvent.RegisterLayerDefinitions event)
        {
            event.registerLayerDefinition(Spindash.LAYER_LOCATION,Spindash::createBodyLayer);
            event.registerLayerDefinition(HomingAttack.LAYER_LOCATION,HomingAttack::createBodyLayer);
            event.registerLayerDefinition(HummingTop.LAYER_LOCATION,HummingTop::createBodyLayer);
        }
    }
}
