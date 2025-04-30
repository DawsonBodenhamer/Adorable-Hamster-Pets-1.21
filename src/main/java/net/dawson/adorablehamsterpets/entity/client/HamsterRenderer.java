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
import software.bernie.geckolib.cache.object.BakedGeoModel;
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
    // --- Override preRender ---
    @Override
    public void preRender(MatrixStack poseStack, HamsterEntity animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        // --- Call before model rendering. Access the target bone here as requested. ---

        // 1. Call the superclass method FIRST to ensure base functionality runs
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

        // 2. Access the 'left_foot' bone from the provided model instance
        // Not storing it or doing anything with it, just accessing it.
        // Added logging to confirm this happens for the correct entity.
        try {
            model.getBone("left_foot"); // Attempt to get the bone
        } catch (Exception e) {
            // Log if the bone couldn't be found, which would indicate a model/locator name issue
            AdorableHamsterPets.LOGGER.info("[HamsterRenderer PreRender] Failed to get 'left_foot' bone for entity {} in preRender!", animatable.getId(), e);
        }
    }
}