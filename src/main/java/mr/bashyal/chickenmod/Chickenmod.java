package mr.bashyal.chickenmod;

import net.fabricmc.api.ModInitializer;
import mr.bashyal.chickenmod.registry.ModEntities;
import mr.bashyal.chickenmod.registry.ModEntityAttributes;

public class Chickenmod implements ModInitializer {

    @Override
    public void onInitialize() {
        ModEntities.register();
        ModEntityAttributes.register();
    }
}
