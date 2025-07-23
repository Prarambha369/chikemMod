package mr.bashyal.chikemmod.client;

import mr.bashyal.chikemmod.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ChickenEntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.util.Identifier;

/**
 * Client-side initializer for ChickenMod.
 * Handles entity renderers, key bindings, and client events.
 */
public class ChickenmodClient implements ClientModInitializer {
    /**
     * Initializes client-side features: renderers and other client-side setup.
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
    }
}
