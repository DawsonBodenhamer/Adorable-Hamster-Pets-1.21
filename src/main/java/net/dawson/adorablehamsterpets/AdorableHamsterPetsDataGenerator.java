package net.dawson.adorablehamsterpets;

import net.dawson.adorablehamsterpets.datagen.*;
import net.dawson.adorablehamsterpets.world.gen.feature.ModConfiguredFeatures;
import net.dawson.adorablehamsterpets.world.gen.feature.ModPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class AdorableHamsterPetsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		// Keep existing providers
		pack.addProvider(ModLootTableProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModRecipeProvider::new);

		// [ADDED] Add provider for dynamic registries (features)
		pack.addProvider(ModWorldGenerator::new);
	}

	// [ADDED] Inner class for dynamic registry provider
	private static class ModWorldGenerator extends FabricDynamicRegistryProvider {
		public ModWorldGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
			// Add entries for configured and placed features using their bootstrap methods
			entries.addAll(registries.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE));
			entries.addAll(registries.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE));
		}

		@Override
		public String getName() {
			return "World Gen";
		}
	}

	// [ADDED] Override buildRegistry to register our bootstrap methods
	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
	}
}