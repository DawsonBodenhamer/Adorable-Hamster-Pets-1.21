package net.dawson.adorablehamsterpets.entity.client.feature;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
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
import org.jetbrains.annotations.Nullable;

public class HamsterShoulderFeatureRenderer
        extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private final HamsterShoulderModel hamsterShoulderModel;
    // Reference for closed eyes
    @Nullable private final ModelPart closedEyesPart;

    // Constants for scale and position (Keep these as they are)
    private static final float BABY_SCALE = 0.5f;
    private static final float ADULT_SCALE = 0.8f;
    private static final float BABY_Y_OFFSET_SNEAKING = -0.55F;
    private static final float BABY_Y_OFFSET_STANDING = -0.75F;
    private static final float ADULT_Y_OFFSET_SNEAKING = -0.85F;
    private static final float ADULT_Y_OFFSET_STANDING = -1.13F;

    public HamsterShoulderFeatureRenderer(
            FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context,
            EntityModelLoader modelLoader
    ) {
        super(context);
        this.hamsterShoulderModel = new HamsterShoulderModel(modelLoader.getModelPart(ModModelLayers.HAMSTER_SHOULDER_LAYER));
        // --- Get closed eyes part reference ---
        // Attempt to get the part, handle potential errors
        ModelPart tempClosedEyes = null;
        try {
            // Adjust path if necessary based on HamsterShoulderModel structure
            tempClosedEyes = this.hamsterShoulderModel.root.getChild("root")
                    .getChild("body")
                    .getChild("head_parent")
                    .getChild("head_child")
                    .getChild("closed_eyes");
        } catch (Exception e) {
            AdorableHamsterPets.LOGGER.error("[ShoulderRender] Failed to get closed_eyes ModelPart in constructor. Eyes may not hide.", e);
        }
        this.closedEyesPart = tempClosedEyes;
        // --- End get reference ---
    }

    // --- Helper to get texture identifier ---
    private Identifier getTextureId(String textureName) {
        return Identifier.of(AdorableHamsterPets.MOD_ID, "textures/entity/hamster/" + textureName + ".png");
    }
    // --- End Helper ---

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        HamsterShoulderData shoulderData = player.getAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA);

        if (shoulderData == null) {
            return;
        }

        // --- Set Cheek Visibility ---
        setCheekVisibility(shoulderData);

        // --- Hide Closed Eyes ---
        if (this.closedEyesPart != null) {
            this.closedEyesPart.visible = false; // Always hide closed eyes on shoulder
        }
        // --- End Hide Closed Eyes ---

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

        // --- Determine Scale and Position based on Age (Keep this) ---
        boolean isBaby = shoulderData.breedingAge() < 0;
        float scaleFactor = isBaby ? BABY_SCALE : ADULT_SCALE;
        float yOffset = player.isInSneakingPose()
                ? (isBaby ? BABY_Y_OFFSET_SNEAKING : ADULT_Y_OFFSET_SNEAKING)
                : (isBaby ? BABY_Y_OFFSET_STANDING : ADULT_Y_OFFSET_STANDING);
        float xOffset = leftShoulder ? 0.4F : -0.4F;

        matrices.translate(xOffset, yOffset, 0.0F);
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        // --- End Scale and Position Adjustment ---

        // --- Get Variant and Texture Names ---
        HamsterVariant variant = HamsterVariant.byId(shoulderData.variantId());
        String baseTextureName = variant.getBaseTextureName();
        @Nullable String overlayTextureName = variant.getOverlayTextureName(); // Can be null

        Identifier baseTextureId = getTextureId(baseTextureName);
        // --- End Get Variant ---

        // --- Render Base Model ---
        RenderLayer baseRenderLayer = RenderLayer.getEntityCutoutNoCull(baseTextureId);
        this.hamsterShoulderModel.render(matrices, vertexConsumers.getBuffer(baseRenderLayer), light, OverlayTexture.DEFAULT_UV);
        // --- End Render Base Model ---

        // --- Render Overlay Model (if applicable) ---
        if (overlayTextureName != null) {
            Identifier overlayTextureId = getTextureId(overlayTextureName);
            RenderLayer overlayRenderLayer = RenderLayer.getEntityTranslucent(overlayTextureId);
            this.hamsterShoulderModel.render(matrices, vertexConsumers.getBuffer(overlayRenderLayer), light, OverlayTexture.DEFAULT_UV);
        }
        // --- End Render Overlay Model ---

        matrices.pop(); // Restore matrix state
    }


    // --- Helper method to set cheek visibility (Keep this) ---
    private void setCheekVisibility(HamsterShoulderData data) {
        boolean leftCheekFull = data.leftCheekFull();
        boolean rightCheekFull = data.rightCheekFull();

        // Use direct access from HamsterShoulderModel instance
        if (this.hamsterShoulderModel.left_cheek_deflated != null && this.hamsterShoulderModel.left_cheek_inflated != null &&
                this.hamsterShoulderModel.right_cheek_deflated != null && this.hamsterShoulderModel.right_cheek_inflated != null) {
            this.hamsterShoulderModel.left_cheek_deflated.visible = !leftCheekFull;
            this.hamsterShoulderModel.left_cheek_inflated.visible = leftCheekFull;
            this.hamsterShoulderModel.right_cheek_deflated.visible = !rightCheekFull;
            this.hamsterShoulderModel.right_cheek_inflated.visible = rightCheekFull;
        } else {
            AdorableHamsterPets.LOGGER.error("[ShoulderRender] One or more cheek model parts are null in HamsterShoulderModel!");
        }
    }
}