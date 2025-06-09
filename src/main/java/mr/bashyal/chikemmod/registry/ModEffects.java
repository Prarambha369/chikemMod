package mr.bashyal.chikemmod.registry;

import mr.bashyal.chikemmod.effect.EggLayingBoostEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEffects {
    public static final StatusEffect EGG_LAYING_BOOST = Registry.register(
            Registries.STATUS_EFFECT,
            Identifier.of("chikemmod", "egg_laying_boost"),
            new EggLayingBoostEffect(StatusEffectCategory.BENEFICIAL, 0xffd700)
    );

    public static void register() {
        // Effects registered via static initializer
    }
}
