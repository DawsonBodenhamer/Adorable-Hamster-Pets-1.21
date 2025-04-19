package net.dawson.adorablehamsterpets.entity.AI;

import net.dawson.adorablehamsterpets.AdorableHamsterPets; // Import logger
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.dawson.adorablehamsterpets.sound.ModSounds; // Import ModSounds
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.sound.SoundCategory; // Import SoundCategory
import net.minecraft.sound.SoundEvent; // Import SoundEvent
import net.minecraft.util.Hand;

public class HamsterMeleeAttackGoal extends MeleeAttackGoal {

    private final HamsterEntity hamster;
    private final int instanceId; // Store ID for logging

    public HamsterMeleeAttackGoal(HamsterEntity mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
        this.hamster = mob;
        this.instanceId = mob.getId(); // Capture ID on creation
        // AdorableHamsterPets.LOGGER.info("[AttackGoal {}] Initialized.", instanceId); // Optional log
    }

    @Override
    public boolean canStart() {
        boolean canStart = super.canStart();
        // AdorableHamsterPets.LOGGER.info("[AttackGoal {}] canStart() called. Result: {}", instanceId, canStart);
        return canStart;
    }

    @Override
    public void start() {
        // AdorableHamsterPets.LOGGER.info("[AttackGoal {}] start() called.", instanceId); // Optional log
        super.start();
    }

    @Override
    public void tick() {
        // AdorableHamsterPets.LOGGER.info("[AttackGoal {}] tick() called. Cooldown: {}", instanceId, this.getCooldown()); // Very spammy
        super.tick();
    }

    @Override
    protected void attack(LivingEntity target) {
        // AdorableHamsterPets.LOGGER.info("[AttackGoal {}] attack() called. Target: {}", instanceId, target.getName().getString()); // Optional log
        if (this.canAttack(target)) {
            // AdorableHamsterPets.LOGGER.info("[AttackGoal {}] canAttack() returned true. Proceeding with attack.", instanceId); // Optional log
            this.resetCooldown();
            // AdorableHamsterPets.LOGGER.info("[AttackGoal {}] Cooldown reset.", instanceId); // Optional log

            // --- Play Custom Attack Sound ---
            SoundEvent attackSound = ModSounds.getRandomSoundFrom(ModSounds.HAMSTER_ATTACK_SOUNDS, this.hamster.getRandom());
            if (attackSound != null) {
                // AdorableHamsterPets.LOGGER.info("[AttackGoal {}] Playing sound: {}", instanceId, attackSound.getId().getPath()); // Optional log
                this.hamster.getWorld().playSound(null, this.hamster.getBlockPos(), attackSound, SoundCategory.NEUTRAL, 3.0F, this.hamster.getSoundPitch());
            } else {
                AdorableHamsterPets.LOGGER.warn("[AttackGoal {}] Failed to get random attack sound!", instanceId);
            }
            // --- End Custom Attack Sound ---

            // --- Swing Hand (Sets handSwinging flag for controller) ---
            // AdorableHamsterPets.LOGGER.info("[AttackGoal {}] Calling mob.swingHand()", instanceId); // Optional log
            this.mob.swingHand(Hand.MAIN_HAND);
            // --- End Swing Hand ---

            // --- NO setJustAttacked flag call ---

            // --- Attempt the actual attack ---
            // AdorableHamsterPets.LOGGER.info("[AttackGoal {}] Calling mob.tryAttack()", instanceId); // Optional log
            this.mob.tryAttack(target);
            // --- End Attack ---

        } // Removed else log
    }

    @Override
    public void stop() {
        // AdorableHamsterPets.LOGGER.info("[AttackGoal {}] stop() called.", instanceId); // Optional log
        super.stop(); // Let the parent class handle setting attacking state to false
    }
}