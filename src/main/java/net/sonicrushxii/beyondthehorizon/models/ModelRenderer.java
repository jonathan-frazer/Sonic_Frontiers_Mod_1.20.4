package net.sonicrushxii.beyondthehorizon.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.event_handler.PlayerTickHandler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ModelRenderer {
    public record Texture(String textureLocation, byte frameNo) {}

    //Used for Animations
    private static String getTextureLocation(Texture[] textures, byte animationLength)
    {
        assert animationLength <= 20;
        if(animationLength == 0 || textures.length == 1)
            return textures[0].textureLocation;

        byte frame = (byte)(PlayerTickHandler.clientTickCounter%animationLength);
        while(true)
        {
            for(int i=0;i<textures.length;++i)
                if(frame == textures[i].frameNo)
                    return textures[i].textureLocation;
            frame = (byte) ((frame < 0)?19:frame-1);
        }
    }

    public static void renderModel(Class<? extends EntityModel> modelClass, RenderLivingEvent<?, ?> event, PoseStack poseStack)
    {
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
            Texture[] textures = (Texture[])textureField.get(null);
            Field animLengthField = modelClass.getDeclaredField("ANIMATION_LENGTH");
            byte animationLength = (byte)animLengthField.get(null);

            EntityModelSet entityModelSet = Minecraft.getInstance().getEntityModels();
            ModelPart modelPart = entityModelSet.bakeLayer(layerLocation);

            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(new ResourceLocation(BeyondTheHorizon.MOD_ID, getTextureLocation(textures,animationLength))));
            EntityModel model = modelClass.getConstructor(ModelPart.class).newInstance(modelPart);
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }

        catch (NullPointerException | ClassCastException | NoSuchMethodError | NoSuchFieldException |
               NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored) {}
    }
}
