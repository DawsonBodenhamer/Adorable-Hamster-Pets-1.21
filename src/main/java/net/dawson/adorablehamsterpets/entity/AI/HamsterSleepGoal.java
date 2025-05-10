package net.dawson.adorablehamsterpets.entity.AI;

import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.dawson.adorablehamsterpets.sound.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

import java.util.EnumSet;

public class HamsterSleepGoal extends Goal {

    // --- 1. Constants and Static Utilities ---
    private static final int CHECK_INTERVAL = 20; // Check for threats every second

    // --- 2. Fields ---
    private final HamsterEntity hamster;
    private int checkTimer = 0;

    // --- 3. Constructors ---
    public HamsterSleepGoal(HamsterEntity hamster) {
        this.hamster = hamster;
        // Control movement and look to prevent interference
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
    }

    // --- 4. Public Methods (Overrides from Goal) ---
    @Override
    public boolean canStart() {
        // --- 1. Pre-conditions for Sleep ---
        // Only wild hamsters sleep via this goal.
        // Do not start if already sleeping, player-commanded sitting, or knocked out.
        if (this.hamster.isTamed() ||
                this.hamster.isSleeping() ||
                this.hamster.getDataTracker().get(HamsterEntity.IS_SITTING) || // Check raw sitting state
                this.hamster.isKnockedOut()) {
            return false;
        }

        // Only sleep during the day.
        if (!this.hamster.getWorld().isDay()) {
            return false;
        }

        // Must be on the ground.
        if (!this.hamster.isOnGround()) {
            return false;
        }
        // --- End 1. Pre-conditions ---

        // --- 2. Threat Check Timer ---
        // Check for nearby threats less frequently to avoid constant scanning.
        if (this.checkTimer > 0) {
            this.checkTimer--;
            return false; // Don't re-evaluate threats until timer expires.
        }
        this.checkTimer = CHECK_INTERVAL; // Reset timer for next check.
        // --- End 2. Threat Check Timer ---

        // --- 3. Threat Detection ---
        // Define the radius for threat detection.
        double radius = 5.0;
        // Check if any threatening entities are within the defined radius.
        boolean threatNearby = !this.hamster.getWorld().getOtherEntities(
                this.hamster,
                this.hamster.getBoundingBox().expand(radius),
                this::isThreat // Predicate to determine if an entity is a threat
        ).isEmpty();
        // --- End 3. Threat Detection ---

        // Can start sleeping only if no threats are nearby.
        return !threatNearby;
    }

    @Override
    public void start() {
        // --- 1. Stop Movement and Targeting ---
        this.hamster.getNavigation().stop();
        this.hamster.setTarget(null);
        // --- End 1. Stop Movement ---

        // --- 2. Set Sleep State ---
        // Set the custom sleep state and vanilla sitting pose (to stop other AI).
        this.hamster.setSleeping(true);
        this.hamster.setInSittingPose(true); // Vanilla flag used by SitGoal to prevent movement.
        // --- End 2. Set Sleep State ---

        // --- 3. Play Sound ---
        // Play a sleep sound on the server.
        if (!this.hamster.getWorld().isClient()) {
            SoundEvent sleepSound = ModSounds.getRandomSoundFrom(ModSounds.HAMSTER_SLEEP_SOUNDS, this.hamster.getRandom());
            if (sleepSound != null) {
                this.hamster.getWorld().playSound(
                        null,
                        this.hamster.getBlockPos(),
                        sleepSound,
                        SoundCategory.NEUTRAL,
                        1.0F,
                        1.0F
                );
            }
        }
        // --- End 3. Play Sound ---
    }

    @Override
    public boolean shouldContinue() {
        // --- 1. Basic Conditions to Stop ---
        // Stop if the hamster is tamed or if it becomes night.
        if (this.hamster.isTamed() || !this.hamster.getWorld().isDay()) {
            return false;
        }
        // --- End 1. Basic Conditions ---

        // --- 2. Threat Check Timer ---
        // Continue checking for threats periodically.
        if (this.checkTimer > 0) {
            this.checkTimer--;
            return true; // Continue sleeping if timer is active and no other condition stops it.
        }
        this.checkTimer = CHECK_INTERVAL; // Reset timer.
        // --- End 2. Threat Check Timer ---

        // --- 3. Threat Detection ---
        double radius = 5.0; // Same radius as in canStart().
        boolean threatNearby = !this.hamster.getWorld().getOtherEntities(
                this.hamster,
                this.hamster.getBoundingBox().expand(radius),
                this::isThreat
        ).isEmpty();
        // --- End 3. Threat Detection ---

        // Continue sleeping only if no new threats are found.
        return !threatNearby;
    }

    @Override
    public void stop() {
        // --- 1. Clear Sleep State ---
        this.hamster.setSleeping(false);
        this.hamster.setInSittingPose(false); // Clear vanilla pose flag.
        // --- End 1. Clear Sleep State ---

        // --- 2. Reset Timer ---
        this.checkTimer = 0; // Reset check timer immediately.
        // --- End 2. Reset Timer ---
    }

    // --- 5. Private Helper Methods ---

    /**
     * Determines if the given entity is considered a threat to a sleeping wild hamster,
     * which would cause it to wake up.
     *
     * @param entity The entity to check.
     * @return True if the entity is a threat, false otherwise.
     */
    private boolean isThreat(Entity entity) {
        // --- 1. Hostile Check ---
        // Hostile entities are always considered threats.
        if (entity instanceof HostileEntity) {
            return true;
        }
        // --- End 1. Hostile Check ---

        // --- 2. Player Check ---
        // For a SLEEPING WILD hamster, ANY nearby player is a threat that should cause it to wake.
        // The isPlayerSafe check (sneaking + cucumber) is for the fleeing behavior of an AWAKE hamster.
        if (entity instanceof PlayerEntity) {
            return true;
        }
        // --- End 2. Player Check ---

        // --- 3. Default ---
        // Other entities are not considered threats for waking up.
        return false;
        // --- End 3. Default ---
    }
}