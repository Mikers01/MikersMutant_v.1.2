package com.mikersmutant.mutant.entity.goal;

import com.mikersmutant.mutant.entity.MutantEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Creeper;

public class MutantCreeperProvokeGoal extends Goal {
    private final MutantEntity mutant;
    private Creeper targetCreeper;
    private int provokeTimer = 0;
    private int cooldown = 0;
    
    public MutantCreeperProvokeGoal(MutantEntity mutant) {
        this.mutant = mutant;
    }
    
    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        
        for (Entity entity : mutant.level().getEntitiesOfClass(Creeper.class, mutant.getBoundingBox().inflate(10))) {
            targetCreeper = (Creeper) entity;
            return true;
        }
        return false;
    }
    
    @Override
    public void start() {
        provokeTimer = 30;
        if (targetCreeper != null) {
            targetCreeper.ignite();
        }
    }
    
    @Override
    public void tick() {
        provokeTimer--;
        if (provokeTimer <= 0 && targetCreeper != null) {
            double dx = mutant.getX() - targetCreeper.getX();
            double dz = mutant.getZ() - targetCreeper.getZ();
            double dist = Math.sqrt(dx * dx + dz * dz);
            if (dist < 5.0) {
                double vx = dx / dist * 5.0;
                double vz = dz / dist * 5.0;
                mutant.setDeltaMovement(vx, 0.5, vz);
            }
            cooldown = 600;
            targetCreeper = null;
        }
    }
    
    @Override
    public boolean canContinueToUse() {
        return provokeTimer > 0 && targetCreeper != null;
    }
}
