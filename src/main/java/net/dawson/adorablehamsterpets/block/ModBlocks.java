package net.dawson.adorablehamsterpets.block;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.block.custom.*;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;


public class ModBlocks {

    public static final Block GREEN_BEANS_CROP = registerBlockWithoutBlockItem("green_beans_crop",
            new GreenBeansCropBlock(AbstractBlock.Settings.create().noCollision()
                    .mapColor(MapColor.DARK_GREEN)
                    .ticksRandomly()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.CROP)
                    .pistonBehavior(PistonBehavior.DESTROY)));


    public static final Block CUCUMBER_CROP = registerBlockWithoutBlockItem("cucumber_crop",
            new CucumberCropBlock(AbstractBlock.Settings.create().noCollision()
                    .mapColor(MapColor.DARK_GREEN)
                    .ticksRandomly()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.CROP)
                    .pistonBehavior(PistonBehavior.DESTROY)));




    private static Block registerBlockWithoutBlockItem(String name, Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of(AdorableHamsterPets.MOD_ID, name), block);
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(AdorableHamsterPets.MOD_ID, name), block);
    }


    private static void registerBlockItem(String name, Block block){
        Registry.register(Registries.ITEM, Identifier.of(AdorableHamsterPets.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

//    public static void registerModBlocks(){
//
//        AdorableHamsterPets.LOGGER.info("Registering Mod Blocks for " + AdorableHamsterPets.MOD_ID);
//
//        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(fabricItemGroupEntries -> {
//            fabricItemGroupEntries.add(ModBlocks.CHEESE_BLOCK);
//        });
//    }
}
