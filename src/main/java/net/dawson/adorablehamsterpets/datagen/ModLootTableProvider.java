package net.dawson.adorablehamsterpets.datagen;

import net.dawson.adorablehamsterpets.block.ModBlocks;
import net.dawson.adorablehamsterpets.block.custom.CucumberCropBlock;
import net.dawson.adorablehamsterpets.block.custom.GreenBeansCropBlock;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
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

        //this says that if the green beans crop block is fully grown (age 6), only then will it drop the green beans item
        BlockStatePropertyLootCondition.Builder builder3 = BlockStatePropertyLootCondition.builder(ModBlocks.GREEN_BEANS_CROP)
                .properties(StatePredicate.Builder.create().exactMatch(GreenBeansCropBlock.AGE, GreenBeansCropBlock.MAX_AGE));
        this.addDrop(ModBlocks.GREEN_BEANS_CROP, this.cropDrops(ModBlocks.GREEN_BEANS_CROP, ModItems.GREEN_BEANS, ModItems.GREEN_BEAN_SEEDS, builder3));

        //same thing as above but for the cucumber
        BlockStatePropertyLootCondition.Builder builder4 = BlockStatePropertyLootCondition.builder(ModBlocks.CUCUMBER_CROP)
                .properties(StatePredicate.Builder.create().exactMatch(CucumberCropBlock.AGE, CucumberCropBlock.MAX_AGE));
        this.addDrop(ModBlocks.CUCUMBER_CROP, this.cropDrops(ModBlocks.CUCUMBER_CROP, ModItems.CUCUMBER, ModItems.CUCUMBER_SEEDS, builder4));

//        //this says that this block will drop itself
//        addDrop(ModBlocks.CHEESE_BLOCK);

    }

    //this is copied over from copperOreDrops, this now allows us to define an item that has a minimum and maximum amount of drops
    public LootTable.Builder multipleOreDrops(Block drop, Item item, float minDrops, float maxDrops) {
        RegistryWrapper.Impl<Enchantment> impl = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
        return this.dropsWithSilkTouch(drop, this.applyExplosionDecay(drop, ((LeafEntry.Builder<?>)
                ItemEntry.builder(item).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(minDrops, maxDrops))))
                .apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE)))));
    }
}
