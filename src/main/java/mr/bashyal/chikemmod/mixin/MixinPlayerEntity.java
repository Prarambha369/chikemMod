package mr.bashyal.chikemmod.mixin;

import mr.bashyal.chikemmod.interfaces.MountJumpable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity implements MountJumpable {
    @Shadow public abstract boolean isSpectator();
    @Shadow public abstract boolean isCreative();
    @Shadow public abstract Vec3d getVelocity();
    @Shadow public abstract boolean isOnGround();

    private boolean wasJumping = false;
    private boolean shouldMountJump = false;
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        // Detect jumping by checking if player has upward velocity while previously on ground
        boolean isJumping = !this.isOnGround() && this.getVelocity().y > 0.1;

        if (((PlayerEntity)(Object)this).getVehicle() != null) {
            if (isJumping && !wasJumping) {
                shouldMountJump = true;
            } else if (!isJumping) {
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
