package mr.bashyal.chikemmod.entity;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Pure identity/lore data for the four Special Chickem types.
 * Kept in main sourceset with no Minecraft types so it is unit-testable.
 */
public enum ChickenIdentity {
    SWIFTWING("SPEED", "Swiftwing", "The Storm-Hatched"),
    DASHCLAW("DASH", "Dashclaw", "The Ambush Striker"),
    FEATHERFALL("SLOW_FALL", "Featherfall", "The Quiet Descent"),
    LUCKY_BEAK("LUCK", "Lucky Beak", "The Gilded Trickster");

    private final String typeKey;
    private final String individualName;
    private final String title;

    ChickenIdentity(String typeKey, String individualName, String title) {
        this.typeKey = typeKey;
        this.individualName = individualName;
        this.title = title;
    }

    /** Type key stored in entity data / NBT (e.g. "SPEED"). */
    public String typeKey() {
        return typeKey;
    }

    /** Individual name (e.g. "Swiftwing"). */
    public String individualName() {
        return individualName;
    }

    /** Lore title (e.g. "The Storm-Hatched"). */
    public String title() {
        return title;
    }

    /** Ability summary. */
    public String ability() {
        return switch (this) {
            case SWIFTWING -> "fastest mount in the flock";
            case DASHCLAW -> "recharging dash burst";
            case FEATHERFALL -> "fall-damage immune, glides while falling";
            case LUCKY_BEAK -> "golden aura blessing nearby players with Luck";
        };
    }

    /** Drawback summary. */
    public String drawback() {
        return switch (this) {
            case SWIFTWING -> "fragile — one less heart than a normal chicken";
            case DASHCLAW -> "brief slowdown after each dash";
            case FEATHERFALL -> "slow on ground and in air";
            case LUCKY_BEAK -> "blesses only travelers within sight";
        };
    }

    /** Lore text without the ability/drawback suffix. */
    private String lore() {
        return switch (this) {
            case SWIFTWING -> "Hatched from an egg that rolled into a lightning scar during a mesa storm, Swiftwing learned to read the wind before it could walk. It does not run — it chases weather, ferrying swift seeds to barren soil and scouting clear skies for the flock.";
            case DASHCLAW -> "Born in a fox-raided coop, Dashclaw survived by striking first and vanishing. Its golden leg feathers are scar tissue from the night it escaped. It asks no fight — only distance.";
            case FEATHERFALL -> "An ancient city's last chick, found sleeping in a clean chest guarded by a sculk warden who never woke her. She learned that the safest way down is to be too quiet to notice.";
            case LUCKY_BEAK -> "The only chick ever hatched inside a treasure chest, Lucky Beak carries a dull gold nugget in her beak that she swears is a soul-gem. It isn't. But fortune seems to loiter near her anyway.";
        };
    }

    /** Backstory text (also in en_us.json under entity.chikem-mod.mountable_chicken.&lt;type&gt;.backstory). */
    public String backstory() {
        return lore() + " Ability: " + ability() + ". Drawback: " + drawback() + ".";
    }

    /** Translation key for the type title. */
    public String titleKey() {
        return "entity.chikem-mod.mountable_chicken." + typeKey().toLowerCase(java.util.Locale.ROOT) + ".title";
    }

    /** Returns null for NORMAL/unknown type keys so normal chickens are not misidentified. */
    public static ChickenIdentity byTypeKey(String typeKey) {
        if (typeKey != null) {
            for (ChickenIdentity identity : values()) {
                if (identity.typeKey.equalsIgnoreCase(typeKey)) {
                    return identity;
                }
            }
        }
        return null;
    }

    public static ChickenIdentity random() {
        ChickenIdentity[] all = values();
        return all[ThreadLocalRandom.current().nextInt(all.length)];
    }

    /** Individual-name pool formerly in rare_chicken_names.json (inlined; JSON deleted). */
    private static final List<String> NAME_POOL = List.copyOf(List.of(
            "Kazuki Takahashi",
            "Ryoichi Featherblade",
            "Hinata Clawstrike",
            "Tori Yamaguchi",
            "Shiro Eggami",
            "Kensuke Peckara",
            "Ayame Henshin",
            "Takumi Roosteru",
            "Sora Wingata",
            "Yuki Chickamoto"
    ));

    /** Unmodifiable individual-name pool for spawned chicks. */
    public static List<String> namePool() {
        return NAME_POOL;
    }

    /** Pick a random individual name from the pool. */
    public static String randomName() {
        return NAME_POOL.get(ThreadLocalRandom.current().nextInt(NAME_POOL.size()));
    }

    /** Display custom name for a spawned chick: "Individual Title" (e.g. "Swiftwing The Storm-Hatched"). */
    public String displayName() {
        return individualName + " " + title;
    }
}
