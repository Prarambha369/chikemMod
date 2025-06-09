package mr.bashyal.chikemmod;

import mr.bashyal.chikemmod.network.DashPayload;
import mr.bashyal.chikemmod.registry.ModEffects;
import mr.bashyal.chikemmod.registry.ModEntities;
import mr.bashyal.chikemmod.registry.ModEntityAttributes;
import mr.bashyal.chikemmod.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.village.TradeOffers;

public class Chickenmod implements ModInitializer {

    @Override
    public void onInitialize() {
        ModEntities.register();
        ModEntityAttributes.register();
        ModItems.register();
        ModEffects.register();

        // Register the DashPayload type for networking (serverbound)
        PayloadTypeRegistry.playC2S().register(DashPayload.ID, PacketCodec.unit(DashPayload.INSTANCE));

        ServerPlayNetworking.registerGlobalReceiver(DashPayload.ID, (server, player) -> {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                if (serverPlayer.hasVehicle() && serverPlayer.getVehicle() instanceof mr.bashyal.chikemmod.entity.MountableChickenEntity chicken) {
                    if (chicken.isRareChicken() && chicken.getSpecialAbility() == mr.bashyal.chikemmod.entity.MountableChickenEntity.SpecialAbility.DASH) {
                        chicken.performDash();
                    }
                }
            }
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
