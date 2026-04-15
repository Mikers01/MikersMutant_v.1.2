package com.mikersmutant.mutant;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MikersMutantMod.MOD_ID);

    public static final RegistryObject<SoundEvent> MUTANT_AMBIENT = register("mutant_ambient");
    public static final RegistryObject<SoundEvent> MUTANT_HURT = register("mutant_hurt");
    public static final RegistryObject<SoundEvent> MUTANT_DEATH = register("mutant_death");
    public static final RegistryObject<SoundEvent> MUTANT_EAT = register("mutant_eat");
    public static final RegistryObject<SoundEvent> MUTANT_ROAR = register("mutant_roar");
    public static final RegistryObject<SoundEvent> MUTANT_PANIC = register("mutant_panic");
    public static final RegistryObject<SoundEvent> MUTANT_BREAK = register("mutant_break");
    public static final RegistryObject<SoundEvent> MUTANT_CLIMB = register("mutant_climb");
    public static final RegistryObject<SoundEvent> MUTANT_SNIFF = register("mutant_sniff");
    public static final RegistryObject<SoundEvent> MUTANT_DOOR_KNOCK = register("mutant_door_knock");
    public static final RegistryObject<SoundEvent> MUTANT_DOOR_PLAY = register("mutant_door_play");
    public static final RegistryObject<SoundEvent> MUTANT_SCRAPE = register("mutant_scrape");
    public static final RegistryObject<SoundEvent> MUTANT_COMMUNICATE = register("mutant_communicate");

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MikersMutantMod.MOD_ID, name)));
    }

    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }
}