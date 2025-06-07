package mr.bashyal.chickenmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ChickenEntityRenderer;
import net.minecraft.util.Identifier;
import mr.bashyal.chickenmod.registry.ModEntities;
import mr.bashyal.chickenmod.entity.MountableChickenEntity;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.ChickenEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.CustomPayload;

public class ChickenmodClient implements ClientModInitializer {
    public static KeyBinding dashKeyBinding;

    // Create a custom payload for the dash action
    public static final class DashPayload implements CustomPayload {
        public static final Identifier IDENTIFIER = Identifier.of("chickenmod", "dash");

        @Override
        public CustomPayload.Id getId() {
            return new CustomPayload.Id(IDENTIFIER);
        }
    }

    @Override
    public void onInitializeClient() {
        // Register mountable chicken renderer
        EntityRendererRegistry.register(ModEntities.MOUNTABLE_CHICKEN,
            context -> new ChickenEntityRenderer(context) {
                // Must use the exact method signature from parent class
                @Override
                public Identifier getTexture(ChickenEntity entity) {
                    // Cast to MountableChickenEntity if needed for specific behavior
                    return Identifier.of("chickenmod", "textures/entity/mountable_chicken.png");
                }
            }
        );

        // Register our dash key binding
        dashKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.chickenmod.dash",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "category.chickenmod"
        ));

        // Set up the client tick event handler
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
