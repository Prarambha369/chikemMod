package mr.bashyal.chikemmod.item;

import mr.bashyal.chikemmod.entity.GolChickBreedingTracker;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;

import java.util.List;
import java.util.function.Consumer;

public class GolChickFoodItem extends Item {
    public GolChickFoodItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof ChickenEntity chicken) {
            if (!user.getEntityWorld().isClient()) {
                // Use vanilla breeding logic to consistently trigger breeding state.
                chicken.lovePlayer(user);
                // Explicitly send love particles for clearer player feedback.
                chicken.getEntityWorld().sendEntityStatus(chicken, (byte) 18);
                GolChickBreedingTracker.markFed(chicken);
                if (!user.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        textConsumer.accept(Text.translatable("item.chikem-mod.golchick_food.tooltip.ability").formatted(Formatting.GRAY));
        textConsumer.accept(Text.translatable("item.chikem-mod.golchick_food.tooltip.spawn_rates").formatted(Formatting.GOLD));
        textConsumer.accept(Text.translatable("item.chikem-mod.golchick_food.tooltip.loot_sources").formatted(Formatting.DARK_AQUA));
    }
}