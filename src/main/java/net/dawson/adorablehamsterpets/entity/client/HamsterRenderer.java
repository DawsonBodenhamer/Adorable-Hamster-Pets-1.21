package net.dawson.adorablehamsterpets.entity.client;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.entity.client.layer.HamsterOverlayLayer; // Import the new layer
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

    public HamsterRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new HamsterModel());
        this.shadowRadius = 0.2F;

        // --- ADD RENDER LAYER ---
        addRenderLayer(new HamsterOverlayLayer(this));
        // --- END ADD ---
    }

    @Override
    public Identifier getTextureLocation(HamsterEntity entity) {
        // --- Use Base Texture Name ---
        HamsterVariant variant = HamsterVariant.byId(entity.getVariant());
        String baseTextureName = variant.getBaseTextureName(); // Get base name from enum
        return Identifier.of(
                AdorableHamsterPets.MOD_ID,
                "textures/entity/hamster/" + baseTextureName + ".png"
        );
    }
    // --- MODIFIED: Override preRender ---
    @Override
    public void preRender(MatrixStack poseStack, HamsterEntity animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        // --- Description: Called before model rendering. Access the target bone AND its position here. ---

        // 1. Call the superclass method FIRST
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

        // 2. Access the 'left_foot' bone AND call getWorldPosition() on it
        try {
            // Get the bone instance from the model provided to this render call
            GeoBone bone = model.getBone("left_foot").orElse(null);
            if (bone != null) {
                Vector3d ignoredPosition = bone.getWorldPosition();

            } else {
                AdorableHamsterPets.LOGGER.info("[HamsterRenderer PreRender] Could not find 'left_foot' bone for entity {} in preRender!", animatable.getId());
            }
        } catch (Exception e) {
            // Catch potential exceptions during bone access/position calculation
            AdorableHamsterPets.LOGGER.info("[HamsterRenderer PreRender] Exception while accessing bone/position for entity {} in preRender!", animatable.getId(), e);
        }
    }
}