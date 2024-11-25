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

public class GrandSlamModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(BeyondTheHorizon.MOD_ID, "baseform_grand_slam"), "main");
	public static final ModModelRenderer.Texture[] TEXTURE_LOCATIONS = {
			new ModModelRenderer.Texture("textures/custom_model/baseform/spindash/0.png",(byte)0),
			new ModModelRenderer.Texture("textures/custom_model/baseform/spindash/1.png",(byte)1),
			new ModModelRenderer.Texture("textures/custom_model/baseform/spindash/2.png",(byte)2),
			new ModModelRenderer.Texture("textures/custom_model/baseform/spindash/3.png",(byte)3)
	};
	public static final byte ANIMATION_LENGTH = 4;

	private final ModelPart Ballform;
	private final ModelPart hexadecagon1;
	private final ModelPart hexadecagon2;
	private final ModelPart hexadecagon3;
	private final ModelPart hexadecagon4;
	private final ModelPart SpikeAura;
	private final ModelPart spike16;
	private final ModelPart spike15;
	private final ModelPart spike14;
	private final ModelPart spike13;
	private final ModelPart spike12;
	private final ModelPart spike11;
	private final ModelPart spike10;
	private final ModelPart spike9;
	private final ModelPart spike8;
	private final ModelPart spike7;
	private final ModelPart spike6;
	private final ModelPart spike5;
	private final ModelPart spike4;
	private final ModelPart spike3;
	private final ModelPart spike2;
	private final ModelPart spike;
	private final ModelPart hexadecagon;

	public GrandSlamModel(ModelPart root) {
		this.Ballform = root.getChild("Ballform");
		this.hexadecagon1 = this.Ballform.getChild("hexadecagon1");
		this.hexadecagon2 = this.Ballform.getChild("hexadecagon2");
		this.hexadecagon3 = this.Ballform.getChild("hexadecagon3");
		this.hexadecagon4 = this.Ballform.getChild("hexadecagon4");
		this.SpikeAura = root.getChild("SpikeAura");
		this.spike16 = this.SpikeAura.getChild("spike16");
		this.spike15 = this.SpikeAura.getChild("spike15");
		this.spike14 = this.SpikeAura.getChild("spike14");
		this.spike13 = this.SpikeAura.getChild("spike13");
		this.spike12 = this.SpikeAura.getChild("spike12");
		this.spike11 = this.SpikeAura.getChild("spike11");
		this.spike10 = this.SpikeAura.getChild("spike10");
		this.spike9 = this.SpikeAura.getChild("spike9");
		this.spike8 = this.SpikeAura.getChild("spike8");
		this.spike7 = this.SpikeAura.getChild("spike7");
		this.spike6 = this.SpikeAura.getChild("spike6");
		this.spike5 = this.SpikeAura.getChild("spike5");
		this.spike4 = this.SpikeAura.getChild("spike4");
		this.spike3 = this.SpikeAura.getChild("spike3");
		this.spike2 = this.SpikeAura.getChild("spike2");
		this.spike = this.SpikeAura.getChild("spike");
		this.hexadecagon = this.SpikeAura.getChild("hexadecagon");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Ballform = partdefinition.addOrReplaceChild("Ballform", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 29.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition hexadecagon1 = Ballform.addOrReplaceChild("hexadecagon1", CubeListBuilder.create().texOffs(38, 0).addBox(-1.6012F, 0.1466F, -8.05F, 3.2025F, 7.9734F, 16.1F, new CubeDeformation(0.0F))
		.texOffs(52, 121).addBox(-8.05F, 0.1466F, -1.6012F, 16.1F, 7.9734F, 3.2025F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, -4.0F, 0.0F));

		PartDefinition hexadecagon12_r1 = hexadecagon1.addOrReplaceChild("hexadecagon12_r1", CubeListBuilder.create().texOffs(90, 121).addBox(-8.05F, -3.8534F, -1.6012F, 16.1F, 7.9734F, 3.2025F, new CubeDeformation(0.0F))
		.texOffs(38, 24).addBox(-1.6012F, -3.8534F, -8.05F, 3.2025F, 7.9734F, 16.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition hexadecagon10_r1 = hexadecagon1.addOrReplaceChild("hexadecagon10_r1", CubeListBuilder.create().texOffs(102, 75).addBox(-8.05F, -3.8534F, -1.6012F, 16.1F, 7.9734F, 3.2025F, new CubeDeformation(0.0F))
		.texOffs(0, 24).addBox(-1.6012F, -3.8534F, -8.05F, 3.2025F, 7.9734F, 16.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition hexadecagon9_r1 = hexadecagon1.addOrReplaceChild("hexadecagon9_r1", CubeListBuilder.create().texOffs(0, 48).addBox(-1.6012F, -3.8534F, -8.05F, 3.2025F, 7.9734F, 16.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hexadecagon5_r1 = hexadecagon1.addOrReplaceChild("hexadecagon5_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.6012F, -3.8534F, -8.05F, 3.2025F, 7.9734F, 16.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition hexadecagon2 = Ballform.addOrReplaceChild("hexadecagon2", CubeListBuilder.create().texOffs(34, 72).addBox(-1.4411F, -0.9662F, -7.245F, 2.8822F, 10.0522F, 14.49F, new CubeDeformation(0.0F))
		.texOffs(128, 99).addBox(-7.245F, -0.9662F, -1.4411F, 14.49F, 10.0522F, 2.8822F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, -4.0F, 0.0F));

		PartDefinition hexadecagon20_r1 = hexadecagon2.addOrReplaceChild("hexadecagon20_r1", CubeListBuilder.create().texOffs(128, 112).addBox(-7.245F, 57.1798F, -1.4411F, 14.49F, 10.0522F, 2.8822F, new CubeDeformation(0.0F))
		.texOffs(72, 48).addBox(-1.4411F, 57.1798F, -7.245F, 2.8822F, 10.0522F, 14.49F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -58.146F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition hexadecagon18_r1 = hexadecagon2.addOrReplaceChild("hexadecagon18_r1", CubeListBuilder.create().texOffs(128, 86).addBox(-7.245F, 57.1798F, -1.4411F, 14.49F, 10.0522F, 2.8822F, new CubeDeformation(0.0F))
		.texOffs(0, 72).addBox(-1.4411F, 57.1798F, -7.245F, 2.8822F, 10.0522F, 14.49F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -58.146F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition hexadecagon17_r1 = hexadecagon2.addOrReplaceChild("hexadecagon17_r1", CubeListBuilder.create().texOffs(68, 72).addBox(-1.4411F, 57.1798F, -7.245F, 2.8822F, 10.0522F, 14.49F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -58.146F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hexadecagon13_r1 = hexadecagon2.addOrReplaceChild("hexadecagon13_r1", CubeListBuilder.create().texOffs(38, 48).addBox(-1.4411F, 57.1798F, -7.245F, 2.8822F, 10.0522F, 14.49F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -58.146F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition hexadecagon3 = Ballform.addOrReplaceChild("hexadecagon3", CubeListBuilder.create().texOffs(32, 96).addBox(-1.281F, -2.0919F, -6.44F, 2.562F, 12.4659F, 12.88F, new CubeDeformation(0.0F))
		.texOffs(132, 25).addBox(-6.44F, -2.0919F, -1.281F, 12.88F, 12.4659F, 2.562F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, -4.0F, 0.0F));

		PartDefinition hexadecagon28_r1 = hexadecagon3.addOrReplaceChild("hexadecagon28_r1", CubeListBuilder.create().texOffs(132, 40).addBox(-6.44F, -6.4139F, -1.281F, 12.88F, 12.4659F, 2.562F, new CubeDeformation(0.0F))
		.texOffs(64, 96).addBox(-1.281F, -6.4139F, -6.44F, 2.562F, 12.4659F, 12.88F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.322F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition hexadecagon26_r1 = hexadecagon3.addOrReplaceChild("hexadecagon26_r1", CubeListBuilder.create().texOffs(128, 125).addBox(-6.44F, -6.4139F, -1.281F, 12.88F, 12.4659F, 2.562F, new CubeDeformation(0.0F))
		.texOffs(0, 96).addBox(-1.281F, -6.4139F, -6.44F, 2.562F, 12.4659F, 12.88F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.322F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition hexadecagon25_r1 = hexadecagon3.addOrReplaceChild("hexadecagon25_r1", CubeListBuilder.create().texOffs(96, 96).addBox(-1.281F, -6.4139F, -6.44F, 2.562F, 12.4659F, 12.88F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.322F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hexadecagon21_r1 = hexadecagon3.addOrReplaceChild("hexadecagon21_r1", CubeListBuilder.create().texOffs(76, 0).addBox(-1.281F, -6.4139F, -6.44F, 2.562F, 12.4659F, 12.88F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.322F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition hexadecagon4 = Ballform.addOrReplaceChild("hexadecagon4", CubeListBuilder.create().texOffs(108, 0).addBox(-1.0568F, -2.9967F, -5.313F, 2.1137F, 14.3367F, 10.626F, new CubeDeformation(0.0F))
		.texOffs(52, 132).addBox(-5.313F, -2.9967F, -1.0568F, 10.626F, 14.3367F, 2.1137F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, -4.0F, 0.0F));

		PartDefinition hexadecagon36_r1 = hexadecagon4.addOrReplaceChild("hexadecagon36_r1", CubeListBuilder.create().texOffs(132, 55).addBox(-5.313F, 27.4573F, -1.0568F, 10.626F, 14.3367F, 2.1137F, new CubeDeformation(0.0F))
		.texOffs(0, 121).addBox(-1.0568F, 27.4573F, -5.313F, 2.1137F, 14.3367F, 10.626F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -30.454F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition hexadecagon34_r1 = hexadecagon4.addOrReplaceChild("hexadecagon34_r1", CubeListBuilder.create().texOffs(76, 25).addBox(-5.313F, 27.4573F, -1.0568F, 10.626F, 14.3367F, 2.1137F, new CubeDeformation(0.0F))
		.texOffs(106, 50).addBox(-1.0568F, 27.4573F, -5.313F, 2.1137F, 14.3367F, 10.626F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -30.454F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition hexadecagon33_r1 = hexadecagon4.addOrReplaceChild("hexadecagon33_r1", CubeListBuilder.create().texOffs(26, 121).addBox(-1.0568F, 27.4573F, -5.313F, 2.1137F, 14.3367F, 10.626F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -30.454F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hexadecagon29_r1 = hexadecagon4.addOrReplaceChild("hexadecagon29_r1", CubeListBuilder.create().texOffs(106, 25).addBox(-1.0568F, 27.4573F, -5.313F, 2.1137F, 14.3367F, 10.626F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -30.454F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition SpikeAura = partdefinition.addOrReplaceChild("SpikeAura", CubeListBuilder.create(), PartPose.offset(10.0F, 16.0F, 0.0F));

		PartDefinition spike16 = SpikeAura.addOrReplaceChild("spike16", CubeListBuilder.create().texOffs(163, 0).addBox(-0.959F, -3.54F, -4.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(165, 2).addBox(-0.459F, -2.541F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(169, 3).addBox(0.5F, -1.5F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 5.0F, 9.0F, -0.3054F, 0.0F, 0.0F));

		PartDefinition spike15 = SpikeAura.addOrReplaceChild("spike15", CubeListBuilder.create().texOffs(163, 0).addBox(-0.959F, -3.54F, -4.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(165, 2).addBox(-0.459F, -2.541F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(169, 3).addBox(0.5F, -1.5F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 8.0F, 7.0F, -0.5672F, 0.0F, 0.0F));

		PartDefinition spike14 = SpikeAura.addOrReplaceChild("spike14", CubeListBuilder.create().texOffs(163, 0).addBox(-0.959F, -3.54F, -4.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(165, 2).addBox(-0.459F, -2.541F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(169, 3).addBox(0.5F, -1.5F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 10.0F, 3.0F, -1.0908F, 0.0F, 0.0F));

		PartDefinition spike13 = SpikeAura.addOrReplaceChild("spike13", CubeListBuilder.create().texOffs(163, 0).addBox(-0.959F, -4.0F, -4.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(165, 2).addBox(-0.459F, -3.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(169, 3).addBox(0.5F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition spike12 = SpikeAura.addOrReplaceChild("spike12", CubeListBuilder.create().texOffs(163, 0).addBox(-0.959F, -3.54F, -4.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(165, 2).addBox(-0.459F, -2.541F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(169, 3).addBox(0.5F, -1.5F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 9.0F, -6.0F, -2.0071F, 0.0F, 0.0F));

		PartDefinition spike11 = SpikeAura.addOrReplaceChild("spike11", CubeListBuilder.create().texOffs(163, 0).addBox(-0.959F, -3.041F, -5.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(165, 2).addBox(-0.459F, -2.041F, -2.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(169, 3).addBox(0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 7.0F, -9.0F, -2.3126F, 0.0F, 0.0F));

		PartDefinition spike10 = SpikeAura.addOrReplaceChild("spike10", CubeListBuilder.create().texOffs(163, 0).addBox(-0.959F, -3.041F, -5.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(165, 2).addBox(-0.459F, -2.041F, -2.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(169, 3).addBox(0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 3.0F, -11.0F, -2.7925F, 0.0F, 0.0F));

		PartDefinition spike9 = SpikeAura.addOrReplaceChild("spike9", CubeListBuilder.create().texOffs(164, 1).addBox(-0.959F, -3.041F, -3.5F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(165, 2).addBox(-0.459F, -2.041F, -1.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(169, 3).addBox(0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, -1.0F, -11.0F, -3.0543F, 0.0F, 0.0F));

		PartDefinition spike8 = SpikeAura.addOrReplaceChild("spike8", CubeListBuilder.create().texOffs(164, 1).addBox(-0.959F, -3.041F, -3.5F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(165, 2).addBox(-0.459F, -2.041F, -1.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(169, 3).addBox(0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, -5.0F, -10.0F, 2.8362F, 0.0F, 0.0F));

		PartDefinition spike7 = SpikeAura.addOrReplaceChild("spike7", CubeListBuilder.create().texOffs(164, 1).addBox(-0.959F, -3.041F, -3.5F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(165, 2).addBox(-0.459F, -2.041F, -1.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(169, 3).addBox(0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, -8.0F, -7.0F, 2.3998F, 0.0F, 0.0F));

		PartDefinition spike6 = SpikeAura.addOrReplaceChild("spike6", CubeListBuilder.create().texOffs(164, 1).addBox(-0.959F, -3.041F, -3.5F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(165, 2).addBox(-0.459F, -2.041F, -1.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(169, 3).addBox(0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, -10.0F, -3.0F, 1.9635F, 0.0F, 0.0F));

		PartDefinition spike5 = SpikeAura.addOrReplaceChild("spike5", CubeListBuilder.create().texOffs(164, 1).addBox(-0.959F, -3.041F, -3.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(165, 2).addBox(-0.459F, -2.041F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(169, 3).addBox(0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, -10.0F, 1.0F, 1.6581F, 0.0F, 0.0F));

		PartDefinition spike4 = SpikeAura.addOrReplaceChild("spike4", CubeListBuilder.create().texOffs(164, 1).addBox(-0.959F, -3.041F, -3.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(165, 2).addBox(-0.459F, -2.041F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(169, 3).addBox(0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, -9.0F, 5.0F, 1.2654F, 0.0F, 0.0F));

		PartDefinition spike3 = SpikeAura.addOrReplaceChild("spike3", CubeListBuilder.create().texOffs(164, 1).addBox(-0.959F, -3.041F, -3.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(165, 2).addBox(-0.459F, -2.041F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(169, 3).addBox(0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, -7.0F, 8.0F, 0.9163F, 0.0F, 0.0F));

		PartDefinition spike2 = SpikeAura.addOrReplaceChild("spike2", CubeListBuilder.create().texOffs(164, 1).addBox(-0.959F, -3.041F, -3.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(165, 2).addBox(-0.459F, -2.041F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(169, 3).addBox(0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, -3.0F, 10.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition spike = SpikeAura.addOrReplaceChild("spike", CubeListBuilder.create().texOffs(164, 1).addBox(-0.959F, -3.041F, -3.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(165, 2).addBox(-0.459F, -2.041F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(169, 3).addBox(0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-11.0F, 1.0F, 10.0F));

		PartDefinition hexadecagon = SpikeAura.addOrReplaceChild("hexadecagon", CubeListBuilder.create().texOffs(162, 1).addBox(-4.5F, -1.6908F, -8.5F, 5.0F, 3.3815F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(162, 1).addBox(-4.5F, -1.6908F, 7.5F, 5.0F, 3.3815F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(162, 1).addBox(-4.5F, 7.5F, -1.6908F, 5.0F, 1.0F, 3.3815F, new CubeDeformation(0.0F))
		.texOffs(162, 1).addBox(-4.5F, -8.5F, -1.6908F, 5.0F, 1.0F, 3.3815F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r1 = hexadecagon.addOrReplaceChild("hexadecagon_r1", CubeListBuilder.create().texOffs(162, 1).addBox(-4.5F, -8.5F, -1.6908F, 5.0F, 1.0F, 3.3815F, new CubeDeformation(0.0F))
		.texOffs(162, 1).addBox(-4.5F, 7.5F, -1.6908F, 5.0F, 1.0F, 3.3815F, new CubeDeformation(0.0F))
		.texOffs(162, 1).addBox(-4.5F, -1.6908F, 7.5F, 5.0F, 3.3815F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(162, 1).addBox(-4.5F, -1.6908F, -8.5F, 5.0F, 3.3815F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r2 = hexadecagon.addOrReplaceChild("hexadecagon_r2", CubeListBuilder.create().texOffs(162, 1).addBox(-4.5F, -8.5F, -1.6908F, 5.0F, 1.0F, 3.3815F, new CubeDeformation(0.0F))
		.texOffs(162, 1).addBox(-4.5F, 7.5F, -1.6908F, 5.0F, 1.0F, 3.3815F, new CubeDeformation(0.0F))
		.texOffs(162, 1).addBox(-4.5F, -1.6908F, 7.5F, 5.0F, 3.3815F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(162, 1).addBox(-4.5F, -1.6908F, -8.5F, 5.0F, 3.3815F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r3 = hexadecagon.addOrReplaceChild("hexadecagon_r3", CubeListBuilder.create().texOffs(162, 1).addBox(-4.5F, -1.6908F, 7.5F, 5.0F, 3.3815F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(162, 1).addBox(-4.5F, -1.6908F, -8.5F, 5.0F, 3.3815F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r4 = hexadecagon.addOrReplaceChild("hexadecagon_r4", CubeListBuilder.create().texOffs(162, 1).addBox(-4.5F, -1.6908F, 7.5F, 5.0F, 3.3815F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(162, 1).addBox(-4.5F, -1.6908F, -8.5F, 5.0F, 3.3815F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Ballform.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		SpikeAura.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}