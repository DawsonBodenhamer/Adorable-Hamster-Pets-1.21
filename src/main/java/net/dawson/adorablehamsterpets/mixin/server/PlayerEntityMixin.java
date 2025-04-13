package net.dawson.adorablehamsterpets.mixin.server;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.attachment.HamsterShoulderData;
import net.dawson.adorablehamsterpets.attachment.ModEntityAttachments;
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity; // Import HamsterEntity
import net.dawson.adorablehamsterpets.sound.ModSounds;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld; // Import ServerWorld
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.entity.Entity; // Keep this import if needed elsewhere, but not for the removed method

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Unique private int adorablehamsterpets$diamondCheckTimer = 0;
    @Unique private int adorablehamsterpets$creeperCheckTimer = 0;
    @Unique private static final int CHECK_INTERVAL = 200; // Reduced interval for testing/responsiveness

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void adorablehamsterpets$onTick(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity)(Object)this;

        if (!self.getWorld().isClient()) {
            HamsterShoulderData shoulderData = self.getAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA);

            if (shoulderData != null) {
                boolean shouldDismount = false;
                if (self.isSneaking()) {
                    // AdorableHamsterPets.LOGGER.trace("[PlayerTickMixin] Player {} sneaking, queueing hamster dismount.", self.getName().getString());
                    shouldDismount = true;
                }
                // Add other conditions using 'self' if needed

                if (shouldDismount) {
                    // AdorableHamsterPets.LOGGER.trace("[PlayerTickMixin] Executing dismount for player {}.", self.getName().getString());
                    HamsterEntity.spawnFromShoulderData((ServerWorld) self.getWorld(), self, shoulderData);
                    // AdorableHamsterPets.LOGGER.trace("[PlayerTickMixin] Removing shoulder data from player {}.", self.getName().getString());
                    self.removeAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA);
                    adorablehamsterpets$diamondCheckTimer = 0; // Reset timers on dismount
                    adorablehamsterpets$creeperCheckTimer = 0;
                    return; // Don't tick fake AI if dismounting this tick
                }

                // Only tick fake AI if not dismounting
                tickFakeAI(self, shoulderData);
            }
        }
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), cancellable = true)
    private void adorablehamsterpets$onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity self = (PlayerEntity)(Object)this;

        if (!self.getWorld().isClient && amount > 0.0f) {
            HamsterShoulderData shoulderData = self.getAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA);
            if (shoulderData != null) {
                // AdorableHamsterPets.LOGGER.trace("[PlayerDamageMixin] Player {} took damage ({}), dismounting hamster.", self.getName().getString(), amount);
                HamsterEntity.spawnFromShoulderData((ServerWorld) self.getWorld(), self, shoulderData);
                // AdorableHamsterPets.LOGGER.trace("[PlayerDamageMixin] Removing shoulder data from player {}.", self.getName().getString());
                self.removeAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA);
                adorablehamsterpets$diamondCheckTimer = 0; // Reset timers on dismount
                adorablehamsterpets$creeperCheckTimer = 0;
            }
        }
    }


    @Unique
    private void tickFakeAI(PlayerEntity player, HamsterShoulderData data) {
        adorablehamsterpets$diamondCheckTimer++;
        if (adorablehamsterpets$diamondCheckTimer >= CHECK_INTERVAL) {
            adorablehamsterpets$diamondCheckTimer = 0;
            if (isDiamondNearby(player)) {
                player.getWorld().playSound(null, player.getBlockPos(),
                        ModSounds.getRandomSoundFrom(ModSounds.HAMSTER_DIAMOND_SNIFF_SOUNDS, player.getRandom()),
                        SoundCategory.NEUTRAL, 1.0f, 1.0f);
                // AdorableHamsterPets.LOGGER.trace("[PlayerTickMixin] Player {} triggered fake diamond sniff sound.", player.getName().getString());
            }
        }

        adorablehamsterpets$creeperCheckTimer++;
        if (adorablehamsterpets$creeperCheckTimer >= CHECK_INTERVAL) {
            adorablehamsterpets$creeperCheckTimer = 0;
            if (creeperSeesPlayer(player)) {
                player.getWorld().playSound(null, player.getBlockPos(),
                        ModSounds.getRandomSoundFrom(ModSounds.HAMSTER_CREEPER_DETECT_SOUNDS, player.getRandom()),
                        SoundCategory.NEUTRAL, 1.0f, 1.0f);
                // AdorableHamsterPets.LOGGER.trace("[PlayerTickMixin] Player {} triggered fake creeper detect sound.", player.getName().getString());
            }
        }
    }

    @Unique
    private boolean isDiamondNearby(PlayerEntity player) {
        int radius = 5;
        World world = player.getWorld();
        BlockPos center = player.getBlockPos();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockPos check = center.add(dx, dy, dz);
                    if (world.getBlockState(check).isOf(Blocks.DIAMOND_ORE)
                            || world.getBlockState(check).isOf(Blocks.DEEPSLATE_DIAMOND_ORE)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Unique
    private boolean creeperSeesPlayer(PlayerEntity player) {
        World world = player.getWorld();
        List<CreeperEntity> creepers = world.getEntitiesByClass(
                CreeperEntity.class,
                new Box(player.getBlockPos()).expand(16.0),
                c -> c.getTarget() == player // Check if the creeper's target is the player
        );
        return !creepers.isEmpty();
    }
}