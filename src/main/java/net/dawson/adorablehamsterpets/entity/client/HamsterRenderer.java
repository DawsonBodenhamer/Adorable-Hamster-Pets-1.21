package net.dawson.adorablehamsterpets.entity.client;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class HamsterRenderer extends MobEntityRenderer<HamsterEntity, HamsterModel<HamsterEntity>> {
    public HamsterRenderer(EntityRendererFactory.Context context) {
        super(context, new HamsterModel<>(context.getPart(HamsterModel.HAMSTER)), 0.15f);
    }

    @Override
    public Identifier getTexture(HamsterEntity entity) {
        return Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/hamster.png");
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
