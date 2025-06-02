package mr.bashyal.chickenmod;

import net.fabricmc.api.ModInitializer;
import mr.bashyal.chickenmod.registry.ModEntities;
import mr.bashyal.chickenmod.registry.ModEntityAttributes;
import mr.bashyal.chickenmod.registry.ModItems;
import mr.bashyal.chickenmod.registry.ModEffects;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import mr.bashyal.chickenmod.entity.MountableChickenEntity;

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

        // Register dash packet handler (Fabric 1.21+ networking API)
        // TODO: Update this to use the correct Fabric networking API for 1.21+ if needed
        // ServerPlayNetworking.registerGlobalReceiver(new Identifier("chickenmod", "dash"), ...);
        // Temporarily comment out to avoid compilation error

        // Add GolChick Food to wandering trader trades (Fabric 1.21+ API)
        // Use the correct signature: (int level, Consumer<List<TradeOffers.Factory>>)
        TradeOfferHelper.registerWanderingTraderOffers(1, factories -> {
            factories.add(new TradeOffers.SellItemFactory(
                mr.bashyal.chickenmod.registry.ModItems.GOLCHICK_FOOD, // Item to sell
                5,    // price in emeralds
                1,    // quantity of item being sold
                8,    // maxUses
                2     // experience given
            ));
        });
    }
}
