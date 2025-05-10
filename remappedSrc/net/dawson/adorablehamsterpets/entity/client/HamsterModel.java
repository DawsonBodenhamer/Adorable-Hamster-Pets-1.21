package net.dawson.adorablehamsterpets.entity.client;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

@SuppressWarnings("removal") // Suppress deprecation warnings for the old abstract methods
public class HamsterModel extends GeoModel<HamsterEntity> {

    /*
     * ============= NEW Methods (non-abstract) =============
     * These are not strictly required to override, but you likely do want them,
     * so that your code uses them if the library calls them.
     */

    @Override
    public Identifier getModelResource(HamsterEntity animatable, @Nullable GeoRenderer<HamsterEntity> renderer) {
        return Identifier.of(AdorableHamsterPets.MOD_ID, "geo/hamster.geo.json");
    }

    @Override
    public Identifier getTextureResource(HamsterEntity animatable, @Nullable GeoRenderer<HamsterEntity> renderer) {
        // This is not important since HamsterRenderer is taking care of this, but included for good measure.
        // Return a default or null. Let's return the orange one as a fallback.
        return Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/orange.png");
    }


    /*
     * ============= OLD Methods (abstract, deprecated) =============
     * You MUST implement these, or your class remains abstract.
     * They can simply forward to the new ones above.
     */

    @Deprecated(forRemoval = true)
    @Override
    public Identifier getModelResource(HamsterEntity animatable) {
        // Just pass 'null' for the renderer param if you don't have a real one
        return this.getModelResource(animatable, null);
    }

    @Deprecated(forRemoval = true)
    @Override
    public Identifier getTextureResource(HamsterEntity animatable) {
        // Same approach
        return this.getTextureResource(animatable, null);
    }


    @Override
    public Identifier getAnimationResource(HamsterEntity animatable) {
        return Identifier.of(AdorableHamsterPets.MOD_ID, "animations/anim_hamster.json");
    }


    @Override
    public void setCustomAnimations(HamsterEntity entity, long instanceId, AnimationState<HamsterEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        boolean isCurrentlyKnockedOut = entity.isKnockedOut();

        // --- Blinking Logic ---
        GeoBone closedEyesBone = this.getAnimationProcessor().getBone("closed_eyes");
        if (closedEyesBone != null) {
            int currentBlinkTimer = entity.getBlinkTimer(); // Get timer value
            boolean isBlinkingClosed = currentBlinkTimer > 0 &&
                    (currentBlinkTimer <= 2 || // Closed during ticks 1-2 (single blink AND second part of double blink)
                            currentBlinkTimer >= 5    // Closed during ticks 5-6 (first part of double blink)
                    );
            // Eyes are shown if sleeping OR if currently in a closed phase of blinking OR if knocked out
            boolean showClosedEyes = entity.isSleeping() || isBlinkingClosed || entity.isKnockedOut(); // Keep KO check here

            closedEyesBone.setHidden(!showClosedEyes); // Hide the bone if eyes should be open
        }
        // --- End Blinking Logic ---


        // --- Logic for cheek bones using TrackedData ---
        boolean leftFull = entity.isLeftCheekFull(); // Use new getter
        boolean rightFull = entity.isRightCheekFull(); // Use new getter


        GeoBone leftCheekDef = this.getAnimationProcessor().getBone("left_cheek_deflated");
        GeoBone rightCheekDef = this.getAnimationProcessor().getBone("right_cheek_deflated");
        GeoBone leftCheekInf = this.getAnimationProcessor().getBone("left_cheek_inflated");
        GeoBone rightCheekInf = this.getAnimationProcessor().getBone("right_cheek_inflated");


        if (leftCheekDef != null) leftCheekDef.setHidden(leftFull); // Hide deflated if full
        if (leftCheekInf != null) leftCheekInf.setHidden(!leftFull); // Hide inflated if not full


        if (rightCheekDef != null) rightCheekDef.setHidden(rightFull); // Hide deflated if full
        if (rightCheekInf != null) rightCheekInf.setHidden(!rightFull); // Hide inflated if not full
        // --- END CHEEK BONE LOGIC ---


        // --- BABY SCALING LOGIC ---
        GeoBone rootBone = this.getAnimationProcessor().getBone("root");
        if (rootBone != null) {
            final float adultScale = 1.0f; // Define adult scale constant for clarity
            final float babyScale = 0.5f;  // Define baby scale constant


            if (entity.isBaby()) {
                // Apply baby scale ONLY if it's a baby
                rootBone.setScaleX(babyScale);
                rootBone.setScaleY(babyScale);
                rootBone.setScaleZ(babyScale);
            } else {
                // Apply adult scale if it's NOT a baby, regardless of KO/waking state
                rootBone.setScaleX(adultScale); // Explicitly set adult scale
                rootBone.setScaleY(adultScale);
                rootBone.setScaleZ(adultScale);
                // The crash/ko/wakeup animations will still scale the body_parent relative to this root scale.
            }
        }
        // --- END BABY SCALING LOGIC ---
    }
}