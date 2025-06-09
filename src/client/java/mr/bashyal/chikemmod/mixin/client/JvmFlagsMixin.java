package mr.bashyal.chikemmod.mixin.client;

import net.minecraft.client.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin adds JVM flags to suppress JNI warnings.
 * It's not a perfect solution since it can't modify JVM flags after startup,
 * but it logs a helpful message for users.
 */
@Mixin(Main.class)
public class JvmFlagsMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("ChickenMod");

    /**
     * Logs a message about JNI warnings and how to suppress them.
     */
    @Inject(method = "main", at = @At("HEAD"))
    private static void onMainStart(String[] args, CallbackInfo ci) {
        LOGGER.info("To suppress JNI warnings, add --enable-native-access=ALL-UNNAMED to your JVM arguments");

        // Check if the flag is already set
        boolean hasNativeAccessFlag = false;
        for (String arg : args) {
            if (arg.contains("--enable-native-access=ALL-UNNAMED")) {
                hasNativeAccessFlag = true;
                break;
            }
        }

        if (!hasNativeAccessFlag) {
            LOGGER.info("JNI warnings about restricted methods are expected and can be safely ignored");
        }
    }
}