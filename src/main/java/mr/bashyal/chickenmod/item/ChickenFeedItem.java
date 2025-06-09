package mr.bashyal.chickenmod.item;

import mr.bashyal.chickenmod.registry.ModEntities;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Random;

public class ChickenFeedItem extends Item {
    private static final Random RANDOM = new Random();

    public ChickenFeedItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        World world = user.getWorld();
        if (!world.isClient && entity instanceof ChickenEntity target) {
            var nearby = world.getEntitiesByClass(ChickenEntity.class, target.getBoundingBox().expand(5.0), e -> true);
            if (nearby.size() > 1) {
                stack.decrement(1);
                if (RANDOM.nextDouble() < 0.3) {
                    var special = ModEntities.MOUNTABLE_CHICKEN.create(world);
                    if (special != null) {
                        special.refreshPositionAndAngles(target.getX(), target.getY(), target.getZ(), target.getYaw(), 0.0F);
                        world.spawnEntity(special);
                    }
                } else {
                    ChickenEntity normal = new ChickenEntity(EntityType.CHICKEN, world);
                    normal.refreshPositionAndAngles(target.getX(), target.getY(), target.getZ(), target.getYaw(), 0.0F);
                    world.spawnEntity(normal);
                }
                return ActionResult.SUCCESS;
            }
        }
        return super.useOnEntity(stack, user, entity, hand);
    }
}
