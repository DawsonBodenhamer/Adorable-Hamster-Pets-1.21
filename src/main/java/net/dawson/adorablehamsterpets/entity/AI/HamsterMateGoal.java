package net.dawson.adorablehamsterpets.entity.AI;

import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.server.world.ServerWorld;

import java.util.EnumSet;

public class HamsterMateGoal extends Goal {
    private final HamsterEntity hamster;
    private HamsterEntity targetMate;
    private final double speed;
    private int timer;

    public HamsterMateGoal(HamsterEntity hamster, double speed) {
        this.hamster = hamster;
        this.speed = speed;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        // Must be an adult, in custom love, etc.
        if (this.hamster.isInCustomLove()) {
            // Find a mate
            this.targetMate = this.getNearbyMate();
            return this.targetMate != null;
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        return this.targetMate != null
                && this.targetMate.isAlive()
                && this.hamster.isInCustomLove()
                && this.timer < 60;
        // let's say 3 seconds for them to breed
    }

    @Override
    public void start() {
        this.timer = 0;
    }

    @Override
    public void stop() {
        this.targetMate = null;
    }

    @Override
    public void tick() {
        this.hamster.getNavigation().startMovingTo(this.targetMate, this.speed);
        this.hamster.getLookControl().lookAt(this.targetMate, 10.0F, (float)this.hamster.getMaxLookPitchChange());
        this.timer++;

        if (this.timer >= 60) {
            this.breed();
        }
    }

    private HamsterEntity getNearbyMate() {
        // Find another HamsterEntity that is also in love
        return this.hamster.getWorld().getEntitiesByClass(
                HamsterEntity.class,
                this.hamster.getBoundingBox().expand(8.0D),
                h -> h != this.hamster && h.isInCustomLove() && h.getBreedingAge() == 0
        ).stream().findAny().orElse(null);
    }

    private void breed() {
        this.hamster.setBreedingAge(6000);  // 5 min cooldown
        this.targetMate.setBreedingAge(6000);

        this.hamster.customLoveTimer = 0;   // reset
        this.targetMate.customLoveTimer = 0;

        // Create baby
        HamsterEntity baby = (HamsterEntity)this.hamster.createChild((ServerWorld)this.hamster.getWorld(), this.targetMate);
        if (baby != null) {
            // Position baby
            baby.refreshPositionAndAngles(this.hamster.getX(), this.hamster.getY(), this.hamster.getZ(), 0.0F, 0.0F);
            this.hamster.getWorld().spawnEntity(baby);
        }
    }
}