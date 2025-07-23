package mr.bashyal.chikemmod.mixin;

import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import mr.bashyal.chikemmod.registry.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ChickenEntity.class)
public abstract class MixinChickenEntity {
    private static final Random RANDOM = new Random();

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

    /**
     * Enhanced breeding when using GolChick Food - higher chance of special offspring
     */
    @Inject(method = "createChild", at = @At("HEAD"), cancellable = true)
    private void enhancedBreeding(ServerWorld world, PassiveEntity entity, CallbackInfoReturnable<PassiveEntity> cir) {
        ChickenEntity self = (ChickenEntity) (Object) this;

        // Check if either parent was recently fed GolChick Food (indicated by having regeneration effect)
        boolean premiumBreeding = self.hasStatusEffect(net.minecraft.entity.effect.StatusEffects.REGENERATION) ||
                                 (entity instanceof ChickenEntity otherChicken &&
                                  otherChicken.hasStatusEffect(net.minecraft.entity.effect.StatusEffects.REGENERATION));

        if (premiumBreeding) {
            // Higher chance to create a MountableChickenEntity instead of regular chicken
            if (RANDOM.nextFloat() < 0.3f) { // 30% chance for premium breeding
                MountableChickenEntity baby = new MountableChickenEntity(
                    (EntityType<? extends ChickenEntity>) mr.bashyal.chikemmod.registry.ModEntities.MOUNTABLE_CHICKEN,
                    world
                );

                // Small chance for the baby to be born rare
                if (RANDOM.nextFloat() < 0.1f) { // 10% chance
                    String[] rareNames = {"Wonder", "Miracle", "Blessed", "Divine", "Celestial"};
                    String rareName = rareNames[RANDOM.nextInt(rareNames.length)];
                    baby.setRareChicken(rareName);
                }

                baby.setBaby(true);
                baby.refreshPositionAndAngles(self.getX(), self.getY(), self.getZ(), 0.0F, 0.0F);

                cir.setReturnValue(baby);
            }
        }
    }
}
