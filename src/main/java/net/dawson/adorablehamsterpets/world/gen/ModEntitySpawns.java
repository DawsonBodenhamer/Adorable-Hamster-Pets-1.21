package net.dawson.adorablehamsterpets.world.gen;

import net.dawson.adorablehamsterpets.entity.ModEntities;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

import net.minecraft.block.Block;
import java.util.HashSet;
import java.util.Set;

public class ModEntitySpawns {

    // A collection of blocks that might appear on the surface
    // in Badlands, Wooded Badlands, and Desert biomes.
    private static final Set<Block> VALID_BADLANDS_DESERT_BLOCKS = new HashSet<>();

    static {
        // Sand variants
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.SAND);
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.RED_SAND);

        // Terracotta (uncolored + all colors)
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.TERRACOTTA);
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.WHITE_TERRACOTTA);
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.ORANGE_TERRACOTTA);
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.MAGENTA_TERRACOTTA);
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.LIGHT_BLUE_TERRACOTTA);
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.YELLOW_TERRACOTTA);
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.LIME_TERRACOTTA);
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.PINK_TERRACOTTA);
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.GRAY_TERRACOTTA);
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.LIGHT_GRAY_TERRACOTTA);
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.CYAN_TERRACOTTA);
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.PURPLE_TERRACOTTA);
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.BLUE_TERRACOTTA);
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.BROWN_TERRACOTTA);
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.GREEN_TERRACOTTA);
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.RED_TERRACOTTA);
        VALID_BADLANDS_DESERT_BLOCKS.add(Blocks.BLACK_TERRACOTTA);
    }

    public static void addSpawns() {

        // Spawn rules for the hamster entity in the specified biomes
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(
                        BiomeKeys.PLAINS,
                        BiomeKeys.BADLANDS,
                        BiomeKeys.DESERT,
                        BiomeKeys.DRIPSTONE_CAVES,
                        BiomeKeys.ERODED_BADLANDS,
                        BiomeKeys.LUSH_CAVES,
                        BiomeKeys.MEADOW,
                        BiomeKeys.SAVANNA,
                        BiomeKeys.SAVANNA_PLATEAU,
                        BiomeKeys.SUNFLOWER_PLAINS,
                        BiomeKeys.WINDSWEPT_SAVANNA
                ),
                SpawnGroup.CREATURE,
                ModEntities.HAMSTER,
                30, // spawn weight (adjust as desired)
                1,   // minimum group size (adjust as desired)
                1    // maximum group size (adjust as desired)
        );

        // Custom spawn predicate: allow normal conditions OR if the block below
        // is one of the typical Badlands/Desert surface blocks.
        SpawnRestriction.register(
                ModEntities.HAMSTER,
                SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, spawnReason, pos, random) -> {
                    // If the standard check passes, allow spawn.
                    if (AnimalEntity.isValidNaturalSpawn(entityType, world, spawnReason, pos, random)) {
                        return true;
                    }
                    // Otherwise, check if the block below is in our valid list (Badlands/Desert).
                    Block blockBelow = world.getBlockState(pos.down()).getBlock();
                    return VALID_BADLANDS_DESERT_BLOCKS.contains(blockBelow);
                }
        );
    }
}