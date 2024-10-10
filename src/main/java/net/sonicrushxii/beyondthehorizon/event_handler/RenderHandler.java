package net.sonicrushxii.beyondthehorizon.event_handler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.client.ClientFormData;
import net.sonicrushxii.beyondthehorizon.client.VirtualSlotHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModModelRenderer;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.models.HomingAttack;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.models.Spindash;

@Mod.EventBusSubscriber(modid = BeyondTheHorizon.MOD_ID, value= Dist.CLIENT)
public class RenderHandler {
    @SubscribeEvent
    public static void onPreRenderLiving(RenderLivingEvent.Pre<?, ?> event) {
        //Spindash
        {
            try {
                LivingEntity entity = event.getEntity();

                entity.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
                    //Get Data From the Player
                    BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                    //PoseStack
                    PoseStack poseStack = event.getPoseStack();

                    if (baseformProperties.ballFormState >= 1 || baseformProperties.homingAttackAirTime > 1) {
                        poseStack.pushPose();

                        // Translate to the entity's position
                        poseStack.translate(0.0D, -0.65D, 0.0D);

                        // Scale
                        poseStack.scale(1.15f, 1.15f, 1.15f);

                        //Control Orientation
                        float entityYaw = (entity.getYRot() > 180.0) ? entity.getYRot() - 180.0f : entity.getYRot() + 180.0f;
                        poseStack.mulPose(Axis.YP.rotationDegrees(-entityYaw));

                        //Render The Custom Model
                        ModModelRenderer.renderModel(Spindash.class, event, poseStack);
                        poseStack.popPose();

                        event.setCanceled(true);
                    }
                });
            } catch (NullPointerException | NoSuchMethodError | ClassCastException ignored) {
            }

            try {
                BaseformProperties baseformProperties = (BaseformProperties) ClientFormData.getPlayerFormDetails();
                if (baseformProperties.ballFormState >= 1 || baseformProperties.homingAttackAirTime > 1) {
                    LivingEntity entity = event.getEntity();
                    LocalPlayer player = Minecraft.getInstance().player;
                    if (player != null && entity.is(player)) {
                        PoseStack poseStack = event.getPoseStack();

                        poseStack.pushPose();

                        //Translate
                        poseStack.translate(0.0D, -0.65D, 0.0D);

                        // Scale
                        poseStack.scale(1.15f, 1.15f, 1.15f);


                        //Apply Rotation
                        float entityYaw = (entity.getYRot() > 180.0) ? entity.getYRot() - 180.0f : entity.getYRot() + 180.0f;
                        poseStack.mulPose(Axis.YP.rotationDegrees(-entityYaw));

                        //Render The Custom Model
                        ModModelRenderer.renderModel(Spindash.class, event, poseStack);
                        poseStack.popPose();

                        event.setCanceled(true);
                    }
                }
            } catch (NullPointerException | NoSuchMethodError | ClassCastException ignored) {}
        }
    }

    @SubscribeEvent
    public static void onPostRenderLiving(RenderLivingEvent.Post<?, ?> event)
    {
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
