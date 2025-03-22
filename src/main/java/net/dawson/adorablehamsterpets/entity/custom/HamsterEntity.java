package net.dawson.adorablehamsterpets.entity.custom;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.entity.AI.*;
import net.dawson.adorablehamsterpets.entity.ModEntities;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.dawson.adorablehamsterpets.sound.ModSounds; // CHANGED: referencing ModSounds
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
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static net.dawson.adorablehamsterpets.sound.ModSounds.*;


public class HamsterEntity extends TameableEntity implements GeoEntity {


    // The geckolib instance cache
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // A public field for the parrot-like "ShoulderMountRequested" logic
    public boolean shoulderMountRequested = false;

    public HamsterEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 3; // if you want them to drop XP
    }

    /**
     * Register your attribute modifiers. Usually called in ModEntities.
     */
    public static DefaultAttributeContainer.Builder createHamsterAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0D);
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        // No fall damage for these little guys
        return false;
    }

    /* -------------------------------------------------------------------------
     *                             Data Tracking Setup
     * ------------------------------------------------------------------------- */

    public static final TrackedData<Boolean> IS_BEGGING =   // <---- MADE PUBLIC STATIC FINAL
            DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public static final TrackedData<Boolean> IS_IN_LOVE =    // <---- MADE PUBLIC STATIC FINAL
            DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public static final TrackedData<Boolean> IS_SLEEPING =   // <---- MADE PUBLIC STATIC FINAL
            DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public static final TrackedData<Boolean> NEAR_DIAMOND =  // <---- MADE PUBLIC STATIC FINAL
            DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public static final TrackedData<Boolean> IS_REFUSING_FOOD =  // <---- MADE PUBLIC STATIC FINAL
            DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private int refuseTimer = 0;      // how many ticks remain for the “refuse” animation
    private ItemStack lastFoodItem = ItemStack.EMPTY; // track last item fed


    public static final TrackedData<Boolean> IS_SITTING =  // <---- MADE PUBLIC STATIC FINAL
            DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);



    private static final TrackedData<Integer> VARIANT =
            DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.INTEGER);


    public int customLoveTimer;

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(VARIANT, 0);
        builder.add(IS_SLEEPING, false);
        builder.add(IS_BEGGING, false);
        builder.add(IS_IN_LOVE, false);
        builder.add(NEAR_DIAMOND, false);
        builder.add(IS_REFUSING_FOOD, false);
        builder.add(IS_SITTING, false); // <---- ENSURE IS_SITTING IS REGISTERED HERE
    }

    public boolean isInCustomLove() {
        return this.customLoveTimer > 0;
    }

    public void setCustomInLove(PlayerEntity player) {
        this.customLoveTimer = 600; // e.g., 600 ticks = 30 seconds
        this.getWorld().sendEntityStatus(this, (byte) 18); // hearts
    }

    /* -------------------------------------------------------------------------
     *                       Getters / Setters for DataTracker
     * ------------------------------------------------------------------------- */


    public boolean isSleeping() {
        return this.dataTracker.get(IS_SLEEPING);
    }

    public void setSleeping(boolean sleeping) {
        this.dataTracker.set(IS_SLEEPING, sleeping);
    }

    public int getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public void setVariant(int variantId) {
        this.dataTracker.set(VARIANT, variantId);
    }

    public boolean isBegging() {
        return this.dataTracker.get(IS_BEGGING);
    }

    public void setBegging(boolean value) {
        this.dataTracker.set(IS_BEGGING, value);
    }

    public boolean isInLove() {
        return this.dataTracker.get(IS_IN_LOVE);
    }

    public void setInLove(boolean value) {
        this.dataTracker.set(IS_IN_LOVE, value);
    }

    public boolean isNearDiamond() {
        return this.dataTracker.get(NEAR_DIAMOND);
    }

    public void setNearDiamond(boolean value) {
        this.dataTracker.set(NEAR_DIAMOND, value);
    }

    public boolean isRefusingFood() {
        return this.dataTracker.get(IS_REFUSING_FOOD);
    }

    public void setRefusingFood(boolean value) {
        this.dataTracker.set(IS_REFUSING_FOOD, value);
    }

    /* -------------------------------------------------------------------------
     *                       Tamed Status - NBT read/write
     * ------------------------------------------------------------------------- */

    @Override
    public void setTamed(boolean tamed, boolean updateAttributes) {
        // Call the vanilla method so the dataTracker is updated.
        super.setTamed(tamed, updateAttributes);

        // Then do your custom logic:
        if (tamed) {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(16.0D);
            this.setHealth(16.0F);
        } else {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(8.0D);
            this.setHealth(8.0F);
        }
    }


    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        // --- Let TameableEntity store "tamed" and "owner" automatically ---
        super.writeCustomDataToNbt(nbt);

        // --- Store only hamster-specific stuff ---
        nbt.putInt("HamsterVariant", this.getVariant());
        nbt.putBoolean("IsSleeping", this.isSleeping());

    }


    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        this.setTamed(true, true);


        if (nbt.contains("HamsterVariant")) {
            this.setVariant(nbt.getInt("HamsterVariant"));
        }
        if (nbt.contains("IsSleeping")) {
            this.setSleeping(nbt.getBoolean("IsSleeping"));
        }
    }

    /* -------------------------------------------------------------------------
     *                         Spawning & Variant Logic
     * ------------------------------------------------------------------------- */

    @Override
    public EntityData initialize(
            ServerWorldAccess world,
            LocalDifficulty difficulty,
            SpawnReason spawnReason,
            @Nullable EntityData entityData
    ) {
        entityData = super.initialize(world, difficulty, spawnReason, entityData);

        // If variant not set, pick a random one
        if (this.getVariant() == 0) {
            int totalVariants = 32;
            int chosen = this.random.nextInt(totalVariants) + 1;
            this.setVariant(chosen);
        }
        return entityData;
    }

    /* -------------------------------------------------------------------------
     *                              AI Goals
     * ------------------------------------------------------------------------- */
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));

        // If tamed, follow owner
        this.goalSelector.add(1, new FollowOwnerGoal(this, 1.2D, 4.0F, 16.0F));

        // FLEE from players (under certain conditions) or from hostile mobs:
        this.goalSelector.add(2, new HamsterFleeGoal<>(
                this,
                LivingEntity.class, // We can pass LivingEntity to catch both players & hostiles
                8.0F,               // max distance to start fleeing
                1.0D,               // walk speed
                1.5D                // sprint speed
        ));

        this.goalSelector.add(3, new HamsterMateGoal(this, 1.0D));

        this.goalSelector.add(4, new HamsterBegGoal(this));

        this.goalSelector.add(5, new HamsterTemptGoal(this, 1.4D,
                stack -> stack.isOf(ModItems.SLICED_CUCUMBER), false));

        this.goalSelector.add(6, new SitGoal(this));

        this.goalSelector.add(7, new HamsterSleepGoal(this)); // wild hamsters sleep in day
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 0.75D));

        this.goalSelector.add(9, new HamsterShoulderRideGoal(this));
        this.goalSelector.add(10, new DiamondSniffGoal(this));
        this.goalSelector.add(11, new CreeperDetectGoal(this));

        this.goalSelector.add(12, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(13, new LookAroundGoal(this));
    }

    /* -------------------------------------------------------------------------
     *                       Prevent Rotation While Sleeping
     * ------------------------------------------------------------------------- */

    @Override
    public void changeLookDirection(double cursorX, double cursorY) {
        // --- ADDED: Prevent rotation while sleeping ---
        if (this.isSleeping()) {
            // Do NOTHING to prevent rotation
            return;
        }
        // --- END Rotation Prevention ---

        super.changeLookDirection(cursorX, cursorY); // Default behavior when not sleeping
    }

    /* -------------------------------------------------------------------------
     *                             Modified Sitting Logic for Sleep
     * ------------------------------------------------------------------------- */

    @Override
    public void setSitting(boolean sitting) {
        // --- MODIFIED: Use "sitting" state to represent "sleeping" ---
        super.setSitting(sitting); // Call super to handle vanilla sitting logic

        // --- ADD CUSTOM LOGIC HERE WHEN SLEEPING/WAKING ---
        AdorableHamsterPets.LOGGER.info("setSitting() called: sitting = " + sitting + ", Entity ID: " + this.getId()); // DEBUG LOGGING
        if (sitting) {
            AdorableHamsterPets.LOGGER.info("Hamster is now SLEEPING (sitting state: true)"); // Optional log
            // Play sleep animation or sound HERE when sitting STARTS (going to sleep)
            // --- ADDED SLEEP SOUND LOGIC ---
            SoundEvent sleepSound = getRandomSoundFrom(ModSounds.HAMSTER_SLEEP_SOUNDS, this.random);
            this.getWorld().playSound(null, this.getBlockPos(), sleepSound, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        } else {
            AdorableHamsterPets.LOGGER.info("Hamster is now AWAKE (sitting state: false)"); // Optional log
            // Play wake-up animation or sound if desired when sitting STOPS (waking up)
            // (Optional) You could add a "wake up" sound here if you have one
        }
    }

    @Override
    public boolean isSitting() {
        // --- MODIFIED: "isSitting" now means "isSleeping" for our hamster ---
        return super.isSitting(); // Just return the vanilla sitting state
    }


    /* -------------------------------------------------------------------------
     *                        Right-Click Interaction
     * ------------------------------------------------------------------------- */
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        // --- 1. WILD HAMSTER INTERACTIONS ---
        if (!this.isTamed()) {
            AdorableHamsterPets.LOGGER.info("interactMob: Wild hamster interaction check");
            if (player.isSneaking() && stack.isOf(ModItems.SLICED_CUCUMBER)) {
                AdorableHamsterPets.LOGGER.info("interactMob: Attempting taming");
                if (tryTame(player, stack)) {
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        }

        // --- 2. TAMED HAMSTER INTERACTIONS ---
        if (isIsFood(stack)) {
            AdorableHamsterPets.LOGGER.info("interactMob: Food item detected: " + stack.getItem().getName().getString());
            if (checkRepeatFoodRefusal(stack, player)) {
                AdorableHamsterPets.LOGGER.info("interactMob: Food refused (repeat)");
                return ActionResult.SUCCESS;
            }

            boolean feedingHappened = tryFeedingAsTamed(player, stack);
            if (feedingHappened) {
                AdorableHamsterPets.LOGGER.info("interactMob: Feeding action happened");
                this.lastFoodItem = stack.copy();
                return ActionResult.SUCCESS;
            }
            // If no feeding action occurred, we continue to other interactions.
        }
        // c. → If the player is sneaking:
        else if (player.isSneaking()) {
            AdorableHamsterPets.LOGGER.info("interactMob: Player sneaking - opening inventory");
            openHamsterInventory(player);
            return ActionResult.SUCCESS;
        }
        // Otherwise (if not wild, not feeding, not sneaking): => SIT TOGGLE REACHED!
        else {
            // a. → Toggle the sitting as the final action.
            // --- DEBUG LOGGING BEFORE SIT TOGGLE ---
            AdorableHamsterPets.LOGGER.info("interactMob: Sit toggle logic was reached unexpectedly during breeding.");
            // --- END DEBUG LOGGING ---
            // --- TRIGGER VANILLA SITTING ---
            if (!this.getWorld().isClient) {
                // Toggle sitting state using vanilla setSitting method
                this.setSitting(!this.isSitting()); // Toggles sitting state (which looks like SLEEPING)
                this.jumping = false; // Vanilla WolfEntity also does this
                this.navigation.stop(); // Vanilla WolfEntity also does this
                this.setTarget(null);  // Vanilla WolfEntity also does this
            }
            // c. → Stop.
            // --- REMOVED RETURN FROM HERE ---
        }

        // === MOVED DEFAULT RETURN STATEMENT TO HERE ===
        return ActionResult.SUCCESS; // Default return for sleep toggle and other cases
        // =============================================
    }


    /* -------------------------------------------------------------------------
     *                      Helper Methods for interactMob
     * ------------------------------------------------------------------------- */

    private boolean tryTame(PlayerEntity player, ItemStack itemStack) {
        if (!itemStack.isOf(ModItems.SLICED_CUCUMBER)) {
            return false; // Not the right item => no taming attempt
        }

        SoundEvent begSound = ModSounds.getRandomSoundFrom(ModSounds.HAMSTER_BEG_SOUNDS, this.random);
        this.getWorld().playSound(null, this.getBlockPos(), begSound, SoundCategory.NEUTRAL, 1.0F, 1.0F);

        // Decrement the item if not in creative
        if (!player.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        // Random chance to succeed
        if (this.random.nextInt(3) == 0) {
            // Success: set owner, set tamed, etc.
            this.setOwnerUuid(player.getUuid());
            this.setTamed(true, true);
            this.getNavigation().stop();
            this.setSleeping(false);
            this.setTarget(null);
            this.getWorld().sendEntityStatus(this, (byte)7);

            // --- ACHIEVEMENT TRIGGER ---
            if (!this.getWorld().isClient && player instanceof ServerPlayerEntity serverPlayer) { // Check server-side and player type
                Criteria.TAME_ANIMAL.trigger(serverPlayer, this);
            }

        } else {
            // Failure
            this.getWorld().sendEntityStatus(this, (byte)6);
        }

        // We did attempt taming, so return true to indicate the logic was executed
        return true;
    }

    private boolean checkRepeatFoodRefusal(ItemStack currentStack, PlayerEntity player) {
        // 1) If hamster has never been fed OR item is different => no refusal
        if (this.lastFoodItem.isEmpty() ||
                !ItemStack.areItemsEqual(this.lastFoodItem, currentStack)) {
            return false;
        }

        // 2) Same item => hamster refuses
        this.setRefusingFood(true);      // triggers your "anim_hamster_no"
        this.refuseTimer = 40;          // 2 seconds of refusal

        // 3) Send a message to the player
        player.sendMessage(Text.literal("Hamster wants to try something different."), true);

        return true;
    }


    // List of items that can feed or heal the hamster

    private boolean tryFeedingAsTamed(PlayerEntity player, ItemStack stack) {
        // Check if the item is considered food for hamsters (using the isIsFood helper method)
        boolean isFood = isIsFood(stack);
        // Check if the item is specifically STEAMED_GREEN_BEANS (for buffs)
        boolean isBuffItem = (stack.isOf(ModItems.STEAMED_GREEN_BEANS));
        // Check if the hamster's health is below maximum (can heal)
        boolean canHeal = (this.getHealth() < this.getMaxHealth());
        // Check if the item is SUNFLOWER_SEEDS (for breeding)
        boolean isBreedingFood = stack.isOf(ModItems.SUNFLOWER_SEEDS);
        // Check if the hamster is an adult and ready to breed (breeding age is 0 and not already in love)
        boolean readyToBreed = (this.getBreedingAge() == 0 && !this.isInLove());

        // If the item is NOT a food item and NOT a buff item, we can't feed, so return false
        if (!isFood && !isBuffItem) {
            return false; // Not a valid feeding item
        }

        // We only proceed with feeding if at least one condition is met:
        // either the hamster can heal, or it's breeding food and ready to breed, or it's a buff item
        if (!canHeal && !readyToBreed && !isBuffItem) {
            return false; // No feeding needed or item not relevant for current state
        }

        boolean feedingOccurred = false; // Initialize a flag to track if any feeding action happened

        // If not in creative mode, decrement the stack of the item being used
        if (!player.getAbilities().creativeMode) {
            stack.decrement(1);
        }

        // 1) Heal if needed
        if (canHeal) {
            this.heal(2.0F); // Heal the hamster by 2 health points
            feedingOccurred = true; // Mark that a feeding action (healing) occurred
        }

        // 2) Breed if it's the special breeding food and hamster is ready to breed
        if (isBreedingFood && readyToBreed) {
            this.setSleeping(false); // --- ADDED: Force hamster to NOT sleep before breeding ---
            this.setCustomInLove(player); // Set the hamster in "custom love" mode (for breeding animation/logic)
            this.setInLove(true);          // Set the data tracker 'inLove' to true (for visual hearts)
            feedingOccurred = true; // Mark that a feeding action (breeding setup) occurred
        }

        // 3) Buff logic for STEAMED_GREEN_BEANS
        if (isBuffItem) {
            // Apply buff effects (e.g., Speed, Jump Boost, etc.) - your buff logic here
            // Example buffs are already in your original code
            int duration = 3600; // 3 minutes in ticks
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, duration, 1));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, duration, 1));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, duration, 0));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, duration, 0));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, duration, 0));

            // Play a "celebrate" sound when buffed
            SoundEvent buffSound = getRandomSoundFrom(ModSounds.HAMSTER_CELEBRATE_SOUNDS, this.random);
            this.getWorld().playSound(null, this.getBlockPos(), buffSound, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            feedingOccurred = true; // Mark that a feeding action (buffing) occurred
        }

        return feedingOccurred; // Return true if any feeding action (heal, breed, or buff) was executed, false otherwise
    }

    private static boolean isIsFood(ItemStack stack) {
        final Set<Item> HAMSTER_FOODS = new HashSet<>(Arrays.asList(
                ModItems.HAMSTER_FOOD_MIX,
                ModItems.SUNFLOWER_SEEDS,
                ModItems.GREEN_BEANS,
                ModItems.CUCUMBER,
                ModItems.GREEN_BEAN_SEEDS,
                ModItems.CUCUMBER_SEEDS,
                Items.APPLE,
                Items.CARROT,
                Items.MELON_SLICE,
                Items.SWEET_BERRIES,
                Items.BEETROOT,
                Items.WHEAT,
                Items.WHEAT_SEEDS
        ));

        boolean isFood = HAMSTER_FOODS.contains(stack.getItem());
        return isFood;
    }

    public boolean isCheekPouchEmpty() {
        // I haven’t yet implemented a real inventory, just returning true or false for now
        // but once I do have one, it will be something like:
        // return this.cheekInventory.isEmpty();
        return true;
    }


    private void openHamsterInventory(PlayerEntity player) {
        player.sendMessage(Text.literal("Opening hamster cheek pouch..."), true);
    }


    /* -------------------------------------------------------------------------
     *                           Food Refusal Logic
     * ------------------------------------------------------------------------- */




    /* -------------------------------------------------------------------------
     *                            Tick Logic
     * ------------------------------------------------------------------------- */

    private int tamingCooldown = 0;  // When > 0, do not allow sleep toggling

    @Override
    public void tick() {
        super.tick();

        if (this.isRefusingFood()) {
            if (refuseTimer > 0) {
                refuseTimer--;
            } else {
                // Timer finished => stop refusing
                this.setRefusingFood(false);
            }
        }

        // Decrease tamingCooldown each tick if it's above 0
        if (tamingCooldown > 0) {
            tamingCooldown--;
        }

        if (this.hasPlayerRider()) {
            if (this.customLoveTimer > 0) {
                this.customLoveTimer--;
            }
        }
    }


    /* -------------------------------------------------------------------------
     *                    Sound Events (Ambient, Hurt, Death)
     * ------------------------------------------------------------------------- */
    @Override
    protected SoundEvent getAmbientSound() {

//        if (this.creeperSeesPlayer()) {
//            return getRandomSoundFrom(ModSounds.HAMSTER_CREEPER_DETECT_SOUNDS, this.random);
//        }

        if (this.isNearDiamond()) {
            return getRandomSoundFrom(ModSounds.HAMSTER_DIAMOND_SNIFF_SOUNDS, this.random);
        }

        if (this.isBegging()) {
            return getRandomSoundFrom(ModSounds.HAMSTER_BEG_SOUNDS, this.random);
        }

        if (this.isSitting()) {
            return getRandomSoundFrom(ModSounds.HAMSTER_SLEEP_SOUNDS, this.random);
        }

        return getRandomSoundFrom(ModSounds.HAMSTER_IDLE_SOUNDS, this.random);
    }


    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return getRandomSoundFrom(ModSounds.HAMSTER_HURT_SOUNDS, this.random);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return getRandomSoundFrom(ModSounds.HAMSTER_DEATH_SOUNDS, this.random);
    }

    /* -------------------------------------------------------------------------
     *                    Changing Footstep Volume and Pitch
     * ------------------------------------------------------------------------- */
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        try {
            java.lang.reflect.Method method = AbstractBlock.class.getDeclaredMethod("getSoundGroup", BlockState.class);
            method.setAccessible(true);
            BlockSoundGroup group = (BlockSoundGroup) method.invoke(state.getBlock(), state);
            this.playSound(group.getStepSound(), 0.5F, 1.2F);
        } catch (Exception ex) {
            AdorableHamsterPets.LOGGER.error("Error obtaining block sound group for footstep", ex);
            this.playSound(net.minecraft.sound.SoundEvents.BLOCK_NYLIUM_STEP, 0.5F, 1.2F);
        }
    }


    /* -------------------------------------------------------------------------
     *      Helper Methods to Play Diamond Sniff and Creeper Detect Sounds... not sure if we need these or not
     * ------------------------------------------------------------------------- */

    public void playDiamondSniffSound() {
        // pick a random from DIAMOND_SNIFF_SOUNDS array
        this.getWorld().playSound(null, this.getBlockPos(),
                getRandomSoundFrom(HAMSTER_DIAMOND_SNIFF_SOUNDS, this.random),
                net.minecraft.sound.SoundCategory.NEUTRAL,
                1.0F, 1.0F);
    }

    public void playCreeperDetectSound() {
        // pick a random from DIAMOND_SNIFF_SOUNDS array
        this.getWorld().playSound(null, this.getBlockPos(),
                getRandomSoundFrom(HAMSTER_CREEPER_DETECT_SOUNDS, this.random),
                net.minecraft.sound.SoundCategory.NEUTRAL,
                1.0F, 1.0F);
    }


    /* -------------------------------------------------------------------------
     *                      Breeding & Offspring
     * ------------------------------------------------------------------------- */
    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity mate) {
        // --- ADDED DEBUG LOGGING - METHOD START ---
        AdorableHamsterPets.LOGGER.info("HamsterEntity: createChild() - START, Parent 1 Class: " + this.getClass().getSimpleName() + ", Parent 2 Class: " + mate.getClass().getSimpleName());
        // --- END DEBUG LOGGING ---

        HamsterEntity baby = ModEntities.HAMSTER.create(world);
        if (baby != null && this.isTamed()) {
            baby.setOwner((PlayerEntity) this.getOwner());
            baby.setVariant(this.getVariant());
        }

        // --- ADDED DEBUG LOGGING - BEFORE RETURN ---
        AdorableHamsterPets.LOGGER.info("HamsterEntity: createChild() - END, Baby ID: " + (baby != null ? baby.getId() : "null"));
        // --- END DEBUG LOGGING ---
        return baby;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(ModItems.HAMSTER_FOOD_MIX);
    }


    /* -------------------------------------------------------------------------
     *                    Geckolib Animation & Controllers
     * ------------------------------------------------------------------------- */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                new AnimationController<>(this, "movementController", 5, event -> {
                    AdorableHamsterPets.LOGGER.info("Animation Tick START - isSitting: " + this.isSitting() + ", isRefusingFood: " + this.isRefusingFood() + ", isMoving: " + (this.getVelocity().horizontalLengthSquared() > 0.0001) + ", isBegging: " + this.isBegging()); // <---- VERY DETAILED CONDITION LOGGING

                    // 1) Check if refusing food => "anim_hamster_no"
                    if (this.isRefusingFood()) {
                        AdorableHamsterPets.LOGGER.info("Condition 1 (isRefusingFood) is TRUE"); // <---- LOGGING CONDITION CHECK
                        AdorableHamsterPets.LOGGER.info("Playing anim_hamster_no");
                        return event.setAndContinue(RawAnimation.begin().thenPlay("anim_hamster_no"));
                    } else {
                        AdorableHamsterPets.LOGGER.info("Condition 1 (isRefusingFood) is FALSE"); // <---- LOGGING CONDITION CHECK
                    }


                    // 2) Movement checks (evaluate movement first)
                    if (this.getVelocity().horizontalLengthSquared() > 0.0001) {
                        AdorableHamsterPets.LOGGER.info("Condition 2 (isMoving) is TRUE"); // <---- LOGGING CONDITION CHECK
                        double speed = this.getVelocity().horizontalLength();
                        if (speed > 0.05) {
                            AdorableHamsterPets.LOGGER.info("Playing anim_hamster_running");
                            return event.setAndContinue(RawAnimation.begin().thenPlay("anim_hamster_running"));
                        } else {
                            AdorableHamsterPets.LOGGER.info("Playing anim_hamster_walking");
                            return event.setAndContinue(RawAnimation.begin().thenPlay("anim_hamster_walking"));
                        }
                    } else {
                        AdorableHamsterPets.LOGGER.info("Condition 2 (isMoving) is FALSE"); // <---- LOGGING CONDITION CHECK
                    }


                    // 3) **CORRECTED ORDER: Check if sitting (sleeping) => "anim_hamster_sleeping"**
                    if (this.isSitting()) {
                        AdorableHamsterPets.LOGGER.info("Condition 3 (isSitting) is TRUE"); // <---- LOGGING CONDITION CHECK
                        AdorableHamsterPets.LOGGER.info("Playing anim_hamster_sleeping");
                        return event.setAndContinue(RawAnimation.begin().thenPlay("anim_hamster_sleeping"));
                    } else {
                        AdorableHamsterPets.LOGGER.info("Condition 3 (isSitting) is FALSE"); // <---- LOGGING CONDITION CHECK
                    }


                    // 4) If begging => "anim_hamster_begging"
                    if (this.isBegging()) {
                        AdorableHamsterPets.LOGGER.info("Condition 4 (isBegging) is TRUE"); // <---- LOGGING CONDITION CHECK
                        AdorableHamsterPets.LOGGER.info("Playing anim_hamster_begging");
                        return event.setAndContinue(RawAnimation.begin().thenPlay("anim_hamster_begging"));
                    } else {
                        AdorableHamsterPets.LOGGER.info("Condition 4 (isBegging) is FALSE"); // <---- LOGGING CONDITION CHECK
                    }


                    // 5) Default => "anim_hamster_idle"
                    AdorableHamsterPets.LOGGER.info("Condition 5 (Default) - Playing anim_hamster_idle"); // <---- LOGGING CONDITION REACHED
                    return event.setAndContinue(RawAnimation.begin().thenPlay("anim_hamster_idle"));
                })
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
