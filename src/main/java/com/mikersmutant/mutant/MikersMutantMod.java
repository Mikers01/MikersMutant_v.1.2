package com.mikersmutant.mutant;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(MikersMutantMod.MOD_ID)
public class MikersMutantMod {
    public static final String MOD_ID = "mikersmutant";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MikersMutantMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);
        ModBlocks.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
