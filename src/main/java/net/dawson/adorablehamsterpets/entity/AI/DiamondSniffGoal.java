package net.dawson.adorablehamsterpets.entity.AI;

import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.EnumSet;

public class DiamondSniffGoal extends Goal {
    private final HamsterEntity hamster;
    // Check diamond every 2 seconds
    private static final int CHECK_INTERVAL = 40;
    private int timer;

    public DiamondSniffGoal(HamsterEntity hamster) {
        this.hamster = hamster;
        this.setControls(EnumSet.noneOf(Control.class));
    }

    @Override
    public boolean canStart() {
        // Only start if hamster is riding (mounted on) a player
        return hamster.hasVehicle() && hamster.getVehicle() instanceof PlayerEntity;
    }

    @Override
    public boolean shouldContinue() {
        // Continue as long as hamster remains on a player's shoulder
        return canStart();
    }

    @Override
    public void start() {
        // reset
        timer = 0;
        // optional: setNearDiamond(false) initially
        hamster.setNearDiamond(false);
    }

    @Override
    public void stop() {
        // Once goal ends, you can turn nearDiamond off, or keep it.
        hamster.setNearDiamond(false);
    }

    @Override
    public void tick() {
        timer++;
        if (timer >= CHECK_INTERVAL) {
            timer = 0;
            PlayerEntity rider = (PlayerEntity) hamster.getVehicle();
            if (rider == null) {
                // fallback
                hamster.setNearDiamond(false);
                return;
            }
            // Check for diamond ore:
            boolean found = isDiamondNearby(rider);
            hamster.setNearDiamond(found);
        }
    }

    private boolean isDiamondNearby(PlayerEntity player) {
        // If you want an actual 5×5×5 around the player, a radius of 2 is correct
        int radius = 2;
        // For a 10×10×10, set radius=5, etc.

        World world = player.getWorld();
        BlockPos center = player.getBlockPos();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockPos check = center.add(dx, dy, dz);
                    if (world.getBlockState(check).isOf(Blocks.DIAMOND_ORE)
                            || world.getBlockState(check).isOf(Blocks.DEEPSLATE_DIAMOND_ORE)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
