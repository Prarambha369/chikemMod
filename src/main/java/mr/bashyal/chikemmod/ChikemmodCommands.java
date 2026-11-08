package mr.bashyal.chikemmod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import mr.bashyal.chikemmod.entity.ChickenIdentity;
import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ChikemmodCommands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerChickemCommand(dispatcher);
        });
    }

    private static void registerChickemCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("chickem")
            .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
            .executes(ChikemmodCommands::executeChickem));
    }

    private static int executeChickem(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(Component.literal("Command must be executed by a player!").withStyle(ChatFormatting.RED));
            return 0;
        }

        ChickenIdentity identity = ChickenIdentity.random();
        MountableChickenEntity chicken = new MountableChickenEntity(source.getLevel());
        chicken.setChickenType(identity.typeKey());
        // Spawn offset above ground to avoid suffocation (net ease for safety).
        double spawnY = player.getY() + 2.0;
        chicken.setPos(player.getX(), spawnY, player.getZ());
        chicken.setYRot(player.getYRot());
        chicken.setXRot(0.0F);
        chicken.applyIdentityDisplayName();

        source.getLevel().addFreshEntity(chicken);
        player.sendSystemMessage(Component.literal("Spawned a " + identity.individualName() + " (" + identity.typeKey() + ") chicken!")
                .withStyle(ChatFormatting.GREEN));

        return 1;
    }
}
