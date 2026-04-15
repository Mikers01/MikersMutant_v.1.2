package com.mikersmutant.mutant.entity.goal;

import com.mikersmutant.mutant.ModSounds;
import com.mikersmutant.mutant.entity.MutantEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class MutantPanicGoal extends Goal {
    private final MutantEntity mutant;
    
    public MutantPanicGoal(MutantEntity mutant) {
        this.mutant = mutant;
    }
    
    @Override
    public boolean canUse() {
        return mutant.isPanicking();
    }
    
    @Override
    public void start() {
        mutant.playSound(ModSounds.MUTANT_PANIC.get(), 1.0f, 1.0f);
        mutant.setNoAi(true);
    }
    
    @Override
    public void tick() {
        // Убегаем в случайном направлении
        double dx = mutant.getRandom().nextDouble() - 0.5;
        double dz = mutant.getRandom().nextDouble() - 0.5;
        mutant.setDeltaMovement(dx * 0.8, 0.2, dz * 0.8);
    }
    
    @Override
    public void stop() {
        mutant.setNoAi(false);
    }
    
    @Override
    public boolean canContinueToUse() {
        return mutant.isPanicking();
    }
}