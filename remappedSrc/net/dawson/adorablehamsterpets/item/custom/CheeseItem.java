package net.dawson.adorablehamsterpets.item.custom;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.attachment.ModEntityAttachments;
import net.dawson.adorablehamsterpets.attachment.HamsterShoulderData;
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import java.util.List; // Import List

public class CheeseItem extends Item {

    private static final double TELEPORT_RADIUS = 50.0;

    public CheeseItem(net.minecraft.item.Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            // --- ADDED: Sneaking Check ---
            if (user.isSneaking()) {
                user.sendMessage(Text.literal("Cannot mount hamster while sneaking."), true); // Optional message
                AdorableHamsterPets.LOGGER.trace("[CheeseItem] use: Player {} is sneaking, preventing mount.", user.getName().getString());
                return TypedActionResult.fail(user.getStackInHand(hand)); // Fail action
            }
            // --- END ADDED ---

            if (user.getAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA) != null) {
                AdorableHamsterPets.LOGGER.trace("[CheeseItem] use: Player {} already has shoulder data. Aborting search.", user.getName().getString());
                return TypedActionResult.pass(user.getStackInHand(hand));
            }

            AdorableHamsterPets.LOGGER.trace("[CheeseItem] use: Server side. Searching for hamster...");
            HamsterEntity hamster = findNearestOwnedHamster(world, user);

            if (hamster != null) {
                AdorableHamsterPets.LOGGER.trace("[CheeseItem] use: Found hamster ID {}. Preparing to store data.", hamster.getId());
                HamsterShoulderData data = hamster.saveToShoulderData();
                AdorableHamsterPets.LOGGER.trace("[CheeseItem] use: Data created: {}", data);

                AdorableHamsterPets.LOGGER.trace("[CheeseItem] use: Attaching data to player {}...", user.getName().getString());
                HamsterShoulderData previousData = user.setAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA, data);
                AdorableHamsterPets.LOGGER.trace("[CheeseItem] use: setAttached called. Previous data was null? {}", previousData == null);
                AdorableHamsterPets.LOGGER.trace("[CheeseItem] use: Verification getAttached result: {}", user.getAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA));


                AdorableHamsterPets.LOGGER.trace("[CheeseItem] use: Discarding hamster ID {}...", hamster.getId());
                hamster.discard();
                AdorableHamsterPets.LOGGER.trace("[CheeseItem] use: Hamster ID {} discarded.", hamster.getId());

                user.sendMessage(Text.literal("Your hamster scurries onto your shoulder!"), true); // Message success

                if (!user.getAbilities().creativeMode) {
                    user.getStackInHand(hand).decrement(1);
                }
                return TypedActionResult.success(user.getStackInHand(hand)); // Success
            } else {
                AdorableHamsterPets.LOGGER.trace("[CheeseItem] use: No owned hamster found.");
                user.sendMessage(Text.literal("No owned hamster found within " + TELEPORT_RADIUS + " blocks."), true); // Keep no-hamster message
                return TypedActionResult.fail(user.getStackInHand(hand)); // Fail if no hamster found
            }
        }
        AdorableHamsterPets.LOGGER.trace("[CheeseItem] use: Client side. Passing.");
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResult.PASS;
        World world = context.getWorld();

        if (!world.isClient) {
            // --- ADDED: Sneaking Check ---
            if (player.isSneaking()) {
                player.sendMessage(Text.literal("Cannot mount hamster while sneaking."), true); // Optional message
                AdorableHamsterPets.LOGGER.trace("[CheeseItem] useOnBlock: Player {} is sneaking, preventing mount.", player.getName().getString());
                return ActionResult.FAIL; // Fail action
            }
            // --- END ADDED ---

            if (player.getAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA) != null) {
                AdorableHamsterPets.LOGGER.trace("[CheeseItem] useOnBlock: Player {} already has shoulder data. Aborting search.", player.getName().getString());
                return ActionResult.PASS;
            }

            AdorableHamsterPets.LOGGER.trace("[CheeseItem] useOnBlock: Server side. Searching for hamster...");
            HamsterEntity hamster = findNearestOwnedHamster(world, player);

            if (hamster != null) {
                AdorableHamsterPets.LOGGER.trace("[CheeseItem] useOnBlock: Found hamster ID {}. Preparing to store data.", hamster.getId());
                HamsterShoulderData data = hamster.saveToShoulderData();
                AdorableHamsterPets.LOGGER.trace("[CheeseItem] useOnBlock: Data created: {}", data);

                AdorableHamsterPets.LOGGER.trace("[CheeseItem] useOnBlock: Attaching data to player {}...", player.getName().getString());
                HamsterShoulderData previousData = player.setAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA, data);
                AdorableHamsterPets.LOGGER.trace("[CheeseItem] useOnBlock: setAttached called. Previous data was null? {}", previousData == null);
                AdorableHamsterPets.LOGGER.trace("[CheeseItem] useOnBlock: Verification getAttached result: {}", player.getAttached(ModEntityAttachments.HAMSTER_SHOULDER_DATA));


                AdorableHamsterPets.LOGGER.trace("[CheeseItem] useOnBlock: Discarding hamster ID {}...", hamster.getId());
                hamster.discard();
                AdorableHamsterPets.LOGGER.trace("[CheeseItem] useOnBlock: Hamster ID {} discarded.", hamster.getId());

                player.sendMessage(Text.literal("Your hamster scurries onto your shoulder!"), true); // Message success

                if (!player.getAbilities().creativeMode) {
                    player.getStackInHand(context.getHand()).decrement(1);
                }
                return ActionResult.SUCCESS; // Success
            } else {
                AdorableHamsterPets.LOGGER.trace("[CheeseItem] useOnBlock: No owned hamster found.");
                player.sendMessage(Text.literal("No owned hamster found within " + TELEPORT_RADIUS + " blocks."), true); // Keep no-hamster message
                return ActionResult.FAIL; // Fail if no hamster found
            }
        }
        AdorableHamsterPets.LOGGER.trace("[CheeseItem] useOnBlock: Client side. Passing.");
        return ActionResult.PASS;
    }

    // findNearestOwnedHamster remains the same
    private HamsterEntity findNearestOwnedHamster(World world, PlayerEntity player) {
        // ... existing code ...
        Box searchBox = new Box(
                player.getX() - TELEPORT_RADIUS, player.getY() - TELEPORT_RADIUS, player.getZ() - TELEPORT_RADIUS,
                player.getX() + TELEPORT_RADIUS, player.getY() + TELEPORT_RADIUS, player.getZ() + TELEPORT_RADIUS
        );

        HamsterEntity nearest = null;
        double nearestDistSq = TELEPORT_RADIUS * TELEPORT_RADIUS; // Use squared distance

        for (HamsterEntity h : world.getEntitiesByClass(HamsterEntity.class, searchBox, e -> e.isTamed() && e.isOwner(player))) {
            // Check ownership using isOwner for safety
            // if (h.isTamed() && h.getOwnerUuid() != null && h.getOwnerUuid().equals(player.getUuid())) { // Alternative check
            double distSq = h.squaredDistanceTo(player);
            if (distSq < nearestDistSq) {
                nearestDistSq = distSq;
                nearest = h;
            }
            // }
        }
        return nearest;
    }

    // --- Add Tooltip Method ---
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.adorablehamsterpets.cheese.hint1").formatted(Formatting.GOLD)); // §6 Special use
        tooltip.add(Text.translatable("tooltip.adorablehamsterpets.cheese.hint2").formatted(Formatting.GRAY)); // §7 Humorous description
        super.appendTooltip(stack, context, tooltip, type);
    }
}