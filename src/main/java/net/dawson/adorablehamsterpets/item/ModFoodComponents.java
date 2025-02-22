package net.dawson.adorablehamsterpets.item;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class ModFoodComponents {

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

    public static final FoodComponent HAMSTER_FOOD_MIX = new FoodComponent.Builder()
            .nutrition(4)
            .saturationModifier(0.6F)
            .build();
}
