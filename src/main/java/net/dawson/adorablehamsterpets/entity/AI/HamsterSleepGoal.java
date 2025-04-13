package net.dawson.adorablehamsterpets.entity.AI;

import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.dawson.adorablehamsterpets.sound.ModSounds;

import java.util.EnumSet;

public class HamsterSleepGoal extends Goal {
    private final HamsterEntity hamster;
    private static final int CHECK_INTERVAL = 20; // Check for threats every second
    private int checkTimer = 0;

    public HamsterSleepGoal(HamsterEntity hamster) {
        this.hamster = hamster;
        // Control movement and look to prevent interference
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
    }

    @Override
    public boolean canStart() {
        // Only wild hamsters sleep via this goal
        // Use the specific trackers here, not the overridden isSitting()
        if (hamster.isTamed() || hamster.isSleeping() || hamster.getDataTracker().get(HamsterEntity.IS_SITTING) || hamster.isKnockedOut()) {
            return false;
        }
        // Only sleep during the day
        if (!hamster.getWorld().isDay()) {
            return false;
        }


        // Check if on ground
        if (!hamster.isOnGround()) {
            return false;
        }

        // Check for nearby threats less frequently
        if (checkTimer > 0) {
            checkTimer--;
            return false;
        }
        checkTimer = CHECK_INTERVAL;

        double radius = 5.0;
        boolean threatNearby = !hamster.getWorld().getOtherEntities(
                hamster,
                hamster.getBoundingBox().expand(radius),
                this::isThreat
        ).isEmpty();

        return !threatNearby;
    }

    // isThreat method remains the same

    @Override
    public void start() {
        hamster.getNavigation().stop();
        hamster.setTarget(null);

        // --- Explicitly set states ---
        hamster.setSleeping(true); // Set the specific sleep state
        hamster.setInSittingPose(true); // Use vanilla pose flag for AI compatibility (stops movement goals)

        // Play sleep sound
        if (!hamster.getWorld().isClient()) {
            SoundEvent sleepSound = ModSounds.getRandomSoundFrom(ModSounds.HAMSTER_SLEEP_SOUNDS, hamster.getRandom());
            if (sleepSound != null) {
                hamster.getWorld().playSound(null, hamster.getBlockPos(), sleepSound, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public boolean shouldContinue() {
        // Stop if tamed, night time, or threat appears
        if (hamster.isTamed() || !hamster.getWorld().isDay()) {
            return false;
        }

        // Check for threats less frequently
        if (checkTimer > 0) {
            checkTimer--;
            return true; // Continue if timer active
        }
        checkTimer = CHECK_INTERVAL;

        double radius = 5.0;
        boolean threatNearby = !hamster.getWorld().getOtherEntities(
                hamster,
                hamster.getBoundingBox().expand(radius),
                this::isThreat
        ).isEmpty();

        return !threatNearby; // Continue only if no threat found
    }

    @Override
    public void stop() {
        // --- Explicitly clear states ---
        hamster.setSleeping(false); // Clear sleep state
        hamster.setInSittingPose(false); // Clear vanilla pose flag
        checkTimer = 0; // Reset check timer
    }

    // Add the isPlayerSafe helper method reference if needed, or copy it
    private static boolean isPlayerSafe(PlayerEntity player) {
        if (!player.isSneaking()) {
            return false;
        }
        ItemStack main = player.getMainHandStack();
        ItemStack off = player.getOffHandStack();
        boolean mainIsCucumber = main.isOf(ModItems.SLICED_CUCUMBER);
        boolean offIsCucumber  = off.isOf(ModItems.SLICED_CUCUMBER);
        return (mainIsCucumber || offIsCucumber);
    }

    private boolean isThreat(Entity entity) {
        if (entity instanceof HostileEntity) {
            return true;
        }
        // Wild hamsters flee players unless sneaking with cucumber, consider them threats otherwise
        return (entity instanceof PlayerEntity player && !isPlayerSafe(player));
    }
}