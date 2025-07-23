package mr.bashyal.chikemmod.mixin;

import mr.bashyal.chikemmod.interfaces.MountJumpable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to handle player input for mounted jumping
 */

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity implements MountJumpable {
    @Unique
    private boolean wasJumping = false;
    @Unique
    private boolean shouldMountJump = false;
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        
        // Check if player is trying to jump by checking their vertical movement input
        boolean isJumping = false;
        try {
            // Try to access the input field using reflection as a fallback
            Object input = PlayerEntity.class.getDeclaredField("input").get(player);
            if (input != null) {
                isJumping = (boolean) input.getClass().getDeclaredField("jumping").get(input) && player.isOnGround();
            }
        } catch (Exception e) {
            // If reflection fails, use a more reliable approach
            Vec3d movementInput = player.getRotationVector();
            isJumping = movementInput.y > 0 && player.isOnGround();
        }
        
        if (player.getVehicle() != null) {
            if (isJumping && !wasJumping) {
                shouldMountJump = true;
            } else {
                shouldMountJump = false;
            }
        }
        wasJumping = isJumping;
    }
    
    @Override
    public boolean shouldMountJump() {
        return shouldMountJump;
    }
}
