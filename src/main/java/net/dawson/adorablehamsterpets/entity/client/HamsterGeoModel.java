package net.dawson.adorablehamsterpets.entity.client;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.dawson.adorablehamsterpets.entity.custom.HamsterVariant;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.AnimatedGeoModel;

public class HamsterGeoModel extends AnimatedGeoModel<HamsterEntity> {

    // NEW: Override the new method names introduced in GeckoLib 4.5
    @Override
    public Identifier getModelLocation(HamsterEntity hamster) {
        // This looks for the file at assets/adorablehamsterpets/geo/hamster.geo.json
        return new Identifier(AdorableHamsterPets.MOD_ID + ":geo/hamster.geo.json");
    }

    @Override
    public Identifier getTextureLocation(HamsterEntity hamster) {
        // Choose texture based on hamster variant.
        HamsterVariant variant = hamster.getVariant();
        return switch (variant) {
            case BLACK -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/black.png");
            case BLACK_BANDED -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/black_banded.png");
            case BLACK_ROAN -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/black_roan.png");
            case BLACK_SPOTTED -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/black_spotted.png");
            case BLACK_WHITEBELLY -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/black_whitebelly.png");
            case CHOCOLATE -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/chocolate.png");
            case CHOCOLATE_BANDED -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/chocolate_banded.png");
            case CHOCOLATE_ROAN -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/chocolate_roan.png");
            case CHOCOLATE_SPOTTED -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/chocolate_spotted.png");
            case CHOCOLATE_WHITEBELLY -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/chocolate_whitebelly.png");
            case CREAM -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/cream.png");
            case CREAM_BANDED -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/cream_banded.png");
            case CREAM_ROAN -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/cream_roan.png");
            case CREAM_SPOTTED -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/cream_spotted.png");
            case CREAM_WHITEBELLY -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/cream_whitebelly.png");
            case DEFAULT -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/default.png");
            case DEFAULT_BANDED -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/default_banded.png");
            case DEFAULT_ROAN -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/default_roan.png");
            case DEFAULT_SPOTTED -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/default_spotted.png");
            case DEFAULT_WHITEBELLY -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/default_whitebelly.png");
            case DOVE -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/dove.png");
            case DOVE_BANDED -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/dove_banded.png");
            case DOVE_ROAN -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/dove_roan.png");
            case DOVE_SPOTTED -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/dove_spotted.png");
            case DOVE_WHITEBELLY -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/dove_whitebelly.png");
            case SILVER_DOVE -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/silver_dove.png");
            case SILVER_DOVE_BANDED -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/silver_dove_banded.png");
            case SILVER_DOVE_ROAN -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/silver_dove_roan.png");
            case SILVER_DOVE_SPOTTED -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/silver_dove_spotted.png");
            case SILVER_DOVE_WHITEBELLY -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/silver_dove_whitebelly.png");
            case WHITE -> new Identifier(AdorableHamsterPets.MOD_ID + ":textures/entity/hamster/white.png");
        };
    }

    @Override
    public Identifier getAnimationFileLocation(HamsterEntity hamster) {
        // This looks for the file at assets/adorablehamsterpets/animations/anim_hamster.json
        return new Identifier(AdorableHamsterPets.MOD_ID + ":animations/anim_hamster.json");
    }
}
