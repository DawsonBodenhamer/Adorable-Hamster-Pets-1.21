package net.dawson.adorablehamsterpets;

import net.dawson.adorablehamsterpets.block.ModBlocks;
import net.dawson.adorablehamsterpets.client.option.ModKeyBindings;
import net.dawson.adorablehamsterpets.client.sound.HamsterThrowSoundInstance;
import net.dawson.adorablehamsterpets.config.ModConfig;
import net.dawson.adorablehamsterpets.entity.ModEntities;
import net.dawson.adorablehamsterpets.entity.client.HamsterRenderer;
import net.dawson.adorablehamsterpets.entity.client.ModModelLayers;
import net.dawson.adorablehamsterpets.entity.client.model.HamsterShoulderModel;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.dawson.adorablehamsterpets.networking.payload.StartHamsterThrowSoundPayload;
import net.dawson.adorablehamsterpets.screen.HamsterInventoryScreen;
import net.dawson.adorablehamsterpets.screen.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.dawson.adorablehamsterpets.client.sound.HamsterFlightSoundInstance;
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.dawson.adorablehamsterpets.networking.payload.StartHamsterFlightSoundPayload;
import net.dawson.adorablehamsterpets.sound.ModSounds;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.dawson.adorablehamsterpets.attachment.ModEntityAttachments; // Import attachments
import net.dawson.adorablehamsterpets.networking.payload.ThrowHamsterPayload; // Import C2S payload
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents; // Import tick event
import net.minecraft.client.option.GameOptions; // Import GameOptions
import net.minecraft.text.Text;
import net.minecraft.util.hit.HitResult;

public class AdorableHamsterPetsClient implements ClientModInitializer {

    // Flag to prevent sending multiple packets per throw action
    private int throwInputCooldown = 0;
    private static final int THROW_COOLDOWN_TICKS = 5; // Cooldown in ticks


    @Override
    public void onInitializeClient() {

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GREEN_BEANS_CROP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CUCUMBER_CROP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SUNFLOWER_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WILD_CUCUMBER_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WILD_GREEN_BEAN_BUSH, RenderLayer.getCutout());
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.HAMSTER_SHOULDER_LAYER, HamsterShoulderModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.HAMSTER, HamsterRenderer::new);
        HandledScreens.register(ModScreenHandlers.HAMSTER_INVENTORY_SCREEN_HANDLER, HamsterInventoryScreen::new);
        ModKeyBindings.registerKeyInputs();
        registerPacketHandlers();
        // --- Register Client Tick Event Handler ---
        ClientTickEvents.START_CLIENT_TICK.register(this::handleClientTick);
        // --- End Registration ---

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            return -1; // No tint
        }, ModItems.HAMSTER_SPAWN_EGG);
    }

    // --- Client Tick Handler Method ---
    private void handleClientTick(MinecraftClient client) {
        // Cooldown logic (if any) would go here first...
        // if (throwInputCooldown > 0) { throwInputCooldown--; return; }

        // Check if the throw key was pressed
        if (ModKeyBindings.THROW_HAMSTER_KEY.wasPressed()) {
            ClientPlayerEntity player = client.player;
            if (player == null) return; // Ensure player exists

            // --- Check Config Setting ---
            final ModConfig config = AdorableHamsterPets.CONFIG;
            if (!config.features.enableHamsterThrowing()) {
                // Send a message to the player explaining why it's disabled
                player.sendMessage(Text.literal("Hamster throwing is disabled in config."), true);
                return; // Stop if throwing is disabled
            }
            // --- End Check Config Setting ---

            // Check other conditions: not looking at block, has shoulder data
            boolean lookingAtReachableBlock = client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.BLOCK;
            boolean hasShoulderHamsterClient = player.getAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA) != null;

            if (!lookingAtReachableBlock && hasShoulderHamsterClient) {
                AdorableHamsterPets.LOGGER.debug("[ClientTick START] Throw key pressed! Sending packet.");
                ClientPlayNetworking.send(new ThrowHamsterPayload());
            }
        }
    }



    // --- Register S2C Packet Handler ---
    private void registerPacketHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(StartHamsterFlightSoundPayload.ID, (payload, context) -> {
            context.client().execute(() -> handleStartFlightSound(payload, context.client()));
        });
        ClientPlayNetworking.registerGlobalReceiver(StartHamsterThrowSoundPayload.ID, (payload, context) -> {
            context.client().execute(() -> handleStartThrowSound(payload, context.client()));
        });
        AdorableHamsterPets.LOGGER.debug("Registered S2C Packet Handlers");
    }
    // --- End Packet Handler Registration ---



    // --- Handle the S2C Packet ---
    private void handleStartFlightSound(StartHamsterFlightSoundPayload payload, MinecraftClient client) {
        // --- Add Logging ---
        AdorableHamsterPets.LOGGER.debug("[Client] Executing handleStartFlightSound on client thread for entity ID: {}", payload.hamsterEntityId());
        // --- End Logging ---

        if (client.world == null) {
            // --- Log specific method ---
            AdorableHamsterPets.LOGGER.warn("handleStartFlightSound: Client world is null!");
            // --- End Log ---
            return;
        }

        Entity entity = client.world.getEntityById(payload.hamsterEntityId());

        // --- Add Logging: Check if entity was found ---
        if (entity == null) {
            AdorableHamsterPets.LOGGER.warn("handleStartFlightSound: Could not find entity with ID: {}", payload.hamsterEntityId());
            return;
        }
        // --- End Logging ---

        if (entity instanceof HamsterEntity hamster) {
            // --- Add Logging: Confirm it's a HamsterEntity ---
            AdorableHamsterPets.LOGGER.debug("handleStartFlightSound: Found HamsterEntity instance for ID: {}", payload.hamsterEntityId());
            // --- End Logging ---


            SoundEvent flightSound = ModSounds.getRandomSoundFrom(ModSounds.HAMSTER_FLYING_SOUNDS, hamster.getRandom());

            // --- Add Logging: Check if sound event is valid ---
            if (flightSound == null) {
                AdorableHamsterPets.LOGGER.error("handleStartFlightSound: getRandomSoundFrom(HAMSTER_FLYING_SOUNDS) returned null!");
                return;
            }
            AdorableHamsterPets.LOGGER.debug("handleStartFlightSound: Selected flight sound: {}", flightSound.getId());
            // --- End Logging ---

            try {
                HamsterFlightSoundInstance soundInstance = new HamsterFlightSoundInstance(flightSound, SoundCategory.NEUTRAL, hamster);
                // --- Add Logging: Confirm sound instance creation ---
                AdorableHamsterPets.LOGGER.debug("handleStartFlightSound: Created HamsterFlightSoundInstance.");
                // --- End Logging ---

                client.getSoundManager().play(soundInstance);
                // --- Add Logging: Confirm play() was called ---
                AdorableHamsterPets.LOGGER.debug("handleStartFlightSound: Called soundManager.play() for hamster {}", payload.hamsterEntityId());
                // --- End Logging ---

            } catch (Exception e) {
                // --- Add Logging: Catch any unexpected errors during sound creation/play ---
                AdorableHamsterPets.LOGGER.error("handleStartFlightSound: Error creating or playing sound instance!", e);
                // --- End Logging ---
            }

        } else {
            // --- Add Logging: Entity found but wasn't a HamsterEntity ---
            AdorableHamsterPets.LOGGER.warn("handleStartFlightSound: Found entity for ID {}, but it was not a HamsterEntity (Type: {})", payload.hamsterEntityId(), entity.getType().getUntranslatedName());
            // --- End Logging ---
        }
    }
    // --- End Packet Handler ---

    // --- HANDLER for Throw Sound ---
    private void handleStartThrowSound(StartHamsterThrowSoundPayload payload, MinecraftClient client) {
        if (client.world == null) {
            AdorableHamsterPets.LOGGER.warn("Received StartHamsterThrowSoundPayload but client world is null!");
            return;
        }

        Entity entity = client.world.getEntityById(payload.hamsterEntityId());
        if (entity instanceof HamsterEntity hamster) {
            AdorableHamsterPets.LOGGER.debug("Found hamster {} for throw sound.", payload.hamsterEntityId());
            // Use the specific throw sound
            SoundEvent throwSound = ModSounds.HAMSTER_THROW;
            if (throwSound != null) {
                HamsterThrowSoundInstance soundInstance = new HamsterThrowSoundInstance(throwSound, SoundCategory.PLAYERS, hamster); // Use PLAYERS category like original
                client.getSoundManager().play(soundInstance);
                AdorableHamsterPets.LOGGER.debug("Playing HamsterThrowSoundInstance for hamster {}", payload.hamsterEntityId());
            } else {
                AdorableHamsterPets.LOGGER.error("HAMSTER_THROW sound event is null! Cannot play throw sound."); // Should not happen
            }
        } else {
            AdorableHamsterPets.LOGGER.warn("Received StartHamsterThrowSoundPayload for non-hamster or unknown entity ID: {}", payload.hamsterEntityId());
        }
    }
}