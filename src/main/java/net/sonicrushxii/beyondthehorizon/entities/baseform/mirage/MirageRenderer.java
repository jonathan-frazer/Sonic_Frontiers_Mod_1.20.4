package net.sonicrushxii.beyondthehorizon.entities.baseform.mirage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.models.MirageModel;
import org.jetbrains.annotations.NotNull;

public class MirageRenderer extends EntityRenderer<MirageEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(BeyondTheHorizon.MOD_ID, "textures/custom_model/baseform/mirage_skin.png");
    private final EntityModel<MirageEntity> model;

    public MirageRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelPart = context.bakeLayer(new ModelLayerLocation(new ResourceLocation(BeyondTheHorizon.MOD_ID, "baseform/mirage"), "main"));
        this.model = new MirageModel<>(modelPart);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(MirageEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(MirageEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        // Translate and rotate the pose stack as needed
        poseStack.pushPose();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity)));

        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot()));
        poseStack.translate(0D,-1.5D,0D);

        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }
}