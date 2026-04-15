package com.mikersmutant.mutant.entity.goal;

import com.mikersmutant.mutant.entity.MutantEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MutantTunnelGoal extends Goal {
    private final MutantEntity mutant;
    private BlockPos targetPos;
    private int digTimer = 0;
    
    public MutantTunnelGoal(MutantEntity mutant) {
        this.mutant = mutant;
    }
    
    @Override
    public boolean canUse() {
        return mutant.getTarget() == null && mutant.level().isDay() && !mutant.isSleeping();
    }
    
    @Override
    public void start() {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = -64; x <= 64; x++) {
            for (int y = -30; y <= 30; y++) {
                for (int z = -64; z <= 64; z++) {
                    pos.set(mutant.getX() + x, mutant.getY() + y, mutant.getZ() + z);
                    if (mutant.level().getBlockState(pos).is(Blocks.CHEST)) {
                        targetPos = pos.immutable();
                        return;
                    }
                }
            }
        }
    }
    
    @Override
    public void tick() {
        if (targetPos == null) return;
        
        digTimer--;
        if (digTimer <= 0) {
            BlockPos currentPos = mutant.blockPosition();
            int dx = Integer.compare(targetPos.getX(), currentPos.getX());
            int dz = Integer.compare(targetPos.getZ(), currentPos.getZ());
            BlockPos digPos = currentPos.offset(dx, 0, dz);
            
            BlockState state = mutant.level().getBlockState(digPos);
            if (!state.isAir()) {
                mutant.level().destroyBlock(digPos, true);
                digTimer = 10;
            }
        }
    }
    
    @Override
    public boolean canContinueToUse() {
        return targetPos != null && mutant.getTarget() == null && !mutant.isSleeping();
    }
}
