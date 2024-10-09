package net.sonicrushxii.beyondthehorizon.models.baseform;// Made with Blockbench 4.11.1
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
import net.sonicrushxii.beyondthehorizon.models.ModelRenderer;

public class HomingAttack<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(BeyondTheHorizon.MOD_ID, "homingattack"), "main");
	public static final ModelRenderer.Texture[] TEXTURE_LOCATIONS = {
					new ModelRenderer.Texture("textures/custom_model/homingattack.png",(byte)0)
			};
	public static final byte ANIMATION_LENGTH = 1;

	private final ModelPart HomingReticle;
	private final ModelPart hexadecagon_center;
	private final ModelPart hexadecagon_inner;
	private final ModelPart hexadecagon_outer;

	public HomingAttack(ModelPart root) {
		this.HomingReticle = root.getChild("HomingReticle");
		this.hexadecagon_center = this.HomingReticle.getChild("hexadecagon_center");
		this.hexadecagon_inner = this.HomingReticle.getChild("hexadecagon_inner");
		this.hexadecagon_outer = this.HomingReticle.getChild("hexadecagon_outer");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition HomingReticle = partdefinition.addOrReplaceChild("HomingReticle", CubeListBuilder.create(), PartPose.offset(1.0F, 15.0F, 0.0F));

		PartDefinition hexadecagon_center = HomingReticle.addOrReplaceChild("hexadecagon_center", CubeListBuilder.create().texOffs(8, 1).addBox(-9.0F, -8.3978F, 6.0F, 2.0F, 0.7956F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(13, 22).addBox(-9.0F, -10.0F, 7.6022F, 2.0F, 4.0F, 0.7956F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 8.0F, -8.0F));

		PartDefinition hexadecagon_r1 = hexadecagon_center.addOrReplaceChild("hexadecagon_r1", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -2.0F, -0.3978F, 2.0F, 4.0F, 0.7956F, new CubeDeformation(0.0F))
				.texOffs(0, 5).addBox(-1.0F, -0.3978F, -2.0F, 2.0F, 0.7956F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -8.0F, 8.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r2 = hexadecagon_center.addOrReplaceChild("hexadecagon_r2", CubeListBuilder.create().texOffs(19, 22).addBox(-1.0F, -2.0F, -0.3978F, 2.0F, 4.0F, 0.7956F, new CubeDeformation(0.0F))
				.texOffs(8, 6).addBox(-1.0F, -0.3978F, -2.0F, 2.0F, 0.7956F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -8.0F, 8.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r3 = hexadecagon_center.addOrReplaceChild("hexadecagon_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.3978F, -2.0F, 2.0F, 0.7956F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -8.0F, 8.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r4 = hexadecagon_center.addOrReplaceChild("hexadecagon_r4", CubeListBuilder.create().texOffs(0, 10).addBox(-1.0F, -0.3978F, -2.0F, 2.0F, 0.7956F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -8.0F, 8.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_inner = HomingReticle.addOrReplaceChild("hexadecagon_inner", CubeListBuilder.create().texOffs(0, 31).addBox(-9.0F, -8.9946F, 3.0F, 2.0F, 1.9891F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(28, 7).addBox(-9.0F, -8.9946F, 12.0F, 2.0F, 1.9891F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(20, 15).addBox(-9.0F, -4.0F, 7.0054F, 2.0F, 1.0F, 1.9891F, new CubeDeformation(0.0F))
				.texOffs(0, 19).addBox(-9.0F, -13.0F, 7.0054F, 2.0F, 1.0F, 1.9891F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 8.0F, -8.0F));

		PartDefinition hexadecagon_r5 = hexadecagon_inner.addOrReplaceChild("hexadecagon_r5", CubeListBuilder.create().texOffs(14, 16).addBox(-1.0F, -5.0F, -0.9946F, 2.0F, 1.0F, 1.9891F, new CubeDeformation(0.0F))
				.texOffs(6, 20).addBox(-1.0F, 4.0F, -0.9946F, 2.0F, 1.0F, 1.9891F, new CubeDeformation(0.0F))
				.texOffs(27, 17).addBox(-1.0F, -0.9946F, 4.0F, 2.0F, 1.9891F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(30, 28).addBox(-1.0F, -0.9946F, -5.0F, 2.0F, 1.9891F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -8.0F, 8.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r6 = hexadecagon_inner.addOrReplaceChild("hexadecagon_r6", CubeListBuilder.create().texOffs(15, 19).addBox(-1.0F, -5.0F, -0.9946F, 2.0F, 1.0F, 1.9891F, new CubeDeformation(0.0F))
				.texOffs(21, 18).addBox(-1.0F, 4.0F, -0.9946F, 2.0F, 1.0F, 1.9891F, new CubeDeformation(0.0F))
				.texOffs(29, 4).addBox(-1.0F, -0.9946F, 4.0F, 2.0F, 1.9891F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(6, 31).addBox(-1.0F, -0.9946F, -5.0F, 2.0F, 1.9891F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -8.0F, 8.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r7 = hexadecagon_inner.addOrReplaceChild("hexadecagon_r7", CubeListBuilder.create().texOffs(26, 14).addBox(-1.0F, -0.9946F, 4.0F, 2.0F, 1.9891F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(24, 29).addBox(-1.0F, -0.9946F, -5.0F, 2.0F, 1.9891F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -8.0F, 8.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r8 = hexadecagon_inner.addOrReplaceChild("hexadecagon_r8", CubeListBuilder.create().texOffs(29, 10).addBox(-1.0F, -0.9946F, 4.0F, 2.0F, 1.9891F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(12, 31).addBox(-1.0F, -0.9946F, -5.0F, 2.0F, 1.9891F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -8.0F, 8.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_outer = HomingReticle.addOrReplaceChild("hexadecagon_outer", CubeListBuilder.create().texOffs(6, 27).addBox(-2.0F, -9.5913F, -8.0F, 2.0F, 3.1826F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(23, 10).addBox(-2.0F, -9.5913F, 7.0F, 2.0F, 3.1826F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(7, 16).addBox(-2.0F, -1.0F, -1.5913F, 2.0F, 1.0F, 3.1826F, new CubeDeformation(0.0F))
				.texOffs(0, 15).addBox(-2.0F, -16.0F, -1.5913F, 2.0F, 1.0F, 3.1826F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

		PartDefinition hexadecagon_r9 = hexadecagon_outer.addOrReplaceChild("hexadecagon_r9", CubeListBuilder.create().texOffs(9, 12).addBox(-1.0F, -8.0F, -1.5913F, 2.0F, 1.0F, 3.1826F, new CubeDeformation(0.0F))
				.texOffs(16, 6).addBox(-1.0F, 7.0F, -1.5913F, 2.0F, 1.0F, 3.1826F, new CubeDeformation(0.0F))
				.texOffs(6, 23).addBox(-1.0F, -1.5913F, 7.0F, 2.0F, 3.1826F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 27).addBox(-1.0F, -1.5913F, -8.0F, 2.0F, 3.1826F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -8.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r10 = hexadecagon_outer.addOrReplaceChild("hexadecagon_r10", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, -8.0F, -1.5913F, 2.0F, 1.0F, 3.1826F, new CubeDeformation(0.0F))
				.texOffs(16, 11).addBox(-1.0F, 7.0F, -1.5913F, 2.0F, 1.0F, 3.1826F, new CubeDeformation(0.0F))
				.texOffs(25, 21).addBox(-1.0F, -1.5913F, 7.0F, 2.0F, 3.1826F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(12, 27).addBox(-1.0F, -1.5913F, -8.0F, 2.0F, 3.1826F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -8.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r11 = hexadecagon_outer.addOrReplaceChild("hexadecagon_r11", CubeListBuilder.create().texOffs(23, 4).addBox(-1.0F, -1.5913F, 7.0F, 2.0F, 3.1826F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(26, 0).addBox(-1.0F, -1.5913F, -8.0F, 2.0F, 3.1826F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -8.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r12 = hexadecagon_outer.addOrReplaceChild("hexadecagon_r12", CubeListBuilder.create().texOffs(25, 25).addBox(-1.0F, -1.5913F, 7.0F, 2.0F, 3.1826F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(18, 27).addBox(-1.0F, -1.5913F, -8.0F, 2.0F, 3.1826F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -8.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		HomingReticle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}