package net.dawson.adorablehamsterpets.entity.client.feature;

import net.minecraft.client.render.VertexConsumer;
import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.attachment.HamsterShoulderData;
import net.dawson.adorablehamsterpets.attachment.ModEntityAttachments;
import net.dawson.adorablehamsterpets.entity.client.ModModelLayers;
import net.dawson.adorablehamsterpets.entity.client.model.HamsterShoulderModel;
import net.dawson.adorablehamsterpets.entity.custom.HamsterVariant;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class HamsterShoulderFeatureRenderer
        extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private final HamsterShoulderModel hamsterShoulderModel;

    // Constants for scale and position
    private static final float BABY_SCALE = 0.5f;
    private static final float ADULT_SCALE = 0.8f;
    private static final float BABY_Y_OFFSET_SNEAKING = -0.55F; // never used because sneaking dismounts
    private static final float BABY_Y_OFFSET_STANDING = -0.75F;
    private static final float ADULT_Y_OFFSET_SNEAKING = -0.85F; // never used because sneaking dismounts
    private static final float ADULT_Y_OFFSET_STANDING = -1.13F;

    public HamsterShoulderFeatureRenderer(
            FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context,
            EntityModelLoader modelLoader
    ) {
        super(context);
        this.hamsterShoulderModel = new HamsterShoulderModel(modelLoader.getModelPart(ModModelLayers.HAMSTER_SHOULDER_LAYER));
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        HamsterShoulderData shoulderData = player.getAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA);

        if (shoulderData == null) {
            return;
        }

        // --- Set Cheek Visibility ---
        setCheekVisibility(shoulderData); // Call helper before rendering

        // --- Render on Right Shoulder ---
        renderShoulderHamster(matrices, vertexConsumers, light, player, false, shoulderData);
    }

    private void renderShoulderHamster(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            AbstractClientPlayerEntity player,
            boolean leftShoulder, // Currently unused, always renders on right
            HamsterShoulderData shoulderData
    ) {
        matrices.push();

        // --- Determine Scale and Position based on Age ---
        // --- FIX: Check breedingAge directly ---
        boolean isBaby = shoulderData.breedingAge() < 0; // Check if age is negative
        // --- END FIX ---
        float scaleFactor = isBaby ? BABY_SCALE : ADULT_SCALE;
        float yOffset = player.isInSneakingPose()
                ? (isBaby ? BABY_Y_OFFSET_SNEAKING : ADULT_Y_OFFSET_SNEAKING)
                : (isBaby ? BABY_Y_OFFSET_STANDING : ADULT_Y_OFFSET_STANDING);
        float xOffset = leftShoulder ? 0.4F : -0.4F; // Keep xOffset standard for now

        // Apply Translation
        matrices.translate(xOffset, yOffset, 0.0F);

        // Apply Scaling
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        // --- End Scale and Position Adjustment ---

        // Determine texture based on variant data
        HamsterVariant variant = HamsterVariant.byId(shoulderData.variantId());
        Identifier texture = Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/" + variant.name().toLowerCase() + ".png");

        // Get buffer and render the model
        VertexConsumer buffer = vertexConsumers.getBuffer(this.hamsterShoulderModel.getLayer(texture));
        this.hamsterShoulderModel.render(matrices, buffer, light, OverlayTexture.DEFAULT_UV);

        matrices.pop(); // Restore matrix state
    }



    // --- Helper method to set cheek visibility ---
    private void setCheekVisibility(HamsterShoulderData data) {
        boolean leftCheekFull = data.leftCheekFull(); // Get flag from data
        boolean rightCheekFull = data.rightCheekFull(); // Get flag from data

        // Set visibility on the model parts
        if (this.hamsterShoulderModel.left_cheek_deflated != null && this.hamsterShoulderModel.left_cheek_inflated != null &&
                this.hamsterShoulderModel.right_cheek_deflated != null && this.hamsterShoulderModel.right_cheek_inflated != null) { // Enhanced Null check
            this.hamsterShoulderModel.left_cheek_deflated.visible = !leftCheekFull;
            this.hamsterShoulderModel.left_cheek_inflated.visible = leftCheekFull;
            this.hamsterShoulderModel.right_cheek_deflated.visible = !rightCheekFull;
            this.hamsterShoulderModel.right_cheek_inflated.visible = rightCheekFull;
        } else {
            AdorableHamsterPets.LOGGER.error("[ShoulderRender] One or more cheek model parts are null in HamsterShoulderModel!");
        }
    }
}