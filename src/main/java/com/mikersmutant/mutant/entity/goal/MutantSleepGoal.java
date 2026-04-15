package com.mikersmutant.mutant.entity.goal;

import com.mikersmutant.mutant.entity.MutantEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class MutantSleepGoal extends Goal {
    private final MutantEntity mutant;
    
    public MutantSleepGoal(MutantEntity mutant) {
        this.mutant = mutant;
    }
    
    @Override
    public boolean canUse() {
        return mutant.isSleeping();
    }
    
    @Override
    public void start() {
        mutant.setNoAi(true);
    }
    
    @Override
    public void stop() {
        mutant.setNoAi(false);
    }
}