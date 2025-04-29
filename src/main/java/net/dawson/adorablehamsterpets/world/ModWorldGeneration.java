package net.dawson.adorablehamsterpets.world;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
// Keep ModPlacedFeatures import
import net.dawson.adorablehamsterpets.world.gen.feature.ModPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags; // Keep this import for Cucumber Bush tag selector
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;

public class ModWorldGeneration {

    public static void generateModWorldGen() {
        AdorableHamsterPets.LOGGER.info("Registering Biome Modifications for " + AdorableHamsterPets.MOD_ID);

        // --- Sunflower Replacement Logic (Existing - Keep As Is) ---
        RegistryKey<PlacedFeature> vanillaSunflowerPatch = VegetationPlacedFeatures.PATCH_SUNFLOWER;
        BiomeModifications.create(Identifier.of(AdorableHamsterPets.MOD_ID, "remove_vanilla_sunflower_universal"))
                .add(ModificationPhase.REMOVALS,
                        BiomeSelectors.foundInOverworld(),
                        context -> {
                            context.getGenerationSettings().removeFeature(vanillaSunflowerPatch);
                        });
        var customSunflowerSpawnSelector = BiomeSelectors.includeByKey(BiomeKeys.SUNFLOWER_PLAINS);
        BiomeModifications.create(Identifier.of(AdorableHamsterPets.MOD_ID, "add_custom_sunflower_specific"))
                .add(ModificationPhase.ADDITIONS,
                        customSunflowerSpawnSelector,
                        context -> {
                            context.getGenerationSettings().addFeature(
                                    GenerationStep.Feature.VEGETAL_DECORATION,
                                    ModPlacedFeatures.CUSTOM_SUNFLOWER_PLACED_KEY
                            );
                        });
        // --- End Sunflower Replacement Logic ---


        // --- Add Wild Bush Generation ---
        addWildGreenBeanBushGeneration();
        addWildCucumberBushGeneration();
        // --- End Wild Bush Generation ---

        // Other world gen modifications can go here in the future
    }

    // --- Private helper methods for bush generation ---
    private static void addWildGreenBeanBushGeneration() {
        BiomeModifications.addFeature(
                // Biome Selector: Target temperate/moist biomes
                BiomeSelectors.includeByKey(
                        BiomeKeys.FOREST,
                        BiomeKeys.BIRCH_FOREST,
                        BiomeKeys.DARK_FOREST,
                        BiomeKeys.FLOWER_FOREST,
                        BiomeKeys.SWAMP,
                        BiomeKeys.LUSH_CAVES
                ),
                // Generation Step: Place with other vegetation
                GenerationStep.Feature.VEGETAL_DECORATION,
                // Placed Feature Key: The key we defined in ModPlacedFeatures
                ModPlacedFeatures.WILD_GREEN_BEAN_BUSH_PLACED_KEY
        );
    }

    private static void addWildCucumberBushGeneration() {
        BiomeModifications.addFeature(
                // Biome Selector: Target warm/sunny biomes
                BiomeSelectors.includeByKey(
                        BiomeKeys.PLAINS,
                        BiomeKeys.SUNFLOWER_PLAINS,
                        BiomeKeys.SAVANNA,
                        BiomeKeys.SAVANNA_PLATEAU,
                        BiomeKeys.JUNGLE,
                        BiomeKeys.SPARSE_JUNGLE
                ),
                // Generation Step: Place with other vegetation
                GenerationStep.Feature.VEGETAL_DECORATION,
                // Placed Feature Key: The key we defined in ModPlacedFeatures
                ModPlacedFeatures.WILD_CUCUMBER_BUSH_PLACED_KEY
        );
    }
}