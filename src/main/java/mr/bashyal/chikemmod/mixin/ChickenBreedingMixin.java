package mr.bashyal.chikemmod.mixin;

import mr.bashyal.chikemmod.entity.BreedingRates;
import mr.bashyal.chikemmod.entity.GolChickBreedingTracker;
import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.chicken.Chicken;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.ThreadLocalRandom;

@Mixin(Chicken.class)
public abstract class ChickenBreedingMixin {

    @Inject(method = "getBreedOffspring", at = @At("RETURN"), cancellable = true)
    private void onCreateChild(ServerLevel level, AgeableMob other, CallbackInfoReturnable<AgeableMob> cir) {
        Chicken parent1 = (Chicken) (Object) this;

        if (!(other instanceof Chicken parent2)) {
            return;
        }

        if (GolChickBreedingTracker.wasRecentlyFed(parent1) && GolChickBreedingTracker.wasRecentlyFed(parent2)) {
            String difficulty = level.getDifficulty().name();
            boolean hardcore = level.getServer() != null && level.getServer().isHardcore();

            double specialSpawnChance = BreedingRates.getSpecialSpawnChance(difficulty, hardcore);
            double rareTypeShare = BreedingRates.getRareTypeShare(difficulty, hardcore);
            double luckShareInRare = BreedingRates.getLuckShareInRare(difficulty, hardcore);
            double slowFallShareInCommon = BreedingRates.getSlowFallShareInCommon(difficulty, hardcore);

            if (ThreadLocalRandom.current().nextDouble() < specialSpawnChance) {
                MountableChickenEntity specialChicken = new MountableChickenEntity(level);
                specialChicken.snapTo(parent1.getX(), parent1.getY(), parent1.getZ(), 0.0F, 0.0F);
                specialChicken.setBaby(true);

                String typeKey;
                if (ThreadLocalRandom.current().nextDouble() < rareTypeShare) {
                    if (ThreadLocalRandom.current().nextDouble() < luckShareInRare) {
                        typeKey = "LUCK";
                    } else {
                        typeKey = "DASH";
                    }
                } else {
                    if (ThreadLocalRandom.current().nextDouble() < slowFallShareInCommon) {
                        typeKey = "SLOW_FALL";
                    } else {
                        typeKey = "SPEED";
                    }
                }
                specialChicken.setChickenType(typeKey);
                specialChicken.applyIdentityDisplayName();

                cir.setReturnValue(specialChicken);
            }

            GolChickBreedingTracker.consume(parent1);
            GolChickBreedingTracker.consume(parent2);
        }
    }
}
