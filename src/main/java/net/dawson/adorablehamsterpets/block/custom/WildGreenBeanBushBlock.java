package net.dawson.adorablehamsterpets.block.custom;

import com.mojang.serialization.MapCodec;
import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.config.ModConfig;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;

public class WildGreenBeanBushBlock extends PlantBlock {
    // Define a MapCodec for serialization (required for custom blocks extending vanilla ones)
    public static final MapCodec<WildGreenBeanBushBlock> CODEC = createCodec(WildGreenBeanBushBlock::new);

    // Define the property to track if the bush has seeds
    public static final BooleanProperty SEEDED = BooleanProperty.of("seeded");

    // Define the shapes for the different states (adjust values as needed for your model/texture)
    // Shape when it has no seeds (e.g., smaller)
    private static final VoxelShape SEEDLESS_SHAPE = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);
    // Shape when it has seeds (e.g., fuller)
    private static final VoxelShape SEEDED_SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    // Constructor
    public WildGreenBeanBushBlock(Settings settings) {
        super(settings);
        // Set the default state to be seeded when placed
        this.setDefaultState(this.stateManager.getDefaultState().with(SEEDED, true));
    }

    // Return the MapCodec
    @Override
    public MapCodec<WildGreenBeanBushBlock> getCodec() {
        return CODEC;
    }

    // Define which item to return when using the pick block action (middle mouse click)
    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.GREEN_BEAN_SEEDS);
    }

    // Define the outline shape based on the SEEDED state
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(SEEDED) ? SEEDED_SHAPE : SEEDLESS_SHAPE;
    }

    // The block should only tick randomly when it's seedless (to try and regrow)
    @Override
    public boolean hasRandomTicks(BlockState state) {
        return !state.get(SEEDED);
    }

    // Handle the random tick logic (for regrowing seeds)
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.get(SEEDED)) {
            // --- Use Config Value for Regrowth Chance ---
            final ModConfig config = AdorableHamsterPets.CONFIG; // Access static config
            double modifier = config.worldGen.wildBushRegrowthModifier();
            modifier = Math.max(0.1, modifier); // Ensure positive modifier

            int baseRegrowthChanceDenominator = 5; // Default 1 in 5 chance
            int effectiveDenominator = (int) Math.round(baseRegrowthChanceDenominator * modifier);
            effectiveDenominator = Math.max(1, effectiveDenominator); // Ensure at least 1

            if (world.getBaseLightLevel(pos.up(), 0) >= 9 && random.nextInt(effectiveDenominator) == 0) {
                BlockState newState = state.with(SEEDED, true);
                world.setBlockState(pos, newState, Block.NOTIFY_LISTENERS);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(newState));
            }
        }
    }

    // Handle right-click interaction (harvesting)
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        // Only proceed if the bush is currently seeded
        if (state.get(SEEDED)) {
            // Server-side logic
            if (!world.isClient) {
                // Drop 1 or 2 seeds (random.nextInt(2) gives 0 or 1, add 1)
                int seedAmount = 1 + world.random.nextInt(2);
                dropStack(world, pos, new ItemStack(ModItems.GREEN_BEAN_SEEDS, seedAmount));

                // Play the picking sound
                world.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);

                // Set the state to seedless
                BlockState newState = state.with(SEEDED, false);
                world.setBlockState(pos, newState, Block.NOTIFY_LISTENERS); // Notify listeners of the change

                // Emit a game event
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, newState));

                return ActionResult.SUCCESS; // Action was successful on the server
            }
            // Client-side: still return success to indicate the interaction was handled
            // (prevents swinging arm if clicking air, etc.)
            return ActionResult.success(world.isClient);
        }

        // If not seeded, pass the interaction along (might be useful for other interactions later)
        return ActionResult.PASS;
    }

    // Add the SEEDED property to the block's state manager
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SEEDED);
    }

    // Override canPlantOnTop to specify valid ground (similar to PlantBlock default, but explicit)
    // This ensures it can only be placed on typical plant-supporting blocks.
    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(Blocks.GRASS_BLOCK) || floor.isOf(Blocks.DIRT) || floor.isOf(Blocks.COARSE_DIRT)
                || floor.isOf(Blocks.PODZOL) || floor.isOf(Blocks.FARMLAND);
    }
}