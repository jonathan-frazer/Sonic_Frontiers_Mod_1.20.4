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

public class CrossSlashModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(BeyondTheHorizon.MOD_ID, "baseform_cross_slash"), "main");
	public static final ModModelRenderer.Texture[] TEXTURE_LOCATIONS = {
			new ModModelRenderer.Texture("textures/custom_model/baseform/cross_slash.png",(byte)0)
	};
	public static final byte ANIMATION_LENGTH = 1;

	private final ModelPart cross_slash;
	private final ModelPart cross_slash_1;
	private final ModelPart cross_slash_2;

	public CrossSlashModel(ModelPart root) {
		this.cross_slash = root.getChild("cross_slash");
		this.cross_slash_1 = this.cross_slash.getChild("cross_slash_1");
		this.cross_slash_2 = this.cross_slash.getChild("cross_slash_2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition cross_slash = partdefinition.addOrReplaceChild("cross_slash", CubeListBuilder.create(), PartPose.offset(0.1F, 16.25F, 1.0F));

		PartDefinition cross_slash_1 = cross_slash.addOrReplaceChild("cross_slash_1", CubeListBuilder.create().texOffs(8, 6).addBox(-2.0F, -2.5F, -3.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(8, 8).addBox(-2.0F, 1.5F, -3.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 10).addBox(-2.0F, 2.0F, -3.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4F, -0.25F, -1.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r1 = cross_slash_1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -0.9F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 5.0F, -1.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r2 = cross_slash_1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 7).addBox(-1.5F, -0.5F, -0.7F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 3.0F, -2.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r3 = cross_slash_1.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(8, 12).addBox(-1.0F, -1.7F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -4.0F, -1.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r4 = cross_slash_1.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(16, 0).addBox(-0.5F, -2.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -2.0F, 2.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r5 = cross_slash_1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 4).addBox(-1.5F, -0.3F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -4.0F, -2.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r6 = cross_slash_1.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(12, 15).addBox(-0.5F, -1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 4.0F, 3.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cross_slash_2 = cross_slash.addOrReplaceChild("cross_slash_2", CubeListBuilder.create().texOffs(8, 10).addBox(-1.5F, -2.0F, -3.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.5F, -1.0F, -3.25F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 12).addBox(-1.5F, 2.0F, -3.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4F, -0.25F, -1.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r7 = cross_slash_2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(6, 15).addBox(-0.5F, -0.9F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 5.0F, -1.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r8 = cross_slash_2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(8, 3).addBox(-1.0F, -0.5F, -0.7F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 3.0F, -2.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r9 = cross_slash_2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(14, 12).addBox(-0.5F, -1.2F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -4.0F, -1.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r10 = cross_slash_2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(16, 4).addBox(0.0F, -1.5F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -2.0F, 2.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r11 = cross_slash_2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, 0.2F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -4.0F, -2.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r12 = cross_slash_2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(16, 2).addBox(0.0F, -1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 4.0F, 3.0F, 0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		cross_slash.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}