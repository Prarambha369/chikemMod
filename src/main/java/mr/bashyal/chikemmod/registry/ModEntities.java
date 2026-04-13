package mr.bashyal.chikemmod.registry;

import mr.bashyal.chikemmod.ChikemMod;
import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<MountableChickenEntity> MOUNTABLE_CHICKEN = Registry.register(
        Registries.ENTITY_TYPE,
        Identifier.of(ChikemMod.MOD_ID, "mountable_chicken"),
        EntityType.Builder.<MountableChickenEntity>create(MountableChickenEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.4f, 0.7f)
            .maxTrackingRange(10)
            .build()
    );

    public static void initialize() {
        ChikemMod.LOGGER.info("Registering entities for " + ChikemMod.MOD_ID);

        // Required in 1.21+: without this, custom living entities can crash on spawn/breeding.
        FabricDefaultAttributeRegistry.register(MOUNTABLE_CHICKEN, ChickenEntity.createChickenAttributes());
    }
}