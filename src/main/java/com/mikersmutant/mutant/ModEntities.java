package com.mikersmutant.mutant;

import com.mikersmutant.mutant.entity.MutantEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MikersMutantMod.MOD_ID);

    public static final RegistryObject<EntityType<MutantEntity>> MUTANT =
            ENTITIES.register("mutant",
                    () -> EntityType.Builder.of(MutantEntity::new, MobCategory.MONSTER)
                            .sized(0.9f, 3.0f)
                            .build(new ResourceLocation(MikersMutantMod.MOD_ID, "mutant").toString()));

    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
    }
}