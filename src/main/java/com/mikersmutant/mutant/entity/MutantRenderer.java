package com.mikersmutant.mutant.entity;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import com.mikersmutant.mutant.MikersMutantMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MutantRenderer extends GeoEntityRenderer<MutantEntity> {
    public MutantRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MutantModel());
        this.shadowRadius = 0.9f;
    }

    @Override
    public ResourceLocation getTextureLocation(MutantEntity instance) {
        return new ResourceLocation(MikersMutantMod.MOD_ID, "textures/entity/mutant.png");
    }
}
