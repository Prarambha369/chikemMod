package mr.bashyal.chickenmod.item;

import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import mr.bashyal.chickenmod.registry.ModEffects;

public class ChickenSuperFeedItem extends Item {
    public ChickenSuperFeedItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!user.getWorld().isClient && entity instanceof net.minecraft.entity.passive.ChickenEntity chicken) {
            var eggLayingBoostEntry = net.minecraft.registry.Registries.STATUS_EFFECT.getEntry(ModEffects.EGG_LAYING_BOOST);
            if (eggLayingBoostEntry != null) {
                chicken.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                    eggLayingBoostEntry,
                    20 * 60 * 5,
                    0
                ));
                stack.decrement(1);
                return ActionResult.SUCCESS;
            }
        }
        return super.useOnEntity(stack, user, entity, hand);
    }
}
