package mr.bashyal.chikemmod.registry;

import mr.bashyal.chikemmod.entity.GoldenEggEntity;
import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntities {
    public static final EntityType<MountableChickenEntity> MOUNTABLE_CHICKEN = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of("chikemmod", "mountable_chicken"),
            EntityType.Builder.create(MountableChickenEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.4F, 0.7F)
                    .build()
    );

    public static final EntityType<GoldenEggEntity> GOLDEN_EGG_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of("chikemmod", "golden_egg_entity"),
            EntityType.Builder.<GoldenEggEntity>create(GoldenEggEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25F, 0.25F)
                    .build()
    );

    public static void register() {
        // Entities registered via static initializer
        // Spawn rare mountable chickens naturally in overworld
        BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld(), SpawnGroup.CREATURE,
                MOUNTABLE_CHICKEN, 1, 1, 1);
    }
}
