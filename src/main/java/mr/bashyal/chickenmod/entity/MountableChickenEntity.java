package mr.bashyal.chickenmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MountableChickenEntity extends ChickenEntity {
    private boolean isGliding = false;

    public MountableChickenEntity(EntityType<? extends ChickenEntity> type, World world) {
        super(type, world);
        this.goalSelector.add(1, new WanderAroundGoal(this, 1.0));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
    }

    public boolean canBeRidden() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.hasPassengers()) {
            if (!this.getWorld().isClient && this.getFirstPassenger() instanceof PlayerEntity rider) {
                if (rider.fallDistance > 2 && !this.isOnGround()) {
                    this.isGliding = true;
                    rider.fallDistance = 0;
                    rider.setVelocity(rider.getVelocity().multiply(1, 0.6, 1));
                    this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_CHICKEN_HURT, this.getSoundCategory(), 1.0F, 1.0F);
                } else {
                    this.isGliding = false;
                }
            }
        }
    }
}

