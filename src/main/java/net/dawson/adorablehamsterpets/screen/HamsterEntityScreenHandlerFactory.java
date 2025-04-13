package net.dawson.adorablehamsterpets.screen;

import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf; // Keep if needed elsewhere
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

// Specify the data type D as Integer (for the entity ID)
public class HamsterEntityScreenHandlerFactory implements ExtendedScreenHandlerFactory<Integer> {
    private final HamsterEntity entity;

    public HamsterEntityScreenHandlerFactory(HamsterEntity entity) {
        this.entity = entity;
    }

    @Override
    public Text getDisplayName() {
        // Using the key you added to en_us.json
        return Text.translatable("entity.adorablehamsterpets.hamster.inventory_title");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        // Pass the HamsterEntity itself, as it implements Inventory via ImplementedInventory
        return new HamsterInventoryScreenHandler(syncId, playerInventory, this.entity);
    }

    @Override
    public Integer getScreenOpeningData(ServerPlayerEntity player) { // Return type is Integer
        // Return the hamster's entity ID. This data will be encoded using the PacketCodec
        // specified during registration and sent to the client.
        return entity.getId();
    }
}