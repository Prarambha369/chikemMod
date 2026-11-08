package mr.bashyal.chikemmod.entity;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.entity.animal.chicken.Chicken;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class GolChickBreedingTracker {
    // Match vanilla-style breeding cadence: 600 ticks = 30 seconds.
    private static final long FEED_WINDOW_TICKS = 20L * 30L;

    /** Feed window in ticks (exposed for tests). */
    public static final long FEED_WINDOW = FEED_WINDOW_TICKS;

    private static final Map<UUID, Long> FED_CHICKENS = new ConcurrentHashMap<>();

    private GolChickBreedingTracker() {
    }

    /**
     * Pure freshness predicate: a chicken fed at {@code fedTick} is still fresh at
     * {@code now} when the age is in [0, FEED_WINDOW]. Negative age (clock skew /
     * level reload) is not fresh. No Minecraft types; unit-testable.
     */
    public static boolean isFresh(long fedTick, long now) {
        long age = now - fedTick;
        return age >= 0 && age <= FEED_WINDOW_TICKS;
    }

    public static void markFed(Chicken chicken) {
        FED_CHICKENS.put(chicken.getUUID(), chicken.level().getGameTime());
    }

    public static boolean wasRecentlyFed(Chicken chicken) {
        Long fedTick = FED_CHICKENS.get(chicken.getUUID());
        if (fedTick == null) {
            return false;
        }

        if (!isFresh(fedTick, chicken.level().getGameTime())) {
            FED_CHICKENS.remove(chicken.getUUID());
            return false;
        }

        return true;
    }

    public static void consume(Chicken chicken) {
        FED_CHICKENS.remove(chicken.getUUID());
    }

    /** Evict all stale entries older than the feed window relative to {@code now}. */
    public static int sweep(long now) {
        int removed = 0;
        for (Map.Entry<UUID, Long> entry : FED_CHICKENS.entrySet()) {
            if (!isFresh(entry.getValue(), now)) {
                FED_CHICKENS.remove(entry.getKey(), entry.getValue());
                removed++;
            }
        }
        return removed;
    }

    /** Drop a single entry (e.g. on entity unload). */
    public static void forget(UUID uuid) {
        if (uuid != null) {
            FED_CHICKENS.remove(uuid);
        }
    }

    /** Clear all tracked entries (register on server stop). */
    public static void clear() {
        FED_CHICKENS.clear();
    }

    /** Test hook: current tracked-entry count. */
    public static int size() {
        return FED_CHICKENS.size();
    }

    /** Wire lifecycle cleanup: clear on server stop, periodic sweep on server tick. */
    public static void initialize() {
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> clear());
        // ServerEntityEvents.ENTITY_UNLOAD was removed from Fabric API (6.x),
        // so stale entries are reclaimed by a throttled sweep instead.
        final int[] tickCounter = {0};
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (++tickCounter[0] >= 600) {
                tickCounter[0] = 0;
                long maxTime = Long.MIN_VALUE;
                for (var level : server.getAllLevels()) {
                    maxTime = Math.max(maxTime, level.getGameTime());
                }
                if (maxTime != Long.MIN_VALUE) {
                    sweep(maxTime);
                }
            }
        });
    }
}
