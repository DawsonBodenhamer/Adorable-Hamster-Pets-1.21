package net.dawson.adorablehamsterpets.item;


import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.block.ModBlocks;
import net.dawson.adorablehamsterpets.entity.ModEntities;
import net.dawson.adorablehamsterpets.item.custom.ChiselItem;
import net.dawson.adorablehamsterpets.item.custom.HammerItem;
import net.dawson.adorablehamsterpets.item.custom.ModArmorItem;
import net.dawson.adorablehamsterpets.sound.ModSounds;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.render.entity.FallingBlockEntityRenderer;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModItems {

    public static final Item PINK_GARNET = registerItem("pink_garnet",new Item(new Item.Settings()));
    public static final Item RAW_PINK_GARNET = registerItem("raw_pink_garnet",new Item(new Item.Settings()));
    public static final Item CHISEL = registerItem("chisel", new ChiselItem(new Item.Settings().maxDamage(32)));
    public static final Item CAULIFLOWER = registerItem("cauliflower", new Item(new Item.Settings().food(ModFoodComponents.CAULIFLOWER)) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.adorablehamsterpets.cauliflower.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });
    public static final Item STARLIGHT_ASHES = registerItem("starlight_ashes", new Item(new Item.Settings()));

    public static final Item PINK_GARNET_SWORD = registerItem("pink_garnet_sword",
            new SwordItem(ModToolMaterials.PINK_GARNET, new Item.Settings()
                    .attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.PINK_GARNET, 3, -2.4f))));

    public static final Item PINK_GARNET_PICKAXE = registerItem("pink_garnet_pickaxe",
            new PickaxeItem(ModToolMaterials.PINK_GARNET, new Item.Settings()
                    .attributeModifiers(PickaxeItem.createAttributeModifiers(ModToolMaterials.PINK_GARNET, 1, -2.8f))));

    public static final Item PINK_GARNET_SHOVEL = registerItem("pink_garnet_shovel",
            new ShovelItem(ModToolMaterials.PINK_GARNET, new Item.Settings()
                    .attributeModifiers(ShovelItem.createAttributeModifiers(ModToolMaterials.PINK_GARNET, 1.5F, -3.0f))));

    public static final Item PINK_GARNET_AXE = registerItem("pink_garnet_axe",
            new AxeItem(ModToolMaterials.PINK_GARNET, new Item.Settings()
                    .attributeModifiers(AxeItem.createAttributeModifiers(ModToolMaterials.PINK_GARNET, 6, -3.2f))));

    public static final Item PINK_GARNET_HOE = registerItem("pink_garnet_hoe",
            new HoeItem(ModToolMaterials.PINK_GARNET, new Item.Settings()
                    .attributeModifiers(HoeItem.createAttributeModifiers(ModToolMaterials.PINK_GARNET, 0, -3.0f))));

    public static final Item PINK_GARNET_HAMMER = registerItem("pink_garnet_hammer",
            new HammerItem(ModToolMaterials.PINK_GARNET, new Item.Settings()
                    .attributeModifiers(HammerItem.createAttributeModifiers(ModToolMaterials.PINK_GARNET, 7, -3.4f))));

    public static final Item PINK_GARNET_HELMET = registerItem("pink_garnet_helmet",
            new ModArmorItem(ModArmorMaterials.PINK_GARNET_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                    .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(15))));

    public static final Item PINK_GARNET_CHESTPLATE = registerItem("pink_garnet_chestplate",
            new ArmorItem(ModArmorMaterials.PINK_GARNET_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                    .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(15))));

    public static final Item PINK_GARNET_LEGGINGS = registerItem("pink_garnet_leggings",
            new ArmorItem(ModArmorMaterials.PINK_GARNET_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(15))));

    public static final Item PINK_GARNET_BOOTS = registerItem("pink_garnet_boots",
            new ArmorItem(ModArmorMaterials.PINK_GARNET_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15))));

    public static final Item PINK_GARNET_HORSE_ARMOR = registerItem("pink_garnet_horse_armor",
            new AnimalArmorItem(ModArmorMaterials.PINK_GARNET_ARMOR_MATERIAL, AnimalArmorItem.Type.EQUESTRIAN, false, new Item.Settings().maxCount(1)));

    public static final Item DAWSON_SMITHING_TEMPLATE = registerItem("dawson_armor_trim_smithing_template",
            SmithingTemplateItem.of(Identifier.of(AdorableHamsterPets.MOD_ID, "dawson"), FeatureFlags.VANILLA));

    public static final Item DAWSON_BOW = registerItem("dawson_bow",
            new BowItem(new Item.Settings().maxDamage(500)));

    public static final Item BAR_BRAWL_MUSIC_DISC = registerItem("bar_brawl_music_disc",
            new Item(new Item.Settings().jukeboxPlayable(ModSounds.BAR_BRAWL_KEY).maxCount(1)));

    public static final Item CAULIFLOWER_SEEDS = registerItem("cauliflower_seeds",
            new AliasedBlockItem(ModBlocks.CAULIFLOWER_CROP, new Item.Settings()));

    public static final Item HONEY_BERRIES = registerItem("honey_berries",
            new AliasedBlockItem(ModBlocks.HONEY_BERRY_BUSH, new Item.Settings().food(ModFoodComponents.HONEY_BERRY)));

    public static final Item MANTIS_SPAWN_EGG = registerItem("mantis_spawn_egg",
            new SpawnEggItem(ModEntities.MANTIS, 0x9dc783, 0xbfaf5f, new Item.Settings()));






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
    public static final Item CHEESE = registerItem("cheese",new Item(new Item.Settings().food(ModFoodComponents.CHEESE)));
    public static final Item HAMSTER_FOOD_MIX = registerItem("hamster_food_mix",new Item(new Item.Settings().food(ModFoodComponents.HAMSTER_FOOD_MIX)));

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
