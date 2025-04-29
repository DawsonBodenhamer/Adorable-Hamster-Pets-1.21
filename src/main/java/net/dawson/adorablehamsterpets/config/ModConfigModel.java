package net.dawson.adorablehamsterpets.config;

/*  ────────────────────────────────────────────────────────────────────────────────
    GENERIC CONFIGURATION TEMPLATE
    --------------------------------
      • This file is **NOT** intended to ship with the finished mod.
      • Every field, range, and nested group is a placeholder so IDE-autocomplete
        works and other contributors (or AI assistants) can copy / adapt quickly.
      • Replace the dummy fields with REAL settings that actually matter to the
        mod once you know what those are.
      • Keep the annotations themselves (@Config, @Modmenu, @Nest, @RangeConstraint).
        They power code-generation and Mod Menu integration.
  ──────────────────────────────────────────────────────────────────────────────── */

/*  ── Required OWO-Config annotations ────────────────────────────────────────────
    @Config      – Declares this class as an owo-config model.
                   • name        → file name on disk (name.json)
                   • wrapperName → generated helper class ( <wrapperName>.java )
    @Modmenu     – Adds a “Config” button in Mod Menu for the specified mod id.
    @Nest        – Marks an inner class as a grouped block of settings.
    @RangeConstraint – Adds min / max validation on numeric fields.
*/

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.Nest;
import io.wispforest.owo.config.annotation.RangeConstraint;

@Modmenu(modId = "adorablehamsterpets")          // ← Your mod id goes here
@Config(
        name        = "adorablehamsterpets",     // ← Output file: adorablehamsterpets.json
        wrapperName = "ModConfig"                // ← Generated helper: ModConfig.java
)
public class ModConfigModel {

    // ──────────────────────────────────────────────────────────────────────────
    // BASIC EXAMPLE FLAGS  —  DELETE OR REPLACE WITH REAL ONES
    // ──────────────────────────────────────────────────────────────────────────

    /** Example boolean toggle (rename & re-document) */
    public boolean exampleBoolean = true;

    /** Example integer with no constraints (rename or add @RangeConstraint) */
    public int exampleInteger = 42;

    // ──────────────────────────────────────────────────────────────────────────
    // GROUPED OPTIONS (showing how to nest)
    // ──────────────────────────────────────────────────────────────────────────

    @Nest
    public GroupOne groupOne = new GroupOne();

    @Nest
    public GroupTwo groupTwo = new GroupTwo();

    // ── NESTED CLASS #1  —  Placeholder options for “Group One” ──────────────
    public static class GroupOne {

        /** Placeholder text value */
        public String exampleText = "replace-me";

        /** Range-constrained double */
        @RangeConstraint(min = 0, max = 1)
        public double exampleRatio = 0.5;
    }

    // ── NESTED CLASS #2  —  Placeholder options for “Group Two” ──────────────
    public static class GroupTwo {

        /** Another boolean flag */
        public boolean enableFeatureX = false;

        /** An enum example (replace with your own enum) */
        public DemoMode demoMode = DemoMode.OFF;

        // Dummy enum to illustrate enum config support
        public enum DemoMode {
            OFF,
            PARTIAL,
            FULL
        }
    }
}
