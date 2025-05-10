package net.dawson.adorablehamsterpets.block.custom;

import com.mojang.serialization.MapCodec;
import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.config.ModConfig;
import net.dawson.adorablehamsterpets.item.ModItems; // Ensure this points to your ModItems
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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

// Renamed class
public class WildCucumberBushBlock extends PlantBlock {
    // Updated MapCodec reference
    public static final MapCodec<WildCucumberBushBlock> CODEC = createCodec(WildCucumberBushBlock::new);

    // Same property name, conceptually represents if it has cucumbers ready
    public static final BooleanProperty SEEDED = BooleanProperty.of("seeded");

    // Shapes (using the same placeholders as before, adjust if needed)
    private static final VoxelShape SEEDLESS_SHAPE = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);
    private static final VoxelShape SEEDED_SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    // Constructor
    public WildCucumberBushBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(SEEDED, true));
    }

    // Return the updated MapCodec
    @Override
    public MapCodec<WildCucumberBushBlock> getCodec() {
        return CODEC;
    }

    // Pick block should give CUCUMBER_SEEDS
    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.CUCUMBER_SEEDS); // Changed item
    }

    // Outline shape logic remains the same
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(SEEDED) ? SEEDED_SHAPE : SEEDLESS_SHAPE;
    }

    // Random tick logic remains the same (only ticks when seedless)
    @Override
    public boolean hasRandomTicks(BlockState state) {
        return !state.get(SEEDED);
    }

    // Random tick regrowth logic remains the same
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

    // onUse logic adapted for Cucumber Seeds
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (state.get(SEEDED)) {
            if (!world.isClient) {
                // Drop 1 or 2 CUCUMBER seeds
                int seedAmount = 1 + world.random.nextInt(2);
                dropStack(world, pos, new ItemStack(ModItems.CUCUMBER_SEEDS, seedAmount)); // Changed item

                // Play the same sound
                world.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);

                // Set state to seedless
                BlockState newState = state.with(SEEDED, false);
                world.setBlockState(pos, newState, Block.NOTIFY_LISTENERS);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, newState));

                return ActionResult.SUCCESS;
            }
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }

    // Append properties remains the same
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SEEDED);
    }

    // Planting conditions remain the same
    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(Blocks.GRASS_BLOCK) || floor.isOf(Blocks.DIRT) || floor.isOf(Blocks.COARSE_DIRT)
                || floor.isOf(Blocks.PODZOL) || floor.isOf(Blocks.FARMLAND);
    }
}