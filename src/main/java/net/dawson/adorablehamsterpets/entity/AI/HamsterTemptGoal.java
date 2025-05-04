package net.dawson.adorablehamsterpets.entity.AI;

import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class HamsterTemptGoal extends TemptGoal {
    private final HamsterEntity hamster;
    private final Predicate<ItemStack> temptPredicate;
    private int recheckTimer = 0;

    public HamsterTemptGoal(HamsterEntity hamster, double speed, Predicate<ItemStack> predicate, boolean canBeScared) {
        super(hamster, speed, predicate, canBeScared);
        this.hamster = hamster;
        this.temptPredicate = predicate;
    }

    @Override
    public void tick() {
        super.tick(); // Handles movement towards player


        // Re-check begging state frequently
        if (recheckTimer > 0) {
            recheckTimer--;
            return;
        }
        recheckTimer = 5; // Re-check roughly every 5 ticks


        World world = this.hamster.getWorld();
        if (world.isClient()) {
            return;
        }

        PlayerEntity target = this.closestPlayer; // Get the player targeted by the parent goal


        if (target != null && target.isAlive() && this.hamster.squaredDistanceTo(target) < 64.0) { // Check range
            // Set begging based on whether the *target* player is holding the item
            this.hamster.setBegging(isHoldingTemptItem(target));
        } else {
            // If no valid target player, ensure begging is off
            this.hamster.setBegging(false);
        }
    }

    @Override
    public void stop() {
        super.stop(); // Call parent stop logic
        // Explicitly ensure begging state is false when the goal stops for any reason
        this.hamster.setBegging(false);
        this.recheckTimer = 0; // Reset timer
    }

    // Helper method
    private boolean isHoldingTemptItem(PlayerEntity player) {
        ItemStack main = player.getMainHandStack();
        ItemStack off = player.getOffHandStack();
        return temptPredicate.test(main) || temptPredicate.test(off);
    }
}