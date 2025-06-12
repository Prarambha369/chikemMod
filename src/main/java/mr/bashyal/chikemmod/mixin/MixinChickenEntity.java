package mr.bashyal.chikemmod.mixin;

import mr.bashyal.chikemmod.registry.ModItems;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ChickenEntity.class)
public abstract class MixinChickenEntity {
    /**
     * Allow GolChickFood as a breeding item for vanilla chickens.
     *
     * @author Prarambha369
     * @reason Allow custom mod food (GolChickFood) to be used for breeding vanilla chickens.
     */
    @Overwrite
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == net.minecraft.item.Items.WHEAT_SEEDS ||
               stack.getItem() == net.minecraft.item.Items.BEETROOT_SEEDS ||
               stack.getItem() == net.minecraft.item.Items.PUMPKIN_SEEDS ||
               stack.getItem() == net.minecraft.item.Items.MELON_SEEDS ||
               stack.getItem() == net.minecraft.item.Items.TORCHFLOWER_SEEDS ||
               stack.getItem() == ModItems.GOLCHICK_FOOD;
    }
}
