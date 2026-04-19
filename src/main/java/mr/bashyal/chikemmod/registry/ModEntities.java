package mr.bashyal.chikemmod.registry;

import mr.bashyal.chikemmod.ChikemMod;
import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    private static final Identifier MOUNTABLE_CHICKEN_ID = Identifier.of(ChikemMod.MOD_ID, "mountable_chicken");
    private static final RegistryKey<EntityType<?>> MOUNTABLE_CHICKEN_KEY = RegistryKey.of(RegistryKeys.ENTITY_TYPE, MOUNTABLE_CHICKEN_ID);

    public static final EntityType<MountableChickenEntity> MOUNTABLE_CHICKEN = Registry.register(
        Registries.ENTITY_TYPE,
        MOUNTABLE_CHICKEN_ID,
        EntityType.Builder.<MountableChickenEntity>create(MountableChickenEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.4f, 0.7f)
            .maxTrackingRange(10)
            .build(MOUNTABLE_CHICKEN_KEY)
    );

    public static void initialize() {
        ChikemMod.LOGGER.info("Registering entities for " + ChikemMod.MOD_ID);

        // Required in 1.21+: without this, custom living entities can crash on spawn/breeding.
        FabricDefaultAttributeRegistry.register(MOUNTABLE_CHICKEN, ChickenEntity.createChickenAttributes());
    }
}