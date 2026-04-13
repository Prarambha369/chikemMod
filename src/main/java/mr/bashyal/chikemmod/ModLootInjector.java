package mr.bashyal.chikemmod;

//noinspection unused
@SuppressWarnings("unused")
public final class ModLootInjector {
    private ModLootInjector() {
    }

    public static void initialize() {
        mr.bashyal.chikemmod.world.ModLootInjector.initialize();
    }
}
