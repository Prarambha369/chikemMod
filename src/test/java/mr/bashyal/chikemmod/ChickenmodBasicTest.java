package mr.bashyal.chikemmod;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Lightweight JVM tests that do not depend on a running Minecraft runtime.
 */
public class ChickenmodBasicTest {
    @Test
    public void testModIdFormat() {
        String modId = "chikem-mod";
        assertTrue(modId.matches("[a-z0-9-_]+"));
    }

    @Test
    public void testVersionSemverLike() {
        String version = "1.4.0";
        assertTrue(version.matches("\\d+\\.\\d+\\.\\d+"));
    }

    @Test
    public void testBasicArithmeticSanity() {
        assertEquals(4, 2 + 2);
    }
}
