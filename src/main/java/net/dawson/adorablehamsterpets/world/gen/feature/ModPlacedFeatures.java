package net.dawson.adorablehamsterpets.world.gen.feature;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.*;
import net.dawson.adorablehamsterpets.world.gen.feature.ModConfiguredFeatures;
import net.minecraft.world.gen.placementmodifier.*; // Import all placement modifiers

import java.util.List;

public class ModPlacedFeatures {

    public static final RegistryKey<PlacedFeature> CUSTOM_SUNFLOWER_PLACED_KEY = registerKey("custom_sunflower_placed");

    // --- Add Keys for Placed Bushes ---
    public static final RegistryKey<PlacedFeature> WILD_GREEN_BEAN_BUSH_PLACED_KEY = registerKey("wild_green_bean_bush_placed");
    public static final RegistryKey<PlacedFeature> WILD_CUCUMBER_BUSH_PLACED_KEY = registerKey("wild_cucumber_bush_placed");
    // --- End Add Keys ---

    public static void bootstrap(Registerable<PlacedFeature> context) {
        var configuredFeatureRegistryEntryLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        // Sunflower (Existing)
        register(context, CUSTOM_SUNFLOWER_PLACED_KEY,
                configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.CUSTOM_SUNFLOWER_PATCH_KEY), // Corrected reference
                RarityFilterPlacementModifier.of(3), // Sunflower rarity
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of()
        );

        // --- Register Placed Green Bean Bush ---
        register(context, WILD_GREEN_BEAN_BUSH_PLACED_KEY,
                configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.WILD_GREEN_BEAN_BUSH_KEY),
                // Placement Modifiers:
                RarityFilterPlacementModifier.of(24), // Chance: 1/24 chunks attempt placement. Adjust for desired rarity.
                SquarePlacementModifier.of(), // Spread attempts horizontally.
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, // Place on the surface.
                BiomePlacementModifier.of() // Required final modifier.
        );
        // --- End Register Placed Green Bean ---

        // --- Register Placed Cucumber Bush ---
        register(context, WILD_CUCUMBER_BUSH_PLACED_KEY,
                configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.WILD_CUCUMBER_BUSH_KEY),
                // Placement Modifiers:
                RarityFilterPlacementModifier.of(24), // Same rarity as green beans for now. Adjust as needed.
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of()
        );
        // --- End Register Placed Cucumber ---
    }

    // Helper methods (Existing)
    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(AdorableHamsterPets.MOD_ID, name));
    }

    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    // Overload to accept varargs for modifiers (makes registration cleaner)
    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                 PlacementModifier... modifiers) {
        register(context, key, configuration, List.of(modifiers));
    }
}