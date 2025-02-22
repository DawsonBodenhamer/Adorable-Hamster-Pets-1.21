package net.dawson.adorablehamsterpets.entity.custom;

import net.dawson.adorablehamsterpets.item.ModItems;
import net.dawson.adorablehamsterpets.sound.ModSounds;
import net.dawson.adorablehamsterpets.entity.ModEntities;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;


public class HamsterEntity extends TameableEntity implements GeoEntity {
    // DataTracker keys for additional states
    // DataTracker keys for additional states
    private static final TrackedData<Boolean> TAMED =
            DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> VARIANT =
            DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> MOOD =
            DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> IS_SLEEPING =
            DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    // Our mood levels can be conceptual. E.g., 0 = NEUTRAL, 1 = HAPPY, -1 = SAD, etc.
    // Or you can use a bigger scale. For advanced usage, an enum + ordinal or a float can be used.
    public static final int MOOD_HAPPY = 1;
    public static final int MOOD_NEUTRAL = 0;
    public static final int MOOD_SAD = -1;

    // If you prefer a more fine-grained system, you might track mood as an integer from 0-100, for instance.
    // We'll keep it simple here.

    // We’ll store the last “food” fed, to detect repeated feedings => mood penalty, etc.
    private ItemStack lastFoodItem = ItemStack.EMPTY;

    // The geckolib instance cache
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // Simple constructor
    public HamsterEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.setTamed(false); // default to wild
        this.experiencePoints = 3; // if you want them to drop XP
    }


    // Register your attribute modifiers. This must be called from somewhere like ModEntities.
    // Typically done via FabricDefaultAttributeRegistry.register(..., HamsterEntity.createHamsterAttributes());
    public static DefaultAttributeContainer.Builder createHamsterAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0D);
        // You mentioned wanting 0 fall damage => can also override handleFallDamage or setNoGravity, etc.
    }

    // Prevent fall damage without removing gravity
    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false; // no fall damage
    }


    /* -------------------------------------------------------------------------
     *                             Data Tracking Setup
     * ------------------------------------------------------------------------- */

    // The new Yarn style data tracking
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(TAMED, false);
        builder.add(VARIANT, 0);
        builder.add(MOOD, MOOD_NEUTRAL);
        builder.add(IS_SLEEPING, false);
    }

    /* -------------------------------------------------------------------------
     *                       Getters / Setters for DataTracker
     * ------------------------------------------------------------------------- */


    public boolean isTamed() {
        return this.dataTracker.get(TAMED);
    }

    public void setTamed(boolean tamed) {
        this.dataTracker.set(TAMED, tamed);
    }

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

    public int getMood() {
        return this.dataTracker.get(MOOD);
    }
    public void setMood(int mood) {
        this.dataTracker.set(MOOD, mood);
    }

    // For reading/writing extra data to NBT (so it persists between world loads)
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("HamsterVariant", getVariant());
        nbt.putInt("HamsterMood", getMood());
        nbt.putBoolean("IsSleeping", isSleeping());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setVariant(nbt.getInt("HamsterVariant"));
        setMood(nbt.getInt("HamsterMood"));
        setSleeping(nbt.getBoolean("IsSleeping"));
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
        // Basic example goals:
        this.goalSelector.add(0, new SwimGoal(this));

        // Sleep in the daytime if wild or commanded to stay
        this.goalSelector.add(1, new HamsterSleepGoal(this));

        // If tamed, follow the owner
        this.goalSelector.add(2, new FollowOwnerGoal(this, 1.0D, 4.0F, 2.0F));

        // Wander around
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.75D));

        // Tempt if the player is holding the taming food
        this.goalSelector.add(4, new TemptGoal(this, 1.0D,
                // The items that cause the hamster to be tempted:
                stack -> stack.isOf(ModItems.SLICED_CUCUMBER), false));

        // Look at player occasionally
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));

        // (Optionally) some flee logic if not tamed
        this.goalSelector.add(7, new FleeEntityGoal<>(this, PlayerEntity.class,
                // 6 block radius
                6.0F,
                1.0D, // walk speed
                1.2D, // sprint speed
                living -> !this.isTamed() // condition: only flee if not tamed
        ));
    }


    /* -------------------------------------------------------------------------
     *                        Taming & Interaction
     * ------------------------------------------------------------------------- */

    // This is called when a player right-clicks on the hamster with an item in hand.
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!this.isTamed()) {
            // Attempt to tame with SLICED_CUCUMBER
            if (stack.isOf(ModItems.SLICED_CUCUMBER)) {
                this.getWorld().playSound(null, this.getBlockPos(), ModSounds.HAMSTER_BEG1, // pick random or just one
                        net.minecraft.sound.SoundCategory.NEUTRAL, 1.0F, 1.0F);
                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
                // 25% chance to tame per feed for example
                if (this.random.nextInt(4) == 0) {
                    this.setOwner(player);
                    this.navigation.stop();
                    this.getWorld().sendEntityStatus(this, (byte) 7); // heart particles
                } else {
                    this.getWorld().sendEntityStatus(this, (byte) 6); // smoke/fail particles
                }
                return ActionResult.SUCCESS;
            }
        } else {
            // Already tamed => we can do other interactions
            if (player.isSneaking()) {
                // If sneaking => toggle "stay" = sleep
                boolean newState = !this.isSleeping();
                this.setSleeping(newState);
                if (newState) {
                    this.getWorld().playSound(null, this.getBlockPos(), ModSounds.HAMSTER_SLEEP1,
                            net.minecraft.sound.SoundCategory.NEUTRAL, 1.0F, 1.0F);
                }
                return ActionResult.SUCCESS;
            } else {
                // Possibly some other tamed interaction, like opening cheek pouch inventory
                // Or placing on shoulder, etc.
            }
        }
        return super.interactMob(player, hand);
    }

    // TameableEntity helper
    public void setOwner(PlayerEntity player) {
        super.setOwner(player);
        this.setTamed(true);
        this.navigation.stop();
    }

    /* -------------------------------------------------------------------------
     *                              Mood Logic
     * ------------------------------------------------------------------------- */

    // Example method: called whenever the hamster is fed
    // You’d call this from wherever you handle feeding code
    public void onFedBy(PlayerEntity feeder, ItemStack foodStack) {
        // If it’s the same item as last time => lower mood
        if (ItemStack.areItemsEqual(foodStack, lastFoodItem)) {
            // degrade mood
            this.setMood(Math.max(MOOD_SAD, getMood() - 1));
        } else {
            // otherwise raise or reset mood
            this.setMood(MOOD_HAPPY);
        }
        this.lastFoodItem = foodStack.copy();
    }

    // You can then reference getMood() for AI or animation logic, or
    // further expand to track mood timer, degrade over time, etc.

    /* -------------------------------------------------------------------------
     *                            Sleeping Logic
     * ------------------------------------------------------------------------- */

    // We'll handle the day sleeping for WILD hamsters via a simple check in the custom goal
    // Tamed hamsters only sleep if commanded to "stay" (i.e. isSleeping=true).
    // See the "HamsterSleepGoal" below.

    /* -------------------------------------------------------------------------
     *                    Example Diamond Sniff & Creeper Detect
     * ------------------------------------------------------------------------- */

    @Override
    public void tick() {
        super.tick();

        // 1. Diamond sniff while on player’s shoulder
        if (this.hasPlayerRider()) {
            // e.g. check if diamond ore is near
            // if so => play squeaks every 30 seconds
        }

        // 2. Creeper detect if the hamster is on the player's shoulder
        // or near the player: if a creeper is targeting the player, hamster squeaks, etc.
    }

    public boolean hasPlayerRider() {
        // Quick check if the hamster is "riding" the player or on their shoulder
        // For a parrot-like approach, you'd do a custom logic or replicate parrot mechanics
        Entity vehicle = this.getVehicle();
        return vehicle instanceof PlayerEntity;
    }

    /* -------------------------------------------------------------------------
     *                    Sound Events (Ambient, Hurt, Death)
     * ------------------------------------------------------------------------- */

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isSleeping()) {
            // pick a random SLEEP_SOUNDS array element
            return ModSounds.HAMSTER_SLEEP1;
        } else {
            // pick a random from AMBIENT_SOUNDS
            return ModSounds.HAMSTER_IDLE1;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // pick random from HURT_SOUNDS
        return ModSounds.HAMSTER_HURT1;
    }

    @Override
    protected SoundEvent getDeathSound() {
        // pick random from DEATH_SOUNDS
        return ModSounds.HAMSTER_DEATH1;
    }

    /* -------------------------------------------------------------------------
     *                      Breeding & Offspring (optional)
     * ------------------------------------------------------------------------- */
    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity mate) {
        // If you want hamsters to be breedable, define the child entity
        HamsterEntity baby = ModEntities.HAMSTER.create(world);
        if (baby != null && this.isTamed()) {
            baby.setOwner((PlayerEntity) this.getOwner());
            baby.setTamed(true);
            baby.setVariant(this.getVariant());
            // Or random
        }
        return baby;
    }

    // If you want certain items to be recognized as "breeding food"
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        // e.g. cucumbers or seeds
        return stack.isOf(ModItems.SLICED_CUCUMBER);
    }

    /* -------------------------------------------------------------------------
     *                    Geckolib Animation & Controllers
     * ------------------------------------------------------------------------- */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                new AnimationController<>(this, "movementController", 5, event -> {
                    // Example logic. You can refine with your states
                    if (this.isSleeping()) {
                        return event.setAndContinue(RawAnimation.begin().thenPlay("anim_hamster_sleeping"));
                    }
                    // If moving quickly => run
                    else if (this.getVelocity().horizontalLengthSquared() > 0.02) {
                        // maybe decide walk vs. run threshold
                        double speed = this.getVelocity().horizontalLength();
                        if (speed > 0.25) {
                            return event.setAndContinue(RawAnimation.begin().thenPlay("anim_hamster_running"));
                        } else {
                            return event.setAndContinue(RawAnimation.begin().thenPlay("anim_hamster_walking"));
                        }
                    }
                    // If “begging” is flagged?
                    // else if (someBegCondition) {
                    //     return event.setAndContinue(RawAnimation.begin().thenPlay("anim_hamster_begging"));
                    // }
                    else {
                        return event.setAndContinue(RawAnimation.begin().thenPlay("anim_hamster_idle"));
                    }
                })
        );
    }

    // Required by GeoEntity
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenPlay("anim_hamster_idle");
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
