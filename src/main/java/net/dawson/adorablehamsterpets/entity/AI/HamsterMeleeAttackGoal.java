package net.dawson.adorablehamsterpets.entity.AI;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.dawson.adorablehamsterpets.sound.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.world.World; // Import World for getTime

public class HamsterMeleeAttackGoal extends MeleeAttackGoal {

    private final HamsterEntity hamster;
    private final int instanceId;
    private static final int HAMSTER_ATTACK_ANIM_TICKS = 21;

    public HamsterMeleeAttackGoal(HamsterEntity mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
        this.hamster = mob;
        this.instanceId = mob.getId();
        // AdorableHamsterPets.LOGGER.info("[AttackGoal {}] Initialized.", instanceId);
    }

    @Override
    protected void resetCooldown() {
        // Use the field directly since we widened it
        this.cooldown = this.getTickCount(HAMSTER_ATTACK_ANIM_TICKS);
        long worldTime = this.hamster.getWorld().getTime();
        AdorableHamsterPets.LOGGER.info("[AttackGoal {} Tick {}] Cooldown reset to {} ticks.", instanceId, worldTime, this.cooldown);
    }

    @Override
    protected int getMaxCooldown() {
        return this.getTickCount(HAMSTER_ATTACK_ANIM_TICKS);
    }

    @Override
    protected void attack(LivingEntity target) {
        long worldTime = this.hamster.getWorld().getTime();
        // AdorableHamsterPets.LOGGER.info("[AttackGoal {} Tick {}] attack() called. Target: {}", instanceId, worldTime, target.getName().getString());

        if (this.canAttack(target)) {
            AdorableHamsterPets.LOGGER.info("[AttackGoal {} Tick {}] +++ Attack Possible +++", instanceId, worldTime);
            this.resetCooldown(); // Logs cooldown reset time

            // Play Custom Attack Sound
            SoundEvent attackSound = ModSounds.getRandomSoundFrom(ModSounds.HAMSTER_ATTACK_SOUNDS, this.hamster.getRandom());
            if (attackSound != null) {
                AdorableHamsterPets.LOGGER.info("[AttackGoal {} Tick {}] Playing sound: {}", instanceId, worldTime, attackSound.getId().getPath());
                this.hamster.getWorld().playSound(null, this.hamster.getBlockPos(), attackSound, SoundCategory.NEUTRAL, 3.0F, this.hamster.getSoundPitch());
            }

            // Set Swing Duration Flag for getHandSwingDuration override
            AdorableHamsterPets.LOGGER.info("[AttackGoal {} Tick {}] Setting isAttackSwinging = true", instanceId, worldTime);
            this.hamster.isAttackSwinging = true;

            // Swing Hand (triggers handSwinging and calls getHandSwingDuration)
            AdorableHamsterPets.LOGGER.info("[AttackGoal {} Tick {}] Calling mob.swingHand()", instanceId, worldTime);
            this.mob.swingHand(Hand.MAIN_HAND);

            // Attempt the actual attack (Damage happens here)
            AdorableHamsterPets.LOGGER.info("[AttackGoal {} Tick {}] Calling mob.tryAttack()", instanceId, worldTime);
            this.mob.tryAttack(target);
            AdorableHamsterPets.LOGGER.info("[AttackGoal {} Tick {}] --- Attack Action Complete ---", instanceId, worldTime);

        } else {
            // Log why attack failed if needed (can be spammy)
            // AdorableHamsterPets.LOGGER.info("[AttackGoal {} Tick {}] Cannot attack. Cooldown: {}, InRange: {}, CanSee: {}",
            //     instanceId, worldTime, this.getCooldown(), this.mob.isInAttackRange(target), this.mob.getVisibilityCache().canSee(target));
        }
    }

    @Override
    public void stop() {
        long worldTime = this.hamster.getWorld().isClient ? 0 : this.hamster.getWorld().getTime(); // Avoid client world time call if possible
        AdorableHamsterPets.LOGGER.info("[AttackGoal {} Tick {}] stop() called. Resetting handSwingTicks.", instanceId, worldTime);
        this.hamster.handSwingTicks = 0;
        this.hamster.handSwinging = false;
        super.stop();
    }

    // Keep other methods like canStart, tick as default
}