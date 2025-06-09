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

public class ChickenmodCommands {
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
                SpecialAbility requestedAbility = SpecialAbility.valueOf(abilityArg.toUpperCase());
                // Find a chicken name with this ability
                chicken.setRareChicken(findChickenNameForAbility(requestedAbility));

                // Use the final requestedAbility variable in the lambda
                final SpecialAbility finalAbility = requestedAbility;
                source.sendFeedback(() -> Text.literal("Spawned a rare chicken with " + finalAbility.name() + " ability!"), false);
            } catch (IllegalArgumentException e) {
                source.sendError(Text.literal("Invalid ability: " + abilityArg));
                return 0;
            }
        } else {
            // Default behavior (always make it rare)
            String rareName = MountableChickenEntity.getRandomRareChickenName();
            if (rareName != null) {
                chicken.setRareChicken(rareName);
            }
            source.sendFeedback(() -> Text.literal("Spawned a rare chicken!"), false);
        }

        world.spawnEntity(chicken);
        return 1;
    }

    private static String findChickenNameForAbility(SpecialAbility ability) {
        // First, attempt to access the ability-to-name mapping if possible
        try {
            // Try to get reflection access to the internal map in MountableChickenEntity
            java.lang.reflect.Field field = MountableChickenEntity.class.getDeclaredField("rareChickenAbilities");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            java.util.Map<String, SpecialAbility> abilityMap =
                    (java.util.Map<String, SpecialAbility>) field.get(null);

            if (abilityMap != null) {
                // Find all chicken names that have the requested ability
                java.util.List<String> matchingChickens = new java.util.ArrayList<>();

                for (java.util.Map.Entry<String, SpecialAbility> entry : abilityMap.entrySet()) {
                    if (entry.getValue() == ability) {
                        matchingChickens.add(entry.getKey());
                    }
                }

                // If we found any matches, return a random one
                if (!matchingChickens.isEmpty()) {
                    return matchingChickens.get(new java.util.Random().nextInt(matchingChickens.size()));
                }
            }
        } catch (Exception e) {
            // If reflection access fails, fall back to returning a random name
            System.err.println("[ChickenMod] Couldn't access ability mapping: " + e.getMessage());
        }

        // If no mapping exists or no match was found, return a random name as fallback
        return MountableChickenEntity.getRandomRareChickenName();
    }
}
