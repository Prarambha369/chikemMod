package mr.bashyal.chickenmod.registry;

import mr.bashyal.chickenmod.item.ChickenSuperFeedItem;
import mr.bashyal.chickenmod.item.GoldenEggItem;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item SUPER_FEED = Registry.register(
        Registries.ITEM,
        new Identifier("chickenmod", "chicken_super_feed"),
        new ChickenSuperFeedItem(new Item.Settings().group(ItemGroup.MISC).maxCount(16))
    );

    public static final Item GOLDEN_EGG = Registry.register(
        Registries.ITEM,
        new Identifier("chickenmod", "golden_egg"),
        new GoldenEggItem(new Item.Settings().group(ItemGroup.MISC))
    );

    public static void register() {
        // items registered via static initializers
    }
}
