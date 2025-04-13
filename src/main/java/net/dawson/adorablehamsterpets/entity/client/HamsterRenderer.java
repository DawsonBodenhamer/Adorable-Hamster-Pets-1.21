package net.dawson.adorablehamsterpets.entity.client;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.entity.client.layer.HamsterOverlayLayer; // Import the new layer
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.dawson.adorablehamsterpets.entity.custom.HamsterVariant;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
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
}