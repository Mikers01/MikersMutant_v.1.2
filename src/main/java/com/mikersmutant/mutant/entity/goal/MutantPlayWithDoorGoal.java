package com.mikersmutant.mutant.entity.goal;

import com.mikersmutant.mutant.ModSounds;
import com.mikersmutant.mutant.entity.MutantEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MutantPlayWithDoorGoal extends Goal {
    private final MutantEntity mutant;
    private BlockPos doorPos;
    private int playTimer = 0;
    private int actionTimer = 0;
    
    public MutantPlayWithDoorGoal(MutantEntity mutant) {
        this.mutant = mutant;
    }
    
    @Override
    public boolean canUse() {
        if (mutant.getTarget() != null) return false;
        
        for (int x = -5; x <= 5; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -5; z <= 5; z++) {
                    BlockPos pos = mutant.blockPosition().offset(x, y, z);
                    if (mutant.level().getBlockState(pos).getBlock() instanceof DoorBlock) {
                        doorPos = pos;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public void start() {
        playTimer = 200;
        actionTimer = 0;
        mutant.setNoAi(true);
    }
    
    @Override
    public void tick() {
        if (doorPos == null) return;
        
        if (actionTimer <= 0) {
            BlockState state = mutant.level().getBlockState(doorPos);
            if (state.getBlock() instanceof DoorBlock) {
                boolean open = !state.getValue(DoorBlock.OPEN);
                mutant.level().setBlock(doorPos, state.setValue(DoorBlock.OPEN, open), 2);
                mutant.playSound(ModSounds.MUTANT_DOOR_PLAY.get(), 0.5f, 1.0f);
                actionTimer = 20;
            }
        } else {
            actionTimer--;
        }
        
        playTimer--;
    }
    
    @Override
    public boolean canContinueToUse() {
        return playTimer > 0 && doorPos != null && mutant.getTarget() == null;
    }
    
    @Override
    public void stop() {
        mutant.setNoAi(false);
        doorPos = null;
    }
}
