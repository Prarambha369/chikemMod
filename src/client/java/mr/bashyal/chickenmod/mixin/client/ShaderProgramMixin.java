package mr.bashyal.chickenmod.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.RenderPhase;

/**
 * This mixin suppresses the warning about missing Sampler2 in the rendertype_entity_translucent_emissive shader.
 */
@Mixin(ShaderProgram.class)
public class ShaderProgramMixin {

    /**
     * This method modifies the log level for the specific warning we want to suppress.
     * By returning true for the specific case we're targeting, we prevent the warning from being logged.
     */
    @ModifyVariable(method = "addSampler", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private String modifySamplerName(String name) {
        // If this is the Sampler2 we're trying to suppress warnings for,
        // we'll modify it to a name that exists in the shader
        if (name != null && name.equals("Sampler2") && 
            ((ShaderProgram)(Object)this).getName().equals("rendertype_entity_translucent_emissive")) {
            // Return a sampler name that does exist in the shader to avoid the warning
            // This is a hack, but it should work to suppress the warning
            return "Sampler0";
        }
        return name;
    }
}
