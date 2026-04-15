package com.mikersmutant.mutant.entity.goal;

import com.mikersmutant.mutant.entity.MutantEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;

public class MutantBreakHiveGoal extends Goal {
    private final MutantEntity mutant;
    private BlockPos hivePos;
    private int breakTimer = 0;
    
    public MutantBreakHiveGoal(MutantEntity mutant) {
        this.mutant = mutant;
    }
    
    @Override
    public boolean canUse() {
        for (int x = -8; x <= 8; x++) {
            for (int y = -4; y <= 4; y++) {
                for (int z = -8; z <= 8; z++) {
                    BlockPos pos = mutant.blockPosition().offset(x, y, z);
                    if (mutant.level().getBlockState(pos).is(Blocks.BEEHIVE) || 
                        mutant.level().getBlockState(pos).is(Blocks.BEE_NEST)) {
                        hivePos = pos;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public void start() {
        breakTimer = 20;
    }
    
    @Override
    public void tick() {
        if (hivePos != null && breakTimer-- <= 0) {
            mutant.level().destroyBlock(hivePos, true);
            hivePos = null;
        }
    }
    
    @Override
    public boolean canContinueToUse() {
        return hivePos != null && breakTimer > 0;
    }
}
