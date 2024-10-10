package net.sonicrushxii.beyondthehorizon.event_handler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformRenderer;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.client.ClientFormData;
import net.sonicrushxii.beyondthehorizon.client.VirtualSlotHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModModelRenderer;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.models.HomingAttack;

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

            try {
                LivingEntity entity = event.getEntity();
                LocalPlayer player = Minecraft.getInstance().player;

                if (player != null && entity.is(player))
                    switch(ClientFormData.getPlayerForm())
                    {
                        case BASEFORM -> BaseformRenderer.onRenderPlayerModelPre(event, player, (BaseformProperties)ClientFormData.getPlayerFormDetails());
                                /*
                                case SUPERFORM
                                case STARFALLFORM
                                case HYPERFORM
                                 */
                    }

            } catch (NullPointerException | NoSuchMethodError | ClassCastException ignored) {}
        }

        //Manage Render to Everyone
        {
            BaseformRenderer.onRenderToEveryonePre(event, event.getEntity());
        }

        //Manage Render to Self
        {
            switch(ClientFormData.getPlayerForm())
            {
                case BASEFORM:      BaseformRenderer.onRenderToSelfPre(event, event.getEntity(), (BaseformProperties) ClientFormData.getPlayerFormDetails());
                case SUPERFORM:
                case STARFALLFORM:
                case HYPERFORM:
                case PLAYER:
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

            try {
                LivingEntity entity = event.getEntity();
                LocalPlayer player = Minecraft.getInstance().player;

                if (player != null && entity.is(player))
                    switch(ClientFormData.getPlayerForm())
                    {
                        case BASEFORM -> BaseformRenderer.onRenderPlayerModelPost(event, player, (BaseformProperties)ClientFormData.getPlayerFormDetails());
                                /*
                                case SUPERFORM
                                case STARFALLFORM
                                case HYPERFORM
                                 */
                    }

            } catch (NullPointerException | NoSuchMethodError | ClassCastException ignored) {}
        }

        //Manage Render to Everyone
        {
            BaseformRenderer.onRenderToEveryonePost(event, event.getEntity());
        }

        //Manage Render to Self
        {
            switch(ClientFormData.getPlayerForm())
            {
                case BASEFORM -> BaseformRenderer.onRenderToSelfPost(event, event.getEntity(), (BaseformProperties) ClientFormData.getPlayerFormDetails());
                /*
                case SUPERFORM
                case STARFALLFORM
                case HYPERFORM
                 */
            }
        }

        LivingEntity entity = event.getEntity();
        LocalPlayer player = Minecraft.getInstance().player;

        assert player != null;
        Vec3 playerPos = new Vec3(player.getX(),player.getY()+entity.getEyeHeight(),player.getZ());
        Vec3 entityPos = new Vec3(entity.getX(),entity.getY()+entity.getEyeHeight(),entity.getZ());

        //Homing Attack
        {
            if (VirtualSlotHandler.getCurrAbility() == 1 && entity.getUUID().equals(ClientFormData.hasHomingReticle()))
            {
                PoseStack poseStack = event.getPoseStack();

                // Push the current matrix stack
                poseStack.pushPose();

                Vec3 dir = playerPos.subtract(entityPos).normalize().scale(1.5);
                poseStack.translate(dir.x, dir.y, dir.z);

                //Apply Rotation
                float[] yawPitch = Utilities.getYawPitchFromVec(dir);
                poseStack.mulPose(Axis.YP.rotationDegrees(-yawPitch[0]));
                poseStack.mulPose(Axis.ZP.rotationDegrees(yawPitch[1]));

                // Render the custom model
                ModModelRenderer.renderModel(HomingAttack.class, event, poseStack);

                poseStack.popPose();
            }
        }
    }
}
