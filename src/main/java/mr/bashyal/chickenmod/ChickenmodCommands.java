package mr.bashyal.chickenmod;

import com.mojang.brigadier.CommandDispatcher;
import mr.bashyal.chickenmod.entity.MountableChickenEntity;
import mr.bashyal.chickenmod.registry.ModEntities;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.server.command.CommandManager.literal;

public class ChickenmodCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("chickem")
                .requires(source -> source.hasPermissionLevel(0))
                .executes(context -> {
                    var source = context.getSource();
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

                    // Always make it rare
                    String rareName = MountableChickenEntity.getRandomRareChickenName();
                    if (rareName != null) {
                        chicken.setRareChicken(rareName);
                    }

                    world.spawnEntity(chicken);
                    source.sendFeedback(() -> Text.literal("Spawned a rare chicken!"), false);
                    return 1;
                })
        );
    }
}
