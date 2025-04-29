package net.dawson.adorablehamsterpets;

import net.dawson.adorablehamsterpets.attachment.ModEntityAttachments;
import net.dawson.adorablehamsterpets.block.ModBlocks;
import net.dawson.adorablehamsterpets.component.ModDataComponentTypes;
import net.dawson.adorablehamsterpets.config.ModConfig;
import net.dawson.adorablehamsterpets.config.ModConfigModel;
import net.dawson.adorablehamsterpets.entity.ModEntities;
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.dawson.adorablehamsterpets.item.ModItemGroups;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.dawson.adorablehamsterpets.networking.payload.StartHamsterFlightSoundPayload;
import net.dawson.adorablehamsterpets.networking.payload.StartHamsterThrowSoundPayload;
import net.dawson.adorablehamsterpets.screen.ModScreenHandlers;
import net.dawson.adorablehamsterpets.sound.ModSounds;
import net.dawson.adorablehamsterpets.world.ModWorldGeneration;
import net.dawson.adorablehamsterpets.world.gen.ModEntitySpawns;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.dawson.adorablehamsterpets.networking.payload.ThrowHamsterPayload;



public class AdorableHamsterPets implements ModInitializer {
	public static final String MOD_ID = "adorablehamsterpets";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final int HAMSTER_THROW_COOLDOWN_TICKS = 2 * 60 * 20;


	// --- 1. Define Packet ID ---
	public static final Identifier THROW_HAMSTER_PACKET_ID = Identifier.of(MOD_ID, "throw_hamster");
	// --- End Packet ID ---


	@Override
	public void onInitialize() {
		ModDataComponentTypes.registerDataComponentTypes();
		ModSounds.registerSounds();
		ModBlocks.registerModBlocks();
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ModEntities.registerModEntities();
		ModEntityAttachments.registerAttachments();
		ModScreenHandlers.registerScreenHandlers();
		ModWorldGeneration.generateModWorldGen();
		ModEntitySpawns.addSpawns();
		PayloadTypeRegistry.playC2S().register(ThrowHamsterPayload.ID, ThrowHamsterPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(StartHamsterFlightSoundPayload.ID, StartHamsterFlightSoundPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(StartHamsterThrowSoundPayload.ID, StartHamsterThrowSoundPayload.CODEC);
		registerC2SPackets();
		CompostingChanceRegistry.INSTANCE.add(ModItems.GREEN_BEANS, 0.5f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.CUCUMBER, 0.5f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.GREEN_BEAN_SEEDS, 0.25f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.CUCUMBER_SEEDS, 0.25f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.SUNFLOWER_SEEDS, 0.25f);
		FabricDefaultAttributeRegistry.register(ModEntities.HAMSTER, HamsterEntity.createHamsterAttributes());
		ModConfig.createAndLoad();
	}


	// --- 2. Register Server-Side Packet Receiver ---
	public static void registerC2SPackets() {
		// --- FIX: Register for the specific Payload Type and use correct lambda signature ---
		ServerPlayNetworking.registerGlobalReceiver(ThrowHamsterPayload.ID, // Use the Payload ID here
				(ThrowHamsterPayload payload, ServerPlayNetworking.Context context) -> { // Correct lambda signature
					// Access server and player via context
					MinecraftServer server = context.server();
					ServerPlayerEntity player = context.player();

					// Execute on the server thread to ensure thread safety
					server.execute(() -> {
						handleThrowHamsterPacket(player); // Pass the player from the context
					});
				});
		// --- END FIX ---
		LOGGER.info("Registered C2S Packet Receiver for Payload: {}", ThrowHamsterPayload.ID.id()); // Log the actual ID
	}
	// --- End Receiver Registration ---

	// --- 3. Handle the Received Packet ---
	private static void handleThrowHamsterPacket(ServerPlayerEntity player) {
		// This method is called when the server receives the THROW_HAMSTER_PACKET_ID
		// It calls the actual throw logic which we'll put in HamsterEntity
		HamsterEntity.tryThrowFromShoulder(player);
	}
	// --- End Packet Handler ---

}