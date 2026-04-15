package com.mikersmutant.mutant.entity;

import com.mikersmutant.mutant.MikersMutantMod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MutantModel extends AnimatedGeoModel<MutantEntity> {
    public ResourceLocation getModelResource(MutantEntity object) {
        return new ResourceLocation(MikersMutantMod.MOD_ID, "geo/mutant.geo.json");
    }

    public ResourceLocation getTextureResource(MutantEntity object) {
        return new ResourceLocation(MikersMutantMod.MOD_ID, "textures/entity/mutant.png");
    }

    public ResourceLocation getAnimationResource(MutantEntity animatable) {
        return new ResourceLocation(MikersMutantMod.MOD_ID, "animations/mutant.animation.json");
    }
}
