// This file is redundant. Chickenmod.java is the main entrypoint as per fabric.mod.json.
// Keeping this file for legacy reasons, but it should not be used or referenced.

// You can safely delete this file if you have no mods or loaders referencing it.

package mr.bashyal.chikemmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

/**
 * @deprecated This class is no longer the main entrypoint. Use Chickenmod.java instead.
 */
@Deprecated
public class ChickenMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // Register custom commands using Fabric API
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ChickenmodCommands.register(dispatcher);
        });
    }
}