package mr.bashyal.chickenmod.registry;

import mr.bashyal.chickenmod.entity.MountableChickenEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntities {
    public static final EntityType<MountableChickenEntity> MOUNTABLE_CHICKEN =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    new Identifier("chickenmod", "mountable_chicken"),
                    FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MountableChickenEntity::new)
                            .dimensions(0.6F, 0.8F)
                            .build()
            );

    public static void register() {
        // Called from Chickenmod.onInitialize
    }
}

