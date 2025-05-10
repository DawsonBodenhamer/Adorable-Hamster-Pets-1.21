package net.dawson.adorablehamsterpets.item;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.block.ModBlocks;
import net.dawson.adorablehamsterpets.entity.ModEntities;
import net.dawson.adorablehamsterpets.item.custom.CheeseItem;
// No longer need HamsterFoodMixItem import
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.WrittenBookContentComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text; // Import Text
import net.minecraft.util.Formatting; // Import Formatting
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List; // Import List

public class ModItems {

    // --- Guide Book ---
    public static final Item HAMSTER_GUIDE_BOOK = registerItem("hamster_guide_book",
            new Item(new Item.Settings().maxCount(1)) {
                @Override
                public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
                    // ... (tooltip code remains the same) ...
                    tooltip.add(Text.translatable("tooltip.adorablehamsterpets.hamster_guide_book.hint1").formatted(Formatting.GRAY));
                    WrittenBookContentComponent content = stack.get(DataComponentTypes.WRITTEN_BOOK_CONTENT);
                    if (content != null && !content.title().raw().isEmpty()) {
                        tooltip.add(Text.translatable("book.byAuthor", content.author()).formatted(Formatting.GRAY));
                    }
                    super.appendTooltip(stack, context, tooltip, type);
                }

                // Keep this for the feel
                @Override
                public boolean isUsedOnRelease(ItemStack stack) {
                    return true;
                }

                // --- REVISED 'use' method ---
                @Override
                public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
                    ItemStack itemStack = user.getStackInHand(hand);
                    // Check if the book has content
                    if (itemStack.contains(DataComponentTypes.WRITTEN_BOOK_CONTENT)) {
                        // --- Client-side screen opening ---
                        if (world.isClient) {
                            BookScreen.Contents contents = BookScreen.Contents.create(itemStack);
                            if (contents != null) {
                                MinecraftClient.getInstance().setScreen(new BookScreen(contents));
                            }
                        }
                        // --- End client-side ---

                        user.incrementStat(Stats.USED.getOrCreateStat(this)); // Increment stat on both sides potentially
                        return TypedActionResult.success(itemStack, world.isClient()); // Indicate success
                    }
                    // If somehow the book has no content component, pass (do nothing)
                    return TypedActionResult.pass(itemStack);
                }
                // --- End REVISED 'use' method ---

            });
    // --- End Guide Book ---

    // Spawn Egg (No tooltip needed)
    public static final Item HAMSTER_SPAWN_EGG = registerItem("hamster_spawn_egg",
            new SpawnEggItem(ModEntities.HAMSTER, 0x9c631f, 0xffffff, new Item.Settings()));

    // --- Seeds with Revised Hints ---
    public static final Item GREEN_BEAN_SEEDS = registerItem("green_bean_seeds",
            new AliasedBlockItem(ModBlocks.GREEN_BEANS_CROP, new Item.Settings()) {
                @Override
                public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
                    tooltip.add(Text.translatable("tooltip.adorablehamsterpets.green_bean_seeds.hint1").formatted(Formatting.AQUA)); // §b Planting hint
                    tooltip.add(Text.translatable("tooltip.adorablehamsterpets.green_bean_seeds.hint2").formatted(Formatting.GRAY));   // §7 Humorous description
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });

    public static final Item CUCUMBER_SEEDS = registerItem("cucumber_seeds",
            new AliasedBlockItem(ModBlocks.CUCUMBER_CROP, new Item.Settings()) {
                @Override
                public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
                    tooltip.add(Text.translatable("tooltip.adorablehamsterpets.cucumber_seeds.hint1").formatted(Formatting.AQUA)); // §b Planting hint
                    tooltip.add(Text.translatable("tooltip.adorablehamsterpets.cucumber_seeds.hint2").formatted(Formatting.GRAY));   // §7 Humorous description
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });
    // --- End Seeds ---

    // --- Other Items with Revised Hints ---
    public static final Item SUNFLOWER_SEEDS = registerItem("sunflower_seeds",
            new Item(new Item.Settings()) {
                @Override
                public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
                    tooltip.add(Text.translatable("tooltip.adorablehamsterpets.sunflower_seeds.hint1").formatted(Formatting.YELLOW)); // §e Neutral (food)
                    tooltip.add(Text.translatable("tooltip.adorablehamsterpets.sunflower_seeds.hint2").formatted(Formatting.GRAY));   // §7 Humorous source
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });

    public static final Item CUCUMBER = registerItem("cucumber",
            new Item(new Item.Settings().food(ModFoodComponents.CUCUMBER)) {
                @Override
                public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
                    tooltip.add(Text.translatable("tooltip.adorablehamsterpets.cucumber.hint1").formatted(Formatting.YELLOW)); // §e Neutral (food)
                    tooltip.add(Text.translatable("tooltip.adorablehamsterpets.cucumber.hint2").formatted(Formatting.AQUA));   // §b Crafting hint
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });

    public static final Item SLICED_CUCUMBER = registerItem("sliced_cucumber",
            new Item(new Item.Settings().food(ModFoodComponents.SLICED_CUCUMBER)) {
                @Override
                public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
                    tooltip.add(Text.translatable("tooltip.adorablehamsterpets.sliced_cucumber.hint1").formatted(Formatting.GREEN)); // §a Positive use (taming)
                    tooltip.add(Text.translatable("tooltip.adorablehamsterpets.sliced_cucumber.hint2").formatted(Formatting.GRAY));  // §7 Humorous description
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });

    public static final Item GREEN_BEANS = registerItem("green_beans",
            new Item(new Item.Settings().food(ModFoodComponents.GREEN_BEANS)) {
                @Override
                public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
                    tooltip.add(Text.translatable("tooltip.adorablehamsterpets.green_beans.hint1").formatted(Formatting.YELLOW)); // §e Neutral (food)
                    tooltip.add(Text.translatable("tooltip.adorablehamsterpets.green_beans.hint2").formatted(Formatting.AQUA));   // §b Cooking hint
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });

    public static final Item STEAMED_GREEN_BEANS = registerItem("steamed_green_beans",
            new Item(new Item.Settings().food(ModFoodComponents.STEAMED_GREEN_BEANS)) {
                @Override
                public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
                    tooltip.add(Text.translatable("tooltip.adorablehamsterpets.steamed_green_beans.hint1").formatted(Formatting.GOLD)); // §6 Special use (buff)
                    tooltip.add(Text.translatable("tooltip.adorablehamsterpets.steamed_green_beans.hint2").formatted(Formatting.GRAY)); // §7 Humorous description (cooldown)
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });

    public static final Item HAMSTER_FOOD_MIX = registerItem("hamster_food_mix",
            new Item(new Item.Settings().food(ModFoodComponents.HAMSTER_FOOD_MIX).maxCount(16)) {
                @Override
                public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
                    tooltip.add(Text.translatable("tooltip.adorablehamsterpets.hamster_food_mix.hint1").formatted(Formatting.GREEN));
                    tooltip.add(Text.translatable("tooltip.adorablehamsterpets.hamster_food_mix.hint2").formatted(Formatting.GRAY));
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });

    // Cheese already uses its custom class
    public static final Item CHEESE = registerItem("cheese", new CheeseItem(new Item.Settings()));

    // Helper method (no changes needed)
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(AdorableHamsterPets.MOD_ID, name), item);
    }

    // Item Group registration (no changes needed)
    public static void registerModItems() {
        AdorableHamsterPets.LOGGER.info("Registering Mod Items for " + AdorableHamsterPets.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(fabricItemGroupEntries -> {
            // Add items in a logical order if desired
            fabricItemGroupEntries.add(GREEN_BEAN_SEEDS);
            fabricItemGroupEntries.add(CUCUMBER_SEEDS);
            fabricItemGroupEntries.add(SUNFLOWER_SEEDS);
            fabricItemGroupEntries.add(GREEN_BEANS);
            fabricItemGroupEntries.add(CUCUMBER);
            fabricItemGroupEntries.add(SLICED_CUCUMBER);
            fabricItemGroupEntries.add(STEAMED_GREEN_BEANS);
            fabricItemGroupEntries.add(HAMSTER_FOOD_MIX);
            fabricItemGroupEntries.add(CHEESE);
            fabricItemGroupEntries.add(HAMSTER_GUIDE_BOOK);
            // Spawn egg is usually in a different group, but added via ModItemGroups.java
        });
    }
}