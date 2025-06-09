package mr.bashyal.chickenmod.entity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
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

public class MountableChickenEntity extends ChickenEntity {
    public enum SpecialAbility {
        SPEED, DASH, SLOW_FALL, LUCK
    }

     private boolean isRareChicken = false;
     private SpecialAbility specialAbility = null;
     private static volatile List<String> rareChickenNames;
     private static volatile java.util.Map<String, SpecialAbility> rareChickenAbilities;
     private static final Random random = new Random();

    // expose rarity and ability for external checks
    public boolean isRareChicken() { return isRareChicken; }
    public SpecialAbility getSpecialAbility() { return specialAbility; }

     public MountableChickenEntity(EntityType<? extends ChickenEntity> type, World world) {
         super(type, world);
         this.goalSelector.add(1, new WanderAroundGoal(this, 1.0));
         this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
         // Rare spawn logic: 1 in 612 chance (like pink sheep)
         if (!world.isClient && random.nextInt(612) == 0) {
             String rareName = getRandomRareChickenName();
             if (rareName != null) {
                 this.setCustomName(Text.literal(rareName));
                 this.setCustomNameVisible(true);
                 this.isRareChicken = true;
                 this.specialAbility = getAbilityForName(rareName);
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
         return true;
     }

     public void tick() {
         super.tick();
         if (this.hasPassengers()) {
             if (!this.getWorld().isClient && this.getFirstPassenger() instanceof PlayerEntity rider) {
                 if (rider.fallDistance > 2 && !this.isOnGround()) {
                     rider.fallDistance = 0;
                     rider.setVelocity(rider.getVelocity().multiply(1, 0.6, 1));
                     this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_CHICKEN_HURT, this.getSoundCategory(), 1.0F, 1.0F);
                 }
             }
         }

         // Ability effects
         if (this.hasPassengers() && this.isRareChicken && this.specialAbility != null && this.getFirstPassenger() instanceof PlayerEntity rider) {
             var speedAttr = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
             switch (this.specialAbility) {
                 case SPEED -> { if (speedAttr != null) speedAttr.setBaseValue(0.45D); }
                 case DASH -> performDash();
                 case SLOW_FALL -> { if (!rider.getWorld().isClient) rider.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 40, 0, true, false)); }
                 case LUCK -> { if (!rider.getWorld().isClient) rider.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 40, 0, true, false)); }
             }
         } else if (this.specialAbility == SpecialAbility.SPEED) {
             var speedAttr = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
             if (speedAttr != null) speedAttr.setBaseValue(0.25D);
         }

         // Egg laying logic
         if (!this.getWorld().isClient && this.isAlive() && !this.isBaby()) {
             if (this.isRareChicken) {
                 // Lay golden egg
                 if (random.nextInt(6000) == 0) { // same as vanilla chicken
                     this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
                     this.getWorld().spawnEntity(new net.minecraft.entity.ItemEntity(
                             this.getWorld(), this.getX(), this.getY(), this.getZ(),
                             new net.minecraft.item.ItemStack(mr.bashyal.chickenmod.registry.ModItems.GOLDEN_EGG)));
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

     public void performDash() {
         if (this.hasPassengers() && this.isRareChicken && this.specialAbility == SpecialAbility.DASH) {
             // Give a forward burst of speed
             double dashStrength = 1.5D;
             double dx = this.getRotationVector().x * dashStrength;
             double dz = this.getRotationVector().z * dashStrength;
             this.setVelocity(dx, 0.3, dz);
             this.velocityDirty = true;
             this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_CHICKEN_HURT, this.getSoundCategory(), 1.0F, 1.2F);
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

     @Override
     public ActionResult interactMob(PlayerEntity player, Hand hand) {
         if (!this.getWorld().isClient && this.canBeRidden() && !player.hasVehicle()) {
             player.startRiding(this);
             return ActionResult.SUCCESS;
         }
         return super.interactMob(player, hand);
     }

     @Override
     public void travel(Vec3d movementInput) {
         if (this.hasPassengers() && this.getFirstPassenger() instanceof LivingEntity rider) {
             // Apply rider steering and orientation
             this.setRotation(rider.getYaw(), rider.getPitch() * 0.5F);
             this.sidewaysSpeed = rider.sidewaysSpeed;
             this.forwardSpeed = rider.forwardSpeed <= 0.0F ? rider.forwardSpeed * 0.25F : rider.forwardSpeed;

             // Handle jump input (movementInput.y > 0 indicates jump button)
             if (movementInput.y > 0.0D && this.isOnGround()) {
                 // Jump one block high
                 double jumpStrength = Math.sqrt(0.16D * 1.0D);
                 this.setVelocity(this.getVelocity().x, jumpStrength, this.getVelocity().z);
                 this.velocityDirty = true;
             }

             // Delegate movement
             super.travel(new Vec3d(this.sidewaysSpeed, 0.0D, this.forwardSpeed));
         } else {
             super.travel(movementInput);
         }
     }
}
