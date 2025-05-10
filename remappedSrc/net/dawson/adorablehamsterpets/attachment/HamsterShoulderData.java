package net.dawson.adorablehamsterpets.attachment;


import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;


import java.util.Optional;


public record HamsterShoulderData(
        int variantId,
        float health,
        NbtCompound inventoryNbt,
        boolean leftCheekFull,
        boolean rightCheekFull,
        int breedingAge,
        long throwCooldownEndTick,
        long steamedBeansCooldownEndTick,
        NbtList activeEffectsNbt,
        int autoEatCooldownTicks,
        Optional<String> customName
) {


    // Codec for NbtCompound 
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


    // Codec for NbtList 
    public static final Codec<NbtList> NBT_LIST_CODEC = Codec.PASSTHROUGH.comapFlatMap(
            (dynamic) -> {
                NbtElement element = dynamic.convert(NbtOps.INSTANCE).getValue();
                if (element instanceof NbtList list) {
                    return DataResult.success(list);
                }
                return DataResult.error(() -> "Not a list NBT: " + element);
            },
            (nbt) -> new Dynamic<>(NbtOps.INSTANCE, nbt)
    );


    public static final Codec<HamsterShoulderData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("variantId").forGetter(HamsterShoulderData::variantId),
                    Codec.FLOAT.fieldOf("health").forGetter(HamsterShoulderData::health),
                    NBT_COMPOUND_CODEC.fieldOf("inventoryNbt").forGetter(HamsterShoulderData::inventoryNbt),
                    Codec.BOOL.fieldOf("leftCheekFull").forGetter(HamsterShoulderData::leftCheekFull),
                    Codec.BOOL.fieldOf("rightCheekFull").forGetter(HamsterShoulderData::rightCheekFull),
                    Codec.INT.fieldOf("breedingAge").forGetter(HamsterShoulderData::breedingAge),
                    Codec.LONG.fieldOf("throwCooldownEndTick").forGetter(HamsterShoulderData::throwCooldownEndTick),
                    Codec.LONG.fieldOf("steamedBeansCooldownEndTick").forGetter(HamsterShoulderData::steamedBeansCooldownEndTick),
                    NBT_LIST_CODEC.fieldOf("activeEffectsNbt").forGetter(HamsterShoulderData::activeEffectsNbt),
                    Codec.INT.fieldOf("autoEatCooldownTicks").forGetter(HamsterShoulderData::autoEatCooldownTicks),
                    Codec.STRING.optionalFieldOf("customName").forGetter(HamsterShoulderData::customName)
            ).apply(instance, HamsterShoulderData::new)
    );


    @Override
    public String toString() {
        return "HamsterShoulderData[variantId=" + variantId +
                ", health=" + health +
                ", inventoryNbt=" + inventoryNbt.toString().substring(0, Math.min(inventoryNbt.toString().length(), 50)) + "..." +
                ", leftFull=" + leftCheekFull +
                ", rightFull=" + rightCheekFull +
                ", age=" + breedingAge +
                ", throwCooldownEnd=" + throwCooldownEndTick +
                ", beansCooldownEnd=" + steamedBeansCooldownEndTick +
                ", effectsNbtCount=" + activeEffectsNbt.size() +
                ", autoEatCooldown=" + autoEatCooldownTicks +
                ", customName=" + customName.orElse("None") +
                "]";
    }


    // Updated empty() method
    public static HamsterShoulderData empty() {
        return new HamsterShoulderData(0, 8.0f, new NbtCompound(), false, false, 0, 0L, 0L, new NbtList(), 0, Optional.empty());
    }
}