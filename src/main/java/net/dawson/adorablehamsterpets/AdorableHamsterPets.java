package net.dawson.adorablehamsterpets;

import net.dawson.adorablehamsterpets.block.ModBlocks;
import net.dawson.adorablehamsterpets.item.ModItemGroups;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class AdorableHamsterPets implements ModInitializer {
	public static final String MOD_ID = "adorablehamsterpets";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		FuelRegistry.INSTANCE.add(ModItems.STARLIGHT_ASHES, 600);
	}
}