package net.dawson.adorablehamsterpets.item;


import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.render.entity.FallingBlockEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item PINK_GARNET = registerItem("pink_garnet",new Item(new Item.Settings()));
    public static final Item RAW_PINK_GARNET = registerItem("raw_pink_garnet",new Item(new Item.Settings()));

    public static final Item CUCUMBER = registerItem("cucumber",new Item(new Item.Settings()));
    public static final Item SLICED_CUCUMBER = registerItem("sliced_cucumber",new Item(new Item.Settings()));
    public static final Item GREEN_BEANS = registerItem("green_beans",new Item(new Item.Settings()));
    public static final Item STEAMED_GREEN_BEANS = registerItem("steamed_green_beans",new Item(new Item.Settings()));
    public static final Item SUNFLOWER_SEEDS = registerItem("sunflower_seeds",new Item(new Item.Settings()));
    public static final Item CHEESE = registerItem("cheese",new Item(new Item.Settings()));
    public static final Item HAMSTER_FOOD_MIX = registerItem("hamster_food_mix",new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(AdorableHamsterPets.MOD_ID, name), item);
    }


  public static void registerModItems() {
      AdorableHamsterPets.LOGGER.info("Registering Mod Items for " + AdorableHamsterPets.MOD_ID);

      ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(fabricItemGroupEntries -> {
          fabricItemGroupEntries.add(PINK_GARNET);
          fabricItemGroupEntries.add(RAW_PINK_GARNET);
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
