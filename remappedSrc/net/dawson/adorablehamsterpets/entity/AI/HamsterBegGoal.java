package net.dawson.adorablehamsterpets.entity.AI;

import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;

/**
 * Similar to WolfBegGoal. Responsible ONLY for toggling the hamster's "begging" state
 * if a nearby player is holding SLICED_CUCUMBER. This ensures "begging" is near-instant.
 */
public class HamsterBegGoal extends Goal {

    private final HamsterEntity hamster;
    private PlayerEntity closestPlayer;
    // We can keep the same distance as Wolf, e.g., 8.0F
    private static final double BEG_RANGE = 8.0D;

    public HamsterBegGoal(HamsterEntity hamster) {
        this.hamster = hamster;
        // We don't need to control movement, just want to check conditions each tick
        this.setControls(EnumSet.of(Control.LOOK));
    }

    @Override
    public boolean canStart() {
        // If hamster is dead or not in a state to do anything, skip
        if (!hamster.isAlive()) {
            return false;
        }
        // Find the closest player within BEG_RANGE who is holding SLICED_CUCUMBER
        World world = hamster.getWorld();
        List<PlayerEntity> nearbyPlayers = world.getEntitiesByClass(
                PlayerEntity.class,
                new Box(hamster.getBlockPos()).expand(BEG_RANGE),
                player -> isHoldingCucumber(player)
        );

        if (nearbyPlayers.isEmpty()) {
            return false;
        }
        // Just pick the first or any
        this.closestPlayer = nearbyPlayers.get(0);
        return true;
    }

    @Override
    public boolean shouldContinue() {
        // Keep going if that player is still around and still holding cucumber
        if (this.closestPlayer == null || !this.closestPlayer.isAlive()) {
            return false;
        }
        // If out of range or no longer holding SLICED_CUCUMBER, we stop
        if (this.hamster.distanceTo(this.closestPlayer) > BEG_RANGE) {
            return false;
        }
        return isHoldingCucumber(this.closestPlayer);
    }

    @Override
    public void start() {
        // Begin begging
        this.hamster.setBegging(true);
    }

    @Override
    public void stop() {
        // If the goal is no longer active, hamster stops begging
        this.hamster.setBegging(false);
        this.closestPlayer = null;
    }

    @Override
    public void tick() {
        // (Optional) you can rotate to face the player
        if (this.closestPlayer != null) {
            this.hamster.getLookControl().lookAt(this.closestPlayer, 30F, 30F);
        }
    }

    private boolean isHoldingCucumber(PlayerEntity player) {
        ItemStack main = player.getMainHandStack();
        ItemStack off = player.getOffHandStack();
        // Return true if either hand is SLICED_CUCUMBER
        return main.isOf(ModItems.SLICED_CUCUMBER) || off.isOf(ModItems.SLICED_CUCUMBER);
    }
}
