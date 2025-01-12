package net.dawson.adorablehamsterpets.block;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final



    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(AdorableHamsterPets.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block){
        Registry.register(Registries.ITEM, Identifier.of(AdorableHamsterPets.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks(){

        AdorableHamsterPets.LOGGER.info("Registering Mod Blocks for " + AdorableHamsterPets.MOD_ID);

    }
}
