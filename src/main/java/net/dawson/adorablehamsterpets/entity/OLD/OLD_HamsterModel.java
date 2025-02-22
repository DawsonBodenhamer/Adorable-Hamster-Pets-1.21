//package net.dawson.adorablehamsterpets.entity.client;//package net.dawson.adorablehamsterpets.entity.client;
//
//import net.dawson.adorablehamsterpets.AdorableHamsterPets;
//import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
//import net.minecraft.client.model.*;
//import net.minecraft.client.render.VertexConsumer;
//import net.minecraft.client.render.entity.model.EntityModelLayer;
//import net.minecraft.client.render.entity.model.SinglePartEntityModel;
//import net.minecraft.client.util.math.MatrixStack;
//import net.minecraft.util.Identifier;
//import net.minecraft.util.math.MathHelper;
//
//public class HamsterModel<T extends HamsterEntity> extends SinglePartEntityModel<T> {
//
//    public static final EntityModelLayer HAMSTER = new EntityModelLayer(Identifier.of(AdorableHamsterPets.MOD_ID, "root"), "main");
//
//    private final ModelPart root;
//    private final ModelPart body;
//    private final ModelPart crown_cape;
//    private final ModelPart head_rot;
//    private final ModelPart head;
//    private final ModelPart armor_biped_head;
//    private final ModelPart ears;
//    private final ModelPart leftEar;
//    private final ModelPart rightEar;
//    private final ModelPart cheeks;
//    private final ModelPart left_cheek;
//    private final ModelPart right_cheek;
//    private final ModelPart sleep;
//    private final ModelPart tail;
//    private final ModelPart bunny_tail;
//    private final ModelPart right_hand;
//    private final ModelPart left_hand;
//    private final ModelPart legs;
//    private final ModelPart left_foot;
//    private final ModelPart right_foot;
//
//    public HamsterModel(ModelPart root) {
//        this.root = root.getChild("root");
//        this.body = this.root.getChild("body");
//        this.crown_cape = this.body.getChild("crown_cape");
//        this.head_rot = this.body.getChild("head_rot");
//        this.head = this.head_rot.getChild("head");
//        this.armor_biped_head = this.head.getChild("armor_biped_head");
//        this.ears = this.head.getChild("ears");
//        this.leftEar = this.ears.getChild("leftEar");
//        this.rightEar = this.ears.getChild("rightEar");
//        this.cheeks = this.head.getChild("cheeks");
//        this.left_cheek = this.cheeks.getChild("left_cheek");
//        this.right_cheek = this.cheeks.getChild("right_cheek");
//        this.sleep = this.head.getChild("sleep");
//        this.tail = this.body.getChild("tail");
//        this.bunny_tail = this.tail.getChild("bunny_tail");
//        this.right_hand = this.body.getChild("right_hand");
//        this.left_hand = this.body.getChild("left_hand");
//        this.legs = this.root.getChild("legs");
//        this.left_foot = this.legs.getChild("left_foot");
//        this.right_foot = this.legs.getChild("right_foot");
//    }
//
//
//
//    public static TexturedModelData getTexturedModelData() {
//        ModelData modelData = new ModelData();
//        ModelPartData modelPartData = modelData.getRoot();
//        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
//
//        ModelPartData body = root.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -3.0F, -5.0F, 6.0F, 4.0F, 6.0F, new Dilation(0.01F)), ModelTransform.pivot(0.0F, -1.5F, 3.0F));
//
//        ModelPartData crown_cape = body.addChild("crown_cape", ModelPartBuilder.create().uv(0, 110).cuboid(-3.0F, 0.0F, -1.0F, 6.0F, 4.0F, 6.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -3.0F, -4.0F));
//
//        ModelPartData head_rot = body.addChild("head_rot", ModelPartBuilder.create(), ModelTransform.pivot(-0.5F, -2.0F, -3.0F));
//
//        ModelPartData head = head_rot.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
//
//        ModelPartData armor_biped_head = head.addChild("armor_biped_head", ModelPartBuilder.create().uv(1, 11).cuboid(-2.5F, -4.0F, -2.0F, 5.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, 1.5F, -2.0F));
//
//        ModelPartData ears = head.addChild("ears", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, -1.5F, -2.0F));
//
//        ModelPartData leftEar = ears.addChild("leftEar", ModelPartBuilder.create().uv(0, 11).cuboid(0.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new Dilation(0.01F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
//
//        ModelPartData rightEar = ears.addChild("rightEar", ModelPartBuilder.create().uv(0, 11).mirrored().cuboid(-2.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new Dilation(0.01F)).mirrored(false), ModelTransform.pivot(-3.0F, 0.0F, 0.0F));
//
//        ModelPartData cheeks = head.addChild("cheeks", ModelPartBuilder.create(), ModelTransform.pivot(-4.0F, 0.0F, -2.0F));
//
//        ModelPartData left_cheek = cheeks.addChild("left_cheek", ModelPartBuilder.create().uv(20, 13).cuboid(0.0F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(7.0F, 0.0F, -0.5F));
//
//        ModelPartData right_cheek = cheeks.addChild("right_cheek", ModelPartBuilder.create().uv(20, 13).mirrored().cuboid(-2.0F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(2.0F, 0.0F, -0.5F));
//
//        ModelPartData sleep = head.addChild("sleep", ModelPartBuilder.create().uv(5, 22).cuboid(-2.5F, -4.0F, -4.025F, 5.0F, 1.0F, 0.0F, new Dilation(0.01F)), ModelTransform.pivot(0.5F, 2.5F, 0.0F));
//
//        ModelPartData tail = body.addChild("tail", ModelPartBuilder.create().uv(19, 0).cuboid(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -3.0F, 1.0F));
//
//        ModelPartData bunny_tail = tail.addChild("bunny_tail", ModelPartBuilder.create().uv(56, 86).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.02F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
//
//        ModelPartData right_hand = body.addChild("right_hand", ModelPartBuilder.create().uv(24, 2).mirrored().cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-1.5F, 0.5F, -4.0F));
//
//        ModelPartData left_hand = body.addChild("left_hand", ModelPartBuilder.create().uv(24, 2).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(1.5F, 0.5F, -4.0F));
//
//        ModelPartData legs = root.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(1.5F, -1.0F, -1.0F));
//
//        ModelPartData left_foot = legs.addChild("left_foot", ModelPartBuilder.create().uv(24, 2).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 4.0F));
//
//        ModelPartData right_foot = legs.addChild("right_foot", ModelPartBuilder.create().uv(24, 2).mirrored().cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-3.0F, 0.0F, 4.0F));
//        return TexturedModelData.of(modelData, 128, 128);
//    }
//
//
//    @Override
//    public void setAngles(HamsterEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//        this.getPart().traverse().forEach(ModelPart::resetTransform);
//        this.setHeadAngles(netHeadYaw, headPitch);
//
//        this.animateMovement(HamsterAnimations.ANIM_HAMSTER_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
//        this.updateAnimation(entity.idleAnimationState, HamsterAnimations.ANIM_HAMSTER_IDLE, ageInTicks, 1f);
//    }
//
//    private void setHeadAngles(float headYaw, float headPitch) {
//        headYaw = MathHelper.clamp(headYaw, -30.0F, 30.0F);
//        headPitch = MathHelper.clamp(headPitch, -25.0F, 45.0F);
//
//        this.head.yaw = headYaw * 0.017453292F;
//        this.head.pitch = headPitch * 0.017453292F;
//    }
//
//    @Override
//    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
//        root.render(matrices, vertexConsumer, light, overlay, color);
//    }
//
//    @Override
//    public ModelPart getPart() {
//        return root;
//    }
//}
//
