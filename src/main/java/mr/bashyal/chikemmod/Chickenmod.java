package mr.bashyal.chikemmod;

import mr.bashyal.chikemmod.registry.ModEffects;
import mr.bashyal.chikemmod.registry.ModEntities;
import mr.bashyal.chikemmod.registry.ModEntityAttributes;
import mr.bashyal.chikemmod.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.village.TradeOffers;

/**
 * Main entrypoint for ChickemMod. Registers entities, items, commands, networking, and trader offers.
 * 
 * - Uses Fabric API v2+ for commands and networking
 * - Registers rare/mountable chickens, golden eggs, and GolChickFood
 * - Adds /chickem command for spawning rare chickens
 * 
 * @author Prarambha369
 */
public class Chickenmod implements ModInitializer {

    @Override
    public void onInitialize() {
        // Register entities, entity attributes, items, and effects
        ModEntities.register();
        ModEntityAttributes.register();
        ModItems.register();
        ModEffects.register();

        // Register commands
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ChickenmodCommands.register(dispatcher);
        });

        // Add GolChick Food to wandering trader trades (Fabric 1.21+ API)
        TradeOfferHelper.registerWanderingTraderOffers(1, factories -> factories.add(new TradeOffers.SellItemFactory(
                mr.bashyal.chikemmod.registry.ModItems.GOLCHICK_FOOD, // Item to sell
                5,    // price in emeralds
                1,    // quantity of item being sold
                8,    // maxUses
                2     // experience given
        )));
    }
}
