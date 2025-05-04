package net.dawson.adorablehamsterpets.entity.custom;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.attachment.HamsterShoulderData;
import net.dawson.adorablehamsterpets.attachment.ModEntityAttachments;
import net.dawson.adorablehamsterpets.config.ModConfig;
import net.dawson.adorablehamsterpets.entity.AI.*;
import net.dawson.adorablehamsterpets.entity.ImplementedInventory;
import net.dawson.adorablehamsterpets.entity.ModEntities;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.dawson.adorablehamsterpets.networking.payload.SpawnAttackParticlesPayload;
import net.dawson.adorablehamsterpets.networking.payload.StartHamsterFlightSoundPayload;
import net.dawson.adorablehamsterpets.networking.payload.StartHamsterThrowSoundPayload;
import net.dawson.adorablehamsterpets.screen.HamsterEntityScreenHandlerFactory;
import net.dawson.adorablehamsterpets.sound.ModSounds;
import net.dawson.adorablehamsterpets.tag.ModItemTags;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import org.joml.Vector3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import java.util.*;
import static net.dawson.adorablehamsterpets.sound.ModSounds.HAMSTER_CELEBRATE_SOUNDS;
import static net.dawson.adorablehamsterpets.sound.ModSounds.getRandomSoundFrom;


public class HamsterEntity extends TameableEntity implements GeoEntity, ImplementedInventory {


    /* ──────────────────────────────────────────────────────────────────────────────
     *                    1. Constants and Static Utilities
     * ────────────────────────────────────────────────────────────────────────────*/

    // --- Constants ---
    private static final int INVENTORY_SIZE = 6;
    private static final int REFUSE_FOOD_TIMER_TICKS = 40; // 2 seconds
    private static final int CUSTOM_LOVE_TICKS = 600; // 30 seconds
    private static final float THROW_DAMAGE = 20.0f;
    private static final double THROWN_GRAVITY = -0.05;
    private static final double HAMSTER_ATTACK_BOX_EXPANSION = 0.5D; // Expand by 0.5 blocks horizontally (vanilla is 0.83 blocks)

    // --- Item Restriction Sets ---
    private static final Set<TagKey<Item>> DISALLOWED_ITEM_TAGS = Set.of(
            // --- Tool Tags ---
            net.minecraft.registry.tag.ItemTags.AXES,
            net.minecraft.registry.tag.ItemTags.HOES,
            net.minecraft.registry.tag.ItemTags.PICKAXES,
            net.minecraft.registry.tag.ItemTags.SHOVELS,
            net.minecraft.registry.tag.ItemTags.SWORDS,
            // Armor
            net.minecraft.registry.tag.ItemTags.TRIMMABLE_ARMOR,
            // Large Blocks/Structures
            net.minecraft.registry.tag.ItemTags.BEDS,
            net.minecraft.registry.tag.ItemTags.BANNERS,
            net.minecraft.registry.tag.ItemTags.DOORS,
            // Vehicles
            net.minecraft.registry.tag.ItemTags.BOATS, // Covers Boats & Chest Boats
            net.minecraft.registry.tag.ItemTags.CREEPER_DROP_MUSIC_DISCS

    );

    private static final Set<Item> DISALLOWED_ITEMS = Set.of(
            // Specific Tools/Weapons not covered by tags
            Items.BOW, Items.CROSSBOW, Items.TRIDENT, Items.FISHING_ROD,
            // Specific Armor/Wearables
            Items.SHIELD, Items.ELYTRA,
            Items.TURTLE_HELMET,
            Items.CARVED_PUMPKIN,
            Items.PLAYER_HEAD, Items.ZOMBIE_HEAD, Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL, Items.CREEPER_HEAD, Items.DRAGON_HEAD, Items.PIGLIN_HEAD,
            // Vehicles/Mounts
            Items.MINECART, Items.CHEST_MINECART, Items.FURNACE_MINECART, Items.TNT_MINECART, Items.HOPPER_MINECART, Items.COMMAND_BLOCK_MINECART,
            Items.SADDLE,
            // Buckets
            Items.BUCKET, Items.WATER_BUCKET, Items.LAVA_BUCKET, Items.MILK_BUCKET, Items.POWDER_SNOW_BUCKET,
            Items.AXOLOTL_BUCKET, Items.TADPOLE_BUCKET, Items.COD_BUCKET, Items.PUFFERFISH_BUCKET, Items.SALMON_BUCKET, Items.TROPICAL_FISH_BUCKET,
            // Complex/Utility/Special
            Items.ITEM_FRAME, Items.GLOW_ITEM_FRAME,
            Items.PAINTING,
            Items.ARMOR_STAND,
            Items.END_CRYSTAL,
            Items.SPYGLASS,
            Items.NETHER_STAR, Items.DRAGON_EGG,
            Items.BUNDLE,
            // Mod Items
            ModItems.HAMSTER_GUIDE_BOOK
    );
    // --- End Item Restriction Sets ---

    // Define food sets as static final fields
    private static final Set<Item> HAMSTER_FOODS = new HashSet<>(Arrays.asList(
            ModItems.HAMSTER_FOOD_MIX, ModItems.SUNFLOWER_SEEDS, ModItems.GREEN_BEANS,
            ModItems.CUCUMBER, ModItems.GREEN_BEAN_SEEDS, ModItems.CUCUMBER_SEEDS,
            Items.APPLE, Items.CARROT, Items.MELON_SLICE, Items.SWEET_BERRIES,
            Items.BEETROOT, Items.WHEAT, Items.WHEAT_SEEDS
    ));
    private static final Set<Item> REPEATABLE_FOODS = new HashSet<>(Arrays.asList(
            ModItems.HAMSTER_FOOD_MIX, ModItems.STEAMED_GREEN_BEANS
    ));

    // --- Auto-Feedable Healing Foods ---
    private static final Set<Item> AUTO_HEAL_FOODS = new HashSet<>(List.of(
            ModItems.HAMSTER_FOOD_MIX // Only allow Hamster Food Mix
    ));
    // --- End Auto-Feedable Healing Foods ---

    // --- Variant Pools ---
    private static final List<HamsterVariant> ORANGE_VARIANTS = List.of(
            HamsterVariant.ORANGE, HamsterVariant.ORANGE_WHITE_SPLIT, HamsterVariant.ORANGE_MOSTLY_WHITE,
            HamsterVariant.ORANGE_WHITE_SPOTS, HamsterVariant.ORANGE_WHITE_CHEST
    );
    private static final List<HamsterVariant> BLACK_VARIANTS = List.of(
            HamsterVariant.BLACK, HamsterVariant.BLACK_WHITE_SPLIT, HamsterVariant.BLACK_MOSTLY_WHITE,
            HamsterVariant.BLACK_WHITE_SPOTS, HamsterVariant.BLACK_WHITE_CHEST
    );
    private static final List<HamsterVariant> CHOCOLATE_VARIANTS = List.of(
            HamsterVariant.CHOCOLATE, HamsterVariant.CHOCOLATE_WHITE_SPLIT, HamsterVariant.CHOCOLATE_MOSTLY_WHITE,
            HamsterVariant.CHOCOLATE_WHITE_SPOTS, HamsterVariant.CHOCOLATE_WHITE_CHEST
    );
    private static final List<HamsterVariant> CREAM_VARIANTS = List.of(
            HamsterVariant.CREAM, HamsterVariant.CREAM_WHITE_SPLIT, HamsterVariant.CREAM_MOSTLY_WHITE,
            HamsterVariant.CREAM_WHITE_SPOTS, HamsterVariant.CREAM_WHITE_CHEST
    );
    private static final List<HamsterVariant> DARK_GRAY_VARIANTS = List.of(
            HamsterVariant.DARK_GRAY, HamsterVariant.DARK_GRAY_WHITE_SPLIT, HamsterVariant.DARK_GRAY_MOSTLY_WHITE,
            HamsterVariant.DARK_GRAY_WHITE_SPOTS, HamsterVariant.DARK_GRAY_WHITE_CHEST
    );
    private static final List<HamsterVariant> LIGHT_GRAY_VARIANTS = List.of(
            HamsterVariant.LIGHT_GRAY, HamsterVariant.LIGHT_GRAY_WHITE_SPLIT, HamsterVariant.LIGHT_GRAY_MOSTLY_WHITE,
            HamsterVariant.LIGHT_GRAY_WHITE_SPOTS, HamsterVariant.LIGHT_GRAY_WHITE_CHEST
    );
    // White has no sub-variants in the spec
    private static final List<HamsterVariant> WHITE_VARIANTS = List.of(HamsterVariant.WHITE);

    // --- Hamster Spawning In Different Biomes ---

    // Helper to check if a biome key matches any key in a list
    private static boolean matchesAnyBiomeKey(RegistryEntry<Biome> biomeEntry, RegistryKey<Biome>... keysToMatch) {
        for (RegistryKey<Biome> key : keysToMatch) {
            if (biomeEntry.matchesKey(key)) {
                return true;
            }
        }
        return false;
    }

    // Specific biome category checks using the helper
    private static boolean isSnowyBiome(RegistryEntry<Biome> biomeEntry) {
        return matchesAnyBiomeKey(biomeEntry,
                BiomeKeys.SNOWY_PLAINS, BiomeKeys.SNOWY_TAIGA, BiomeKeys.SNOWY_SLOPES,
                BiomeKeys.FROZEN_PEAKS, BiomeKeys.JAGGED_PEAKS, BiomeKeys.ICE_SPIKES,
                BiomeKeys.GROVE, BiomeKeys.FROZEN_RIVER, BiomeKeys.SNOWY_BEACH,
                BiomeKeys.FROZEN_OCEAN, BiomeKeys.DEEP_FROZEN_OCEAN
        );
    }

    private static boolean isDesertBiome(RegistryEntry<Biome> biomeEntry) {
        // Deserts don't have a tag, check the specific key
        return biomeEntry.matchesKey(BiomeKeys.DESERT);
    }

    private static boolean isPlainsBiome(RegistryEntry<Biome> biomeEntry) {
        // Plains don't have a tag, check specific keys
        return matchesAnyBiomeKey(biomeEntry, BiomeKeys.PLAINS, BiomeKeys.SUNFLOWER_PLAINS, BiomeKeys.MEADOW);
        // Including Meadow here as it's plains-like
    }

    private static boolean isSwampBiome(RegistryEntry<Biome> biomeEntry) {
        // Swamps don't have a tag, check specific keys
        return matchesAnyBiomeKey(biomeEntry, BiomeKeys.SWAMP, BiomeKeys.MANGROVE_SWAMP);
    }

    private static boolean isCaveBiome(RegistryEntry<Biome> biomeEntry) {
        // Check specific cave keys, excluding Deep Dark
        return matchesAnyBiomeKey(biomeEntry, BiomeKeys.LUSH_CAVES, BiomeKeys.DRIPSTONE_CAVES);
    }

    // Helper Method for Variant Choosing
    private static HamsterVariant determineVariantForBiome(RegistryEntry<Biome> biomeEntry, net.minecraft.util.math.random.Random random) {
        String biomeKeyStr = biomeEntry.getKey().map(key -> key.getValue().toString()).orElse("UNKNOWN");
        AdorableHamsterPets.LOGGER.trace("[DetermineVariant] Checking biome: {}", biomeKeyStr);

        // Use helper methods and BiomeTags constants
        if (isSnowyBiome(biomeEntry)) {
            AdorableHamsterPets.LOGGER.trace("  - Matched: Snowy Keys");
            return getRandomVariant(WHITE_VARIANTS, random);
        } else if (isCaveBiome(biomeEntry)) {
            AdorableHamsterPets.LOGGER.trace("  - Matched: Cave Keys (Lush/Dripstone)");
            int chance = random.nextInt(4); // 0, 1, 2, 3
            AdorableHamsterPets.LOGGER.trace("  - Cave chance roll: {}", chance);
            if (chance < 2) { return getRandomVariant(BLACK_VARIANTS, random); }
            else if (chance == 2) { return getRandomVariant(DARK_GRAY_VARIANTS, random); }
            else { return getRandomVariant(LIGHT_GRAY_VARIANTS, random); }
        } else if (isSwampBiome(biomeEntry)) {
            AdorableHamsterPets.LOGGER.trace("  - Matched: Swamp Keys");
            return getRandomVariant(BLACK_VARIANTS, random);
        } else if (isDesertBiome(biomeEntry)) {
            AdorableHamsterPets.LOGGER.trace("  - Matched: Desert Key");
            return getRandomVariant(CREAM_VARIANTS, random);
        } else if (biomeEntry.isIn(BiomeTags.IS_BADLANDS)) { // Use BiomeTags
            AdorableHamsterPets.LOGGER.trace("  - Matched: BiomeTags.IS_BADLANDS");
            return getRandomVariant(ORANGE_VARIANTS, random);
        } else if (biomeEntry.isIn(BiomeTags.IS_BEACH)) { // Use BiomeTags
            AdorableHamsterPets.LOGGER.trace("  - Matched: BiomeTags.IS_BEACH");
            return getRandomVariant(CREAM_VARIANTS, random);
        } else if (biomeEntry.isIn(BiomeTags.IS_FOREST) || biomeEntry.isIn(BiomeTags.IS_TAIGA)) { // Use BiomeTags
            AdorableHamsterPets.LOGGER.trace("  - Matched: BiomeTags.IS_FOREST or IS_TAIGA");
            return getRandomVariant(CHOCOLATE_VARIANTS, random);
        } else if (biomeEntry.isIn(BiomeTags.IS_SAVANNA) || isPlainsBiome(biomeEntry)) { // Use BiomeTags + Helper
            AdorableHamsterPets.LOGGER.trace("  - Matched: BiomeTags.IS_SAVANNA or Plains Keys");
            return getRandomVariant(ORANGE_VARIANTS, random);
        } else if (biomeEntry.isIn(BiomeTags.IS_MOUNTAIN)) { // Use BiomeTags (covers hills too generally)
            AdorableHamsterPets.LOGGER.trace("  - Matched: BiomeTags.IS_MOUNTAIN");
            boolean doveChance = random.nextBoolean();
            AdorableHamsterPets.LOGGER.trace("  - Mountain roll (true=Dove, false=SilverDove): {}", doveChance);
            if (doveChance) { return getRandomVariant(DARK_GRAY_VARIANTS, random); }
            else { return getRandomVariant(LIGHT_GRAY_VARIANTS, random); }
        } else {
            // Default fallback is Orange
            AdorableHamsterPets.LOGGER.trace("  - No specific tags/keys matched. Using default ORANGE.");
            return getRandomVariant(ORANGE_VARIANTS, random);
        }
    }

    private static HamsterVariant getRandomVariant(List<HamsterVariant> variantPool, net.minecraft.util.math.random.Random random) {
        if (variantPool == null || variantPool.isEmpty()) {
            // Fallback if a pool is somehow empty
            return HamsterVariant.ORANGE;
        }
        // CHANGE: Use nextInt(bound) from the correct Random type
        return variantPool.get(random.nextInt(variantPool.size()));
    }

    // Heler Method for Choosing Baby Variant
    private static List<HamsterVariant> getPoolForBaseVariant(HamsterVariant baseVariant) {
        return switch (baseVariant) {
            case ORANGE -> ORANGE_VARIANTS;
            case BLACK -> BLACK_VARIANTS;
            case CHOCOLATE -> CHOCOLATE_VARIANTS;
            case CREAM -> CREAM_VARIANTS;
            case DARK_GRAY -> DARK_GRAY_VARIANTS;
            case LIGHT_GRAY -> LIGHT_GRAY_VARIANTS;
            case WHITE -> WHITE_VARIANTS;
            // Default case should not be reachable if baseVariant is always one of the above
            default -> ORANGE_VARIANTS; // Fallback
        };
    }

    /**
     * Creates the attribute container for the Hamster entity.
     * @return The attribute container builder.
     */
    public static DefaultAttributeContainer.Builder createHamsterAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, THROW_DAMAGE)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, AdorableHamsterPets.CONFIG.behavior.hamsterMeleeDamage()) // Use config
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0D);
    }


    /**
     * Creates a HamsterEntity instance from shoulder data, loading variant, health, age, inventory, effects, and custom name.
     * Does NOT set position or spawn the entity.
     * @param world The server world.
     * @param player The player the hamster belongs to.
     * @param data The shoulder data.
     * @return The created HamsterEntity, or null if creation failed.
     */
    @Nullable
    public static HamsterEntity createFromShoulderData(ServerWorld world, PlayerEntity player, HamsterShoulderData data) {
        // --- 1. Create Hamster Instance ---
        AdorableHamsterPets.LOGGER.debug("[HamsterEntity] createFromShoulderData called for player {} with data: {}", player.getName().getString(), data);
        HamsterEntity hamster = ModEntities.HAMSTER.create(world);
        // --- End 1. Create Hamster Instance ---


        if (hamster != null) {
            // --- 2. Load Core Data ---
            hamster.setVariant(data.variantId());
            hamster.setHealth(data.health());
            hamster.setOwnerUuid(player.getUuid());
            hamster.setTamed(true, true);
            hamster.setBreedingAge(data.breedingAge());
            hamster.throwCooldownEndTick = data.throwCooldownEndTick();
            hamster.steamedBeansCooldownEndTick = data.steamedBeansCooldownEndTick();
            hamster.autoEatCooldownTicks = data.autoEatCooldownTicks();
            // --- End 2. Load Core Data ---


            // --- 2b. Load Custom Name ---
            data.customName().ifPresent(name -> {
                if (!name.isEmpty()) { // Check if the name string is actually present
                    hamster.setCustomName(Text.literal(name));
                    AdorableHamsterPets.LOGGER.debug("[HamsterEntity] Restored custom name '{}' from shoulder data.", name);
                } else {
                    AdorableHamsterPets.LOGGER.debug("[HamsterEntity] Custom name in shoulder data was empty, not setting name.");
                }
            });
            // --- End 2b. Load Custom Name ---


            // --- 3. Load Inventory ---
            NbtCompound inventoryNbt = data.inventoryNbt();
            RegistryWrapper.WrapperLookup registries = world.getRegistryManager();
            if (!inventoryNbt.isEmpty()) {
                Inventories.readNbt(inventoryNbt, hamster.items, registries);
                AdorableHamsterPets.LOGGER.debug("[HamsterEntity] Loaded inventory from NBT: {}", inventoryNbt);
                hamster.updateCheekTrackers();
            } else {
                AdorableHamsterPets.LOGGER.debug("[HamsterEntity] No inventory NBT found in shoulder data, clearing inventory.");
                hamster.items.clear();
                hamster.updateCheekTrackers();
            }
            // --- End 3. Load Inventory ---


            // --- 4. Load and Apply Active Status Effects ---
            NbtList effectsList = data.activeEffectsNbt();
            if (!effectsList.isEmpty()) {
                AdorableHamsterPets.LOGGER.debug("[HamsterEntity] Loading {} active effects from NBT list.", effectsList.size());
                for (NbtElement effectElement : effectsList) {
                    if (effectElement instanceof NbtCompound effectNbt) {
                        StatusEffectInstance effectInstance = StatusEffectInstance.fromNbt(effectNbt);
                        if (effectInstance != null) {
                            hamster.addStatusEffect(effectInstance);
                        } else {
                            AdorableHamsterPets.LOGGER.warn("[HamsterEntity] Failed to deserialize StatusEffectInstance from NBT: {}", effectNbt);
                        }
                    }
                }
            } else {
                AdorableHamsterPets.LOGGER.debug("[HamsterEntity] No active effects NBT found in shoulder data.");
            }
            // --- End 4. Load Active Status Effects ---


            // --- 5. Reset Eating State ---
            hamster.isAutoEating = false;
            hamster.autoEatProgressTicks = 0;
            // --- End 5. Reset Eating State ---


        } else {
            AdorableHamsterPets.LOGGER.error("[HamsterEntity] Failed to create HamsterEntity instance in createFromShoulderData.");
        }
        return hamster;
    }

    /**
     * Spawns a HamsterEntity from shoulder data near the player, handling position and spawning.
     * Uses createFromShoulderData to load the entity's state.
     * @param world The server world.
     * @param player The player dismounting the hamster.
     * @param data The shoulder data.
     */
    public static void spawnFromShoulderData(ServerWorld world, PlayerEntity player, HamsterShoulderData data) {
        // --- 1. Create and Configure Hamster ---
        AdorableHamsterPets.LOGGER.debug("[HamsterEntity] spawnFromShoulderData called for player {} with data: {}", player.getName().getString(), data);
        HamsterEntity hamster = createFromShoulderData(world, player, data);
        // --- End 1. Create and Configure Hamster ---

        if (hamster != null) {
            // --- 2. Set Position for Normal Dismount ---
            double angle = Math.toRadians(player.getYaw());
            double offsetX = -Math.sin(angle) * 0.7;
            double offsetZ = Math.cos(angle) * 0.7;
            hamster.refreshPositionAndAngles(player.getX() + offsetX, player.getY() + 0.1, player.getZ() + offsetZ, player.getYaw(), player.getPitch());
            // --- End 2. Position Setting ---

            // --- 3. Spawn Entity ---
            AdorableHamsterPets.LOGGER.debug("[HamsterEntity] Spawning hamster entity from shoulder data...");
            world.spawnEntityAndPassengers(hamster); // Spawn the fully configured hamster
            AdorableHamsterPets.LOGGER.debug("[HamsterEntity] Spawned Hamster ID {} from shoulder data near Player {}", hamster.getId(), player.getName().getString());
            // --- End 3. Spawn Entity ---
        }
    }

    /**
     * Attempts to throw the hamster from the player's shoulder.
     * Called server-side when the throw packet is received.
     * @param player The player attempting the throw.
     */
    public static void tryThrowFromShoulder(ServerPlayerEntity player) {
        // --- 1. Initial Setup ---
        World world = player.getWorld();
        UUID playerUuid = player.getUuid();
        HamsterShoulderData shoulderData = player.getAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA);
        final ModConfig config = AdorableHamsterPets.CONFIG; // Access static config
        // --- End 1. Initial Setup ---

        // --- Check Config Toggle ---
        if (!config.features.enableHamsterThrowing()) {
            AdorableHamsterPets.LOGGER.debug("[HamsterEntity] tryThrowFromShoulder: Throwing disabled in config for player {}.", player.getName().getString());
            // Send message
            player.sendMessage(Text.literal("Hamster throwing is disabled in config."), true);
            return;
        }
        // --- End Check Config Toggle ---

        if (shoulderData != null) {
            AdorableHamsterPets.LOGGER.debug("[HamsterEntity] tryThrowFromShoulder: Player {} has shoulder data, proceeding.", player.getName().getString());

            // --- 2. Create Hamster Instance ---
            ServerWorld serverWorld = (ServerWorld) world;
            HamsterEntity hamster = HamsterEntity.createFromShoulderData(serverWorld, player, shoulderData);
            // --- End 2. Create Hamster Instance ---

            if (hamster != null) {
                AdorableHamsterPets.LOGGER.debug("[HamsterEntity] tryThrowFromShoulder: Hamster instance created with cooldownEndTick {} and isBaby={}.", hamster.throwCooldownEndTick, hamster.isBaby());

                // --- 3. Check if Hamster is Baby ---
                if (hamster.isBaby()) {
                    player.sendMessage(
                            Text.translatable("message.adorablehamsterpets.baby_throw_refusal")
                                    .formatted(Formatting.RED),
                            true // Send to action bar
                    );
                    AdorableHamsterPets.LOGGER.debug("[HamsterEntity] Hamster instance ID {} (from player {}) is a baby. Aborting throw.", hamster.getId(), player.getName().getString());
                    // Re-attach data since the throw is aborted
                    player.setAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA, shoulderData);
                    return; // Stop the throw
                }
                // --- End 3. Baby Check ---

                // --- 4. Check Specific Hamster's Cooldown ---
                long currentTime = world.getTime();
                // Use Config Value for Throw Cooldown Check
                if (hamster.throwCooldownEndTick > currentTime) {
                    long remainingTicks = hamster.throwCooldownEndTick - currentTime;
                    long totalSecondsRemaining = remainingTicks / 20;
                    long minutes = totalSecondsRemaining / 60;
                    long seconds = totalSecondsRemaining % 60;
                    player.sendMessage(Text.translatable("message.adorablehamsterpets.throw_cooldown", minutes, seconds).formatted(Formatting.RED), true);
                    player.setAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA, shoulderData); // Re-attach data
                    return;
                }
                // --- End 4. Hamster Cooldown Check ---

                // --- 5. Proceed with Throw ---
                player.removeAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA); // Now remove shoulder data

                // --- 5a. Set Position and Velocity ---
                hamster.refreshPositionAndAngles(player.getX(), player.getEyeY() - 0.1, player.getZ(), player.getYaw(), player.getPitch());
                hamster.setThrown(true);
                hamster.interactionCooldown = 10;
                hamster.throwTicks = 0;

                // --- Set Cooldown on the Hamster Instance ---
                hamster.throwCooldownEndTick = currentTime + config.cooldowns.hamsterThrowCooldown();
                AdorableHamsterPets.LOGGER.debug("[HamsterEntity] Set throw cooldown for hamster instance ID {} ending at tick {}.", hamster.getId(), hamster.throwCooldownEndTick);
                // --- End Set Cooldown ---

                float throwSpeed = 1.5f;
                Vec3d lookVec = player.getRotationVec(1.0f);
                Vec3d throwVec = new Vec3d(lookVec.x, lookVec.y + 0.1f, lookVec.z).normalize();
                hamster.setVelocity(throwVec.multiply(throwSpeed));
                hamster.velocityDirty = true;
                // --- End 5a. Set Position and Velocity ---

                // --- 5b. Spawn the entity ---
                serverWorld.spawnEntity(hamster);
                AdorableHamsterPets.LOGGER.debug("[HamsterEntity] tryThrowFromShoulder: Spawned thrown Hamster ID {}.", hamster.getId());
                // --- End 5b. Spawn the entity ---

                // --- 5c. Send S2C Packets ---
                StartHamsterFlightSoundPayload flightPayload = new StartHamsterFlightSoundPayload(hamster.getId());
                StartHamsterThrowSoundPayload throwPayload = new StartHamsterThrowSoundPayload(hamster.getId());

                // Send to thrower and nearby players (existing logic)
                ServerPlayNetworking.send(player, flightPayload);
                ServerPlayNetworking.send(player, throwPayload);
                double radius = 64.0;
                Vec3d hamsterPos = hamster.getPos();
                Box searchBox = new Box(hamsterPos.subtract(radius, radius, radius), hamsterPos.add(radius, radius, radius));
                List<ServerPlayerEntity> nearbyPlayers = serverWorld.getPlayers(p -> p != player && searchBox.contains(p.getPos()));
                for (ServerPlayerEntity nearbyPlayer : nearbyPlayers) {
                    ServerPlayNetworking.send(nearbyPlayer, flightPayload);
                    ServerPlayNetworking.send(nearbyPlayer, throwPayload);
                }
                // --- End 5c. Send S2C Packets ---
                // --- End 5. Proceed with Throw ---

            } else {
                AdorableHamsterPets.LOGGER.error("[HamsterEntity] tryThrowFromShoulder: Failed to create HamsterEntity instance. Re-attaching data (attempt).");
                // Ensure data is re-attached if hamster creation fails
                player.setAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA, shoulderData);
            }
        } else {
            AdorableHamsterPets.LOGGER.warn("[HamsterEntity] tryThrowFromShoulder: Player {} received throw packet but had no shoulder data.", player.getName().getString());
        }
    }

    /**
     * Checks if the given item stack is considered a standard hamster food item.
     * @param stack The item stack to check.
     * @return True if the item is in the HAMSTER_FOODS set, false otherwise.
     */
    private static boolean isIsFood(ItemStack stack) {
        return HAMSTER_FOODS.contains(stack.getItem());
    }

    // --- Data Trackers ---
    private static final TrackedData<Integer> VARIANT = DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Boolean> IS_SLEEPING = DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> IS_SITTING = DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> IS_BEGGING = DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> IS_IN_LOVE = DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> IS_REFUSING_FOOD = DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> IS_THROWN = DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> LEFT_CHEEK_FULL = DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> RIGHT_CHEEK_FULL = DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> IS_KNOCKED_OUT = DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    // --- Animation Constants ---
    private static final RawAnimation CRASH_ANIM = RawAnimation.begin().thenPlay("anim_hamster_crash");
    private static final RawAnimation KNOCKED_OUT_ANIM = RawAnimation.begin().thenPlay("anim_hamster_ko");
    private static final RawAnimation WAKE_UP_ANIM = RawAnimation.begin().thenPlay("anim_hamster_wakeup");
    private static final RawAnimation FLYING_ANIM = RawAnimation.begin().thenPlay("anim_hamster_flying");
    private static final RawAnimation NO_ANIM = RawAnimation.begin().thenPlay("anim_hamster_no");
    private static final RawAnimation SLEEPING_ANIM = RawAnimation.begin().thenPlay("anim_hamster_sleeping");
    private static final RawAnimation SITTING_ANIM = RawAnimation.begin().thenPlay("anim_hamster_sitting");
    private static final RawAnimation CLEANING_ANIM = RawAnimation.begin().thenPlay("anim_hamster_cleaning");
    private static final RawAnimation RUNNING_ANIM = RawAnimation.begin().thenPlay("anim_hamster_running");
    private static final RawAnimation WALKING_ANIM = RawAnimation.begin().thenPlay("anim_hamster_walking");
    private static final RawAnimation BEGGING_ANIM = RawAnimation.begin().thenPlay("anim_hamster_begging");
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenPlay("anim_hamster_idle");
    private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("anim_hamster_attack");



    /* ──────────────────────────────────────────────────────────────────────────────
     *                                  2. Fields
     * ────────────────────────────────────────────────────────────────────────────*/

    // --- Unique Instance Fields ---
    @Unique private int interactionCooldown = 0;
    @Unique private int throwTicks = 0;
    @Unique public int wakingUpTicks = 0;
    @Unique private int ejectionCheckCooldown = 20;

    // --- Inventory ---
    private final DefaultedList<ItemStack> items = ImplementedInventory.create(INVENTORY_SIZE);

    // --- Animation ---
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // --- State Variables ---
    private int refuseTimer = 0;
    private ItemStack lastFoodItem = ItemStack.EMPTY;
    public int customLoveTimer;
    private int tamingCooldown = 0;
    private long throwCooldownEndTick = 0L;
    private long steamedBeansCooldownEndTick = 0L;

    // --- Auto-Eating State/Cooldown Fields ---
    private boolean isAutoEating = false; // Flag for potential animation hook
    private int autoEatProgressTicks = 0; // Ticks remaining for the current eating action
    private int autoEatCooldownTicks = 0; // Ticks remaining before it can start eating again
    // --- End Auto-Eating Fields ---

    private int cleaningTimer = 0;
    private int cleaningCooldownTimer = 0;
    private int blinkTimer = 0; // Timer for current blink duration/state
    private int nextBlinkCheckTick = 200; // Ticks until next potential blink



    /* ──────────────────────────────────────────────────────────────────────────────
     *                             3. Constructor
     * ────────────────────────────────────────────────────────────────────────────*/

    public HamsterEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 3;
    }



    /* ──────────────────────────────────────────────────────────────────────────────
     *                             4. Public Methods
     * ────────────────────────────────────────────────────────────────────────────*/

    // --- Data Tracker Getters/Setters ---
    public int getVariant() { return this.dataTracker.get(VARIANT); }
    public void setVariant(int variantId) { this.dataTracker.set(VARIANT, variantId); }
    public boolean isSleeping() { return this.dataTracker.get(IS_SLEEPING); }
    public void setSleeping(boolean sleeping) { this.dataTracker.set(IS_SLEEPING, sleeping); }
    @Override public boolean isSitting() {  return this.dataTracker.get(IS_SITTING) || this.dataTracker.get(IS_SLEEPING) || this.dataTracker.get(IS_KNOCKED_OUT); }
    public boolean isBegging() { return this.dataTracker.get(IS_BEGGING); }
    public void setBegging(boolean value) { this.dataTracker.set(IS_BEGGING, value); }
    public boolean isInLove() { return this.dataTracker.get(IS_IN_LOVE); }
    public void setInLove(boolean value) { this.dataTracker.set(IS_IN_LOVE, value); }
    public boolean isRefusingFood() { return this.dataTracker.get(IS_REFUSING_FOOD); }
    public void setRefusingFood(boolean value) { this.dataTracker.set(IS_REFUSING_FOOD, value); }
    public boolean isThrown() { return this.dataTracker.get(IS_THROWN); }
    public void setThrown(boolean thrown) { this.dataTracker.set(IS_THROWN, thrown); }
    public boolean isLeftCheekFull() { return this.dataTracker.get(LEFT_CHEEK_FULL); }
    public void setLeftCheekFull(boolean full) { this.dataTracker.set(LEFT_CHEEK_FULL, full); }
    public boolean isRightCheekFull() { return this.dataTracker.get(RIGHT_CHEEK_FULL); }
    public void setRightCheekFull(boolean full) { this.dataTracker.set(RIGHT_CHEEK_FULL, full); }
    public boolean isKnockedOut() { return this.dataTracker.get(IS_KNOCKED_OUT); }
    public void setKnockedOut(boolean knocked_out) { this.dataTracker.set(IS_KNOCKED_OUT, knocked_out); }
    public int getBlinkTimer() {return this.blinkTimer;}

    // --- Add Getter for Animation State ---
    /**
     * Returns true if the hamster is currently in the process of auto-eating.
     * Can be used by animation controllers, but currently there is no eating animation so it's not being used.
     */
    public boolean isAutoEating() {
        return this.isAutoEating;
    }
    // --- End Getter ---

    // --- Inventory Implementation (ImplementedInventory) ---
    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public void markDirty() {
        if (!this.getWorld().isClient()) {
            this.updateCheekTrackers();
        }
    }

    // --- Override isValid for Hopper Interaction ---
    @Override
    public boolean isValid(int slot, ItemStack stack) {
        // --- 1. Check if the item is allowed based on the disallowed logic ---
        // Ensure the slot index is valid for the hamster inventory (0-5)
        if (slot < 0 || slot >= INVENTORY_SIZE) {
            return false;
        }
        // Use the helper method to determine if the item is allowed
        return !this.isItemDisallowed(stack);
        // --- End 1. Check if the item is allowed based on the disallowed logic ---
    }
    // --- End isValid Override ---

    public void updateCheekTrackers() {
        boolean leftFull = false;
        for (int i = 0; i < 3; i++) { if (!this.items.get(i).isEmpty()) { leftFull = true; break; } }
        boolean rightFull = false;
        for (int i = 3; i < INVENTORY_SIZE; i++) { if (!this.items.get(i).isEmpty()) { rightFull = true; break; } }
        if (this.isLeftCheekFull() != leftFull) this.setLeftCheekFull(leftFull);
        if (this.isRightCheekFull() != rightFull) this.setRightCheekFull(rightFull);
    }

    // --- NBT Saving/Loading ---
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        // --- 1. Write Core Data ---
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("HamsterVariant", this.getVariant());
        nbt.putBoolean("Sitting", this.dataTracker.get(IS_SITTING)); // Write raw sitting state
        nbt.putBoolean("KnockedOut", this.isKnockedOut());
        nbt.putLong("ThrowCooldownEnd", this.throwCooldownEndTick);
        nbt.putLong("SteamedBeansCooldownEnd", this.steamedBeansCooldownEndTick);
        nbt.putInt("AutoEatCooldown", this.autoEatCooldownTicks);
        nbt.putInt("EjectionCheckCooldown", this.ejectionCheckCooldown);
        // --- End 1. Write Core Data ---

        // --- 2. Write Inventory ---
        RegistryWrapper.WrapperLookup registries = getRegistryLookup();
        NbtCompound inventoryWrapperNbt = new NbtCompound();
        Inventories.writeNbt(inventoryWrapperNbt, this.items, registries);
        nbt.put("Inventory", inventoryWrapperNbt);
        // --- End 2. Write Inventory ---
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        // --- 1. Read Core Data ---
        super.readCustomDataFromNbt(nbt);
        AdorableHamsterPets.LOGGER.info("[NBT Read {}] Start reading NBT data.", this.getId());
        this.setVariant(nbt.getInt("HamsterVariant"));
        boolean wasSitting = nbt.getBoolean("Sitting");
        this.setSitting(wasSitting, true); // Apply loaded sitting state


        this.setKnockedOut(nbt.getBoolean("KnockedOut"));
        this.throwCooldownEndTick = nbt.getLong("ThrowCooldownEnd");
        this.steamedBeansCooldownEndTick = nbt.getLong("SteamedBeansCooldownEnd");
        this.autoEatCooldownTicks = nbt.getInt("AutoEatCooldown");
        this.ejectionCheckCooldown = nbt.contains("EjectionCheckCooldown", NbtElement.INT_TYPE) ? nbt.getInt("EjectionCheckCooldown") : 20;
        // --- End 1. Read Core Data ---


        // --- 2. Read Inventory ---
        this.items.clear();
        RegistryWrapper.WrapperLookup registries = getRegistryLookup();
        if (nbt.contains("Inventory", NbtElement.COMPOUND_TYPE)) {
            Inventories.readNbt(nbt.getCompound("Inventory"), this.items, registries);
        }
        this.updateCheekTrackers();
        // --- End 2. Read Inventory ---

        // Log state after reading
        AdorableHamsterPets.LOGGER.info("[NBT Read {}] Finished NBT read. State from NBT: isSitting={}",
                this.getId(), this.dataTracker.get(IS_SITTING));
    }

    // --- Shoulder Riding Data Handling ---
    public HamsterShoulderData saveToShoulderData() {
        // --- 1. Save Shoulder Data ---
        AdorableHamsterPets.LOGGER.trace("[HamsterEntity {}] saveToShoulderData called", this.getId());
        this.updateCheekTrackers();


        // --- 1a. Save Inventory NBT ---
        NbtCompound inventoryNbt = new NbtCompound();
        RegistryWrapper.WrapperLookup registries = null;
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            registries = serverWorld.getRegistryManager();
            Inventories.writeNbt(inventoryNbt, this.items, registries);
            AdorableHamsterPets.LOGGER.trace("[HamsterEntity {}] Saved inventory NBT: {}", this.getId(), inventoryNbt);
        } else {
            AdorableHamsterPets.LOGGER.warn("[HamsterEntity {}] Cannot save inventory NBT on client side or in non-server world!", this.getId());
        }
        // --- End 1a. Save Inventory NBT ---


        // --- 1b. Save Active Status Effects ---
        NbtList effectsList = new NbtList();
        for (StatusEffectInstance effectInstance : this.getStatusEffects()) {
            NbtElement effectNbt = effectInstance.writeNbt();
            effectsList.add(effectNbt);
        }
        AdorableHamsterPets.LOGGER.trace("[HamsterEntity {}] Saved {} active effects to NBT list.", this.getId(), effectsList.size());
        // --- End 1b. Save Active Status Effects ---


        // --- 1c. Get Custom Name ---
        Optional<String> nameOptional = Optional.ofNullable(this.getCustomName()).map(Text::getString);
        AdorableHamsterPets.LOGGER.trace("[HamsterEntity {}] Custom name to save: {}", this.getId(), nameOptional.orElse("None"));
        // --- End 1c. Get Custom Name ---


        // --- 1d. Create data record (Updated Constructor) ---
        HamsterShoulderData data = new HamsterShoulderData(
                this.getVariant(),
                this.getHealth(),
                inventoryNbt,
                this.isLeftCheekFull(),
                this.isRightCheekFull(),
                this.getBreedingAge(),
                this.throwCooldownEndTick,
                this.steamedBeansCooldownEndTick,
                effectsList,
                this.autoEatCooldownTicks,
                nameOptional
        );
        AdorableHamsterPets.LOGGER.trace("[HamsterEntity {}] Returning shoulder data: {}", this.getId(), data);
        return data;
        // --- End 1d. Create data record ---
        // --- End 1. Save Shoulder Data ---
    }

    // --- Entity Behavior ---
    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) { return false; }

    @Override
    public void changeLookDirection(double cursorX, double cursorY) {
        if (this.isSleeping()) return;
        super.changeLookDirection(cursorX, cursorY);
    }

    @Override
    public void setSitting(boolean sitting) {
        // Calls the overload below. We want player-initiated sits to NOT play the sleep sound.
        // So, suppressSound should always be true when called from here.
        this.setSitting(sitting, true); // Always suppress sound for this basic toggle
    }

    // --- Overload for setSitting (NOW ONLY controls IS_SITTING) ---
    public void setSitting(boolean sitting, boolean suppressSound) {
        // --- 1. Set Sitting State ---
        this.dataTracker.set(IS_SITTING, sitting);

        // Ensure knocked out state is cleared if we are manually sitting/standing
        if (this.isKnockedOut()) {
            this.setKnockedOut(false);
        }
        this.setInSittingPose(sitting); // Vanilla flag used by SitGoal

        // --- Set Initial Cleaning Cooldown on Sit ---
        if (sitting) {
            this.cleaningCooldownTimer = 200; // Start 10-second cooldown when sitting starts
            this.cleaningTimer = 0; // Ensure cleaning doesn't start immediately if it was interrupted
        }
        // --- End Initial Cooldown ---

        // Sound logic removed previously
        // --- End 1. Set Sitting State ---
    }

    // --- Override isInAttackRange ---
    /**
     * Checks if the target entity is within the hamster's shorter melee attack range.
     * Overrides the default MobEntity check which uses a larger expansion.
     * @param entity The entity to check range against.
     * @return True if the entity is within the custom attack range, false otherwise.
     */
    @Override
    public boolean isInAttackRange(LivingEntity entity) {
        // --- Description: Calculate and check intersection with a smaller attack box ---
        // Get the hamster's current bounding box
        Box hamsterBox = this.getBoundingBox();
        // Expand it horizontally by the custom smaller amount
        Box attackBox = hamsterBox.expand(HAMSTER_ATTACK_BOX_EXPANSION, 0.0D, HAMSTER_ATTACK_BOX_EXPANSION);
        // Check if this smaller attack box intersects the target's hitbox
        boolean intersects = attackBox.intersects(entity.getBoundingBox());
        return intersects;
        // --- End Description ---
    }
    // --- End Override ---

    // --- Override canAttackWithOwner for Target Exclusions ---
    // Added this method to prevent the hamster from attacking specific entities
    // when commanded by the owner (via AttackWithOwnerGoal).
    @Override
    public boolean canAttackWithOwner(LivingEntity target, LivingEntity owner) {
        // --- 1. Check Target Exclusions ---
        if (!(target instanceof CreeperEntity) && !(target instanceof ArmorStandEntity)) {
            // Standard check: Don't attack owner or other pets owned by the same owner
            if (target == owner) return false;
            if (target == this) return false;
            // Check UUID just in case owner instance is different but represents the same player
            if (target instanceof PlayerEntity && target.getUuid().equals(owner.getUuid())) return false;
            // Check TameableEntity owner UUID
            if (target instanceof TameableEntity tameableTarget && tameableTarget.getOwnerUuid() != null && tameableTarget.getOwnerUuid().equals(owner.getUuid())) {
                // Don't attack other tameables owned by the same player (covers other hamsters)
                return false;
            }
            // --- Add Wolf Exclusion ---
            // Specifically check if the target is a WolfEntity owned by the same player.
            // --- End Wolf Exclusion ---
            if (target instanceof WolfEntity wolfTarget && wolfTarget.isTamed() && wolfTarget.getOwnerUuid() != null && wolfTarget.getOwnerUuid().equals(owner.getUuid())) {
                // Don't attack wolves owned by the same player
                return false;
            }
            return true; // Can attack other valid entities
        } else {
            // Don't attack creepers or armor stands when owner attacks them
            return false;
        }
        // --- End 1. Check Target Exclusions ---
    }
    // --- End Target Exclusion Override ---

    // --- Interaction Logic ---
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        // --- 1. Initial Setup ---
        ItemStack stack = player.getStackInHand(hand);
        World world = this.getWorld();
        AdorableHamsterPets.LOGGER.debug("[InteractMob {} Tick {}] Interaction start. Player: {}, Hand: {}, Item: {}", this.getId(), world.getTime(), player.getName().getString(), hand, stack.getItem());


        // --- 2. Knocked Out Check ---
        if (this.isKnockedOut()) {
            AdorableHamsterPets.LOGGER.debug("[InteractMob {} Tick {}] Hamster is knocked out. Waking up.", this.getId(), world.getTime());
            if (!world.isClient()) {
                SoundEvent wakeUpSound = getRandomSoundFrom(ModSounds.HAMSTER_WAKE_UP_SOUNDS, this.random);
                if (wakeUpSound != null) {
                    world.playSound(null, this.getBlockPos(), wakeUpSound, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                }
                this.setKnockedOut(false);
                this.triggerAnimOnServer("mainController", "wakeup");
            }
            return ActionResult.success(world.isClient());
        }
        // --- End 2. Knocked Out Check ---


        // --- 3. Interaction Cooldown Check ---
        if (this.interactionCooldown > 0) {
            AdorableHamsterPets.LOGGER.debug("[InteractMob {} Tick {}] Interaction cooldown active ({} ticks left). Passing.", this.getId(), world.getTime(), this.interactionCooldown);
            return ActionResult.PASS;
        }
        // --- End 3. Interaction Cooldown Check ---


        // --- 4. Taming Logic ---
        if (!this.isTamed()) {
            AdorableHamsterPets.LOGGER.debug("[InteractMob {} Tick {}] Hamster not tamed. Checking for taming attempt.", this.getId(), world.getTime());
            if (player.isSneaking() && stack.isOf(ModItems.SLICED_CUCUMBER)) {
                AdorableHamsterPets.LOGGER.debug("[InteractMob {} Tick {}] Taming attempt detected.", this.getId(), world.getTime());
                if (!world.isClient) { tryTame(player, stack); }
                return ActionResult.success(world.isClient());
            }
            AdorableHamsterPets.LOGGER.debug("[InteractMob {} Tick {}] Not a taming attempt. Calling super.interactMob for untamed.", this.getId(), world.getTime());
            return super.interactMob(player, hand);
        }
        // --- End 4. Taming Logic ---


        // --- 5. Owner Interaction Logic (Reordered) ---
        if (this.isOwner(player)) {
            AdorableHamsterPets.LOGGER.debug("[InteractMob {} Tick {}] Player is owner. Processing owner interactions.", this.getId(), world.getTime());
            boolean isSneaking = player.isSneaking();


            // --- 5a. Custom Owner Interactions (PRIORITIZED) ---


            // Inventory Access (Server-Side)
            if (!world.isClient() && isSneaking) {
                AdorableHamsterPets.LOGGER.debug("[InteractMob {} Tick {}] Owner sneaking. Opening inventory.", this.getId(), world.getTime());
                player.openHandledScreen(new HamsterEntityScreenHandlerFactory(this));
                return ActionResult.CONSUME;
            }


            // Feeding Logic (Server-Side, only if not sneaking)
            boolean isPotentialFood = isIsFood(stack) || stack.isOf(ModItems.STEAMED_GREEN_BEANS);
            if (!world.isClient() && !isSneaking && isPotentialFood) {
                AdorableHamsterPets.LOGGER.debug("[InteractMob {} Tick {}] Owner not sneaking, holding potential food. Checking refusal.", this.getId(), world.getTime());
                if (checkRepeatFoodRefusal(stack, player)) {
                    AdorableHamsterPets.LOGGER.debug("[InteractMob {} Tick {}] Food refused. Consuming interaction.", this.getId(), world.getTime());
                    return ActionResult.CONSUME; // Consume refusal action
                }


                AdorableHamsterPets.LOGGER.debug("[InteractMob {} Tick {}] Attempting feeding via tryFeedingAsTamed.", this.getId(), world.getTime());
                boolean feedingOccurred = tryFeedingAsTamed(player, stack); // Calls the method with detailed logging


                if (feedingOccurred) {
                    AdorableHamsterPets.LOGGER.debug("[InteractMob {} Tick {}] tryFeedingAsTamed returned true. Setting last food, decrementing stack.", this.getId(), world.getTime());
                    this.lastFoodItem = stack.copy(); // Track last food *only* if feeding was successful
                    if (!player.getAbilities().creativeMode) {
                        stack.decrement(1);
                    }
                    return ActionResult.CONSUME; // Consume successful feeding action
                } else {
                    // If tryFeedingAsTamed returned false (e.g., cooldown, full health+no breed),
                    // We might still want to allow vanilla interaction or sitting.
                    // Let's PASS for now to allow super.interactMob to run.
                    AdorableHamsterPets.LOGGER.debug("[InteractMob {} Tick {}] tryFeedingAsTamed returned false. Passing to vanilla/sitting.", this.getId(), world.getTime());
                }
            }
            // --- End 5a. Custom Owner Interactions ---


            // --- 5b. Vanilla Interaction Handling (AFTER custom checks) ---
            // If it wasn't sneaking (inventory) or a successful feeding action...
            // Let vanilla handle Name Tags, Leashes, etc.
            // Also handles the sit toggle if hand is empty or item wasn't food.
            if (!isSneaking && !isPotentialFood) { // Only call super if not sneaking AND not holding food we handle
                AdorableHamsterPets.LOGGER.debug("[InteractMob {} Tick {}] Not sneaking or holding handled food. Calling super.interactMob.", this.getId(), world.getTime());
                ActionResult vanillaResult = super.interactMob(player, hand);
                AdorableHamsterPets.LOGGER.debug("[InteractMob {} Tick {}] super.interactMob returned: {}", this.getId(), world.getTime(), vanillaResult);
                if (vanillaResult.isAccepted()) {
                    return vanillaResult; // Return if vanilla handled it (e.g., name tag)
                }
            }
            // --- End 5b. Vanilla Interaction Handling ---


            // --- 5c. Sitting Logic (Fallback if nothing else handled it) ---
            // This now acts as the default right-click action if not sneaking,
            // not feeding successfully, and vanilla didn't handle it.
            if (!world.isClient() && !isSneaking) {
                AdorableHamsterPets.LOGGER.debug("[InteractMob {} Tick {}] Fallback: Toggling sitting state.", this.getId(), world.getTime());
                this.setSitting(!this.dataTracker.get(IS_SITTING)); // Toggle sitting state
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
                return ActionResult.CONSUME_PARTIAL; // Indicate partial consumption for state toggle
            }
            // --- End 5c. Sitting Logic ---


            // Client-side success or fallback pass for owner
            AdorableHamsterPets.LOGGER.debug("[InteractMob {} Tick {}] Reached end of owner logic. Returning client-side success/pass.", this.getId(), world.getTime());
            return ActionResult.success(world.isClient());


        } else {
            // Interaction by a non-owner on a tamed hamster. Let vanilla handle it.
            AdorableHamsterPets.LOGGER.debug("[InteractMob {} Tick {}] Player is not owner. Calling super.interactMob.", this.getId(), world.getTime());
            return super.interactMob(player, hand);
        }
        // --- End 5. Owner Interaction Logic ---
    }

    // --- Taming Override ---
    @Override
    public void setTamed(boolean tamed, boolean updateAttributes) {
        // --- 1. Set Tamed State and Attributes ---
        super.setTamed(tamed, updateAttributes);
        if (tamed) {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(16.0D);
            this.setHealth(this.getMaxHealth()); // Set health to the updated maximum
            // --- Ensure Attack Damage is set correctly on tame ---
            // Set the base attack damage attribute to the defined melee damage when tamed.
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(AdorableHamsterPets.CONFIG.behavior.hamsterMeleeDamage()); // Use config
            // --- End Attack Damage ---
        } else {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(8.0D);
            // Reset attack damage if untamed (optional, but good practice)
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(AdorableHamsterPets.CONFIG.behavior.hamsterMeleeDamage()); // Use config
        }
        // --- End 1. Set Tamed State and Attributes ---
    }

    // --- Breeding ---
    public boolean isInCustomLove() { return this.customLoveTimer > 0; }
    public void setCustomInLove(PlayerEntity player) {
        this.customLoveTimer = CUSTOM_LOVE_TICKS;
        if (!this.getWorld().isClient) { this.getWorld().sendEntityStatus(this, (byte) 18); }
    }

    @Override
    public void setBaby(boolean baby) {
        this.setBreedingAge(baby ? -24000 : 0); // Vanilla logic for setting age based on baby status
    }

    @Nullable @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity mate) {
        // --- 1. Create Child Logic ---
        HamsterEntity baby = ModEntities.HAMSTER.create(world);
        if (baby != null && mate instanceof HamsterEntity parent2) { // Ensure mate is a HamsterEntity
            UUID ownerUUID = this.getOwnerUuid(); // Inherit owner from the 'this' parent
            if (ownerUUID != null) {
                baby.setOwnerUuid(ownerUUID);
                baby.setTamed(true, true); // Ensure baby is tamed if parents are
            }

            // --- 1a. Variant Logic ---
            // 1. Get the variants of both parents
            HamsterVariant parent1Variant = HamsterVariant.byId(this.getVariant());
            HamsterVariant parent2Variant = HamsterVariant.byId(parent2.getVariant());

            // 2. Randomly choose one parent's variant to determine the base color pool
            HamsterVariant chosenParentVariant = this.random.nextBoolean() ? parent1Variant : parent2Variant;

            // 3. Get the base variant of the chosen parent
            HamsterVariant baseVariant = chosenParentVariant.getBaseVariant();

            // 4. Get the appropriate variant pool for that base variant
            List<HamsterVariant> variantPool = getPoolForBaseVariant(baseVariant);

            // 5. Select a random variant from that pool for the baby
            HamsterVariant babyVariant = getRandomVariant(variantPool, this.random);

            // 6. Set the baby's variant
            baby.setVariant(babyVariant.getId());
            // --- End 1a. New Variant Logic ---

            baby.setBaby(true); // Mark the entity as a baby
        } else if (baby != null) {
            // Handle case where mate is not a HamsterEntity (shouldn't happen with vanilla breeding mechanics)
            // Assign a random variant as a fallback
            int randomVariantId = this.random.nextInt(HamsterVariant.values().length);
            baby.setVariant(randomVariantId);
            baby.setBaby(true);
            AdorableHamsterPets.LOGGER.trace("Hamster breeding attempted with non-hamster mate? Assigning random variant to baby.");
        }
        return baby;
        // --- End 1. Create Child Logic ---
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) { return isIsFood(stack); } // Use helper

    // --- Tick Logic ---
    @Override
    public void tick() {

        // Log first few ticks (keep for debugging)
        if (!this.getWorld().isClient() && this.age < 5) {
            AdorableHamsterPets.LOGGER.info("[Tick {} Age {}] State: isSleeping={}, isSittingPose={}, Navigating={}",
                    this.getId(), this.age, this.isSleeping(), this.isInSittingPose(), !this.getNavigation().isIdle());
        }



        // --- 1. Decrement Timers ---
        if (this.interactionCooldown > 0) this.interactionCooldown--;
        if (this.cleaningCooldownTimer > 0) this.cleaningCooldownTimer--;
        if (this.cleaningTimer > 0) {
            this.cleaningTimer--;
            if (this.cleaningTimer == 0) this.cleaningCooldownTimer = 200;
        }
        if (this.wakingUpTicks > 0) this.wakingUpTicks--;
        if (this.autoEatCooldownTicks > 0) this.autoEatCooldownTicks--;
        if (this.autoEatProgressTicks > 0) this.autoEatProgressTicks--;
        if (this.ejectionCheckCooldown > 0) this.ejectionCheckCooldown--;
        // --- End 1. Decrement Timers ---

        // --- 2. Blink Timer Logic ---
        if (this.blinkTimer > 0) {
            this.blinkTimer--; // Decrement active blink timer
        } else if (!this.isSleeping()) { // Only check for next blink if not sleeping and not already blinking
            if (this.nextBlinkCheckTick > 0) {
                this.nextBlinkCheckTick--; // Decrement check timer
            } else {
                // Time to check for a blink
                this.nextBlinkCheckTick = this.random.nextBetween(60, 100); // Reset check timer (3-5 seconds)

                if (this.random.nextInt(3) == 0) { // 1 in 3 chance to blink at all
                    // --- Probability ---
                    // nextInt(4) gives 0, 1, 2, or 3. Only trigger double blink on 0 (25% chance).
                    if (this.random.nextInt(4) == 0) { // 25% chance for double blink
                        this.blinkTimer = 6; // Double blink duration
                    } else { // 75% chance for single blink
                        this.blinkTimer = 2;  // Single blink duration
                    }
                }
            }
        }
        // --- End 2. Blink Timer Logic ---

        // --- 3. Thrown State Logic ---
        if (this.isThrown()) {
            this.throwTicks++; // Increment throw timer

            Vec3d currentPos = this.getPos();
            Vec3d currentVel = this.getVelocity();
            Vec3d nextPos = currentPos.add(currentVel);
            World world = this.getWorld();

            HitResult blockHit = world.raycast(new RaycastContext(currentPos, nextPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));

            boolean stopped = false;

            if (blockHit.getType() == HitResult.Type.BLOCK) {
                // --- 3a. Block Collision Handling ---
                Vec3d hitPos = blockHit.getPos();
                Vec3d pushback = currentVel.normalize().multiply(-0.1).add(0, 0.1, 0);
                this.setPosition(currentPos.add(pushback));
                AdorableHamsterPets.LOGGER.debug("[HamsterTick] Hit block, applying pushback: {}", pushback);

                this.setVelocity(currentVel.multiply(0.6, 0.0, 0.6));
                this.setThrown(false);
                this.playSound(SoundEvents.ENTITY_GENERIC_SMALL_FALL, 0.4f, 1.5f);
                this.setKnockedOut(true);
                // --- Trigger Crash Animation ---
                if (!world.isClient()) {
                    this.triggerAnimOnServer("mainController", "crash");
                }
                // --- End Trigger ---
                stopped = true;
                // --- End 3a. Block Collision Handling ---

            } else {
                EntityHitResult entityHit = ProjectileUtil.getEntityCollision(world, this, currentPos, nextPos, this.getBoundingBox().stretch(currentVel).expand(1.0), this::canHitEntity);

                if (entityHit != null && entityHit.getEntity() != null) {
                    // --- 3b. Entity Collision Handling ---
                    Entity hitEntity = entityHit.getEntity();
                    boolean playEffects = false;

                    if (hitEntity instanceof ArmorStandEntity) {
                        AdorableHamsterPets.LOGGER.debug("Hamster hit Armor Stand.");
                        playEffects = true;
                    } else if (hitEntity instanceof LivingEntity livingHit) {
                        // --- Use Config Value for Throw Damage ---
                        boolean damaged = livingHit.damage(this.getDamageSources().thrown(this, this.getOwner()), AdorableHamsterPets.CONFIG.features.hamsterThrowDamage());
                        if (damaged) {
                            int nauseaDuration = 20;
                            livingHit.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, nauseaDuration, 0, false, false, false));
                            playEffects = true;
                        }
                    } else {
                        playEffects = true;
                    }

                    if (playEffects) {
                        world.playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.HAMSTER_IMPACT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                        if (!world.isClient()) {
                            ((ServerWorld)world).spawnParticles(
                                    ParticleTypes.POOF,
                                    this.getX(), this.getY() + this.getHeight() / 2.0, this.getZ(),
                                    50, 0.4, 0.4, 0.4, 0.1
                            );
                            AdorableHamsterPets.LOGGER.debug("Spawned POOF particles at impact.");
                        }
                    }

                    this.setVelocity(currentVel.multiply(0.1, 0.1, 0.1));
                    this.setThrown(false);
                    this.setKnockedOut(true);
                    // --- Trigger Crash Animation ---
                    if (!world.isClient()) {
                        this.triggerAnimOnServer("mainController", "crash");
                    }
                    // --- End Trigger ---
                    stopped = true;
                    // --- End 3b. Entity Collision Handling ---
                }
            }

            // Apply gravity, update position, and spawn trail particles if still thrown
            if (this.isThrown() && !stopped) {
                if (!this.hasNoGravity()) {
                    this.setVelocity(this.getVelocity().add(0.0, THROWN_GRAVITY, 0.0));
                }

                Vec3d currentVelocity = this.getVelocity();
                if (Double.isNaN(currentVelocity.x) || Double.isNaN(currentVelocity.y) || Double.isNaN(currentVelocity.z)) {
                    this.setVelocity(Vec3d.ZERO);
                    this.setThrown(false);
                    AdorableHamsterPets.LOGGER.warn("Hamster velocity became NaN, resetting and stopping throw.");
                } else {
                    this.setPosition(this.getX() + currentVelocity.x, this.getY() + currentVelocity.y, this.getZ() + currentVelocity.z);
                    this.velocityDirty = true;

                    if (!world.isClient() && this.throwTicks > 5) {
                        ((ServerWorld)world).spawnParticles(
                                ParticleTypes.GUST,
                                this.getX(), this.getY() + this.getHeight() / 2.0, this.getZ(),
                                1, 0.1, 0.1, 0.1, 0.0
                        );
                    }
                }
            } else {
                if (this.throwTicks != 0) {
                    this.throwTicks = 0;
                }
            }
        }
        // --- End 3. Thrown State Logic ---

        // Call super.tick() *after* processing thrown state and timers
        super.tick();

        // --- 4. Server-Side Logic ---
        World world = this.getWorld();
        if (!world.isClient()) {

            // --- 4a. Ejection Logic ---
            if (this.ejectionCheckCooldown <= 0) {
                this.ejectionCheckCooldown = 100; // Reset cooldown (check every 5 seconds)
                boolean ejectedItem = false; // Flag to only eject one item per cycle

                for (int i = 0; i < this.items.size(); ++i) {
                    ItemStack stack = this.items.get(i);
                    if (!stack.isEmpty() && this.isItemDisallowed(stack)) {
                        AdorableHamsterPets.LOGGER.warn("[HamsterTick {}] Ejecting disallowed item {} from slot {}.", this.getId(), stack.getItem(), i);
                        // Drop the item at the hamster's feet
                        ItemScatterer.spawn(world, this.getX(), this.getY(), this.getZ(), stack.copy());
                        // Remove it from the inventory
                        this.items.set(i, ItemStack.EMPTY);
                        // Mark dirty and update visuals
                        this.markDirty(); // This calls updateCheekTrackers
                        ejectedItem = true;
                        break; // Eject only one item per check cycle
                    }
                }
            }
            // --- End 4a. Ejection Logic ---

            // --- 4b. Auto Eating Logic ---
            if (this.isAutoEating && this.autoEatProgressTicks == 0) {
                this.heal(AdorableHamsterPets.CONFIG.behavior.hamsterFoodMixHealing()); // Use config healing
                // --- Auto Eating Cooldown ---
                this.autoEatCooldownTicks = 20; // Short cooldown after finishing
                this.isAutoEating = false;
            }
            // --- End 4b. Auto Eating Logic ---

            // --- 4c. Start Eating Action ---
            // Check conditions: tamed, injured, not already eating, cooldown finished, not thrown/KO'd
            if (this.isTamed() && this.getHealth() < this.getMaxHealth() &&
                    !this.isAutoEating && this.autoEatCooldownTicks == 0 &&
                    !this.isThrown() && !this.isKnockedOut())
            {
                // Check inventory for HAMSTER_FOOD_MIX
                for (int i = 0; i < this.items.size(); ++i) {
                    ItemStack stack = this.items.get(i);
                    // Use the restricted AUTO_HEAL_FOODS set
                    if (!stack.isEmpty() && AUTO_HEAL_FOODS.contains(stack.getItem())) {
                        // Found eligible food - Start eating process
                        AdorableHamsterPets.LOGGER.trace("[HamsterTick {}] Starting auto-eat on {} from slot {}", this.getId(), stack.getItem(), i);

                        // Set eating state and duration
                        this.isAutoEating = true;
                        this.autoEatProgressTicks = 60; // 3 seconds eating time

                        // Play eating sound immediately
                        this.playSound(
                                SoundEvents.ENTITY_GENERIC_EAT, // The sound event to play
                                0.5F, // 50% volume
                                1.1F  // 110% pitch
                        );

                        // Spawn eating particles immediately
                        if (world instanceof ServerWorld serverWorld) {
                            serverWorld.spawnParticles(
                                    new ItemStackParticleEffect(ParticleTypes.ITEM, stack.split(1)), // Use split(1) to get a single item for particles
                                    this.getX() + this.random.nextGaussian() * 0.1,
                                    this.getY() + this.getHeight() / 2.0 + this.random.nextGaussian() * 0.1,
                                    this.getZ() + this.random.nextGaussian() * 0.1,
                                    5, 0.1, 0.1, 0.1, 0.02
                            );
                        }
                        // Note: stack is now one less due to split(1)

                        // If the original stack is now empty after split(1), clear the slot
                        if (stack.isEmpty()) {
                            this.items.set(i, ItemStack.EMPTY);
                        }

                        // Update cheek visibility trackers
                        this.updateCheekTrackers();

                        // Stop searching after starting to eat one item
                        break;
                    }
                }
            }
            // --- End 4c. Start Eating Action ---
        }
        // --- End 4. Server-Side Logic ---

        // --- 5. Client-Side Buff Particle Logic ---
        if (world.isClient) {
            // Check if the hamster has one of the buff effects (e.g., Strength)
            if (this.hasStatusEffect(StatusEffects.STRENGTH)) {
                // Only spawn particles occasionally to avoid clutter
                if (this.random.nextInt(5) == 0) { // Spawn roughly every 1/4 second
                    // Spawn standard entity effect particles randomly around the hamster
                    for (int i = 0; i < 2; ++i) { // Spawn a couple particles each time
                        world.addParticle((ParticleEffect)ParticleTypes.ENTITY_EFFECT, // Cast ParticleType to ParticleEffect
                                this.getParticleX(0.6), // Get random X within bounds
                                this.getRandomBodyY(),     // Get random Y on the body
                                this.getParticleZ(0.6), // Get random Z within bounds
                                this.random.nextGaussian() * 0.02, // dx (slight random motion)
                                this.random.nextGaussian() * 0.02, // dy
                                this.random.nextGaussian() * 0.02  // dz
                        );
                    }
                }
            }
        }
        // --- End 5. Client-Side Buff Particle Logic ---

        // --- 6. Other Non-Movement Tick Logic ---
        if (this.isRefusingFood() && refuseTimer > 0) { if (--refuseTimer <= 0) this.setRefusingFood(false); }
        if (tamingCooldown > 0) tamingCooldown--;
        if (customLoveTimer > 0) customLoveTimer--;
        if (customLoveTimer <= 0 && this.isInLove()) this.setInLove(false);
        // --- End 6. Other Non-Movement Tick Logic ---
    }

    @Override public boolean canMoveVoluntarily() { return super.canMoveVoluntarily() && !this.isThrown(); }
    @Override public boolean isPushable() { return super.isPushable() && !this.isThrown(); }

    // --- Override onDeath to Drop Inventory ---
    @Override
    public void onDeath(DamageSource source) {
        // --- 1. Drop Cheek Pouch Inventory ---
        World world = this.getWorld(); // Get the world instance
        if (!world.isClient()) {
            // Iterate through the items list and drop each non-empty stack
            for (ItemStack stack : this.items) {
                if (!stack.isEmpty()) {
                    // Use ItemScatterer to drop the stack at the hamster's position
                    ItemScatterer.spawn(world, this.getX(), this.getY(), this.getZ(), stack);
                }
            }
            // Clear the internal list after dropping
            this.items.clear();
            // Update cheek trackers one last time
            this.updateCheekTrackers();
        }
        // --- End 1. Drop Cheek Pouch Inventory ---

        // Call the superclass method AFTER dropping items
        super.onDeath(source);
    }
    // --- End Override ---

    // --- Animation ---
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "mainController", 5, event -> {
                    // --- 1. Animation State Machine ---
                    // Determine the primary looping animation based on the hamster's current state.
                    // Triggerable animations (attack, crash, wakeup, no) will override this base state when fired.

                    // --- 1a. Knocked Out State (Highest Priority Loop) ---
                    if (this.isKnockedOut()) {
                        return event.setAndContinue(KNOCKED_OUT_ANIM); // Loop KO animation
                    }

                    // --- 1b. Thrown State ---
                    if (this.isThrown()) {
                        return event.setAndContinue(FLYING_ANIM); // Loop flying animation
                    }

                    // --- 1c. Sleeping State ---
                    if (this.isSleeping()) {
                        return event.setAndContinue(SLEEPING_ANIM); // Loop sleeping animation
                    }

                    // --- 1d. Sitting State (Includes potential cleaning) ---
                    if (this.dataTracker.get(IS_SITTING)) { // Check raw sitting tracker
                        if (this.cleaningTimer > 0) {
                            return event.setAndContinue(CLEANING_ANIM); // Loop cleaning animation if timer active
                        } else {
                            // Check if cooldown is over and randomly start cleaning
                            // MODIFIED: Changed 600 to 1200 for less frequent cleaning checks (Preserved from original)
                            if (this.cleaningCooldownTimer <= 0 && this.random.nextInt(1200) == 0) {
                                this.cleaningTimer = this.random.nextBetween(30, 60); // Start cleaning timer
                                // Fall through to SITTING_ANIM this tick; CLEANING_ANIM will start next tick
                            }
                            return event.setAndContinue(SITTING_ANIM); // Default to sitting animation loop
                        }
                    }

                    // --- 1e. Movement State (Walking/Running) ---
                    double horizontalSpeedSquared = this.getVelocity().horizontalLengthSquared();
                    double runThresholdSquared = 0.002; // Threshold to differentiate walk/run
                    if (horizontalSpeedSquared > 1.0E-6) { // Check if moving significantly
                        RawAnimation targetMoveAnim = horizontalSpeedSquared > runThresholdSquared ? RUNNING_ANIM : WALKING_ANIM;
                        return event.setAndContinue(targetMoveAnim); // Loop appropriate movement animation
                    }

                    // --- 1f. Begging State ---
                    if (this.isBegging()) {
                        return event.setAndContinue(BEGGING_ANIM); // Loop begging animation
                    }

                    // --- 1g. Default Idle State (Lowest Priority Loop) ---
                    return event.setAndContinue(IDLE_ANIM); // Loop idle animation

                    // --- End 1. Animation State Machine ---

                })
                        // --- 2. Register Triggerable Animations ---
                        // These animations play once when triggered via triggerAnimOnServer()
                        .triggerableAnim("crash", CRASH_ANIM)
                        .triggerableAnim("wakeup", WAKE_UP_ANIM)
                        .triggerableAnim("no", NO_ANIM)
                        .triggerableAnim("attack", ATTACK_ANIM)
                        // --- End 2. Register Triggerable Animations ---

                        // --- 3. Particle Keyframe Handler ---
                        // Handles events defined in the animation JSON (e.g., spawning particles at a specific time/bone)
                        .setParticleKeyframeHandler(event -> {

                            // --- 3a. Log Initial Trigger ---
                            final int currentEntityId = this.getId();
                            AdorableHamsterPets.LOGGER.debug("[ParticleHandler {} Tick {}] Particle keyframe handler triggered.", currentEntityId, this.getWorld().getTime());

                            // --- 3b. Extract Keyframe Data ---
                            String effect = event.getKeyframeData().getEffect();
                            String locator = event.getKeyframeData().getLocator(); // Get the locator string from the keyframe
                            AdorableHamsterPets.LOGGER.debug("[ParticleHandler {}] Received effect string: '{}', Locator: '{}'", this.getId(), effect, locator);

                            // --- 3c. Handle Specific Effect ("attack_poof") ---
                            if ("attack_poof".equals(effect)) {
                                AdorableHamsterPets.LOGGER.debug("[ParticleHandler {}] Effect string matches 'attack_poof'.", this.getId());
                                World world = this.getWorld();

                                // Particle spawning needs bone position, which is calculated client-side during rendering
                                AdorableHamsterPets.LOGGER.debug("[ParticleHandler {}] Checking world side. Is client? {}", this.getId(), world.isClient());
                                if (world.isClient()) {
                                    // --- 3c.i. Validate Locator ---
                                    if (locator == null || locator.isEmpty()) {
                                        AdorableHamsterPets.LOGGER.debug("[ParticleHandler {}] Locator is null or empty for effect '{}'. Cannot calculate bone position.", this.getId(), effect);
                                        return;
                                    }

                                    // --- 3c.ii. Get Renderer and Model ---
                                    EntityRenderer<?> renderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(this);
                                    if (!(renderer instanceof GeoEntityRenderer<?> geoRenderer)) {
                                        AdorableHamsterPets.LOGGER.debug("[ParticleHandler {}] Could not get GeoEntityRenderer instance for entity.", this.getId());
                                        return;
                                    }
                                    GeoModel<?> model = geoRenderer.getGeoModel();
                                    if (model == null) {
                                        AdorableHamsterPets.LOGGER.debug("[ParticleHandler {}] Could not get model from GeoRenderer.", this.getId());
                                        return;
                                    }

                                    // --- 3c.iii. Find Bone and Get Position ---
                                    GeoBone bone = model.getBone(locator).orElse(null);
                                    if (bone != null) {
                                        Vector3d boneWorldPos = bone.getWorldPosition();
                                        double boneX = boneWorldPos.x();
                                        double boneY = boneWorldPos.y();
                                        double boneZ = boneWorldPos.z();

                                        // --- 3c.iv. Log and Send Packet ---
                                        // --- (Temporarily disabled until we hear back from the Geckolib community) ---
                                        // TODO: Re-enable attack particle effect when Geckolib multi-entity keyframe issue is resolved.
                                        // ClientPlayNetworking.send(new SpawnAttackParticlesPayload(boneX, boneY, boneZ));
                                        // AdorableHamsterPets.LOGGER.info("[ParticleHandler {}] Found bone '{}'. Calculated Pos: ({}, {}, {}). PACKET SENDING DISABLED.", currentEntityId, locator, boneX, boneY, boneZ); // Optional: Update log

                                    } else {
                                        AdorableHamsterPets.LOGGER.debug("[ParticleHandler {}] Could not find bone with locator '{}' for effect '{}'.", this.getId(), locator, effect);
                                    }
                                }
                            }
                        })
                // --- End 3. Particle Keyframe Handler ---
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    // --- Helper method to trigger animations server-side ---
    // Needs to be called from server-side logic (tick, interactMob, goals)
    public void triggerAnimOnServer(String controllerName, String animName) {
        if (!this.getWorld().isClient()) { // Ensure we're on the server
            ServerWorld serverWorld = (ServerWorld) this.getWorld();
            // Use the GeoAnimatable's built-in method for triggering server-side
            this.triggerAnim(controllerName, animName);
            // The library handles the synchronization to clients automatically.
            AdorableHamsterPets.LOGGER.trace("[HamsterEntity {}] Triggered server-side animation: Controller='{}', Anim='{}'", this.getId(), controllerName, animName);
        }
    }



    /* ──────────────────────────────────────────────────────────────────────────────
     *                           5. Protected Methods
     * ────────────────────────────────────────────────────────────────────────────*/

    // --- Data Tracker Initialization ---
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(VARIANT, 0);
        builder.add(IS_SLEEPING, false);
        builder.add(IS_SITTING, false);
        builder.add(IS_BEGGING, false);
        builder.add(IS_IN_LOVE, false);
        builder.add(IS_REFUSING_FOOD, false);
        builder.add(IS_THROWN, false);
        builder.add(LEFT_CHEEK_FULL, false);
        builder.add(RIGHT_CHEEK_FULL, false);
        builder.add(IS_KNOCKED_OUT, false);
    }

    // --- AI Goals ---
    @Override
    protected void initGoals() {
        AdorableHamsterPets.LOGGER.info("[AI Init {} Tick {}] Initializing goals. Current State: isSleeping={}, isSittingPose={}",
                this.getId(), this.getWorld().isClient ? "ClientTick?" : this.getWorld().getTime(), this.isSleeping(), this.isInSittingPose());
        // --- 1. Initialize Goals ---
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new HamsterMeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.add(2, new HamsterMateGoal(this, 1.0D));
        this.goalSelector.add(3, new FollowOwnerGoal(this, 1.2D, 4.0F, 16.0F));
        this.goalSelector.add(4, new HamsterFleeGoal<>(this, LivingEntity.class, 8.0F, 1.0D, 1.5D));
        this.goalSelector.add(5, new HamsterTemptGoal(this, 1.4D, stack -> stack.isOf(ModItems.SLICED_CUCUMBER), false));
        this.goalSelector.add(6, new HamsterBegGoal(this));
        this.goalSelector.add(7, new SitGoal(this)); // Vanilla SitGoal uses TameableEntity.isInSittingPose()
        this.goalSelector.add(8, new HamsterSleepGoal(this));
        this.goalSelector.add(9, new WanderAroundFarGoal(this, 0.75D));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(11, new LookAroundGoal(this));

        // --- Target Selector Goals ---
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new RevengeGoal(this).setGroupRevenge());
        // --- End Target Selector Goals ---
        // --- End 1. Initialize Goals ---
        AdorableHamsterPets.LOGGER.info("[AI Init {} Tick {}] Finished initializing goals.",
                this.getId(), this.getWorld().isClient ? "ClientTick?" : this.getWorld().getTime());
    }

    // --- Sounds ---
    @Override protected SoundEvent getAmbientSound() {
        if (this.isBegging()) return getRandomSoundFrom(ModSounds.HAMSTER_BEG_SOUNDS, this.random);
        if (this.isSitting()) return getRandomSoundFrom(ModSounds.HAMSTER_SLEEP_SOUNDS, this.random);
        return getRandomSoundFrom(ModSounds.HAMSTER_IDLE_SOUNDS, this.random);
    }
    @Override protected SoundEvent getHurtSound(DamageSource source) { return getRandomSoundFrom(ModSounds.HAMSTER_HURT_SOUNDS, this.random); }
    @Override protected SoundEvent getDeathSound() { return getRandomSoundFrom(ModSounds.HAMSTER_DEATH_SOUNDS, this.random); }
    @Override protected void playStepSound(BlockPos pos, BlockState state) {
        try {
            // Directly get the sound group from the block state
            BlockSoundGroup group = state.getSoundGroup();
            // Play the step sound using the obtained group
            this.playSound(group.getStepSound(), 0.5F, 1.2F);
        } catch (Exception ex) {
            AdorableHamsterPets.LOGGER.debug("Error obtaining block sound group for footstep (direct call failed)", ex);
            this.playSound(SoundEvents.BLOCK_GRASS_STEP, 0.5F, 1.2F);
        }
    }


    protected boolean canHitEntity(Entity entity) {
        // --- 1. Check if Entity Can Be Hit ---
        // Allow hitting armor stands specifically
        if (entity instanceof net.minecraft.entity.decoration.ArmorStandEntity) {
            return !entity.isSpectator(); // Can hit non-spectator armor stands
        }

        // Original logic for other entities
        if (!entity.isSpectator() && entity.isAlive() && entity.canHit()) {
            Entity owner = this.getOwner();
            // Prevent hitting self or owner or entities owner is riding
            return entity != this && (owner == null || !owner.isConnectedThroughVehicle(entity));
        }
        return false;
        // --- End 1. Check if Entity Can Be Hit ---
    }

    @Override protected void applyGravity() { if (this.isThrown() && this.hasNoGravity()) return; super.applyGravity(); }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        // --- 1. Initialize Variant ---
        // Apply biome variants for natural spawns, spawn eggs, AND chunk generation
        // MODIFIED Condition: Added CHUNK_GENERATION
        if (spawnReason == SpawnReason.NATURAL || spawnReason == SpawnReason.SPAWN_EGG || spawnReason == SpawnReason.CHUNK_GENERATION) {
            RegistryEntry<Biome> biomeEntry = world.getBiome(this.getBlockPos());
            String biomeKeyStr = biomeEntry.getKey().map(key -> key.getValue().toString()).orElse("UNKNOWN");
            AdorableHamsterPets.LOGGER.trace("[HamsterInit] SpawnReason: {}, BiomeKey: {}", spawnReason, biomeKeyStr); // Keep this log

            HamsterVariant chosenVariant = determineVariantForBiome(biomeEntry, this.random);
            this.setVariant(chosenVariant.getId());
            AdorableHamsterPets.LOGGER.trace("[HamsterInit] Assigned variant: {}", chosenVariant.name());

        } else {
            // Fallback for other spawns (command, breeding, structure, etc.)
            int randomVariantId = this.random.nextInt(HamsterVariant.values().length);
            this.setVariant(randomVariantId);
            AdorableHamsterPets.LOGGER.trace("[HamsterInit] SpawnReason: {}, Assigned random variant: {}",
                    spawnReason, HamsterVariant.byId(randomVariantId).name());
        }
        // --- End 1. Initialize Variant ---

        // Always update cheek trackers on initialization
        this.updateCheekTrackers();

        // Call and return the super method's result
        return super.initialize(world, difficulty, spawnReason, entityData);
    }



    /* ──────────────────────────────────────────────────────────────────────────────
     *                       6. Private Helper Methods
     * ────────────────────────────────────────────────────────────────────────────*/

    /**
     * Checks if the given item stack is disallowed in the hamster's inventory based on a disallow list.
     * @param stack The ItemStack to check.
     * @return True if the item is explicitly disallowed, false otherwise.
     */
    public boolean isItemDisallowed(ItemStack stack) {
        if (stack.isEmpty()) return false;

        Item item = stack.getItem();

        // --- 1. Explicit item + tag bans ---
        if (DISALLOWED_ITEMS.contains(item)) return true;
        for (TagKey<Item> tag : DISALLOWED_ITEM_TAGS)
            if (stack.isIn(tag)) return true;

        // --- 2. Global block‑size rule ---
        if (item instanceof BlockItem) {
            // Any block not on the tiny‑block whitelist is too big
            return !stack.isIn(ModItemTags.ALLOWED_POUCH_BLOCKS);
        }

        // --- 3. Spawn eggs ---
        return item instanceof SpawnEggItem;
    }

    private RegistryWrapper.WrapperLookup getRegistryLookup() {
        return this.getWorld().getRegistryManager();
    }

    private boolean tryTame(PlayerEntity player, ItemStack itemStack) {
        // --- 1. Taming Attempt ---
        if (!player.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        // --- Use Config Value for Taming Chance ---
        final ModConfig config = AdorableHamsterPets.CONFIG;
        int denominator = Math.max(1, config.behavior.tamingChanceDenominator()); // Ensure denominator is at least 1
        if (this.random.nextInt(denominator) == 0) {
            // --- End Use Config Value ---
            this.setOwnerUuid(player.getUuid());
            this.setTamed(true, true);
            this.navigation.stop();
            this.setSitting(false);
            this.setSleeping(false);
            this.setTarget(null);
            this.getWorld().sendEntityStatus(this, (byte) 7);

            // Play celebrate sound only on success
            SoundEvent celebrateSound = getRandomSoundFrom(HAMSTER_CELEBRATE_SOUNDS, this.random);
            this.getWorld().playSound(null, this.getBlockPos(), celebrateSound, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            if (player instanceof ServerPlayerEntity serverPlayer) {
                Criteria.TAME_ANIMAL.trigger(serverPlayer, this);
            }

            return true;
        } else {
            this.getWorld().sendEntityStatus(this, (byte) 6);
            return false;
        }
        // --- End 1. Taming Attempt ---
    }

    // --- Check for Repeatable Foods ---
    private boolean checkRepeatFoodRefusal(ItemStack currentStack, PlayerEntity player) {
        // --- 1. Check Repeat Food Refusal ---
        if (REPEATABLE_FOODS.contains(currentStack.getItem())) return false;
        if (!this.lastFoodItem.isEmpty() && ItemStack.areItemsEqual(this.lastFoodItem, currentStack)) {
            this.setRefusingFood(true);
            this.refuseTimer = REFUSE_FOOD_TIMER_TICKS;
            player.sendMessage(Text.literal("Hamster wants to try something different."), true);
            // --- Trigger Refusal Animation ---
            if (!this.getWorld().isClient()) {
                this.triggerAnimOnServer("mainController", "no");
            }
            // --- End Trigger ---
            return true;
        }
        return false;
        // --- End 1. Check Repeat Food Refusal ---
    }
    // --- End Check for Repeatable Foods ---

    /**
     * Attempts to feed the hamster when interacted with by its owner.
     * Handles healing, breeding initiation, and buff application.
     *
     * @param player The player feeding the hamster.
     * @param stack  The ItemStack being used for feeding.
     * @return True if the feeding action (healing, breeding, buff) was successfully processed, false otherwise.
     */
    private boolean tryFeedingAsTamed(PlayerEntity player, ItemStack stack) {
        // --- 1. Initial Setup & Logging ---
        boolean isFood = isIsFood(stack);
        boolean isBuffItem = stack.isOf(ModItems.STEAMED_GREEN_BEANS);
        boolean canHeal = this.getHealth() < this.getMaxHealth();
        boolean readyToBreed = this.getBreedingAge() == 0 && !this.isInCustomLove(); // Check custom love timer
        World world = this.getWorld();
        final ModConfig config = AdorableHamsterPets.CONFIG;
        boolean actionTaken = false; // Initialize return value


        AdorableHamsterPets.LOGGER.debug("[FeedAttempt {} Tick {}] Entering tryFeedingAsTamed. Item: {}, isFood={}, isBuff={}, canHeal={}, breedingAge={}, isInCustomLove={}, readyToBreed={}",
                this.getId(), world.getTime(), stack.getItem(), isFood, isBuffItem, canHeal, this.getBreedingAge(), this.isInCustomLove(), readyToBreed);


        if (!isFood && !isBuffItem) {
            AdorableHamsterPets.LOGGER.debug("[FeedAttempt {} Tick {}] Item is not valid food or buff item. Returning false.", this.getId(), world.getTime());
            return false; // Not a valid item for feeding
        }
        // --- End 1. Initial Setup & Logging ---


        // --- 2. Steamed Green Beans Logic ---
        if (isBuffItem) {
            long currentTime = world.getTime();
            if (this.steamedBeansCooldownEndTick > currentTime) {
                // Still on cooldown
                long remainingTicks = this.steamedBeansCooldownEndTick - currentTime;
                long totalSecondsRemaining = remainingTicks / 20;
                long minutes = totalSecondsRemaining / 60;
                long seconds = totalSecondsRemaining % 60;
                player.sendMessage(Text.translatable("message.adorablehamsterpets.beans_cooldown", minutes, seconds).formatted(Formatting.RED), true);
                AdorableHamsterPets.LOGGER.debug("[FeedAttempt {} Tick {}] Buff item used, but on cooldown ({} ticks remaining). Returning false.", this.getId(), world.getTime(), remainingTicks);
                return false; // Action failed due to cooldown
            } else {
                // Apply Buffs
                int duration = config.buffs.greenBeanBuffDuration();
                int speedAmplifier = config.buffs.greenBeanBuffAmplifierSpeed();
                int strengthAmplifier = config.buffs.greenBeanBuffAmplifierStrength();
                int absorptionAmplifier = config.buffs.greenBeanBuffAmplifierAbsorption();
                int regenAmplifier = config.buffs.greenBeanBuffAmplifierRegen();


                this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, duration, speedAmplifier));
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, duration, strengthAmplifier));
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, duration, absorptionAmplifier));
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, duration, regenAmplifier));


                // Play sound
                SoundEvent buffSound = getRandomSoundFrom(ModSounds.HAMSTER_CELEBRATE_SOUNDS, this.random);
                world.playSound(null, this.getBlockPos(), buffSound, SoundCategory.NEUTRAL, 1.0F, 1.0F);


                // Set cooldown
                this.steamedBeansCooldownEndTick = currentTime + config.cooldowns.steamedGreenBeansBuffCooldown();
                actionTaken = true; // Action was successful
                AdorableHamsterPets.LOGGER.debug("[FeedAttempt {} Tick {}] Applied buffs from Steamed Green Beans. Cooldown set to {}. Returning true.", this.getId(), world.getTime(), this.steamedBeansCooldownEndTick);
            }
        }
        // --- End 2. Steamed Green Beans Logic ---


        // --- 3. Standard Food Logic (Only if not buff item) ---
        else if (isFood) {
            if (canHeal) {
                float healAmount = config.behavior.standardFoodHealing();
                this.heal(healAmount);
                actionTaken = true;
                AdorableHamsterPets.LOGGER.debug("[FeedAttempt {} Tick {}] Applied healing ({} HP) from standard food. Returning true.", this.getId(), world.getTime(), healAmount);
            } else if (readyToBreed) {
                this.setSitting(false, true); // Stand up, suppress sound
                this.setCustomInLove(player); // Sets customLoveTimer and sends status packet
                this.setInLove(true); // Sets data tracker IS_IN_LOVE
                actionTaken = true;
                AdorableHamsterPets.LOGGER.debug("[FeedAttempt {} Tick {}] Initiated love mode from standard food. customLoveTimer={}, IS_IN_LOVE={}. Returning true.", this.getId(), world.getTime(), this.customLoveTimer, this.dataTracker.get(IS_IN_LOVE));
            } else {
                // Cannot heal (full health) AND cannot breed (e.g., baby, already in love, on cooldown)
                AdorableHamsterPets.LOGGER.debug("[FeedAttempt {} Tick {}] Standard food used, but cannot heal and not ready to breed. Returning false.", this.getId(), world.getTime());
                // actionTaken remains false
            }
        }
        // --- End 3. Standard Food Logic ---


        // --- 4. Final Return ---
        // Returns true only if healing, breeding, or buffing occurred.
        return actionTaken;
        // --- End 4. Final Return ---
    }
    // --- Final Closing Bracket ---
}