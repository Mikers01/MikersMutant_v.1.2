package com.mikersmutant.mutant.entity.goal;

import com.mikersmutant.mutant.ModSounds;
import com.mikersmutant.mutant.entity.MutantEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;

public class MutantScrapeBarkGoal extends Goal {
    private final MutantEntity mutant;
    private BlockPos treePos;
    private int scrapeTimer = 0;
    
    public MutantScrapeBarkGoal(MutantEntity mutant) {
        this.mutant = mutant;
    }
    
    @Override
    public boolean canUse() {
        if (mutant.getHealth() > mutant.getMaxHealth() / 2) return false;
        
        for (int x = -3; x <= 3; x++) {
            for (int y = -2; y <= 4; y++) {
                for (int z = -3; z <= 3; z++) {
                    BlockPos pos = mutant.blockPosition().offset(x, y, z);
                    Block block = mutant.level.getBlockState(pos).getBlock();
                    if (block == Blocks.OAK_LOG || block == Blocks.BIRCH_LOG || block == Blocks.SPRUCE_LOG ||
                        block == Blocks.JUNGLE_LOG || block == Blocks.ACACIA_LOG || block == Blocks.DARK_OAK_LOG) {
                        treePos = pos;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public void start() {
        scrapeTimer = 60; // 3 секунды
        mutant.setNoAi(true);
    }
    
    @Override
    public void tick() {
        if (scrapeTimer-- <= 0 && treePos != null) {
            Block block = mutant.level.getBlockState(treePos).getBlock();
            Block strippedBlock = getStripped(block);
            if (strippedBlock != null) {
                mutant.level.setBlock(treePos, strippedBlock.defaultBlockState()
                        .setValue(RotatedPillarBlock.AXIS, mutant.level.getBlockState(treePos).getValue(RotatedPillarBlock.AXIS)), 3);
                mutant.playSound(ModSounds.MUTANT_SCRAPE.get(), 1.0f, 1.0f);
                mutant.heal(2.0f);
            }
            mutant.setNoAi(false);
        }
    }
    
    private Block getStripped(Block log) {
        if (log == Blocks.OAK_LOG) return Blocks.STRIPPED_OAK_LOG;
        if (log == Blocks.BIRCH_LOG) return Blocks.STRIPPED_BIRCH_LOG;
        if (log == Blocks.SPRUCE_LOG) return Blocks.STRIPPED_SPRUCE_LOG;
        if (log == Blocks.JUNGLE_LOG) return Blocks.STRIPPED_JUNGLE_LOG;
        if (log == Blocks.ACACIA_LOG) return Blocks.STRIPPED_ACACIA_LOG;
        if (log == Blocks.DARK_OAK_LOG) return Blocks.STRIPPED_DARK_OAK_LOG;
        return null;
    }
    
    @Override
    public boolean canContinueToUse() {
        return scrapeTimer > 0;
    }
}