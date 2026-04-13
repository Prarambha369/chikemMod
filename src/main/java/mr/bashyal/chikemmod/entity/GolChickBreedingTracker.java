package mr.bashyal.chikemmod.entity;

import net.minecraft.entity.passive.ChickenEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class GolChickBreedingTracker {
    // Match vanilla-style breeding cadence: 600 ticks = 30 seconds.
    private static final long FEED_WINDOW_TICKS = 20L * 30L;
    private static final Map<UUID, Long> FED_CHICKENS = new ConcurrentHashMap<>();

    private GolChickBreedingTracker() {
    }

    public static void markFed(ChickenEntity chicken) {
        FED_CHICKENS.put(chicken.getUuid(), chicken.getWorld().getTime());
    }

    public static boolean wasRecentlyFed(ChickenEntity chicken) {
        Long fedTick = FED_CHICKENS.get(chicken.getUuid());
        if (fedTick == null) {
            return false;
        }

        long age = chicken.getWorld().getTime() - fedTick;
        if (age < 0 || age > FEED_WINDOW_TICKS) {
            FED_CHICKENS.remove(chicken.getUuid());
            return false;
        }

        return true;
    }

    public static void consume(ChickenEntity chicken) {
        FED_CHICKENS.remove(chicken.getUuid());
    }
}
