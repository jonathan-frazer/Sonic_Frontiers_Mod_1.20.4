package net.sonicrushxii.beyondthehorizon.entities.baseform.homing_shot;

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
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.models.Spinslash;
import org.jetbrains.annotations.NotNull;

public class HomingShotRenderer extends EntityRenderer<HomingShotProjectile> {
    private final EntityModel<HomingShotProjectile> model;

    public HomingShotRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelPart = context.bakeLayer(new ModelLayerLocation(new ResourceLocation(BeyondTheHorizon.MOD_ID, "baseform_spinslash"), "main"));
        this.model = new Spinslash<>(modelPart);
    }
    @Override
    public @NotNull ResourceLocation getTextureLocation(HomingShotProjectile entity) {
        int idx = (entity.getDuration()%(Spinslash.TEXTURE_LOCATIONS.length*2) );
        return new ResourceLocation(BeyondTheHorizon.MOD_ID,Spinslash.TEXTURE_LOCATIONS[idx/2].textureLocation());
    }

    @Override
    public void render(@NotNull HomingShotProjectile entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        // Translate and rotate the pose stack as needed
        poseStack.pushPose();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity)));

        poseStack.scale(0.75F,0.75F,0.75F);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot()));
        poseStack.translate(0D,-1.5D,0D);

        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }
}