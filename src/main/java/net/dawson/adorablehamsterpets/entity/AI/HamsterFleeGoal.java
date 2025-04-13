package net.dawson.adorablehamsterpets.entity.AI;

import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class HamsterFleeGoal<T extends LivingEntity> extends FleeEntityGoal<T> {

    private final HamsterEntity hamster;

    public HamsterFleeGoal(
            HamsterEntity hamster,
            Class<T> fleeFromType,
            float distance,
            double slowSpeed,
            double fastSpeed
    ) {
        super(
                hamster,
                fleeFromType,
                distance,
                slowSpeed,
                fastSpeed,
                living -> shouldFlee(hamster, living) // Use private helper
        );
        this.hamster = hamster;
    }

    // Keep shouldFlee private as it uses instance field 'hamster'
    private static boolean shouldFlee(HamsterEntity hamster, LivingEntity living) {
        if (hamster.isTamed()) {
            return false; // Tamed hamsters don't flee this way
        }

        if (living instanceof HostileEntity) {
            return true; // Always flee hostiles
        }

        if (living instanceof PlayerEntity player) {
            // Flee players unless they are safe (sneaking with cucumber)
            return !isPlayerSafe(player);
        }

        return false; // Don't flee other non-hostile entities
    }

    // --- Make this helper public static ---
    public static boolean isPlayerSafe(PlayerEntity player) {
        if (!player.isSneaking()) {
            return false;
        }
        ItemStack main = player.getMainHandStack();
        ItemStack off = player.getOffHandStack();
        boolean mainIsCucumber = main.isOf(ModItems.SLICED_CUCUMBER);
        boolean offIsCucumber  = off.isOf(ModItems.SLICED_CUCUMBER);
        return (mainIsCucumber || offIsCucumber);
    }
    // --- End Change ---
}