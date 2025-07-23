package mr.bashyal.chikemmod;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic tests for Chickenmod registration and logic.
 */
public class ChickenmodBasicTest {
    @Test
    public void testSpecialAbilityEnum() {
        assertNotNull(mr.bashyal.chikemmod.entity.MountableChickenEntity.SpecialAbility.valueOf("SPEED"));
    }

    @Test
    public void testRareChickenAbilitiesLoaded() {
        var map = mr.bashyal.chikemmod.entity.MountableChickenEntity.getRareChickenAbilities();
        assertNotNull(map);
        assertFalse(map.isEmpty(), "Rare chicken abilities should not be empty");
    }

    @Test
    public void testRandomRareChickenName() {
        String name = mr.bashyal.chikemmod.entity.MountableChickenEntity.getRandomRareChickenName();
        assertNotNull(name);
        assertFalse(name.isEmpty());
    }
}
