package net.dawson.adorablehamsterpets.entity.custom;

import net.dawson.adorablehamsterpets.entity.ModEntities;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class HamsterEntity extends TameableEntity {
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;


    public HamsterEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    protected void initGoals() {

        //from Tutorial
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new AnimalMateGoal(this, 1.15D));
        this.goalSelector.add(2, new TemptGoal(this, 2.25D, Ingredient.ofItems(ModItems.SLICED_CUCUMBER), false));
        this.goalSelector.add(3, new FollowParentGoal(this, 1.1D));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 4.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));

        //FROM old hamster mod
//        this.goalSelector.add(0, new FloatGoal(this));
//        this.goalSelector.add(1, new PanicGoal(this, 1.25D));
//        this.goalSelector.add(2, new AvoidEntityGoal<>(this, LivingEntity.class, 6.0F, 1.3D, 1.5D, livingEntity -> livingEntity instanceof Player player ? !this.isTame() && !player.isCrouching() : livingEntity.getType().is(HamstersTags.HAMSTER_AVOIDED)));
//        this.goalSelector.add(3, new SitGoal(this));
//        this.goalSelector.add(4, new BreedGoal(this, 1.0D));
//        this.goalSelector.add(5, new Hamster.HamsterTemptGoal(this, 1.0D, Ingredient.of(HamstersTags.HAMSTER_FOOD), true));
//        this.goalSelector.add(6, new FollowParentGoal(this, 1.0D));
//        this.goalSelector.add(7, new Hamster.HamsterGoToWheelGoal(this, 1.2D, 8));
//        this.goalSelector.add(7, new Hamster.HamsterGoToBottleGoal(this, 1.2D, 8));
//        this.goalSelector.add(7, new Hamster.HamsterGoToBowlGoal(this, 1.2D, 8));
//        this.goalSelector.add(8, new Hamster.HamsterDismountGoal(this));
//        this.goalSelector.add(9, new WaterAvoidingRandomStrollGoal(this, 1.0D));
//        this.goalSelector.add(10, new SleepGoal<>(this));
//        this.goalSelector.add(11, new LookAtPlayerGoal(this, Player.class, 6.0F) {
//            @Override
//            public void tick() {
//                if (Hamster.this.canUseMovementGoals()) super.tick();
//            }
//        });
//        this.goalSelector.add(12, new Hamster.HamsterLookAroundGoal(this));


        //FROM WolfEntity.java
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
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20)
                .add(EntityAttributes.GENERIC_FALL_DAMAGE_MULTIPLIER, 0);
    }


    //this says "if idle animation timeout is zero or lower, please reset it to 40"
    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 40;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }
    }


    //this just says hey if we are on the client, please call the setupAnimationStates method
    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient()) {
            this.setupAnimationStates();
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(ModItems.SLICED_CUCUMBER);
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.HAMSTER.create(world);
    }
}
