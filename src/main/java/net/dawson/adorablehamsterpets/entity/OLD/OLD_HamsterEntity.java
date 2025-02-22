//package net.dawson.adorablehamsterpets.entity.custom;
//
//import net.dawson.adorablehamsterpets.entity.ModEntities;
//import net.dawson.adorablehamsterpets.item.ModItems;
//import net.dawson.adorablehamsterpets.sound.ModSounds;
//import net.minecraft.block.Block;
//import net.minecraft.block.Blocks;
//import net.minecraft.entity.*;
//import net.minecraft.entity.ai.goal.*;
//import net.minecraft.entity.attribute.DefaultAttributeContainer;
//import net.minecraft.entity.attribute.EntityAttributes;
//import net.minecraft.entity.damage.DamageSource;
//import net.minecraft.entity.data.DataTracker;
//import net.minecraft.entity.data.TrackedData;
//import net.minecraft.entity.data.TrackedDataHandlerRegistry;
//import net.minecraft.entity.mob.MobEntity;
//import net.minecraft.entity.passive.*;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NbtCompound;
//import net.minecraft.recipe.Ingredient;
//import net.minecraft.server.world.ServerWorld;
//import net.minecraft.sound.SoundCategory;
//import net.minecraft.sound.SoundEvent;
//import net.minecraft.sound.SoundEvents;
//import net.minecraft.util.Util;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.random.Random;
//import net.minecraft.world.LocalDifficulty;
//import net.minecraft.world.ServerWorldAccess;
//import net.minecraft.world.World;
//import org.jetbrains.annotations.Nullable;
//import software.bernie.geckolib.animatable.GeoAnimatable;
//import software.bernie.geckolib.animation.AnimatableManager;
//import software.bernie.geckolib.animation.AnimationController;
//import software.bernie.geckolib.animation.RawAnimation;
//import software.bernie.geckolib.constant.DefaultAnimations;
//
//public class HamsterEntity extends TameableEntity implements GeoAnimatable {
//    public final AnimationState idleAnimationState = new AnimationState();
//    private int idleAnimationTimeout = 0;
//
//    private static final TrackedData<Integer> DATA_ID_TYPE_VARIANT =
//            DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.INTEGER);
//
//
//    public HamsterEntity(EntityType<? extends TameableEntity> entityType, World world) {
//        super(entityType, world);
//    }
//
//
//    @Override
//    protected void initGoals() {
//
//        // current functioning goals
//        this.goalSelector.add(0, new SwimGoal(this));
//        this.goalSelector.add(1, new AnimalMateGoal(this, 1.15D));
//        this.goalSelector.add(2, new TemptGoal(this, 2.25D, Ingredient.ofItems(ModItems.SLICED_CUCUMBER), false));
//        this.goalSelector.add(3, new FollowParentGoal(this, 1.1D));
//        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0D));
//        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 4.0F));
//        this.goalSelector.add(6, new LookAroundGoal(this));
//
///*  goals I might want to add (taken from a different hamster mod)
//
//        this.goalSelector.add(0, new FloatGoal(this));
//        this.goalSelector.add(1, new PanicGoal(this, 1.25D));
//        this.goalSelector.add(2, new AvoidEntityGoal<>(this, LivingEntity.class, 6.0F, 1.3D, 1.5D, livingEntity -> livingEntity instanceof Player player ? !this.isTame() && !player.isCrouching() : livingEntity.getType().is(HamstersTags.HAMSTER_AVOIDED)));
//        this.goalSelector.add(3, new SitGoal(this));
//        this.goalSelector.add(4, new BreedGoal(this, 1.0D));
//        this.goalSelector.add(5, new Hamster.HamsterTemptGoal(this, 1.0D, Ingredient.of(HamstersTags.HAMSTER_FOOD), true));
//        this.goalSelector.add(6, new FollowParentGoal(this, 1.0D));
//        this.goalSelector.add(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
//        this.goalSelector.add(8, new SleepGoal<>(this));
//        this.goalSelector.add(9, new LookAtPlayerGoal(this, Player.class, 6.0F) {
//            @Override
//            public void tick() {
//                if (Hamster.this.canUseMovementGoals()) super.tick();
//            }
//        });
//        this.goalSelector.add(10, new Hamster.HamsterLookAroundGoal(this));
//
//
//    other goals I want to look through and decide whether or not to add (taken from WolfEntity.java)
//
//        this.goalSelector.add(1, new SwimGoal(this));
//        this.goalSelector.add(1, new TameableEntity.TameableEscapeDangerGoal(1.5, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
//        this.goalSelector.add(2, new SitGoal(this));
//        this.goalSelector.add(4, new PounceAtTargetGoal(this, 0.4F));
//        this.goalSelector.add(5, new MeleeAttackGoal(this, 1.0, true));
//        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F));
//        this.goalSelector.add(7, new AnimalMateGoal(this, 1.0));
//        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
//        this.goalSelector.add(9, new WolfBegGoal(this, 8.0F));
//        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
//        this.goalSelector.add(10, new LookAroundGoal(this));
//        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
//        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
//        this.targetSelector.add(3, new RevengeGoal(this).setGroupRevenge());
//        this.targetSelector.add(4, new ActiveTargetGoal(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
//        this.targetSelector.add(5, new UntamedActiveTargetGoal(this, AnimalEntity.class, false, FOLLOW_TAMED_PREDICATE));
//        this.targetSelector.add(6, new UntamedActiveTargetGoal(this, TurtleEntity.class, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));
//        this.targetSelector.add(7, new ActiveTargetGoal(this, AbstractSkeletonEntity.class, false));
//        this.targetSelector.add(8, new UniversalAngerGoal<>(this, true));
// */
//    }
//
//    public static DefaultAttributeContainer.Builder createAttributes() {
//        return MobEntity.createMobAttributes()
//                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8)
//                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
//                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4)
//                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20)
//                .add(EntityAttributes.GENERIC_FALL_DAMAGE_MULTIPLIER, 0);
//    }
//
//
//    // REGISTERING GECKOLIB ANIMATION CONTROLLERS (GITHUB EXAMPLE)
//    private static final RawAnimation HAMSTER_BEG = RawAnimation.begin().thenPlay("animation.hamster.anim_hamster_begging");
//    private static final RawAnimation HAMSTER_IDLE = RawAnimation.begin().thenPlay("animation.hamster.anim_hamster_idle");
//    private static final RawAnimation HAMSTER_WALK = RawAnimation.begin().thenPlay("animation.hamster.anim_hamster_walking");
//    private static final RawAnimation HAMSTER_RUN = RawAnimation.begin().thenPlay("animation.hamster.anim_hamster_running");
//    private static final RawAnimation HAMSTER_SLEEP = RawAnimation.begin().thenPlay("animation.hamster.anim_hamster_sleeping");
//    private static final RawAnimation HAMSTER_SQUISH = RawAnimation.begin().thenPlay("animation.hamster.anim_hamster_squish");
//    private static final RawAnimation HAMSTER_SQUISHED = RawAnimation.begin().thenPlay("animation.hamster.anim_hamster_squished");
//    private static final RawAnimation HAMSTER_UNSQUISH = RawAnimation.begin().thenPlay("animation.hamster.anim_hamster_unsquish");
//
//    @Override
//    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//        controllers.add(
//                new AnimationController<>(this, "begging", 10, state -> state.setAndContinue(HAMSTER_BEG)),
//                new AnimationController<>(this, "idle", 10, state -> state.setAndContinue(HAMSTER_IDLE)),
//                new AnimationController<>(this, "walk", 10, state -> state.setAndContinue(HAMSTER_WALK)),
//                new AnimationController<>(this, "run", 10, state -> state.setAndContinue(HAMSTER_RUN)),
//                new AnimationController<>(this, "sleep", 10, state -> state.setAndContinue(HAMSTER_SLEEP)),
//                new AnimationController<>(this, "squish", 10, state -> state.setAndContinue(HAMSTER_SQUISH)),
//                new AnimationController<>(this, "squished", 10, state -> state.setAndContinue(HAMSTER_SQUISHED)),
//                new AnimationController<>(this, "unsquish", 10, state -> state.setAndContinue(HAMSTER_UNSQUISH))
//        );
//    }
//
//
//    //this says "if idle animation timeout is zero or lower, please reset it to 40"
//    private void setupAnimationStates() {
//        if (this.idleAnimationTimeout <= 0) {
//            this.idleAnimationTimeout = 40;
//            this.idleAnimationState.start(this.age);
//        } else {
//            --this.idleAnimationTimeout;
//        }
//    }
//
//
//    //this just says hey if we are on the client, please call the setupAnimationStates method
//    @Override
//    public void tick() {
//        super.tick();
//
//        if (this.getWorld().isClient()) {
//            this.setupAnimationStates();
//        }
//    }
//
//    @Override
//    public boolean isBreedingItem(ItemStack stack) {
//        return stack.isOf(ModItems.SLICED_CUCUMBER);
//    }
//
//    @Nullable
//    @Override
//    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
//        HamsterEntity baby = ModEntities.HAMSTER.create(world);
//        HamsterVariant variant = Util.getRandom(HamsterVariant.values(), this.random);
//        baby.setVariant(variant);
//        return baby;
//    }
//
//    /* VARIANT */
//    @Override
//    protected void initDataTracker(DataTracker.Builder builder) {
//        super.initDataTracker(builder);
//        builder.add(DATA_ID_TYPE_VARIANT, 0);
//    }
//
//    public HamsterVariant getVariant() {
//        return HamsterVariant.byId(this.getTypeVariant() & 255);
//    }
//
//    private int getTypeVariant() {
//        return this.dataTracker.get(DATA_ID_TYPE_VARIANT);
//    }
//
//    private void setVariant(HamsterVariant variant) {
//        this.dataTracker.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
//    }
//
//    @Override
//    public void writeCustomDataToNbt(NbtCompound nbt) {
//        super.writeCustomDataToNbt(nbt);
//        nbt.putInt("Variant", this.getTypeVariant());
//    }
//
//    @Override
//    public void readCustomDataFromNbt(NbtCompound nbt) {
//        super.readCustomDataFromNbt(nbt);
//        this.dataTracker.set(DATA_ID_TYPE_VARIANT, nbt.getInt("Variant"));
//    }
//
//    @Override
//    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
//                                 @Nullable EntityData entityData) {
//        HamsterVariant variant = Util.getRandom(HamsterVariant.values(), this.random);
//        setVariant(variant);
//        return super.initialize(world, difficulty, spawnReason, entityData);
//    }
//
//    /**
//     Rabbit-like spawn check: Hamsters can spawn on grass, snow, sand, or red sand,
//     provided there's enough light (e.g. > 8). This mirrors vanilla Rabbit logic
//     and ensures they can spawn in deserts/badlands.
//     */
//
//    public static boolean canSpawnHamster(
//            EntityType<HamsterEntity> entityType,
//            ServerWorldAccess world,
//            SpawnReason spawnReason,
//            BlockPos pos,
//            Random random
//    ) {
//        Block blockBelow = world.getBlockState(pos.down()).getBlock();
//
//        boolean validBlock =
//                blockBelow == Blocks.GRASS_BLOCK
//                        || blockBelow == Blocks.RED_SAND
//                        || blockBelow == Blocks.TERRACOTTA
//                        || blockBelow == Blocks.WHITE_TERRACOTTA
//                        || blockBelow == Blocks.ORANGE_TERRACOTTA
//                        || blockBelow == Blocks.MAGENTA_TERRACOTTA
//                        || blockBelow == Blocks.LIGHT_BLUE_TERRACOTTA
//                        || blockBelow == Blocks.YELLOW_TERRACOTTA
//                        || blockBelow == Blocks.LIME_TERRACOTTA
//                        || blockBelow == Blocks.PINK_TERRACOTTA
//                        || blockBelow == Blocks.GRAY_TERRACOTTA
//                        || blockBelow == Blocks.LIGHT_GRAY_TERRACOTTA
//                        || blockBelow == Blocks.CYAN_TERRACOTTA
//                        || blockBelow == Blocks.PURPLE_TERRACOTTA
//                        || blockBelow == Blocks.BLUE_TERRACOTTA
//                        || blockBelow == Blocks.BROWN_TERRACOTTA
//                        || blockBelow == Blocks.GREEN_TERRACOTTA
//                        || blockBelow == Blocks.RED_TERRACOTTA
//                        || blockBelow == Blocks.BLACK_TERRACOTTA;
//
//        // Require a decent light level (like rabbits do).
//        // If you want them to spawn in darker areas, lower this check.
//        boolean enoughLight = world.getBaseLightLevel(pos, 0) > 6;
//
//        return validBlock && enoughLight;
//    }
//
//    /**
//
//     SOUNDS
//
//     Here, we create arrays of SoundEvents for each category
//     (idle, hurt, death, etc.) so that we can randomly pick
//     which sound to play every time.
//
//     */
//
//    // Idle (Ambient) sounds
//    private static final SoundEvent[] AMBIENT_SOUNDS = {
//            ModSounds.HAMSTER_IDLE1,
//            ModSounds.HAMSTER_IDLE2,
//            ModSounds.HAMSTER_IDLE3,
//            ModSounds.HAMSTER_IDLE4,
//            ModSounds.HAMSTER_IDLE5
//    };
//
//    // Hurt sounds
//    private static final SoundEvent[] HURT_SOUNDS = {
//            ModSounds.HAMSTER_HURT1,
//            ModSounds.HAMSTER_HURT2,
//            ModSounds.HAMSTER_HURT3,
//            ModSounds.HAMSTER_HURT4,
//            ModSounds.HAMSTER_HURT5
//    };
//
//    // Death sounds
//    private static final SoundEvent[] DEATH_SOUNDS = {
//            ModSounds.HAMSTER_DEATH1,
//            ModSounds.HAMSTER_DEATH2,
//            ModSounds.HAMSTER_DEATH3,
//            ModSounds.HAMSTER_DEATH4,
//            ModSounds.HAMSTER_DEATH5
//    };
//
//    // Beg sounds
//    private static final SoundEvent[] BEG_SOUNDS = {
//            ModSounds.HAMSTER_BEG1,
//            ModSounds.HAMSTER_BEG2,
//            ModSounds.HAMSTER_BEG3,
//            ModSounds.HAMSTER_BEG4
//    };
//
//    // Sleep sounds
//    private static final SoundEvent[] SLEEP_SOUNDS = {
//            ModSounds.HAMSTER_SLEEP1,
//            ModSounds.HAMSTER_SLEEP2,
//            ModSounds.HAMSTER_SLEEP3,
//            ModSounds.HAMSTER_SLEEP4
//    };
//
//    // Sniff sounds
//    private static final SoundEvent[] SNIFF_SOUNDS = {
//            ModSounds.HAMSTER_SNIFF1,
//            ModSounds.HAMSTER_SNIFF2
//    };
//
//    // Creeper detect sounds
//    private static final SoundEvent[] CREEPER_DETECT_SOUNDS = {
//            ModSounds.HAMSTER_CREEPER_DETECT1,
//            ModSounds.HAMSTER_CREEPER_DETECT2,
//            ModSounds.HAMSTER_CREEPER_DETECT3
//    };
//
//    // Celebrate sounds
//    private static final SoundEvent[] CELEBRATE_SOUNDS = {
//            ModSounds.HAMSTER_CELEBRATE1,
//            ModSounds.HAMSTER_CELEBRATE2,
//            ModSounds.HAMSTER_CELEBRATE3
//    };
//
//
//    /**
//     * Helper method to pick a random sound from any given array.
//     *
//     * We use this.random to select the index.
//     * If your random field is different, adjust accordingly.
//     */
//    private SoundEvent getRandomSound(SoundEvent[] sounds) {
//        return sounds[this.random.nextInt(sounds.length)];
//    }
//
//    /**
//     * Vanilla overrides.
//     * These methods are called by the game engine at the relevant times,
//     * and we supply a random sound from each array whenever needed.
//     */
//
//    @Override
//    @Nullable
//    protected SoundEvent getAmbientSound() {
//        // Random idle sound
//        return getRandomSound(AMBIENT_SOUNDS);
//    }
//
//    @Override
//    @Nullable
//    protected SoundEvent getHurtSound(DamageSource source) {
//        // Random hurt sound
//        return getRandomSound(HURT_SOUNDS);
//    }
//
//    @Override
//    @Nullable
//    protected SoundEvent getDeathSound() {
//        // Random death sound
//        return getRandomSound(DEATH_SOUNDS);
//    }
//}
//
//
