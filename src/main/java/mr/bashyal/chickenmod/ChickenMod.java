package mr.bashyal.chickenmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import mr.bashyal.chickenmod.registry.ModEntities;
import mr.bashyal.chickenmod.entity.MountableChickenEntity;

import static net.minecraft.server.command.CommandManager.literal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.text.Text;

public class ChickenMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // Register custom commands using Fabric API
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ChickenmodCommands.register(dispatcher);
        });
    }
}