package net.sonicrushxii.beyondthehorizon.client;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.models.Spindash;

@Mod.EventBusSubscriber(modid = BeyondTheHorizon.MOD_ID)
public class RenderHandler {
    @SubscribeEvent
    public static void onPreRenderLiving(RenderLivingEvent.Pre<?, ?> event)
    {
        try
        {
            LivingEntity entity = event.getEntity();

            entity.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
                //Get Data From the Player
                BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                //PoseStack
                PoseStack poseStack = event.getPoseStack();
                MultiBufferSource buffer = event.getMultiBufferSource();

                if(baseformProperties.ballFormState >= 1 || baseformProperties.homingAttackAirTime > 1)
                {
                    poseStack.pushPose();
                    // Translate to the entity's position
                    poseStack.translate(0.0D, 0.0D, 0.0D);
                    //Control Orientation
                    controlOrientation(poseStack, entity);
                    //Render The Custom Model
                    renderCustomModel(poseStack, buffer, entity, event.getPartialTick(), event.getPackedLight());
                    poseStack.popPose();

                    event.setCanceled(true);
                }
            });
        } catch (NullPointerException|NoSuchMethodError|ClassCastException ignored) {}

        try{
            BaseformProperties baseformProperties = (BaseformProperties)ClientFormData.getPlayerFormDetails();
            if(baseformProperties.ballFormState >= 1 || baseformProperties.homingAttackAirTime > 1)
            {
                LivingEntity entity = event.getEntity();
                LocalPlayer player = Minecraft.getInstance().player;
                if(player != null && entity.is(player))
                {
                    PoseStack poseStack = event.getPoseStack();
                    MultiBufferSource buffer = event.getMultiBufferSource();

                    poseStack.pushPose();
                    // Translate to the entity's position
                    poseStack.translate(0.0D, 0.0D, 0.0D);
                    //Control Orientation
                    controlOrientation(poseStack, entity);
                    //Render The Custom Model
                    renderCustomModel(poseStack, buffer, entity, event.getPartialTick(), event.getPackedLight());
                    poseStack.popPose();

                    event.setCanceled(true);
                }
            }
        }catch (NullPointerException|NoSuchMethodError|ClassCastException ignored) {}
    }

    @SubscribeEvent
    public static void onPostRenderLiving(RenderLivingEvent.Post<?, ?> event)
    {
        LivingEntity entity = event.getEntity();
        Player player = Minecraft.getInstance().player;

        if(VirtualSlotHandler.getCurrAbility() != 1)
            return;

        assert player != null;
        Vec3 entityPos = entity.getPosition(0);
        Vec3 playerPos = player.getPosition(0);

        double dist1 = playerPos.distanceTo(entityPos);

        playerPos = playerPos.add(player.getLookAngle());

        double dist2 = playerPos.distanceTo(entityPos);

        if(dist1 - dist2 < 0.5 || player.onGround())
            return;

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource buffer = event.getMultiBufferSource();

        // Push the current matrix stack
        poseStack.pushPose();
        poseStack.translate(0.0D, entity.getBbHeight() - 0.5D, 0.0D);

        controlOrientation(poseStack, entity);
        // Render the custom model
        renderCustomModel(poseStack, buffer, entity, event.getPartialTick(), event.getPackedLight());

        poseStack.popPose();
    }

    private static void renderCustomModel(PoseStack poseStack, MultiBufferSource buffer, LivingEntity entity, float partialTick, int packedLight) {
        Spindash<LivingEntity> model = new Spindash<>(Minecraft.getInstance().getEntityModels().bakeLayer(Spindash.LAYER_LOCATION));

        // Render the custom model
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(new ResourceLocation(BeyondTheHorizon.MOD_ID, "textures/entity/spindash.png")));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void controlOrientation(PoseStack poseStack, LivingEntity entity) {
        // Example: Rotate the model based on the entity's yaw and pitch
        float entityYaw = (entity.getYRot() > 180.0)?entity.getYRot()-180.0f:entity.getYRot()+180.0f;

        // Apply rotation to the model
        poseStack.mulPose(Axis.YP.rotationDegrees(-entityYaw));
    }
}
