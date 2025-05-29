package mr.bashyal.chickenmod;

import net.fabricmc.api.ModInitializer;
import mr.bashyal.chickenmod.registry.ModEntities;
import mr.bashyal.chickenmod.registry.ModEntityAttributes;
import mr.bashyal.chickenmod.registry.ModItems;
import mr.bashyal.chickenmod.registry.ModEffects;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;

public class Chickenmod implements ModInitializer {

    @Override
    public void onInitialize() {
        ModEntities.register();
        ModEntityAttributes.register();
        ModItems.register();
        ModEffects.register();
        // spawn mountable chickens rarely in overworld
        BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld(), SpawnGroup.CREATURE,
            ModEntities.MOUNTABLE_CHICKEN, 1, 1, 1);
    }
}
