package mr.bashyal.chikemmod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import mr.bashyal.chikemmod.entity.MountableChickenEntity.SpecialAbility;
import mr.bashyal.chikemmod.registry.ModEntities;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * Registers and handles the /chickem command, allowing users to spawn rare chickens with optional abilities.
 */
public class ChickenmodCommands {
    /**
     * Registers the /chickem command, allowing users to spawn rare chickens with optional abilities.
     * Uses Brigadier command API (Fabric v2+).
     */
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("chickem")
                .requires(source -> source.hasPermissionLevel(0))
                .executes(context -> spawnChicken(context.getSource(), null))
                .then(argument("ability", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            for (SpecialAbility ability : SpecialAbility.values()) {
                                builder.suggest(ability.name().toLowerCase());
                            }
                            return builder.buildFuture();
                        })
                        .executes(context -> spawnChicken(
                                context.getSource(),
                                StringArgumentType.getString(context, "ability")
                        ))
                )
        );
    }

    /**
     * Spawns a rare mountable chicken at the player's location.
     * If an ability is specified, assigns it; otherwise, assigns a random rare name and ability.
     * Uses reflection to access rare chicken names/abilities for flexibility, but should be avoided if possible for future-proofing.
     */
    private static int spawnChicken(ServerCommandSource source, String abilityArg) {
        var player = source.getPlayer();
        var world = source.getWorld();

        if (player == null || world == null) {
            return 0;
        }

        MountableChickenEntity chicken = new MountableChickenEntity(ModEntities.MOUNTABLE_CHICKEN, world);
        BlockPos spawnPos = player.getBlockPos().add(0, 1, 0);
        chicken.refreshPositionAndAngles(
                spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(),
                0, 0
        );

        if (abilityArg != null) {
            try {
                SpecialAbility ability = SpecialAbility.valueOf(abilityArg.toUpperCase());
                String rareName = findChickenNameForAbility(ability);
                chicken.setRareChicken(rareName);
                source.sendFeedback(() -> Text.literal("Spawned rare chicken with ability: " + ability), false);
            } catch (IllegalArgumentException e) {
                source.sendError(Text.literal("Invalid ability: " + abilityArg));
                return 0;
            }
        } else {
            chicken.setRareChicken(MountableChickenEntity.getRandomRareChickenName());
            source.sendFeedback(() -> Text.literal("Spawned a rare chicken!"), false);
        }

        world.spawnEntity(chicken);
        return 1;
    }

    /**
     * Finds a rare chicken name associated with the given ability.
     * Instead of using reflection, this now directly reads from the static mapping in MountableChickenEntity.
     * Returns a random rare chicken name if no match is found.
     */
    private static String findChickenNameForAbility(SpecialAbility ability) {
        // Directly access the rareChickenAbilities mapping if possible
        java.util.Map<String, SpecialAbility> abilityMap = mr.bashyal.chikemmod.entity.MountableChickenEntity.getRareChickenAbilities();
        if (abilityMap != null) {
            for (var entry : abilityMap.entrySet()) {
                if (entry.getValue() == ability) {
                    return entry.getKey();
                }
            }
        }
        // Fallback: return a random rare name
        return mr.bashyal.chikemmod.entity.MountableChickenEntity.getRandomRareChickenName();
    }
}
