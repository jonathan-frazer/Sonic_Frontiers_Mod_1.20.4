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

public class SonicPeeloutModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "baseformmodel_powerboost"), "main");
	private final ModelPart Head;
	private final ModelPart Body;
	private final ModelPart RightArm;
	private final ModelPart LeftArm;
	private final ModelPart Figure8;
	private final ModelPart hexadecagon;
	private final ModelPart hexadecagon2;

	public SonicPeeloutModel(ModelPart root) {
		this.Head = root.getChild("Head");
		this.Body = root.getChild("Body");
		this.RightArm = root.getChild("RightArm");
		this.LeftArm = root.getChild("LeftArm");
		this.Figure8 = root.getChild("Figure8");
		this.hexadecagon = root.getChild("hexadecagon");
		this.hexadecagon2 = root.getChild("hexadecagon2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-5.0F, 2.0F, 0.0F, 1.3963F, 0.0F, 0.0F));

		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, 1.3963F, 0.0F, 0.0F));

		PartDefinition Figure8 = partdefinition.addOrReplaceChild("Figure8", CubeListBuilder.create(), PartPose.offset(-0.1F, 17.0F, 0.0F));

		PartDefinition hexadecagon = partdefinition.addOrReplaceChild("hexadecagon", CubeListBuilder.create().texOffs(0, 59).addBox(-6.9F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, 4.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, -6.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 17.0F, -6.0F));

		PartDefinition hexadecagon_r1 = hexadecagon.addOrReplaceChild("hexadecagon_r1", CubeListBuilder.create().texOffs(0, 59).addBox(-6.9F, -6.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, 4.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, -1.2929F, 4.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r2 = hexadecagon.addOrReplaceChild("hexadecagon_r2", CubeListBuilder.create().texOffs(0, 59).addBox(-6.9F, -6.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, 4.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r3 = hexadecagon.addOrReplaceChild("hexadecagon_r3", CubeListBuilder.create().texOffs(0, 59).addBox(-6.9F, -1.2929F, 4.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r4 = hexadecagon.addOrReplaceChild("hexadecagon_r4", CubeListBuilder.create().texOffs(0, 59).addBox(-6.9F, -1.2929F, 4.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 1.0F, -0.3491F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r5 = hexadecagon.addOrReplaceChild("hexadecagon_r5", CubeListBuilder.create().texOffs(0, 59).addBox(-6.9F, -2.7071F, 4.5F, 8.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 2.0F, 0.7418F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r6 = hexadecagon.addOrReplaceChild("hexadecagon_r6", CubeListBuilder.create().texOffs(0, 59).addBox(-6.9F, -1.2929F, 4.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon2 = partdefinition.addOrReplaceChild("hexadecagon2", CubeListBuilder.create().texOffs(0, 59).addBox(-6.9F, -1.2929F, 4.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, 4.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, -6.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 17.0F, 6.0F));

		PartDefinition hexadecagon_r7 = hexadecagon2.addOrReplaceChild("hexadecagon_r7", CubeListBuilder.create().texOffs(0, 59).addBox(-6.9F, -6.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, 4.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, -1.2929F, 4.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r8 = hexadecagon2.addOrReplaceChild("hexadecagon_r8", CubeListBuilder.create().texOffs(0, 59).addBox(-6.9F, -6.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, 4.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, -1.2929F, 4.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r9 = hexadecagon2.addOrReplaceChild("hexadecagon_r9", CubeListBuilder.create().texOffs(0, 59).addBox(-6.9F, -1.2929F, 4.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r10 = hexadecagon2.addOrReplaceChild("hexadecagon_r10", CubeListBuilder.create().texOffs(0, 59).addBox(-6.9F, -1.2929F, 4.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-6.9F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Figure8.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		hexadecagon.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		hexadecagon2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}