package mr.bashyal.chikemmod;

import mr.bashyal.chikemmod.network.ModNetworking;
import mr.bashyal.chikemmod.registry.ModEntities;
import mr.bashyal.chikemmod.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChikemMod implements ModInitializer {
    public static final String MOD_ID = "chikem-mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Chikem Mod v1.4.0");

        ModItems.initialize();
        ModEntities.initialize();
        ModNetworking.initialize();
        mr.bashyal.chikemmod.world.ModLootInjector.initialize();
        ChikemmodCommands.register();
        registerTrades();

        LOGGER.info("Chikem Mod initialized successfully!");
    }

    private void registerTrades() {
        TradeOfferHelper.registerWanderingTraderOffers(builder -> {
            builder.addOffersToPool(TradeOfferHelper.WanderingTraderOffersBuilder.SELL_SPECIAL_ITEMS_POOL,
                (world, entity, random) -> new TradeOffer(
                new TradedItem(Items.EMERALD, 3),
                new ItemStack(ModItems.GOLCHICK_FOOD, 1),
                12, 5, 0.05f
            ));
            
            builder.addOffersToPool(TradeOfferHelper.WanderingTraderOffersBuilder.SELL_SPECIAL_ITEMS_POOL,
                (world, entity, random) -> new TradeOffer(
                new TradedItem(Items.EMERALD, 8),
                new ItemStack(ModItems.GOLDIE_EGG, 1),
                3, 10, 0.1f
            ));
        });
    }
}