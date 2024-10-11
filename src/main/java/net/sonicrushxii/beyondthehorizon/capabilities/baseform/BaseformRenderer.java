package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.models.HomingAttack;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.models.Spindash;
import net.sonicrushxii.beyondthehorizon.client.ClientFormData;
import net.sonicrushxii.beyondthehorizon.client.VirtualSlotHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModModelRenderer;

@Mod.EventBusSubscriber(modid = BeyondTheHorizon.MOD_ID, value= Dist.CLIENT)
public class BaseformRenderer
{
    /**
     * If you want to turn off Player Rendering set event.setCanceled(true)
     * Generally turn off player rendering within an if Condition. Else player will always be invisible
     */
    public static void onRenderPlayerModelPre(RenderLivingEvent.Pre<?,?> event, Player player, BaseformProperties baseformProperties)
    {
        PoseStack poseStack = event.getPoseStack();

        //Ballform
        if (baseformProperties.ballFormState >= 1 || baseformProperties.homingAttackAirTime > 1)
        {
            poseStack.pushPose();

            //Translate
            poseStack.translate(0.0D, -0.65D, 0.0D);

            // Scale
            poseStack.scale(1.15f, 1.15f, 1.15f);

            //Apply Rotation
            float playerYaw = (player.getYRot() > 180.0) ? player.getYRot() - 180.0f : player.getYRot() + 180.0f;
            poseStack.mulPose(Axis.YP.rotationDegrees(-playerYaw));

            //Render The Custom Model
            ModModelRenderer.renderModel(Spindash.class, event, poseStack);
            poseStack.popPose();

            event.setCanceled(true);
        }
    }

    public static void onRenderPlayerModelPost(RenderLivingEvent.Post<?,?> event, Player player, BaseformProperties baseformProperties)
    {

    }

    public static void onRenderToSelfPre(RenderLivingEvent.Pre<?, ?> event, LivingEntity target, BaseformProperties baseformProperties)
    {

    }

    public static void onRenderToSelfPost(RenderLivingEvent.Post<?, ?> event, LivingEntity target, BaseformProperties baseformProperties)
    {
        LocalPlayer player = Minecraft.getInstance().player;

        assert player != null;
        PoseStack poseStack = event.getPoseStack();

        //Homing Attack
        {
            if (VirtualSlotHandler.getCurrAbility() == 1 && target.getUUID().equals(BaseformClient.homingAttackReticle))
            {
                Vec3 playerPos = new Vec3(player.getX(),player.getY()+target.getEyeHeight(),player.getZ());
                Vec3 targetPos = new Vec3(target.getX(),target.getY()+target.getEyeHeight(),target.getZ());

                // Push the current matrix stack
                poseStack.pushPose();

                //Get the vector from enemy to Player
                Vec3 dir = playerPos.subtract(targetPos).normalize().scale(1.5);
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

    public static void onRenderToEveryonePost(RenderLivingEvent.Post<?,?> event, LivingEntity target)
    {

    }

    public static void onRenderToEveryonePre(RenderLivingEvent.Pre<?,?> event, LivingEntity target)
    {

    }
}
