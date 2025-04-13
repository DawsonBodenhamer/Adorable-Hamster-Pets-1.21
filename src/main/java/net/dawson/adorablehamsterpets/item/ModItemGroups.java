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


    public static final ItemGroup ADORABLE_HAMSTER_PETS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(AdorableHamsterPets.MOD_ID, "adorable_hamster_pets"), // Example Renamed ID
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.CHEESE)) // Keep or change icon
                    .displayName(Text.translatable("itemgroup.adorablehamsterpets.main")) // Example Renamed Key
                    .entries((displayContext, entries) -> {

                        // Items
                        entries.add(ModItems.CHEESE);
                        entries.add(ModItems.HAMSTER_FOOD_MIX);
                        entries.add(ModItems.CUCUMBER);
                        entries.add(ModItems.CUCUMBER_SEEDS);
                        entries.add(ModItems.SLICED_CUCUMBER);
                        entries.add(ModItems.GREEN_BEANS);
                        entries.add(ModItems.GREEN_BEAN_SEEDS);
                        entries.add(ModItems.STEAMED_GREEN_BEANS);
                        entries.add(ModItems.SUNFLOWER_SEEDS);
                        entries.add(ModItems.HAMSTER_SPAWN_EGG);

                        // Blocks
                        entries.add(ModBlocks.SUNFLOWER_BLOCK);
                        entries.add(ModBlocks.WILD_GREEN_BEAN_BUSH);
                        entries.add(ModBlocks.WILD_CUCUMBER_BUSH);

                    }).build());


    public static void registerItemGroups(){
        AdorableHamsterPets.LOGGER.info("Registering Item Groups for " + AdorableHamsterPets.MOD_ID);
    }

}
