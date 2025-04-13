package net.dawson.adorablehamsterpets.entity.AI;

import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.dawson.adorablehamsterpets.sound.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;

public class HamsterMeleeAttackGoal extends MeleeAttackGoal {

    private final HamsterEntity hamster;

    public HamsterMeleeAttackGoal(HamsterEntity mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
        this.hamster = mob;
    }

    @Override
    protected void attack(LivingEntity target) {
        if (this.canAttack(target)) {
            this.resetCooldown();

            // --- Play Custom Attack Sound ---
            SoundEvent attackSound = ModSounds.getRandomSoundFrom(ModSounds.HAMSTER_ATTACK_SOUNDS, this.hamster.getRandom());
            if (attackSound != null) {
                this.hamster.getWorld().playSound(null, this.hamster.getBlockPos(), attackSound, SoundCategory.NEUTRAL, 3.0F, this.hamster.getSoundPitch());
            }
            // --- End Custom Attack Sound ---

            this.mob.swingHand(Hand.MAIN_HAND);

            this.mob.tryAttack(target);
        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}