package mr.bashyal.chikemmod.entity;

import mr.bashyal.chikemmod.registry.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

/**
 * Goldie Egg projectile: on impact it hatches a recognizable Special Chickem
 * (random identity: name + per-type tint), instead of a plain vanilla chick.
 */
public class GoldieEggProjectile extends ThrownEgg {

    public GoldieEggProjectile(Level level, LivingEntity owner, ItemStack item) {
        super(level, owner, item);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.GOLDIE_EGG;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (this.level().isClientSide()) {
            return;
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            ChickenIdentity identity = ChickenIdentity.random();
            MountableChickenEntity chickem = new MountableChickenEntity(serverLevel);
            chickem.setChickenType(identity.typeKey());
            chickem.snapTo(this.getX(), this.getY() + 0.1, this.getZ(), this.getYRot(), 0.0F);
            chickem.setBaby(true);
            chickem.applyIdentityDisplayName();
            if (serverLevel.addFreshEntity(chickem)) {
                serverLevel.sendParticles(ParticleTypes.EGG_CRACK,
                        this.getX(), this.getY(), this.getZ(), 20, 0.25, 0.25, 0.25, 0.2);
                serverLevel.playSound(null, this.getX(), this.getY(), this.getZ(),
                        SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 1.0F);
            }
        }
        this.discard();
    }
}
