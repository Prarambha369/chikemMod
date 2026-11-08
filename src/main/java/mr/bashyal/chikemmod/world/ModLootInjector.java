package mr.bashyal.chikemmod.world;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.ints.UniformGenerator;

import java.util.Set;

public final class ModLootInjector {
    private static final Set<Identifier> STRUCTURE_CHEST_TABLES = Set.of(
            Identifier.fromNamespaceAndPath("minecraft", "chests/simple_dungeon"),
            Identifier.fromNamespaceAndPath("minecraft", "chests/abandoned_mineshaft"),
            Identifier.fromNamespaceAndPath("minecraft", "chests/desert_pyramid"),
            Identifier.fromNamespaceAndPath("minecraft", "chests/jungle_temple"),
            Identifier.fromNamespaceAndPath("minecraft", "chests/stronghold_corridor"),
            Identifier.fromNamespaceAndPath("minecraft", "chests/ancient_city"),
            Identifier.fromNamespaceAndPath("minecraft", "chests/end_city_treasure"),
            Identifier.fromNamespaceAndPath("minecraft", "chests/bastion_treasure")
    );

    private static final Identifier FISHING_TREASURE_TABLE = Identifier.fromNamespaceAndPath("minecraft", "gameplay/fishing/treasure");

    private ModLootInjector() {
    }

    public static void initialize() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin()) {
                return;
            }

            Identifier lootTableId = key.identifier();

            if (STRUCTURE_CHEST_TABLES.contains(lootTableId)) {
                // Structure chests: advertised pool chance; counts explicit here.
                tableBuilder.pool(createTreasurePool(mr.bashyal.chikemmod.registry.ModItems.GOLCHICK_FOOD, 0.35f, 1, 2));
                tableBuilder.pool(createTreasurePool(mr.bashyal.chikemmod.registry.ModItems.GOLDIE_EGG, 0.12f, 1, 1));
            }

            if (FISHING_TREASURE_TABLE.equals(lootTableId)) {
                // Fishing treasure: vanilla treasure pool has 5% base weight, so the
                // effective per-catch rate is far lower than the advertised pool chance.
                // Tooltips/README state this honestly (see en_us.json fishing note).
                // Counts explicit per call site.
                tableBuilder.pool(createTreasurePool(mr.bashyal.chikemmod.registry.ModItems.GOLCHICK_FOOD, 0.08f, 1, 2));
                tableBuilder.pool(createTreasurePool(mr.bashyal.chikemmod.registry.ModItems.GOLDIE_EGG, 0.03f, 1, 1));
            }
        });

        mr.bashyal.chikemmod.ChikemMod.LOGGER.info("Registered chikem loot injection for structures and fishing treasure");
    }

    /**
     * Single-entry pool: no weight needed (weight on a sole entry is meaningless).
     * Chance = pool chance; count = uniform [minCount, maxCount], explicit per call site.
     */
    private static LootPool createTreasurePool(
            ItemLike item,
            float chance,
            int minCount,
            int maxCount
    ) {
        return LootPool.lootPool()
                .setRolls(Holder.direct(new ConstantValue(1)))
                .when(Holder.direct(LootItemRandomChanceCondition.randomChance(chance).build()))
                .add(LootItem.lootTableItem(item))
                .apply(Holder.direct(SetItemCountFunction.setCount(
                        Holder.direct(new UniformGenerator(
                                Holder.direct(new ConstantValue(minCount)),
                                Holder.direct(new ConstantValue(maxCount)))))
                        .build()))
                .build();
    }
}
