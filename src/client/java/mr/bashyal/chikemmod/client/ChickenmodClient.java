package mr.bashyal.chikemmod.client;

import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import mr.bashyal.chikemmod.network.DashPayload;
import mr.bashyal.chikemmod.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.ChickenEntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

/**
 * Client-side initializer for ChickenMod.
 * Handles entity renderers, key bindings, and client events.
 */
public class ChickenmodClient implements ClientModInitializer {
    public static KeyBinding dashKeyBinding;

    /**
     * Initializes client-side features: renderers, key bindings, and event handlers.
     */
    @Override
    public void onInitializeClient() {
        // Register mountable chicken renderer
        EntityRendererRegistry.register(ModEntities.MOUNTABLE_CHICKEN,
                context -> new ChickenEntityRenderer(context) {
                    @Override
                    public Identifier getTexture(ChickenEntity entity) {
                        // Always use the mountable chicken texture
                        return Identifier.of("chikemmod", "textures/entity/mountable_chicken.png");
                    }
                }
        );

        // Register golden egg renderer
        EntityRendererRegistry.register(ModEntities.GOLDEN_EGG_ENTITY, FlyingItemEntityRenderer::new);

        // Register our dash key binding
        dashKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.chikemmod.dash",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                "category.chikemmod"
        ));

        // Set up the client tick event handler for dash ability
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client != null && client.player != null) {
                if (dashKeyBinding.isPressed()) {
                    PlayerEntity player = client.player;
                    if (player.hasVehicle()) {
                        Entity vehicle = player.getVehicle();
                        if (vehicle instanceof MountableChickenEntity chicken &&
                                chicken.isRareChicken() &&
                                chicken.getSpecialAbility() == MountableChickenEntity.SpecialAbility.DASH) {
                            ClientPlayNetworking.send(new DashPayload());
                        }
                    }
                }
            }
        });
    }
}
