package com.mikersmutant.mutant.entity;

import com.mikersmutant.mutant.ModEntities;
import com.mikersmutant.mutant.ModSounds;
import com.mikersmutant.mutant.config.MutantConfig;
import com.mikersmutant.mutant.entity.goal.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class MutantEntity extends Monster {
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(MutantEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LYING = SynchedEntityData.defineId(MutantEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> LYING_TIMER = SynchedEntityData.defineId(MutantEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DAYS_ALIVE = SynchedEntityData.defineId(MutantEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_PANICKING = SynchedEntityData.defineId(MutantEntity.class, EntityDataSerializers.BOOLEAN);
    
    private int panicTimer = 0;
    private int attackJumpTimer = 0;
    private int eatTimer = 0;
    private boolean isDaySleeper = true;

    public MutantEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        double health = MutantConfig.DATA.mutant_health;
        double speed = MutantConfig.DATA.mutant_speed;
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, health)
                .add(Attributes.MOVEMENT_SPEED, speed)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.FOLLOW_RANGE, 48.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MutantPanicGoal(this));
        this.goalSelector.addGoal(2, new MutantSleepGoal(this));
        this.goalSelector.addGoal(3, new MutantTunnelGoal(this));
        this.goalSelector.addGoal(4, new MutantEatGoal(this));
        this.goalSelector.addGoal(5, new MutantBreakHiveGoal(this));
        this.goalSelector.addGoal(6, new MutantBreakTorchGoal(this));
        this.goalSelector.addGoal(7, new MutantScrapeBarkGoal(this));
        this.goalSelector.addGoal(8, new MutantPlayWithDoorGoal(this));
        this.goalSelector.addGoal(9, new MutantCreeperProvokeGoal(this));
        this.goalSelector.addGoal(10, new MutantFollowGroupGoal(this));
        this.goalSelector.addGoal(11, new MutantRememberPlaceGoal(this));
        this.goalSelector.addGoal(12, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(13, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(14, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, e -> !(e instanceof MutantEntity)));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SLEEPING, false);
        this.entityData.define(LYING, false);
        this.entityData.define(LYING_TIMER, 0);
        this.entityData.define(DAYS_ALIVE, 0);
        this.entityData.define(IS_PANICKING, false);
    }

    @Override
    public void tick() {
        super.tick();
        
        if (!level().isClientSide) {
            long worldDay = level().getDayTime() / 24000;
            int days = (int) Math.min(worldDay, MutantConfig.DATA.max_days);
            this.entityData.set(DAYS_ALIVE, days);
            applyEvolution(days);
            
            if (this.entityData.get(IS_PANICKING)) {
                panicTimer--;
                if (panicTimer <= 0) {
                    this.entityData.set(IS_PANICKING, false);
                }
            }
            
            if (this.entityData.get(LYING)) {
                int timer = this.entityData.get(LYING_TIMER);
                if (timer <= 0) {
                    this.entityData.set(LYING, false);
                    this.setHealth(this.getMaxHealth());
                    this.setNoAi(false);
                } else {
                    this.entityData.set(LYING_TIMER, timer - 1);
                }
                return;
            }
            
            if (this.getHealth() < 10 && !this.entityData.get(LYING) && !this.isDeadOrDying()) {
                this.entityData.set(LYING, true);
                this.entityData.set(LYING_TIMER, 2000);
                this.setNoAi(true);
                return;
            }
            
            boolean day = level().isDay();
            boolean dark = level().getMaxLocalRawBrightness(this.blockPosition()) == 0;
            boolean inCave = this.blockPosition().getY() < 50;
            if (day && dark && inCave && this.isDaySleeper && !this.entityData.get(LYING)) {
                this.entityData.set(SLEEPING, true);
                this.setNoAi(true);
            } else {
                if (!this.entityData.get(LYING)) {
                    this.entityData.set(SLEEPING, false);
                    this.setNoAi(false);
                }
            }
            
            if (this.attackJumpTimer > 0) {
                attackJumpTimer--;
                if (attackJumpTimer == 0 && this.getTarget() != null) {
                    double dx = this.getTarget().getX() - this.getX();
                    double dz = this.getTarget().getZ() - this.getZ();
                    double dist = Math.sqrt(dx * dx + dz * dz);
                    if (dist <= 5.0) {
                        this.getTarget().hurt(DamageSource.mobAttack(this), 10.0f);
                    }
                }
            }
            
            if (level().isDay() && level().canSeeSky(this.blockPosition())) {
                if (MutantConfig.DATA.sun_damage_enabled) {
                    if (days < 10) {
                        this.hurt(this.damageSources().onFire(), 40.0f);
                    } else if (days < 30) {
                        this.hurt(this.damageSources().onFire(), 4.0f);
                    } else if (days < 60) {
                        this.hurt(this.damageSources().onFire(), 1.0f);
                    }
                }
            }
        }
    }
    
    private void applyEvolution(int days) {
        double health = MutantConfig.DATA.mutant_health;
        double speed = MutantConfig.DATA.mutant_speed;
        
        if (MutantConfig.DATA.evolution_enabled) {
            if (days >= 100) {
                health = 150.0;
                speed = 0.45;
            } else if (days >= 60) {
                health = 120.0;
                speed = 0.42;
            } else if (days >= 30) {
                health = 100.0;
                speed = 0.40;
            } else if (days >= 10) {
                health = 90.0;
                speed = 0.38;
            }
        }
        
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed);
    }
    
    public void startPanic(int ticks) {
        this.entityData.set(IS_PANICKING, true);
        this.panicTimer = ticks;
    }
    
    public boolean isPanicking() {
        return this.entityData.get(IS_PANICKING);
    }
    
    public void startAttackJump() {
        this.attackJumpTimer = 10;
    }
    
    public void startEating(int duration) {
        this.eatTimer = duration;
    }
    
    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }
    
    public boolean isLying() {
        return this.entityData.get(LYING);
    }
    
    public int getDaysAlive() {
        return this.entityData.get(DAYS_ALIVE);
    }
    
    public void setDaySleeper(boolean b) {
        this.isDaySleeper = b;
    }
    
    public void wakeUp() {
        this.entityData.set(SLEEPING, false);
        this.setNoAi(false);
    }
    
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.entityData.get(LYING)) {
            this.setHealth(0);
            this.die(source);
            return true;
        }
        if (this.entityData.get(SLEEPING)) {
            this.wakeUp();
        }
        return super.hurt(source, amount);
    }
    
    @Override
    public void die(DamageSource source) {
        this.entityData.set(LYING, false);
        this.entityData.set(SLEEPING, false);
        super.die(source);
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.MUTANT_AMBIENT.get();
    }
    
    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.MUTANT_HURT.get();
    }
    
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.MUTANT_DEATH.get();
    }
    
    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(SoundEvents.ZOMBIE_STEP, 0.15f, 1.0f);
    }
}
