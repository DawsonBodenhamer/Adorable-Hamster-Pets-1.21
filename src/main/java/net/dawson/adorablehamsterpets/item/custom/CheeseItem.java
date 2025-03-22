package net.dawson.adorablehamsterpets.item.custom;

import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

/**
 * A custom item that, when right-clicked anywhere, finds the nearest tamed Hamster
 * (owned by this player) within a large radius, teleports it to the player,
 * and sets "shoulderMountRequested = true" so the hamster
 * will mount the player's shoulder.
 */
public class CheeseItem extends Item {

    // You can choose a very large radius. It's usually safer than infinite,
    // as searching the entire world can cause lag in huge worlds.
    // 500 blocks in each direction is already quite big,
    // so a bounding box of radius=500 => a 1000×1000×1000 search volume.
    private static final double TELEPORT_RADIUS = 50.0;

    public CheeseItem(Settings settings) {
        super(settings);
    }

    // If you want to right-click in midair (i.e. not on a block) to work,
    // override this method:
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // Only run logic on the server side
        if (!world.isClient) {
            // Attempt to find the nearest hamster
            HamsterEntity hamster = findNearestOwnedHamster(world, user);

            if (hamster != null) {
                // Teleport hamster to the player
                // We can set the position just in front or exactly at player's feet
                hamster.setPosition(user.getX(), user.getY(), user.getZ());

                // Mark that we want the hamster to mount the shoulder
                hamster.shoulderMountRequested = true;

                // Send the player a message
                user.sendMessage(Text.literal("Your hamster scurries onto your shoulder!"), true);

                // If not in creative, consume cheese
                if (!user.getAbilities().creativeMode) {
                    user.getStackInHand(hand).decrement(1);
                }
                return TypedActionResult.success(user.getStackInHand(hand));
            } else {
                user.sendMessage(Text.literal("No owned hamster found within " + TELEPORT_RADIUS + " blocks."), true);
            }
        }
        // If we reach here, no hamster found, or we're on client side
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    // If you also want to right-click on a block to do the same thing, override "useOnBlock" as well
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResult.PASS;

        World world = context.getWorld();
        if (!world.isClient) {
            HamsterEntity hamster = findNearestOwnedHamster(world, player);
            if (hamster != null) {
                hamster.setPosition(player.getX(), player.getY(), player.getZ());
                hamster.shoulderMountRequested = true;

                if (!player.getAbilities().creativeMode) {
                    player.getStackInHand(context.getHand()).decrement(1);
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    /**
     * Finds the nearest Hamster owned by "player" within TELEPORT_RADIUS blocks.
     * Returns null if none found.
     */
    private HamsterEntity findNearestOwnedHamster(World world, PlayerEntity player) {
        // We'll search in a big bounding box around the player's position
        Box searchBox = new Box(
                player.getX() - TELEPORT_RADIUS, player.getY() - TELEPORT_RADIUS, player.getZ() - TELEPORT_RADIUS,
                player.getX() + TELEPORT_RADIUS, player.getY() + TELEPORT_RADIUS, player.getZ() + TELEPORT_RADIUS
        );

        HamsterEntity nearest = null;
        double nearestDist = Double.MAX_VALUE;

        // Gather all hamster entities in the area
        for (HamsterEntity h : world.getEntitiesByClass(HamsterEntity.class, searchBox, e -> true)) {
            if (h.isTamed() && h.getOwner() == player) {
                double dist = h.squaredDistanceTo(player);
                if (dist < nearestDist) {
                    nearestDist = dist;
                    nearest = h;
                }
            }
        }
        return nearest;
    }


}
