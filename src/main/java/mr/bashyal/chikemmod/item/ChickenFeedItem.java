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
import net.minecraft.nbt.NbtCompound;

import java.util.Random;

public class ChickenFeedItem extends Item {
    private static final Random RANDOM = new Random();
    private static final String FEED_COUNT_KEY = "chickenmod_feed_count";
    private static final String LAST_FED_KEY = "chickenmod_last_fed";

    public ChickenFeedItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        World world = user.getWorld();

        if (!world.isClient && entity instanceof ChickenEntity chicken) {
            // Check feeding cooldown to prevent spam feeding
            if (!canFeedChicken(chicken)) {
                user.sendMessage(Text.literal("🐔 This chicken is too full right now. Wait a moment before feeding again.")
                    .formatted(Formatting.YELLOW), false);
                return ActionResult.PASS;
            }

            stack.decrement(1);

            // Track feeding for nutrition system
            updateFeedingData(chicken);

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

            // Enhanced feedback based on nutrition level
            provideFeedingFeedback(chicken, user, world);

            return ActionResult.SUCCESS;
        }

        return super.useOnEntity(stack, user, entity, hand);
    }

    private boolean canFeedChicken(ChickenEntity chicken) {
        NbtCompound nbt = chicken.writeNbt(new NbtCompound());
        if (nbt.contains(LAST_FED_KEY)) {
            long lastFed = nbt.getLong(LAST_FED_KEY);
            long currentTime = chicken.getWorld().getTime();
            return (currentTime - lastFed) > 200; // 10 second cooldown
        }
        return true;
    }

    private void updateFeedingData(ChickenEntity chicken) {
        NbtCompound nbt = chicken.writeNbt(new NbtCompound());
        int feedCount = nbt.getInt(FEED_COUNT_KEY);
        long currentTime = chicken.getWorld().getTime();

        nbt.putInt(FEED_COUNT_KEY, feedCount + 1);
        nbt.putLong(LAST_FED_KEY, currentTime);
        chicken.readNbt(nbt);
    }

    private int getFeedCount(ChickenEntity chicken) {
        NbtCompound nbt = chicken.writeNbt(new NbtCompound());
        return nbt.getInt(FEED_COUNT_KEY);
    }

    private void handleMountableChicken(MountableChickenEntity mountable, PlayerEntity user, World world) {
        int feedCount = getFeedCount(mountable);

        // Increase chance of becoming rare based on nutrition level
        float rareChance = 0.15f + (feedCount * 0.02f); // Base 15% + 2% per feeding
        rareChance = Math.min(rareChance, 0.35f); // Cap at 35%

        if (!mountable.isRareChicken() && RANDOM.nextFloat() < rareChance) {
            String[] rareNames = {"Speedy", "Featherfall", "Lucky", "Golden", "Swift", "Champion"};
            String rareName = rareNames[RANDOM.nextInt(rareNames.length)];
            mountable.setRareChicken(rareName);

            user.sendMessage(Text.literal("✨ Your well-fed chicken has become rare: " + rareName + "!")
                .formatted(Formatting.GOLD), false);

            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.ENCHANT,
                    mountable.getX(), mountable.getY() + 1.0, mountable.getZ(),
                    15, 0.8, 0.8, 0.8, 0.0);
            }
        }

        // Well-fed chickens get bonus effects
        if (feedCount >= 5) {
            mountable.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 1200, 0));
            if (feedCount >= 10) {
                mountable.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1200, 0));
            }
        }

        // Chance for special events
        if (feedCount >= 3 && RANDOM.nextFloat() < 0.15f) {
            spawnSpecialEvent(world, mountable, user);
        }

        // Restore more health for mountable chickens
        mountable.heal(2.0F);
    }

    private void spawnSpecialEvent(World world, MountableChickenEntity mountable, PlayerEntity user) {
        float eventRoll = RANDOM.nextFloat();

        if (eventRoll < 0.4f && mountable.isRareChicken()) {
            // Golden egg for rare chickens
            world.spawnEntity(new net.minecraft.entity.ItemEntity(
                world, mountable.getX(), mountable.getY(), mountable.getZ(),
                new net.minecraft.item.ItemStack(mr.bashyal.chikemmod.registry.ModItems.GOLDEN_EGG)));
            user.sendMessage(Text.literal("🥇 Your rare chicken laid a golden egg!")
                .formatted(Formatting.GOLD), false);
        } else if (eventRoll < 0.7f) {
            // Speed boost
            if (mountable.hasPassengers()) {
                mountable.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1200, 1));
                user.sendMessage(Text.literal("⚡ Your chicken feels energized!")
                    .formatted(Formatting.YELLOW), false);
            }
        } else {
            // Enhanced abilities for rare chickens
            if (mountable.isRareChicken() && mountable.getSpecialAbility() != null) {
                user.sendMessage(Text.literal("🌟 Your chicken's " + mountable.getSpecialAbility().name() + " ability is enhanced!")
                    .formatted(Formatting.AQUA), false);
            }
        }
    }

    private void handleRegularChicken(ChickenEntity chicken, PlayerEntity user, World world) {
        int feedCount = getFeedCount(chicken);

        // Increase breeding readiness
        if (chicken.getBreedingAge() == 0) {
            int loveTime = 600 + (feedCount * 60); // Base 30s + 3s per feeding
            chicken.setLoveTicks(Math.min(loveTime, 1200)); // Cap at 1 minute
        } else if (chicken.getBreedingAge() < 0) {
            // Reduce baby growth time (more effective with better nutrition)
            int growthBonus = 1200 + (feedCount * 100);
            chicken.setBreedingAge(Math.min(0, chicken.getBreedingAge() + growthBonus));
        }

        // Better nutrition increases special event chances
        float eventChance = 0.05f + (feedCount * 0.01f);
        if (RANDOM.nextFloat() < eventChance) {
            user.sendMessage(Text.literal("🐔 The nutritious feed has attracted attention from nearby wildlife!")
                .formatted(Formatting.YELLOW), false);

            // Well-fed chickens may lay extra eggs
            if (feedCount >= 3 && RANDOM.nextFloat() < 0.3f) {
                world.spawnEntity(new net.minecraft.entity.ItemEntity(
                    world, chicken.getX(), chicken.getY(), chicken.getZ(),
                    new net.minecraft.item.ItemStack(net.minecraft.item.Items.EGG)));
                user.sendMessage(Text.literal("🥚 Your well-fed chicken laid a bonus egg!")
                    .formatted(Formatting.GREEN), false);
            }
        }

        // Attract nearby chickens for breeding
        if (feedCount >= 5 && RANDOM.nextFloat() < 0.20f) {
            attractNearbyChickens(chicken, world, user);
        }
    }

    private void attractNearbyChickens(ChickenEntity targetChicken, World world, PlayerEntity user) {
        if (world instanceof ServerWorld serverWorld) {
            var nearbyChickens = world.getEntitiesByClass(ChickenEntity.class,
                targetChicken.getBoundingBox().expand(8.0),
                chicken -> chicken != targetChicken && chicken.getBreedingAge() == 0);

            int madeReady = 0;
            for (ChickenEntity nearbyChicken : nearbyChickens) {
                if (RANDOM.nextFloat() < 0.30f && madeReady < 2) {
                    nearbyChicken.setLoveTicks(600);
                    madeReady++;

                    serverWorld.spawnParticles(ParticleTypes.HEART,
                        nearbyChicken.getX(), nearbyChicken.getY() + 0.5, nearbyChicken.getZ(),
                        3, 0.3, 0.2, 0.3, 0.0);
                }
            }

            if (madeReady > 0) {
                user.sendMessage(Text.literal("💞 " + madeReady + " nearby chicken(s) were attracted by the feed!")
                    .formatted(Formatting.LIGHT_PURPLE), false);
            }
        }
    }

    private void provideFeedingFeedback(ChickenEntity chicken, PlayerEntity user, World world) {
        int feedCount = getFeedCount(chicken);

        // Different sounds and particles based on nutrition level
        if (feedCount >= 10) {
            // Well-nourished chicken
            world.playSound(null, chicken.getX(), chicken.getY(), chicken.getZ(),
                SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.NEUTRAL, 1.0F, 1.5F);

            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                    chicken.getX(), chicken.getY() + 0.5, chicken.getZ(),
                    8, 0.5, 0.3, 0.5, 0.0);
            }

            if (feedCount == 10) {
                user.sendMessage(Text.literal("🌟 This chicken is now perfectly nourished!")
                    .formatted(Formatting.GOLD), false);
            }

        } else if (feedCount >= 5) {
            // Well-fed chicken
            world.playSound(null, chicken.getX(), chicken.getY(), chicken.getZ(),
                SoundEvents.ENTITY_CHICKEN_AMBIENT, SoundCategory.NEUTRAL, 1.0F, 1.3F);

            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.HEART,
                    chicken.getX(), chicken.getY() + 0.5, chicken.getZ(),
                    6, 0.5, 0.3, 0.5, 0.0);
            }

            if (feedCount == 5) {
                user.sendMessage(Text.literal("💚 This chicken is well-fed and healthy!")
                    .formatted(Formatting.GREEN), false);
            }

        } else {
            // Regular feeding
            world.playSound(null, chicken.getX(), chicken.getY(), chicken.getZ(),
                SoundEvents.ENTITY_CHICKEN_AMBIENT, SoundCategory.NEUTRAL, 1.0F, 1.2F);

            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.HEART,
                    chicken.getX(), chicken.getY() + 0.5, chicken.getZ(),
                    5, 0.5, 0.3, 0.5, 0.0);
            }
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        // Enhanced effects when players eat it directly
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
