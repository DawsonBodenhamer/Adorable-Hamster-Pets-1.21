package net.dawson.adorablehamsterpets.block;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.block.custom.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModBlocks {

    public static final Block GREEN_BEANS_CROP = Registry.register(Registries.BLOCK,
            Identifier.of(AdorableHamsterPets.MOD_ID, "green_beans_crop"),
            new GreenBeansCropBlock(AbstractBlock.Settings.copy(Blocks.WHEAT).sounds(BlockSoundGroup.CROP).nonOpaque().noCollision()));
    public static final Block CUCUMBER_CROP = Registry.register(Registries.BLOCK,
            Identifier.of(AdorableHamsterPets.MOD_ID, "cucumber_crop"),
            new CucumberCropBlock(AbstractBlock.Settings.copy(Blocks.WHEAT).sounds(BlockSoundGroup.CROP).nonOpaque().noCollision()));

    // Register the Wild Green Bean Bush
    public static final Block WILD_GREEN_BEAN_BUSH = registerBlock("wild_green_bean_bush",
            new WildGreenBeanBushBlock(AbstractBlock.Settings.copy(Blocks.SWEET_BERRY_BUSH) // Copy settings from Sweet Berry Bush
                    .nonOpaque() // Ensure it doesn't fully block light/rendering
                    .noCollision() // Entities can pass through
                    .ticksRandomly() // Necessary for the regrowth randomTick
                    .sounds(BlockSoundGroup.SWEET_BERRY_BUSH))); // Use appropriate sounds

    // Register the Wild Cucumber Bush
    public static final Block WILD_CUCUMBER_BUSH = registerBlock("wild_cucumber_bush",
            new WildCucumberBushBlock(AbstractBlock.Settings.copy(Blocks.SWEET_BERRY_BUSH) // Copy settings
                    .nonOpaque()
                    .noCollision()
                    .ticksRandomly() // Still needs random ticks for regrowth
                    .sounds(BlockSoundGroup.SWEET_BERRY_BUSH))); // Use same sounds

    // still need to add: `WILD_CUCUMBER_BUSH`

    public static final Block SUNFLOWER_BLOCK = registerBlock("sunflower_block",
            new SunflowerBlock(AbstractBlock.Settings.copy(Blocks.SUNFLOWER).nonOpaque()));

    // Helper method to register block and its item
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(AdorableHamsterPets.MOD_ID, name), block);
    }

    // --- Helper method to register block items with tooltips for bushes ---
    private static Item registerBlockItem(String name, Block block) {
        Item item;
        if (block instanceof WildGreenBeanBushBlock || block instanceof WildCucumberBushBlock) {
            item = new BlockItem(block, new Item.Settings()) {
                @Override
                public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
                    String translationKey = this.getTranslationKey(stack);
                    tooltip.add(Text.translatable(translationKey + ".hint1").formatted(Formatting.YELLOW)); // §e Hint at seed purpose
                    tooltip.add(Text.translatable(translationKey + ".hint2").formatted(Formatting.GRAY));   // §7 Humorous description
                    super.appendTooltip(stack, context, tooltip, type);
                }
            };
        } else if (block instanceof SunflowerBlock) {
            item = new BlockItem(block, new Item.Settings()) {
                @Override
                public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
                    String translationKey = this.getTranslationKey(stack);
                    tooltip.add(Text.translatable(translationKey + ".hint1").formatted(Formatting.YELLOW)); // §e Neutral (harvest hint)
                    tooltip.add(Text.translatable(translationKey + ".hint2").formatted(Formatting.GRAY));   // §7 Descriptive
                    super.appendTooltip(stack, context, tooltip, type);
                }
            };
        }
        else {
            item = new BlockItem(block, new Item.Settings());
        }
        return Registry.register(Registries.ITEM, Identifier.of(AdorableHamsterPets.MOD_ID, name), item);
    }


    public static void registerModBlocks() {
        AdorableHamsterPets.LOGGER.info("Registering ModBlocks for " + AdorableHamsterPets.MOD_ID);
    }
}