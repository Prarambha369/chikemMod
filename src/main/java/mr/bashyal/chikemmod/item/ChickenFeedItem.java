package mr.bashyal.chikemmod.item;

import mr.bashyal.chikemmod.entity.MountableChickenEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
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

        if (!world.isClient && entity instanceof ChickenEntity chicken) {
            stack.decrement(1);

            // Enhanced healing for all chickens
            chicken.heal(4.0F);

            // Add beneficial effects to the chicken
            chicken.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 0));
            chicken.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 600, 0));

            // Special handling for MountableChickenEntity
            if (chicken instanceof MountableChickenEntity mountable) {
                handleMountableChicken(mountable, user, world);
            } else {
                handleRegularChicken(chicken, user, world);
            }

            // Play feeding sound and show particles
            world.playSound(null, chicken.getX(), chicken.getY(), chicken.getZ(),
                SoundEvents.ENTITY_CHICKEN_AMBIENT, SoundCategory.NEUTRAL, 1.0F, 1.2F);

            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.HEART,
                    chicken.getX(), chicken.getY() + 0.5, chicken.getZ(),
                    5, 0.5, 0.3, 0.5, 0.0);
            }

            return ActionResult.SUCCESS;
        }

        return super.useOnEntity(stack, user, entity, hand);
    }

    private void handleMountableChicken(MountableChickenEntity mountable, PlayerEntity user, World world) {
        // Increase chance of becoming rare if not already rare
        if (!mountable.isRareChicken() && RANDOM.nextFloat() < 0.15f) { // 15% chance
            String[] rareNames = {"Speedy", "Featherfall", "Lucky", "Golden", "Swift"};
            String rareName = rareNames[RANDOM.nextInt(rareNames.length)];
            mountable.setRareChicken(rareName);

            user.sendMessage(Text.literal("✨ Your chicken has become rare: " + rareName + "!")
                .formatted(Formatting.GOLD), false);

            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.ENCHANT,
                    mountable.getX(), mountable.getY() + 1.0, mountable.getZ(),
                    15, 0.8, 0.8, 0.8, 0.0);
            }
        }

        // Restore more health for mountable chickens
        mountable.heal(2.0F);
    }

    private void handleRegularChicken(ChickenEntity chicken, PlayerEntity user, World world) {
        // Increase breeding readiness
        if (chicken.getBreedingAge() == 0) {
            chicken.setLoveTicks(600); // 30 seconds of love mode
        } else if (chicken.getBreedingAge() < 0) {
            // Reduce baby growth time
            chicken.setBreedingAge(Math.min(0, chicken.getBreedingAge() + 1200)); // Speed up growth
        }

        // Small chance to spawn a rare chicken nearby when feeding regular chickens
        if (RANDOM.nextFloat() < 0.05f) { // 5% chance
            user.sendMessage(Text.literal("🐔 The premium feed has attracted a special visitor!")
                .formatted(Formatting.YELLOW), false);
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        // When players eat it directly, give beneficial effects (not poison!)
        if (!world.isClient && user instanceof PlayerEntity player) {
            // Give player beneficial effects related to chickens
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 20 * 60 * 5, 0)); // 5 minutes of luck
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20 * 30, 0)); // 30 seconds slow fall
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 60 * 2, 0)); // 2 minutes speed

            player.sendMessage(Text.literal("🌟 You feel energized by the premium chicken feed!")
                .formatted(Formatting.GREEN), false);

            // Restore some hunger and saturation
            player.getHungerManager().add(4, 0.6f);
        }

        return super.finishUsing(stack, world, user);
    }
}
