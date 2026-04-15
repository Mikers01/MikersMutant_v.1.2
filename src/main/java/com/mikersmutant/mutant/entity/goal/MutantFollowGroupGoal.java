package com.mikersmutant.mutant.entity.goal;

import com.mikersmutant.mutant.entity.MutantEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MutantFollowGroupGoal extends Goal {
    private final MutantEntity mutant;
    private MutantEntity leader;
    private int returnTimer = 0;
    
    public MutantFollowGroupGoal(MutantEntity mutant) {
        this.mutant = mutant;
    }
    
    @Override
    public boolean canUse() {
        // Днём спят вместе, ночью разбегаются
        if (mutant.level.isDay()) return false;
        if (mutant.getTarget() != null) return false;
        
        List<MutantEntity> group = mutant.level.getEntitiesOfClass(MutantEntity.class, mutant.getBoundingBox().inflate(30));
        if (group.size() <= 1) return false;
        
        // Ищем ближайшего сородича
        double closestDist = 100;
        for (MutantEntity m : group) {
            if (m != mutant) {
                double dist = mutant.distanceToSqr(m);
                if (dist < closestDist && dist > 25) {
                    closestDist = dist;
                    leader = m;
                }
            }
        }
        return leader != null;
    }
    
    @Override
    public void start() {
        returnTimer = 1200; // 1 минута
    }
    
    @Override
    public void tick() {
        if (leader != null && mutant.distanceToSqr(leader) > 25) {
            Vec3 dir = leader.position().subtract(mutant.position()).normalize();
            mutant.setDeltaMovement(dir.x * 0.5, 0, dir.z * 0.5);
        }
        returnTimer--;
    }
    
    @Override
    public boolean canContinueToUse() {
        return returnTimer > 0 && mutant.getTarget() == null && leader != null && !leader.isRemoved();
    }
}