package net.dawson.adorablehamsterpets.entity.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Vanilla-style ModelPart model for rendering the hamster on the player's shoulder.
 * Translated structure from hamster.geo.json.
 * Includes references to cheek parts for visibility control.
 */
public class HamsterShoulderModel extends EntityModel<AbstractClientPlayerEntity> {

    public final ModelPart root;
    public final ModelPart left_cheek_deflated;
    public final ModelPart left_cheek_inflated;
    public final ModelPart right_cheek_deflated;
    public final ModelPart right_cheek_inflated;

    public HamsterShoulderModel(ModelPart root) {
        this.root = root;

        // Get references to cheek parts
        try {
            ModelPart cheeks = this.root.getChild("root")
                    .getChild("body")
                    .getChild("head_parent")
                    .getChild("head_child")
                    .getChild("cheeks");

            this.left_cheek_deflated = cheeks.getChild("left_cheek_deflated");
            this.left_cheek_inflated = cheeks.getChild("left_cheek_inflated");
            this.right_cheek_deflated = cheeks.getChild("right_cheek_deflated");
            this.right_cheek_inflated = cheeks.getChild("right_cheek_inflated");


        } catch (Exception e) {
            throw new RuntimeException("Failed to get cheek ModelParts in HamsterShoulderModel constructor. Check hierarchy/names.", e);
        }
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        ModelPartData body = root.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -3.0F, -5.0F, 6.0F, 4.0F, 6.0F, new Dilation(0.01F)), ModelTransform.pivot(0.0F, -1.5F, 3.0F));

        // --- Start Updated Bone Definitions ---
        ModelPartData head_parent = body.addChild("head_parent", ModelPartBuilder.create(), ModelTransform.pivot(-0.5F, -2.0F, -3.0F));
        ModelPartData head_child = head_parent.addChild("head_child", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        // --- nose definition ---
        head_child.addChild("nose", ModelPartBuilder.create().uv(27, 8).cuboid(-0.5F, -4.0F, -3.1F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, 3.5F, -1.0F));

        head_child.addChild("head_skull", ModelPartBuilder.create().uv(1, 11).cuboid(-2.5F, -4.0F, -2.0F, 5.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, 1.5F, -2.0F));

        ModelPartData ears = head_child.addChild("ears", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, -1.5F, -2.0F));
        // ---  left_ear cube definition ---
        ears.addChild("left_ear", ModelPartBuilder.create().uv(9, 25).cuboid(0.0F, -2.0F, 0.1F, 2.0F, 3.0F, 1.0F, new Dilation(0.01F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        // ---  right_ear cube definition ---
        ears.addChild("right_ear", ModelPartBuilder.create().uv(9, 25).mirrored().cuboid(-2.0F, -2.0F, 0.1F, 2.0F, 3.0F, 1.0F, new Dilation(0.01F)).mirrored(false), ModelTransform.pivot(-3.0F, 0.0F, 0.0F));

        ModelPartData cheeks = head_child.addChild("cheeks", ModelPartBuilder.create(), ModelTransform.pivot(-4.0F, 0.0F, -2.0F));
        // Deflated Cheeks
        cheeks.addChild("left_cheek_deflated", ModelPartBuilder.create().uv(20, 13).cuboid(0.0F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(7.0F, 0.0F, -0.5F));
        cheeks.addChild("right_cheek_deflated", ModelPartBuilder.create().uv(21, 13).mirrored().cuboid(-1.0F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(2.0F, 0.0F, -0.5F));
        // ---  left_cheek_inflated cube definition ---
        cheeks.addChild("left_cheek_inflated", ModelPartBuilder.create().uv(20, 13).cuboid(0.0F, -1.6F, -1.6F, 2.0F, 3.1F, 3.1F, new Dilation(0.0F)), ModelTransform.pivot(7.0F, 0.0F, -0.5F));
        // ---  right_cheek_inflated cube definition ---
        cheeks.addChild("right_cheek_inflated", ModelPartBuilder.create().uv(20, 13).mirrored().cuboid(-2.0F, -1.6F, -1.6F, 2.0F, 3.1F, 3.1F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(2.0F, 0.0F, -0.5F));

        // ---  closed_eyes definition ---
        head_child.addChild("closed_eyes", ModelPartBuilder.create().uv(5, 21).cuboid(-2.5F, -4.1F, -4.025F, 5.0F, 1.2F, 1.0F, new Dilation(0.01F)), ModelTransform.pivot(0.5F, 2.5F, 0.0F));
        

        // ---  tail cube definition ---
        ModelPartData tail = body.addChild("tail", ModelPartBuilder.create().uv(20, 23).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -3.0F, 1.0F));
        

        // Hands and Legs 
        body.addChild("right_hand", ModelPartBuilder.create().uv(24, 2).mirrored().cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-1.5F, 0.5F, -4.0F));
        body.addChild("left_hand", ModelPartBuilder.create().uv(24, 2).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(1.5F, 0.5F, -4.0F));
        ModelPartData legs = root.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(1.5F, -1.0F, -1.0F));
        legs.addChild("left_foot", ModelPartBuilder.create().uv(24, 2).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 4.0F));
        legs.addChild("right_foot", ModelPartBuilder.create().uv(24, 2).mirrored().cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-3.0F, 0.0F, 4.0F));
        

        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void setAngles(AbstractClientPlayerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // No dynamic angles needed for static shoulder model
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        this.root.render(matrices, vertexConsumer, light, overlay, color);
    }
}