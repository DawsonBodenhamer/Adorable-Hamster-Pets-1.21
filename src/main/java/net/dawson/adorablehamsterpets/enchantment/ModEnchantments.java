//package net.dawson.adorablehamsterpets.enchantment;
//
//import net.dawson.adorablehamsterpets.AdorableHamsterPets;
//import net.dawson.adorablehamsterpets.enchantment.custom.LightningStrikerEnchantmentEffect;
//import net.minecraft.component.EnchantmentEffectComponentTypes;
//import net.minecraft.component.type.AttributeModifierSlot;
//import net.minecraft.enchantment.Enchantment;
//import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
//import net.minecraft.registry.Registerable;
//import net.minecraft.registry.RegistryKey;
//import net.minecraft.registry.RegistryKeys;
//import net.minecraft.registry.tag.EnchantmentTags;
//import net.minecraft.registry.tag.ItemTags;
//import net.minecraft.util.Identifier;
//
//public class ModEnchantments {
//    public static final RegistryKey<Enchantment> LIGHTING_STRIKER =
//            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(AdorableHamsterPets.MOD_ID, "lighting_striker"));
//
//    public static void bootstrap(Registerable<Enchantment> registerable) {
//        var enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
//        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);
//
//        register(registerable, LIGHTING_STRIKER, Enchantment.builder(Enchantment.definition(
//                items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
//                items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
//                5,
//                2,
//                Enchantment.leveledCost(5, 7),
//                Enchantment.leveledCost(25, 9),
//                2,
//                AttributeModifierSlot.MAINHAND))
//                .exclusiveSet(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE_SET))
//                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
//                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM,
//                        new LightningStrikerEnchantmentEffect()));
//    }
//
//    private static void register(Registerable<Enchantment> registery, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
//        registery.register(key, builder.build(key.getValue()));
//    }
//}
