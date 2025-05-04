package net.dawson.adorablehamsterpets.mixin.server;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.attachment.HamsterShoulderData;
import net.dawson.adorablehamsterpets.attachment.ModEntityAttachments;
import net.dawson.adorablehamsterpets.config.ModConfig;
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity; // Import HamsterEntity
import net.dawson.adorablehamsterpets.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld; // Import ServerWorld
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.math.random.Random;
import net.minecraft.entity.Entity; // Keep this import if needed elsewhere, but not for the removed method

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    // --- Define Timers & Cooldowns ---
    @Unique private int adorablehamsterpets$diamondCheckTimer = 0;
    @Unique private int adorablehamsterpets$creeperCheckTimer = 0;
    @Unique private static final int CHECK_INTERVAL_TICKS = 20; // Check conditions once per second

    @Unique private int adorablehamsterpets$diamondSoundCooldownTicks = 0; // Cooldown *after* sound plays
    @Unique private int adorablehamsterpets$creeperSoundCooldownTicks = 0; // Cooldown *after* sound plays
    // --- End Define Timers & Cooldowns ---



    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void adorablehamsterpets$onTick(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity)(Object)this;
        World world = self.getWorld(); // Get world instance
        Random random = world.getRandom(); // Get the world's random instance
        final ModConfig config = AdorableHamsterPets.CONFIG; // Access static config


        if (!world.isClient()) {
            HamsterShoulderData shoulderData = self.getAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA);


            // --- Decrement Sound Cooldowns ---
            if (adorablehamsterpets$diamondSoundCooldownTicks > 0) adorablehamsterpets$diamondSoundCooldownTicks--;
            if (adorablehamsterpets$creeperSoundCooldownTicks > 0) adorablehamsterpets$creeperSoundCooldownTicks--;
            // --- End Decrement ---


            if (shoulderData != null) {
                boolean shouldDismount = self.isSneaking();


                if (shouldDismount) {
                    // --- Dismount Logic ---
                    HamsterEntity.spawnFromShoulderData((ServerWorld) world, self, shoulderData);
                    self.removeAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA);
                    // Reset timers and cooldowns on dismount
                    adorablehamsterpets$diamondCheckTimer = 0;
                    adorablehamsterpets$creeperCheckTimer = 0;
                    adorablehamsterpets$diamondSoundCooldownTicks = 0;
                    adorablehamsterpets$creeperSoundCooldownTicks = 0;
                    // --- End Dismount Logic ---
                    return;
                }


                // --- Start Shoulder Detection Logic ---
                // Diamond Ore Check
                adorablehamsterpets$diamondCheckTimer++;
                if (adorablehamsterpets$diamondCheckTimer >= CHECK_INTERVAL_TICKS) {
                    adorablehamsterpets$diamondCheckTimer = 0;
                    if (config.features.enableShoulderDiamondDetection() && isDiamondNearby(self, config.features.shoulderDiamondDetectionRadius())) {
                        if (adorablehamsterpets$diamondSoundCooldownTicks == 0) {
                            
                            world.playSound(null, self.getBlockPos(),
                                    ModSounds.getRandomSoundFrom(ModSounds.HAMSTER_DIAMOND_SNIFF_SOUNDS, random),
                                    SoundCategory.NEUTRAL,
                                    0.7f,
                                    1.1f);


                            self.sendMessage(Text.translatable("message.adorablehamsterpets.diamond_nearby").formatted(Formatting.AQUA), true);


                            
                            adorablehamsterpets$diamondSoundCooldownTicks = random.nextBetween(140, 200); // Play randomly every 7-10 seconds
                            AdorableHamsterPets.LOGGER.trace("[PlayerTickMixin] Player {} triggered diamond sniff sound & message. Cooldown set to {} ticks.", self.getName().getString(), adorablehamsterpets$diamondSoundCooldownTicks);
                        }
                    }
                }


                // Creeper Check
                adorablehamsterpets$creeperCheckTimer++;
                if (adorablehamsterpets$creeperCheckTimer >= CHECK_INTERVAL_TICKS) {
                    adorablehamsterpets$creeperCheckTimer = 0;
                    if (config.features.enableShoulderCreeperDetection() && creeperSeesPlayer(self, config.features.shoulderCreeperDetectionRadius())) {
                        if (adorablehamsterpets$creeperSoundCooldownTicks == 0) {
                            
                            world.playSound(null, self.getBlockPos(),
                                    ModSounds.getRandomSoundFrom(ModSounds.HAMSTER_CREEPER_DETECT_SOUNDS, random), 
                                    SoundCategory.NEUTRAL, 1.0f, 1.0f);


                            self.sendMessage(Text.translatable("message.adorablehamsterpets.creeper_detected").formatted(Formatting.RED), true);


                            
                            adorablehamsterpets$creeperSoundCooldownTicks = random.nextBetween(100, 160); 
                            AdorableHamsterPets.LOGGER.trace("[PlayerTickMixin] Player {} triggered creeper detect sound & message. Cooldown set to {} ticks.", self.getName().getString(), adorablehamsterpets$creeperSoundCooldownTicks);
                        }
                    }
                }
                // --- End Shoulder Detection Logic ---
            }
        }
    }



    // --- onDamage Method ---
    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), cancellable = true)
    private void adorablehamsterpets$onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity self = (PlayerEntity)(Object)this;
        if (!self.getWorld().isClient && amount > 0.0f) {
            HamsterShoulderData shoulderData = self.getAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA);
            if (shoulderData != null) {
                HamsterEntity.spawnFromShoulderData((ServerWorld) self.getWorld(), self, shoulderData);
                self.removeAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA);
                // Reset timers and cooldowns on damage dismount too
                adorablehamsterpets$diamondCheckTimer = 0;
                adorablehamsterpets$creeperCheckTimer = 0;
                adorablehamsterpets$diamondSoundCooldownTicks = 0; // Reset sound cooldowns
                adorablehamsterpets$creeperSoundCooldownTicks = 0;
            }
        }
    }
    // --- End onDamage Method ---


    // --- isDiamondNearby Method ---
    @Unique
    private boolean isDiamondNearby(PlayerEntity player, double radius) {
        World world = player.getWorld();
        BlockPos center = player.getBlockPos();
        int intRadius = (int) Math.ceil(radius);
        for (BlockPos checkPos : BlockPos.iterate(center.add(-intRadius, -intRadius, -intRadius), center.add(intRadius, intRadius, intRadius))) {
            if (checkPos.getSquaredDistance(center) <= radius * radius) {
                BlockState state = world.getBlockState(checkPos);
                if (state.isOf(Blocks.DIAMOND_ORE) || state.isOf(Blocks.DEEPSLATE_DIAMOND_ORE)) {
                    return true; // Found one
                }
            }
        }
        return false; // None found
    }
    // --- End isDiamondNearby Method ---


    // --- creeperSeesPlayer Method (Keep As Is) ---
    @Unique
    private boolean creeperSeesPlayer(PlayerEntity player, double radius) {
        // --- End Correction ---
        World world = player.getWorld();
        // --- Corrected: Use parameter for search box ---
        Box searchBox = new Box(player.getPos().subtract(radius, radius, radius), player.getPos().add(radius, radius, radius));
        // --- End Correction ---

        List<CreeperEntity> nearbyCreepers = world.getEntitiesByClass(
                CreeperEntity.class,
                searchBox,
                creeper -> creeper.isAlive() && creeper.getTarget() == player && EntityPredicates.VALID_ENTITY.test(creeper)
        );
        return !nearbyCreepers.isEmpty();
    }
    // --- End creeperSeesPlayer Method ---
}