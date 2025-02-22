package net.dawson.adorablehamsterpets.sound;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    /** Hamster Sounds */

    public static final SoundEvent HAMSTER_BEG1 = registerSoundEvent("hamster_beg1");
    public static final SoundEvent HAMSTER_BEG2 = registerSoundEvent("hamster_beg2");
    public static final SoundEvent HAMSTER_BEG3 = registerSoundEvent("hamster_beg3");
    public static final SoundEvent HAMSTER_BEG4 = registerSoundEvent("hamster_beg4");

    public static final SoundEvent HAMSTER_DEATH1 = registerSoundEvent("hamster_death1");
    public static final SoundEvent HAMSTER_DEATH2 = registerSoundEvent("hamster_death2");
    public static final SoundEvent HAMSTER_DEATH3 = registerSoundEvent("hamster_death3");
    public static final SoundEvent HAMSTER_DEATH4 = registerSoundEvent("hamster_death4");
    public static final SoundEvent HAMSTER_DEATH5 = registerSoundEvent("hamster_death5");

    public static final SoundEvent HAMSTER_HURT1 = registerSoundEvent("hamster_hurt1");
    public static final SoundEvent HAMSTER_HURT2 = registerSoundEvent("hamster_hurt2");
    public static final SoundEvent HAMSTER_HURT3 = registerSoundEvent("hamster_hurt3");
    public static final SoundEvent HAMSTER_HURT4 = registerSoundEvent("hamster_hurt4");
    public static final SoundEvent HAMSTER_HURT5 = registerSoundEvent("hamster_hurt5");

    public static final SoundEvent HAMSTER_IDLE1 = registerSoundEvent("hamster_idle1");
    public static final SoundEvent HAMSTER_IDLE2 = registerSoundEvent("hamster_idle2");
    public static final SoundEvent HAMSTER_IDLE3 = registerSoundEvent("hamster_idle3");
    public static final SoundEvent HAMSTER_IDLE4 = registerSoundEvent("hamster_idle4");
    public static final SoundEvent HAMSTER_IDLE5 = registerSoundEvent("hamster_idle5");

    public static final SoundEvent HAMSTER_SLEEP1 = registerSoundEvent("hamster_sleep1");
    public static final SoundEvent HAMSTER_SLEEP2 = registerSoundEvent("hamster_sleep2");
    public static final SoundEvent HAMSTER_SLEEP3 = registerSoundEvent("hamster_sleep3");
    public static final SoundEvent HAMSTER_SLEEP4 = registerSoundEvent("hamster_sleep4");

    public static final SoundEvent HAMSTER_SNIFF1 = registerSoundEvent("hamster_sniff1");
    public static final SoundEvent HAMSTER_SNIFF2 = registerSoundEvent("hamster_sniff2");

    public static final SoundEvent HAMSTER_CREEPER_DETECT1 = registerSoundEvent("hamster_creeper_detect1");
    public static final SoundEvent HAMSTER_CREEPER_DETECT2 = registerSoundEvent("hamster_creeper_detect2");
    public static final SoundEvent HAMSTER_CREEPER_DETECT3 = registerSoundEvent("hamster_creeper_detect3");

    public static final SoundEvent HAMSTER_CELEBRATE1 = registerSoundEvent("hamster_celebrate1");
    public static final SoundEvent HAMSTER_CELEBRATE2 = registerSoundEvent("hamster_celebrate2");
    public static final SoundEvent HAMSTER_CELEBRATE3 = registerSoundEvent("hamster_celebrate3");



    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(AdorableHamsterPets.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }


    public static void registerSounds() {
        AdorableHamsterPets.LOGGER.info("Registering Mod Sounds for " + AdorableHamsterPets.MOD_ID);
    }
}
