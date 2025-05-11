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

    // --- 1. SoundEvent Registrations ---

    // Impact/Throw
    public static final SoundEvent HAMSTER_IMPACT = registerSoundEvent("cartoon_punches");
    public static final SoundEvent HAMSTER_THROW = registerSoundEvent("hamster_throw");

    // Flying / Special
    public static final SoundEvent HAMSTER_AIRBORNE_CELEBRATION = registerSoundEvent("hamster_airborne_celebration");
    public static final SoundEvent HAMSTER_WOW = registerSoundEvent("hamster_wow");

    // Attack Sounds (1-6)
    public static final SoundEvent HAMSTER_ATTACK1 = registerSoundEvent("hamster_attack1");
    public static final SoundEvent HAMSTER_ATTACK2 = registerSoundEvent("hamster_attack2");
    public static final SoundEvent HAMSTER_ATTACK3 = registerSoundEvent("hamster_attack3");
    public static final SoundEvent HAMSTER_ATTACK4 = registerSoundEvent("hamster_attack4");

    // Beg Sounds (1-5)
    public static final SoundEvent HAMSTER_BEG1 = registerSoundEvent("hamster_beg1");
    public static final SoundEvent HAMSTER_BEG2 = registerSoundEvent("hamster_beg2");
    public static final SoundEvent HAMSTER_BEG3 = registerSoundEvent("hamster_beg3");
    public static final SoundEvent HAMSTER_BEG4 = registerSoundEvent("hamster_beg4");
    public static final SoundEvent HAMSTER_BEG5 = registerSoundEvent("hamster_beg5");

    // Celebrate Sounds (1-4)
    public static final SoundEvent HAMSTER_CELEBRATE1 = registerSoundEvent("hamster_celebrate1");
    public static final SoundEvent HAMSTER_CELEBRATE2 = registerSoundEvent("hamster_celebrate2");
    public static final SoundEvent HAMSTER_CELEBRATE3 = registerSoundEvent("hamster_celebrate3");
    public static final SoundEvent HAMSTER_CELEBRATE4 = registerSoundEvent("hamster_celebrate4");

    // Creeper Detect Sounds (1-4)
    public static final SoundEvent HAMSTER_CREEPER_DETECT1 = registerSoundEvent("hamster_creeper_detect1");
    public static final SoundEvent HAMSTER_CREEPER_DETECT2 = registerSoundEvent("hamster_creeper_detect2");
    public static final SoundEvent HAMSTER_CREEPER_DETECT3 = registerSoundEvent("hamster_creeper_detect3");
    public static final SoundEvent HAMSTER_CREEPER_DETECT4 = registerSoundEvent("hamster_creeper_detect4");

    // Sniff Sounds (1-4) - Used for Diamond Detection
    public static final SoundEvent HAMSTER_SNIFF1 = registerSoundEvent("hamster_sniff1");
    public static final SoundEvent HAMSTER_SNIFF2 = registerSoundEvent("hamster_sniff2");
    public static final SoundEvent HAMSTER_SNIFF3 = registerSoundEvent("hamster_sniff3");
    public static final SoundEvent HAMSTER_SNIFF4 = registerSoundEvent("hamster_sniff4");

    // Death Sounds (1-4)
    public static final SoundEvent HAMSTER_DEATH1 = registerSoundEvent("hamster_death1");
    public static final SoundEvent HAMSTER_DEATH2 = registerSoundEvent("hamster_death2");
    public static final SoundEvent HAMSTER_DEATH3 = registerSoundEvent("hamster_death3");
    public static final SoundEvent HAMSTER_DEATH4 = registerSoundEvent("hamster_death4");

    // Hurt Sounds (1-10)
    public static final SoundEvent HAMSTER_HURT1 = registerSoundEvent("hamster_hurt1");
    public static final SoundEvent HAMSTER_HURT2 = registerSoundEvent("hamster_hurt2");
    public static final SoundEvent HAMSTER_HURT3 = registerSoundEvent("hamster_hurt3");
    public static final SoundEvent HAMSTER_HURT4 = registerSoundEvent("hamster_hurt4");
    public static final SoundEvent HAMSTER_HURT5 = registerSoundEvent("hamster_hurt5");
    public static final SoundEvent HAMSTER_HURT6 = registerSoundEvent("hamster_hurt6");
    public static final SoundEvent HAMSTER_HURT7 = registerSoundEvent("hamster_hurt7");
    public static final SoundEvent HAMSTER_HURT8 = registerSoundEvent("hamster_hurt8");
    public static final SoundEvent HAMSTER_HURT9 = registerSoundEvent("hamster_hurt9");
    public static final SoundEvent HAMSTER_HURT10 = registerSoundEvent("hamster_hurt10");

    // Idle Sounds (1-11)
    public static final SoundEvent HAMSTER_IDLE1 = registerSoundEvent("hamster_idle1");
    public static final SoundEvent HAMSTER_IDLE2 = registerSoundEvent("hamster_idle2");
    public static final SoundEvent HAMSTER_IDLE3 = registerSoundEvent("hamster_idle3");
    public static final SoundEvent HAMSTER_IDLE4 = registerSoundEvent("hamster_idle4");
    public static final SoundEvent HAMSTER_IDLE5 = registerSoundEvent("hamster_idle5");
    public static final SoundEvent HAMSTER_IDLE6 = registerSoundEvent("hamster_idle6");
    public static final SoundEvent HAMSTER_IDLE7 = registerSoundEvent("hamster_idle7");
    public static final SoundEvent HAMSTER_IDLE8 = registerSoundEvent("hamster_idle8");
    public static final SoundEvent HAMSTER_IDLE9 = registerSoundEvent("hamster_idle9");
    public static final SoundEvent HAMSTER_IDLE10 = registerSoundEvent("hamster_idle10");
    public static final SoundEvent HAMSTER_IDLE11 = registerSoundEvent("hamster_idle11");

    // Sleep Sounds (1-9)
    public static final SoundEvent HAMSTER_SLEEP1 = registerSoundEvent("hamster_sleep1");
    public static final SoundEvent HAMSTER_SLEEP2 = registerSoundEvent("hamster_sleep2");
    public static final SoundEvent HAMSTER_SLEEP3 = registerSoundEvent("hamster_sleep3");
    public static final SoundEvent HAMSTER_SLEEP4 = registerSoundEvent("hamster_sleep4");
    public static final SoundEvent HAMSTER_SLEEP5 = registerSoundEvent("hamster_sleep5");
    public static final SoundEvent HAMSTER_SLEEP6 = registerSoundEvent("hamster_sleep6");
    public static final SoundEvent HAMSTER_SLEEP7 = registerSoundEvent("hamster_sleep7");
    public static final SoundEvent HAMSTER_SLEEP8 = registerSoundEvent("hamster_sleep8");
    public static final SoundEvent HAMSTER_SLEEP9 = registerSoundEvent("hamster_sleep9");

    // Wake Up Sounds (1-3)
    public static final SoundEvent HAMSTER_WAKE_UP1 = registerSoundEvent("hamster_wake_up1");
    public static final SoundEvent HAMSTER_WAKE_UP2 = registerSoundEvent("hamster_wake_up2");
    public static final SoundEvent HAMSTER_WAKE_UP3 = registerSoundEvent("hamster_wake_up3");
    // --- End 1. SoundEvent Registrations ---


    // --- 2. Public Sound Arrays ---
    // These arrays group related sounds for easy random selection.

    public static final SoundEvent[] HAMSTER_ATTACK_SOUNDS = {
            HAMSTER_ATTACK1, HAMSTER_ATTACK2, HAMSTER_ATTACK3, HAMSTER_ATTACK4
    };

    public static final SoundEvent[] HAMSTER_IDLE_SOUNDS = {
            HAMSTER_IDLE1, HAMSTER_IDLE2, HAMSTER_IDLE3, HAMSTER_IDLE4, HAMSTER_IDLE5,
            HAMSTER_IDLE6, HAMSTER_IDLE7, HAMSTER_IDLE8, HAMSTER_IDLE9, HAMSTER_IDLE10,
            HAMSTER_IDLE11
    };

    public static final SoundEvent[] HAMSTER_SLEEP_SOUNDS = {
            HAMSTER_SLEEP1, HAMSTER_SLEEP2, HAMSTER_SLEEP3, HAMSTER_SLEEP4, HAMSTER_SLEEP5,
            HAMSTER_SLEEP6, HAMSTER_SLEEP7, HAMSTER_SLEEP8, HAMSTER_SLEEP9
    };

    public static final SoundEvent[] HAMSTER_HURT_SOUNDS = {
            HAMSTER_HURT1, HAMSTER_HURT2, HAMSTER_HURT3, HAMSTER_HURT4, HAMSTER_HURT5,
            HAMSTER_HURT6, HAMSTER_HURT7, HAMSTER_HURT8, HAMSTER_HURT9, HAMSTER_HURT10
    };

    public static final SoundEvent[] HAMSTER_DEATH_SOUNDS = {
            HAMSTER_DEATH1, HAMSTER_DEATH2, HAMSTER_DEATH3, HAMSTER_DEATH4
    };

    public static final SoundEvent[] HAMSTER_BEG_SOUNDS = {
            HAMSTER_BEG1, HAMSTER_BEG2, HAMSTER_BEG3, HAMSTER_BEG4, HAMSTER_BEG5
    };

    public static final SoundEvent[] HAMSTER_CREEPER_DETECT_SOUNDS = {
            HAMSTER_CREEPER_DETECT1, HAMSTER_CREEPER_DETECT2, HAMSTER_CREEPER_DETECT3,
            HAMSTER_CREEPER_DETECT4
    };

    public static final SoundEvent[] HAMSTER_DIAMOND_SNIFF_SOUNDS = { // Used for diamond detection
            HAMSTER_SNIFF1, HAMSTER_SNIFF2, HAMSTER_SNIFF3, HAMSTER_SNIFF4
    };

    public static final SoundEvent[] HAMSTER_CELEBRATE_SOUNDS = {
            HAMSTER_CELEBRATE1, HAMSTER_CELEBRATE2, HAMSTER_CELEBRATE3, HAMSTER_CELEBRATE4
    };

    public static final SoundEvent[] HAMSTER_FLYING_SOUNDS = { // Used when thrown
            HAMSTER_WOW, HAMSTER_AIRBORNE_CELEBRATION
    };

    public static final SoundEvent[] HAMSTER_WAKE_UP_SOUNDS = {
            HAMSTER_WAKE_UP1, HAMSTER_WAKE_UP2, HAMSTER_WAKE_UP3
    };
    // --- End 2. Public Sound Arrays ---


    // --- 3. Helper Method for Registration ---
    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(AdorableHamsterPets.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
    // --- End 3. Helper Method ---


    // --- 4. Main Registration Call ---
    /**
     * Called during mod initialization to register all sounds.
     * This method itself doesn't need to do much as the static fields handle registration.
     */
    public static void registerSounds() {
        AdorableHamsterPets.LOGGER.info("Registering Mod Sounds for " + AdorableHamsterPets.MOD_ID);
        // Static initializers for SoundEvent fields handle the actual registration.
    }
    // --- End 4. Main Registration Call ---


    // --- 5. Helper Method for Random Sound Selection ---
    /**
     * Picks a random SoundEvent from the provided array using the given Random instance.
     *
     * @param sounds The array of SoundEvents to choose from.
     * @param random The Random instance to use for selection.
     * @return A randomly selected SoundEvent, or null if the array is empty or null.
     *
     * Example usage:
     * SoundEvent selectedSound = ModSounds.getRandomSoundFrom(ModSounds.HAMSTER_IDLE_SOUNDS, entity.getRandom());
     */
    public static SoundEvent getRandomSoundFrom(SoundEvent[] sounds, Random random) {
        if (sounds == null || sounds.length == 0) {
            AdorableHamsterPets.LOGGER.warn("Attempted to get random sound from empty or null array!");
            return null; // Or a default fallback sound if preferred
        }
        return sounds[random.nextInt(sounds.length)];
    }
    // --- End 5. Helper Method ---
}