package mr.bashyal.chikemmod.client;

import mr.bashyal.chikemmod.ChikemMod;
import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.chicken.Chicken;

/**
 * Vanilla-chicken renderer with a per-type texture so a player's chickens can be
 * told apart at a glance: blue = SWIFTWING (speed), orange/red = DASHCLAW (dash),
 * pale violet = FEATHERFALL (slow fall), bright gold = LUCKY BEAK (luck),
 * warm golden = default Special Chickem.
 */
public class MountableChickenRenderer extends ChickenRenderer {

    public static class MountableChickenState extends ChickenRenderState {
        public String chickenType = "NORMAL";
    }

    private static final Identifier BASE = Identifier.fromNamespaceAndPath(
            ChikemMod.MOD_ID, "textures/entity/mountable_chicken.png");
    private static final Identifier SPEED = Identifier.fromNamespaceAndPath(
            ChikemMod.MOD_ID, "textures/entity/mountable_chicken_speed.png");
    private static final Identifier DASH = Identifier.fromNamespaceAndPath(
            ChikemMod.MOD_ID, "textures/entity/mountable_chicken_dash.png");
    private static final Identifier SLOW_FALL = Identifier.fromNamespaceAndPath(
            ChikemMod.MOD_ID, "textures/entity/mountable_chicken_slow_fall.png");
    private static final Identifier LUCK = Identifier.fromNamespaceAndPath(
            ChikemMod.MOD_ID, "textures/entity/mountable_chicken_luck.png");

    public MountableChickenRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public MountableChickenState createRenderState() {
        return new MountableChickenState();
    }

    @Override
    public void extractRenderState(Chicken entity, ChickenRenderState state, float age) {
        super.extractRenderState(entity, state, age);
        if (state instanceof MountableChickenState mine && entity instanceof MountableChickenEntity chickem) {
            mine.chickenType = chickem.getChickenType();
        }
    }

    @Override
    public Identifier getTextureLocation(ChickenRenderState state) {
        if (state instanceof MountableChickenState mine) {
            return switch (mine.chickenType) {
                case "SPEED" -> SPEED;
                case "DASH" -> DASH;
                case "SLOW_FALL" -> SLOW_FALL;
                case "LUCK" -> LUCK;
                default -> BASE;
            };
        }
        return super.getTextureLocation(state);
    }
}
