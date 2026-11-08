package mr.bashyal.chikemmod.registry;

import mr.bashyal.chikemmod.ChikemMod;
import mr.bashyal.chikemmod.item.GolChickFoodItem;
import mr.bashyal.chikemmod.item.GoldieEggItem;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.function.Function;

public class ModItems {
    public static final Item GOLCHICK_FOOD = registerItem(
        "golchick_food",
        settings -> new GolChickFoodItem(settings.rarity(Rarity.UNCOMMON))
    );

    public static final Item GOLDIE_EGG = registerItem(
        "goldie_egg",
        settings -> new GoldieEggItem(settings.stacksTo(16).rarity(Rarity.RARE))
    );

    private static Item registerItem(String name, Function<Item.Properties, Item> itemFactory) {
        Identifier itemId = Identifier.fromNamespaceAndPath(ChikemMod.MOD_ID, name);
        Item.Properties settings = new Item.Properties()
                .setId(ResourceKey.create(BuiltInRegistries.ITEM.key(), itemId));
        Item item = itemFactory.apply(settings);
        return Registry.register(BuiltInRegistries.ITEM, itemId, item);
    }

    public static void initialize() {
        ChikemMod.LOGGER.info("Registering items for " + ChikemMod.MOD_ID);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> entries.accept(GOLCHICK_FOOD));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> entries.accept(GOLDIE_EGG));
    }
}
