package net.dawson.adorablehamsterpets.datagen;

import net.dawson.adorablehamsterpets.block.ModBlocks;
import net.dawson.adorablehamsterpets.block.custom.CucumberCropBlock;
import net.dawson.adorablehamsterpets.block.custom.GreenBeansCropBlock;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.enums.DoubleBlockHalf; // [ADDED] Import
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items; // [ADDED] Import Items if using vanilla sunflower item
import net.minecraft.loot.LootPool; // [ADDED] Import
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LootCondition; // [ADDED] Import
import net.minecraft.loot.condition.SurvivesExplosionLootCondition; // [ADDED] Import
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry; // [ADDED] Import
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider; // [ADDED] Import
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.state.property.Properties; // [ADDED] Import

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {

        // Keep existing crop loot tables
        BlockStatePropertyLootCondition.Builder builder3 = BlockStatePropertyLootCondition.builder(ModBlocks.GREEN_BEANS_CROP)
                .properties(StatePredicate.Builder.create().exactMatch(GreenBeansCropBlock.AGE, GreenBeansCropBlock.MAX_AGE));
        this.addDrop(ModBlocks.GREEN_BEANS_CROP, this.cropDrops(ModBlocks.GREEN_BEANS_CROP, ModItems.GREEN_BEANS, ModItems.GREEN_BEAN_SEEDS, builder3));

        BlockStatePropertyLootCondition.Builder builder4 = BlockStatePropertyLootCondition.builder(ModBlocks.CUCUMBER_CROP)
                .properties(StatePredicate.Builder.create().exactMatch(CucumberCropBlock.AGE, CucumberCropBlock.MAX_AGE));
        this.addDrop(ModBlocks.CUCUMBER_CROP, this.cropDrops(ModBlocks.CUCUMBER_CROP, ModItems.CUCUMBER, ModItems.CUCUMBER_SEEDS, builder4));

        // --- Custom Sunflower Loot Table ---
        // Only drop the item when the LOWER half is broken
        LootCondition.Builder conditionBuilder = BlockStatePropertyLootCondition.builder(ModBlocks.SUNFLOWER_BLOCK)
                .properties(StatePredicate.Builder.create().exactMatch(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER));

        // Define the drop (sunflower item)
        Item sunflowerItemToDrop = ModBlocks.SUNFLOWER_BLOCK.asItem();

        this.addDrop(ModBlocks.SUNFLOWER_BLOCK, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F)) // Always try to roll once
                        .conditionally(conditionBuilder) // Apply the condition (only drop if lower half)
                        .with(this.applyExplosionDecay(ModBlocks.SUNFLOWER_BLOCK, ItemEntry.builder(sunflowerItemToDrop))) // Add the item entry
                        .conditionally(SurvivesExplosionLootCondition.builder()) // Standard condition for blocks
                )
        );

        // Drop 1-2 seeds when broken
        addDrop(ModBlocks.WILD_GREEN_BEAN_BUSH, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0f)) // Always try to roll once
                        .with(ItemEntry.builder(ModItems.GREEN_BEAN_SEEDS)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)))) // Drop 1 to 2 seeds
                        .conditionally(SurvivesExplosionLootCondition.builder()) // Only drop if not destroyed by explosion
                )
        );

    }


    // Keep your existing multipleOreDrops helper method if needed elsewhere
    public LootTable.Builder multipleOreDrops(Block drop, Item item, float minDrops, float maxDrops) {
        RegistryWrapper.Impl<Enchantment> impl = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
        return this.dropsWithSilkTouch(drop, this.applyExplosionDecay(drop, ((LeafEntry.Builder<?>)
                ItemEntry.builder(item).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(minDrops, maxDrops))))
                .apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE)))));
    }
}