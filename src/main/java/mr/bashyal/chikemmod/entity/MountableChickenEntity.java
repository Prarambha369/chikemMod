package mr.bashyal.chikemmod.entity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mr.bashyal.chikemmod.interfaces.MountJumpable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import mr.bashyal.chikemmod.registry.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.item.ItemStack;

public class MountableChickenEntity extends ChickenEntity {
    public enum SpecialAbility {
        SPEED, SLOW_FALL, LUCK
    }

     private boolean isRareChicken = false;
     private SpecialAbility specialAbility = null;
     private static volatile List<String> rareChickenNames;
     private static volatile java.util.Map<String, SpecialAbility> rareChickenAbilities;
     private static final Random random = new Random();
     private net.minecraft.item.Item lastBredWith = null;

    // expose rarity and ability for external checks
    public boolean isRareChicken() { return isRareChicken; }
    public SpecialAbility getSpecialAbility() { return specialAbility; }

     public MountableChickenEntity(EntityType<? extends ChickenEntity> type, World world) {
         super(type, world);
         this.goalSelector.add(1, new WanderAroundGoal(this, 1.0));
         this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
         
         // Rare spawn logic: 1 in 50 chance (2%) for testing
         if (!world.isClient && random.nextInt(50) == 0) {
             String rareName = getRandomRareChickenName();
             if (rareName != null) {
                 this.setCustomName(Text.literal(rareName));
                 this.setCustomNameVisible(true);
                 this.isRareChicken = true;
                 this.specialAbility = getAbilityForName(rareName);
                 
                 // Log rare chicken spawn
                 System.out.println("[ChickenMod] Spawned rare chicken: " + rareName);
             }
         }
     }

     public void setRareChicken(String rareName) {
         this.setCustomName(Text.literal(rareName));
         this.setCustomNameVisible(true);
         this.isRareChicken = true;
         this.specialAbility = getAbilityForName(rareName);
     }

     @SuppressWarnings("unused")
     public static DefaultAttributeContainer.Builder createAttributes() {
         return MobEntity.createMobAttributes()
                 .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
                 .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
     }

     public boolean canBeRidden() {
         // Check if this is a rare chicken by name or ability
         if (this.hasCustomName() && this.getCustomName() != null) {
             String name = this.getCustomName().getString();
             if (rareChickenAbilities != null && rareChickenAbilities.containsKey(name)) {
                 this.isRareChicken = true;
                 this.specialAbility = getAbilityForName(name);
             }
         }
         return this.isRareChicken;
     }

     public void tick() {
         super.tick();
         if (this.hasPassengers()) {
             if (!this.getWorld().isClient && this.getFirstPassenger() instanceof PlayerEntity rider) {
                 // SLOW_FALL: Prevent fall damage and apply slow falling with controlled descent
                 if (this.specialAbility == SpecialAbility.SLOW_FALL) {
                     // Reset fall distance to prevent fall damage
                     this.fallDistance = 0;
                     rider.fallDistance = 0;
                     
                     // Apply slow falling effect if not already present
                     if (!rider.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
                         rider.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20, 0, true, false));
                     }
                     
                     // Apply slow falling to the chicken as well
                     if (!this.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
                         this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20, 0, true, false));
                     }
                     
                     // If falling, reduce vertical velocity for a controlled but faster descent
                     if (this.getVelocity().y < 0) {
                         this.setVelocity(this.getVelocity().x, Math.max(this.getVelocity().y * 0.8, -0.4), this.getVelocity().z);
                         this.velocityDirty = true;
                     }
                 }
                 // LUCK: Apply Luck effect to rider
                 if (this.specialAbility == SpecialAbility.LUCK) {
                     if (!rider.hasStatusEffect(StatusEffects.LUCK)) {
                         rider.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 20, 0, true, false));
                     }
                 }
             }
         }

         // Apply speed ability effects
         if (this.specialAbility == SpecialAbility.SPEED && this.hasPassengers() && this.getFirstPassenger() instanceof PlayerEntity) {
             var speedAttr = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
             if (speedAttr != null) {
                 speedAttr.setBaseValue(0.25D); // Reset to default speed when not moving
             }
         }

         // Egg laying logic
         if (!this.getWorld().isClient && this.isAlive() && !this.isBaby()) {
             if (this.isRareChicken) {
                 // Lay golden egg
                 if (random.nextInt(6000) == 0) { // same as vanilla chicken
                     this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
                     this.getWorld().spawnEntity(new net.minecraft.entity.ItemEntity(
                             this.getWorld(), this.getX(), this.getY(), this.getZ(),
                             new net.minecraft.item.ItemStack(mr.bashyal.chikemmod.registry.ModItems.GOLDEN_EGG)));
                 }
             } else {
                 // Lay normal egg (vanilla behavior)
                 if (random.nextInt(6000) == 0) {
                     this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
                     this.getWorld().spawnEntity(new net.minecraft.entity.ItemEntity(
                             this.getWorld(), this.getX(), this.getY(), this.getZ(),
                             new net.minecraft.item.ItemStack(net.minecraft.item.Items.EGG)));
                 }
             }
         }
     }

     public boolean isPushable() {
         return false; // Prevent pushing while mounted
     }

     public static String getRandomRareChickenName() {
         if (rareChickenNames == null) {
             rareChickenNames = loadRareChickenNames();
         }
         if (rareChickenNames != null && !rareChickenNames.isEmpty()) {
             return rareChickenNames.get(random.nextInt(rareChickenNames.size()));
         }
         return null;
     }

     private static List<String> loadRareChickenNames() {
         if (rareChickenNames != null) return rareChickenNames;
         synchronized (MountableChickenEntity.class) {
             if (rareChickenNames != null) return rareChickenNames;
             try (InputStream stream = MountableChickenEntity.class.getClassLoader().getResourceAsStream("rare_chicken_names.json")) {
                 if (stream == null) {
                     System.err.println("[ChickenMod] Warning: rare_chicken_names.json not found. No rare chicken names will be loaded.");
                     rareChickenNames = new ArrayList<>();
                     return rareChickenNames;
                 }
                 String json = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                 Gson gson = new Gson();
                 JsonObject obj = gson.fromJson(json, JsonObject.class);
                 JsonArray arr = obj.getAsJsonArray("names");
                 List<String> names = new ArrayList<>();
                 for (int i = 0; i < arr.size(); i++) {
                     names.add(arr.get(i).getAsString());
                 }
                 rareChickenNames = names;
                 return rareChickenNames;
             } catch (Exception e) {
                 System.err.println("[ChickenMod] Error loading rare_chicken_names.json: " + e.getMessage());
                 // Optionally print stack trace in debug mode only
                 // e.printStackTrace();
                 rareChickenNames = new ArrayList<>();
                 return rareChickenNames;
             }
         }
     }

     private static SpecialAbility getAbilityForName(String name) {
         if (rareChickenAbilities == null) {
             rareChickenAbilities = loadRareChickenAbilities();
         }
         if (rareChickenAbilities != null && rareChickenAbilities.containsKey(name)) {
             return rareChickenAbilities.get(name);
         }
         // fallback to random if not found
         return SpecialAbility.values()[random.nextInt(SpecialAbility.values().length)];
     }

     private static java.util.Map<String, SpecialAbility> loadRareChickenAbilities() {
         if (rareChickenAbilities != null) return rareChickenAbilities;
         synchronized (MountableChickenEntity.class) {
             if (rareChickenAbilities != null) return rareChickenAbilities;
             try (InputStream stream = MountableChickenEntity.class.getClassLoader().getResourceAsStream("rare_chicken_abilities.json")) {
                 if (stream == null) {
                     System.err.println("[ChickenMod] Warning: rare_chicken_abilities.json not found. No rare chicken abilities will be loaded.");
                     rareChickenAbilities = new java.util.HashMap<>();
                     return rareChickenAbilities;
                 }
                 String json = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                 Gson gson = new Gson();
                 JsonObject obj = gson.fromJson(json, JsonObject.class);
                 java.util.Map<String, SpecialAbility> map = new java.util.HashMap<>();
                 for (String key : obj.keySet()) {
                     try {
                         SpecialAbility ability = SpecialAbility.valueOf(obj.get(key).getAsString());
                         map.put(key, ability);
                     } catch (IllegalArgumentException ex) {
                         System.err.println("[ChickenMod] Invalid ability '" + obj.get(key).getAsString() + "' for rare chicken '" + key + "'. Skipping.");
                     }
                 }
                 rareChickenAbilities = map;
                 return rareChickenAbilities;
             } catch (Exception e) {
                 System.err.println("[ChickenMod] Error loading rare_chicken_abilities.json: " + e.getMessage());
                 // Optionally print stack trace in debug mode only
                 // e.printStackTrace();
                 rareChickenAbilities = new java.util.HashMap<>();
                 return rareChickenAbilities;
             }
         }
     }

     /**
     * Returns the rare chicken ability mapping (name -> ability).
     * Used by commands and logic to avoid reflection.
     */
    public static java.util.Map<String, SpecialAbility> getRareChickenAbilities() {
        if (rareChickenAbilities == null) {
            rareChickenAbilities = loadRareChickenAbilities();
        }
        return rareChickenAbilities;
    }

     @Override
     public ActionResult interactMob(PlayerEntity player, Hand hand) {
         ItemStack stack = player.getStackInHand(hand);
         if (this.isBreedingItem(stack)) {
             this.lastBredWith = stack.getItem();
         } else if (isSeed(stack)) {
             this.lastBredWith = stack.getItem();
         }
         if (!this.getWorld().isClient && this.canBeRidden() && !player.hasVehicle()) {
             player.startRiding(this);
             return ActionResult.SUCCESS;
         }
         return super.interactMob(player, hand);
     }

     private boolean isSeed(ItemStack stack) {
         return stack.getItem() == net.minecraft.item.Items.WHEAT_SEEDS ||
               stack.getItem() == net.minecraft.item.Items.BEETROOT_SEEDS ||
               stack.getItem() == net.minecraft.item.Items.PUMPKIN_SEEDS ||
               stack.getItem() == net.minecraft.item.Items.MELON_SEEDS ||
               stack.getItem() == net.minecraft.item.Items.TORCHFLOWER_SEEDS;
     }

     public net.minecraft.item.Item getLastBredWith() {
        return lastBredWith;
    }

    public void clearLastBredWith() {
        lastBredWith = null;
    }

     @Override
     public void travel(Vec3d movementInput) {
         // Check if a player is holding food and we're not already mounted
         if (!this.hasPassengers()) {
             List<PlayerEntity> players = this.getWorld().getNonSpectatingEntities(PlayerEntity.class, 
                 this.getBoundingBox().expand(8.0));
             
             for (PlayerEntity player : players) {
                 ItemStack stack = player.getMainHandStack();
                 if (stack.isEmpty()) stack = player.getOffHandStack();
                 
                 if (this.isBreedingItem(stack) || this.isSeed(stack)) {
                     this.getLookControl().lookAt(player, 10.0F, (float)this.getMaxLookPitchChange());
                     
                     if (this.squaredDistanceTo(player) >= 2.25) { // 1.5 blocks squared
                         this.getNavigation().startMovingTo(player, 1.0);
                     }
                     break;
                 }
             }
         }
         
         if (this.hasPassengers() && this.getFirstPassenger() instanceof LivingEntity rider) {
             // Step height is handled by the game's default behavior
             
             // Apply rider steering and orientation
             this.setRotation(rider.getYaw(), rider.getPitch() * 0.5F);
             this.sidewaysSpeed = rider.sidewaysSpeed;
             this.forwardSpeed = rider.forwardSpeed <= 0.0F ? rider.forwardSpeed * 0.25F : rider.forwardSpeed;
             
             // Apply speed boost only when player is actively moving (WASD pressed)
             if (this.specialAbility == SpecialAbility.SPEED && 
                 (Math.abs(rider.forwardSpeed) > 0.01F || Math.abs(rider.sidewaysSpeed) > 0.01F)) {
                 var speedAttr = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                 if (speedAttr != null) {
                     speedAttr.setBaseValue(0.75D); // Base 0.25 + 0.5 speed boost
                 }
                 this.velocityDirty = true;
             }

             // Jump only when spacebar is pressed (using our mixin for jump detection)
             boolean shouldJump = false;
             if (rider instanceof PlayerEntity && rider instanceof MountJumpable) {
                 shouldJump = ((MountJumpable) rider).shouldMountJump();
             }
             if (this.isOnGround() && shouldJump && !this.isTouchingWater()) {
                 // Check in multiple directions for better edge detection
                 Vec3d lookVec = this.getRotationVector();
                 Vec3d sideVec = new Vec3d(-lookVec.z, 0, lookVec.x).normalize().multiply(0.5);
                 
                 // Check center and both sides for better edge detection
                 BlockPos[] checkPositions = {
                     this.getBlockPos().add(
                         (int)Math.round(lookVec.x * 1.5), 
                         0, 
                         (int)Math.round(lookVec.z * 1.5)
                     ), // Center
                     this.getBlockPos().add(
                         (int)Math.round((lookVec.x * 1.5) + sideVec.x), 
                         0, 
                         (int)Math.round((lookVec.z * 1.5) + sideVec.z)
                     ), // Right side
                     this.getBlockPos().add(
                         (int)Math.round((lookVec.x * 1.5) - sideVec.x), 
                         0, 
                         (int)Math.round((lookVec.z * 1.5) - sideVec.z)
                     )  // Left side
                 };
                 
                 for (BlockPos checkPos : checkPositions) {
                     BlockPos groundPos = checkPos.down();
                     
                     // If we find a block to jump over, jump and break
                     if (!this.getWorld().isAir(groundPos) && 
                         this.getWorld().isAir(checkPos) && 
                         this.getWorld().isAir(checkPos.up())) {
                         
                         // Slightly higher jump for better clearance
                         this.setVelocity(this.getVelocity().x, 0.42F * 1.2, this.getVelocity().z);
                         this.velocityDirty = true;
                         
                         // Play jump sound
                         if (!this.isSilent()) {
                             this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), 
                                 SoundEvents.ENTITY_CHICKEN_STEP, this.getSoundCategory(), 
                                 0.15F, 1.5F);
                         }
                         break;
                     }
                 }
             }

             // Delegate movement
             super.travel(new Vec3d(this.sidewaysSpeed, movementInput.y, this.forwardSpeed));
         } else {
             super.travel(movementInput);
         }
     }

    public boolean isBreedingItem(ItemStack stack) {
        // Only GolChickFood can breed MountableChickenEntity
        return stack.getItem() == ModItems.GOLCHICK_FOOD;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ChickenEntity createChild(net.minecraft.server.world.ServerWorld world, net.minecraft.entity.passive.PassiveEntity entity) {
        MountableChickenEntity parent1 = this instanceof MountableChickenEntity ? (MountableChickenEntity) this : null;
        MountableChickenEntity parent2 = entity instanceof MountableChickenEntity ? (MountableChickenEntity) entity : null;
        net.minecraft.item.Item item1 = parent1 != null ? parent1.getLastBredWith() : null;
        net.minecraft.item.Item item2 = parent2 != null ? parent2.getLastBredWith() : null;
        boolean isGolChickFood1 = item1 == ModItems.GOLCHICK_FOOD;
        boolean isGolChickFood2 = item2 == ModItems.GOLCHICK_FOOD;
        boolean isSeed1 = parent1 != null && parent1.isSeed(new ItemStack(item1));
        boolean isSeed2 = parent2 != null && parent2.isSeed(new ItemStack(item2));
        boolean bothMountable = parent1 != null && parent2 != null;
        boolean bothVanilla = parent1 == null && parent2 == null;
        boolean oneMountable = (parent1 != null && parent2 == null) || (parent1 == null && parent2 != null);

        // GolChickFood logic
        if (isGolChickFood1 || isGolChickFood2) {
            if (bothMountable) {
                // 100% MountableChickenEntity
                clearLastBredWith();
                if (parent2 != null) parent2.clearLastBredWith();
                return new MountableChickenEntity((EntityType<? extends ChickenEntity>) this.getType(), world);
            } else if (bothVanilla) {
                // 10% MountableChickenEntity if both vanilla and GolChickFood used
                if (world.getRandom().nextInt(100) < 10) {
                    clearLastBredWith();
                    return new MountableChickenEntity((EntityType<? extends ChickenEntity>) this.getType(), world);
                } else {
                    clearLastBredWith();
                    return EntityType.CHICKEN.create(world);
                }
            }
        }
        // Seed logic
        if ((isSeed1 || isSeed2)) {
            int chance = 0;
            if (bothMountable) {
                chance = 10; // 10%
            } else if (bothVanilla) {
                chance = 1; // 1%
            } else if (oneMountable) {
                chance = 15; // 1.5% (scaled to 15/1000)
                if (world.getRandom().nextInt(1000) < chance) {
                    clearLastBredWith();
                    if (parent2 != null) parent2.clearLastBredWith();
                    return new MountableChickenEntity((EntityType<? extends ChickenEntity>) this.getType(), world);
                } else {
                    clearLastBredWith();
                    if (parent2 != null) parent2.clearLastBredWith();
                    return EntityType.CHICKEN.create(world);
                }
            }
            if (chance > 0 && world.getRandom().nextInt(100) < chance) {
                clearLastBredWith();
                if (parent2 != null) parent2.clearLastBredWith();
                return new MountableChickenEntity((EntityType<? extends ChickenEntity>) this.getType(), world);
            } else {
                clearLastBredWith();
                if (parent2 != null) parent2.clearLastBredWith();
                return EntityType.CHICKEN.create(world);
            }
        }
        // Default fallback
        clearLastBredWith();
        if (parent2 != null) parent2.clearLastBredWith();
        return EntityType.CHICKEN.create(world);
    }
}
