package net.sonicrushxii.beyondthehorizon.capabilities.baseform;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.models.*;
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

        //Peelout
        if(baseformProperties.boostLvl == 2 && player.isSprinting() && (player.onGround() || baseformProperties.isWaterBoosting))
        {
            poseStack.pushPose();

            //Scale
            poseStack.scale(1.0f, 1.0f, 1.0f);

            //Apply Rotation & Translation
            poseStack.mulPose(Axis.YP.rotationDegrees(-player.getYRot()));
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

            poseStack.translate(0D,-1.5D,0D);

            //Render The Custom Model
            ModModelRenderer.renderSonicPeelout(SonicPeeloutModel.class,event,poseStack,baseformProperties,(modelPart)->{
                modelPart.getChild("Head").xRot = (float)(player.getXRot()*Math.PI/180);
            });
            poseStack.popPose();
            event.setCanceled(true);
        }

        //Ballform
        else if(baseformProperties.shouldBeInBallform())
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
            ModModelRenderer.renderModel(Spindash.class, event, poseStack,null);
            poseStack.popPose();

            event.setCanceled(true);
        }

        //Humming Top
        else if(baseformProperties.hummingTop > 0)
        {
            //Rotate Player
            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(-2F));
            poseStack.mulPose(Axis.YP.rotationDegrees((baseformProperties.hummingTop%7)*51.42F));
        }

        //Mirage
        else if(baseformProperties.mirageTimer > 0)
        {
            event.setCanceled(true);
        }

        //Spin Slash
        else if(baseformProperties.spinSlash != 0)
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
            ModModelRenderer.renderModel(Spinslash.class, event, poseStack,null);
            poseStack.popPose();

            event.setCanceled(true);
        }

        //Cyclone Kick
        else if(baseformProperties.cycloneKick > 0)
        {
            //Rotate Player
            poseStack.pushPose();
            if(baseformProperties.cycloneKick <= 3)
                poseStack.mulPose(Axis.ZP.rotationDegrees(60F));
            else if(baseformProperties.cycloneKick <= 63) {
                final byte offsetCycloneKick = (byte) (baseformProperties.cycloneKick-5);
                poseStack.mulPose(Axis.ZP.rotationDegrees(60F + (offsetCycloneKick) * 1.33F));
                poseStack.mulPose(Axis.XP.rotationDegrees(-(offsetCycloneKick % 15) * 24F));
                poseStack.mulPose(Axis.YP.rotationDegrees(-(offsetCycloneKick % 15) * 24F));
            }
            else {
                poseStack.mulPose(Axis.ZP.rotationDegrees(180F));
                poseStack.mulPose(Axis.XP.rotationDegrees(2F));
                poseStack.mulPose(Axis.YP.rotationDegrees(2F));
            }
        }

        //Wild Rush
        else if(baseformProperties.wildRushTime > 10 || baseformProperties.wildRushTime < 0)
        {
            poseStack.pushPose();

            // Scale
            poseStack.scale(1.0f, 1.0f, 1.0f);

            //Apply Rotation & Translation
            poseStack.mulPose(Axis.YP.rotationDegrees(-BaseformClient.ClientOnlyData.wildRushYawPitch[0]));
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F+BaseformClient.ClientOnlyData.wildRushYawPitch[1]));

            //Render The Custom Model
            ModModelRenderer.renderPlayerModel(SonicFlatPlayerModel.class,event,poseStack,baseformProperties,null);

            poseStack.popPose();
            event.setCanceled(true);
        }

        //Loop Kick
        else if(baseformProperties.loopKick > 0 && !(baseformProperties.loopKick >= 24 && baseformProperties.loopKick <= 28))
        {
            poseStack.pushPose();

            // Scale
            poseStack.scale(1.0f, 1.0f, 1.0f);

            if(baseformProperties.loopKick < 24)
            {
                //Apply Rotation & Translation
                poseStack.mulPose(Axis.YP.rotationDegrees(-player.getYRot()));
                poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F-baseformProperties.loopKick*15.0F));

                //Render The Custom Model
                ModModelRenderer.renderPlayerModel(SonicFlatPlayerModel.class,event,poseStack,baseformProperties,null);
            }
            else
            {
                //Apply Rotation & Translation
                poseStack.mulPose(Axis.YP.rotationDegrees(-player.getYRot()));
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.translate(0D,-1.5D,0D);

                //Render The Custom Model
                ModModelRenderer.renderPlayerModel(LoopKickPlayerModel.class,event,poseStack,baseformProperties,null);
            }

            poseStack.popPose();
            event.setCanceled(true);
        }
        else if(baseformProperties.loopKick < 0)
        {
            poseStack.pushPose();

            // Scale
            poseStack.scale(1.0f, 1.0f, 1.0f);

            //Apply Rotation & Translation
            poseStack.mulPose(Axis.YP.rotationDegrees(-player.getYRot()));
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            poseStack.translate(0D,-1.5D,0D);

            //Render The Custom Model
            ModModelRenderer.renderPlayerModel(LoopKickPlayerModel.class, event, poseStack,baseformProperties,null);

            poseStack.popPose();
            event.setCanceled(true);
        }

        //Sonic Boom
        else if(baseformProperties.sonicBoom > 0)
        {
            poseStack.pushPose();

            // Scale
            poseStack.scale(1.0f, 1.0f, 1.0f);

            //Apply Rotation & Translation
            poseStack.mulPose(Axis.YP.rotationDegrees(-player.getYRot() + ((baseformProperties.sonicBoom%6)-2)*36.0F ));
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

            poseStack.translate(0D,-1.5D,0D);

            //Render The Custom Model
            ModModelRenderer.renderPlayerModel(SonicBoomPlayerModel.class, event, poseStack, baseformProperties,null);

            poseStack.popPose();
            event.setCanceled(true);
        }

        //Cross Slash
        else if(baseformProperties.crossSlash > 0)
        {
            poseStack.pushPose();

            // Scale
            poseStack.scale(1.0f, 1.0f, 1.0f);

            //Apply Rotation & Translation
            poseStack.mulPose(Axis.YP.rotationDegrees(-player.getYRot()));
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

            poseStack.translate(0D,-1.5D,0D);

            //Render The Custom Model
            ModModelRenderer.renderPlayerModel(CrossSlashPlayerModel.class, event, poseStack, baseformProperties,
                    (modelPart)->{
                        modelPart.getChild("CrossArm").xRot = player.getXRot()/60;
                        modelPart.getChild("Head").xRot = player.getXRot()/60;
                    });

            poseStack.popPose();
            event.setCanceled(true);
        }

        //Parry
        else if(baseformProperties.parryTime > 0)
        {
            poseStack.pushPose();

            //Scale
            poseStack.scale(1.0f, 1.0f, 1.0f);

            //Apply Rotation & Translation
            poseStack.mulPose(Axis.YP.rotationDegrees(-player.getYRot()));
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

            poseStack.translate(0D,-1.5D,0D);

            //Render The Custom Model
            ModModelRenderer.renderPlayerModel(ParryModelPre.class, event, poseStack, baseformProperties,null);

            poseStack.popPose();
            event.setCanceled(true);
        }
        else if(baseformProperties.parryTime <= -50)
        {
            byte animationFrame = (byte)(baseformProperties.parryTime+60);

            poseStack.pushPose();

            // Scale
            poseStack.scale(1.0f, 1.0f, 1.0f);

            //Apply Rotation & Translation
            poseStack.mulPose(Axis.YP.rotationDegrees(-player.getYRot()));
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

            poseStack.translate(0D,-1.5D,0D);

            //Render The Custom Model
            ModModelRenderer.renderPlayerModel(ParryModelPost.class, event, poseStack, baseformProperties,
                    (modelPart)->{
                        modelPart.getChild("RightArm").yRot -= Math.min(5,animationFrame)/15.0F;
                        modelPart.getChild("LeftArm").yRot  -= Math.min(5,animationFrame)/1.42F;
                        modelPart.getChild("Head").yRot     -= Math.min(5,animationFrame)/7.0F;
                        modelPart.getChild("Body").yRot     -= Math.min(5,animationFrame)/5.0F;
                    });

            poseStack.popPose();
            event.setCanceled(true);
        }

        //Grand Slam
        else if(baseformProperties.grandSlamTime > 0)
        {
            //Render The Slamming enemy upward
            if(baseformProperties.grandSlamTime < 30)
            {
                poseStack.pushPose();

                //Translate
                poseStack.translate(0.0D, -0.65D, 0.0D);

                // Scale
                poseStack.scale(1.15f, 1.15f, 1.15f);

                //Apply Rotation
                poseStack.mulPose(Axis.YP.rotationDegrees(-baseformProperties.atkRotPhase));

                ModModelRenderer.renderModel(GrandSlamModel.class, event, poseStack,(modelPart -> {
                    modelPart.getChild("SpikeAura").yRot += (float) Math.PI;
                    modelPart.getChild("SpikeAura").xRot -= (float) ((baseformProperties.grandSlamTime%10)*(Math.PI/5));
                }));

                poseStack.popPose();

                event.setCanceled(true);
            }

            else if(baseformProperties.grandSlamTime > 35)
            {
                poseStack.pushPose();

                // Scale
                poseStack.scale(1.0f, 1.0f, 1.0f);

                //Apply Rotation & Translation
                poseStack.mulPose(Axis.YP.rotationDegrees(-(player.getYRot()+((baseformProperties.grandSlamTime>=39)?18.0F*(40-baseformProperties.grandSlamTime):36.0F))));
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

                poseStack.translate(0D,-1.5D,0D);

                //Render The Custom Model
                ModModelRenderer.renderPlayerModel(SonicBoomPlayerModel.class, event, poseStack, baseformProperties,null);

                poseStack.popPose();
                event.setCanceled(true);
            }
        }

        //Phantom Rush
        else if(baseformProperties.ultimateUse > 1 && baseformProperties.ultimateUse < 60)
        {
            //Sonic Render stops, Rendering will be done on the targeted mob
            event.setCanceled(true);
        }

        //Coriolis Kick
        else if(baseformProperties.ultimateUse >= 200)
        {
            poseStack.pushPose();

            // Scale
            poseStack.scale(1.0f, 1.0f, 1.0f);

            //Apply Rotation & Translation
            poseStack.mulPose(Axis.YP.rotationDegrees(-player.getYRot()));
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            poseStack.translate(0D,-1.5D,0D);

            //Render The Custom Model
            ModModelRenderer.renderPlayerModel(UltimateKickModel.class, event, poseStack,baseformProperties,null);

            poseStack.popPose();
            event.setCanceled(true);
        }
    }

    public static void onRenderPlayerModelPost(RenderLivingEvent.Post<?,?> event, Player player, BaseformProperties baseformProperties)
    {
        PoseStack poseStack = event.getPoseStack();
        // Humming Top
        if(baseformProperties.hummingTop > 0)
        {
            poseStack.popPose();

            // Push the current matrix stack
            poseStack.pushPose();

            // Translate
            poseStack.translate(0.0D, -1.65D, 0.0D);

            // Scale
            poseStack.scale(2.5f, 2.5f, 2.5f);

            //Apply Rotation
            poseStack.mulPose(Axis.XP.rotationDegrees(-2F));
            poseStack.mulPose(Axis.YP.rotationDegrees((float)-(player.getY()-(baseformProperties.hummingTop%7)*51.42F)));

            // Render the custom model
            ModModelRenderer.renderModel(HummingTop.class, event, poseStack,null);

            poseStack.popPose();
        }

        //Cyclone Kick
        if(baseformProperties.cycloneKick > 0)
        {
            poseStack.popPose();
        }

    }

    public static void onRenderToSelfPre(RenderLivingEvent.Pre<?, ?> event, LivingEntity target, BaseformProperties baseformProperties)
    {

    }

    public static void onRenderToSelfPost(RenderLivingEvent.Post<?, ?> event, LivingEntity target, BaseformProperties baseformProperties)
    {
        AbstractClientPlayer player = Minecraft.getInstance().player;

        assert player != null;
        PoseStack poseStack = event.getPoseStack();

        //Homing Attack
        {
            if (VirtualSlotHandler.getCurrAbility() == 1 && target.getUUID().equals(BaseformClient.ClientOnlyData.homingAttackReticle))
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
                poseStack.mulPose(Axis.XP.rotationDegrees(yawPitch[1]));

                // Render the custom model
                ModModelRenderer.renderModel(HomingAttack.class, event, poseStack,null);

                poseStack.popPose();
            }
        }
    }

    public static void onRenderToEveryonePre(RenderLivingEvent.Pre<?,?> event, LivingEntity target)
    {
    }

    public static void onRenderToEveryonePost(RenderLivingEvent.Post<?,?> event, LivingEntity target)
    {

    }


}
