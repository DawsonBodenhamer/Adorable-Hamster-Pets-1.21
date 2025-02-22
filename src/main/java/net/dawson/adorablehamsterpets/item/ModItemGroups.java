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


    public static final ItemGroup HAMSTER_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(AdorableHamsterPets.MOD_ID, "hamster_items"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.CHEESE))
                    .displayName(Text.translatable("itemgroup.adorablehamsterpets.hamster_items"))
                    .entries((displayContext, entries) -> {
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
                    }).build());



    public static void registerItemGroups(){
        AdorableHamsterPets.LOGGER.info("Registering Item Groups for " + AdorableHamsterPets.MOD_ID);
    }

}
