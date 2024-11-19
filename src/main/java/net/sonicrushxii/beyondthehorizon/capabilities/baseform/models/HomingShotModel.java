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


public class HomingShotModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(BeyondTheHorizon.MOD_ID, "baseform_homing_shot"), "main");

	public static final ModModelRenderer.Texture[] TEXTURE_LOCATIONS = {
			new ModModelRenderer.Texture("textures/custom_model/baseform/homing_shot.png",(byte)0)
	};
	public static final byte ANIMATION_LENGTH = 1;

	private final ModelPart sphere3;
	private final ModelPart sphere2;
	private final ModelPart sphere1;
	private final ModelPart sphere0;
	private final ModelPart sphere4;
	private final ModelPart sphere_center;
	private final ModelPart sphere5;
	private final ModelPart sphere6;
	private final ModelPart sphere7;
	private final ModelPart sphere8;
	private final ModelPart sphere9;

	public HomingShotModel(ModelPart root) {
		this.sphere3 = root.getChild("sphere3");
		this.sphere2 = root.getChild("sphere2");
		this.sphere1 = root.getChild("sphere1");
		this.sphere0 = root.getChild("sphere0");
		this.sphere4 = root.getChild("sphere4");
		this.sphere_center = root.getChild("sphere_center");
		this.sphere5 = root.getChild("sphere5");
		this.sphere6 = root.getChild("sphere6");
		this.sphere7 = root.getChild("sphere7");
		this.sphere8 = root.getChild("sphere8");
		this.sphere9 = root.getChild("sphere9");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition sphere3 = partdefinition.addOrReplaceChild("sphere3", CubeListBuilder.create().texOffs(30, 38).addBox(-0.9946F, -8.5F, -5.0F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(18, 40).addBox(-0.9946F, -8.5F, 4.01F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(32, 18).addBox(4.01F, -8.5F, -0.9946F, 0.99F, 1.0F, 1.9891F, new CubeDeformation(0.0F))
		.texOffs(24, 33).addBox(-5.0F, -8.5F, -0.9946F, 0.99F, 1.0F, 1.9891F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 1.0F));

		PartDefinition hexadecagon_r1 = sphere3.addOrReplaceChild("hexadecagon_r1", CubeListBuilder.create().texOffs(30, 33).addBox(-5.0F, -0.5F, -0.9946F, 0.99F, 1.0F, 1.9891F, new CubeDeformation(0.0F))
		.texOffs(12, 33).addBox(4.01F, -0.5F, -0.9946F, 0.99F, 1.0F, 1.9891F, new CubeDeformation(0.0F))
		.texOffs(40, 20).addBox(-0.9946F, -0.5F, 4.01F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(36, 39).addBox(-0.9946F, -0.5F, -5.0F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition hexadecagon_r2 = sphere3.addOrReplaceChild("hexadecagon_r2", CubeListBuilder.create().texOffs(18, 33).addBox(-5.0F, -0.5F, -0.9946F, 0.99F, 1.0F, 1.9891F, new CubeDeformation(0.0F))
		.texOffs(32, 15).addBox(4.01F, -0.5F, -0.9946F, 0.99F, 1.0F, 1.9891F, new CubeDeformation(0.0F))
		.texOffs(12, 40).addBox(-0.9946F, -0.5F, 4.01F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(24, 38).addBox(-0.9946F, -0.5F, -5.0F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition hexadecagon_r3 = sphere3.addOrReplaceChild("hexadecagon_r3", CubeListBuilder.create().texOffs(40, 22).addBox(-0.9946F, -0.5F, 4.01F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(0, 40).addBox(-0.9946F, -0.5F, -5.0F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hexadecagon_r4 = sphere3.addOrReplaceChild("hexadecagon_r4", CubeListBuilder.create().texOffs(6, 40).addBox(-0.9946F, -0.5F, 4.01F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(38, 18).addBox(-0.9946F, -0.5F, -5.0F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition sphere2 = partdefinition.addOrReplaceChild("sphere2", CubeListBuilder.create().texOffs(6, 44).addBox(-0.7956F, -0.5F, -4.0F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(12, 45).addBox(-0.7956F, -0.5F, 3.01F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(42, 11).addBox(3.01F, -0.5F, -0.7956F, 0.99F, 1.0F, 1.5913F, new CubeDeformation(0.0F))
		.texOffs(24, 42).addBox(-4.0F, -0.5F, -0.7956F, 0.99F, 1.0F, 1.5913F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, 1.0F));

		PartDefinition hexadecagon_r5 = sphere2.addOrReplaceChild("hexadecagon_r5", CubeListBuilder.create().texOffs(42, 26).addBox(-4.0F, -0.5F, -0.7956F, 0.99F, 1.0F, 1.5913F, new CubeDeformation(0.0F))
		.texOffs(12, 42).addBox(3.01F, -0.5F, -0.7956F, 0.99F, 1.0F, 1.5913F, new CubeDeformation(0.0F))
		.texOffs(18, 45).addBox(-0.7956F, -0.5F, 3.01F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(44, 14).addBox(-0.7956F, -0.5F, -4.0F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition hexadecagon_r6 = sphere2.addOrReplaceChild("hexadecagon_r6", CubeListBuilder.create().texOffs(18, 42).addBox(-4.0F, -0.5F, -0.7956F, 0.99F, 1.0F, 1.5913F, new CubeDeformation(0.0F))
		.texOffs(42, 8).addBox(3.01F, -0.5F, -0.7956F, 0.99F, 1.0F, 1.5913F, new CubeDeformation(0.0F))
		.texOffs(42, 44).addBox(-0.7956F, -0.5F, 3.01F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(0, 44).addBox(-0.7956F, -0.5F, -4.0F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition hexadecagon_r7 = sphere2.addOrReplaceChild("hexadecagon_r7", CubeListBuilder.create().texOffs(24, 45).addBox(-0.7956F, -0.5F, 3.01F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(44, 16).addBox(-0.7956F, -0.5F, -4.0F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hexadecagon_r8 = sphere2.addOrReplaceChild("hexadecagon_r8", CubeListBuilder.create().texOffs(44, 18).addBox(-0.7956F, -0.5F, 3.01F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(36, 43).addBox(-0.7956F, -0.5F, -4.0F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition sphere1 = partdefinition.addOrReplaceChild("sphere1", CubeListBuilder.create().texOffs(48, 26).addBox(-0.5967F, -0.75F, -3.0F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(48, 36).addBox(-0.5967F, -0.75F, 2.01F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(28, 47).addBox(2.01F, -0.75F, -0.5967F, 0.99F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(0, 48).addBox(-3.0F, -0.75F, -0.5967F, 0.99F, 1.0F, 1.1935F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 1.0F));

		PartDefinition hexadecagon_r9 = sphere1.addOrReplaceChild("hexadecagon_r9", CubeListBuilder.create().texOffs(48, 0).addBox(-3.0F, -0.75F, -0.5967F, 0.99F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(32, 47).addBox(2.01F, -0.75F, -0.5967F, 0.99F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(48, 38).addBox(-0.5967F, -0.75F, 2.01F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(48, 28).addBox(-0.5967F, -0.75F, -3.0F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition hexadecagon_r10 = sphere1.addOrReplaceChild("hexadecagon_r10", CubeListBuilder.create().texOffs(36, 47).addBox(-3.0F, -0.75F, -0.5967F, 0.99F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(24, 47).addBox(2.01F, -0.75F, -0.5967F, 0.99F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(48, 34).addBox(-0.5967F, -0.75F, 2.01F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(48, 12).addBox(-0.5967F, -0.75F, -3.0F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition hexadecagon_r11 = sphere1.addOrReplaceChild("hexadecagon_r11", CubeListBuilder.create().texOffs(40, 48).addBox(-0.5967F, -0.75F, 2.01F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(48, 30).addBox(-0.5967F, -0.75F, -3.0F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hexadecagon_r12 = sphere1.addOrReplaceChild("hexadecagon_r12", CubeListBuilder.create().texOffs(48, 32).addBox(-0.5967F, -0.75F, 2.01F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(48, 10).addBox(-0.5967F, -0.75F, -3.0F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition sphere0 = partdefinition.addOrReplaceChild("sphere0", CubeListBuilder.create().texOffs(0, 10).addBox(-0.3978F, -1.0F, -2.0F, 0.7956F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(20, 7).addBox(-2.0F, -1.0F, -0.3978F, 4.0F, 1.0F, 0.7956F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 1.0F));

		PartDefinition hexadecagon_r13 = sphere0.addOrReplaceChild("hexadecagon_r13", CubeListBuilder.create().texOffs(20, 9).addBox(-2.0F, -1.0F, -0.3978F, 4.0F, 1.0F, 0.7956F, new CubeDeformation(0.0F))
		.texOffs(10, 0).addBox(-0.3978F, -1.0F, -2.0F, 0.7956F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition hexadecagon_r14 = sphere0.addOrReplaceChild("hexadecagon_r14", CubeListBuilder.create().texOffs(20, 5).addBox(-2.0F, -1.0F, -0.3978F, 4.0F, 1.0F, 0.7956F, new CubeDeformation(0.0F))
		.texOffs(0, 5).addBox(-0.3978F, -1.0F, -2.0F, 0.7956F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition hexadecagon_r15 = sphere0.addOrReplaceChild("hexadecagon_r15", CubeListBuilder.create().texOffs(10, 5).addBox(-0.3978F, -1.0F, -2.0F, 0.7956F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hexadecagon_r16 = sphere0.addOrReplaceChild("hexadecagon_r16", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3978F, -1.0F, -2.0F, 0.7956F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition sphere4 = partdefinition.addOrReplaceChild("sphere4", CubeListBuilder.create().texOffs(36, 10).addBox(-1.094F, -0.5F, -5.5F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(36, 27).addBox(-1.094F, -0.5F, 4.51F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(30, 3).addBox(4.51F, -0.5F, -1.094F, 0.99F, 1.0F, 2.188F, new CubeDeformation(0.0F))
		.texOffs(12, 30).addBox(-5.5F, -0.5F, -1.094F, 0.99F, 1.0F, 2.188F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 1.0F));

		PartDefinition hexadecagon_r17 = sphere4.addOrReplaceChild("hexadecagon_r17", CubeListBuilder.create().texOffs(30, 12).addBox(-5.5F, -0.5F, -1.094F, 0.99F, 1.0F, 2.188F, new CubeDeformation(0.0F))
		.texOffs(30, 6).addBox(4.51F, -0.5F, -1.094F, 0.99F, 1.0F, 2.188F, new CubeDeformation(0.0F))
		.texOffs(36, 29).addBox(-1.094F, -0.5F, 4.51F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(12, 36).addBox(-1.094F, -0.5F, -5.5F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition hexadecagon_r18 = sphere4.addOrReplaceChild("hexadecagon_r18", CubeListBuilder.create().texOffs(30, 9).addBox(-5.5F, -0.5F, -1.094F, 0.99F, 1.0F, 2.188F, new CubeDeformation(0.0F))
		.texOffs(30, 0).addBox(4.51F, -0.5F, -1.094F, 0.99F, 1.0F, 2.188F, new CubeDeformation(0.0F))
		.texOffs(24, 36).addBox(-1.094F, -0.5F, 4.51F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(36, 8).addBox(-1.094F, -0.5F, -5.5F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition hexadecagon_r19 = sphere4.addOrReplaceChild("hexadecagon_r19", CubeListBuilder.create().texOffs(30, 36).addBox(-1.094F, -0.5F, 4.51F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(36, 12).addBox(-1.094F, -0.5F, -5.5F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hexadecagon_r20 = sphere4.addOrReplaceChild("hexadecagon_r20", CubeListBuilder.create().texOffs(18, 36).addBox(-1.094F, -0.5F, 4.51F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(36, 6).addBox(-1.094F, -0.5F, -5.5F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition sphere_center = partdefinition.addOrReplaceChild("sphere_center", CubeListBuilder.create().texOffs(26, 18).addBox(-1.1437F, -1.5F, -5.75F, 2.2875F, 2.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(28, 24).addBox(-1.1437F, -1.5F, 4.76F, 2.2875F, 2.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(20, 19).addBox(4.76F, -1.5F, -1.1437F, 0.99F, 2.0F, 2.2875F, new CubeDeformation(0.0F))
		.texOffs(22, 23).addBox(-5.75F, -1.5F, -1.1437F, 0.99F, 2.0F, 2.2875F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, 1.0F));

		PartDefinition hexadecagon_r21 = sphere_center.addOrReplaceChild("hexadecagon_r21", CubeListBuilder.create().texOffs(0, 25).addBox(-5.75F, -1.5F, -1.1437F, 0.99F, 2.0F, 2.2875F, new CubeDeformation(0.0F))
		.texOffs(10, 22).addBox(4.76F, -1.5F, -1.1437F, 0.99F, 2.0F, 2.2875F, new CubeDeformation(0.0F))
		.texOffs(0, 29).addBox(-1.1437F, -1.5F, 4.76F, 2.2875F, 2.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(12, 27).addBox(-1.1437F, -1.5F, -5.75F, 2.2875F, 2.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition hexadecagon_r22 = sphere_center.addOrReplaceChild("hexadecagon_r22", CubeListBuilder.create().texOffs(16, 23).addBox(-5.75F, -1.5F, -1.1437F, 0.99F, 2.0F, 2.2875F, new CubeDeformation(0.0F))
		.texOffs(20, 15).addBox(4.76F, -1.5F, -1.1437F, 0.99F, 2.0F, 2.2875F, new CubeDeformation(0.0F))
		.texOffs(28, 21).addBox(-1.1437F, -1.5F, 4.76F, 2.2875F, 2.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(26, 15).addBox(-1.1437F, -1.5F, -5.75F, 2.2875F, 2.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition hexadecagon_r23 = sphere_center.addOrReplaceChild("hexadecagon_r23", CubeListBuilder.create().texOffs(6, 29).addBox(-1.1437F, -1.5F, 4.76F, 2.2875F, 2.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(18, 27).addBox(-1.1437F, -1.5F, -5.75F, 2.2875F, 2.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hexadecagon_r24 = sphere_center.addOrReplaceChild("hexadecagon_r24", CubeListBuilder.create().texOffs(24, 27).addBox(-1.1437F, -1.5F, 4.76F, 2.2875F, 2.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(6, 26).addBox(-1.1437F, -1.5F, -5.75F, 2.2875F, 2.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition sphere5 = partdefinition.addOrReplaceChild("sphere5", CubeListBuilder.create().texOffs(36, 35).addBox(-1.094F, -0.5F, -5.5F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(38, 14).addBox(-1.094F, -0.5F, 4.51F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(24, 30).addBox(4.51F, -0.5F, -1.094F, 0.99F, 1.0F, 2.188F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-5.5F, -0.5F, -1.094F, 0.99F, 1.0F, 2.188F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 1.0F));

		PartDefinition hexadecagon_r25 = sphere5.addOrReplaceChild("hexadecagon_r25", CubeListBuilder.create().texOffs(6, 32).addBox(-5.5F, -0.5F, -1.094F, 0.99F, 1.0F, 2.188F, new CubeDeformation(0.0F))
		.texOffs(30, 27).addBox(4.51F, -0.5F, -1.094F, 0.99F, 1.0F, 2.188F, new CubeDeformation(0.0F))
		.texOffs(38, 16).addBox(-1.094F, -0.5F, 4.51F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(36, 37).addBox(-1.094F, -0.5F, -5.5F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition hexadecagon_r26 = sphere5.addOrReplaceChild("hexadecagon_r26", CubeListBuilder.create().texOffs(30, 30).addBox(-5.5F, -0.5F, -1.094F, 0.99F, 1.0F, 2.188F, new CubeDeformation(0.0F))
		.texOffs(18, 30).addBox(4.51F, -0.5F, -1.094F, 0.99F, 1.0F, 2.188F, new CubeDeformation(0.0F))
		.texOffs(12, 38).addBox(-1.094F, -0.5F, 4.51F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(36, 33).addBox(-1.094F, -0.5F, -5.5F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition hexadecagon_r27 = sphere5.addOrReplaceChild("hexadecagon_r27", CubeListBuilder.create().texOffs(18, 38).addBox(-1.094F, -0.5F, 4.51F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(0, 38).addBox(-1.094F, -0.5F, -5.5F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hexadecagon_r28 = sphere5.addOrReplaceChild("hexadecagon_r28", CubeListBuilder.create().texOffs(6, 38).addBox(-1.094F, -0.5F, 4.51F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(36, 31).addBox(-1.094F, -0.5F, -5.5F, 2.188F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition sphere6 = partdefinition.addOrReplaceChild("sphere6", CubeListBuilder.create().texOffs(30, 40).addBox(-0.9946F, -8.5F, -5.0F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(42, 4).addBox(-0.9946F, -8.5F, 4.01F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(34, 24).addBox(4.01F, -8.5F, -0.9946F, 0.99F, 1.0F, 1.9891F, new CubeDeformation(0.0F))
		.texOffs(36, 0).addBox(-5.0F, -8.5F, -0.9946F, 0.99F, 1.0F, 1.9891F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 1.0F));

		PartDefinition hexadecagon_r29 = sphere6.addOrReplaceChild("hexadecagon_r29", CubeListBuilder.create().texOffs(36, 3).addBox(-5.0F, -0.5F, -0.9946F, 0.99F, 1.0F, 1.9891F, new CubeDeformation(0.0F))
		.texOffs(0, 35).addBox(4.01F, -0.5F, -0.9946F, 0.99F, 1.0F, 1.9891F, new CubeDeformation(0.0F))
		.texOffs(6, 42).addBox(-0.9946F, -0.5F, 4.01F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(36, 41).addBox(-0.9946F, -0.5F, -5.0F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition hexadecagon_r30 = sphere6.addOrReplaceChild("hexadecagon_r30", CubeListBuilder.create().texOffs(6, 35).addBox(-5.0F, -0.5F, -0.9946F, 0.99F, 1.0F, 1.9891F, new CubeDeformation(0.0F))
		.texOffs(34, 21).addBox(4.01F, -0.5F, -0.9946F, 0.99F, 1.0F, 1.9891F, new CubeDeformation(0.0F))
		.texOffs(42, 2).addBox(-0.9946F, -0.5F, 4.01F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(40, 24).addBox(-0.9946F, -0.5F, -5.0F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition hexadecagon_r31 = sphere6.addOrReplaceChild("hexadecagon_r31", CubeListBuilder.create().texOffs(42, 6).addBox(-0.9946F, -0.5F, 4.01F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(0, 42).addBox(-0.9946F, -0.5F, -5.0F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hexadecagon_r32 = sphere6.addOrReplaceChild("hexadecagon_r32", CubeListBuilder.create().texOffs(42, 0).addBox(-0.9946F, -0.5F, 4.01F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(24, 40).addBox(-0.9946F, -0.5F, -5.0F, 1.9891F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition sphere7 = partdefinition.addOrReplaceChild("sphere7", CubeListBuilder.create().texOffs(0, 46).addBox(-0.7956F, -0.5F, -4.0F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(42, 46).addBox(-0.7956F, -0.5F, 3.01F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(30, 42).addBox(3.01F, -0.5F, -0.7956F, 0.99F, 1.0F, 1.5913F, new CubeDeformation(0.0F))
		.texOffs(42, 38).addBox(-4.0F, -0.5F, -0.7956F, 0.99F, 1.0F, 1.5913F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 1.0F));

		PartDefinition hexadecagon_r33 = sphere7.addOrReplaceChild("hexadecagon_r33", CubeListBuilder.create().texOffs(42, 41).addBox(-4.0F, -0.5F, -0.7956F, 0.99F, 1.0F, 1.5913F, new CubeDeformation(0.0F))
		.texOffs(42, 32).addBox(3.01F, -0.5F, -0.7956F, 0.99F, 1.0F, 1.5913F, new CubeDeformation(0.0F))
		.texOffs(12, 47).addBox(-0.7956F, -0.5F, 3.01F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(6, 46).addBox(-0.7956F, -0.5F, -4.0F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition hexadecagon_r34 = sphere7.addOrReplaceChild("hexadecagon_r34", CubeListBuilder.create().texOffs(42, 35).addBox(-4.0F, -0.5F, -0.7956F, 0.99F, 1.0F, 1.5913F, new CubeDeformation(0.0F))
		.texOffs(42, 29).addBox(3.01F, -0.5F, -0.7956F, 0.99F, 1.0F, 1.5913F, new CubeDeformation(0.0F))
		.texOffs(46, 24).addBox(-0.7956F, -0.5F, 3.01F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(36, 45).addBox(-0.7956F, -0.5F, -4.0F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition hexadecagon_r35 = sphere7.addOrReplaceChild("hexadecagon_r35", CubeListBuilder.create().texOffs(18, 47).addBox(-0.7956F, -0.5F, 3.01F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(46, 20).addBox(-0.7956F, -0.5F, -4.0F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hexadecagon_r36 = sphere7.addOrReplaceChild("hexadecagon_r36", CubeListBuilder.create().texOffs(46, 22).addBox(-0.7956F, -0.5F, 3.01F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(30, 45).addBox(-0.7956F, -0.5F, -4.0F, 1.5913F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition sphere8 = partdefinition.addOrReplaceChild("sphere8", CubeListBuilder.create().texOffs(44, 48).addBox(-0.5967F, -1.25F, -3.0F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(16, 49).addBox(-0.5967F, -1.25F, 2.01F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(4, 48).addBox(2.01F, -1.25F, -0.5967F, 0.99F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(8, 48).addBox(-3.0F, -1.25F, -0.5967F, 0.99F, 1.0F, 1.1935F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 1.0F));

		PartDefinition hexadecagon_r37 = sphere8.addOrReplaceChild("hexadecagon_r37", CubeListBuilder.create().texOffs(48, 8).addBox(-3.0F, -1.25F, -0.5967F, 0.99F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(48, 4).addBox(2.01F, -1.25F, -0.5967F, 0.99F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(20, 49).addBox(-0.5967F, -1.25F, 2.01F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(48, 44).addBox(-0.5967F, -1.25F, -3.0F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition hexadecagon_r38 = sphere8.addOrReplaceChild("hexadecagon_r38", CubeListBuilder.create().texOffs(48, 6).addBox(-3.0F, -1.25F, -0.5967F, 0.99F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(48, 2).addBox(2.01F, -1.25F, -0.5967F, 0.99F, 1.0F, 1.1935F, new CubeDeformation(0.0F))
		.texOffs(12, 49).addBox(-0.5967F, -1.25F, 2.01F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(48, 42).addBox(-0.5967F, -1.25F, -3.0F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition hexadecagon_r39 = sphere8.addOrReplaceChild("hexadecagon_r39", CubeListBuilder.create().texOffs(24, 49).addBox(-0.5967F, -1.25F, 2.01F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(48, 46).addBox(-0.5967F, -1.25F, -3.0F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hexadecagon_r40 = sphere8.addOrReplaceChild("hexadecagon_r40", CubeListBuilder.create().texOffs(48, 48).addBox(-0.5967F, -1.25F, 2.01F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F))
		.texOffs(48, 40).addBox(-0.5967F, -1.25F, -3.0F, 1.1935F, 1.0F, 0.99F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition sphere9 = partdefinition.addOrReplaceChild("sphere9", CubeListBuilder.create().texOffs(10, 15).addBox(-0.3978F, -0.75F, -2.0F, 0.7956F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(20, 11).addBox(-2.0F, -0.75F, -0.3978F, 4.0F, 1.0F, 0.7956F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 1.0F));

		PartDefinition hexadecagon_r41 = sphere9.addOrReplaceChild("hexadecagon_r41", CubeListBuilder.create().texOffs(20, 13).addBox(-2.0F, -0.75F, -0.3978F, 4.0F, 1.0F, 0.7956F, new CubeDeformation(0.0F))
		.texOffs(0, 20).addBox(-0.3978F, -0.75F, -2.0F, 0.7956F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition hexadecagon_r42 = sphere9.addOrReplaceChild("hexadecagon_r42", CubeListBuilder.create().texOffs(10, 20).addBox(-2.0F, -0.75F, -0.3978F, 4.0F, 1.0F, 0.7956F, new CubeDeformation(0.0F))
		.texOffs(0, 15).addBox(-0.3978F, -0.75F, -2.0F, 0.7956F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition hexadecagon_r43 = sphere9.addOrReplaceChild("hexadecagon_r43", CubeListBuilder.create().texOffs(20, 0).addBox(-0.3978F, -0.75F, -2.0F, 0.7956F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hexadecagon_r44 = sphere9.addOrReplaceChild("hexadecagon_r44", CubeListBuilder.create().texOffs(10, 10).addBox(-0.3978F, -0.75F, -2.0F, 0.7956F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		sphere3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		sphere2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		sphere1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		sphere0.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		sphere4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		sphere_center.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		sphere5.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		sphere6.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		sphere7.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		sphere8.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		sphere9.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}