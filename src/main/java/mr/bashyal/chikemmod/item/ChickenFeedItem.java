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
        NbtCompound entityNbt = new NbtCompound();
        chicken.writeCustomDataToNbt(entityNbt);

        if (entityNbt.contains(LAST_FED_KEY)) {
            long lastFed = entityNbt.getLong(LAST_FED_KEY);
            long currentTime = chicken.getWorld().getTime();
            return (currentTime - lastFed) > 200; // 10 second cooldown
        }
        return true;
    }

    private void updateFeedingData(ChickenEntity chicken) {
        // Get current NBT data
        NbtCompound entityNbt = new NbtCompound();
        chicken.writeCustomDataToNbt(entityNbt);

        // Update feed count and timestamp
        int currentFeedCount = entityNbt.getInt(FEED_COUNT_KEY);
        long currentTime = chicken.getWorld().getTime();

        entityNbt.putInt(FEED_COUNT_KEY, currentFeedCount + 1);
        entityNbt.putLong(LAST_FED_KEY, currentTime);

        // Write data back to chicken
        chicken.readCustomDataFromNbt(entityNbt);
    }

    private int getFeedCount(ChickenEntity chicken) {
        NbtCompound entityNbt = new NbtCompound();
        chicken.writeCustomDataToNbt(entityNbt);
        return entityNbt.getInt(FEED_COUNT_KEY);
    }

    private void handleMountableChicken(MountableChickenEntity mountable, PlayerEntity user, World world) {
        int feedCount = getFeedCount(mountable);

        // Enable breeding for mountable chickens
        if (mountable.getBreedingAge() == 0) {
            int loveTime = 800 + (feedCount * 60); // Base 40s + 3s per feeding
            mountable.setLoveTicks(Math.min(loveTime, 1200)); // Cap at 1 minute

            user.sendMessage(Text.literal("💕 Your mountable chicken is ready for breeding!")
                .formatted(Formatting.LIGHT_PURPLE), false);
        } else if (mountable.getBreedingAge() < 0) {
            // Reduce baby growth time (more effective with better nutrition)
            int growthBonus = 1400 + (feedCount * 120);
            mountable.setBreedingAge(Math.min(0, mountable.getBreedingAge() + growthBonus));
        }

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
                new ItemStack(mr.bashyal.chikemmod.registry.ModItems.GOLDEN_EGG)));
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

        // Primary breeding functionality - Set chicken in love mode
        if (chicken.getBreedingAge() == 0) {
            int loveTime = 600 + (feedCount * 60); // Base 30s + 3s per feeding
            chicken.setLoveTicks(Math.min(loveTime, 1200)); // Cap at 1 minute

            user.sendMessage(Text.literal("💕 This chicken is now ready for breeding!")
                .formatted(Formatting.LIGHT_PURPLE), false);

            // Add hearts particles to show breeding readiness
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.HEART,
                    chicken.getX(), chicken.getY() + 0.5, chicken.getZ(),
                    5, 0.5, 0.5, 0.5, 0.0);
            }
        } else if (chicken.getBreedingAge() < 0) {
            // Reduce baby growth time (more effective with better nutrition)
            int growthBonus = 1200 + (feedCount * 100);
            chicken.setBreedingAge(Math.min(0, chicken.getBreedingAge() + growthBonus));

            user.sendMessage(Text.literal("🐣 The nutritious food is helping this baby chicken grow faster!")
                .formatted(Formatting.GREEN), false);
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
                    new ItemStack(net.minecraft.item.Items.EGG)));
                user.sendMessage(Text.literal("🥚 Your well-fed chicken laid a bonus egg!")
                    .formatted(Formatting.GREEN), false);
            }
        }

        // Attract nearby chickens for breeding if this chicken is well-fed
        if (feedCount >= 5 && RANDOM.nextFloat() < 0.20f) {
            attractNearbyChickens(chicken, world, user);
        }
    }

    private void attractNearbyChickens(ChickenEntity targetChicken, World world, PlayerEntity user) {
        if (world instanceof ServerWorld serverWorld) {
            var nearbyChickens = world.getEntitiesByClass(ChickenEntity.class,
                targetChicken.getBoundingBox().expand(8.0),
                chicken -> chicken != targetChicken && chicken.getBreedingAge() == 0);

            if (!nearbyChickens.isEmpty()) {
                // Set nearby chickens in love mode too
                for (ChickenEntity nearbyChicken : nearbyChickens) {
                    if (nearbyChicken.getLoveTicks() <= 0) {
                        nearbyChicken.setLoveTicks(600);

                        // Spawn attraction particles
                        serverWorld.spawnParticles(ParticleTypes.HEART,
                            nearbyChicken.getX(), nearbyChicken.getY() + 0.5, nearbyChicken.getZ(),
                            3, 0.3, 0.3, 0.3, 0.0);
                    }
                }

                user.sendMessage(Text.literal("💕 The delicious aroma attracted " + nearbyChickens.size() + " nearby chickens for breeding!")
                    .formatted(Formatting.LIGHT_PURPLE), false);
            }
        }
    }

    private void provideFeedingFeedback(ChickenEntity chicken, PlayerEntity user, World world) {
        int feedCount = getFeedCount(chicken);

        // Play feeding sounds
        world.playSound(null, chicken.getBlockPos(), SoundEvents.ENTITY_CHICKEN_EGG,
            SoundCategory.NEUTRAL, 0.5F, 1.0F + (feedCount * 0.1F));

        // Visual feedback based on nutrition level
        if (world instanceof ServerWorld serverWorld) {
            if (feedCount >= 10) {
                // Golden particles for very well-fed chickens
                serverWorld.spawnParticles(ParticleTypes.END_ROD,
                    chicken.getX(), chicken.getY() + 0.5, chicken.getZ(),
                    8, 0.3, 0.3, 0.3, 0.02);
                user.sendMessage(Text.literal("✨ This chicken is exceptionally well-fed! (Fed " + feedCount + " times)")
                    .formatted(Formatting.GOLD), false);
            } else if (feedCount >= 5) {
                // Green particles for well-fed chickens
                serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                    chicken.getX(), chicken.getY() + 0.5, chicken.getZ(),
                    5, 0.3, 0.3, 0.3, 0.0);
                user.sendMessage(Text.literal("🌟 This chicken is well-fed and healthy! (Fed " + feedCount + " times)")
                    .formatted(Formatting.GREEN), false);
            } else {
                // Basic particles for newly fed chickens
                serverWorld.spawnParticles(ParticleTypes.CRIT,
                    chicken.getX(), chicken.getY() + 0.5, chicken.getZ(),
                    3, 0.2, 0.2, 0.2, 0.0);
                user.sendMessage(Text.literal("🐔 Chicken fed successfully! (Fed " + feedCount + " times)")
                    .formatted(Formatting.YELLOW), false);
            }
        }
    }
}
