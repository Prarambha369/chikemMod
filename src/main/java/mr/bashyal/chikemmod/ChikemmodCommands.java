package mr.bashyal.chikemmod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Random;

public class ChikemmodCommands {
    private static final Random RANDOM = new Random();
    private static final String[] SPECIAL_TYPES = {"SPEED", "DASH", "SLOW_FALL", "LUCK"};

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerChickemCommand(dispatcher);
        });
    }

    private static void registerChickemCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("chickem")
            .requires(source -> source.hasPermissionLevel(2))
            .executes(ChikemmodCommands::executeChickem));
    }

    private static int executeChickem(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        
        try {
            ServerPlayerEntity player = source.getPlayerOrThrow();
            MountableChickenEntity chicken = new MountableChickenEntity(player.getWorld());
            
            String randomType = SPECIAL_TYPES[RANDOM.nextInt(SPECIAL_TYPES.length)];
            chicken.setChickenType(randomType);
            chicken.refreshPositionAndAngles(player.getX(), player.getY(), player.getZ(), player.getYaw(), 0.0F);
            
            player.getWorld().spawnEntity(chicken);
            player.sendMessage(Text.literal("§aSpawned a " + randomType + " chicken!"), false);
            
            return 1;
        } catch (Exception e) {
            source.sendError(Text.literal("§cCommand must be executed by a player!"));
            return 0;
        }
    }
}