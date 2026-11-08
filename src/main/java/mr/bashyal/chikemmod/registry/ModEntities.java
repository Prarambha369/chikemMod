package mr.bashyal.chikemmod.registry;

import mr.bashyal.chikemmod.ChikemMod;
import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    private static final Identifier MOUNTABLE_CHICKEN_ID = Identifier.fromNamespaceAndPath(ChikemMod.MOD_ID, "mountable_chicken");

    public static final EntityType<MountableChickenEntity> MOUNTABLE_CHICKEN = Registry.register(
        BuiltInRegistries.ENTITY_TYPE,
        MOUNTABLE_CHICKEN_ID,
        EntityType.Builder.<MountableChickenEntity>of(MountableChickenEntity::new, MobCategory.CREATURE)
            .sized(0.4F, 0.7F)
            .clientTrackingRange(10)
            .updateInterval(1)
            .build(ResourceKey.create(BuiltInRegistries.ENTITY_TYPE.key(), MOUNTABLE_CHICKEN_ID))
    );

    public static void initialize() {
        ChikemMod.LOGGER.info("Registering entities for " + ChikemMod.MOD_ID);
        FabricDefaultAttributeRegistry.register(MOUNTABLE_CHICKEN, net.minecraft.world.entity.animal.chicken.Chicken.createAttributes());
    }
}
