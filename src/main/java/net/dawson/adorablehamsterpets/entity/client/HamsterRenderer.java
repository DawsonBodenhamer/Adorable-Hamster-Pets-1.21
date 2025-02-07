package net.dawson.adorablehamsterpets.entity.client;

import com.google.common.collect.Maps;
import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.dawson.adorablehamsterpets.entity.custom.HamsterVariant;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.Map;

public class HamsterRenderer extends MobEntityRenderer<HamsterEntity, HamsterModel<HamsterEntity>> {
    private static final Map<HamsterVariant, Identifier> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(HamsterVariant.class), map -> {
                map.put(HamsterVariant.DEFAULT, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/default.png"));
                map.put(HamsterVariant.BLACK, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/black.png"));
                map.put(HamsterVariant.WHITE, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/white.png"));
                map.put(HamsterVariant.CHOCOLATE, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/chocolate.png"));
                map.put(HamsterVariant.CREAM, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/cream.png"));
                map.put(HamsterVariant.DOVE, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/dove.png"));
                map.put(HamsterVariant.SILVER_DOVE, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/silver_dove.png"));

                map.put(HamsterVariant.BLACK_BANDED, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/black_banded.png"));
                map.put(HamsterVariant.BLACK_ROAN, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/black_roan.png"));
                map.put(HamsterVariant.BLACK_SPOTTED, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/black_spotted.png"));
                map.put(HamsterVariant.BLACK_WHITEBELLY, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/black_whitebelly.png"));

                map.put(HamsterVariant.CHOCOLATE_BANDED, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/chocolate_banded.png"));
                map.put(HamsterVariant.CHOCOLATE_ROAN, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/chocolate_roan.png"));
                map.put(HamsterVariant.CHOCOLATE_SPOTTED, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/chocolate_spotted.png"));
                map.put(HamsterVariant.CHOCOLATE_WHITEBELLY, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/chocolate_whitebelly.png"));

                map.put(HamsterVariant.CREAM_BANDED, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/cream_banded.png"));
                map.put(HamsterVariant.CREAM_ROAN, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/cream_roan.png"));
                map.put(HamsterVariant.CREAM_SPOTTED, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/cream_spotted.png"));
                map.put(HamsterVariant.CREAM_WHITEBELLY, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/cream_whitebelly.png"));

                map.put(HamsterVariant.DEFAULT_BANDED, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/default_banded.png"));
                map.put(HamsterVariant.DEFAULT_ROAN, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/default_roan.png"));
                map.put(HamsterVariant.DEFAULT_SPOTTED, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/default_spotted.png"));
                map.put(HamsterVariant.DEFAULT_WHITEBELLY, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/default_whitebelly.png"));

                map.put(HamsterVariant.DOVE_BANDED, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/dove_banded.png"));
                map.put(HamsterVariant.DOVE_ROAN, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/dove_roan.png"));
                map.put(HamsterVariant.DOVE_SPOTTED, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/dove_spotted.png"));
                map.put(HamsterVariant.DOVE_WHITEBELLY, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/dove_whitebelly.png"));

                map.put(HamsterVariant.SILVER_DOVE_BANDED, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/silver_dove_banded.png"));
                map.put(HamsterVariant.SILVER_DOVE_ROAN, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/silver_dove_roan.png"));
                map.put(HamsterVariant.SILVER_DOVE_SPOTTED, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/silver_dove_spotted.png"));
                map.put(HamsterVariant.SILVER_DOVE_WHITEBELLY, Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/silver_dove_whitebelly.png"));
            });

    public HamsterRenderer(EntityRendererFactory.Context context) {
        super(context, new HamsterModel<>(context.getPart(HamsterModel.HAMSTER)), 0.15f);
    }

    @Override
    public Identifier getTexture(HamsterEntity entity) {
        return LOCATION_BY_VARIANT.get(entity.getVariant());
    }

    @Override
    public void render(HamsterEntity livingEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        if(livingEntity.isBaby()) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }

        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
