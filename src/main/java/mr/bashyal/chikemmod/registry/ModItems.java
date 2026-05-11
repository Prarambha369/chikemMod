package mr.bashyal.chikemmod.registry;

import mr.bashyal.chikemmod.ChikemMod;
import mr.bashyal.chikemmod.item.GolChickFoodItem;
import mr.bashyal.chikemmod.item.GoldieEggItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.Function;

public class ModItems {
    public static final Item GOLCHICK_FOOD = registerItem(
        "golchick_food",
        settings -> new GolChickFoodItem(settings.rarity(Rarity.UNCOMMON))
    );

    public static final Item GOLDIE_EGG = registerItem(
        "goldie_egg",
        settings -> new GoldieEggItem(settings.maxCount(16).rarity(Rarity.RARE))
    );

    private static Item registerItem(String name, Function<Item.Settings, Item> itemFactory) {
        Identifier itemId = Identifier.of(ChikemMod.MOD_ID, name);
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, itemId);
        Item.Settings settings = new Item.Settings().registryKey(itemKey);
        Item item = itemFactory.apply(settings);
        return Registry.register(Registries.ITEM, itemId, item);
    }

    public static void initialize() {
        ChikemMod.LOGGER.info("Registering items for " + ChikemMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> entries.add(GOLCHICK_FOOD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(GOLDIE_EGG));
    }
}