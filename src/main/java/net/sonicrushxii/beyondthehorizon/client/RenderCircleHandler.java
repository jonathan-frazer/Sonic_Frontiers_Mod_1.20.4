package net.sonicrushxii.beyondthehorizon.client;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;

@Mod.EventBusSubscriber(modid = BeyondTheHorizon.MOD_ID, value = Dist.CLIENT)
public class RenderCircleHandler {

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Post<?, ?> event)
    {
        Entity entity = event.getEntity();
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

        // Render the circle
        drawCircle(poseStack,buffer);

        // Pop the matrix stack to restore previous state
        poseStack.popPose();
    }

    private static void drawCircle(PoseStack poseStack, MultiBufferSource buffer) {
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.solid());

        // Set up the circle properties
        int segments = 36;
        float radius = 0.8F;
        float angleIncrement = (float) (2 * Math.PI / segments);

        // Draw the circle
        for (int i = 0; i < segments; i++)
        {
            float angle1 = i * angleIncrement;
            float angle2 = (i + 1) * angleIncrement;

            float x1 = radius * (float) Math.sin(angle1);
            float y1 = radius * (float) Math.cos(angle1);
            float z1 = 0.0f;

            float x2 = radius * (float) Math.sin(angle2);
            float y2 = radius * (float) Math.cos(angle2);
            float z2 = 0.0f;

            vertexConsumer.vertex(poseStack.last().pose(), x1, y1, z1)
                    .color(0, 255, 0, 255)
                    .normal(poseStack.last().normal(), 0.0F, 1.0F, 0.0F)
                    .endVertex();
            vertexConsumer.vertex(poseStack.last().pose(), x2, y2, z2)
                    .color(0, 255, 0, 255)
                    .normal(poseStack.last().normal(), 0.0F, 1.0F, 0.0F)
                    .endVertex();
        }
    }
}
