package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformRenderer;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;

@EventBusSubscriber(modid = BeyondTheHorizon.MOD_ID, value= Dist.CLIENT)
public class RenderHandler {
    @SubscribeEvent
    public static void onPreRenderLiving(RenderLivingEvent.Pre<?, ?> event) {

        //Manage Player Models
        {
            try {
                LivingEntity entity = event.getEntity();
                {
                    PlayerSonicForm playerSonicForm = entity.getData(ModAttachments.PLAYER_SONIC_FORM);
                    switch(playerSonicForm.getCurrentForm())
                    {
                        case BASEFORM -> BaseformRenderer.onRenderPlayerModelPre(event, (Player)entity,(BaseformProperties) playerSonicForm.getFormProperties());
                        /*
                        case SUPERFORM
                        case STARFALLFORM
                        case HYPERFORM
                         */
                    }
                }
            } catch (NullPointerException | NoSuchMethodError | ClassCastException ignored) {}

            /*
            try {
                LivingEntity entity = event.getEntity();
                AbstractClientPlayer player = Minecraft.getInstance().player;

                if (player != null && entity.is(player)) {
                    PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                    switch(playerSonicForm.getCurrentForm())
                    {
                        case BASEFORM -> BaseformRenderer.onRenderPlayerModelPre(event, player, (BaseformProperties)playerSonicForm.getFormProperties());
                            //case SUPERFORM
                            //case STARFALLFORM
                            //case HYPERFORM

                    }
                }

            } catch (NullPointerException | NoSuchMethodError | ClassCastException ignored) {}
            */
        }

        //Manage Render to Everyone
        {
            BaseformRenderer.onRenderToEveryonePre(event, event.getEntity());
        }

        //Manage Render to Self
        {
            AbstractClientPlayer player = Minecraft.getInstance().player;
            {
                PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                switch(playerSonicForm.getCurrentForm())
                {
                    case BASEFORM:      BaseformRenderer.onRenderToSelfPre(event, event.getEntity(), (BaseformProperties) playerSonicForm.getFormProperties());
                    case SUPERFORM:
                    case STARFALLFORM:
                    case HYPERFORM:
                    case PLAYER:
                }
            }

        }


    }

    @SubscribeEvent
    public static void onPostRenderLiving(RenderLivingEvent.Post<?, ?> event)
    {
        //Manage Player Models
        {
            try {
                LivingEntity entity = event.getEntity();
                {
                    PlayerSonicForm playerSonicForm = entity.getData(ModAttachments.PLAYER_SONIC_FORM);
                    switch(playerSonicForm.getCurrentForm())
                    {
                        case BASEFORM -> BaseformRenderer.onRenderPlayerModelPost(event, (Player)entity,(BaseformProperties) playerSonicForm.getFormProperties());
                        /*
                        case SUPERFORM
                        case STARFALLFORM
                        case HYPERFORM
                         */
                    }
                }
            } catch (NullPointerException | NoSuchMethodError | ClassCastException ignored) {}

            /*
            try {
                LivingEntity entity = event.getEntity();
                AbstractClientPlayer player = Minecraft.getInstance().player;

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
            AbstractClientPlayer player = Minecraft.getInstance().player;
            {
                PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                switch(playerSonicForm.getCurrentForm())
                {
                    case BASEFORM: BaseformRenderer.onRenderToSelfPost(event, event.getEntity(), (BaseformProperties) playerSonicForm.getFormProperties());
                    case SUPERFORM:
                    case STARFALLFORM:
                    case HYPERFORM:
                    case PLAYER:
                }
            }
        }
    }
}
