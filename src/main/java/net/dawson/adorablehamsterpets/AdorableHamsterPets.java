package net.dawson.adorablehamsterpets;

import net.dawson.adorablehamsterpets.block.ModBlocks;
import net.dawson.adorablehamsterpets.component.ModDataComponentTypes;
import net.dawson.adorablehamsterpets.effect.ModEffects;
import net.dawson.adorablehamsterpets.enchantment.ModEnchantmentEffects;
import net.dawson.adorablehamsterpets.entity.ModEntities;
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.dawson.adorablehamsterpets.item.ModItemGroups;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.dawson.adorablehamsterpets.potion.ModPotions;
import net.dawson.adorablehamsterpets.sound.ModSounds;
import net.dawson.adorablehamsterpets.util.HammerUsageEvent;
import net.dawson.adorablehamsterpets.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
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
		ModDataComponentTypes.registerDataComponentTypes();
		ModSounds.registerSounds();
		ModEffects.registerEffects();
		ModPotions.registerPotions();
		ModEnchantmentEffects.registerEnchantmentEffects();
		ModWorldGeneration.generateModWorldGen();
		ModEntities.registerModEntities();


		FuelRegistry.INSTANCE.add(ModItems.STARLIGHT_ASHES, 600);

		PlayerBlockBreakEvents.BEFORE.register(new HammerUsageEvent());
		AttackEntityCallback.EVENT.register((playerEntity, world, hand, entity, entityHitResult) -> {
			if(entity instanceof SheepEntity sheepEntity && !world.isClient()) {
				if (playerEntity.getMainHandStack().getItem() == Items.END_ROD) {
					playerEntity.sendMessage(Text.literal("The player just hit a sheep with an END ROD. YOU SICK LOL"));
					playerEntity.getMainHandStack().decrement(1);
					sheepEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 600, 6));
				}
				return ActionResult.PASS;
			}

			return ActionResult.PASS;
		});

		FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
			builder.registerPotionRecipe(Potions.AWKWARD, Items.SLIME_BALL, ModPotions.SLIMEY_POTION);
		});

		CompostingChanceRegistry.INSTANCE.add(ModItems.CAULIFLOWER, 0.5f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.CAULIFLOWER_SEEDS, 0.25f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.HONEY_BERRIES, 0.15f);

		StrippableBlockRegistry.register(ModBlocks.DRIFTWOOD_LOG, ModBlocks.STRIPPED_DRIFTWOOD_LOG);
		StrippableBlockRegistry.register(ModBlocks.DRIFTWOOD_WOOD, ModBlocks.STRIPPED_DRIFTWOOD_WOOD);

		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.DRIFTWOOD_LOG, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.DRIFTWOOD_WOOD, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.STRIPPED_DRIFTWOOD_LOG, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.STRIPPED_DRIFTWOOD_WOOD, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.DRIFTWOOD_PLANKS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.DRIFTWOOD_LEAVES, 30, 60);




		CompostingChanceRegistry.INSTANCE.add(ModItems.GREEN_BEANS, 0.5f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.CUCUMBER, 0.5f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.GREEN_BEAN_SEEDS, 0.25f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.CUCUMBER_SEEDS, 0.25f);


		FabricDefaultAttributeRegistry.register(ModEntities.HAMSTER, HamsterEntity.createAttributes());
	}
}