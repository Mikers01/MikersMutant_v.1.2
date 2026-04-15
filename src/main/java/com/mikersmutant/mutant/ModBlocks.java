package com.mikersmutant.mutant;

import com.mikersmutant.mutant.block.FreshScratchesBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MikersMutantMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MikersMutantMod.MOD_ID);

    public static final RegistryObject<Block> FRESH_SCRATCHES = BLOCKS.register("fresh_scratches",
            () -> new FreshScratchesBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f).sound(SoundType.STONE).noOcclusion()));

    public static final RegistryObject<Item> FRESH_SCRATCHES_ITEM = ITEMS.register("fresh_scratches",
            () -> new BlockItem(FRESH_SCRATCHES.get(), new Item.Properties()));

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }
}
