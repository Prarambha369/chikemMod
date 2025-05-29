package mr.bashyal.chickenmod.registry;

import mr.bashyal.chickenmod.effect.EggLayingBoostEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEffects {
    public static final StatusEffect EGG_LAYING_BOOST = Registry.register(
        Registries.STATUS_EFFECT,
        new Identifier("chickenmod", "egg_laying_boost"),
        new EggLayingBoostEffect(StatusEffectCategory.BENEFICIAL, 0xffd700)
    );

    public static void register() {
        // Effects registered via static initializer
    }
}
