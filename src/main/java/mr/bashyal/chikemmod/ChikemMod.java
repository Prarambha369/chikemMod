package mr.bashyal.chikemmod;

import mr.bashyal.chikemmod.network.ModNetworking;
import mr.bashyal.chikemmod.registry.ModEntities;
import mr.bashyal.chikemmod.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChikemMod implements ModInitializer {
    public static final String MOD_ID = "chikem-mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        String modVersion = FabricLoader.getInstance().getModContainer(MOD_ID)
                .map(c -> c.getMetadata().getVersion().getFriendlyString())
                .orElse("unknown");
        LOGGER.info("Initializing Chikem Mod v{}", modVersion);

        ModItems.initialize();
        ModEntities.initialize();
        ModNetworking.initialize();
        mr.bashyal.chikemmod.entity.GolChickBreedingTracker.initialize();
        mr.bashyal.chikemmod.world.ModLootInjector.initialize();
        ChikemmodCommands.register();

        LOGGER.info("Chikem Mod initialized successfully!");
    }
}
