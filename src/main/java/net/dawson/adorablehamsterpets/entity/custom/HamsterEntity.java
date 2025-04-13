package net.dawson.adorablehamsterpets.entity.custom;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.attachment.HamsterShoulderData;
import net.dawson.adorablehamsterpets.attachment.ModEntityAttachments;
import net.dawson.adorablehamsterpets.entity.AI.*;
import net.dawson.adorablehamsterpets.entity.ImplementedInventory;
import net.dawson.adorablehamsterpets.entity.ModEntities;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.dawson.adorablehamsterpets.networking.payload.StartHamsterFlightSoundPayload;
import net.dawson.adorablehamsterpets.networking.payload.StartHamsterThrowSoundPayload;
import net.dawson.adorablehamsterpets.screen.HamsterEntityScreenHandlerFactory;
import net.dawson.adorablehamsterpets.sound.ModSounds;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.animation.Animation.LoopType;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.BiomeKeys; // If needed for specific biome checks
import java.util.List; // For variant pools

import net.minecraft.util.math.Box; // Import Box for area checking
import net.minecraft.util.math.Vec3d; // Import Vec3d
import net.minecraft.entity.LivingEntity; // Import LivingEntity

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static net.dawson.adorablehamsterpets.sound.ModSounds.*;


public class HamsterEntity extends TameableEntity implements GeoEntity, ImplementedInventory {

    // --- Constants ---
    @Unique private int interactionCooldown = 0;
    private static final int INVENTORY_SIZE = 6;
    private static final int REFUSE_FOOD_TIMER_TICKS = 40; // 2 seconds
    private static final int CUSTOM_LOVE_TICKS = 600; // 30 seconds
    private static final float THROW_DAMAGE = 20.0f;
    private static final double MELEE_ATTACK_DAMAGE = 2.0D; // Half of the Wolf's 4.0D
    private static final double THROWN_GRAVITY = -0.05;
    @Unique private int throwTicks = 0;

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


    // Creates a HamsterEntity instance from shoulder data, loading variant, health, age, and inventory.
    // Does NOT set position or spawn the entity.
    @Nullable
    public static HamsterEntity createFromShoulderData(ServerWorld world, PlayerEntity player, HamsterShoulderData data) {
        AdorableHamsterPets.LOGGER.debug("[HamsterEntity] createFromShoulderData called for player {} with data: {}", player.getName().getString(), data);
        HamsterEntity hamster = ModEntities.HAMSTER.create(world);
        if (hamster != null) {
            // Load Core Data
            hamster.setVariant(data.variantId());
            hamster.setHealth(data.health());
            hamster.setOwnerUuid(player.getUuid());
            hamster.setTamed(true, true);
            hamster.setBreedingAge(data.breedingAge());
            hamster.throwCooldownEndTick = data.throwCooldownEndTick(); // <-- LOAD COOLDOWN

            // Load Inventory (Keep existing logic)
            NbtCompound inventoryNbt = data.inventoryNbt();
            if (!inventoryNbt.isEmpty()) {
                RegistryWrapper.WrapperLookup registries = world.getRegistryManager();
                Inventories.readNbt(inventoryNbt, hamster.items, registries);
                AdorableHamsterPets.LOGGER.debug("[HamsterEntity] Loaded inventory from NBT: {}", inventoryNbt);
                hamster.updateCheekTrackers();
            } else {
                AdorableHamsterPets.LOGGER.debug("[HamsterEntity] No inventory NBT found in shoulder data, clearing inventory.");
                hamster.items.clear();
                hamster.updateCheekTrackers();
            }
        } else {
            AdorableHamsterPets.LOGGER.error("[HamsterEntity] Failed to create HamsterEntity instance in createFromShoulderData.");
        }
        return hamster;
    }


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

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
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

        // Always update cheek trackers on initialization
        this.updateCheekTrackers();

        // Call and return the super method's result
        return super.initialize(world, difficulty, spawnReason, entityData);
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
    private int cleaningTimer = 0;
    private int cleaningCooldownTimer = 0;
    private int blinkTimer = 0; // Timer for current blink duration/state
    private int nextBlinkCheckTick = 200; // Ticks until next potential blink


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

    // --- Constructor ---
    public HamsterEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 3;
    }

    // --- Attributes ---
    public static DefaultAttributeContainer.Builder createHamsterAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, THROW_DAMAGE)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, MELEE_ATTACK_DAMAGE)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0D);
    }

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

    public void updateCheekTrackers() {
        boolean leftFull = false;
        for (int i = 0; i < 3; i++) { if (!this.items.get(i).isEmpty()) { leftFull = true; break; } }
        boolean rightFull = false;
        for (int i = 3; i < INVENTORY_SIZE; i++) { if (!this.items.get(i).isEmpty()) { rightFull = true; break; } }
        if (this.isLeftCheekFull() != leftFull) this.setLeftCheekFull(leftFull);
        if (this.isRightCheekFull() != rightFull) this.setRightCheekFull(rightFull);
    }

    // --- NBT Saving/Loading ---
    private RegistryWrapper.WrapperLookup getRegistryLookup() {
        return this.getWorld().getRegistryManager();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("HamsterVariant", this.getVariant());
        nbt.putBoolean("IsSleeping", this.isSleeping());
        nbt.putBoolean("Sitting", this.isSitting());

        RegistryWrapper.WrapperLookup registries = getRegistryLookup();
        NbtCompound inventoryWrapperNbt = new NbtCompound();
        Inventories.writeNbt(inventoryWrapperNbt, this.items, registries);
        nbt.put("Inventory", inventoryWrapperNbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setVariant(nbt.getInt("HamsterVariant"));
        this.setSleeping(nbt.getBoolean("IsSleeping"));
        boolean wasSitting = nbt.getBoolean("Sitting");
        this.setSitting(wasSitting);

        this.items.clear();
        RegistryWrapper.WrapperLookup registries = getRegistryLookup();
        if (nbt.contains("Inventory", NbtElement.COMPOUND_TYPE)) {
            Inventories.readNbt(nbt.getCompound("Inventory"), this.items, registries);
        }
        this.updateCheekTrackers();
    }

    // --- Shoulder Riding Data Handling ---
    public HamsterShoulderData saveToShoulderData() {
        AdorableHamsterPets.LOGGER.trace("[HamsterEntity {}] saveToShoulderData called", this.getId());
        this.updateCheekTrackers();

        NbtCompound inventoryNbt = new NbtCompound();
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            RegistryWrapper.WrapperLookup registries = serverWorld.getRegistryManager();
            Inventories.writeNbt(inventoryNbt, this.items, registries);
            AdorableHamsterPets.LOGGER.trace("[HamsterEntity {}] Saved inventory NBT: {}", this.getId(), inventoryNbt);
        } else {
            AdorableHamsterPets.LOGGER.warn("[HamsterEntity {}] Cannot save inventory NBT on client side or in non-server world!", this.getId());
        }

        // Create data record including the new cooldown field
        HamsterShoulderData data = new HamsterShoulderData(
                this.getVariant(),
                this.getHealth(),
                inventoryNbt,
                this.isLeftCheekFull(),
                this.isRightCheekFull(),
                this.getBreedingAge(),
                this.throwCooldownEndTick // <-- SAVE COOLDOWN
        );
        AdorableHamsterPets.LOGGER.trace("[HamsterEntity {}] Returning shoulder data: {}", this.getId(), data);
        return data;
    }


    // Spawns a HamsterEntity from shoulder data near the player, handling position and spawning.
    // Uses createFromShoulderData to load the entity's state.
    public static void spawnFromShoulderData(ServerWorld world, PlayerEntity player, HamsterShoulderData data) {
        AdorableHamsterPets.LOGGER.debug("[HamsterEntity] spawnFromShoulderData called for player {} with data: {}", player.getName().getString(), data);
        // Use the helper to create and configure the hamster
        HamsterEntity hamster = createFromShoulderData(world, player, data);

        if (hamster != null) {
            // --- Set Position for Normal Dismount ---
            double angle = Math.toRadians(player.getYaw());
            double offsetX = -Math.sin(angle) * 0.7;
            double offsetZ = Math.cos(angle) * 0.7;
            hamster.refreshPositionAndAngles(player.getX() + offsetX, player.getY() + 0.1, player.getZ() + offsetZ, player.getYaw(), player.getPitch());
            // --- End Position Setting ---

            AdorableHamsterPets.LOGGER.debug("[HamsterEntity] Spawning hamster entity from shoulder data...");
            world.spawnEntityAndPassengers(hamster); // Spawn the fully configured hamster
            AdorableHamsterPets.LOGGER.debug("[HamsterEntity] Spawned Hamster ID {} from shoulder data near Player {}", hamster.getId(), player.getName().getString());
        }
    }

    /**
     * Attempts to throw the hamster from the player's shoulder.
     * Called server-side when the throw packet is received.
     * @param player The player attempting the throw.
     */
    public static void tryThrowFromShoulder(ServerPlayerEntity player) {
        World world = player.getWorld();
        UUID playerUuid = player.getUuid();

        // --- Proceed with throw logic ---
        HamsterShoulderData shoulderData = player.getAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA);

        if (shoulderData != null) {
            AdorableHamsterPets.LOGGER.debug("[HamsterEntity] tryThrowFromShoulder: Player {} has shoulder data, proceeding.", player.getName().getString());

            // Create hamster instance FIRST to check its cooldown
            ServerWorld serverWorld = (ServerWorld) world;
            HamsterEntity hamster = HamsterEntity.createFromShoulderData(serverWorld, player, shoulderData);

            if (hamster != null) {
                AdorableHamsterPets.LOGGER.debug("[HamsterEntity] tryThrowFromShoulder: Hamster instance created with cooldownEndTick {}.", hamster.throwCooldownEndTick);

                // --- Check Specific Hamster's Cooldown ---
                long currentTime = world.getTime();
                if (hamster.throwCooldownEndTick > currentTime) {
                    long remainingTicks = hamster.throwCooldownEndTick - currentTime;
                    long totalSecondsRemaining = remainingTicks / 20;
                    long minutes = totalSecondsRemaining / 60;
                    long seconds = totalSecondsRemaining % 60;

                    player.sendMessage(
                            Text.translatable(
                                    "message.adorablehamsterpets.throw_cooldown",
                                    minutes,
                                    seconds
                            ),
                            true // Send to action bar
                    );
                    AdorableHamsterPets.LOGGER.debug("[HamsterEntity] Hamster ID {} (from player {}) is on cooldown ({}m {}s left). Aborting throw.", hamster.getId(), player.getName().getString(), minutes, seconds);
                    // Do NOT remove shoulder data if the specific hamster is on cooldown
                    return;
                }
                // --- End Hamster Cooldown Check ---

                // If we reach here, the specific hamster is NOT on cooldown. Proceed with throw.
                player.removeAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA); // Now remove shoulder data

                // Set Position and Velocity for Throw
                hamster.refreshPositionAndAngles(player.getX(), player.getEyeY() - 0.1, player.getZ(), player.getYaw(), player.getPitch());
                hamster.setThrown(true);
                hamster.interactionCooldown = 10;
                hamster.throwTicks = 0;

                // --- Set Cooldown on the Hamster Instance ---
                hamster.throwCooldownEndTick = currentTime + AdorableHamsterPets.HAMSTER_THROW_COOLDOWN_TICKS;
                AdorableHamsterPets.LOGGER.debug("[HamsterEntity] Set throw cooldown for hamster instance ID {} ending at tick {}.", hamster.getId(), hamster.throwCooldownEndTick);
                // --- End Set Cooldown ---

                float throwSpeed = 1.5f;
                Vec3d lookVec = player.getRotationVec(1.0f);
                Vec3d throwVec = new Vec3d(lookVec.x, lookVec.y + 0.1f, lookVec.z).normalize();
                hamster.setVelocity(throwVec.multiply(throwSpeed));
                hamster.velocityDirty = true;

                // Spawn the entity
                serverWorld.spawnEntity(hamster);
                AdorableHamsterPets.LOGGER.debug("[HamsterEntity] tryThrowFromShoulder: Spawned thrown Hamster ID {}.", hamster.getId());


                // Send S2C Packet to Thrower AND Nearby Players
                StartHamsterFlightSoundPayload flightPayload = new StartHamsterFlightSoundPayload(hamster.getId());
                StartHamsterThrowSoundPayload throwPayload = new StartHamsterThrowSoundPayload(hamster.getId());


                // 1. Send to the throwing player
                AdorableHamsterPets.LOGGER.debug("[Server] Attempting to send FlightSoundPayload to thrower {} for entity ID: {}", player.getName().getString(), hamster.getId());
                ServerPlayNetworking.send(player, flightPayload);
                AdorableHamsterPets.LOGGER.debug("[Server] Attempting to send ThrowSoundPayload to thrower {} for entity ID: {}", player.getName().getString(), hamster.getId()); // Log new payload
                ServerPlayNetworking.send(player, throwPayload); // Send new payload
                AdorableHamsterPets.LOGGER.debug("Sent sound payloads to thrower {} for entity {}", player.getName().getString(), hamster.getId());

                // 2. Find and send to nearby players
                double radius = 64.0;
                Vec3d hamsterPos = hamster.getPos();
                Box searchBox = new Box(
                        hamsterPos.getX() - radius, hamsterPos.getY() - radius, hamsterPos.getZ() - radius,
                        hamsterPos.getX() + radius, hamsterPos.getY() + radius, hamsterPos.getZ() + radius
                );
                List<ServerPlayerEntity> nearbyPlayers = ((ServerWorld)world).getPlayers(p -> p != player && searchBox.contains(p.getPos())); // Cast world

                for (ServerPlayerEntity nearbyPlayer : nearbyPlayers) {
                    AdorableHamsterPets.LOGGER.debug("[Server] Attempting to send FlightSoundPayload to nearby player {} for entity ID: {}", nearbyPlayer.getName().getString(), hamster.getId());
                    ServerPlayNetworking.send(nearbyPlayer, flightPayload);
                    AdorableHamsterPets.LOGGER.debug("[Server] Attempting to send ThrowSoundPayload to nearby player {} for entity ID: {}", nearbyPlayer.getName().getString(), hamster.getId()); // Log new payload
                    ServerPlayNetworking.send(nearbyPlayer, throwPayload); // Send new payload
                    AdorableHamsterPets.LOGGER.debug("Sent sound payloads to nearby player {} for entity {}", nearbyPlayer.getName().getString(), hamster.getId());
                }
                // --- End Send S2C Packets ---

            } else {
                AdorableHamsterPets.LOGGER.error("[HamsterEntity] tryThrowFromShoulder: Failed to create HamsterEntity instance. Re-attaching data (attempt).");
                player.setAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA, shoulderData);
            }
        } else {
            AdorableHamsterPets.LOGGER.warn("[HamsterEntity] tryThrowFromShoulder: Player {} received throw packet but had no shoulder data.", player.getName().getString());
        }
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
    }

    // --- Override canAttackWithOwner for Target Exclusions ---
    // Added this method to prevent the hamster from attacking specific entities
    // when commanded by the owner (via AttackWithOwnerGoal).
    // --- End Target Exclusion Override ---
    @Override
    public boolean canAttackWithOwner(LivingEntity target, LivingEntity owner) {
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
    }


    // --- AI Goals ---
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new HamsterMeleeAttackGoal(this, 1.0D, true)); // Standard speed, track target even if not visible
        this.goalSelector.add(2, new FollowOwnerGoal(this, 1.2D, 4.0F, 16.0F)); // Shifted priority
        this.goalSelector.add(3, new HamsterFleeGoal<>(this, LivingEntity.class, 8.0F, 1.0D, 1.5D)); // Shifted priority
        this.goalSelector.add(4, new HamsterMateGoal(this, 1.0D)); // Shifted priority
        this.goalSelector.add(5, new HamsterBegGoal(this)); // Shifted priority
        this.goalSelector.add(6, new HamsterTemptGoal(this, 1.4D, stack -> stack.isOf(ModItems.SLICED_CUCUMBER), false)); // Shifted priority
        this.goalSelector.add(7, new SitGoal(this)); // Vanilla SitGoal uses TameableEntity.isInSittingPose() // Shifted priority
        this.goalSelector.add(8, new HamsterSleepGoal(this)); // Shifted priority
        this.goalSelector.add(9, new WanderAroundFarGoal(this, 0.75D)); // Shifted priority
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F)); // Shifted priority
        this.goalSelector.add(11, new LookAroundGoal(this)); // Shifted priority

        // --- Target Selector Goals ---
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this)); // Target entities attacking owner
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));   // Target entities owner attacks
        this.targetSelector.add(3, new RevengeGoal(this).setGroupRevenge()); // Target entities attacking self (if tamed)
        // --- End Target Selector Goals ---
    }

    // --- Interaction Logic ---
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {

        // --- Check Knocked Out State First ---
        if (this.isKnockedOut()) {
            // --- Play Wake Up Sound ---
            if (!this.getWorld().isClient()) {
                SoundEvent wakeUpSound = ModSounds.getRandomSoundFrom(ModSounds.HAMSTER_WAKE_UP_SOUNDS, this.random);
                if (wakeUpSound != null) {
                    this.getWorld().playSound(null, this.getBlockPos(), wakeUpSound, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                }
                // --- End Play Wake Up Sound ---
                this.setKnockedOut(false); // Set state on server
            }
            // Consume the interaction on both client and server to prevent further actions (like feeding)
            // and ensure the animation trigger works correctly on the client.
            return ActionResult.success(this.getWorld().isClient());
        }
        // --- End Knocked Out Check ---

        // --- Check Interaction Cooldown ---
        if (this.interactionCooldown > 0) {
            return ActionResult.PASS; // Ignore interaction if on cooldown
        }
        // --- End Check ---

        ItemStack stack = player.getStackInHand(hand);
        boolean isTamed = this.isTamed();
        boolean isSneaking = player.isSneaking();

        if (!isTamed) {
            if (isSneaking && stack.isOf(ModItems.SLICED_CUCUMBER)) {
                if (!this.getWorld().isClient) { tryTame(player, stack); }
                return ActionResult.success(this.getWorld().isClient());
            }
            return ActionResult.PASS;
        }

        if (this.isOwner(player)) {
            if (!this.getWorld().isClient) {
                if (isSneaking) {
                    player.openHandledScreen(new HamsterEntityScreenHandlerFactory(this));
                    return ActionResult.CONSUME;
                }
                // --- Call the isIsFood method ---
                else if (isIsFood(stack) || stack.isOf(ModItems.STEAMED_GREEN_BEANS)) {
                    // --- END CALL ---
                    if (checkRepeatFoodRefusal(stack, player)) {
                        return ActionResult.CONSUME;
                    }
                    boolean feedingOccurred = tryFeedingAsTamed(player, stack);
                    if (feedingOccurred) {
                        this.lastFoodItem = stack.copy();
                        if (!player.getAbilities().creativeMode) {
                            stack.decrement(1);
                        }
                        return ActionResult.CONSUME;
                    }
                }
                // --- Toggle Normal Sitting State ---
                // Call the single-argument override which handles suppressing sound correctly
                this.setSitting(!this.dataTracker.get(IS_SITTING)); // Toggle based on the actual IS_SITTING state

                // Reset navigation regardless of state change
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
                return ActionResult.CONSUME_PARTIAL;
            }
            return ActionResult.success(this.getWorld().isClient());
        }
        return ActionResult.PASS;
    }


    // --- isIsFood Helper Method ---
    private static boolean isIsFood(ItemStack stack) {
        return HAMSTER_FOODS.contains(stack.getItem());
    }
    // --- End Helper Method ---

    private boolean tryTame(PlayerEntity player, ItemStack itemStack) {
        if (!player.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        if (this.random.nextInt(3) == 0) {
            this.setOwnerUuid(player.getUuid());
            this.setTamed(true, true);
            this.navigation.stop();
            this.setSitting(false);
            this.setSleeping(false);
            this.setTarget(null);
            this.getWorld().sendEntityStatus(this, (byte) 7);

            // Play celebrate sound only on success
            SoundEvent celebrateSound = ModSounds.getRandomSoundFrom(HAMSTER_CELEBRATE_SOUNDS, this.random);
            this.getWorld().playSound(null, this.getBlockPos(), celebrateSound, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            if (player instanceof ServerPlayerEntity serverPlayer) {
                Criteria.TAME_ANIMAL.trigger(serverPlayer, this);
            }

            return true;
        } else {
            this.getWorld().sendEntityStatus(this, (byte) 6);
            return false;
        }
    }


    private boolean checkRepeatFoodRefusal(ItemStack currentStack, PlayerEntity player) {
        if (REPEATABLE_FOODS.contains(currentStack.getItem())) return false;
        if (!this.lastFoodItem.isEmpty() && ItemStack.areItemsEqual(this.lastFoodItem, currentStack)) {
            this.setRefusingFood(true);
            this.refuseTimer = REFUSE_FOOD_TIMER_TICKS;
            player.sendMessage(Text.literal("Hamster wants to try something different."), true);
            return true;
        }
        return false;
    }

    private boolean tryFeedingAsTamed(PlayerEntity player, ItemStack stack) {
        boolean isFood = isIsFood(stack); // Use the method now
        boolean isBuffItem = stack.isOf(ModItems.STEAMED_GREEN_BEANS);
        boolean canHeal = this.getHealth() < this.getMaxHealth();
        boolean readyToBreed = this.getBreedingAge() == 0 && !this.isInLove();

        if (!isFood && !isBuffItem) return false;

        boolean actionTaken = false;
        if (isFood && canHeal) { this.heal(2.0F); actionTaken = true; }
        else if (isFood && readyToBreed) {
            this.setSitting(false);
            this.setCustomInLove(player);
            this.setInLove(true);
            actionTaken = true;
        }
        if (isBuffItem) {
            int duration = 3 * 60 * 20;
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, duration, 1));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, duration, 1));
            SoundEvent buffSound = getRandomSoundFrom(ModSounds.HAMSTER_CELEBRATE_SOUNDS, this.random);
            this.getWorld().playSound(null, this.getBlockPos(), buffSound, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            actionTaken = true;
        }
        return actionTaken;
    }

    // --- Taming Override ---
    @Override
    public void setTamed(boolean tamed, boolean updateAttributes) {
        super.setTamed(tamed, updateAttributes);
        if (tamed) {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(16.0D);
            this.setHealth(16.0F);
            // --- Ensure Attack Damage is set correctly on tame ---
            // Set the base attack damage attribute to the defined melee damage when tamed.
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(MELEE_ATTACK_DAMAGE);
            // --- End Attack Damage ---
        } else {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(8.0D);
            // Reset attack damage if untamed (optional, but good practice)
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(MELEE_ATTACK_DAMAGE); // Keep base damage consistent
        }
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
        HamsterEntity baby = ModEntities.HAMSTER.create(world);
        if (baby != null && mate instanceof HamsterEntity parent2) { // Ensure mate is a HamsterEntity
            UUID ownerUUID = this.getOwnerUuid(); // Inherit owner from the 'this' parent
            if (ownerUUID != null) {
                baby.setOwnerUuid(ownerUUID);
                baby.setTamed(true, true); // Ensure baby is tamed if parents are
            }

            // ---  Variant Logic ---
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
            // --- End New Variant Logic ---

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
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) { return isIsFood(stack); } // Use helper

    // --- Tick Logic ---
    @Override
    public void tick() {
        // --- Decrement Timers ---
        if (this.interactionCooldown > 0) this.interactionCooldown--;
        if (this.cleaningCooldownTimer > 0) this.cleaningCooldownTimer--;
        if (this.cleaningTimer > 0) {
            this.cleaningTimer--;
            if (this.cleaningTimer == 0) this.cleaningCooldownTimer = 200;
        }
        if (this.attackAnimTimer > 0) {
            this.attackAnimTimer--;
        }
        // --- Blink Timer Logic ---
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
        // --- End Blink Timer Logic ---
        // --- End Decrement Timers ---

        if (this.isThrown()) {
            this.throwTicks++; // Increment throw timer

            Vec3d currentPos = this.getPos();
            Vec3d currentVel = this.getVelocity();
            Vec3d nextPos = currentPos.add(currentVel);
            World world = this.getWorld();

            // Raycast for block collision first
            HitResult blockHit = world.raycast(new RaycastContext(currentPos, nextPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));

            boolean stopped = false; // Flag to check if we stopped due to collision

            if (blockHit.getType() == HitResult.Type.BLOCK) {
                this.setVelocity(currentVel.multiply(0.6, 0.0, 0.6));
                this.setThrown(false);
                this.playSound(SoundEvents.ENTITY_GENERIC_SMALL_FALL, 0.4f, 1.5f);
                this.setKnockedOut(true);
                stopped = true;
            } else { // Only check entity collision if no block was hit closer
                EntityHitResult entityHit = ProjectileUtil.getEntityCollision(world, this, currentPos, nextPos, this.getBoundingBox().stretch(currentVel).expand(1.0), this::canHitEntity);

                if (entityHit != null && entityHit.getEntity() != null) {
                    Entity hitEntity = entityHit.getEntity();
                    boolean playEffects = false; // Flag to trigger effects

                    if (hitEntity instanceof ArmorStandEntity) {
                        // Hit an armor stand - play effects but don't damage
                        AdorableHamsterPets.LOGGER.debug("Hamster hit Armor Stand.");
                        playEffects = true;
                    } else if (hitEntity instanceof LivingEntity livingHit) {
                        // Hit a living entity - attempt damage
                        boolean damaged = livingHit.damage(this.getDamageSources().thrown(this, this.getOwner()), THROW_DAMAGE);
                        if (damaged) {
                            // Apply Nausea only if damaged
                            int nauseaDuration = 20;
                            livingHit.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, nauseaDuration, 0, false, false, false));
                            playEffects = true; // Play effects because damage was dealt
                        }
                    } else {
                        // Hit a non-living entity
                        playEffects = true; // Play effects for any entity hit
                    }

                    // Play effects if flagged
                    if (playEffects) {
                        // --- Play Impact Sound (Cartoon Punches Only) ---
                        world.playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.HAMSTER_IMPACT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                        // --- End Impact Sounds ---

                        // --- Spawn Poof Particles on Impact ---
                        if (!world.isClient()) {
                            ((ServerWorld)world).spawnParticles(
                                    ParticleTypes.POOF, // Changed to POOF
                                    this.getX(), this.getY() + this.getHeight() / 2.0, this.getZ(), // Position
                                    50,      // Increased Count slightly
                                    0.4, 0.4, 0.4, // Moderate Spread
                                    0.1     // Low Speed (poof effect)
                            );
                            AdorableHamsterPets.LOGGER.debug("Spawned POOF particles at impact."); // Log updated
                        }
                        // --- End Spawn Impact Particles ---
                    }

                    // Stop movement and thrown state after any entity hit
                    this.setVelocity(currentVel.multiply(0.1, 0.1, 0.1));
                    this.setThrown(false);
                    this.setKnockedOut(true);
                    stopped = true;
                }
            }

            // Apply gravity, update position, and spawn trail particles if still thrown
            if (this.isThrown() && !stopped) { // Check flag
                // Apply Gravity
                if (!this.hasNoGravity()) {
                    this.setVelocity(this.getVelocity().add(0.0, THROWN_GRAVITY, 0.0));
                }

                // Check for NaN velocity
                Vec3d currentVelocity = this.getVelocity();
                if (Double.isNaN(currentVelocity.x) || Double.isNaN(currentVelocity.y) || Double.isNaN(currentVelocity.z)) {
                    this.setVelocity(Vec3d.ZERO);
                    this.setThrown(false);
                    AdorableHamsterPets.LOGGER.warn("Hamster velocity became NaN, resetting and stopping throw.");
                } else {
                    // Update Position
                    this.setPosition(this.getX() + currentVelocity.x, this.getY() + currentVelocity.y, this.getZ() + currentVelocity.z);
                    this.velocityDirty = true;

                    // --- Spawn Trail Particles (with delay) ---
                    if (!world.isClient() && this.throwTicks > 2) { // Check delay
                        ((ServerWorld)world).spawnParticles(
                                ParticleTypes.GUST,
                                this.getX(), this.getY() + this.getHeight() / 2.0, this.getZ(), // Centered Y
                                1, 0.1, 0.1, 0.1, 0.0
                        );
                    }
                    // --- End Spawn Trail Particles ---
                }
            } else {
                // Reset throwTicks if not thrown
                if (this.throwTicks != 0) {
                    this.throwTicks = 0;
                }
            }
        }

        // Call super.tick() *after* processing thrown state and timers
        super.tick();

        // Handle other non-movement tick logic
        if (this.isRefusingFood() && refuseTimer > 0) { if (--refuseTimer <= 0) this.setRefusingFood(false); }
        if (tamingCooldown > 0) tamingCooldown--; // Assuming tamingCooldown is still used elsewhere
        if (customLoveTimer > 0) customLoveTimer--;
        if (customLoveTimer <= 0 && this.isInLove()) this.setInLove(false);
    }

    protected boolean canHitEntity(Entity entity) {
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
    }

    @Override protected void applyGravity() { if (this.isThrown() && this.hasNoGravity()) return; super.applyGravity(); }
    @Override public boolean canMoveVoluntarily() { return super.canMoveVoluntarily() && !this.isThrown(); }
    @Override public boolean isPushable() { return super.isPushable() && !this.isThrown(); }

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
            java.lang.reflect.Method method = AbstractBlock.class.getDeclaredMethod("getSoundGroup", BlockState.class);
            method.setAccessible(true);
            BlockSoundGroup group = (BlockSoundGroup) method.invoke(state.getBlock(), state);
            this.playSound(group.getStepSound(), 0.5F, 1.2F);
        } catch (Exception ex) {
            AdorableHamsterPets.LOGGER.error("Error obtaining block sound group for footstep", ex);
            this.playSound(SoundEvents.BLOCK_GRASS_STEP, 0.5F, 1.2F);
        }
    }

    // --- Animation ---
    // Define RawAnimation constants
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


    // Keep track of the last crash-related state to handle transitions
    private boolean wasKnockedOutLastTick = false;
    @Unique private int attackAnimTimer = 0;


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "mainController", 5, event -> {
            AnimationController<HamsterEntity> controller = event.getController();

            // Knocked out logic first
            boolean isCurrentlyKnockedOut = this.isKnockedOut();
            boolean justWokeUp = wasKnockedOutLastTick && !isCurrentlyKnockedOut;
            if (justWokeUp) {
                wasKnockedOutLastTick = false;
                return event.setAndContinue(WAKE_UP_ANIM);
            } else if (isCurrentlyKnockedOut) {
                wasKnockedOutLastTick = true;
                String currentAnimNameKO = controller.getCurrentAnimation() != null ? controller.getCurrentAnimation().animation().name() : "";
                if (!currentAnimNameKO.equals("anim_hamster_ko") && !currentAnimNameKO.equals("anim_hamster_crash")) {
                    return event.setAndContinue(CRASH_ANIM);
                } else if (currentAnimNameKO.equals("anim_hamster_crash") && controller.hasAnimationFinished()) {
                    return event.setAndContinue(KNOCKED_OUT_ANIM);
                } else if (currentAnimNameKO.equals("anim_hamster_ko")) {
                    return event.setAndContinue(KNOCKED_OUT_ANIM);
                } else {
                    return event.setAndContinue(CRASH_ANIM); // Continue crash
                }
            }
            wasKnockedOutLastTick = false;
            // End knocked out

            // Check handSwinging FIRST
            if (this.handSwinging) {
                return event.setAndContinue(ATTACK_ANIM);
            }


            // If NOT swinging, play other states
            String currentAnimName = controller.getCurrentAnimation() != null ?
                    controller.getCurrentAnimation().animation().name() : "null";

            // Other base states
            if (this.isThrown()) {
                return event.setAndContinue(FLYING_ANIM);
            }
            if (this.isRefusingFood()) {
                return event.setAndContinue(NO_ANIM);
            }
            if (this.isSleeping()) {
                return event.setAndContinue(SLEEPING_ANIM);
            }
            if (this.dataTracker.get(IS_SITTING)) {
                if (this.cleaningTimer > 0) {
                    return event.setAndContinue(CLEANING_ANIM);
                } else {
                    if (this.cleaningCooldownTimer <= 0 && this.random.nextInt(600) == 0) {
                        this.cleaningTimer = this.random.nextBetween(30, 60);
                        return event.setAndContinue(CLEANING_ANIM);
                    } else {
                        return event.setAndContinue(SITTING_ANIM);
                    }
                }
            }

            // Movement
            double horizontalSpeedSquared = this.getVelocity().horizontalLengthSquared();
            double runThresholdSquared = 0.002;
            if (horizontalSpeedSquared > 1.0E-6) {
                return event.setAndContinue(horizontalSpeedSquared > runThresholdSquared ? RUNNING_ANIM : WALKING_ANIM);
            }

            // Begging
            if (this.isBegging()) {
                return event.setAndContinue(BEGGING_ANIM);
            }

            // Default Idle
            if (!currentAnimName.equals("anim_hamster_attack")) {
                return event.setAndContinue(IDLE_ANIM);
            } else {
                // Let attack finish playing
                return PlayState.CONTINUE;
            }
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}