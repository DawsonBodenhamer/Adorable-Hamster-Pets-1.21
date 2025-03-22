package net.dawson.adorablehamsterpets.entity.AI;

import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.dawson.adorablehamsterpets.item.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * This goal replicates CatEntity's logic:
 * - Flee from players if hamster is NOT tamed and the player is NOT crouching with SLICED_CUCUMBER.
 * - Flee from all hostile mobs if hamster is NOT tamed.
 */
public class HamsterFleeGoal<T extends LivingEntity> extends FleeEntityGoal<T> {

    private final HamsterEntity hamster;

    public HamsterFleeGoal(
            HamsterEntity hamster,
            Class<T> fleeFromType,
            float distance,
            double slowSpeed,
            double fastSpeed
    ) {
        // We pass a custom 'fleeFromPredicate' in super(...).
        // We'll fill that in below.
        super(
                hamster,
                fleeFromType,
                distance,
                slowSpeed,
                fastSpeed,
                living -> shouldFlee(hamster, living) // custom static method
        );
        this.hamster = hamster;
    }

    /**
     * The main logic to decide if the hamster should flee from 'living'.
     */
    private static boolean shouldFlee(HamsterEntity hamster, LivingEntity living) {
        // If hamster is tamed, no fleeing from players or mobs by default:
        if (hamster.isTamed()) {
            return false;
        }

        // If hamster is NOT tamed, check:
        // 1) Is it a hostile mob?
        if (living instanceof HostileEntity) {
            return true; // Always flee from hostile if wild
        }

        // 2) Is it a player?
        if (living instanceof PlayerEntity player) {
            // a) If the player is crouching with SLICED_CUCUMBER,
            //    do NOT flee.
            return !isCrouchingWithCucumber(player);
            // otherwise, yes flee from this player
        }

        // If it’s any other entity (like a cow, sheep, etc.),
        // we can choose not to flee them:
        return false;
    }

    private static boolean isCrouchingWithCucumber(PlayerEntity player) {
        if (!player.isSneaking()) {
            return false;
        }
        // Check main-hand or off-hand for SLICED_CUCUMBER
        ItemStack main = player.getMainHandStack();
        ItemStack off = player.getOffHandStack();
        boolean mainIsCucumber = main.isOf(ModItems.SLICED_CUCUMBER);
        boolean offIsCucumber  = off.isOf(ModItems.SLICED_CUCUMBER);
        return (mainIsCucumber || offIsCucumber);
    }
}
