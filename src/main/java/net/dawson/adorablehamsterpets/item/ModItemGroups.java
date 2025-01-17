package net.dawson.adorablehamsterpets.item;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;




public class ModItemGroups {
    public static final ItemGroup PINK_GARNET_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(AdorableHamsterPets.MOD_ID, "pink_garnet_items"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.PINK_GARNET))
                    .displayName(Text.translatable("itemgroup.adorablehamsterpets.pink_garnet_items"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.PINK_GARNET);
                        entries.add(ModItems.RAW_PINK_GARNET);
                        entries.add(ModItems.CHISEL);
                        entries.add(ModItems.CAULIFLOWER);
                        entries.add(ModItems.STARLIGHT_ASHES);
                        entries.add(ModItems.PINK_GARNET_SWORD);
                        entries.add(ModItems.PINK_GARNET_PICKAXE);
                        entries.add(ModItems.PINK_GARNET_SHOVEL);
                        entries.add(ModItems.PINK_GARNET_AXE);
                        entries.add(ModItems.PINK_GARNET_HOE);
                        entries.add(ModItems.PINK_GARNET_HAMMER);
                        entries.add(ModItems.PINK_GARNET_HELMET);
                        entries.add(ModItems.PINK_GARNET_CHESTPLATE);
                        entries.add(ModItems.PINK_GARNET_LEGGINGS);
                        entries.add(ModItems.PINK_GARNET_BOOTS);
                        entries.add(ModItems.PINK_GARNET_HORSE_ARMOR);
                        entries.add(ModItems.DAWSON_SMITHING_TEMPLATE);
                        entries.add(ModItems.DAWSON_BOW);

                    }).build());


    public static final ItemGroup PINK_GARNET_BLOCKS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(AdorableHamsterPets.MOD_ID, "pink_garnet_blocks"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModBlocks.PINK_GARNET_BLOCK))
                    .displayName(Text.translatable("itemgroup.adorablehamsterpets.pink_garnet_blocks"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModBlocks.PINK_GARNET_BLOCK);
                        entries.add(ModBlocks.RAW_PINK_GARNET_BLOCK);
                        entries.add(ModBlocks.PINK_GARNET_ORE);
                        entries.add(ModBlocks.PINK_GARNET_DEEPSLATE_ORE);
                        entries.add(ModBlocks.MAGIC_BLOCK);
                        entries.add(ModBlocks.PINK_GARNET_STAIRS);
                        entries.add(ModBlocks.PINK_GARNET_SLAB);
                        entries.add(ModBlocks.PINK_GARNET_BUTTON);
                        entries.add(ModBlocks.PINK_GARNET_PRESSURE_PLATE);
                        entries.add(ModBlocks.PINK_GARNET_FENCE);
                        entries.add(ModBlocks.PINK_GARNET_FENCE_GATE);
                        entries.add(ModBlocks.PINK_GARNET_WALL);
                        entries.add(ModBlocks.PINK_GARNET_DOOR);
                        entries.add(ModBlocks.PINK_GARNET_TRAPDOOR);
                        entries.add(ModBlocks.PINK_GARNET_LAMP);
                    }).build());


    public static final ItemGroup HAMSTER_BLOCKS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(AdorableHamsterPets.MOD_ID, "hamster_blocks"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModBlocks.CHEESE_BLOCK))
                    .displayName(Text.translatable("itemgroup.adorablehamsterpets.hamster_blocks"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModBlocks.CHEESE_BLOCK);
                        entries.add(ModBlocks.GREEN_BEANS_BLOCK);
                        entries.add(ModBlocks.CUCUMBER_BLOCK);
                    }).build());


    public static final ItemGroup HAMSTER_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(AdorableHamsterPets.MOD_ID, "hamster_items"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.CHEESE))
                    .displayName(Text.translatable("itemgroup.adorablehamsterpets.hamster_items"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.CHEESE);
                        entries.add(ModItems.HAMSTER_FOOD_MIX);
                        entries.add(ModItems.CUCUMBER);
                        entries.add(ModItems.SLICED_CUCUMBER);
                        entries.add(ModItems.GREEN_BEANS);
                        entries.add(ModItems.STEAMED_GREEN_BEANS);
                        entries.add(ModItems.SUNFLOWER_SEEDS);
                    }).build());






    public static void registerItemGroups(){
        AdorableHamsterPets.LOGGER.info("Registering Item Groups for " + AdorableHamsterPets.MOD_ID);
    }

}
