package mr.bashyal.chickenmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ChickenMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // Register custom commands using Fabric API
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ChickenmodCommands.register(dispatcher);
        });
    }
}