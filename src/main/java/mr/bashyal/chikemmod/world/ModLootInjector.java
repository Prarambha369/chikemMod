package mr.bashyal.chikemmod.world;

import mr.bashyal.chikemmod.ChikemMod;
import mr.bashyal.chikemmod.registry.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.Set;

public final class ModLootInjector {
    private static final Set<Identifier> STRUCTURE_CHEST_TABLES = Set.of(
            Identifier.ofVanilla("chests/simple_dungeon"),
            Identifier.ofVanilla("chests/abandoned_mineshaft"),
            Identifier.ofVanilla("chests/desert_pyramid"),
            Identifier.ofVanilla("chests/jungle_temple"),
            Identifier.ofVanilla("chests/stronghold_corridor"),
            Identifier.ofVanilla("chests/ancient_city"),
            Identifier.ofVanilla("chests/end_city_treasure"),
            Identifier.ofVanilla("chests/bastion_treasure")
    );

    private static final Identifier FISHING_TREASURE_TABLE = Identifier.ofVanilla("gameplay/fishing/treasure");

    private ModLootInjector() {
    }

    public static void initialize() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin()) {
                return;
            }

            Identifier lootTableId = key.getValue();

            if (STRUCTURE_CHEST_TABLES.contains(lootTableId)) {
                tableBuilder.pool(createTreasurePool(ModItems.GOLCHICK_FOOD, 0.35f, 2, 8));
                tableBuilder.pool(createTreasurePool(ModItems.GOLDIE_EGG, 0.12f, 1, 3));
            }

            if (FISHING_TREASURE_TABLE.equals(lootTableId)) {
                tableBuilder.pool(createTreasurePool(ModItems.GOLCHICK_FOOD, 0.08f, 1, 2));
                tableBuilder.pool(createTreasurePool(ModItems.GOLDIE_EGG, 0.03f, 1, 1));
            }
        });

        ChikemMod.LOGGER.info("Registered chikem loot injection for structures and fishing treasure");
    }

    private static LootPool.Builder createTreasurePool(
            ItemConvertible item,
            float chance,
            int maxCount,
            int weight
    ) {
        return LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(RandomChanceLootCondition.builder(chance))
                .with(ItemEntry.builder(item).weight(weight))
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, maxCount)));
    }
}
