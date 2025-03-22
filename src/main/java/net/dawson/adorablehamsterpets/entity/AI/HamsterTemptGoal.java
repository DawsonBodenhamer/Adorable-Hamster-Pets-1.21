package net.dawson.adorablehamsterpets.entity.AI;

import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class HamsterTemptGoal extends TemptGoal {
    private final HamsterEntity hamster;
    private final Predicate<ItemStack> temptPredicate;
    private int recheckTimer = 0;

    public HamsterTemptGoal(HamsterEntity hamster, double speed, Predicate<ItemStack> predicate, boolean canBeScared) {
        super(hamster, speed, predicate, canBeScared);
        this.hamster = hamster;
        this.temptPredicate = predicate;
    }

    @Override
    public void tick() {
        super.tick();

        // We'll re-check more frequently
        if (recheckTimer > 0) {
            recheckTimer--;
            return;
        }
        recheckTimer = 5; // re-check roughly every 5 ticks

        World world = this.hamster.getWorld();
        if (world.isClient()) {
            return;
        }

        PlayerEntity nearestPlayer = world.getClosestPlayer(this.hamster, 8.0D);
        if (nearestPlayer != null && isHoldingTemptItem(nearestPlayer)) {
            this.hamster.setBegging(true);
        } else {
            this.hamster.setBegging(false);
        }
    }

    private boolean isHoldingTemptItem(PlayerEntity player) {
        ItemStack main = player.getMainHandStack();
        ItemStack off = player.getOffHandStack();
        return temptPredicate.test(main) || temptPredicate.test(off);
    }
}
