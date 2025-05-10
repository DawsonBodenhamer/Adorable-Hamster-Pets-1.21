package net.dawson.adorablehamsterpets.config;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.Nest;
import io.wispforest.owo.config.annotation.RangeConstraint;
import io.wispforest.owo.config.annotation.SectionHeader; // Corrected import
import blue.endless.jankson.Comment;
import net.dawson.adorablehamsterpets.AdorableHamsterPets;

@Modmenu(modId = AdorableHamsterPets.MOD_ID)
@Config(name = AdorableHamsterPets.MOD_ID, wrapperName = "ModConfig")
public class ModConfigModel {

    @SectionHeader("Spawning") // Config section for hamster spawning
    @Nest
    public Spawning spawning = new Spawning();

    @SectionHeader("Behavior") // Config section for hamster behavior
    @Nest
    public Behavior behavior = new Behavior();

    @SectionHeader("Cooldowns") // Config section for various cooldowns
    @Nest
    public Cooldowns cooldowns = new Cooldowns();

    @SectionHeader("Features") // Config section for enabling/disabling features
    @Nest
    public Features features = new Features();

    @SectionHeader("World Generation") // Config section for world generation tweaks
    @Nest
    public WorldGen worldGen = new WorldGen();

    // --- Spawning Settings ---
    public static class Spawning {
        @RangeConstraint(min = 0, max = 100)
        @Comment("Adjusts hamster spawn frequency. Higher = more chaos. 0 = blissful silence.")
        public int hamsterSpawnWeight = 30;

        @RangeConstraint(min = 1, max = 10)
        @Comment("Maximum hamsters per spawn group. Because sometimes one just isn't cute enough.")
        public int hamsterMaxGroupSize = 2;
    }

    // --- Behavior Settings ---
    public static class Behavior {
        @RangeConstraint(min = 0.0, max = 40.0)
        @Comment("Tamed hamster melee damage. Mostly for show, let's be honest.")
        public double hamsterMeleeDamage = 2.0;

        @RangeConstraint(min = 1, max = 20)
        @Comment("Taming difficulty (1 in X chance). Higher means more cucumbers sacrificed to the RNG gods.")
        public int tamingChanceDenominator = 3;

        @RangeConstraint(min = 0.0f, max = 10.0f)
        @Comment("Healing amount from Hamster Food Mix. The good stuff.")
        public float hamsterFoodMixHealing = 4.0f;

        @RangeConstraint(min = 0.0f, max = 5.0f)
        @Comment("Healing from basic seeds/crops. Better than nothing... probably.")
        public float standardFoodHealing = 2.0f;
    }

    // --- Cooldown Settings ---
    public static class Cooldowns {
        @RangeConstraint(min = 20, max = 20 * 60 * 10) // 1 sec to 10 mins
        @Comment("Cooldown (ticks) after using a hamster as a projectile. Give it a moment. (20 ticks = 1s)")
        public int hamsterThrowCooldown = 2400; // Default to 2 minutes (2 * 60 * 20)

        @RangeConstraint(min = 20, max = 20 * 60 * 10)
        @Comment("Cooldown (ticks) before Steamed Green Bean buffs can be reapplied. Patience.")
        public int steamedGreenBeansBuffCooldown = 6000;

        @RangeConstraint(min = 600, max = 24000) // 30 seconds to 20 minutes
        @Comment("Breeding cooldown (ticks) for hamsters. They need their space.")
        public int breedingCooldownTicks = 6000;
    }

    // --- Feature Settings ---
    public static class Features {
        @Comment("Enable the 'Sweet Potato' name tag easter egg? Sure, why not.")
        public boolean enableSweetPotatoEasterEgg = true;

        @Comment("Allow shoulder hamsters to detect Creepers? Might save your inventory.")
        public boolean enableShoulderCreeperDetection = true;

        @Comment("Allow shoulder hamsters to detect Diamonds? Or do you enjoy the surprise?")
        public boolean enableShoulderDiamondDetection = true;

        @RangeConstraint(min = 1.0, max = 32.0)
        @Comment("Shoulder Diamond detection radius (blocks). How close do you need to be?")
        public double shoulderDiamondDetectionRadius = 10.0;

        @RangeConstraint(min = 1.0, max = 64.0)
        @Comment("Shoulder Creeper detection radius (blocks). Adjust paranoia levels.")
        public double shoulderCreeperDetectionRadius = 16.0;

        @RangeConstraint(min = 0.0, max = 40.0)
        @Comment("Damage dealt by thrown hamster. Surprisingly, the default value is exactly enough to kill a creeper. What a coincidence.")
        public float hamsterThrowDamage = 20.0f;

        @Comment("Enable hamster throwing? ('G' key default). Use responsibly. Or don't.")
        public boolean enableHamsterThrowing = true;
    }

    // --- World Generation Settings ---
    public static class WorldGen {
        @RangeConstraint(min = 0.1, max = 5.0)
        @Comment("Sunflower seed regrowth speed. Higher values make it slower; lower values make it faster. Makes perfect sense.")
        public double sunflowerRegrowthModifier = 1.0;

        @RangeConstraint(min = 0.1, max = 5.0)
        @Comment("Wild bush seed regrowth speed. Higher values make it slower; lower values make it faster. Makes perfect sense.")
        public double wildBushRegrowthModifier = 1.0;

        @RangeConstraint(min = 1, max = 100)
        @Comment("Wild green bean bush rarity (1 in X chunks). WARNING: Low values (1-5) might cause... bush spam.")
        public int wildGreenBeanBushRarity = 24;

        @RangeConstraint(min = 1, max = 100)
        @Comment("Wild cucumber bush rarity (1 in X chunks). WARNING: Ditto on the bush incidents.")
        public int wildCucumberBushRarity = 24;
    }

    // --- Buff Settings ---
    @SectionHeader("Buffs") // Config section for buff effects
    @Nest
    public Buffs buffs = new Buffs();

    public static class Buffs {
        @RangeConstraint(min = 20, max = 20 * 60 * 10)
        @Comment("Duration (ticks) of the Steamed Green Bean buff. Fleeting power.")
        public int greenBeanBuffDuration = 3600;

        @RangeConstraint(min = 0, max = 4) // Level I to V
        @Comment("Speed boost level (0=I, 1=II...) from beans. Gotta go fast?")
        public int greenBeanBuffAmplifierSpeed = 1;

        @RangeConstraint(min = 0, max = 4)
        @Comment("Strength boost level (0=I, 1=II...) from beans. For slightly mightier nibbles.")
        public int greenBeanBuffAmplifierStrength = 1;

        @RangeConstraint(min = 0, max = 4)
        @Comment("Absorption boost level (0=I, 1=II...) from beans. Extra fluff padding.")
        public int greenBeanBuffAmplifierAbsorption = 1;

        @RangeConstraint(min = 0, max = 4)
        @Comment("Regeneration boost level (0=I, 1=II...) from beans. Heals minor paper-cuts.")
        public int greenBeanBuffAmplifierRegen = 0;
    }
}