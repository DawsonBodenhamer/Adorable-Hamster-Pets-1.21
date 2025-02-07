package net.dawson.adorablehamsterpets.entity.client;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class HamsterAnimations {

    public static final Animation ANIM_HAMSTER_STANDING = Animation.Builder.create(2f).looping()
            .addBoneAnimation("body",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, -1f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("body",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-55f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("root",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, -2f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("root",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.65f, AnimationHelper.createScalingVector(0.97f, 1.04f, 0.94f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.35f, AnimationHelper.createScalingVector(1f, 0.95f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(2f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head_rot",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, -1.5f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head_rot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(55f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_hand",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, -1f, 0.5f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_hand",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(132.5f, 15f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_hand",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, -1f, 0.5f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_hand",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(132.5f, -15f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_foot",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, -1f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_foot",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, -1f),
                                    Transformation.Interpolations.LINEAR))).build();
    public static final Animation ANIM_HAMSTER_IDLE = Animation.Builder.create(2f).looping()
            .addBoneAnimation("tail",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.625f, AnimationHelper.createRotationalVector(0f, 10f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.75f, AnimationHelper.createRotationalVector(0f, -20f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(0f, 10f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1.0417f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("leftEar",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(5.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("rightEar",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-0.5f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("root",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.625f, AnimationHelper.createScalingVector(0.97f, 1.04f, 0.94f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.3333f, AnimationHelper.createScalingVector(1f, 0.95f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(2f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR))).build();
    public static final Animation ANIM_HAMSTER_WALK = Animation.Builder.create(21.5417f).looping()
            .addBoneAnimation("tail",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(8.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("body",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, -0.5f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("body",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 5.0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("rightEar",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(3.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("leftEar",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-3.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("pink_bow_left",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 2.0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("pink_bow_string_left",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 2.0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("pink_bow_right",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, -2.0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("pink_bow_string_right",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, -2.0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("santa_poof",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(8.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head_rot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 7.5f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_hand",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(12.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_hand",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-12.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_foot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(15.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_foot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-15.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR))).build();

    public static final Animation ANIM_HAMSTER_RUN = Animation.Builder.create(1f).looping()
            .addBoneAnimation("tail",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(8.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("body",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, -1.0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("body",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(2.5f, 0f, 10.0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("rightEar",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(5.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("leftEar",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-5.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("pink_bow_string_left",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 5.0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("pink_bow_string_right",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, -5.0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("santa_poof",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(12.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head_rot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-7.5f, 0f, 12.5f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_hand",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(18.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_hand",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-18.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_foot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(20.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_foot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-20.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR))).build();

    public static final Animation ANIM_HAMSTER_SLEEP = Animation.Builder.create(2.75f).looping()
            .addBoneAnimation("tail",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-35f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("body",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_hand",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(-1f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_hand",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 17.5f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_foot",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(-1f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_foot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 20f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("root",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(2f, 3f, -3f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("root",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-25.0f, 20f, -90f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("root",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1f, AnimationHelper.createScalingVector(1.1f, 1.2f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.5417f, AnimationHelper.createScalingVector(1.1f, 1.2f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(2.5417f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(2.75f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("rightEar",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-11.58f, 4.75f, 22.02f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("leftEar",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(15f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.4583f, AnimationHelper.createRotationalVector(15f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5417f, AnimationHelper.createRotationalVector(25f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.625f, AnimationHelper.createRotationalVector(-22.5f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.7083f, AnimationHelper.createRotationalVector(25f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.8333f, AnimationHelper.createRotationalVector(15f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head_rot",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(-0.75f, 0f, -1.25f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head_rot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(41.76f, -6.98f, -20.9f),
                                    Transformation.Interpolations.LINEAR))).build();

    public static final Animation ANIM_HAMSTER_SQUISH = Animation.Builder.create(0.4f)
            .addBoneAnimation("root",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createScalingVector(1.4f, 0.3f, 1.3f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createScalingVector(1.3f, 0.4f, 1.2f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3f, AnimationHelper.createScalingVector(1.3f, 0.4f, 1.2f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("body",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createTranslationalVector(0f, -0.5f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createTranslationalVector(0f, -0.5f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3f, AnimationHelper.createTranslationalVector(0f, -0.5f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createTranslationalVector(0f, -1.6f, -1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createTranslationalVector(0f, -1.6f, -1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3f, AnimationHelper.createTranslationalVector(0f, -1.6f, -1f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createScalingVector(1f, 0.9f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createScalingVector(1f, 0.9f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3f, AnimationHelper.createScalingVector(1f, 0.9f, 1f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_hand",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createTranslationalVector(-1.5f, 0.5f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createTranslationalVector(-1.5f, 0.5f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3f, AnimationHelper.createTranslationalVector(-1.5f, 0.5f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_hand",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createRotationalVector(0f, 0f, 90f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createRotationalVector(0f, 0f, 90f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3f, AnimationHelper.createRotationalVector(0f, 0f, 90f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_hand",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createTranslationalVector(1.5f, 0.5f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createTranslationalVector(1.5f, 0.5f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3f, AnimationHelper.createTranslationalVector(1.5f, 0.5f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_hand",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createRotationalVector(0f, 0f, -90f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createRotationalVector(0f, 0f, -90f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3f, AnimationHelper.createRotationalVector(0f, 0f, -90f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_foot",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createTranslationalVector(1.5f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createTranslationalVector(1.5f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3f, AnimationHelper.createTranslationalVector(1.5f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_foot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createRotationalVector(-360f, 0f, -90f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createRotationalVector(-360f, 0f, -90f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3f, AnimationHelper.createRotationalVector(-360f, 0f, -90f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_foot",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createTranslationalVector(-1.5f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createTranslationalVector(-1.5f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3f, AnimationHelper.createTranslationalVector(-1.5f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_foot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createRotationalVector(-360f, 0f, -270f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createRotationalVector(-360f, 0f, -270f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3f, AnimationHelper.createRotationalVector(-360f, 0f, -270f),
                                    Transformation.Interpolations.LINEAR))).build();

    public static final Animation ANIM_HAMSTER_SQUISHED = Animation.Builder.create(1f).looping()
            .addBoneAnimation("root",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.createScalingVector(1.3f, 0.4f, 1.2f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("body",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, -0.5f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_hand",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(-1.5f, 0.5f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_hand",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 90f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_hand",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(1.5f, 0.5f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_hand",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, -90f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_foot",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(1.5f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_foot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-360f, 0f, -90f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_foot",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(-1.5f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_foot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-360f, 0f, -270f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head_rot",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, -1.6f, -1f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head_rot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head_rot",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.createScalingVector(1f, 0.9f, 1f),
                                    Transformation.Interpolations.LINEAR))).build();

    public static final Animation ANIM_HAMSTER_UNSQUISH = Animation.Builder.create(0.25f)
            .addBoneAnimation("root",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createTranslationalVector(0f, 1f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("root",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.createScalingVector(1.3f, 0.4f, 1.2f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createScalingVector(0.8f, 1.2f, 0.8f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("body",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, -0.5f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, -1.6f, -1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.createScalingVector(1f, 0.9f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_hand",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(-1.5f, 0.5f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_hand",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 90f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_hand",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(1.5f, 0.5f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_hand",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, -90f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_foot",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(1.5f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_foot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-360f, 0f, -90f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_foot",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(-1.5f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_foot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-360f, 0f, -270f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.1f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR))).build();

    public static final Animation ANIM_HAMSTER_BABY = Animation.Builder.create(0f).looping()
            .addBoneAnimation("head",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.createScalingVector(1.4f, 1.4f, 1.4f),
                                    Transformation.Interpolations.LINEAR))).build();
    public static final Animation ANIM_HAMSTER_PINKIE_WALK = Animation.Builder.create(0.7f).looping()
            .addBoneAnimation("tail",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(8.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("body",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, -0.75f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("body",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 6.0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_hand",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(10.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_hand",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-10.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_foot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(14.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_foot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-14.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("rightEar",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(4.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("leftEar",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-4.0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head_rot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 5.5f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("root",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.15f, AnimationHelper.createTranslationalVector(0f, 1f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.25f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.35f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5f, AnimationHelper.createTranslationalVector(0f, 1f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.6f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("root",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.15f, AnimationHelper.createRotationalVector(0f, 0f, 22.5f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.35f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(0f, 0f, -22.5f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.7f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("root",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.15f, AnimationHelper.createScalingVector(0.9f, 1.2f, 0.9f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.25f, AnimationHelper.createScalingVector(1.5f, 0.9f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.35f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5f, AnimationHelper.createScalingVector(0.9f, 1.2f, 0.9f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.6f, AnimationHelper.createScalingVector(1.5f, 0.9f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.7f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.CUBIC))).build();
}
