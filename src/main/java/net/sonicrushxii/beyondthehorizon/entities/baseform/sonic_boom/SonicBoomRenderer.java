package net.sonicrushxii.beyondthehorizon.entities.baseform.sonic_boom;

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
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.models.SonicBoomModel;
import org.jetbrains.annotations.NotNull;

public class SonicBoomRenderer extends EntityRenderer<SonicBoomProjectile> {
    private final EntityModel<SonicBoomProjectile> model;

    public SonicBoomRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelPart = context.bakeLayer(new ModelLayerLocation(new ResourceLocation(BeyondTheHorizon.MOD_ID, "baseform/sonic_boom"), "main"));
        this.model = new SonicBoomModel<>(modelPart);
    }
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SonicBoomProjectile entity) {
        return new ResourceLocation(BeyondTheHorizon.MOD_ID,SonicBoomModel.TEXTURE_LOCATIONS[0].textureLocation());
    }

    @Override
    public void render(@NotNull SonicBoomProjectile entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        // Translate and rotate the pose stack as needed
        poseStack.pushPose();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity)));

        poseStack.scale(2.5F,2.5F,2.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F-entity.getXRot()));
        poseStack.translate(0D,-1.0D,0D);

        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }
}