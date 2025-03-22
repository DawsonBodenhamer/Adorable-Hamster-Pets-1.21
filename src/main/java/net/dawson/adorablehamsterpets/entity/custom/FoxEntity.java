////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by FernFlower decompiler)
////
//
//package net.minecraft.entity.passive;
//
//import com.google.common.collect.Lists;
//import java.util.EnumSet;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.function.IntFunction;
//import java.util.function.Predicate;
//import net.minecraft.advancement.criterion.Criteria;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.block.CaveVines;
//import net.minecraft.block.SweetBerryBushBlock;
//import net.minecraft.component.DataComponentTypes;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityData;
//import net.minecraft.entity.EntityDimensions;
//import net.minecraft.entity.EntityPose;
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.EquipmentSlot;
//import net.minecraft.entity.ExperienceOrbEntity;
//import net.minecraft.entity.ItemEntity;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.SpawnReason;
//import net.minecraft.entity.VariantHolder;
//import net.minecraft.entity.ai.TargetPredicate;
//import net.minecraft.entity.ai.control.LookControl;
//import net.minecraft.entity.ai.control.MoveControl;
//import net.minecraft.entity.ai.goal.ActiveTargetGoal;
//import net.minecraft.entity.ai.goal.AnimalMateGoal;
//import net.minecraft.entity.ai.goal.DiveJumpingGoal;
//import net.minecraft.entity.ai.goal.EscapeDangerGoal;
//import net.minecraft.entity.ai.goal.EscapeSunlightGoal;
//import net.minecraft.entity.ai.goal.FleeEntityGoal;
//import net.minecraft.entity.ai.goal.Goal;
//import net.minecraft.entity.ai.goal.MeleeAttackGoal;
//import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
//import net.minecraft.entity.ai.goal.PounceAtTargetGoal;
//import net.minecraft.entity.ai.goal.PowderSnowJumpGoal;
//import net.minecraft.entity.ai.goal.SwimGoal;
//import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
//import net.minecraft.entity.ai.goal.Goal.Control;
//import net.minecraft.entity.ai.pathing.PathNodeType;
//import net.minecraft.entity.attribute.DefaultAttributeContainer;
//import net.minecraft.entity.attribute.EntityAttributes;
//import net.minecraft.entity.damage.DamageSource;
//import net.minecraft.entity.data.DataTracker;
//import net.minecraft.entity.data.TrackedData;
//import net.minecraft.entity.data.TrackedDataHandlerRegistry;
//import net.minecraft.entity.mob.HostileEntity;
//import net.minecraft.entity.mob.MobEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.Items;
//import net.minecraft.nbt.NbtCompound;
//import net.minecraft.nbt.NbtElement;
//import net.minecraft.nbt.NbtHelper;
//import net.minecraft.nbt.NbtList;
//import net.minecraft.particle.ItemStackParticleEffect;
//import net.minecraft.particle.ParticleTypes;
//import net.minecraft.predicate.entity.EntityPredicates;
//import net.minecraft.registry.entry.RegistryEntry;
//import net.minecraft.registry.tag.BiomeTags;
//import net.minecraft.registry.tag.BlockTags;
//import net.minecraft.registry.tag.FluidTags;
//import net.minecraft.registry.tag.ItemTags;
//import net.minecraft.server.network.ServerPlayerEntity;
//import net.minecraft.server.world.ServerWorld;
//import net.minecraft.sound.SoundEvent;
//import net.minecraft.sound.SoundEvents;
//import net.minecraft.stat.Stats;
//import net.minecraft.util.Hand;
//import net.minecraft.util.StringIdentifiable;
//import net.minecraft.util.function.ValueLists;
//import net.minecraft.util.function.ValueLists.OutOfBoundsHandling;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.util.math.random.Random;
//import net.minecraft.world.GameRules;
//import net.minecraft.world.LocalDifficulty;
//import net.minecraft.world.ServerWorldAccess;
//import net.minecraft.world.World;
//import net.minecraft.world.WorldAccess;
//import net.minecraft.world.WorldView;
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.event.GameEvent;
//import net.minecraft.world.event.GameEvent.Emitter;
//import org.jetbrains.annotations.Nullable;
//
//public class FoxEntity extends AnimalEntity implements VariantHolder<Type> {
//    private static final TrackedData<Integer> TYPE;
//    private static final TrackedData<Byte> FOX_FLAGS;
//    private static final int SITTING_FLAG = 1;
//    public static final int CROUCHING_FLAG = 4;
//    public static final int ROLLING_HEAD_FLAG = 8;
//    public static final int CHASING_FLAG = 16;
//    private static final int SLEEPING_FLAG = 32;
//    private static final int WALKING_FLAG = 64;
//    private static final int AGGRESSIVE_FLAG = 128;
//    private static final TrackedData<Optional<UUID>> OWNER;
//    private static final TrackedData<Optional<UUID>> OTHER_TRUSTED;
//    static final Predicate<ItemEntity> PICKABLE_DROP_FILTER;
//    private static final Predicate<Entity> JUST_ATTACKED_SOMETHING_FILTER;
//    static final Predicate<Entity> CHICKEN_AND_RABBIT_FILTER;
//    private static final Predicate<Entity> NOTICEABLE_PLAYER_FILTER;
//    private static final int EATING_DURATION = 600;
//    private static final EntityDimensions BABY_BASE_DIMENSIONS;
//    private Goal followChickenAndRabbitGoal;
//    private Goal followBabyTurtleGoal;
//    private Goal followFishGoal;
//    private float headRollProgress;
//    private float lastHeadRollProgress;
//    float extraRollingHeight;
//    float lastExtraRollingHeight;
//    private int eatingTime;
//
//    public FoxEntity(EntityType<? extends FoxEntity> entityType, World world) {
//        super(entityType, world);
//        this.lookControl = new FoxLookControl();
//        this.moveControl = new FoxMoveControl();
//        this.setPathfindingPenalty(PathNodeType.DANGER_OTHER, 0.0F);
//        this.setPathfindingPenalty(PathNodeType.DAMAGE_OTHER, 0.0F);
//        this.setCanPickUpLoot(true);
//    }
//
//    protected void initDataTracker(DataTracker.Builder builder) {
//        super.initDataTracker(builder);
//        builder.add(OWNER, Optional.empty());
//        builder.add(OTHER_TRUSTED, Optional.empty());
//        builder.add(TYPE, 0);
//        builder.add(FOX_FLAGS, (byte)0);
//    }
//
//    protected void initGoals() {
//        this.followChickenAndRabbitGoal = new ActiveTargetGoal(this, AnimalEntity.class, 10, false, false, (entity) -> entity instanceof ChickenEntity || entity instanceof RabbitEntity);
//        this.followBabyTurtleGoal = new ActiveTargetGoal(this, TurtleEntity.class, 10, false, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER);
//        this.followFishGoal = new ActiveTargetGoal(this, FishEntity.class, 20, false, false, (entity) -> entity instanceof SchoolingFishEntity);
//        this.goalSelector.add(0, new FoxSwimGoal());
//        this.goalSelector.add(0, new PowderSnowJumpGoal(this, this.getWorld()));
//        this.goalSelector.add(1, new StopWanderingGoal());
//        this.goalSelector.add(2, new EscapeWhenNotAggressiveGoal(2.2));
//        this.goalSelector.add(3, new MateGoal((double)1.0F));
//        this.goalSelector.add(4, new FleeEntityGoal(this, PlayerEntity.class, 16.0F, 1.6, 1.4, (entity) -> NOTICEABLE_PLAYER_FILTER.test(entity) && !this.canTrust(entity.getUuid()) && !this.isAggressive()));
//        this.goalSelector.add(4, new FleeEntityGoal(this, WolfEntity.class, 8.0F, 1.6, 1.4, (entity) -> !((WolfEntity)entity).isTamed() && !this.isAggressive()));
//        this.goalSelector.add(4, new FleeEntityGoal(this, PolarBearEntity.class, 8.0F, 1.6, 1.4, (entity) -> !this.isAggressive()));
//        this.goalSelector.add(5, new MoveToHuntGoal());
//        this.goalSelector.add(6, new JumpChasingGoal());
//        this.goalSelector.add(6, new AvoidDaylightGoal((double)1.25F));
//        this.goalSelector.add(7, new AttackGoal((double)1.2F, true));
//        this.goalSelector.add(7, new DelayedCalmDownGoal());
//        this.goalSelector.add(8, new FollowParentGoal(this, (double)1.25F));
//        this.goalSelector.add(9, new GoToVillageGoal(32, 200));
//        this.goalSelector.add(10, new EatBerriesGoal((double)1.2F, 12, 1));
//        this.goalSelector.add(10, new PounceAtTargetGoal(this, 0.4F));
//        this.goalSelector.add(11, new WanderAroundFarGoal(this, (double)1.0F));
//        this.goalSelector.add(11, new PickupItemGoal());
//        this.goalSelector.add(12, new LookAtEntityGoal(this, PlayerEntity.class, 24.0F));
//        this.goalSelector.add(13, new SitDownAndLookAroundGoal());
//        this.targetSelector.add(3, new DefendFriendGoal(LivingEntity.class, false, false, (entity) -> JUST_ATTACKED_SOMETHING_FILTER.test(entity) && !this.canTrust(entity.getUuid())));
//    }
//
//    public SoundEvent getEatSound(ItemStack stack) {
//        return SoundEvents.ENTITY_FOX_EAT;
//    }
//
//    public void tickMovement() {
//        if (!this.getWorld().isClient && this.isAlive() && this.canMoveVoluntarily()) {
//            ++this.eatingTime;
//            ItemStack itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
//            if (this.canEat(itemStack)) {
//                if (this.eatingTime > 600) {
//                    ItemStack itemStack2 = itemStack.finishUsing(this.getWorld(), this);
//                    if (!itemStack2.isEmpty()) {
//                        this.equipStack(EquipmentSlot.MAINHAND, itemStack2);
//                    }
//
//                    this.eatingTime = 0;
//                } else if (this.eatingTime > 560 && this.random.nextFloat() < 0.1F) {
//                    this.playSound(this.getEatSound(itemStack), 1.0F, 1.0F);
//                    this.getWorld().sendEntityStatus(this, (byte)45);
//                }
//            }
//
//            LivingEntity livingEntity = this.getTarget();
//            if (livingEntity == null || !livingEntity.isAlive()) {
//                this.setCrouching(false);
//                this.setRollingHead(false);
//            }
//        }
//
//        if (this.isSleeping() || this.isImmobile()) {
//            this.jumping = false;
//            this.sidewaysSpeed = 0.0F;
//            this.forwardSpeed = 0.0F;
//        }
//
//        super.tickMovement();
//        if (this.isAggressive() && this.random.nextFloat() < 0.05F) {
//            this.playSound(SoundEvents.ENTITY_FOX_AGGRO, 1.0F, 1.0F);
//        }
//
//    }
//
//    protected boolean isImmobile() {
//        return this.isDead();
//    }
//
//    private boolean canEat(ItemStack stack) {
//        return stack.contains(DataComponentTypes.FOOD) && this.getTarget() == null && this.isOnGround() && !this.isSleeping();
//    }
//
//    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
//        if (random.nextFloat() < 0.2F) {
//            float f = random.nextFloat();
//            ItemStack itemStack;
//            if (f < 0.05F) {
//                itemStack = new ItemStack(Items.EMERALD);
//            } else if (f < 0.2F) {
//                itemStack = new ItemStack(Items.EGG);
//            } else if (f < 0.4F) {
//                itemStack = random.nextBoolean() ? new ItemStack(Items.RABBIT_FOOT) : new ItemStack(Items.RABBIT_HIDE);
//            } else if (f < 0.6F) {
//                itemStack = new ItemStack(Items.WHEAT);
//            } else if (f < 0.8F) {
//                itemStack = new ItemStack(Items.LEATHER);
//            } else {
//                itemStack = new ItemStack(Items.FEATHER);
//            }
//
//            this.equipStack(EquipmentSlot.MAINHAND, itemStack);
//        }
//
//    }
//
//    public void handleStatus(byte status) {
//        if (status == 45) {
//            ItemStack itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
//            if (!itemStack.isEmpty()) {
//                for(int i = 0; i < 8; ++i) {
//                    Vec3d vec3d = (new Vec3d(((double)this.random.nextFloat() - (double)0.5F) * 0.1, Math.random() * 0.1 + 0.1, (double)0.0F)).rotateX(-this.getPitch() * ((float)Math.PI / 180F)).rotateY(-this.getYaw() * ((float)Math.PI / 180F));
//                    this.getWorld().addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack), this.getX() + this.getRotationVector().x / (double)2.0F, this.getY(), this.getZ() + this.getRotationVector().z / (double)2.0F, vec3d.x, vec3d.y + 0.05, vec3d.z);
//                }
//            }
//        } else {
//            super.handleStatus(status);
//        }
//
//    }
//
//    public static DefaultAttributeContainer.Builder createFoxAttributes() {
//        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, (double)0.3F).add(EntityAttributes.GENERIC_MAX_HEALTH, (double)10.0F).add(EntityAttributes.GENERIC_FOLLOW_RANGE, (double)32.0F).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, (double)2.0F).add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, (double)5.0F);
//    }
//
//    @Nullable
//    public FoxEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
//        FoxEntity foxEntity = (FoxEntity)EntityType.FOX.create(serverWorld);
//        if (foxEntity != null) {
//            foxEntity.setVariant(this.random.nextBoolean() ? this.getVariant() : ((FoxEntity)passiveEntity).getVariant());
//        }
//
//        return foxEntity;
//    }
//
//    public static boolean canSpawn(EntityType<FoxEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
//        return world.getBlockState(pos.down()).isIn(BlockTags.FOXES_SPAWNABLE_ON) && isLightLevelValidForNaturalSpawn(world, pos);
//    }
//
//    @Nullable
//    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
//        RegistryEntry<Biome> registryEntry = world.getBiome(this.getBlockPos());
//        Type type = FoxEntity.Type.fromBiome(registryEntry);
//        boolean bl = false;
//        if (entityData instanceof FoxData foxData) {
//            type = foxData.type;
//            if (foxData.getSpawnedCount() >= 2) {
//                bl = true;
//            }
//        } else {
//            entityData = new FoxData(type);
//        }
//
//        this.setVariant(type);
//        if (bl) {
//            this.setBreedingAge(-24000);
//        }
//
//        if (world instanceof ServerWorld) {
//            this.addTypeSpecificGoals();
//        }
//
//        this.initEquipment(world.getRandom(), difficulty);
//        return super.initialize(world, difficulty, spawnReason, entityData);
//    }
//
//    private void addTypeSpecificGoals() {
//        if (this.getVariant() == FoxEntity.Type.RED) {
//            this.targetSelector.add(4, this.followChickenAndRabbitGoal);
//            this.targetSelector.add(4, this.followBabyTurtleGoal);
//            this.targetSelector.add(6, this.followFishGoal);
//        } else {
//            this.targetSelector.add(4, this.followFishGoal);
//            this.targetSelector.add(6, this.followChickenAndRabbitGoal);
//            this.targetSelector.add(6, this.followBabyTurtleGoal);
//        }
//
//    }
//
//    protected void eat(PlayerEntity player, Hand hand, ItemStack stack) {
//        if (this.isBreedingItem(stack)) {
//            this.playSound(this.getEatSound(stack), 1.0F, 1.0F);
//        }
//
//        super.eat(player, hand, stack);
//    }
//
//    public EntityDimensions getBaseDimensions(EntityPose pose) {
//        return this.isBaby() ? BABY_BASE_DIMENSIONS : super.getBaseDimensions(pose);
//    }
//
//    public Type getVariant() {
//        return FoxEntity.Type.fromId((Integer)this.dataTracker.get(TYPE));
//    }
//
//    public void setVariant(Type type) {
//        this.dataTracker.set(TYPE, type.getId());
//    }
//
//    List<UUID> getTrustedUuids() {
//        List<UUID> list = Lists.newArrayList();
//        list.add((UUID)((Optional)this.dataTracker.get(OWNER)).orElse((Object)null));
//        list.add((UUID)((Optional)this.dataTracker.get(OTHER_TRUSTED)).orElse((Object)null));
//        return list;
//    }
//
//    void addTrustedUuid(@Nullable UUID uuid) {
//        if (((Optional)this.dataTracker.get(OWNER)).isPresent()) {
//            this.dataTracker.set(OTHER_TRUSTED, Optional.ofNullable(uuid));
//        } else {
//            this.dataTracker.set(OWNER, Optional.ofNullable(uuid));
//        }
//
//    }
//
//    public void writeCustomDataToNbt(NbtCompound nbt) {
//        super.writeCustomDataToNbt(nbt);
//        List<UUID> list = this.getTrustedUuids();
//        NbtList nbtList = new NbtList();
//
//        for(UUID uUID : list) {
//            if (uUID != null) {
//                nbtList.add(NbtHelper.fromUuid(uUID));
//            }
//        }
//
//        nbt.put("Trusted", nbtList);
//        nbt.putBoolean("Sleeping", this.isSleeping());
//        nbt.putString("Type", this.getVariant().asString());
//        nbt.putBoolean("Sitting", this.isSitting());
//        nbt.putBoolean("Crouching", this.isInSneakingPose());
//    }
//
//    public void readCustomDataFromNbt(NbtCompound nbt) {
//        super.readCustomDataFromNbt(nbt);
//
//        for(NbtElement nbtElement : nbt.getList("Trusted", 11)) {
//            this.addTrustedUuid(NbtHelper.toUuid(nbtElement));
//        }
//
//        this.setSleeping(nbt.getBoolean("Sleeping"));
//        this.setVariant(FoxEntity.Type.byName(nbt.getString("Type")));
//        this.setSitting(nbt.getBoolean("Sitting"));
//        this.setCrouching(nbt.getBoolean("Crouching"));
//        if (this.getWorld() instanceof ServerWorld) {
//            this.addTypeSpecificGoals();
//        }
//
//    }
//
//    public boolean isSitting() {
//        return this.getFoxFlag(1);
//    }
//
//    public void setSitting(boolean sitting) {
//        this.setFoxFlag(1, sitting);
//    }
//
//    public boolean isWalking() {
//        return this.getFoxFlag(64);
//    }
//
//    void setWalking(boolean walking) {
//        this.setFoxFlag(64, walking);
//    }
//
//    boolean isAggressive() {
//        return this.getFoxFlag(128);
//    }
//
//    void setAggressive(boolean aggressive) {
//        this.setFoxFlag(128, aggressive);
//    }
//
//    public boolean isSleeping() {
//        return this.getFoxFlag(32);
//    }
//
//    void setSleeping(boolean sleeping) {
//        this.setFoxFlag(32, sleeping);
//    }
//
//    private void setFoxFlag(int mask, boolean value) {
//        if (value) {
//            this.dataTracker.set(FOX_FLAGS, (byte)((Byte)this.dataTracker.get(FOX_FLAGS) | mask));
//        } else {
//            this.dataTracker.set(FOX_FLAGS, (byte)((Byte)this.dataTracker.get(FOX_FLAGS) & ~mask));
//        }
//
//    }
//
//    private boolean getFoxFlag(int bitmask) {
//        return ((Byte)this.dataTracker.get(FOX_FLAGS) & bitmask) != 0;
//    }
//
//    public boolean canEquip(ItemStack stack) {
//        EquipmentSlot equipmentSlot = this.getPreferredEquipmentSlot(stack);
//        if (!this.getEquippedStack(equipmentSlot).isEmpty()) {
//            return false;
//        } else {
//            return equipmentSlot == EquipmentSlot.MAINHAND && super.canEquip(stack);
//        }
//    }
//
//    public boolean canPickupItem(ItemStack stack) {
//        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
//        return itemStack.isEmpty() || this.eatingTime > 0 && stack.contains(DataComponentTypes.FOOD) && !itemStack.contains(DataComponentTypes.FOOD);
//    }
//
//    private void spit(ItemStack stack) {
//        if (!stack.isEmpty() && !this.getWorld().isClient) {
//            ItemEntity itemEntity = new ItemEntity(this.getWorld(), this.getX() + this.getRotationVector().x, this.getY() + (double)1.0F, this.getZ() + this.getRotationVector().z, stack);
//            itemEntity.setPickupDelay(40);
//            itemEntity.setThrower(this);
//            this.playSound(SoundEvents.ENTITY_FOX_SPIT, 1.0F, 1.0F);
//            this.getWorld().spawnEntity(itemEntity);
//        }
//    }
//
//    private void dropItem(ItemStack stack) {
//        ItemEntity itemEntity = new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), stack);
//        this.getWorld().spawnEntity(itemEntity);
//    }
//
//    protected void loot(ItemEntity item) {
//        ItemStack itemStack = item.getStack();
//        if (this.canPickupItem(itemStack)) {
//            int i = itemStack.getCount();
//            if (i > 1) {
//                this.dropItem(itemStack.split(i - 1));
//            }
//
//            this.spit(this.getEquippedStack(EquipmentSlot.MAINHAND));
//            this.triggerItemPickedUpByEntityCriteria(item);
//            this.equipStack(EquipmentSlot.MAINHAND, itemStack.split(1));
//            this.updateDropChances(EquipmentSlot.MAINHAND);
//            this.sendPickup(item, itemStack.getCount());
//            item.discard();
//            this.eatingTime = 0;
//        }
//
//    }
//
//    public void tick() {
//        super.tick();
//        if (this.canMoveVoluntarily()) {
//            boolean bl = this.isTouchingWater();
//            if (bl || this.getTarget() != null || this.getWorld().isThundering()) {
//                this.stopSleeping();
//            }
//
//            if (bl || this.isSleeping()) {
//                this.setSitting(false);
//            }
//
//            if (this.isWalking() && this.getWorld().random.nextFloat() < 0.2F) {
//                BlockPos blockPos = this.getBlockPos();
//                BlockState blockState = this.getWorld().getBlockState(blockPos);
//                this.getWorld().syncWorldEvent(2001, blockPos, Block.getRawIdFromState(blockState));
//            }
//        }
//
//        this.lastHeadRollProgress = this.headRollProgress;
//        if (this.isRollingHead()) {
//            this.headRollProgress += (1.0F - this.headRollProgress) * 0.4F;
//        } else {
//            this.headRollProgress += (0.0F - this.headRollProgress) * 0.4F;
//        }
//
//        this.lastExtraRollingHeight = this.extraRollingHeight;
//        if (this.isInSneakingPose()) {
//            this.extraRollingHeight += 0.2F;
//            if (this.extraRollingHeight > 3.0F) {
//                this.extraRollingHeight = 3.0F;
//            }
//        } else {
//            this.extraRollingHeight = 0.0F;
//        }
//
//    }
//
//    public boolean isBreedingItem(ItemStack stack) {
//        return stack.isIn(ItemTags.FOX_FOOD);
//    }
//
//    protected void onPlayerSpawnedChild(PlayerEntity player, MobEntity child) {
//        ((FoxEntity)child).addTrustedUuid(player.getUuid());
//    }
//
//    public boolean isChasing() {
//        return this.getFoxFlag(16);
//    }
//
//    public void setChasing(boolean chasing) {
//        this.setFoxFlag(16, chasing);
//    }
//
//    public boolean isJumping() {
//        return this.jumping;
//    }
//
//    public boolean isFullyCrouched() {
//        return this.extraRollingHeight == 3.0F;
//    }
//
//    public void setCrouching(boolean crouching) {
//        this.setFoxFlag(4, crouching);
//    }
//
//    public boolean isInSneakingPose() {
//        return this.getFoxFlag(4);
//    }
//
//    public void setRollingHead(boolean rollingHead) {
//        this.setFoxFlag(8, rollingHead);
//    }
//
//    public boolean isRollingHead() {
//        return this.getFoxFlag(8);
//    }
//
//    public float getHeadRoll(float tickDelta) {
//        return MathHelper.lerp(tickDelta, this.lastHeadRollProgress, this.headRollProgress) * 0.11F * (float)Math.PI;
//    }
//
//    public float getBodyRotationHeightOffset(float tickDelta) {
//        return MathHelper.lerp(tickDelta, this.lastExtraRollingHeight, this.extraRollingHeight);
//    }
//
//    public void setTarget(@Nullable LivingEntity target) {
//        if (this.isAggressive() && target == null) {
//            this.setAggressive(false);
//        }
//
//        super.setTarget(target);
//    }
//
//    void stopSleeping() {
//        this.setSleeping(false);
//    }
//
//    void stopActions() {
//        this.setRollingHead(false);
//        this.setCrouching(false);
//        this.setSitting(false);
//        this.setSleeping(false);
//        this.setAggressive(false);
//        this.setWalking(false);
//    }
//
//    boolean wantsToPickupItem() {
//        return !this.isSleeping() && !this.isSitting() && !this.isWalking();
//    }
//
//    public void playAmbientSound() {
//        SoundEvent soundEvent = this.getAmbientSound();
//        if (soundEvent == SoundEvents.ENTITY_FOX_SCREECH) {
//            this.playSound(soundEvent, 2.0F, this.getSoundPitch());
//        } else {
//            super.playAmbientSound();
//        }
//
//    }
//
//    @Nullable
//    protected SoundEvent getAmbientSound() {
//        if (this.isSleeping()) {
//            return SoundEvents.ENTITY_FOX_SLEEP;
//        } else {
//            if (!this.getWorld().isDay() && this.random.nextFloat() < 0.1F) {
//                List<PlayerEntity> list = this.getWorld().getEntitiesByClass(PlayerEntity.class, this.getBoundingBox().expand((double)16.0F, (double)16.0F, (double)16.0F), EntityPredicates.EXCEPT_SPECTATOR);
//                if (list.isEmpty()) {
//                    return SoundEvents.ENTITY_FOX_SCREECH;
//                }
//            }
//
//            return SoundEvents.ENTITY_FOX_AMBIENT;
//        }
//    }
//
//    @Nullable
//    protected SoundEvent getHurtSound(DamageSource source) {
//        return SoundEvents.ENTITY_FOX_HURT;
//    }
//
//    @Nullable
//    protected SoundEvent getDeathSound() {
//        return SoundEvents.ENTITY_FOX_DEATH;
//    }
//
//    boolean canTrust(UUID uuid) {
//        return this.getTrustedUuids().contains(uuid);
//    }
//
//    protected void drop(ServerWorld world, DamageSource damageSource) {
//        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
//        if (!itemStack.isEmpty()) {
//            this.dropStack(itemStack);
//            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
//        }
//
//        super.drop(world, damageSource);
//    }
//
//    public static boolean canJumpChase(FoxEntity fox, LivingEntity chasedEntity) {
//        double d = chasedEntity.getZ() - fox.getZ();
//        double e = chasedEntity.getX() - fox.getX();
//        double f = d / e;
//        int i = 6;
//
//        for(int j = 0; j < 6; ++j) {
//            double g = f == (double)0.0F ? (double)0.0F : d * (double)((float)j / 6.0F);
//            double h = f == (double)0.0F ? e * (double)((float)j / 6.0F) : g / f;
//
//            for(int k = 1; k < 4; ++k) {
//                if (!fox.getWorld().getBlockState(BlockPos.ofFloored(fox.getX() + h, fox.getY() + (double)k, fox.getZ() + g)).isReplaceable()) {
//                    return false;
//                }
//            }
//        }
//
//        return true;
//    }
//
//    public Vec3d getLeashOffset() {
//        return new Vec3d((double)0.0F, (double)(0.55F * this.getStandingEyeHeight()), (double)(this.getWidth() * 0.4F));
//    }
//
//    static {
//        TYPE = DataTracker.registerData(FoxEntity.class, TrackedDataHandlerRegistry.INTEGER);
//        FOX_FLAGS = DataTracker.registerData(FoxEntity.class, TrackedDataHandlerRegistry.BYTE);
//        OWNER = DataTracker.registerData(FoxEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
//        OTHER_TRUSTED = DataTracker.registerData(FoxEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
//        PICKABLE_DROP_FILTER = (item) -> !item.cannotPickup() && item.isAlive();
//        JUST_ATTACKED_SOMETHING_FILTER = (entity) -> {
//            if (!(entity instanceof LivingEntity livingEntity)) {
//                return false;
//            } else {
//                return livingEntity.getAttacking() != null && livingEntity.getLastAttackTime() < livingEntity.age + 600;
//            }
//        };
//        CHICKEN_AND_RABBIT_FILTER = (entity) -> entity instanceof ChickenEntity || entity instanceof RabbitEntity;
//        NOTICEABLE_PLAYER_FILTER = (entity) -> !entity.isSneaky() && EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(entity);
//        BABY_BASE_DIMENSIONS = EntityType.FOX.getDimensions().scaled(0.5F).withEyeHeight(0.2975F);
//    }
//
//    public static enum Type implements StringIdentifiable {
//        RED(0, "red"),
//        SNOW(1, "snow");
//
//        public static final StringIdentifiable.EnumCodec<Type> CODEC = StringIdentifiable.createCodec(Type::values);
//        private static final IntFunction<Type> BY_ID = ValueLists.createIdToValueFunction(Type::getId, values(), OutOfBoundsHandling.ZERO);
//        private final int id;
//        private final String key;
//
//        private Type(final int id, final String key) {
//            this.id = id;
//            this.key = key;
//        }
//
//        public String asString() {
//            return this.key;
//        }
//
//        public int getId() {
//            return this.id;
//        }
//
//        public static Type byName(String name) {
//            return (Type)CODEC.byId(name, RED);
//        }
//
//        public static Type fromId(int id) {
//            return (Type)BY_ID.apply(id);
//        }
//
//        public static Type fromBiome(RegistryEntry<Biome> biome) {
//            return biome.isIn(BiomeTags.SPAWNS_SNOW_FOXES) ? SNOW : RED;
//        }
//    }
//
//    class PickupItemGoal extends Goal {
//        public PickupItemGoal() {
//            this.setControls(EnumSet.of(Control.MOVE));
//        }
//
//        public boolean canStart() {
//            if (!FoxEntity.this.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty()) {
//                return false;
//            } else if (FoxEntity.this.getTarget() == null && FoxEntity.this.getAttacker() == null) {
//                if (!FoxEntity.this.wantsToPickupItem()) {
//                    return false;
//                } else if (FoxEntity.this.getRandom().nextInt(toGoalTicks(10)) != 0) {
//                    return false;
//                } else {
//                    List<ItemEntity> list = FoxEntity.this.getWorld().getEntitiesByClass(ItemEntity.class, FoxEntity.this.getBoundingBox().expand((double)8.0F, (double)8.0F, (double)8.0F), FoxEntity.PICKABLE_DROP_FILTER);
//                    return !list.isEmpty() && FoxEntity.this.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty();
//                }
//            } else {
//                return false;
//            }
//        }
//
//        public void tick() {
//            List<ItemEntity> list = FoxEntity.this.getWorld().getEntitiesByClass(ItemEntity.class, FoxEntity.this.getBoundingBox().expand((double)8.0F, (double)8.0F, (double)8.0F), FoxEntity.PICKABLE_DROP_FILTER);
//            ItemStack itemStack = FoxEntity.this.getEquippedStack(EquipmentSlot.MAINHAND);
//            if (itemStack.isEmpty() && !list.isEmpty()) {
//                FoxEntity.this.getNavigation().startMovingTo((Entity)list.get(0), (double)1.2F);
//            }
//
//        }
//
//        public void start() {
//            List<ItemEntity> list = FoxEntity.this.getWorld().getEntitiesByClass(ItemEntity.class, FoxEntity.this.getBoundingBox().expand((double)8.0F, (double)8.0F, (double)8.0F), FoxEntity.PICKABLE_DROP_FILTER);
//            if (!list.isEmpty()) {
//                FoxEntity.this.getNavigation().startMovingTo((Entity)list.get(0), (double)1.2F);
//            }
//
//        }
//    }
//
//    class FoxMoveControl extends MoveControl {
//        public FoxMoveControl() {
//            super(FoxEntity.this);
//        }
//
//        public void tick() {
//            if (FoxEntity.this.wantsToPickupItem()) {
//                super.tick();
//            }
//
//        }
//    }
//
//    class MoveToHuntGoal extends Goal {
//        public MoveToHuntGoal() {
//            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
//        }
//
//        public boolean canStart() {
//            if (FoxEntity.this.isSleeping()) {
//                return false;
//            } else {
//                LivingEntity livingEntity = FoxEntity.this.getTarget();
//                return livingEntity != null && livingEntity.isAlive() && FoxEntity.CHICKEN_AND_RABBIT_FILTER.test(livingEntity) && FoxEntity.this.squaredDistanceTo(livingEntity) > (double)36.0F && !FoxEntity.this.isInSneakingPose() && !FoxEntity.this.isRollingHead() && !FoxEntity.this.jumping;
//            }
//        }
//
//        public void start() {
//            FoxEntity.this.setSitting(false);
//            FoxEntity.this.setWalking(false);
//        }
//
//        public void stop() {
//            LivingEntity livingEntity = FoxEntity.this.getTarget();
//            if (livingEntity != null && FoxEntity.canJumpChase(FoxEntity.this, livingEntity)) {
//                FoxEntity.this.setRollingHead(true);
//                FoxEntity.this.setCrouching(true);
//                FoxEntity.this.getNavigation().stop();
//                FoxEntity.this.getLookControl().lookAt(livingEntity, (float)FoxEntity.this.getMaxHeadRotation(), (float)FoxEntity.this.getMaxLookPitchChange());
//            } else {
//                FoxEntity.this.setRollingHead(false);
//                FoxEntity.this.setCrouching(false);
//            }
//
//        }
//
//        public void tick() {
//            LivingEntity livingEntity = FoxEntity.this.getTarget();
//            if (livingEntity != null) {
//                FoxEntity.this.getLookControl().lookAt(livingEntity, (float)FoxEntity.this.getMaxHeadRotation(), (float)FoxEntity.this.getMaxLookPitchChange());
//                if (FoxEntity.this.squaredDistanceTo(livingEntity) <= (double)36.0F) {
//                    FoxEntity.this.setRollingHead(true);
//                    FoxEntity.this.setCrouching(true);
//                    FoxEntity.this.getNavigation().stop();
//                } else {
//                    FoxEntity.this.getNavigation().startMovingTo(livingEntity, (double)1.5F);
//                }
//
//            }
//        }
//    }
//
//    class AttackGoal extends MeleeAttackGoal {
//        public AttackGoal(final double speed, final boolean pauseWhenIdle) {
//            super(FoxEntity.this, speed, pauseWhenIdle);
//        }
//
//        protected void attack(LivingEntity target) {
//            if (this.canAttack(target)) {
//                this.resetCooldown();
//                this.mob.tryAttack(target);
//                FoxEntity.this.playSound(SoundEvents.ENTITY_FOX_BITE, 1.0F, 1.0F);
//            }
//
//        }
//
//        public void start() {
//            FoxEntity.this.setRollingHead(false);
//            super.start();
//        }
//
//        public boolean canStart() {
//            return !FoxEntity.this.isSitting() && !FoxEntity.this.isSleeping() && !FoxEntity.this.isInSneakingPose() && !FoxEntity.this.isWalking() && super.canStart();
//        }
//    }
//
//    class MateGoal extends AnimalMateGoal {
//        public MateGoal(final double chance) {
//            super(FoxEntity.this, chance);
//        }
//
//        public void start() {
//            ((FoxEntity)this.animal).stopActions();
//            ((FoxEntity)this.mate).stopActions();
//            super.start();
//        }
//
//        protected void breed() {
//            ServerWorld serverWorld = (ServerWorld)this.world;
//            FoxEntity foxEntity = (FoxEntity)this.animal.createChild(serverWorld, this.mate);
//            if (foxEntity != null) {
//                ServerPlayerEntity serverPlayerEntity = this.animal.getLovingPlayer();
//                ServerPlayerEntity serverPlayerEntity2 = this.mate.getLovingPlayer();
//                ServerPlayerEntity serverPlayerEntity3 = serverPlayerEntity;
//                if (serverPlayerEntity != null) {
//                    foxEntity.addTrustedUuid(serverPlayerEntity.getUuid());
//                } else {
//                    serverPlayerEntity3 = serverPlayerEntity2;
//                }
//
//                if (serverPlayerEntity2 != null && serverPlayerEntity != serverPlayerEntity2) {
//                    foxEntity.addTrustedUuid(serverPlayerEntity2.getUuid());
//                }
//
//                if (serverPlayerEntity3 != null) {
//                    serverPlayerEntity3.incrementStat(Stats.ANIMALS_BRED);
//                    Criteria.BRED_ANIMALS.trigger(serverPlayerEntity3, this.animal, this.mate, foxEntity);
//                }
//
//                this.animal.setBreedingAge(6000);
//                this.mate.setBreedingAge(6000);
//                this.animal.resetLoveTicks();
//                this.mate.resetLoveTicks();
//                foxEntity.setBreedingAge(-24000);
//                foxEntity.refreshPositionAndAngles(this.animal.getX(), this.animal.getY(), this.animal.getZ(), 0.0F, 0.0F);
//                serverWorld.spawnEntityAndPassengers(foxEntity);
//                this.world.sendEntityStatus(this.animal, (byte)18);
//                if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
//                    this.world.spawnEntity(new ExperienceOrbEntity(this.world, this.animal.getX(), this.animal.getY(), this.animal.getZ(), this.animal.getRandom().nextInt(7) + 1));
//                }
//
//            }
//        }
//    }
//
//    class DefendFriendGoal extends ActiveTargetGoal<LivingEntity> {
//        @Nullable
//        private LivingEntity offender;
//        @Nullable
//        private LivingEntity friend;
//        private int lastAttackedTime;
//
//        public DefendFriendGoal(final Class<LivingEntity> targetEntityClass, final boolean checkVisibility, final boolean checkCanNavigate, @Nullable final Predicate<LivingEntity> targetPredicate) {
//            super(FoxEntity.this, targetEntityClass, 10, checkVisibility, checkCanNavigate, targetPredicate);
//        }
//
//        public boolean canStart() {
//            if (this.reciprocalChance > 0 && this.mob.getRandom().nextInt(this.reciprocalChance) != 0) {
//                return false;
//            } else {
//                for(UUID uUID : FoxEntity.this.getTrustedUuids()) {
//                    if (uUID != null && FoxEntity.this.getWorld() instanceof ServerWorld) {
//                        Entity entity = ((ServerWorld)FoxEntity.this.getWorld()).getEntity(uUID);
//                        if (entity instanceof LivingEntity) {
//                            LivingEntity livingEntity = (LivingEntity)entity;
//                            this.friend = livingEntity;
//                            this.offender = livingEntity.getAttacker();
//                            int i = livingEntity.getLastAttackedTime();
//                            return i != this.lastAttackedTime && this.canTrack(this.offender, this.targetPredicate);
//                        }
//                    }
//                }
//
//                return false;
//            }
//        }
//
//        public void start() {
//            this.setTargetEntity(this.offender);
//            this.targetEntity = this.offender;
//            if (this.friend != null) {
//                this.lastAttackedTime = this.friend.getLastAttackedTime();
//            }
//
//            FoxEntity.this.playSound(SoundEvents.ENTITY_FOX_AGGRO, 1.0F, 1.0F);
//            FoxEntity.this.setAggressive(true);
//            FoxEntity.this.stopSleeping();
//            super.start();
//        }
//    }
//
//    class AvoidDaylightGoal extends EscapeSunlightGoal {
//        private int timer = toGoalTicks(100);
//
//        public AvoidDaylightGoal(final double speed) {
//            super(FoxEntity.this, speed);
//        }
//
//        public boolean canStart() {
//            if (!FoxEntity.this.isSleeping() && this.mob.getTarget() == null) {
//                if (FoxEntity.this.getWorld().isThundering() && FoxEntity.this.getWorld().isSkyVisible(this.mob.getBlockPos())) {
//                    return this.targetShadedPos();
//                } else if (this.timer > 0) {
//                    --this.timer;
//                    return false;
//                } else {
//                    this.timer = 100;
//                    BlockPos blockPos = this.mob.getBlockPos();
//                    return FoxEntity.this.getWorld().isDay() && FoxEntity.this.getWorld().isSkyVisible(blockPos) && !((ServerWorld)FoxEntity.this.getWorld()).isNearOccupiedPointOfInterest(blockPos) && this.targetShadedPos();
//                }
//            } else {
//                return false;
//            }
//        }
//
//        public void start() {
//            FoxEntity.this.stopActions();
//            super.start();
//        }
//    }
//
//    public class WorriableEntityFilter implements Predicate<LivingEntity> {
//        public WorriableEntityFilter() {
//        }
//
//        public boolean test(LivingEntity livingEntity) {
//            if (livingEntity instanceof FoxEntity) {
//                return false;
//            } else if (!(livingEntity instanceof ChickenEntity) && !(livingEntity instanceof RabbitEntity) && !(livingEntity instanceof HostileEntity)) {
//                if (livingEntity instanceof TameableEntity) {
//                    return !((TameableEntity)livingEntity).isTamed();
//                } else if (!(livingEntity instanceof PlayerEntity) || !livingEntity.isSpectator() && !((PlayerEntity)livingEntity).isCreative()) {
//                    if (FoxEntity.this.canTrust(livingEntity.getUuid())) {
//                        return false;
//                    } else {
//                        return !livingEntity.isSleeping() && !livingEntity.isSneaky();
//                    }
//                } else {
//                    return false;
//                }
//            } else {
//                return true;
//            }
//        }
//    }
//
//    abstract class CalmDownGoal extends Goal {
//        private final TargetPredicate WORRIABLE_ENTITY_PREDICATE = TargetPredicate.createAttackable().setBaseMaxDistance((double)12.0F).ignoreVisibility().setPredicate(FoxEntity.this.new WorriableEntityFilter());
//
//        CalmDownGoal() {
//        }
//
//        protected boolean isAtFavoredLocation() {
//            BlockPos blockPos = BlockPos.ofFloored(FoxEntity.this.getX(), FoxEntity.this.getBoundingBox().maxY, FoxEntity.this.getZ());
//            return !FoxEntity.this.getWorld().isSkyVisible(blockPos) && FoxEntity.this.getPathfindingFavor(blockPos) >= 0.0F;
//        }
//
//        protected boolean canCalmDown() {
//            return !FoxEntity.this.getWorld().getTargets(LivingEntity.class, this.WORRIABLE_ENTITY_PREDICATE, FoxEntity.this, FoxEntity.this.getBoundingBox().expand((double)12.0F, (double)6.0F, (double)12.0F)).isEmpty();
//        }
//    }
//
//    class DelayedCalmDownGoal extends CalmDownGoal {
//        private static final int MAX_CALM_DOWN_TIME = toGoalTicks(140);
//        private int timer;
//
//        public DelayedCalmDownGoal() {
//            this.timer = FoxEntity.this.random.nextInt(MAX_CALM_DOWN_TIME);
//            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
//        }
//
//        public boolean canStart() {
//            if (FoxEntity.this.sidewaysSpeed == 0.0F && FoxEntity.this.upwardSpeed == 0.0F && FoxEntity.this.forwardSpeed == 0.0F) {
//                return this.canNotCalmDown() || FoxEntity.this.isSleeping();
//            } else {
//                return false;
//            }
//        }
//
//        public boolean shouldContinue() {
//            return this.canNotCalmDown();
//        }
//
//        private boolean canNotCalmDown() {
//            if (this.timer > 0) {
//                --this.timer;
//                return false;
//            } else {
//                return FoxEntity.this.getWorld().isDay() && this.isAtFavoredLocation() && !this.canCalmDown() && !FoxEntity.this.inPowderSnow;
//            }
//        }
//
//        public void stop() {
//            this.timer = FoxEntity.this.random.nextInt(MAX_CALM_DOWN_TIME);
//            FoxEntity.this.stopActions();
//        }
//
//        public void start() {
//            FoxEntity.this.setSitting(false);
//            FoxEntity.this.setCrouching(false);
//            FoxEntity.this.setRollingHead(false);
//            FoxEntity.this.setJumping(false);
//            FoxEntity.this.setSleeping(true);
//            FoxEntity.this.getNavigation().stop();
//            FoxEntity.this.getMoveControl().moveTo(FoxEntity.this.getX(), FoxEntity.this.getY(), FoxEntity.this.getZ(), (double)0.0F);
//        }
//    }
//
//    class SitDownAndLookAroundGoal extends CalmDownGoal {
//        private double lookX;
//        private double lookZ;
//        private int timer;
//        private int counter;
//
//        public SitDownAndLookAroundGoal() {
//            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
//        }
//
//        public boolean canStart() {
//            return FoxEntity.this.getAttacker() == null && FoxEntity.this.getRandom().nextFloat() < 0.02F && !FoxEntity.this.isSleeping() && FoxEntity.this.getTarget() == null && FoxEntity.this.getNavigation().isIdle() && !this.canCalmDown() && !FoxEntity.this.isChasing() && !FoxEntity.this.isInSneakingPose();
//        }
//
//        public boolean shouldContinue() {
//            return this.counter > 0;
//        }
//
//        public void start() {
//            this.chooseNewAngle();
//            this.counter = 2 + FoxEntity.this.getRandom().nextInt(3);
//            FoxEntity.this.setSitting(true);
//            FoxEntity.this.getNavigation().stop();
//        }
//
//        public void stop() {
//            FoxEntity.this.setSitting(false);
//        }
//
//        public void tick() {
//            --this.timer;
//            if (this.timer <= 0) {
//                --this.counter;
//                this.chooseNewAngle();
//            }
//
//            FoxEntity.this.getLookControl().lookAt(FoxEntity.this.getX() + this.lookX, FoxEntity.this.getEyeY(), FoxEntity.this.getZ() + this.lookZ, (float)FoxEntity.this.getMaxHeadRotation(), (float)FoxEntity.this.getMaxLookPitchChange());
//        }
//
//        private void chooseNewAngle() {
//            double d = (Math.PI * 2D) * FoxEntity.this.getRandom().nextDouble();
//            this.lookX = Math.cos(d);
//            this.lookZ = Math.sin(d);
//            this.timer = this.getTickCount(80 + FoxEntity.this.getRandom().nextInt(20));
//        }
//    }
//
//    public class EatBerriesGoal extends MoveToTargetPosGoal {
//        private static final int EATING_TIME = 40;
//        protected int timer;
//
//        public EatBerriesGoal(final double speed, final int range, final int maxYDifference) {
//            super(FoxEntity.this, speed, range, maxYDifference);
//        }
//
//        public double getDesiredDistanceToTarget() {
//            return (double)2.0F;
//        }
//
//        public boolean shouldResetPath() {
//            return this.tryingTime % 100 == 0;
//        }
//
//        protected boolean isTargetPos(WorldView world, BlockPos pos) {
//            BlockState blockState = world.getBlockState(pos);
//            return blockState.isOf(Blocks.SWEET_BERRY_BUSH) && (Integer)blockState.get(SweetBerryBushBlock.AGE) >= 2 || CaveVines.hasBerries(blockState);
//        }
//
//        public void tick() {
//            if (this.hasReached()) {
//                if (this.timer >= 40) {
//                    this.eatBerries();
//                } else {
//                    ++this.timer;
//                }
//            } else if (!this.hasReached() && FoxEntity.this.random.nextFloat() < 0.05F) {
//                FoxEntity.this.playSound(SoundEvents.ENTITY_FOX_SNIFF, 1.0F, 1.0F);
//            }
//
//            super.tick();
//        }
//
//        protected void eatBerries() {
//            if (FoxEntity.this.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
//                BlockState blockState = FoxEntity.this.getWorld().getBlockState(this.targetPos);
//                if (blockState.isOf(Blocks.SWEET_BERRY_BUSH)) {
//                    this.pickSweetBerries(blockState);
//                } else if (CaveVines.hasBerries(blockState)) {
//                    this.pickGlowBerries(blockState);
//                }
//
//            }
//        }
//
//        private void pickGlowBerries(BlockState state) {
//            CaveVines.pickBerries(FoxEntity.this, state, FoxEntity.this.getWorld(), this.targetPos);
//        }
//
//        private void pickSweetBerries(BlockState state) {
//            int i = (Integer)state.get(SweetBerryBushBlock.AGE);
//            state.with(SweetBerryBushBlock.AGE, 1);
//            int j = 1 + FoxEntity.this.getWorld().random.nextInt(2) + (i == 3 ? 1 : 0);
//            ItemStack itemStack = FoxEntity.this.getEquippedStack(EquipmentSlot.MAINHAND);
//            if (itemStack.isEmpty()) {
//                FoxEntity.this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.SWEET_BERRIES));
//                --j;
//            }
//
//            if (j > 0) {
//                Block.dropStack(FoxEntity.this.getWorld(), this.targetPos, new ItemStack(Items.SWEET_BERRIES, j));
//            }
//
//            FoxEntity.this.playSound(SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
//            FoxEntity.this.getWorld().setBlockState(this.targetPos, (BlockState)state.with(SweetBerryBushBlock.AGE, 1), 2);
//            FoxEntity.this.getWorld().emitGameEvent(GameEvent.BLOCK_CHANGE, this.targetPos, Emitter.of(FoxEntity.this));
//        }
//
//        public boolean canStart() {
//            return !FoxEntity.this.isSleeping() && super.canStart();
//        }
//
//        public void start() {
//            this.timer = 0;
//            FoxEntity.this.setSitting(false);
//            super.start();
//        }
//    }
//
//    public static class FoxData extends PassiveEntity.PassiveData {
//        public final Type type;
//
//        public FoxData(Type type) {
//            super(false);
//            this.type = type;
//        }
//    }
//
//    class StopWanderingGoal extends Goal {
//        int timer;
//
//        public StopWanderingGoal() {
//            this.setControls(EnumSet.of(Control.LOOK, Control.JUMP, Control.MOVE));
//        }
//
//        public boolean canStart() {
//            return FoxEntity.this.isWalking();
//        }
//
//        public boolean shouldContinue() {
//            return this.canStart() && this.timer > 0;
//        }
//
//        public void start() {
//            this.timer = this.getTickCount(40);
//        }
//
//        public void stop() {
//            FoxEntity.this.setWalking(false);
//        }
//
//        public void tick() {
//            --this.timer;
//        }
//    }
//
//    class EscapeWhenNotAggressiveGoal extends EscapeDangerGoal {
//        public EscapeWhenNotAggressiveGoal(final double speed) {
//            super(FoxEntity.this, speed);
//        }
//
//        public boolean isInDanger() {
//            return !FoxEntity.this.isAggressive() && super.isInDanger();
//        }
//    }
//
//    class GoToVillageGoal extends net.minecraft.entity.ai.goal.GoToVillageGoal {
//        public GoToVillageGoal(final int unused, final int searchRange) {
//            super(FoxEntity.this, searchRange);
//        }
//
//        public void start() {
//            FoxEntity.this.stopActions();
//            super.start();
//        }
//
//        public boolean canStart() {
//            return super.canStart() && this.canGoToVillage();
//        }
//
//        public boolean shouldContinue() {
//            return super.shouldContinue() && this.canGoToVillage();
//        }
//
//        private boolean canGoToVillage() {
//            return !FoxEntity.this.isSleeping() && !FoxEntity.this.isSitting() && !FoxEntity.this.isAggressive() && FoxEntity.this.getTarget() == null;
//        }
//    }
//
//    class FoxSwimGoal extends SwimGoal {
//        public FoxSwimGoal() {
//            super(FoxEntity.this);
//        }
//
//        public void start() {
//            super.start();
//            FoxEntity.this.stopActions();
//        }
//
//        public boolean canStart() {
//            return FoxEntity.this.isTouchingWater() && FoxEntity.this.getFluidHeight(FluidTags.WATER) > (double)0.25F || FoxEntity.this.isInLava();
//        }
//    }
//
//    public class JumpChasingGoal extends DiveJumpingGoal {
//        public JumpChasingGoal() {
//        }
//
//        public boolean canStart() {
//            if (!FoxEntity.this.isFullyCrouched()) {
//                return false;
//            } else {
//                LivingEntity livingEntity = FoxEntity.this.getTarget();
//                if (livingEntity != null && livingEntity.isAlive()) {
//                    if (livingEntity.getMovementDirection() != livingEntity.getHorizontalFacing()) {
//                        return false;
//                    } else {
//                        boolean bl = FoxEntity.canJumpChase(FoxEntity.this, livingEntity);
//                        if (!bl) {
//                            FoxEntity.this.getNavigation().findPathTo(livingEntity, 0);
//                            FoxEntity.this.setCrouching(false);
//                            FoxEntity.this.setRollingHead(false);
//                        }
//
//                        return bl;
//                    }
//                } else {
//                    return false;
//                }
//            }
//        }
//
//        public boolean shouldContinue() {
//            LivingEntity livingEntity = FoxEntity.this.getTarget();
//            if (livingEntity != null && livingEntity.isAlive()) {
//                double d = FoxEntity.this.getVelocity().y;
//                return (!(d * d < (double)0.05F) || !(Math.abs(FoxEntity.this.getPitch()) < 15.0F) || !FoxEntity.this.isOnGround()) && !FoxEntity.this.isWalking();
//            } else {
//                return false;
//            }
//        }
//
//        public boolean canStop() {
//            return false;
//        }
//
//        public void start() {
//            FoxEntity.this.setJumping(true);
//            FoxEntity.this.setChasing(true);
//            FoxEntity.this.setRollingHead(false);
//            LivingEntity livingEntity = FoxEntity.this.getTarget();
//            if (livingEntity != null) {
//                FoxEntity.this.getLookControl().lookAt(livingEntity, 60.0F, 30.0F);
//                Vec3d vec3d = (new Vec3d(livingEntity.getX() - FoxEntity.this.getX(), livingEntity.getY() - FoxEntity.this.getY(), livingEntity.getZ() - FoxEntity.this.getZ())).normalize();
//                FoxEntity.this.setVelocity(FoxEntity.this.getVelocity().add(vec3d.x * 0.8, 0.9, vec3d.z * 0.8));
//            }
//
//            FoxEntity.this.getNavigation().stop();
//        }
//
//        public void stop() {
//            FoxEntity.this.setCrouching(false);
//            FoxEntity.this.extraRollingHeight = 0.0F;
//            FoxEntity.this.lastExtraRollingHeight = 0.0F;
//            FoxEntity.this.setRollingHead(false);
//            FoxEntity.this.setChasing(false);
//        }
//
//        public void tick() {
//            LivingEntity livingEntity = FoxEntity.this.getTarget();
//            if (livingEntity != null) {
//                FoxEntity.this.getLookControl().lookAt(livingEntity, 60.0F, 30.0F);
//            }
//
//            if (!FoxEntity.this.isWalking()) {
//                Vec3d vec3d = FoxEntity.this.getVelocity();
//                if (vec3d.y * vec3d.y < (double)0.03F && FoxEntity.this.getPitch() != 0.0F) {
//                    FoxEntity.this.setPitch(MathHelper.lerpAngleDegrees(0.2F, FoxEntity.this.getPitch(), 0.0F));
//                } else {
//                    double d = vec3d.horizontalLength();
//                    double e = Math.signum(-vec3d.y) * Math.acos(d / vec3d.length()) * (double)(180F / (float)Math.PI);
//                    FoxEntity.this.setPitch((float)e);
//                }
//            }
//
//            if (livingEntity != null && FoxEntity.this.distanceTo(livingEntity) <= 2.0F) {
//                FoxEntity.this.tryAttack(livingEntity);
//            } else if (FoxEntity.this.getPitch() > 0.0F && FoxEntity.this.isOnGround() && (float)FoxEntity.this.getVelocity().y != 0.0F && FoxEntity.this.getWorld().getBlockState(FoxEntity.this.getBlockPos()).isOf(Blocks.SNOW)) {
//                FoxEntity.this.setPitch(60.0F);
//                FoxEntity.this.setTarget((LivingEntity)null);
//                FoxEntity.this.setWalking(true);
//            }
//
//        }
//    }
//
//    public class FoxLookControl extends LookControl {
//        public FoxLookControl() {
//            super(FoxEntity.this);
//        }
//
//        public void tick() {
//            if (!FoxEntity.this.isSleeping()) {
//                super.tick();
//            }
//
//        }
//
//        protected boolean shouldStayHorizontal() {
//            return !FoxEntity.this.isChasing() && !FoxEntity.this.isInSneakingPose() && !FoxEntity.this.isRollingHead() && !FoxEntity.this.isWalking();
//        }
//    }
//
//    class FollowParentGoal extends net.minecraft.entity.ai.goal.FollowParentGoal {
//        private final FoxEntity fox;
//
//        public FollowParentGoal(final FoxEntity fox, final double speed) {
//            super(fox, speed);
//            this.fox = fox;
//        }
//
//        public boolean canStart() {
//            return !this.fox.isAggressive() && super.canStart();
//        }
//
//        public boolean shouldContinue() {
//            return !this.fox.isAggressive() && super.shouldContinue();
//        }
//
//        public void start() {
//            this.fox.stopActions();
//            super.start();
//        }
//    }
//
//    class LookAtEntityGoal extends net.minecraft.entity.ai.goal.LookAtEntityGoal {
//        public LookAtEntityGoal(final MobEntity fox, final Class<? extends LivingEntity> targetType, final float range) {
//            super(fox, targetType, range);
//        }
//
//        public boolean canStart() {
//            return super.canStart() && !FoxEntity.this.isWalking() && !FoxEntity.this.isRollingHead();
//        }
//
//        public boolean shouldContinue() {
//            return super.shouldContinue() && !FoxEntity.this.isWalking() && !FoxEntity.this.isRollingHead();
//        }
//    }
//}
