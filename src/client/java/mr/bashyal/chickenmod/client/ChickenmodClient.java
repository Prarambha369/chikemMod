package mr.bashyal.chickenmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ChickenEntityRenderer;
import net.minecraft.util.Identifier;
import mr.bashyal.chickenmod.registry.ModEntities;
import mr.bashyal.chickenmod.entity.MountableChickenEntity;

public class ChickenmodClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register mountable chicken renderer
        EntityRendererRegistry.register(ModEntities.MOUNTABLE_CHICKEN,
            context -> new ChickenEntityRenderer(context) {
                @Override
                public Identifier getTextureLocation(MountableChickenEntity entity) {
                    return new Identifier("chickenmod", "textures/entity/mountable_chicken.png");
                }
            }
        );
    }
}
