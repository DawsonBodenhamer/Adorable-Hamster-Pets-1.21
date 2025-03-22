package net.dawson.adorablehamsterpets.entity.AI;

import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class HamsterShoulderRideGoal extends Goal {

    private final HamsterEntity hamster;
    private PlayerEntity owner;
    private int dismountDelay = 0;

    public HamsterShoulderRideGoal(HamsterEntity hamster) {
        this.hamster = hamster;
        // We don't need standard movement controls for a mounted hamster
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        // We can only mount if:
        // 1) Hamster is tamed
        // 2) The hamster has no vehicle
        // 3) "shoulderMountRequested" is set
        if (!hamster.isTamed()) return false;
        if (hamster.hasVehicle()) return false;
        if (!hamster.shoulderMountRequested) return false;

        // Identify the owner
        PlayerEntity potentialOwner = (PlayerEntity) hamster.getOwner();
        if (potentialOwner == null || !potentialOwner.isAlive() || potentialOwner.isSpectator()) {
            return false;
        }

        this.owner = potentialOwner;
        return true;
    }

    @Override
    public void start() {
        hamster.shoulderMountRequested = false;
        // Immediately start riding
        hamster.startRiding(this.owner, true);
        this.dismountDelay = 20; // short delay before we check dismount logic
    }

    @Override
    public boolean shouldContinue() {
        // Only continue if hamster is still riding the same (alive) player
        if (!hamster.hasVehicle() || !(hamster.getVehicle() instanceof PlayerEntity current)) {
            return false;
        }
        if (current != this.owner) {
            return false;
        }
        return current.isAlive() && !current.isSpectator();
    }

    @Override
    public void tick() {
        if (dismountDelay > 0) {
            dismountDelay--;
        } else {
            // If the owner sneaks or sleeps => forcibly dismount
            if (owner.isSneaking() || owner.isSleeping()) {
                hamster.stopRiding();
                // Optionally place the hamster on the ground by the player’s feet
                hamster.setPosition(owner.getX(), owner.getY(), owner.getZ());
                return;
            }
        }

        // If still riding, reposition the hamster each tick to appear on the shoulder
        if (hamster.hasVehicle() && hamster.getVehicle() == owner) {
            double xOffset = 0.35D * (owner.isSneaking() ? 0.5D : 1.0D); // reduce offset if sneaking
            double yOffset = owner.getEyeY() - 0.2D - hamster.getHeight();
            double zOffset = 0.0D; // you can shift forward/back if desired

            // We'll do a rough alignment to the player's Yaw
            float angle = owner.bodyYaw * 0.017453292F; // convert to radians
            double dx = -xOffset * Math.sin(angle);
            double dz = xOffset * Math.cos(angle);

            hamster.setPosition(
                    owner.getX() + dx,
                    yOffset,
                    owner.getZ() + dz
            );

            // Optionally match yaw to player’s yaw
            hamster.setYaw(owner.getYaw());
            hamster.setBodyYaw(owner.getYaw());
        }
    }

    @Override
    public void stop() {
        owner = null;
    }
}
