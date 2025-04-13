package net.dawson.adorablehamsterpets.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;

public record HamsterShoulderData(
        int variantId,
        float health,
        NbtCompound inventoryNbt,
        boolean leftCheekFull,
        boolean rightCheekFull,
        int breedingAge,
        long throwCooldownEndTick // <-- ADDED FIELD
) {

    // Codec for NbtCompound (Keep this)
    public static final Codec<NbtCompound> NBT_COMPOUND_CODEC = Codec.PASSTHROUGH.comapFlatMap(
            (dynamic) -> {
                NbtElement element = dynamic.convert(NbtOps.INSTANCE).getValue();
                if (element instanceof NbtCompound compound) {
                    return DataResult.success(compound);
                }
                return DataResult.error(() -> "Not a compound NBT: " + element);
            },
            (nbt) -> new Dynamic<>(NbtOps.INSTANCE, nbt)
    );

    // Update main Codec to include new fields
    public static final Codec<HamsterShoulderData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("variantId").forGetter(HamsterShoulderData::variantId),
                    Codec.FLOAT.fieldOf("health").forGetter(HamsterShoulderData::health),
                    NBT_COMPOUND_CODEC.fieldOf("inventoryNbt").forGetter(HamsterShoulderData::inventoryNbt),
                    Codec.BOOL.fieldOf("leftCheekFull").forGetter(HamsterShoulderData::leftCheekFull),
                    Codec.BOOL.fieldOf("rightCheekFull").forGetter(HamsterShoulderData::rightCheekFull),
                    Codec.INT.fieldOf("breedingAge").forGetter(HamsterShoulderData::breedingAge),
                    Codec.LONG.fieldOf("throwCooldownEndTick").forGetter(HamsterShoulderData::throwCooldownEndTick) // <-- ADDED CODEC PART
            ).apply(instance, HamsterShoulderData::new)
    );

    @Override
    public String toString() {
        // Add new field for logging clarity
        return "HamsterShoulderData[variantId=" + variantId +
                ", health=" + health +
                ", inventoryNbt=" + inventoryNbt.toString().substring(0, Math.min(inventoryNbt.toString().length(), 50)) + "..." +
                ", leftFull=" + leftCheekFull +
                ", rightFull=" + rightCheekFull +
                ", age=" + breedingAge +
                ", cooldownEnd=" + throwCooldownEndTick + // <-- ADDED TO STRING
                "]";
    }

    public static HamsterShoulderData empty() {
        // Add default value for new field
        return new HamsterShoulderData(0, 8.0f, new NbtCompound(), false, false, 0, 0L); // <-- ADDED DEFAULT (0L)
    }
}