package net.dawson.adorablehamsterpets.block.custom;

import com.mojang.serialization.MapCodec;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity; // Import LivingEntity
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
// Removed Properties import as we only use HALF from TallPlantBlock now
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random; // Import Random
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable; // Import Nullable

public class SunflowerBlock extends TallFlowerBlock implements Fertilizable { // Keep Fertilizable if you keep those methods

    public static final BooleanProperty HAS_SEEDS = BooleanProperty.of("has_seeds");
    public static final MapCodec<TallFlowerBlock> CODEC = TallFlowerBlock.createCodec(SunflowerBlock::new); // Use superclass codec method

    public SunflowerBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(HALF, DoubleBlockHalf.LOWER)
                .with(HAS_SEEDS, true));
    }

    @Override
    public MapCodec<TallFlowerBlock> getCodec() {
        // Return the codec defined in the superclass or a specific one if needed
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HAS_SEEDS);
    }

    // --- Random Tick Logic ---
    @Override
    public boolean hasRandomTicks(BlockState state) {
        // Only the top half needs random ticks, and only when it doesn't have seeds
        return state.get(HALF) == DoubleBlockHalf.UPPER && !state.get(HAS_SEEDS);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // Check conditions again just to be safe
        if (state.get(HALF) == DoubleBlockHalf.UPPER && !state.get(HAS_SEEDS)) {
            // --- Regrowth Chance ---
            // Higher number = slower average regrowth. TUNING REQUIRED!
            int regrowthChanceDenominator = 150; // Initial guess, adjust based on testing

            if (random.nextInt(regrowthChanceDenominator) == 0) {
                // Regrowth successful! Set state back to having seeds.
                world.setBlockState(pos, state.with(HAS_SEEDS, true), Block.NOTIFY_LISTENERS);
            }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (state.get(HALF) != DoubleBlockHalf.UPPER) {
            if (state.get(HALF) == DoubleBlockHalf.LOWER) {
                BlockPos topPos = pos.up();
                BlockState topState = world.getBlockState(topPos);
                if (topState.isOf(this) && topState.get(HALF) == DoubleBlockHalf.UPPER) {
                    // Use explicit cast that worked before
                    return ((SunflowerBlock)topState.getBlock()).onUse(topState, world, topPos, player, hit);
                }
            }
            return ActionResult.PASS;
        }

        if (!world.isClient) {
            if (state.get(HAS_SEEDS)) {
                int seedAmount = world.random.nextInt(3) + 1;
                ItemStack seedStack = new ItemStack(ModItems.SUNFLOWER_SEEDS, seedAmount);
                ItemScatterer.spawn(world, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, seedStack);

                world.setBlockState(pos, state.with(HAS_SEEDS, false), Block.NOTIFY_LISTENERS);
                // REMOVED world.scheduleBlockTick(...)

                world.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0f, 1.0f);

                return ActionResult.SUCCESS;
            }
        } else {
            if (state.get(HAS_SEEDS)) {
                return ActionResult.CONSUME;
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        // Let the parent class place the top half FIRST
        super.onPlaced(world, pos, state, placer, itemStack);

        // Now, find the top half and modify its state if on the server
        if (!world.isClient) {
            BlockPos topPos = pos.up();
            BlockState topState = world.getBlockState(topPos);

            if (topState.isOf(this) && topState.get(HALF) == DoubleBlockHalf.UPPER) {
                // Set the state to NO seeds initially
                world.setBlockState(topPos, topState.with(HAS_SEEDS, false), Block.NOTIFY_LISTENERS);
                // REMOVED world.scheduleBlockTick(...) - Random ticks will handle regrowth now
            }
        }
    }


    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(Items.SUNFLOWER);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        // Keep vanilla behavior or return false to disable bonemeal
        return state.get(HALF) == DoubleBlockHalf.LOWER && super.isFertilizable(world, pos, state);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        // Keep vanilla behavior or return false
        return state.get(HALF) == DoubleBlockHalf.LOWER && super.canGrow(world, random, pos, state);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            super.grow(world, random, pos, state);
            // Ensure the newly placed top half starts WITHOUT seeds and relies on random ticks
            BlockPos topPos = pos.up();
            BlockState topState = world.getBlockState(topPos);
            if (topState.isOf(this) && topState.get(HALF) == DoubleBlockHalf.UPPER) {
                world.setBlockState(topPos, topState.with(HAS_SEEDS, false), Block.NOTIFY_LISTENERS);
            }
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        BlockState updatedState = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        if (updatedState.isAir()) {
            return updatedState;
        }
        return updatedState;
    }
}