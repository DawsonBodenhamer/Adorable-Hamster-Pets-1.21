package net.dawson.adorablehamsterpets.entity.client;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.entity.client.layer.HamsterOverlayLayer;
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.dawson.adorablehamsterpets.entity.custom.HamsterVariant;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HamsterRenderer extends GeoEntityRenderer<HamsterEntity> {

    // --- 1. Store the default adult shadow radius ---
    private final float adultShadowRadius;
    // --- End 1. ---

    public HamsterRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new HamsterModel());
        // --- 2. Initialize adultShadowRadius and set initial shadowRadius ---
        this.adultShadowRadius = 0.2F;
        this.shadowRadius = this.adultShadowRadius;
        // --- End 2. ---

        addRenderLayer(new HamsterOverlayLayer(this));
    }

    @Override
    public Identifier getTextureLocation(HamsterEntity entity) {
        HamsterVariant variant = HamsterVariant.byId(entity.getVariant());
        String baseTextureName = variant.getBaseTextureName();
        return Identifier.of(
                AdorableHamsterPets.MOD_ID,
                "textures/entity/hamster/" + baseTextureName + ".png"
        );
    }

    // --- 3. Override render method for dynamic shadow radius ---
    @Override
    public void render(HamsterEntity entity, float entityYaw, float partialTick, MatrixStack poseStack,
                       VertexConsumerProvider bufferSource, int packedLight) {
        // --- 3a. Adjust shadow radius for babies ---
        if (entity.isBaby()) {
            this.shadowRadius = this.adultShadowRadius * 0.5f;
        } else {
            this.shadowRadius = this.adultShadowRadius;
        }
        // --- End 3a. ---

        // --- 3b. Call super.render() ---
        // This will use the temporarily modified this.shadowRadius
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        // --- End 3b. ---
    }
    // --- End 3. ---

    @Override
    public void preRender(MatrixStack poseStack, HamsterEntity animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

        // Bone access for Geckolib issue debugging (can be kept or removed)
        try {
            GeoBone bone = model.getBone("left_foot").orElse(null);
            if (bone != null) {
                Vector3d ignoredPosition = bone.getWorldPosition();
            } else {
                // AdorableHamsterPets.LOGGER.info("[HamsterRenderer PreRender] Could not find 'left_foot' bone for entity {} in preRender!", animatable.getId());
            }
        } catch (Exception e) {
            // AdorableHamsterPets.LOGGER.info("[HamsterRenderer PreRender] Exception while accessing bone/position for entity {} in preRender!", animatable.getId(), e);
        }
    }
}