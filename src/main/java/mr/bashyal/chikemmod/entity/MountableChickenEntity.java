package mr.bashyal.chikemmod.entity;

import mr.bashyal.chikemmod.registry.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class MountableChickenEntity extends ChickenEntity {
    public enum SpecialAbility {
        NORMAL,
        SPEED,
        DASH,
        SLOW_FALL,
        LUCK
    }

    private static final Map<String, SpecialAbility> RARE_CHICKEN_ABILITIES = Map.ofEntries(
            Map.entry("Swiftwing", SpecialAbility.SPEED),
            Map.entry("Dashclaw", SpecialAbility.DASH),
            Map.entry("Featherfall", SpecialAbility.SLOW_FALL),
            Map.entry("Lucky Beak", SpecialAbility.LUCK)
    );

    private static final String CHICKEN_TYPE_KEY = "ChickenType";
    private String chickenType = "NORMAL";
    private int dashCooldown = 0;
    private int luckEffectTimer = 0;

    public MountableChickenEntity(EntityType<? extends MountableChickenEntity> entityType, World world) {
        super(entityType, world);
    }

    public MountableChickenEntity(World world) {
        this(ModEntities.MOUNTABLE_CHICKEN, world);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString(CHICKEN_TYPE_KEY, this.chickenType);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains(CHICKEN_TYPE_KEY)) {
            this.chickenType = nbt.getString(CHICKEN_TYPE_KEY);
        }
    }

    public String getChickenType() {
        return this.chickenType;
    }

    public void setChickenType(String type) {
        this.chickenType = type;
        applyTypeAttributes();
    }

    public static Map<String, SpecialAbility> getRareChickenAbilities() {
        return new LinkedHashMap<>(RARE_CHICKEN_ABILITIES);
    }

    public static String getRandomRareChickenName() {
        var names = RARE_CHICKEN_ABILITIES.keySet().toArray(String[]::new);
        return names[ThreadLocalRandom.current().nextInt(names.length)];
    }

    private void applyTypeAttributes() {
        if ("SPEED".equals(chickenType)) {
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3125);
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (player.getStackInHand(hand).isEmpty() && !player.hasVehicle()) {
            if (!this.getWorld().isClient) {
                player.startRiding(this);
            }
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    public boolean canBeControlledByRider() {
        return this.getFirstPassenger() instanceof PlayerEntity;
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.hasPassengers() && this.canBeControlledByRider()) {
            LivingEntity rider = (LivingEntity) this.getFirstPassenger();
            this.setYaw(rider.getYaw());
            this.prevYaw = this.getYaw();
            this.setPitch(rider.getPitch() * 0.5F);
            this.setRotation(this.getYaw(), this.getPitch());
            this.bodyYaw = this.getYaw();
            this.headYaw = this.bodyYaw;

            float strafe = rider.sidewaysSpeed * 0.5F;
            float forward = rider.forwardSpeed;

            if (forward <= 0.0F) {
                forward *= 0.25F;
            }

            this.setMovementSpeed((float) this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
            super.travel(new Vec3d(strafe, movementInput.y, forward));
        } else {
            super.travel(movementInput);
        }
    }

    public void performDash() {
        if (dashCooldown <= 0 && "DASH".equals(chickenType)) {
            Vec3d lookVec = this.getRotationVec(1.0F);
            this.setVelocity(lookVec.x * 2.0, 0.5, lookVec.z * 2.0);
            this.velocityModified = true;
            dashCooldown = 100;
        }
    }

    @Override
    public void tick() {
        super.tick();
        
        if (dashCooldown > 0) {
            dashCooldown--;
        }

        if ("SLOW_FALL".equals(chickenType)) {
            if (!this.isOnGround() && this.getVelocity().y < 0) {
                this.setVelocity(this.getVelocity().multiply(1.0, 0.6, 1.0));
                this.velocityModified = true;
            }
        }

        if ("LUCK".equals(chickenType)) {
            luckEffectTimer++;
            if (luckEffectTimer >= 100) {
                luckEffectTimer = 0;
                for (PlayerEntity player : this.getWorld().getPlayers()) {
                    if (player.squaredDistanceTo(this) <= 64.0) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 200, 0, false, false));
                    }
                }
            }
        }
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, net.minecraft.entity.damage.DamageSource damageSource) {
        if ("SLOW_FALL".equals(chickenType)) {
            return false;
        }
        return super.handleFallDamage(fallDistance, damageMultiplier, damageSource);
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, net.minecraft.block.BlockState state, net.minecraft.util.math.BlockPos pos) {
        if ("SLOW_FALL".equals(chickenType)) {
            this.fallDistance = 0.0F;
        } else {
            super.fall(heightDifference, onGround, state, pos);
        }
    }
}