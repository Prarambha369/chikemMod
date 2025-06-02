package mr.bashyal.chickenmod.entity;

import mr.bashyal.chickenmod.entity.MountableChickenEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.entity.SpawnReason;

public class GoldenEggEntity extends ThrownItemEntity {
    public GoldenEggEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public GoldenEggEntity(World world, LivingEntity owner) {
        super(mr.bashyal.chickenmod.registry.ModEntities.GOLDEN_EGG_ENTITY, owner, world);
    }

    @Override
    protected Item getDefaultItem() {
        return mr.bashyal.chickenmod.registry.ModItems.GOLDEN_EGG;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            MountableChickenEntity chicken = new MountableChickenEntity(mr.bashyal.chickenmod.registry.ModEntities.MOUNTABLE_CHICKEN, this.getWorld());
            chicken.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
            // Always rare with random name, saddle, and random ability
            String rareName = MountableChickenEntity.getRandomRareChickenName();
            if (rareName != null) {
                chicken.setRareChicken(rareName);
            }
            this.getWorld().spawnEntity(chicken);
            this.discard();
        }
    }
}

