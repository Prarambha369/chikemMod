package mr.bashyal.chikemmod.item;

import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import mr.bashyal.chikemmod.entity.MountableChickenEntity.SpecialAbility;

import java.util.Random;

public class ChickenFeedItem extends Item {
    private static final Random RANDOM = new Random();

    public ChickenFeedItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        World world = user.getWorld();
        if (!world.isClient && entity instanceof MountableChickenEntity mount) {
            // Feeding only affects existing mountable rare chickens
            stack.decrement(1);
            // optionally restore health or trigger special effect
            mount.heal(2.0F);
            return ActionResult.SUCCESS;
        }
        return super.useOnEntity(stack, user, entity, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack result = super.finishUsing(stack, world, user);
        if (!world.isClient && user instanceof PlayerEntity player) {
            if (RANDOM.nextDouble() < 0.3) {
                SpecialAbility[] abilities = SpecialAbility.values();
                SpecialAbility ability = abilities[RANDOM.nextInt(abilities.length)];
                var effect = switch (ability) {
                    case SPEED -> StatusEffects.SPEED;
                    case SLOW_FALL -> StatusEffects.SLOW_FALLING;
                    case LUCK -> StatusEffects.LUCK;
                    case DASH -> StatusEffects.SPEED; // approximate dash with speed
                };
                player.addStatusEffect(new StatusEffectInstance(effect, 20 * 60 * 2, 0));
            } else {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 20 * 60 * 2, 0));
            }
        }
        return result;
    }
}
