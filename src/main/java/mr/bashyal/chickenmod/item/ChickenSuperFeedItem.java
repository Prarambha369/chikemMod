package mr.bashyal.chickenmod.item;

import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import mr.bashyal.chickenmod.registry.ModEffects;

public class ChickenSuperFeedItem extends Item {
    public ChickenSuperFeedItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemUsageContext context) {
        if (!context.getWorld().isClient && context.getEntity() instanceof ChickenEntity chicken) {
            chicken.addStatusEffect(new StatusEffectInstance(
                ModEffects.EGG_LAYING_BOOST,
                20 * 60 * 5, // 5 minutes
                0
            ));
            context.getStack().decrement(1);
            return ActionResult.SUCCESS;
        }
        return super.useOnEntity(context);
    }
}
