package mr.bashyal.chickenmod.registry;

import mr.bashyal.chickenmod.entity.MountableChickenEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntities {
    public static final EntityType<MountableChickenEntity> MOUNTABLE_CHICKEN =
        Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of("chickenmod", "mountable_chicken"),
            EntityType.Builder.create(MountableChickenEntity::new, SpawnGroup.CREATURE)
                .build()
        );

    public static void register() {
        // Entities registered via static initializer
    }
}
