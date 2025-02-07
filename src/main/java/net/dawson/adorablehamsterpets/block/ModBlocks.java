package net.dawson.adorablehamsterpets.block;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.block.custom.*;
import net.dawson.adorablehamsterpets.sound.ModSounds;
import net.dawson.adorablehamsterpets.world.tree.ModSaplingGenerators;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.structure.StrongholdGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;


public class ModBlocks {
    public static final Block PINK_GARNET_BLOCK = registerBlock("pink_garnet_block",
    new Block(
            AbstractBlock.Settings.create()
                    .strength(4f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)));

    public static final Block RAW_PINK_GARNET_BLOCK = registerBlock("raw_pink_garnet_block",
            new Block(
                    AbstractBlock.Settings.create()
                            .strength(3f)
                            .requiresTool()
                            .sounds(BlockSoundGroup.AMETHYST_BLOCK)));

    public static final Block PINK_GARNET_ORE = registerBlock("pink_garnet_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(2, 5),
                    AbstractBlock.Settings.create()
                            .strength(3f)
                            .requiresTool()
                            .sounds(BlockSoundGroup.STONE)));

    public static final Block PINK_GARNET_DEEPSLATE_ORE = registerBlock("pink_garnet_deepslate_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(3, 6),
                    AbstractBlock.Settings.create()
                            .strength(4f)
                            .requiresTool()
                            .sounds(BlockSoundGroup.DEEPSLATE)));

    public static final Block PINK_GARNET_END_ORE = registerBlock("pink_garnet_end_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(4, 8),
                    AbstractBlock.Settings.create()
                            .strength(7f)
                            .requiresTool()));

    public static final Block PINK_GARNET_NETHER_ORE = registerBlock("pink_garnet_nether_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(1, 5),
                    AbstractBlock.Settings.create()
                            .strength(3f)
                            .requiresTool()));

    public static final Block MAGIC_BLOCK = registerBlock("magic_block",
            new MagicBlock(AbstractBlock.Settings.create()
                    .strength(1f)
                    .requiresTool()
                    .sounds(ModSounds.MAGIC_BLOCK_SOUNDS)));

    public static final Block PINK_GARNET_STAIRS = registerBlock("pink_garnet_stairs",
            new StairsBlock(ModBlocks.PINK_GARNET_BLOCK.getDefaultState(),
                    AbstractBlock.Settings.create()
                            .strength(2f)
                            .requiresTool()));

    public static final Block PINK_GARNET_SLAB = registerBlock("pink_garnet_slab",
            new SlabBlock(AbstractBlock.Settings.create()
                    .strength(2f)
                    .requiresTool()));

    public static final Block PINK_GARNET_BUTTON = registerBlock("pink_garnet_button",
            new ButtonBlock(BlockSetType.IRON, 2,
                    AbstractBlock.Settings.create()
                            .strength(2f)
                            .requiresTool()
                            .noCollision()));

    public static final Block PINK_GARNET_PRESSURE_PLATE = registerBlock("pink_garnet_pressure_plate",
            new PressurePlateBlock(BlockSetType.IRON,
                    AbstractBlock.Settings.create()
                            .strength(2f)
                            .requiresTool()));

    public static final Block PINK_GARNET_FENCE = registerBlock("pink_garnet_fence",
            new FenceBlock(
                    AbstractBlock.Settings.create()
                            .strength(2f)
                            .requiresTool()));

    public static final Block PINK_GARNET_FENCE_GATE = registerBlock("pink_garnet_fence_gate",
            new FenceGateBlock(WoodType.ACACIA,
                    AbstractBlock.Settings.create()
                            .strength(2f)
                            .requiresTool()));

    public static final Block PINK_GARNET_WALL = registerBlock("pink_garnet_wall",
            new WallBlock(
                    AbstractBlock.Settings.create()
                            .strength(2f)
                            .requiresTool()));

    public static final Block PINK_GARNET_DOOR = registerBlock("pink_garnet_door",
            new DoorBlock(BlockSetType.IRON,
                    AbstractBlock.Settings.create()
                            .strength(2f)
                            .requiresTool()
                            .nonOpaque()));

    public static final Block PINK_GARNET_TRAPDOOR = registerBlock("pink_garnet_trapdoor",
            new TrapdoorBlock(BlockSetType.IRON,
                    AbstractBlock.Settings.create()
                            .strength(2f)
                            .requiresTool()
                            .nonOpaque()));

    public static final Block PINK_GARNET_LAMP = registerBlock("pink_garnet_lamp",
            new PinkGarnetLampBlock(
                    AbstractBlock.Settings.create()
                            .strength(1f)
                            .requiresTool()
                            .luminance(state -> state.get(PinkGarnetLampBlock.CLICKED) ? 15 : 0)));

    public static final Block CAULIFLOWER_CROP = registerBlockWithoutBlockItem("cauliflower_crop",
            new CauliflowerCropBlock(AbstractBlock.Settings.create().noCollision()
                    .mapColor(MapColor.DARK_GREEN)
                    .ticksRandomly()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.CROP)
                    .pistonBehavior(PistonBehavior.DESTROY)));

    public static final Block HONEY_BERRY_BUSH = registerBlockWithoutBlockItem("honey_berry_bush",
            new HoneyBerryBushBlock(AbstractBlock.Settings.copy(Blocks.SWEET_BERRY_BUSH)));

    public static final Block DRIFTWOOD_LOG = registerBlock("driftwood_log",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.OAK_LOG)));
    public static final Block DRIFTWOOD_WOOD = registerBlock("driftwood_wood",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.OAK_WOOD)));
    public static final Block STRIPPED_DRIFTWOOD_LOG = registerBlock("stripped_driftwood_log",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final Block STRIPPED_DRIFTWOOD_WOOD = registerBlock("stripped_driftwood_wood",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_OAK_WOOD)));

    public static final Block DRIFTWOOD_PLANKS = registerBlock("driftwood_planks",
            new Block(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)));
    public static final Block DRIFTWOOD_LEAVES = registerBlock("driftwood_leaves",
            new LeavesBlock(AbstractBlock.Settings.copy(Blocks.OAK_LEAVES)));

    public static final Block DRIFTWOOD_SAPLING = registerBlock("driftwood_sapling",
            new ModSaplingBlock(ModSaplingGenerators.DRIFTWOOD, AbstractBlock.Settings.copy(Blocks.OAK_SAPLING), Blocks.STONE));










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



    public static final Block CHEESE_BLOCK = registerBlock("cheese_block",
            new Block(
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.YELLOW)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .strength(0.4F)
                            .sounds(BlockSoundGroup.MUD)
                            .ticksRandomly()
              ));






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

    public static void registerModBlocks(){

        AdorableHamsterPets.LOGGER.info("Registering Mod Blocks for " + AdorableHamsterPets.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(ModBlocks.PINK_GARNET_BLOCK);
            fabricItemGroupEntries.add(ModBlocks.RAW_PINK_GARNET_BLOCK);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(ModBlocks.CHEESE_BLOCK);
        });
    }
}
