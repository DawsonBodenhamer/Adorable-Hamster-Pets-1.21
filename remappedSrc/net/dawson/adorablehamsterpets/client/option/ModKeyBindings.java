package net.dawson.adorablehamsterpets.client.option;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW; // Keep GLFW import

public class ModKeyBindings {

    public static final String KEY_CATEGORY_HAMSTERPETS = "key.category.adorablehamsterpets.main";
    public static final String KEY_THROW_HAMSTER = "key.adorablehamsterpets.throw_hamster";

    public static KeyBinding THROW_HAMSTER_KEY;

    public static void registerKeyInputs() {
        THROW_HAMSTER_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_THROW_HAMSTER,
                InputUtil.Type.KEYSYM, // <-- Correct type for keyboard keys like 'G'
                GLFW.GLFW_KEY_G,       // <-- Correct key code for 'G'
                KEY_CATEGORY_HAMSTERPETS
        ));
    }
}