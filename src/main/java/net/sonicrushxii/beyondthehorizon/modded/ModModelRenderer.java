package net.sonicrushxii.beyondthehorizon.modded;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.capabilities.all.FormProperties;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.event_handler.PlayerTickHandler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

public class ModModelRenderer {
    public record Texture(String textureLocation, byte frameNo) {
    }

    //Used for Animations
    private static String getTextureLocation(Texture[] textures, byte animationLength) {
        if (animationLength == 0 || textures.length == 1)
            return textures[0].textureLocation;

        if (animationLength > 20 || 20 % animationLength != 0)
            throw new RuntimeException("Incorrect Animation Length, Must be a divisor of 20");

        byte frame = (byte) (PlayerTickHandler.clientTickCounter % animationLength);
        while (true) {
            for (Texture texture : textures)
                if (frame == texture.frameNo)
                    return texture.textureLocation;
            frame = (byte) ((frame < 0) ? 19 : frame - 1);
        }
    }

    public static void renderModel(Class<? extends EntityModel> modelClass, RenderLivingEvent<?, ?> event, PoseStack poseStack, Consumer<ModelPart> customTransform) {
        MultiBufferSource buffer = event.getMultiBufferSource();
        LivingEntity entity = event.getEntity();
        int packedLight = event.getPackedLight();

        // Render the custom model
        try {
            //Get Layer Location
            Field layerField = modelClass.getDeclaredField("LAYER_LOCATION");
            ModelLayerLocation layerLocation = (ModelLayerLocation) layerField.get(null);

            //Get Texture Location
            Field textureField = modelClass.getDeclaredField("TEXTURE_LOCATIONS");
            Texture[] textures = (Texture[]) textureField.get(null);
            Field animLengthField = modelClass.getDeclaredField("ANIMATION_LENGTH");
            byte animationLength = (byte) animLengthField.get(null);

            //Instantiate Model
            EntityModelSet entityModelSet = Minecraft.getInstance().getEntityModels();
            ModelPart modelPart = entityModelSet.bakeLayer(layerLocation);

            //Perform Custom Transform
            if (customTransform != null) customTransform.accept(modelPart);

            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(new ResourceLocation(BeyondTheHorizon.MOD_ID, getTextureLocation(textures, animationLength))));
            EntityModel model = modelClass.getConstructor(ModelPart.class).newInstance(modelPart);
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        } catch (NullPointerException | ClassCastException | NoSuchMethodError | NoSuchFieldException |
                 NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException ignored) {
        }
    }

    public static void renderPlayerModel(Class<? extends EntityModel> modelClass, RenderLivingEvent<?, ?> event, PoseStack poseStack, FormProperties formProperties, Consumer<ModelPart> customTransform) {
        MultiBufferSource buffer = event.getMultiBufferSource();
        LocalPlayer player = (LocalPlayer) event.getEntity();
        int packedLight = event.getPackedLight();

        // Render the custom model
        try {
            //Get Layer Location
            Field layerField = modelClass.getDeclaredField("LAYER_LOCATION");
            ModelLayerLocation layerLocation = (ModelLayerLocation) layerField.get(null);

            //Find Model
            EntityModelSet entityModelSet = Minecraft.getInstance().getEntityModels();
            ModelPart modelPart = entityModelSet.bakeLayer(layerLocation);

            //Perform Custom Transform
            if (customTransform != null) customTransform.accept(modelPart);

            //Texture Path
            StringBuilder texturePath = new StringBuilder();

            //Handle Baseform Rendering
            if (formProperties instanceof BaseformProperties baseformProperties)
            {
                texturePath.append("baseform/");

                //Find Texture based on Condition
                if (baseformProperties.lightSpeedState == 2) texturePath.append("lightspeed_skin");
                else if (baseformProperties.powerBoost) texturePath.append("powerboost_skin");
                else texturePath.append("base_skin");
            }

            //Handle Superform Rendering

            //Handle Starfall Rendering

            //Handle HyperForm Rendering


            VertexConsumer vertexConsumer;
            //Handle Default Player Rendering
            if (texturePath.isEmpty()) {
                vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(
                        player.getSkin().texture()
                ));
            }
            //Handle Custom Rendering
            else {
                vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(
                        new ResourceLocation(BeyondTheHorizon.MOD_ID, String.format("textures/custom_model/%s.png", texturePath))
                ));

            }

            EntityModel model = modelClass.getConstructor(ModelPart.class).newInstance(modelPart);
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(player, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        } catch (NullPointerException | ClassCastException | NoSuchMethodError | NoSuchFieldException |
                 NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException ignored) {
        }
    }

    public static void renderSonicPeelout(Class<? extends EntityModel> modelClass, RenderLivingEvent<?, ?> event, PoseStack poseStack, FormProperties formProperties, Consumer<ModelPart> customTransform) {
        MultiBufferSource buffer = event.getMultiBufferSource();
        LocalPlayer player = (LocalPlayer) event.getEntity();
        int packedLight = event.getPackedLight();

        // Render the custom model
        try {
            //Get Layer Location
            Field layerField = modelClass.getDeclaredField("LAYER_LOCATION");
            ModelLayerLocation layerLocation = (ModelLayerLocation) layerField.get(null);

            //Find Model
            EntityModelSet entityModelSet = Minecraft.getInstance().getEntityModels();
            ModelPart modelPart = entityModelSet.bakeLayer(layerLocation);

            //Perform Custom Transform
            if (customTransform != null) customTransform.accept(modelPart);

            //Texture Path
            StringBuilder texturePath = new StringBuilder();

            //Handle Baseform Rendering
            if (formProperties instanceof BaseformProperties baseformProperties)
            {
                texturePath.append("baseform/");

                //Find Texture based on Condition
                if (baseformProperties.lightSpeedState == 2) texturePath.append("lightspeed_skin_peelout");
                else if (baseformProperties.powerBoost) texturePath.append("powerboost_skin_peelout");
                else texturePath.append("base_skin_peelout");
            }

            //Handle Superform Rendering

            //Handle Starfall Rendering

            //Handle HyperForm Rendering


            VertexConsumer vertexConsumer;
            //Handle Default Player Rendering
            if (texturePath.isEmpty()) {
                vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(
                        player.getSkin().texture()
                ));
            }
            //Handle Custom Rendering
            else {
                vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(
                        new ResourceLocation(BeyondTheHorizon.MOD_ID, String.format("textures/custom_model/%s.png", texturePath))
                ));

            }

            EntityModel model = modelClass.getConstructor(ModelPart.class).newInstance(modelPart);
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(player, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        } catch (NullPointerException | ClassCastException | NoSuchMethodError | NoSuchFieldException |
                 NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException ignored) {
        }
    }
}