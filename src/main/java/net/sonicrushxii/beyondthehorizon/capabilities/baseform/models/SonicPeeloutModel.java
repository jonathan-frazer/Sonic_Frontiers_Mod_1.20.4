package net.sonicrushxii.beyondthehorizon.capabilities.baseform.models;

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

public class SonicPeeloutModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(BeyondTheHorizon.MOD_ID, "sonicpeeloutmodel"), "main");
	private final ModelPart Head;
	private final ModelPart Body;
	private final ModelPart RightArm;
	private final ModelPart LeftArm;
	private final ModelPart Figure8;
	private final ModelPart hexadecagon_1;
	private final ModelPart hexadecagon_2;

	public SonicPeeloutModel(ModelPart root) {
		this.Head = root.getChild("Head");
		this.Body = root.getChild("Body");
		this.RightArm = root.getChild("RightArm");
		this.LeftArm = root.getChild("LeftArm");
		this.Figure8 = root.getChild("Figure8");
		this.hexadecagon_1 = this.Figure8.getChild("hexadecagon_1");
		this.hexadecagon_2 = this.Figure8.getChild("hexadecagon_2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 5.0F, -9.0F, 0.6283F, 0.0F, 0.0F));

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 2.0F, -3.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 3.0F, -9.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-5.0F, 6.0F, -7.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(5.0F, 6.0F, -7.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition Figure8 = partdefinition.addOrReplaceChild("Figure8", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -1.0F));

		PartDefinition hexadecagon_1 = Figure8.addOrReplaceChild("hexadecagon_1", CubeListBuilder.create().texOffs(0, 59).addBox(-7.0F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, -1.2929F, 4.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, 4.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, -6.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -6.0F, 6.0F));

		PartDefinition hexadecagon_r1 = hexadecagon_1.addOrReplaceChild("hexadecagon_r1", CubeListBuilder.create().texOffs(0, 59).addBox(-7.0F, -6.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, 4.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, -1.2929F, 4.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r2 = hexadecagon_1.addOrReplaceChild("hexadecagon_r2", CubeListBuilder.create().texOffs(0, 59).addBox(-7.0F, -6.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, 4.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, -1.2929F, 4.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r3 = hexadecagon_1.addOrReplaceChild("hexadecagon_r3", CubeListBuilder.create().texOffs(0, 59).addBox(-7.0F, -1.2929F, 4.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r4 = hexadecagon_1.addOrReplaceChild("hexadecagon_r4", CubeListBuilder.create().texOffs(0, 59).addBox(-7.0F, -1.2929F, 4.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_2 = Figure8.addOrReplaceChild("hexadecagon_2", CubeListBuilder.create().texOffs(0, 59).addBox(-7.0F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, 4.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, -6.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -6.0F, -5.0F));

		PartDefinition hexadecagon_r5 = hexadecagon_2.addOrReplaceChild("hexadecagon_r5", CubeListBuilder.create().texOffs(0, 59).addBox(-7.0F, -6.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, 4.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, -1.2929F, 4.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r6 = hexadecagon_2.addOrReplaceChild("hexadecagon_r6", CubeListBuilder.create().texOffs(0, 59).addBox(-7.0F, -6.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, 4.5F, -1.2929F, 8.0F, 2.0F, 2.5859F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, -1.2929F, 4.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r7 = hexadecagon_2.addOrReplaceChild("hexadecagon_r7", CubeListBuilder.create().texOffs(0, 59).addBox(-7.0F, -1.2929F, 4.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r8 = hexadecagon_2.addOrReplaceChild("hexadecagon_r8", CubeListBuilder.create().texOffs(0, 59).addBox(-7.0F, -1.2929F, 4.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-7.0F, -1.2929F, -6.5F, 8.0F, 2.5859F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

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
	}
}