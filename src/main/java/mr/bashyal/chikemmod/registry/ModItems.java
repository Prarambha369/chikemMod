package mr.bashyal.chikemmod.registry;
import mr.bashyal.chikemmod.item.GoldenEggItem;
import mr.bashyal.chikemmod.item.ChickenFeedItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item GOLDEN_EGG = Registry.register(
            Registries.ITEM,
            Identifier.of("chikemmod", "goldieegg"),
            new GoldenEggItem(new Item.Settings().maxCount(16))
    );

    public static final Item GOLCHICK_FOOD = Registry.register(
            Registries.ITEM,
            Identifier.of("chikemmod", "golchick_food"),
            new ChickenFeedItem(new Item.Settings().maxCount(64))
    );

    public static void register() {
        // items registered via static initializers
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(GOLDEN_EGG);
            entries.add(GOLCHICK_FOOD);
        });
    }
}
