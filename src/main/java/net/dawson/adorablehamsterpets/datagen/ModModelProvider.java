package net.dawson.adorablehamsterpets.datagen;

import net.dawson.adorablehamsterpets.block.ModBlocks;
import net.dawson.adorablehamsterpets.block.custom.*;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }


    //generates block_states.json file, block_model.json file, and the item_model.json file for all blocks
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

        blockStateModelGenerator.registerCrop(ModBlocks.GREEN_BEANS_CROP, GreenBeansCropBlock.AGE, 0, 1, 2, 3, 4, 5, 6);
        blockStateModelGenerator.registerCrop(ModBlocks.CUCUMBER_CROP, CucumberCropBlock.AGE, 0, 1, 2, 3, 4, 5, 6);

    }


    //generates model json files for all items
    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

        itemModelGenerator.register(ModItems.CUCUMBER, Models.GENERATED);
        itemModelGenerator.register(ModItems.SLICED_CUCUMBER, Models.GENERATED);
        itemModelGenerator.register(ModItems.GREEN_BEANS, Models.GENERATED);
        itemModelGenerator.register(ModItems.STEAMED_GREEN_BEANS, Models.GENERATED);
        itemModelGenerator.register(ModItems.CHEESE, Models.GENERATED);
        itemModelGenerator.register(ModItems.HAMSTER_FOOD_MIX, Models.GENERATED);
        itemModelGenerator.register(ModItems.SUNFLOWER_SEEDS, Models.GENERATED);
        itemModelGenerator.register(ModItems.HAMSTER_SPAWN_EGG,
                new Model(Optional.of(Identifier.of("item/template_spawn_egg")), Optional.empty()));

    }
}
