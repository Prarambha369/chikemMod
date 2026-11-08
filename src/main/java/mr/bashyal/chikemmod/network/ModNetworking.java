package mr.bashyal.chikemmod.network;

import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class ModNetworking {
    private ModNetworking() {
    }

    public static void initialize() {
        PayloadTypeRegistry.serverboundPlay().register(DashRequestC2SPayload.ID, DashRequestC2SPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(DashRequestC2SPayload.ID, (payload, context) ->
            context.server().execute(() -> {
                var player = context.player();
                if (!(player.getVehicle() instanceof MountableChickenEntity mountableChicken)) {
                    return;
                }
                if (mountableChicken.getControllingPassenger() != player) {
                    return;
                }
                mountableChicken.performDash();
            })
        );
    }
}
