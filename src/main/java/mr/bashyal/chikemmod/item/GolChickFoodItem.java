package mr.bashyal.chikemmod.item;

import mr.bashyal.chikemmod.entity.GolChickBreedingTracker;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class GolChickFoodItem extends Item {
    public GolChickFoodItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (entity instanceof Chicken chicken) {
            // Reject before consuming/marking: babies, already-in-love, or on breeding cooldown.
            if (chicken.isBaby()) {
                return InteractionResult.PASS;
            }
            if (chicken.isInLove()) {
                return InteractionResult.PASS;
            }
            if (chicken.getAge() > 0) {
                return InteractionResult.PASS;
            }

            if (!user.level().isClientSide()) {
                chicken.setInLove(user);
                chicken.level().broadcastEntityEvent(chicken, (byte) 18);
                GolChickBreedingTracker.markFed(chicken);
                if (!user.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> textConsumer, TooltipFlag flag) {
        textConsumer.accept(Component.translatable("item.chikem-mod.golchick_food.tooltip.ability").withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.chikem-mod.golchick_food.tooltip.spawn_rates").withStyle(ChatFormatting.GOLD));
        textConsumer.accept(Component.translatable("item.chikem-mod.golchick_food.tooltip.loot_sources").withStyle(ChatFormatting.DARK_AQUA));
        textConsumer.accept(Component.translatable("item.chikem-mod.golchick_food.tooltip.flavor").withStyle(ChatFormatting.AQUA));
    }
}
