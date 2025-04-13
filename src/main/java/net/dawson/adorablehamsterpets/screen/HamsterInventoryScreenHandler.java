package net.dawson.adorablehamsterpets.screen;

import net.dawson.adorablehamsterpets.AdorableHamsterPets; // Import for logging if needed
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity; // Import HamsterEntity
import net.minecraft.client.MinecraftClient; // Import MinecraftClient (only needed if accessing client directly, often avoidable)
import net.minecraft.entity.Entity; // Import Entity
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World; // Import World

public class HamsterInventoryScreenHandler extends ScreenHandler {
    private final Inventory inventory; // This will hold the HamsterEntity instance
    private final PlayerEntity player;
    private final int entityId; // Store the entity ID for client-side lookup

    // Server-side constructor - accepts HamsterEntity
    public HamsterInventoryScreenHandler(int syncId, PlayerInventory playerInventory, HamsterEntity hamsterEntity) {
        super(ModScreenHandlers.HAMSTER_INVENTORY_SCREEN_HANDLER, syncId);
        // HamsterEntity implements Inventory, so we can assign it directly
        this.inventory = hamsterEntity;
        checkSize(this.inventory, 6); // Check the size of the inventory provided by the entity
        this.player = playerInventory.player; // Store player
        this.entityId = hamsterEntity.getId(); // Store the entity ID
        setupSlots(playerInventory); // Set up all the slots
        this.inventory.onOpen(playerInventory.player); // Notify inventory it's being opened
    }

    // Client-side constructor (called by the ExtendedFactory in ModScreenHandlers)
    public HamsterInventoryScreenHandler(int syncId, PlayerInventory playerInventory, int entityId) {
        super(ModScreenHandlers.HAMSTER_INVENTORY_SCREEN_HANDLER, syncId);
        this.player = playerInventory.player; // Store player
        this.entityId = entityId; // Store the entity ID received from server

        // --- Client-side entity lookup ---
        World world = player.getWorld(); // Get the world (should be client world)
        Entity entity = world.getEntityById(entityId); // Find entity by ID

        if (entity instanceof HamsterEntity hamster) {
            // Successfully found the hamster entity on the client
            // Assign the HamsterEntity itself as the inventory
            this.inventory = hamster;
            checkSize(this.inventory, 6); // Verify size again
        } else {
            // Fallback if entity not found or not a HamsterEntity on the client
            AdorableHamsterPets.LOGGER.warn("Could not find HamsterEntity with ID {} on client, using empty inventory.", entityId);
            this.inventory = new SimpleInventory(6);
        }
        // --- End client-side entity lookup ---

        setupSlots(playerInventory); // Set up all the slots
        // Use this.inventory which is now correctly assigned (either HamsterEntity or SimpleInventory)
        this.inventory.onOpen(playerInventory.player); // Notify inventory it's being opened
    }

    // Getter for the entity ID, needed by the Screen
    public int getEntityId() {
        return this.entityId;
    }

    // Helper method to set up slots
    private void setupSlots(PlayerInventory playerInventory) {
        int m;
        int l;

        // --- Hamster Cheek Pouch Slots --- (Already shifted in previous step)
        this.addSlot(new Slot(this.inventory, 0, 26, 95)); // Slot 1
        this.addSlot(new Slot(this.inventory, 1, 44, 95)); // Slot 2
        this.addSlot(new Slot(this.inventory, 2, 62, 95)); // Slot 3
        this.addSlot(new Slot(new SimpleInventory(1), 0, 80, 95) { /* Gap */
            @Override public boolean canInsert(ItemStack stack) { return false; }
            @Override public boolean canTakeItems(PlayerEntity playerEntity) { return false; }
            @Override public boolean isEnabled() { return false; }
        });
        this.addSlot(new Slot(this.inventory, 3, 98, 95));  // Slot 4
        this.addSlot(new Slot(this.inventory, 4, 116, 95)); // Slot 5
        this.addSlot(new Slot(this.inventory, 5, 134, 95)); // Slot 6
        // --- End Hamster Slots ---


        // --- Player Inventory Slots ---
        // Shifted right 1, down 1 from original base (7, 139)
        int playerInvX = 7 + 1;  // Now 8
        int playerInvY = 139 + 1; // Now 140
        for (l = 0; l < 3; ++l) {
            for (m = 0; m < 9; ++m) {
                this.addSlot(new Slot(playerInventory, m + l * 9 + 9, playerInvX + m * 18, playerInvY + l * 18));
            }
        }

        // --- Player Hotbar Slots ---
        // Shifted right 1, down 1 from original base (7, 197)
        int hotbarX = 7 + 1;  // Now 8
        int hotbarY = 197 + 1; // Now 198
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, hotbarX + m * 18, hotbarY));
        }
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        // Check if the inventory can be used by the player.
        // Add distance check later if needed.
        return this.inventory.canPlayerUse(player);
    }

    // Shift + Click Logic
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlotIndex) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlotIndex);

        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            // Define slot index ranges based on the setupSlots method
            int hamsterInvTotalSlots = 6; // Actual number of slots in the hamster's inventory
            int gapSlotIndex = 3;         // Index of the visual gap slot in the screen handler's list
            int hamsterSlotsEndIndex = hamsterInvTotalSlots + 1; // End index for hamster + gap slots in screen handler list (0-6)
            int playerInvStartIndex = hamsterSlotsEndIndex; // Player inventory starts right after the gap slot (index 7)
            int playerInvEndIndex = playerInvStartIndex + 27; // Player main inventory (indices 7 to 33)
            int hotbarStartIndex = playerInvEndIndex;         // Hotbar starts after main inventory (index 34)
            int hotbarEndIndex = hotbarStartIndex + 9;          // Hotbar end (indices 34 to 42)

            // Check if the click was in the Hamster's inventory slots (excluding the gap)
            if (invSlotIndex < hamsterSlotsEndIndex && invSlotIndex != gapSlotIndex) {
                // Try to move from Hamster inventory to Player inventory (hotbar first, then main)
                if (!this.insertItem(originalStack, hotbarStartIndex, hotbarEndIndex, false)) { // Try hotbar first
                    if (!this.insertItem(originalStack, playerInvStartIndex, playerInvEndIndex, false)) { // Then try main inventory
                        return ItemStack.EMPTY; // Failed to move to player inventory
                    }
                }
            }
            // Check if the click was in the Player's inventory (main or hotbar)
            else if (invSlotIndex >= playerInvStartIndex && invSlotIndex < hotbarEndIndex) {
                // Try to move from Player inventory to Hamster inventory
                // Attempt to insert into the first row (slots 0, 1, 2)
                if (!this.insertItem(originalStack, 0, gapSlotIndex, false)) {
                    // Attempt to insert into the second row (slots 4, 5, 6)
                    if (!this.insertItem(originalStack, gapSlotIndex + 1, hamsterSlotsEndIndex, false)) {
                        return ItemStack.EMPTY; // Failed to move to hamster inventory
                    }
                }
            }

            // If stack size is now 0, set the slot to empty
            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                // Otherwise, mark the slot as dirty
                slot.markDirty();
            }

            // If stack sizes are the same after attempting transfer, it means no transfer occurred
            if (originalStack.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }

            // Notify the slot that the item was taken
            slot.onTakeItem(player, originalStack);
        }

        return newStack;
    }


    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player); // Notify the actual inventory it was closed
    }
}