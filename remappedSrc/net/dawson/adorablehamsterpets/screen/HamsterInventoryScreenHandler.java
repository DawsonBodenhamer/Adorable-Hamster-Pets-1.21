package net.dawson.adorablehamsterpets.screen;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.dawson.adorablehamsterpets.screen.slot.HamsterSlot; // Ensure this import is correct
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

public class HamsterInventoryScreenHandler extends ScreenHandler {
    private final Inventory inventory; // This will hold the HamsterEntity instance or SimpleInventory
    private final PlayerEntity player;
    private final int entityId; // Store the entity ID for client-side lookup
    // Store the instance for quickMove check, can be null on client if entity not found
    @org.jetbrains.annotations.Nullable
    private final HamsterEntity hamsterEntityInstance;

    // Server-side constructor - accepts HamsterEntity
    public HamsterInventoryScreenHandler(int syncId, PlayerInventory playerInventory, HamsterEntity hamsterEntity) {
        super(ModScreenHandlers.HAMSTER_INVENTORY_SCREEN_HANDLER, syncId);
        this.inventory = hamsterEntity;
        this.hamsterEntityInstance = hamsterEntity; // Store instance
        checkSize(this.inventory, 6); // Check the size
        this.player = playerInventory.player;
        this.entityId = hamsterEntity.getId();
        setupSlots(playerInventory);
        this.inventory.onOpen(playerInventory.player);
    }

    // Client-side constructor (called by the ExtendedFactory in ModScreenHandlers)
    public HamsterInventoryScreenHandler(int syncId, PlayerInventory playerInventory, int entityId) {
        super(ModScreenHandlers.HAMSTER_INVENTORY_SCREEN_HANDLER, syncId);
        this.player = playerInventory.player;
        this.entityId = entityId;

        World world = player.getWorld();
        Entity entity = world.getEntityById(entityId);

        if (entity instanceof HamsterEntity hamster) {
            this.inventory = hamster;
            this.hamsterEntityInstance = hamster; // Store instance
            checkSize(this.inventory, 6);
        } else {
            AdorableHamsterPets.LOGGER.warn("Could not find HamsterEntity with ID {} on client, using empty inventory.", entityId);
            // Use SimpleInventory as fallback ONLY if entity not found/wrong type
            this.inventory = new SimpleInventory(6);
            this.hamsterEntityInstance = null; // No instance available
        }

        setupSlots(playerInventory);
        this.inventory.onOpen(playerInventory.player);
    }

    public int getEntityId() {
        return this.entityId;
    }

    // Helper method to set up slots
    private void setupSlots(PlayerInventory playerInventory) {
        int m;
        int l;

        // Hamster Cheek Pouch Slots (Using HamsterSlot)
        this.addSlot(new HamsterSlot(this.inventory, 0, 26, 95)); // Slot 0 (Inv Index 0)
        this.addSlot(new HamsterSlot(this.inventory, 1, 44, 95)); // Slot 1 (Inv Index 1)
        this.addSlot(new HamsterSlot(this.inventory, 2, 62, 95)); // Slot 2 (Inv Index 2)
        // Gap Slot
        this.addSlot(new Slot(new SimpleInventory(1), 0, 80, 95) {
            @Override public boolean canInsert(ItemStack stack) { return false; }
            @Override public boolean canTakeItems(PlayerEntity playerEntity) { return false; }
            @Override public boolean isEnabled() { return false; }
        }); // Slot 3 (Gap)
        this.addSlot(new HamsterSlot(this.inventory, 3, 98, 95));  // Slot 4 (Inv Index 3)
        this.addSlot(new HamsterSlot(this.inventory, 4, 116, 95)); // Slot 5 (Inv Index 4)
        this.addSlot(new HamsterSlot(this.inventory, 5, 134, 95)); // Slot 6 (Inv Index 5)

        // Player Inventory Slots
        int playerInvX = 8;
        int playerInvY = 140;
        for (l = 0; l < 3; ++l) {
            for (m = 0; m < 9; ++m) {
                // Player inventory slots start at index 7 in the handler's list
                this.addSlot(new Slot(playerInventory, m + l * 9 + 9, playerInvX + m * 18, playerInvY + l * 18));
            }
        }

        // Player Hotbar Slots
        int hotbarX = 8;
        int hotbarY = 198;
        for (m = 0; m < 9; ++m) {
            // Hotbar slots start after main inventory (index 7 + 27 = 34)
            this.addSlot(new Slot(playerInventory, m, hotbarX + m * 18, hotbarY));
        }
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    // Shift + Click Logic (Revised for clarity and correctness)
    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        // --- Description: Handle shift-clicking items between player and hamster inventories ---
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(slotIndex);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy(); // Copy the original stack

            // Define slot index ranges based on setupSlots
            int hamsterInvSize = 6; // Actual number of slots in hamster's inventory
            int gapSlotIndex = 3;   // Index of the visual gap slot in the handler's list
            int totalHamsterAreaSlots = hamsterInvSize + 1; // 7 slots (0-6) including the gap

            int playerInvStartIndex = totalHamsterAreaSlots; // Player inventory starts at index 7
            int playerHotbarStartIndex = playerInvStartIndex + 27; // Hotbar starts at index 34
            int playerTotalEndIndex = playerHotbarStartIndex + 9; // Total slots = 43 (indices 0-42)

            // --- Case 1: Moving FROM Hamster Inventory TO Player ---
            if (slotIndex < totalHamsterAreaSlots && slotIndex != gapSlotIndex) {
                // Try merging into player inventory (hotbar first, then main inventory)
                // The 'true' argument reverses the insertion order (tries hotbar last)
                if (!this.insertItem(itemStack2, playerInvStartIndex, playerTotalEndIndex, true)) {
                    return ItemStack.EMPTY; // Failed to move to player
                }
            }
            // --- Case 2: Moving FROM Player Inventory/Hotbar TO Hamster ---
            else if (slotIndex >= playerInvStartIndex) {
                // --- Add Disallowed Item Check ---
                if (this.hamsterEntityInstance != null && this.hamsterEntityInstance.isItemDisallowed(itemStack2)) {
                    return ItemStack.EMPTY; // Prevent disallowed items from being moved in
                }
                // --- End Check ---

                // Try merging into the hamster slots (excluding the gap)
                // Attempt first row (slots 0, 1, 2)
                if (!this.insertItem(itemStack2, 0, gapSlotIndex, false)) {
                    // Attempt second row (slots 4, 5, 6 - corresponding to handler indices gapSlotIndex+1 to totalHamsterAreaSlots)
                    if (!this.insertItem(itemStack2, gapSlotIndex + 1, totalHamsterAreaSlots, false)) {
                        return ItemStack.EMPTY; // Failed to move to hamster
                    }
                }
            }
            // --- Case 3: Clicked the Gap Slot or other invalid area ---
            // This case should ideally not happen if the slot exists, but added for completeness
            else {
                return ItemStack.EMPTY;
            }


            // Final cleanup after attempting insertion
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                // If count is unchanged, transfer failed
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
        }

        return itemStack; // Return the successfully moved stack (or its remainder)
        // --- End Description ---
    }


    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
    }
}