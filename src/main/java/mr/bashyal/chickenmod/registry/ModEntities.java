package mr.bashyal.chickenmod.registry;

import mr.bashyal.chickenmod.entity.MountableChickenEntity;
import mr.bashyal.chickenmod.entity.GoldenEggEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.util.Identifier;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntities {
    public static final EntityType<MountableChickenEntity> MOUNTABLE_CHICKEN = Registry.register(
        Registries.ENTITY_TYPE,
        Identifier.of("chickenmod", "mountable_chicken"),
        EntityType.Builder.create(MountableChickenEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.4F, 0.7F)
            .build()
    );

    public static final EntityType<GoldenEggEntity> GOLDEN_EGG_ENTITY = Registry.register(
        Registries.ENTITY_TYPE,
        Identifier.of("chickenmod", "golden_egg_entity"),
        EntityType.Builder.<GoldenEggEntity>create(GoldenEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F)
            .build()
    );

    public static void register() {
        // Entities registered via static initializer
    }
}

