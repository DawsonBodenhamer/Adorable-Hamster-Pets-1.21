package net.dawson.adorablehamsterpets.screen;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType; // Import ExtendedScreenHandlerType
import net.minecraft.network.codec.PacketCodecs; // Import PacketCodecs
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType; // Keep this import if needed elsewhere, but not for the constructor
import net.minecraft.util.Identifier; // Import Identifier

public class ModScreenHandlers {
    // Change the type to ExtendedScreenHandlerType, specifying the data type as Integer
    public static ExtendedScreenHandlerType<HamsterInventoryScreenHandler, Integer> HAMSTER_INVENTORY_SCREEN_HANDLER;

    public static void registerScreenHandlers() {
        AdorableHamsterPets.LOGGER.info("Registering Screen Handlers for " + AdorableHamsterPets.MOD_ID); // Added logger info

        HAMSTER_INVENTORY_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER,
                Identifier.of(AdorableHamsterPets.MOD_ID, "hamster_inventory"), // Use Identifier.of
                // Use the ExtendedScreenHandlerType constructor
                new ExtendedScreenHandlerType<>(
                        // Provide the ExtendedFactory lambda: (syncId, inventory, entityId) -> create handler
                        // This is called on the CLIENT to create the screen handler instance
                        HamsterInventoryScreenHandler::new, // Method reference to the client-side constructor
                        // Provide the PacketCodec for Integer (entity ID)
                        PacketCodecs.VAR_INT // Use the standard codec for variable-length integers
                ));
    }
}