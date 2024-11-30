package net.sonicrushxii.beyondthehorizon.capabilities.baseform.models;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.modded.ModModelRenderer;

public class SonicBoomModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(BeyondTheHorizon.MOD_ID, "baseform/sonic_boom"), "main");
	public static final ModModelRenderer.Texture[] TEXTURE_LOCATIONS = {
			new ModModelRenderer.Texture("textures/custom_model/baseform/sonic_boom.png",(byte)0)
	};
	public static final byte ANIMATION_LENGTH = 1;

	private final ModelPart boom_slash;

	public SonicBoomModel(ModelPart root) {
		this.boom_slash = root.getChild("boom_slash");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition boom_slash = partdefinition.addOrReplaceChild("boom_slash", CubeListBuilder.create().texOffs(8, 2).addBox(-1.0F, -2.0F, -3.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.0F, -1.0F, -3.25F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(8, 0).addBox(-1.0F, 2.0F, -3.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition cube_r1 = boom_slash.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(8, 4).addBox(-0.5F, -0.9F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, -1.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r2 = boom_slash.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 4).addBox(-1.0F, -0.5F, -0.7F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -2.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r3 = boom_slash.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(8, 7).addBox(-0.5F, -1.2F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, -1.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r4 = boom_slash.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(4, 10).addBox(0.0F, -1.5F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 2.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r5 = boom_slash.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 7).addBox(-1.0F, 0.2F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, -2.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r6 = boom_slash.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, -1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 3.0F, 0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		boom_slash.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}