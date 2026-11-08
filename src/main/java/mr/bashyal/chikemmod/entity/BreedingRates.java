package mr.bashyal.chikemmod.entity;

/**
 * Pure difficulty-based breeding odds (unit-testable; no Minecraft types taken as input).
 * The breeding mixin delegates to these so the tables can be tested without a runtime.
 */
public final class BreedingRates {

    private BreedingRates() {
    }

    public static double getSpecialSpawnChance(String difficulty, boolean hardcore) {
        if (hardcore) {
            return 0.12;
        }
        return switch (difficulty) {
            case "PEACEFUL" -> 0.30;
            case "EASY" -> 0.24;
            case "NORMAL" -> 0.18;
            case "HARD" -> 0.14;
            default -> 0.18;
        };
    }

    public static double getRareTypeShare(String difficulty, boolean hardcore) {
        if (hardcore) {
            return 0.10;
        }
        return switch (difficulty) {
            case "PEACEFUL" -> 0.25;
            case "EASY" -> 0.20;
            case "NORMAL" -> 0.15;
            case "HARD" -> 0.12;
            default -> 0.15;
        };
    }

    public static double getLuckShareInRare(String difficulty, boolean hardcore) {
        if (hardcore) {
            return 0.40;
        }
        return switch (difficulty) {
            case "PEACEFUL" -> 0.80;
            case "EASY" -> 0.75;
            case "NORMAL" -> 0.65;
            case "HARD" -> 0.50;
            default -> 0.65;
        };
    }

    public static double getSlowFallShareInCommon(String difficulty, boolean hardcore) {
        if (hardcore) {
            return 0.45;
        }
        return switch (difficulty) {
            case "PEACEFUL" -> 0.65;
            case "EASY" -> 0.60;
            case "NORMAL" -> 0.55;
            case "HARD" -> 0.50;
            default -> 0.55;
        };
    }
}
