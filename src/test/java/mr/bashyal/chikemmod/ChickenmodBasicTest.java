package mr.bashyal.chikemmod;

import mr.bashyal.chikemmod.entity.ChickenIdentity;
import mr.bashyal.chikemmod.entity.GolChickBreedingTracker;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Real unit tests over pure logic only (no Minecraft runtime classes).
 */
public class ChickenmodBasicTest {

    // --- Difficulty chance table (pure) ---

    @Test
    public void testSpecialSpawnChanceTable() {
        assertEquals(0.30, mr.bashyal.chikemmod.entity.BreedingRates.getSpecialSpawnChance("PEACEFUL", false));
        assertEquals(0.24, mr.bashyal.chikemmod.entity.BreedingRates.getSpecialSpawnChance("EASY", false));
        assertEquals(0.18, mr.bashyal.chikemmod.entity.BreedingRates.getSpecialSpawnChance("NORMAL", false));
        assertEquals(0.14, mr.bashyal.chikemmod.entity.BreedingRates.getSpecialSpawnChance("HARD", false));
        assertEquals(0.12, mr.bashyal.chikemmod.entity.BreedingRates.getSpecialSpawnChance("PEACEFUL", true));
    }

    @Test
    public void testRareTypeShareTable() {
        assertEquals(0.25, mr.bashyal.chikemmod.entity.BreedingRates.getRareTypeShare("PEACEFUL", false));
        assertEquals(0.15, mr.bashyal.chikemmod.entity.BreedingRates.getRareTypeShare("NORMAL", false));
        assertEquals(0.10, mr.bashyal.chikemmod.entity.BreedingRates.getRareTypeShare("NORMAL", true));
    }

    // --- Feed-window arithmetic (pure isFresh) ---

    @Test
    public void testFeedWindowFreshAtBoundary() {
        assertTrue(GolChickBreedingTracker.isFresh(0, 600));
        assertTrue(GolChickBreedingTracker.isFresh(1000, 1600));
        assertFalse(GolChickBreedingTracker.isFresh(0, 601));
        assertFalse(GolChickBreedingTracker.isFresh(1000, 1601));
    }

    @Test
    public void testFeedWindowNegativeAgeNotFresh() {
        assertFalse(GolChickBreedingTracker.isFresh(100, 50));
        assertTrue(GolChickBreedingTracker.isFresh(100, 100));
    }

    // --- ChickenIdentity fields ---

    @Test
    public void testChickenIdentityTable() {
        assertEquals(4, ChickenIdentity.values().length);
        assertEquals("SPEED", ChickenIdentity.SWIFTWING.typeKey());
        assertEquals("Swiftwing", ChickenIdentity.SWIFTWING.individualName());
        assertEquals("The Storm-Hatched", ChickenIdentity.SWIFTWING.title());
        assertEquals("Dashclaw", ChickenIdentity.DASHCLAW.individualName());
        assertEquals("The Ambush Striker", ChickenIdentity.DASHCLAW.title());
        assertEquals("SLOW_FALL", ChickenIdentity.FEATHERFALL.typeKey());
        assertEquals("Lucky Beak", ChickenIdentity.LUCKY_BEAK.individualName());
        assertEquals("The Gilded Trickster", ChickenIdentity.LUCKY_BEAK.title());
    }

    @Test
    public void testChickenIdentityLookupAndBackstories() {
        assertSame(ChickenIdentity.SWIFTWING, ChickenIdentity.byTypeKey("SPEED"));
        assertSame(ChickenIdentity.FEATHERFALL, ChickenIdentity.byTypeKey("slow_fall"));
        assertTrue(ChickenIdentity.SWIFTWING.backstory().contains("lightning scar"));
        assertTrue(ChickenIdentity.DASHCLAW.backstory().contains("fox-raided"));
        assertTrue(ChickenIdentity.FEATHERFALL.backstory().contains("sculk warden"));
        assertTrue(ChickenIdentity.LUCKY_BEAK.backstory().contains("gold nugget"));
        assertEquals(10, ChickenIdentity.namePool().size());
        assertTrue(ChickenIdentity.namePool().contains("Kazuki Takahashi"));
    }

    // --- Config consistency: gradle.properties + fabric.mod.json ---

    @Test
    public void testConfigConsistency() throws Exception {
        Path propsPath = findProjectFile("gradle.properties");
        Path fabricPath = findProjectFile(Path.of("src", "main", "resources", "fabric.mod.json").toString());
        assertNotNull(propsPath, "gradle.properties not found");
        assertNotNull(fabricPath, "fabric.mod.json not found");

        Properties props = new Properties();
        try (var in = Files.newInputStream(propsPath)) {
            props.load(in);
        }
        assertEquals("26.3", props.getProperty("minecraft_version"));
        assertEquals("0.20.1", props.getProperty("loader_version"));
        assertEquals("0.165.2+26.3", props.getProperty("fabric_version"));
        assertEquals("1.5.0", props.getProperty("mod_version"));
        assertEquals("chikem-mod", props.getProperty("mod_id"));
        assertNull(props.getProperty("yarn_mappings"), "yarn_mappings must be removed");

        String fabricJson = Files.readString(fabricPath);
        assertTrue(fabricJson.contains("~26.3"), "fabric.mod.json minecraft dep must be ~26.3");
        assertTrue(fabricJson.contains(">=0.20.1"), "fabric.mod.json loader dep");
        assertTrue(fabricJson.contains(">=25"), "fabric.mod.json java dep");
        assertFalse(fabricJson.contains("chikemmod.client.mixins.json"), "client mixin entry must be removed");
    }

    @Test
    public void testModIdAndVersionFormat() {
        String modId = "chikem-mod";
        assertTrue(modId.matches("[a-z0-9-_]+"));
        assertTrue("1.5.0".matches("\\d+\\.\\d+\\.\\d+"));
    }

    private static Path findProjectFile(String name) {
        Path dir = Path.of("").toAbsolutePath();
        while (dir != null) {
            Path candidate = dir.resolve(name);
            if (Files.exists(candidate)) {
                return candidate;
            }
            candidate = dir.resolve("chikemMod").resolve(name);
            if (Files.exists(candidate)) {
                return candidate;
            }
            dir = dir.getParent();
        }
        return null;
    }
}
