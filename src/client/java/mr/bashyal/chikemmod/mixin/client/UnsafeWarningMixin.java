package mr.bashyal.chikemmod.mixin.client;

import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin logs a message about the deprecated sun.misc.Unsafe method warning.
 * Since we can't modify the third-party code that's using the deprecated method,
 * we just inform the user that the warning is expected and can be safely ignored.
 */
@Mixin(MinecraftClient.class)
public class UnsafeWarningMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("ChickenMod");
    private static boolean loggedUnsafeWarning = false;

    /**
     * Logs a message about the deprecated sun.misc.Unsafe method warning.
     */
    @Inject(method = "<init>*", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        if (!loggedUnsafeWarning) {
            LOGGER.info("Warning about deprecated sun.misc.Unsafe method is expected and can be safely ignored");
            LOGGER.info("This warning comes from a third-party library and does not affect functionality");
            loggedUnsafeWarning = true;
        }
    }
}