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
        return Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/default.png");
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


    /*
     * ============= Animation File =============
     * This one is only declared once in your base class (abstract).
     * There's no 'deprecated' second version for this, so we only have to do it once.
     */
    @Override
    public Identifier getAnimationResource(HamsterEntity animatable) {
        return Identifier.of(AdorableHamsterPets.MOD_ID, "animations/anim_hamster.json");
    }

    /*
     * ============= Optional Sleep-Bone Logic =============
     */
    @Override
    public void setCustomAnimations(HamsterEntity entity, long instanceId, AnimationState<HamsterEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        GeoBone sleepBone = this.getAnimationProcessor().getBone("sleep");
        if (sleepBone != null) {
            sleepBone.setHidden(!entity.isSleeping());

        }

        // Logic for cheek bones:

        boolean cheeksEmpty = entity.isCheekPouchEmpty();

        GeoBone leftCheekDef = this.getAnimationProcessor().getBone("left_cheek_deflated");
        GeoBone rightCheekDef = this.getAnimationProcessor().getBone("right_cheek_deflated");
        GeoBone leftCheekInf = this.getAnimationProcessor().getBone("left_cheek_inflated");
        GeoBone rightCheekInf = this.getAnimationProcessor().getBone("right_cheek_inflated");

        if (leftCheekDef != null && rightCheekDef != null && leftCheekInf != null && rightCheekInf != null) {
            leftCheekDef.setHidden(!cheeksEmpty);
            rightCheekDef.setHidden(!cheeksEmpty);

            leftCheekInf.setHidden(cheeksEmpty);
            rightCheekInf.setHidden(cheeksEmpty);
        }
    }
}
