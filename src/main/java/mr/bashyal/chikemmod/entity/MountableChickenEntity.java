package mr.bashyal.chikemmod.entity;

import mr.bashyal.chikemmod.registry.ModEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.Vec3;

public class MountableChickenEntity extends Chicken {
    private static final EntityDataAccessor<String> CHICKEN_TYPE_DATA = SynchedEntityData.defineId(MountableChickenEntity.class, EntityDataSerializers.STRING);

    private static final String CHICKEN_TYPE_KEY = "ChickenType";
    private int dashCooldown = 0;
    private int luckEffectTimer = 0;
    private boolean dashSlowdown = false;

    public MountableChickenEntity(EntityType<? extends MountableChickenEntity> entityType, Level level) {
        super(entityType, level);
    }

    public MountableChickenEntity(Level level) {
        this(ModEntities.MOUNTABLE_CHICKEN, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CHICKEN_TYPE_DATA, "NORMAL");
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putString(CHICKEN_TYPE_KEY, getChickenType());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setChickenType(input.getStringOr(CHICKEN_TYPE_KEY, "NORMAL"));
        applyTypeAttributes();
    }

    public String getChickenType() {
        return this.entityData.get(CHICKEN_TYPE_DATA);
    }

    public void setChickenType(String type) {
        this.entityData.set(CHICKEN_TYPE_DATA, type == null ? "NORMAL" : type);
        applyTypeAttributes();
    }

    public ChickenIdentity getIdentity() {
        return ChickenIdentity.byTypeKey(getChickenType());
    }

    /** Apply display custom name = random pool name + type title (Task 4 naming). Visible above head. */
    public void applyIdentityDisplayName() {
        ChickenIdentity identity = ChickenIdentity.byTypeKey(getChickenType());
        if (identity == null) {
            return;
        }
        setCustomName(Component.literal(ChickenIdentity.randomName()).append(" ").append(Component.translatable(identity.titleKey())));
        setCustomNameVisible(true);
    }

    private void applyTypeAttributes() {
        String type = getChickenType();
        AttributeInstance speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null) {
            if ("SPEED".equals(type)) {
                speed.setBaseValue(0.3125);
            } else if ("SLOW_FALL".equals(type)) {
                speed.setBaseValue(0.15);
            } else {
                // Reset base speed for non-matching types (vanilla chicken base).
                speed.setBaseValue(0.25);
            }
        }
        AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
        if (health != null) {
            if ("SPEED".equals(type)) {
                health.setBaseValue(2.0);
            } else {
                // Reset base health for non-matching types (vanilla chicken: 4.0).
                health.setBaseValue(4.0);
            }
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 pos) {
        // Sneak + right-click: read this chickem's backstory into chat (no mounting).
        if (player.isCrouching() && !this.level().isClientSide()) {
            var identity = getIdentity();
            if (identity != null) {
                player.sendSystemMessage(Component.literal(identity.individualName()
                        + " \u2014 " + identity.title()));
                player.sendSystemMessage(Component.literal(identity.backstory()));
            }
            return InteractionResult.SUCCESS;
        }
        // Only mount when unoccupied.
        if (player.getItemInHand(hand).isEmpty() && !player.isPassenger() && getControllingPassenger() == null && getFirstPassenger() == null) {
            if (!this.level().isClientSide()) {
                player.startRiding(this);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public boolean canBeControlledByRider() {
        return this.getControllingPassenger() != null;
    }

    @Override
    public Player getControllingPassenger() {
        if (getFirstPassenger() instanceof Player player) {
            return player;
        }
        return null;
    }

    @Override
    public void travel(Vec3 movementInput) {
        if (getControllingPassenger() != null) {
            LivingEntity rider = getControllingPassenger();
            this.setYRot(rider.getYRot());
            this.setXRot(rider.getXRot() * 0.5F);
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.yBodyRot;

            float strafe = rider.xxa * 0.5F;
            float forward = rider.zza;

            if (forward <= 0.0F) {
                forward *= 0.25F;
            }

            // Rider jump control.
            if (rider.isJumping() && this.onGround()) {
                this.setDeltaMovement(this.getDeltaMovement().x, 0.42, this.getDeltaMovement().z);
            }

            AttributeInstance speedInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            double speedAttr = speedInstance != null ? speedInstance.getValue() : 0.25;
            if ("DASH".equals(getChickenType()) && dashSlowdown) {
                speedAttr *= 0.5;
            }
            this.setSpeed((float) speedAttr);
            super.travel(new Vec3(strafe, movementInput.y, forward));
        } else {
            super.travel(movementInput);
        }
    }

    public void performDash() {
        if (dashCooldown <= 0 && "DASH".equals(getChickenType())) {
            Vec3 lookVec = this.getViewVector(1.0F);
            this.setDeltaMovement(lookVec.x * 2.0, 0.5, lookVec.z * 2.0);
            dashCooldown = 100;
            dashSlowdown = true;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (dashCooldown > 0) {
            dashCooldown--;
        }
        if (dashCooldown <= 0) {
            dashSlowdown = false;
        }

        if ("SLOW_FALL".equals(getChickenType())) {
            if (!this.onGround() && this.getDeltaMovement().y < 0) {
                Vec3 v = this.getDeltaMovement();
                this.setDeltaMovement(v.x, v.y * 0.6, v.z);
            }
        }

        if ("LUCK".equals(getChickenType())) {
            luckEffectTimer++;
            if (luckEffectTimer >= 100) {
                luckEffectTimer = 0;
                for (Player player : this.level().players()) {
                    if (player.distanceToSqr(this) <= 64.0) {
                        player.addEffect(new MobEffectInstance(MobEffects.LUCK, 200, 0, false, false));
                    }
                }
            }
        }
    }

    @Override
    protected void checkFallDamage(double heightDifference, boolean onGround, net.minecraft.world.level.block.state.BlockState state, net.minecraft.core.BlockPos pos) {
        if ("SLOW_FALL".equals(getChickenType())) {
            this.fallDistance = 0.0F;
        } else {
            super.checkFallDamage(heightDifference, onGround, state, pos);
        }
    }
}
