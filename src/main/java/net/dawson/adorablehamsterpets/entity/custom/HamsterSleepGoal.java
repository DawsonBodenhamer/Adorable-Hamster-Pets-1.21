package net.dawson.adorablehamsterpets.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class HamsterSleepGoal extends Goal {
    private final HamsterEntity hamster;

    public HamsterSleepGoal(HamsterEntity hamster) {
        this.hamster = hamster;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (hamster.isTamed()) {
            // Tamed => only sleep if externally set
            return hamster.isSleeping();
        } else {
            // Wild => Sleep if day and no immediate threats
            if (!hamster.getWorld().isDay()) {
                return false;
            }
            double radius = 5.0;
            boolean threatNearby = !hamster.getWorld().getOtherEntities(
                    hamster,
                    hamster.getBoundingBox().expand(radius),
                    this::isThreat
            ).isEmpty();
            return !threatNearby;
        }
    }

    private boolean isThreat(Entity entity) {
        // If it's a hostile mob or a player => threat
        if (entity instanceof HostileEntity) {
            return true;
        }
        return (entity instanceof PlayerEntity);
    }

    @Override
    public void start() {
        hamster.getNavigation().stop();
        if (!hamster.isTamed()) {
            hamster.setSleeping(true);
        }
    }

    @Override
    public boolean shouldContinue() {
        if (hamster.isTamed()) {
            return hamster.isSleeping();
        } else {
            // Continue if day + no threat
            return hamster.getWorld().isDay();
        }
    }

    @Override
    public void stop() {
        if (!hamster.isTamed()) {
            hamster.setSleeping(false);
        }
    }
}
