package mr.bashyal.chickenmod.registry;

import mr.bashyal.chickenmod.item.ChickenSuperFeedItem;
import mr.bashyal.chickenmod.item.GoldenEggItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item SUPER_FEED = Registry.register(
            Registries.ITEM,
            Identifier.of("chickenmod", "chicken_super_feed"),
            new ChickenSuperFeedItem(new Item.Settings().maxCount(16))
    );

    public static final Item GOLDEN_EGG = Registry.register(
            Registries.ITEM,
            Identifier.of("chickenmod", "golden_egg"),
            new GoldenEggItem(new Item.Settings())
    );

    public static final Item GOLCHICK_FOOD = Registry.register(
            Registries.ITEM,
            Identifier.of("chickenmod", "golchick_food"),
            new Item(new Item.Settings().maxCount(16))
    );

    public static void register() {
        // items registered via static initializers
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(SUPER_FEED);
            entries.add(GOLDEN_EGG);
            entries.add(GOLCHICK_FOOD);
        });
    }
}
