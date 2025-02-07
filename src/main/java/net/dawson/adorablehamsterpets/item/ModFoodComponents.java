package net.dawson.adorablehamsterpets.item;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class ModFoodComponents {
    public static final FoodComponent CAULIFLOWER = new FoodComponent.Builder()
            .nutrition(3)
            .saturationModifier(0.25f)
            .statusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 200), 0.15f)
            .build();

    public static final FoodComponent HONEY_BERRY = new FoodComponent.Builder()
            .nutrition(2)
            .saturationModifier(0.15f)
            .snack()
            .build();

    public static final FoodComponent CUCUMBER = new FoodComponent.Builder()
            .nutrition(2)
            .saturationModifier(0.3F)
            .build();

    public static final FoodComponent SLICED_CUCUMBER = new FoodComponent.Builder()
            .nutrition(4)
            .saturationModifier(0.6F)
            .build();

    public static final FoodComponent GREEN_BEANS = new FoodComponent.Builder()
            .nutrition(2)
            .saturationModifier(0.3F)
            .build();

    public static final FoodComponent STEAMED_GREEN_BEANS = new FoodComponent.Builder()
            .nutrition(4)
            .saturationModifier(0.6F)
            .build();

    public static final FoodComponent CHEESE = new FoodComponent.Builder()
            .nutrition(6)
            .saturationModifier(0.8F)
            .build();

    public static final FoodComponent HAMSTER_FOOD_MIX = new FoodComponent.Builder()
            .nutrition(4)
            .saturationModifier(0.6F)
            .build();
}
