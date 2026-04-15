package com.mikersmutant.mutant.entity.goal;

import com.mikersmutant.mutant.entity.MutantEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MutantRememberPlaceGoal extends Goal {
    private final MutantEntity mutant;
    private BlockPos rememberedPos;
    private int searchTimer = 0;
    private static final Map<UUID, BlockPos> MEMORY = new HashMap<>();
    
    public MutantRememberPlaceGoal(MutantEntity mutant) {
        this.mutant = mutant;
    }
    
    @Override
    public boolean canUse() {
        if (!mutant.level().isNight()) return false;
        if (mutant.getTarget() != null) return false;
        
        UUID worldKey = mutant.level().dimension().location().toString() + "_" + mutant.getUUID();
        if (MEMORY.containsKey(worldKey)) {
            rememberedPos = MEMORY.get(worldKey);
            return true;
        }
        return false;
    }
    
    @Override
    public void start() {
        searchTimer = 1200;
    }
    
    @Override
    public void tick() {
        if (rememberedPos != null && searchTimer > 0) {
            double dist = mutant.distanceToSqr(rememberedPos.getX(), rememberedPos.getY(), rememberedPos.getZ());
            if (dist > 4) {
                Vec3 dir = new Vec3(rememberedPos.getX() - mutant.getX(), 0, rememberedPos.getZ() - mutant.getZ()).normalize();
                mutant.setDeltaMovement(dir.x * 0.5, 0, dir.z * 0.5);
            }
            searchTimer--;
        }
    }
    
    @Override
    public boolean canContinueToUse() {
        return searchTimer > 0 && rememberedPos != null && mutant.getTarget() == null;
    }
    
    public static void rememberPlace(UUID worldKey, BlockPos pos) {
        MEMORY.put(worldKey, pos);
    }
}
