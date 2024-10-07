package net.sonicrushxii.beyondthehorizon.client;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.models.Spindash;

@Mod.EventBusSubscriber(modid = BeyondTheHorizon.MOD_ID)
public class RenderCircleHandler {

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Post<?, ?> event)
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

        // Translate to the entity's position
        poseStack.translate(0.0D, entity.getBbHeight() - 0.5D, 0.0D);

        // Adjust the orientation
        controlOrientation(poseStack, entity);

        // Render the custom model
        renderCustomModel(poseStack, buffer, entity, event.getPartialTick(), event.getPackedLight());

        // Pop the matrix stack to restore previous state
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
        float entityYaw = entity.getYRot();
        float entityPitch = entity.getXRot();

        // Apply rotation to the model
        poseStack.mulPose(Axis.YP.rotationDegrees(-entityYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(entityPitch));

        // You can add additional rotations or translations here if needed
    }
}
