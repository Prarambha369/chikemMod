package mr.bashyal.chickenmod.mixin.client;

import com.mojang.text2speech.Narrator;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mixin(NarratorManager.class)
public class NarratorManagerMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("ChickenMod");

    @Shadow private Narrator narrator;

    /**
     * This method is called when the narrator tries to narrate something.
     * We catch any exceptions that might occur and log them.
     */
    @Inject(method = "narrate(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    private void onNarrate(Text text, CallbackInfo ci) {
        if (narrator == null) {
            LOGGER.warn("Narrator is null. This is likely due to the narrator library not being found.");
            ci.cancel(); // Cancel the original method to prevent NullPointerException
        }
    }
}
