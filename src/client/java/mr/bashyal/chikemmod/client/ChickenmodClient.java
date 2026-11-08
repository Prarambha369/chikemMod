package mr.bashyal.chikemmod.client;

import com.mojang.blaze3d.platform.InputConstants;
import mr.bashyal.chikemmod.ChikemMod;
import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import mr.bashyal.chikemmod.network.DashRequestC2SPayload;
import mr.bashyal.chikemmod.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

/**
 * Client-side initializer for ChickenMod.
 * Handles entity renderers, key bindings, and client events.
 */
public class ChickenmodClient implements ClientModInitializer {
    private static final KeyMapping.Category DASH_CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath("chikem-mod", "keybindings"));

    private static KeyMapping dashKey;
    private static boolean initialized;

    /**
     * Initializes client-side features: renderers and other client-side setup.
     */
    @Override
    public void onInitializeClient() {
        if (initialized) {
            ChikemMod.LOGGER.warn("ChickenmodClient already initialized; skipping duplicate registration");
            return;
        }
        initialized = true;

        dashKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.chikem-mod.dash",
                InputConstants.Type.KEYBOARD,
                74,
                DASH_CATEGORY
        ));

        // Register mountable chicken renderer
        EntityRendererRegistry.register(ModEntities.MOUNTABLE_CHICKEN,
                MountableChickenRenderer::new
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || !dashKey.consumeClick()) {
                return;
            }

            Entity vehicle = client.player.getVehicle();
            if (vehicle instanceof MountableChickenEntity) {
                ClientPlayNetworking.send(DashRequestC2SPayload.INSTANCE);
            }
        });
    }
}
