package net.dawson.adorablehamsterpets.entity;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import net.dawson.adorablehamsterpets.entity.custom.MantisEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<MantisEntity> MANTIS = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(AdorableHamsterPets.MOD_ID, "mantis"),
            EntityType.Builder.create(MantisEntity::new, SpawnGroup.CREATURE)
                    .dimensions(1f, 2.5f).build());

    public static final EntityType<HamsterEntity> HAMSTER = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(AdorableHamsterPets.MOD_ID, "hamster"),
            EntityType.Builder.create(HamsterEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.5F, 0.5F).build());



    public static void  registerModEntities() {
        AdorableHamsterPets.LOGGER.info("Registering Mod Entities for " + AdorableHamsterPets.MOD_ID);
    }
}
