package net.dawson.adorablehamsterpets.sound;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final SoundEvent CHISEL_USE = registerSoundEvent("chisel_use");
    public static final SoundEvent MAGIC_BLOCK_BREAK = registerSoundEvent("magic_block_break");
    public static final SoundEvent MAGIC_BLOCK_STEP = registerSoundEvent("magic_block_step");
    public static final SoundEvent MAGIC_BLOCK_PLACE = registerSoundEvent("magic_block_place");
    public static final SoundEvent MAGIC_BLOCK_HIT = registerSoundEvent("magic_block_hit");
    public static final SoundEvent MAGIC_BLOCK_FALL = registerSoundEvent("magic_block_fall");

    public static final BlockSoundGroup MAGIC_BLOCK_SOUNDS = new BlockSoundGroup(1f, 1f,
            MAGIC_BLOCK_BREAK, MAGIC_BLOCK_STEP, MAGIC_BLOCK_PLACE, MAGIC_BLOCK_HIT, MAGIC_BLOCK_FALL);

    public static final SoundEvent BAR_BRAWL = registerSoundEvent("bar_brawl");
    public static final RegistryKey<JukeboxSong> BAR_BRAWL_KEY =
            RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(AdorableHamsterPets.MOD_ID, "bar_brawl"));



    /** Hamster Sounds */

    public static final SoundEvent HAMSTER_BEG1  = registerSoundEvent("hamster_beg1");
    public static final SoundEvent HAMSTER_BEG2  = registerSoundEvent("hamster_beg2");
    public static final SoundEvent HAMSTER_BEG3  = registerSoundEvent("hamster_beg3");
    public static final SoundEvent HAMSTER_BEG4  = registerSoundEvent("hamster_beg4");

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

    public static final SoundEvent HAMSTER_YAY = registerSoundEvent("hamster_yay");


    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(AdorableHamsterPets.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }


    public static void registerSounds() {
        AdorableHamsterPets.LOGGER.info("Registering Mod Sounds for " + AdorableHamsterPets.MOD_ID);
    }
}
