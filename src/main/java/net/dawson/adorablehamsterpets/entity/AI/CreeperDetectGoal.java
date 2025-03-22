package net.dawson.adorablehamsterpets.entity.AI;

import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Box;

import java.util.EnumSet;
import java.util.List;

public class CreeperDetectGoal extends Goal {
    private final HamsterEntity hamster;
    private int checkTimer = 0;
    private static final int CHECK_INTERVAL = 20; // once per second

    public CreeperDetectGoal(HamsterEntity hamster) {
        this.hamster = hamster;
        this.setControls(EnumSet.noneOf(Control.class));
    }

    @Override
    public boolean canStart() {
        // only if hamster is riding the player
        if (!hamster.hasVehicle() || !(hamster.getVehicle() instanceof PlayerEntity)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean shouldContinue() {
        // same condition => continue
        return canStart();
    }

    @Override
    public void tick() {
        if (checkTimer > 0) {
            checkTimer--;
            return;
        }
        checkTimer = CHECK_INTERVAL;

        PlayerEntity rider = (PlayerEntity) hamster.getVehicle();
        if (rider == null) return;

        // find creepers in range that are targeting this player
        List<CreeperEntity> creepers = hamster.getWorld().getEntitiesByClass(
                CreeperEntity.class,
                new Box(rider.getBlockPos()).expand(16.0),
                c -> c.getTarget() == rider
        );
        if (!creepers.isEmpty()) {
            hamster.playCreeperDetectSound();
        }
    }
}
