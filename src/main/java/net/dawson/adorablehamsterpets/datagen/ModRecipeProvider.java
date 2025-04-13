
package net.dawson.adorablehamsterpets.datagen;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.block.ModBlocks;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }


    @Override
    public void generate(RecipeExporter recipeExporter) {
        List<ItemConvertible> HAMSTER_SMELTABLES = List.of(ModItems.STEAMED_GREEN_BEANS, ModItems.GREEN_BEANS);


        // This is a list of things that, when smelted or blasted, steamed green beans
        offerSmelting(recipeExporter, HAMSTER_SMELTABLES, RecipeCategory.FOOD, ModItems.STEAMED_GREEN_BEANS, 0.25f, 200, "hamster");


        //RESULT = 1 HAMSTER FOOD MIX
        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, ModItems.HAMSTER_FOOD_MIX)
                .pattern("SSS")
                .pattern("PCP")
                .pattern("WWW")
                .input('S', ModItems.SUNFLOWER_SEEDS)
                .input('P', Items.PUMPKIN_SEEDS)
                .input('C', Items.CARROT)
                .input('W', Items.WHEAT)
                //this says that the recipe will unlock once you've had sunflower seeds in your inventory
                .criterion(hasItem(ModItems.SUNFLOWER_SEEDS), conditionsFromItem(ModItems.SUNFLOWER_SEEDS))
                .offerTo(recipeExporter);


        //RESULT = 3 SLICED CUCUMBER
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, ModItems.SLICED_CUCUMBER, 3)
                .input(ModItems.CUCUMBER)
                .criterion(hasItem(ModItems.CUCUMBER), conditionsFromItem(ModItems.CUCUMBER))
                .offerTo(recipeExporter);

        //RESULT = 3 CHEESE
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, ModItems.CHEESE, 3)
                .input(Items.MILK_BUCKET)
                .criterion(hasItem(Items.MILK_BUCKET), conditionsFromItem(Items.MILK_BUCKET))
                .offerTo(recipeExporter);

    }
}
