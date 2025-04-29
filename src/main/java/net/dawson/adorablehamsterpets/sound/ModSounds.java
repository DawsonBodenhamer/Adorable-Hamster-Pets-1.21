package net.dawson.adorablehamsterpets.sound;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

/**
 * This class registers all custom hamster SoundEvents
 * and also contains public arrays of hamster sounds, plus a helper
 * method for retrieving a random sound from an array.
 */
public class ModSounds {

    /**
     * =========== SoundEvent Registrations ===========
     */

    public static final SoundEvent HAMSTER_THROW = registerSoundEvent("hamster_throw");
    public static final SoundEvent HAMSTER_IMPACT = registerSoundEvent("hamster_impact");

    public static final SoundEvent HAMSTER_ATTACK1 = registerSoundEvent("hamster_attack1");
    public static final SoundEvent HAMSTER_ATTACK2 = registerSoundEvent("hamster_attack2");
    public static final SoundEvent HAMSTER_ATTACK3 = registerSoundEvent("hamster_attack3");
    public static final SoundEvent HAMSTER_ATTACK4 = registerSoundEvent("hamster_attack4");
    public static final SoundEvent HAMSTER_ATTACK5 = registerSoundEvent("hamster_attack5");
    public static final SoundEvent HAMSTER_ATTACK6 = registerSoundEvent("hamster_attack6");

    public static final SoundEvent HAMSTER_BEG1 = registerSoundEvent("hamster_beg1");
    public static final SoundEvent HAMSTER_BEG2 = registerSoundEvent("hamster_beg2");
    public static final SoundEvent HAMSTER_BEG3 = registerSoundEvent("hamster_beg3");

    public static final SoundEvent HAMSTER_DEATH1 = registerSoundEvent("hamster_death1");
    public static final SoundEvent HAMSTER_DEATH2 = registerSoundEvent("hamster_death2");
    public static final SoundEvent HAMSTER_DEATH3 = registerSoundEvent("hamster_death3");

    public static final SoundEvent HAMSTER_HURT1 = registerSoundEvent("hamster_hurt1");
    public static final SoundEvent HAMSTER_HURT2 = registerSoundEvent("hamster_hurt2");
    public static final SoundEvent HAMSTER_HURT3 = registerSoundEvent("hamster_hurt3");
    public static final SoundEvent HAMSTER_HURT4 = registerSoundEvent("hamster_hurt4");

    public static final SoundEvent HAMSTER_IDLE1 = registerSoundEvent("hamster_idle1");
    public static final SoundEvent HAMSTER_IDLE2 = registerSoundEvent("hamster_idle2");
    public static final SoundEvent HAMSTER_IDLE3 = registerSoundEvent("hamster_idle3");
    public static final SoundEvent HAMSTER_IDLE4 = registerSoundEvent("hamster_idle4");

    public static final SoundEvent HAMSTER_SLEEP1 = registerSoundEvent("hamster_sleep1");
    public static final SoundEvent HAMSTER_SLEEP2 = registerSoundEvent("hamster_sleep2");
    public static final SoundEvent HAMSTER_SLEEP3 = registerSoundEvent("hamster_sleep3");

    public static final SoundEvent HAMSTER_SNIFF1 = registerSoundEvent("hamster_sniff1");
    public static final SoundEvent HAMSTER_SNIFF2 = registerSoundEvent("hamster_sniff2");

    public static final SoundEvent HAMSTER_CREEPER_DETECT1 = registerSoundEvent("hamster_creeper_detect1");
    public static final SoundEvent HAMSTER_CREEPER_DETECT2 = registerSoundEvent("hamster_creeper_detect2");
    public static final SoundEvent HAMSTER_CREEPER_DETECT3 = registerSoundEvent("hamster_creeper_detect3");

    public static final SoundEvent HAMSTER_CELEBRATE1 = registerSoundEvent("hamster_celebrate1");
    public static final SoundEvent HAMSTER_CELEBRATE2 = registerSoundEvent("hamster_celebrate2");
    public static final SoundEvent HAMSTER_CELEBRATE3 = registerSoundEvent("hamster_celebrate3");

    public static final SoundEvent HAMSTER_AIRBORNE_CELEBRATION = registerSoundEvent("hamster_airborne_celebration");
    public static final SoundEvent HAMSTER_WOW = registerSoundEvent("hamster_wow");

    public static final SoundEvent HAMSTER_WAKE_UP1 = registerSoundEvent("hamster_wake_up1");
    public static final SoundEvent HAMSTER_WAKE_UP2 = registerSoundEvent("hamster_wake_up2");
    public static final SoundEvent HAMSTER_WAKE_UP3 = registerSoundEvent("hamster_wake_up3");


    /**
     * =========== Public Sound Arrays ===========
     * These can be accessed by any class (e.g. HamsterEntity, CheeseItem)
     * to easily pick random hamster sounds.
     */

    public static final SoundEvent[] HAMSTER_ATTACK_SOUNDS = {
            HAMSTER_ATTACK1,
            HAMSTER_ATTACK2,
            HAMSTER_ATTACK3,
            HAMSTER_ATTACK4,
            HAMSTER_ATTACK5,
            HAMSTER_ATTACK6
    };

    public static final SoundEvent[] HAMSTER_IDLE_SOUNDS = {
            HAMSTER_IDLE1,
            HAMSTER_IDLE2,
            HAMSTER_IDLE3,
            HAMSTER_IDLE4
    };

    public static final SoundEvent[] HAMSTER_SLEEP_SOUNDS = {
            HAMSTER_SLEEP1,
            HAMSTER_SLEEP2,
            HAMSTER_SLEEP3
    };

    public static final SoundEvent[] HAMSTER_HURT_SOUNDS = {
            HAMSTER_HURT1,
            HAMSTER_HURT2,
            HAMSTER_HURT3,
            HAMSTER_HURT4
    };

    public static final SoundEvent[] HAMSTER_DEATH_SOUNDS = {
            HAMSTER_DEATH1,
            HAMSTER_DEATH2,
            HAMSTER_DEATH3
    };

    public static final SoundEvent[] HAMSTER_BEG_SOUNDS = {
            HAMSTER_BEG1,
            HAMSTER_BEG2,
            HAMSTER_BEG3
    };

    public static final SoundEvent[] HAMSTER_CREEPER_DETECT_SOUNDS = {
            HAMSTER_CREEPER_DETECT1,
            HAMSTER_CREEPER_DETECT2,
            HAMSTER_CREEPER_DETECT3
    };

    public static final SoundEvent[] HAMSTER_DIAMOND_SNIFF_SOUNDS = {
            HAMSTER_SNIFF1,
            HAMSTER_SNIFF2
    };

    public static final SoundEvent[] HAMSTER_CELEBRATE_SOUNDS = {
            HAMSTER_CELEBRATE1,
            HAMSTER_CELEBRATE2,
            HAMSTER_CELEBRATE3
    };

    public static final SoundEvent[] HAMSTER_FLYING_SOUNDS = {
            HAMSTER_WOW,
            HAMSTER_AIRBORNE_CELEBRATION
    };

    public static final SoundEvent[] HAMSTER_WAKE_UP_SOUNDS = {
            HAMSTER_WAKE_UP1,
            HAMSTER_WAKE_UP2,
            HAMSTER_WAKE_UP3
    };


    /**
     * =========== Helper Method ===========
     * Picks a random SoundEvent from the provided array using your Random instance.
     *
     * Example usage:
     *   SoundEvent selectedSound = ModSounds.getRandomSoundFrom(ModSounds.HAMSTER_CELEBRATE_SOUNDS, hamster.getRandom());
     */
    public static SoundEvent getRandomSoundFrom(SoundEvent[] sounds, Random random) {
        if (sounds == null || sounds.length == 0) {
            // fallback to a known sound or null if you prefer
            AdorableHamsterPets.LOGGER.warn("Attempted to get random sound from empty or null array!"); // Added warning
            return null;
        }
        int index = random.nextInt(sounds.length);
        return sounds[index];
    }

    /**
     * Standard registry method for a single SoundEvent.
     */
    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(AdorableHamsterPets.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    /**
     * Called during mod initialization to register all sounds.
     */
    public static void registerSounds() {
        AdorableHamsterPets.LOGGER.info("Registering Mod Sounds for " + AdorableHamsterPets.MOD_ID);
        // No additional code needed; static fields handle the rest.
    }
}