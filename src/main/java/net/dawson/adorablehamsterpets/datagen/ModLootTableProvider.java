package net.dawson.adorablehamsterpets.datagen;

import net.dawson.adorablehamsterpets.block.ModBlocks;
import net.dawson.adorablehamsterpets.block.custom.CauliflowerCropBlock;
import net.dawson.adorablehamsterpets.block.custom.CucumberCropBlock;
import net.dawson.adorablehamsterpets.block.custom.GreenBeansCropBlock;
import net.dawson.adorablehamsterpets.block.custom.HoneyBerryBushBlock;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {

        //this line is necessary for the honey berry bush but otherwise not needed
        RegistryWrapper.Impl<Enchantment> impl = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
        //this says that these blocks will drop themselves
        addDrop(ModBlocks.PINK_GARNET_BLOCK);
        addDrop(ModBlocks.RAW_PINK_GARNET_BLOCK);
        addDrop(ModBlocks.MAGIC_BLOCK);
        addDrop(ModBlocks.PINK_GARNET_STAIRS);
        addDrop(ModBlocks.PINK_GARNET_BUTTON);
        addDrop(ModBlocks.PINK_GARNET_PRESSURE_PLATE);
        addDrop(ModBlocks.PINK_GARNET_WALL);
        addDrop(ModBlocks.PINK_GARNET_FENCE);
        addDrop(ModBlocks.PINK_GARNET_FENCE_GATE);
        addDrop(ModBlocks.PINK_GARNET_TRAPDOOR);
        //this says that when you mine these ore blocks, it will drop raw pink garnet
        addDrop(ModBlocks.PINK_GARNET_ORE, oreDrops(ModBlocks.PINK_GARNET_ORE, ModItems.RAW_PINK_GARNET));
        addDrop(ModBlocks.PINK_GARNET_DEEPSLATE_ORE, multipleOreDrops(ModBlocks.PINK_GARNET_DEEPSLATE_ORE, ModItems.RAW_PINK_GARNET, 3, 7));
        addDrop(ModBlocks.PINK_GARNET_END_ORE, multipleOreDrops(ModBlocks.PINK_GARNET_END_ORE, ModItems.RAW_PINK_GARNET, 4, 9));
        addDrop(ModBlocks.PINK_GARNET_NETHER_ORE, multipleOreDrops(ModBlocks.PINK_GARNET_NETHER_ORE, ModItems.RAW_PINK_GARNET, 3, 8));
        addDrop(ModBlocks.DRIFTWOOD_LOG);
        addDrop(ModBlocks.DRIFTWOOD_WOOD);
        addDrop(ModBlocks.STRIPPED_DRIFTWOOD_LOG);
        addDrop(ModBlocks.STRIPPED_DRIFTWOOD_WOOD);
        addDrop(ModBlocks.DRIFTWOOD_PLANKS);
        addDrop(ModBlocks.DRIFTWOOD_SAPLING);
        //this one is special because it needs to drop leaves when you use sheers, and there's a chance of getting a sapling when you don't
        addDrop(ModBlocks.DRIFTWOOD_LEAVES, leavesDrops(ModBlocks.DRIFTWOOD_LEAVES, ModBlocks.DRIFTWOOD_SAPLING, 0.0625f));
        this.addDrop(ModBlocks.HONEY_BERRY_BUSH,
                block -> this.applyExplosionDecay(block, LootTable.builder().pool(LootPool.builder()
                                                .conditionally(BlockStatePropertyLootCondition.builder(ModBlocks.HONEY_BERRY_BUSH)
                                                        .properties(StatePredicate.Builder.create().exactMatch(HoneyBerryBushBlock.AGE, 3)))
                                                .with(ItemEntry.builder(ModItems.HONEY_BERRIES))
                                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 3.0F)))
                                                .apply(ApplyBonusLootFunction.uniformBonusCount(impl.getOrThrow(Enchantments.FORTUNE)))).pool(LootPool.builder()
                                                .conditionally(BlockStatePropertyLootCondition.builder(ModBlocks.HONEY_BERRY_BUSH)
                                                        .properties(StatePredicate.Builder.create().exactMatch(HoneyBerryBushBlock.AGE, 2)))
                                                .with(ItemEntry.builder(ModItems.HONEY_BERRIES))
                                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F)))
                                                .apply(ApplyBonusLootFunction.uniformBonusCount(impl.getOrThrow(Enchantments.FORTUNE))))));
        //this says that if the cauliflower crop block is fully grown (age 6), only then will it drop the cauliflower item
        BlockStatePropertyLootCondition.Builder builder2 = BlockStatePropertyLootCondition.builder(ModBlocks.CAULIFLOWER_CROP)
                .properties(StatePredicate.Builder.create().exactMatch(CauliflowerCropBlock.AGE, CauliflowerCropBlock.MAX_AGE));
        this.addDrop(ModBlocks.CAULIFLOWER_CROP, this.cropDrops(ModBlocks.CAULIFLOWER_CROP, ModItems.CAULIFLOWER, ModItems.CAULIFLOWER_SEEDS, builder2));
        //this is for making sure you get two slab drops, and ensuring you only get one door drop since a door technically occupies two blocks
        addDrop(ModBlocks.PINK_GARNET_DOOR, doorDrops(ModBlocks.PINK_GARNET_DOOR));
        addDrop(ModBlocks.PINK_GARNET_SLAB, slabDrops(ModBlocks.PINK_GARNET_SLAB));


        //this says that if the green beans crop block is fully grown (age 6), only then will it drop the green beans item
        BlockStatePropertyLootCondition.Builder builder3 = BlockStatePropertyLootCondition.builder(ModBlocks.GREEN_BEANS_CROP)
                .properties(StatePredicate.Builder.create().exactMatch(GreenBeansCropBlock.AGE, GreenBeansCropBlock.MAX_AGE));
        this.addDrop(ModBlocks.GREEN_BEANS_CROP, this.cropDrops(ModBlocks.GREEN_BEANS_CROP, ModItems.GREEN_BEANS, ModItems.GREEN_BEAN_SEEDS, builder3));

        //same thing as above but for the cucumber
        BlockStatePropertyLootCondition.Builder builder4 = BlockStatePropertyLootCondition.builder(ModBlocks.CUCUMBER_CROP)
                .properties(StatePredicate.Builder.create().exactMatch(CucumberCropBlock.AGE, CucumberCropBlock.MAX_AGE));
        this.addDrop(ModBlocks.CUCUMBER_CROP, this.cropDrops(ModBlocks.CUCUMBER_CROP, ModItems.CUCUMBER, ModItems.CUCUMBER_SEEDS, builder4));

        //this says that this block will drop itself
        addDrop(ModBlocks.CHEESE_BLOCK);

    }

    //this is copied over from copperOreDrops, this now allows us to define an item that has a minimum and maximum amount of drops
    public LootTable.Builder multipleOreDrops(Block drop, Item item, float minDrops, float maxDrops) {
        RegistryWrapper.Impl<Enchantment> impl = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
        return this.dropsWithSilkTouch(drop, this.applyExplosionDecay(drop, ((LeafEntry.Builder<?>)
                ItemEntry.builder(item).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(minDrops, maxDrops))))
                .apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE)))));
    }
}
