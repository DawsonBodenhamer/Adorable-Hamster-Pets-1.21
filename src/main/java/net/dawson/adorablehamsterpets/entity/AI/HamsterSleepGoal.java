package net.dawson.adorablehamsterpets.entity.AI;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity; // <-- Import HamsterEntity for IS_SITTING
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
            return false;
        } else {
            if (!hamster.getWorld().isDay()) {
                return false;
            }
            if (hamster.isSitting()) {
                return false;
            }

            double radius = 5.0;
            boolean threatNearby = !hamster.getWorld().getOtherEntities(
                    hamster,
                    hamster.getBoundingBox().expand(radius),
                    this::isThreat
            ).isEmpty();
            boolean canSleep = !threatNearby;

            AdorableHamsterPets.LOGGER.info("HamsterSleepGoal - canStart: Day=" + hamster.getWorld().isDay() + ", isSitting=" + hamster.isSitting() + ", ThreatNearby=" + threatNearby + ", CanSleep=" + canSleep);

            return canSleep;
        }
    }

    private boolean isThreat(Entity entity) {
        if (entity instanceof HostileEntity) {
            return true;
        }
        return (entity instanceof PlayerEntity);
    }

    @Override
    public void start() {
        hamster.getNavigation().stop();
        if (!hamster.isTamed()) {
            AdorableHamsterPets.LOGGER.info("HamsterSleepGoal - start: Setting isSitting(true)");
            hamster.setSitting(true);
            hamster.setSleeping(true);

            // --- CORRECT DATA SYNC METHOD ---
            hamster.getDataTracker().set(HamsterEntity.IS_SITTING, true); // <---- USE dataTracker.set()
            // hamster.dataTracker.sendDirty();  <---- NO NEED TO CALL sendDirty() MANUALLY

            AdorableHamsterPets.LOGGER.info("HamsterSleepGoal - start: isSitting now = " + hamster.isSitting());
        }
    }

    @Override
    public boolean shouldContinue() {
        if (hamster.isTamed()) {
            return false;
        } else {
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

    @Override
    public void stop() {
        if (!hamster.isTamed()) {
            AdorableHamsterPets.LOGGER.info("HamsterSleepGoal - stop: Setting isSitting(false)");
            hamster.setSitting(false);
            hamster.setSleeping(false);
            // --- CORRECT DATA SYNC METHOD ---
            hamster.getDataTracker().set(HamsterEntity.IS_SITTING, false); // <---- USE dataTracker.set()
            // hamster.dataTracker.sendDirty();  <---- NO NEED TO CALL sendDirty() manually
        }
    }
}