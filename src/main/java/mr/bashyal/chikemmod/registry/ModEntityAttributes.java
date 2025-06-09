package mr.bashyal.chikemmod.registry;

import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class ModEntityAttributes {
    public static void register() {
        FabricDefaultAttributeRegistry.register(ModEntities.MOUNTABLE_CHICKEN, MountableChickenEntity.createAttributes());
    }
}

