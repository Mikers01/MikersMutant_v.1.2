package com.mikersmutant.mutant.entity;

import com.mikersmutant.mutant.MikersMutantMod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MutantModel extends AnimatedGeoModel<MutantEntity> {
    @Override
    public ResourceLocation getModelResource(MutantEntity object) {
        return new ResourceLocation(MikersMutantMod.MOD_ID, "geo/mutant.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MutantEntity object) {
        return new ResourceLocation(MikersMutantMod.MOD_ID, "textures/entity/mutant.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MutantEntity animatable) {
        return new ResourceLocation(MikersMutantMod.MOD_ID, "animations/mutant.animation.json");
    }
}
