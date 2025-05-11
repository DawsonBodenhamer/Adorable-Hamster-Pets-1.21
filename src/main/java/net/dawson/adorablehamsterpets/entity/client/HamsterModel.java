package net.dawson.adorablehamsterpets.entity.client;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

@SuppressWarnings("removal") // Suppress deprecation warnings for the old abstract methods
public class HamsterModel extends GeoModel<HamsterEntity> {

    // --- 1. Constants for Scaling and Positioning ---
    private static final float ADULT_SCALE = 1.0f;
    private static final float BABY_SCALE = 0.5f;
    private static final float BABY_HEAD_SCALE = 1.2f; // Your preferred value
    // --- End 1. Constants ---

    @Override
    public Identifier getModelResource(HamsterEntity animatable, @Nullable GeoRenderer<HamsterEntity> renderer) {
        return Identifier.of(AdorableHamsterPets.MOD_ID, "geo/hamster.geo.json");
    }

    @Override
    public Identifier getTextureResource(HamsterEntity animatable, @Nullable GeoRenderer<HamsterEntity> renderer) {
        return Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/orange.png"); // Fallback
    }

    @Deprecated(forRemoval = true)
    @Override
    public Identifier getModelResource(HamsterEntity animatable) {
        return this.getModelResource(animatable, null);
    }

    @Deprecated(forRemoval = true)
    @Override
    public Identifier getTextureResource(HamsterEntity animatable) {
        return this.getTextureResource(animatable, null);
    }

    @Override
    public Identifier getAnimationResource(HamsterEntity animatable) {
        return Identifier.of(AdorableHamsterPets.MOD_ID, "animations/anim_hamster.json");
    }

    @Override
    public void setCustomAnimations(HamsterEntity entity, long instanceId, AnimationState<HamsterEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        // --- Bone References ---
        GeoBone rootBone = this.getAnimationProcessor().getBone("root");
        GeoBone bodyParentBone = this.getAnimationProcessor().getBone("body_parent");
        GeoBone headParentBone = this.getAnimationProcessor().getBone("head_parent");
        GeoBone closedEyesBone = this.getAnimationProcessor().getBone("closed_eyes");
        GeoBone leftCheekDefBone = this.getAnimationProcessor().getBone("left_cheek_deflated");
        GeoBone rightCheekDefBone = this.getAnimationProcessor().getBone("right_cheek_deflated");
        GeoBone leftCheekInfBone = this.getAnimationProcessor().getBone("left_cheek_inflated");
        GeoBone rightCheekInfBone = this.getAnimationProcessor().getBone("right_cheek_inflated");
        // --- End Bone References ---

        // --- Blinking Logic ---
        if (closedEyesBone != null) {
            int currentBlinkTimer = entity.getBlinkTimer();
            boolean isBlinkingClosed = currentBlinkTimer > 0 &&
                    (currentBlinkTimer <= 2 || currentBlinkTimer >= 5);
            boolean showClosedEyes = entity.isSleeping() || isBlinkingClosed || entity.isKnockedOut();
            closedEyesBone.setHidden(!showClosedEyes);
        }
        // --- End Blinking Logic ---

        // --- Cheek Pouch Visibility Logic ---
        if (leftCheekDefBone != null && leftCheekInfBone != null) {
            boolean leftFull = entity.isLeftCheekFull();
            leftCheekDefBone.setHidden(leftFull);
            leftCheekInfBone.setHidden(!leftFull);
        }
        if (rightCheekDefBone != null && rightCheekInfBone != null) {
            boolean rightFull = entity.isRightCheekFull();
            rightCheekDefBone.setHidden(rightFull);
            rightCheekInfBone.setHidden(!rightFull);
        }
        // --- End Cheek Pouch Logic ---

        // --- 2. Baby/Adult Scaling Logic ---
        if (rootBone != null && bodyParentBone != null && headParentBone != null) {
            if (entity.isBaby()) {
                // --- 2a. Baby Scaling ---
                // Root bone remains at 1.0f scale for babies, acting as a neutral parent.
                rootBone.setScaleX(BABY_SCALE);
                rootBone.setScaleY(BABY_SCALE);
                rootBone.setScaleZ(BABY_SCALE);

                // Scale up head slightly.
                headParentBone.setScaleX(BABY_HEAD_SCALE);
                headParentBone.setScaleY(BABY_HEAD_SCALE);
                headParentBone.setScaleZ(BABY_HEAD_SCALE);
                // --- End 2a. Baby Scaling ---
            } else {
                // --- 2b. Adult Scaling (Explicit Reset) ---
                rootBone.setScaleX(ADULT_SCALE);
                rootBone.setScaleY(ADULT_SCALE);
                rootBone.setScaleZ(ADULT_SCALE);

                headParentBone.setScaleX(ADULT_SCALE);
                headParentBone.setScaleY(ADULT_SCALE);
                headParentBone.setScaleZ(ADULT_SCALE);

                // --- End 2b. Adult Scaling ---
            }
        }
        // --- End 2. Baby/Adult Scaling Logic ---
    }
}