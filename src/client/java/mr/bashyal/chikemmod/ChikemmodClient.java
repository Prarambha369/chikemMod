package mr.bashyal.chikemmod;

import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import org.lwjgl.glfw.GLFW;

public class ChikemmodClient implements ClientModInitializer {
    private static KeyBinding dashKey;

    @Override
    public void onInitializeClient() {
        dashKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.chikem-mod.dash",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "category.chikem-mod"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && dashKey.wasPressed()) {
                Entity vehicle = client.player.getVehicle();
                if (vehicle instanceof MountableChickenEntity mountableChicken) {
                    mountableChicken.performDash();
                }
            }
        });
    }
}
