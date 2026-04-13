package mr.bashyal.chikemmod.mixin;

import mr.bashyal.chikemmod.entity.GolChickBreedingTracker;
import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ChickenEntity.class)
public abstract class ChickenBreedingMixin {
    @Unique
    private static final Random RANDOM = new Random();

    @Inject(method = "createChild(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/PassiveEntity;)Lnet/minecraft/entity/passive/ChickenEntity;", at = @At("RETURN"), cancellable = true)
    private void onCreateChild(ServerWorld world, PassiveEntity entity, CallbackInfoReturnable<ChickenEntity> cir) {
        ChickenEntity parent1 = (ChickenEntity) (Object) this;

        if (!(entity instanceof ChickenEntity parent2)) {
            return;
        }

        if (GolChickBreedingTracker.wasRecentlyFed(parent1) && GolChickBreedingTracker.wasRecentlyFed(parent2)) {
            double specialSpawnChance = getSpecialSpawnChance(world);
            double rareTypeShare = getRareTypeShare(world);
            double luckShareInRare = getLuckShareInRare(world);
            double slowFallShareInCommon = getSlowFallShareInCommon(world);

            if (RANDOM.nextDouble() < specialSpawnChance) {
                MountableChickenEntity specialChicken = new MountableChickenEntity(world);
                specialChicken.refreshPositionAndAngles(parent1.getX(), parent1.getY(), parent1.getZ(), 0.0F, 0.0F);
                specialChicken.setBaby(true);

                if (RANDOM.nextDouble() < rareTypeShare) {
                    if (RANDOM.nextDouble() < luckShareInRare) {
                        specialChicken.setChickenType("LUCK");
                    } else {
                        specialChicken.setChickenType("DASH");
                    }
                } else {
                    if (RANDOM.nextDouble() < slowFallShareInCommon) {
                        specialChicken.setChickenType("SLOW_FALL");
                    } else {
                        specialChicken.setChickenType("SPEED");
                    }
                }

                cir.setReturnValue(specialChicken);
            }

            GolChickBreedingTracker.consume(parent1);
            GolChickBreedingTracker.consume(parent2);
        }
    }

    @Unique
    private static double getSpecialSpawnChance(ServerWorld world) {
        if (isHardcore(world)) {
            return 0.12;
        }
        return switch (world.getDifficulty()) {
            case PEACEFUL -> 0.30;
            case EASY -> 0.24;
            case NORMAL -> 0.18;
            case HARD -> 0.14;
        };
    }

    @Unique
    private static double getRareTypeShare(ServerWorld world) {
        if (isHardcore(world)) {
            return 0.10;
        }
        return switch (world.getDifficulty()) {
            case PEACEFUL -> 0.25;
            case EASY -> 0.20;
            case NORMAL -> 0.15;
            case HARD -> 0.12;
        };
    }

    @Unique
    private static double getLuckShareInRare(ServerWorld world) {
        if (isHardcore(world)) {
            return 0.40;
        }
        return switch (world.getDifficulty()) {
            case PEACEFUL -> 0.80;
            case EASY -> 0.75;
            case NORMAL -> 0.65;
            case HARD -> 0.50;
        };
    }

    @Unique
    private static double getSlowFallShareInCommon(ServerWorld world) {
        if (isHardcore(world)) {
            return 0.45;
        }
        return switch (world.getDifficulty()) {
            case PEACEFUL -> 0.65;
            case EASY -> 0.60;
            case NORMAL -> 0.55;
            case HARD -> 0.50;
        };
    }

    @Unique
    private static boolean isHardcore(ServerWorld world) {
        return world.getServer() != null && world.getServer().isHardcore();
    }
}