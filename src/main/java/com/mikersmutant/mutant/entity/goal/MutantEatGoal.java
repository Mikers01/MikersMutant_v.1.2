package com.mikersmutant.mutant.entity.goal;

import com.mikersmutant.mutant.ModSounds;
import com.mikersmutant.mutant.entity.MutantEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;

import java.util.EnumSet;

public class MutantEatGoal extends Goal {
    private final MutantEntity mutant;
    private int eatTimer = 0;
    private ItemStack foodItem = ItemStack.EMPTY;
    
    public MutantEatGoal(MutantEntity mutant) {
        this.mutant = mutant;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }
    
    @Override
    public boolean canUse() {
        // Проверяем, есть ли рядом съедобный предмет
        var items = mutant.level.getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class,
                mutant.getBoundingBox().inflate(3.0));
        for (var item : items) {
            ItemStack stack = item.getItem();
            if (isEdible(stack)) {
                foodItem = stack;
                return true;
            }
        }
        return false;
    }
    
    private boolean isEdible(ItemStack stack) {
        return stack.is(Items.BEEF) || stack.is(Items.PORKCHOP) || stack.is(Items.MUTTON) ||
               stack.is(Items.RABBIT) || stack.is(Items.ROTTEN_FLESH) || stack.is(Items.BROWN_MUSHROOM) ||
               stack.is(Items.RED_MUSHROOM) || stack.is(Items.HONEYCOMB) || stack.is(Items.EGG);
    }
    
    @Override
    public void start() {
        eatTimer = 200; // 10 секунд
        mutant.startEating(eatTimer);
        mutant.setNoAi(true);
    }
    
    @Override
    public void tick() {
        eatTimer--;
        if (eatTimer <= 0) {
            mutant.heal(2.0f);
            mutant.playSound(ModSounds.MUTANT_EAT.get(), 1.0f, 1.0f);
            // удаляем предмет
            var items = mutant.level.getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class,
                    mutant.getBoundingBox().inflate(3.0));
            for (var item : items) {
                if (item.getItem().getItem() == foodItem.getItem()) {
                    item.discard();
                    break;
                }
            }
            // ускорение на 20 секунд
            mutant.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED).setBaseValue(0.45);
            mutant.getPersistentData().putLong("speedBoostEnd", mutant.level.getGameTime() + 400);
        }
    }
    
    @Override
    public boolean canContinueToUse() {
        return eatTimer > 0 && mutant.getTarget() == null;
    }
    
    @Override
    public void stop() {
        mutant.setNoAi(false);
        foodItem = ItemStack.EMPTY;
    }
}