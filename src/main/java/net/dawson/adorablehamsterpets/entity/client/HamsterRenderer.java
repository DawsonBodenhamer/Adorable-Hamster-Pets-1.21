package net.dawson.adorablehamsterpets.entity.client;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.dawson.adorablehamsterpets.entity.custom.HamsterVariant;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier; // Yarn import
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HamsterRenderer extends GeoEntityRenderer<HamsterEntity> {

    public HamsterRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new HamsterModel());
        this.shadowRadius = 0.2F;
    }

    // This is the method the renderer calls to pick the final texture at runtime
    @Override
    public Identifier getTextureLocation(HamsterEntity entity) {
        // If you're on a version where the constructor is private, do:
        // Identifier.of(...)
        HamsterVariant variant = HamsterVariant.byId(entity.getVariant());
        String variantName = variant.name().toLowerCase(); // e.g. "black_banded"

        return Identifier.of(
                AdorableHamsterPets.MOD_ID,
                "textures/entity/hamster/" + variantName + ".png"
        );
    }
}
