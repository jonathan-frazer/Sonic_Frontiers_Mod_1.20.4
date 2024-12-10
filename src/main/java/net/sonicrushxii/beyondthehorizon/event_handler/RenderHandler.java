package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformRenderer;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;

@Mod.EventBusSubscriber(modid = BeyondTheHorizon.MOD_ID, value= Dist.CLIENT)
public class RenderHandler {
    @SubscribeEvent
    public static void onPreRenderLiving(RenderLivingEvent.Pre<?, ?> event) {

        //Manage Player Models
        {
            try {
                LivingEntity entity = event.getEntity();
                entity.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
                    switch(playerSonicForm.getCurrentForm())
                    {
                        case BASEFORM -> BaseformRenderer.onRenderPlayerModelPre(event, (Player)entity,(BaseformProperties) playerSonicForm.getFormProperties());
                        /*
                        case SUPERFORM
                        case STARFALLFORM
                        case HYPERFORM
                         */
                    }
                });
            } catch (NullPointerException | NoSuchMethodError | ClassCastException ignored) {}

            /*
            try {
                LivingEntity entity = event.getEntity();
                LocalPlayer player = Minecraft.getInstance().player;

                if (player != null && entity.is(player))
                    player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
                        switch(playerSonicForm.getCurrentForm())
                        {
                            case BASEFORM -> BaseformRenderer.onRenderPlayerModelPre(event, player, (BaseformProperties)playerSonicForm.getFormProperties());
                                //case SUPERFORM
                                //case STARFALLFORM
                                //case HYPERFORM

                        }
                    });

            } catch (NullPointerException | NoSuchMethodError | ClassCastException ignored) {}
            */
        }

        //Manage Render to Everyone
        {
            BaseformRenderer.onRenderToEveryonePre(event, event.getEntity());
        }

        //Manage Render to Self
        {
            LocalPlayer player = Minecraft.getInstance().player;
            player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
                switch(playerSonicForm.getCurrentForm())
                {
                    case BASEFORM:      BaseformRenderer.onRenderToSelfPre(event, event.getEntity(), (BaseformProperties) playerSonicForm.getFormProperties());
                    case SUPERFORM:
                    case STARFALLFORM:
                    case HYPERFORM:
                    case PLAYER:
                }
            });

        }


    }

    @SubscribeEvent
    public static void onPostRenderLiving(RenderLivingEvent.Post<?, ?> event)
    {
        //Manage Player Models
        {
            try {
                LivingEntity entity = event.getEntity();
                entity.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
                    switch(playerSonicForm.getCurrentForm())
                    {
                        case BASEFORM -> BaseformRenderer.onRenderPlayerModelPost(event, (Player)entity,(BaseformProperties) playerSonicForm.getFormProperties());
                        /*
                        case SUPERFORM
                        case STARFALLFORM
                        case HYPERFORM
                         */
                    }
                });
            } catch (NullPointerException | NoSuchMethodError | ClassCastException ignored) {}

            /*
            try {
                LivingEntity entity = event.getEntity();
                LocalPlayer player = Minecraft.getInstance().player;

                if (player != null && entity.is(player))
                    switch(ClientFormData.getPlayerForm())
                    {
                        case BASEFORM -> BaseformRenderer.onRenderPlayerModelPost(event, player, (BaseformProperties)ClientFormData.getPlayerFormDetails());
                                //case SUPERFORM
                                //case STARFALLFORM
                                //case HYPERFORM

                    }

            } catch (NullPointerException | NoSuchMethodError | ClassCastException ignored) {}*/
        }

        //Manage Render to Everyone
        {
            BaseformRenderer.onRenderToEveryonePost(event, event.getEntity());
        }

        //Manage Render to Self
        {
            LocalPlayer player = Minecraft.getInstance().player;
            player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
                switch(playerSonicForm.getCurrentForm())
                {
                    case BASEFORM: BaseformRenderer.onRenderToSelfPost(event, event.getEntity(), (BaseformProperties) playerSonicForm.getFormProperties());
                    case SUPERFORM:
                    case STARFALLFORM:
                    case HYPERFORM:
                    case PLAYER:
                }
            });
        }
    }
}
