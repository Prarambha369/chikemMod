package mr.bashyal.chikemmod.client;

import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import mr.bashyal.chikemmod.network.DashRequestC2SPayload;
import mr.bashyal.chikemmod.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.render.entity.ChickenEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

/**
 * Client-side initializer for ChickenMod.
 * Handles entity renderers, key bindings, and client events.
 */
public class ChickenmodClient implements ClientModInitializer {
    private static KeyBinding dashKey;

    /**
     * Initializes client-side features: renderers and other client-side setup.
     */
    @Override
    public void onInitializeClient() {
        dashKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.chikem-mod.dash",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                KeyBinding.Category.create(Identifier.of("chikem-mod", "keybindings"))
        ));

        // Register mountable chicken renderer
        EntityRendererRegistry.register(ModEntities.MOUNTABLE_CHICKEN,
                ChickenEntityRenderer::new
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || !dashKey.wasPressed()) {
                return;
            }

            Entity vehicle = client.player.getVehicle();
            if (vehicle instanceof MountableChickenEntity) {
                ClientPlayNetworking.send(DashRequestC2SPayload.INSTANCE);
            }
        });
    }
}
