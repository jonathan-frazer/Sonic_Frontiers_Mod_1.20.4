// Made with Blockbench 4.11.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package net.sonicrushxii.beyondthehorizon.capabilities.baseform.models;// Made with Blockbench 4.11.1
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

public class HummingTop<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(BeyondTheHorizon.MOD_ID, "baseform/hummingtop"), "main");
	public static final ModModelRenderer.Texture[] TEXTURE_LOCATIONS = {
			new ModModelRenderer.Texture("textures/custom_model/baseform/humming_top.png",(byte)0)
	};

	public static final byte ANIMATION_LENGTH = 1;
	private final ModelPart HummingTop;
	private final ModelPart hexadecagon_3;
	private final ModelPart hexadecagon_2;
	private final ModelPart hexadecagon_1;

	public HummingTop(ModelPart root) {
		this.HummingTop = root.getChild("HummingTop");
		this.hexadecagon_3 = this.HummingTop.getChild("hexadecagon_3");
		this.hexadecagon_2 = this.HummingTop.getChild("hexadecagon_2");
		this.hexadecagon_1 = this.HummingTop.getChild("hexadecagon_1");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition HummingTop = partdefinition.addOrReplaceChild("HummingTop", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition hexadecagon_3 = HummingTop.addOrReplaceChild("hexadecagon_3", CubeListBuilder.create().texOffs(10, 33).addBox(-1.3924F, -0.5F, -7.0F, 2.7848F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(30, 37).addBox(-1.3924F, -0.5F, 5.0F, 2.7848F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(10, 25).addBox(5.0F, -0.5F, -1.3924F, 2.0F, 1.0F, 2.7848F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 0.0F));

		PartDefinition hexadecagon_r1 = hexadecagon_3.addOrReplaceChild("hexadecagon_r1", CubeListBuilder.create().texOffs(-2, 27).addBox(-7.0F, -0.5F, -2.3924F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 25).addBox(5.0F, -0.5F, -1.3924F, 2.0F, 1.0F, 2.7848F, new CubeDeformation(0.0F))
		.texOffs(10, 36).addBox(-1.3924F, -0.5F, 5.0F, 2.7848F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition hexadecagon_r2 = hexadecagon_3.addOrReplaceChild("hexadecagon_r2", CubeListBuilder.create().texOffs(20, 28).addBox(5.0F, -0.5F, -1.3924F, 2.0F, 1.0F, 2.7848F, new CubeDeformation(0.0F))
		.texOffs(20, 38).addBox(-1.3924F, -0.5F, 5.0F, 2.7848F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(30, 34).addBox(-1.3924F, -0.5F, -7.0F, 2.7848F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition hexadecagon_r3 = hexadecagon_3.addOrReplaceChild("hexadecagon_r3", CubeListBuilder.create().texOffs(0, 39).addBox(-1.3924F, -0.5F, 5.0F, 2.7848F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 35).addBox(-1.3924F, -0.5F, -7.0F, 2.7848F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hexadecagon_r4 = hexadecagon_3.addOrReplaceChild("hexadecagon_r4", CubeListBuilder.create().texOffs(0, 36).addBox(-1.3924F, -0.5F, 5.0F, 2.7848F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition hexadecagon_r5 = hexadecagon_3.addOrReplaceChild("hexadecagon_r5", CubeListBuilder.create().texOffs(-2, 33).addBox(-2.6076F, -0.5F, -7.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3491F, 0.0F));

		PartDefinition hexadecagon_2 = HummingTop.addOrReplaceChild("hexadecagon_2", CubeListBuilder.create().texOffs(30, 10).addBox(-1.3924F, -0.5F, -7.0F, 2.7848F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(30, 25).addBox(-1.3924F, -0.5F, 5.0F, 2.7848F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 16).addBox(5.0F, -0.5F, -1.3924F, 2.0F, 1.0F, 2.7848F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

		PartDefinition hexadecagon_r6 = hexadecagon_2.addOrReplaceChild("hexadecagon_r6", CubeListBuilder.create().texOffs(20, 24).addBox(-7.0F, -0.5F, -1.3924F, 2.0F, 1.0F, 2.7848F, new CubeDeformation(0.0F))
		.texOffs(20, 20).addBox(5.0F, -0.5F, -1.3924F, 2.0F, 1.0F, 2.7848F, new CubeDeformation(0.0F))
		.texOffs(30, 28).addBox(-1.3924F, -0.5F, 5.0F, 2.7848F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(30, 13).addBox(-1.3924F, -0.5F, -7.0F, 2.7848F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition hexadecagon_r7 = hexadecagon_2.addOrReplaceChild("hexadecagon_r7", CubeListBuilder.create().texOffs(20, 12).addBox(5.0F, -0.5F, -1.3924F, 2.0F, 1.0F, 2.7848F, new CubeDeformation(0.0F))
		.texOffs(30, 22).addBox(-1.3924F, -0.5F, 5.0F, 2.7848F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(30, 7).addBox(-1.3924F, -0.5F, -7.0F, 2.7848F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition hexadecagon_r8 = hexadecagon_2.addOrReplaceChild("hexadecagon_r8", CubeListBuilder.create().texOffs(30, 19).addBox(-1.3924F, -0.5F, 5.0F, 2.7848F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(30, 4).addBox(-1.3924F, -0.5F, -7.0F, 2.7848F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition hexadecagon_r9 = hexadecagon_2.addOrReplaceChild("hexadecagon_r9", CubeListBuilder.create().texOffs(30, 16).addBox(-1.3924F, -0.5F, -7.0F, 2.7848F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hexadecagon_1 = HummingTop.addOrReplaceChild("hexadecagon_1", CubeListBuilder.create().texOffs(0, 15).addBox(-1.5913F, -8.5F, -8.0F, 3.1826F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 3).addBox(-1.5913F, -8.5F, 6.0F, 3.1826F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 4).addBox(6.0F, -8.5F, -1.5913F, 2.0F, 1.0F, 3.1826F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r10 = hexadecagon_1.addOrReplaceChild("hexadecagon_r10", CubeListBuilder.create().texOffs(0, 8).addBox(6.0F, -0.5F, -1.5913F, 2.0F, 1.0F, 3.1826F, new CubeDeformation(0.0F))
		.texOffs(20, 6).addBox(-1.5913F, -0.5F, 6.0F, 3.1826F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(10, 15).addBox(-1.5913F, -0.5F, -8.0F, 3.1826F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition hexadecagon_r11 = hexadecagon_1.addOrReplaceChild("hexadecagon_r11", CubeListBuilder.create().texOffs(0, 0).addBox(6.0F, -0.5F, -1.5913F, 2.0F, 1.0F, 3.1826F, new CubeDeformation(0.0F))
		.texOffs(20, 0).addBox(-1.5913F, -0.5F, 6.0F, 3.1826F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(10, 12).addBox(-1.5913F, -0.5F, -8.0F, 3.1826F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition hexadecagon_r12 = hexadecagon_1.addOrReplaceChild("hexadecagon_r12", CubeListBuilder.create().texOffs(20, 9).addBox(-1.5913F, -0.5F, 6.0F, 3.1826F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 18).addBox(-1.5913F, -0.5F, -8.0F, 3.1826F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hexadecagon_r13 = hexadecagon_1.addOrReplaceChild("hexadecagon_r13", CubeListBuilder.create().texOffs(10, 18).addBox(-1.5913F, -0.5F, 6.0F, 3.1826F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(-1, 12).addBox(-2.4087F, -0.5F, -8.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		HummingTop.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}