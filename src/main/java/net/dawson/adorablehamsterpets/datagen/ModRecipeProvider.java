
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


        //creates json recipe files.


        // This is a list of things that, when smelted or blasted, steamed green beans
        offerSmelting(recipeExporter, HAMSTER_SMELTABLES, RecipeCategory.FOOD, ModItems.STEAMED_GREEN_BEANS, 0.25f, 200, "hamster");


//        //creates shapeless json recipe files. this says that if you place 9 pink garnets in a crafting grid, you'll get 1 pink garnet block
//        offerReversibleCompactingRecipes(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModItems.PINK_GARNET, RecipeCategory.DECORATIONS, ModBlocks.PINK_GARNET_BLOCK);



//        //creates shaped json recipe files.
//        //this says that if you place R in this particular pattern, you get raw pink garnet block.
//        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.RAW_PINK_GARNET_BLOCK)
//                .pattern("RRR")
//                .pattern("RRR")
//                .pattern("RRR")
//                .input('R', ModItems.RAW_PINK_GARNET)
//                //this says that the recipe will unlock once you've had a raw pink garnet in your inventory
//                .criterion(hasItem(ModItems.RAW_PINK_GARNET), conditionsFromItem(ModItems.RAW_PINK_GARNET))
//                .offerTo(recipeExporter);
//
//        //this recipe outputs a pink garnet sword
//        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.PINK_GARNET_SWORD)
//                .pattern(" R ")
//                .pattern(" R ")
//                .pattern(" S ")
//                .input('R', ModItems.RAW_PINK_GARNET)
//                .input('S', Items.STICK)
//                .criterion(hasItem(ModItems.RAW_PINK_GARNET), conditionsFromItem(ModItems.RAW_PINK_GARNET))
//                .offerTo(recipeExporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.PINK_GARNET_PICKAXE)
//                .pattern("RRR")
//                .pattern(" S ")
//                .pattern(" S ")
//                .input('R', ModItems.RAW_PINK_GARNET)
//                .input('S', Items.STICK)
//                .criterion(hasItem(ModItems.RAW_PINK_GARNET), conditionsFromItem(ModItems.RAW_PINK_GARNET))
//                .offerTo(recipeExporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.PINK_GARNET_SHOVEL)
//                .pattern(" R ")
//                .pattern(" S ")
//                .pattern(" S ")
//                .input('R', ModItems.RAW_PINK_GARNET)
//                .input('S', Items.STICK)
//                .criterion(hasItem(ModItems.RAW_PINK_GARNET), conditionsFromItem(ModItems.RAW_PINK_GARNET))
//                .offerTo(recipeExporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.PINK_GARNET_AXE)
//                .pattern(" RR")
//                .pattern(" SR")
//                .pattern(" S ")
//                .input('R', ModItems.RAW_PINK_GARNET)
//                .input('S', Items.STICK)
//                .criterion(hasItem(ModItems.RAW_PINK_GARNET), conditionsFromItem(ModItems.RAW_PINK_GARNET))
//                .offerTo(recipeExporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.PINK_GARNET_HOE)
//                .pattern("RR ")
//                .pattern(" S ")
//                .pattern(" S ")
//                .input('R', ModItems.RAW_PINK_GARNET)
//                .input('S', Items.STICK)
//                .criterion(hasItem(ModItems.RAW_PINK_GARNET), conditionsFromItem(ModItems.RAW_PINK_GARNET))
//                .offerTo(recipeExporter);



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


//        //this is the reverse of the above recipe, but done in a shapeless way. this says if you place 1 raw pink garnet block in a crafting grid, you'll get 9 raw pink garnet
//        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.RAW_PINK_GARNET, 9)
//                .input(ModBlocks.RAW_PINK_GARNET_BLOCK)
//                .criterion(hasItem(ModBlocks.RAW_PINK_GARNET_BLOCK), conditionsFromItem(ModBlocks.RAW_PINK_GARNET_BLOCK))
//                .offerTo(recipeExporter);

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


//        //here's what you do if you want to have two things that result in the same item. for example this says that if you input a magic block, you'll get 32 raw pink garnets.
//        //this recipe needs an additional identifier to ensure there are not two recipes with the same name
//        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.RAW_PINK_GARNET, 32)
//                .input(ModBlocks.MAGIC_BLOCK)
//                .criterion(hasItem(ModBlocks.MAGIC_BLOCK), conditionsFromItem(ModBlocks.MAGIC_BLOCK))
//                //additional identifier
//                .offerTo(recipeExporter, Identifier.of(AdorableHamsterPets.MOD_ID, "raw_pink_garnet_from_magic_block"));
//
//        offerSmithingTrimRecipe(recipeExporter, ModItems.DAWSON_SMITHING_TEMPLATE, Identifier.of(AdorableHamsterPets.MOD_ID, "dawson"));

    }
}
