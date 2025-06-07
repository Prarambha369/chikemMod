package mr.bashyal.chickenmod.registry;

import mr.bashyal.chickenmod.entity.MountableChickenEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class ModEntityAttributes {
    public static void register() {x
        FabricDefaultAttributeRegistry.register(ModEntities.MOUNTABLE_CHICKEN, MountableChickenEntity.createAttributes());
    }
}

