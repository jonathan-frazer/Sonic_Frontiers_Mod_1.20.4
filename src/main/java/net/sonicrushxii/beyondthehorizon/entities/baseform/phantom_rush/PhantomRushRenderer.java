package net.sonicrushxii.beyondthehorizon.entities.baseform.phantom_rush;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.models.*;
import org.jetbrains.annotations.NotNull;

public class PhantomRushRenderer extends EntityRenderer<PhantomRushEntity> {
    private final EntityModel<PhantomRushEntity> model1;
    private final EntityModel<PhantomRushEntity> model2;
    private final EntityModel<PhantomRushEntity> model3;
    private final EntityModel<PhantomRushEntity> model4;
    private final EntityModel<PhantomRushEntity> model5;

    public PhantomRushRenderer(EntityRendererProvider.Context context) {
        super(context);

        //Model Part
        ModelPart modelPart1 = context.bakeLayer(PhantomRushModel_1.LAYER_LOCATION);
        ModelPart modelPart2 = context.bakeLayer(PhantomRushModel_2.LAYER_LOCATION);
        ModelPart modelPart3 = context.bakeLayer(PhantomRushModel_3.LAYER_LOCATION);
        ModelPart modelPart4 = context.bakeLayer(PhantomRushModel_4.LAYER_LOCATION);
        ModelPart modelPart5 = context.bakeLayer(PhantomRushModel_5.LAYER_LOCATION);

        //Generate new Model Part
        this.model1 = new PhantomRushModel_1<>(modelPart1);
        this.model2 = new PhantomRushModel_2<>(modelPart2);
        this.model3 = new PhantomRushModel_3<>(modelPart3);
        this.model4 = new PhantomRushModel_4<>(modelPart4);
        this.model5 = new PhantomRushModel_5<>(modelPart5);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull PhantomRushEntity entity)
    {
        if(entity.getDuration() > 1) {
            return switch (entity.getPoseType() / 10) {
                case 0 -> new ResourceLocation(BeyondTheHorizon.MOD_ID, "textures/custom_model/baseform/base_skin.png");
                case 1 ->  new ResourceLocation(BeyondTheHorizon.MOD_ID, "textures/custom_model/baseform/powerboost_skin.png");
                case 2 ->  new ResourceLocation(BeyondTheHorizon.MOD_ID, "textures/custom_model/baseform/lightspeed_skin.png");
                default -> new ResourceLocation(BeyondTheHorizon.MOD_ID, "textures/custom_model/baseform/mirage_skin.png");
            };
        }
        else return new ResourceLocation(BeyondTheHorizon.MOD_ID, "textures/custom_model/baseform/mirage_skin.png");
    }

    @Override
    public void render(@NotNull PhantomRushEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        // Translate and rotate the pose stack as needed
        poseStack.pushPose();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity)));

        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot()));
        poseStack.translate(0D,-1.5D,0D);

        switch (entity.getPoseType()%10)
        {
            case 0: this.model1.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F); break;
            case 1: this.model2.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F); break;
            case 2: this.model3.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F); break;
            case 3: this.model4.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F); break;
            case 4: this.model5.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F); break;
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }
}