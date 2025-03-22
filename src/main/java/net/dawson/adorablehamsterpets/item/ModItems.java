package net.dawson.adorablehamsterpets.item;


import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.block.ModBlocks;
import net.dawson.adorablehamsterpets.entity.ModEntities;
import net.dawson.adorablehamsterpets.item.custom.CheeseItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ModItems {

    public static final Item HAMSTER_SPAWN_EGG = registerItem("hamster_spawn_egg",
            new SpawnEggItem(ModEntities.HAMSTER, 0x9c631f, 0xffffff, new Item.Settings()));

    public static final Item GREEN_BEAN_SEEDS = registerItem("green_bean_seeds",
            new AliasedBlockItem(ModBlocks.GREEN_BEANS_CROP, new Item.Settings()));
    public static final Item CUCUMBER_SEEDS = registerItem("cucumber_seeds",
            new AliasedBlockItem(ModBlocks.CUCUMBER_CROP, new Item.Settings()));

    public static final Item SUNFLOWER_SEEDS = registerItem("sunflower_seeds",new Item(new Item.Settings()));
    public static final Item CUCUMBER = registerItem("cucumber", new Item(new Item.Settings().food(ModFoodComponents.CUCUMBER)));
    public static final Item SLICED_CUCUMBER = registerItem("sliced_cucumber",new Item(new Item.Settings().food(ModFoodComponents.SLICED_CUCUMBER)));
    public static final Item GREEN_BEANS = registerItem("green_beans",new Item(new Item.Settings().food(ModFoodComponents.GREEN_BEANS)));
    public static final Item STEAMED_GREEN_BEANS = registerItem("steamed_green_beans",new Item(new Item.Settings().food(ModFoodComponents.STEAMED_GREEN_BEANS)));
    public static final Item HAMSTER_FOOD_MIX = registerItem("hamster_food_mix",new Item(new Item.Settings().food(ModFoodComponents.HAMSTER_FOOD_MIX)));

    public static final Item CHEESE = registerItem("cheese", new CheeseItem(new Item.Settings()));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(AdorableHamsterPets.MOD_ID, name), item);
    }


  public static void registerModItems() {
      AdorableHamsterPets.LOGGER.info("Registering Mod Items for " + AdorableHamsterPets.MOD_ID);

      ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(fabricItemGroupEntries -> {
          fabricItemGroupEntries.add(CUCUMBER);
          fabricItemGroupEntries.add(SLICED_CUCUMBER);
          fabricItemGroupEntries.add(GREEN_BEANS);
          fabricItemGroupEntries.add(STEAMED_GREEN_BEANS);
          fabricItemGroupEntries.add(SUNFLOWER_SEEDS);
          fabricItemGroupEntries.add(CHEESE);
          fabricItemGroupEntries.add(HAMSTER_FOOD_MIX);
      });

  }

}
