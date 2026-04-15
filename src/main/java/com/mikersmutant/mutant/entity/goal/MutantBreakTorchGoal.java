package com.mikersmutant.mutant.entity.goal;

import com.mikersmutant.mutant.entity.MutantEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;

public class MutantBreakTorchGoal extends Goal {
    private final MutantEntity mutant;
    private BlockPos torchPos;
    
    public MutantBreakTorchGoal(MutantEntity mutant) {
        this.mutant = mutant;
    }
    
    @Override
    public boolean canUse() {
        for (int x = -4; x <= 4; x++) {
            for (int y = -2; y <= 3; y++) {
                for (int z = -4; z <= 4; z++) {
                    BlockPos pos = mutant.blockPosition().offset(x, y, z);
                    if (mutant.level().getBlockState(pos).is(Blocks.TORCH) ||
                        mutant.level().getBlockState(pos).is(Blocks.WALL_TORCH)) {
                        torchPos = pos;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public void start() {
        if (torchPos != null) {
            mutant.level().destroyBlock(torchPos, false);
            torchPos = null;
        }
    }
}
