package com.teambrmodding.assistedprogression.managers;

import com.teambrmodding.assistedprogression.common.blocks.BlockCrafter;
import com.teambrmodding.assistedprogression.common.blocks.BlockGrinder;
import com.teambrmodding.assistedprogression.common.blocks.BlockPlayerPlate;
import com.teambrmodding.assistedprogression.common.blocks.BlockRedstoneClock;
import com.teambrmodding.assistedprogression.common.blocks.storage.BlockFlushableChest;
import com.teambrmodding.assistedprogression.common.tiles.TileCrafter;
import com.teambrmodding.assistedprogression.common.tiles.TileGrinder;
import com.teambrmodding.assistedprogression.common.tiles.TileRedstoneClock;
import com.teambrmodding.assistedprogression.common.tiles.storage.TileFlushableChest;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 11/13/17
 */
public class BlockManager {

    /*******************************************************************************************************************
     * Block Variables                                                                                                 *
     *******************************************************************************************************************/

    public static BlockCrafter blockCrafter = new BlockCrafter();
    public static BlockFlushableChest blockFlushableChest = new BlockFlushableChest();
    public static BlockPlayerPlate blockPlayerPlate = new BlockPlayerPlate();
    public static BlockRedstoneClock blockRedstoneClock = new BlockRedstoneClock();
    public static BlockGrinder blockGrinder = new BlockGrinder();

    /*******************************************************************************************************************
     * Registration                                                                                                    *
     *******************************************************************************************************************/

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        registerBlock(event, blockCrafter, TileCrafter.class, null);
        registerBlock(event, blockFlushableChest, TileFlushableChest.class, "blockChest");
        registerBlock(event, blockPlayerPlate, null, "blockPressurePlate");
        registerBlock(event, blockRedstoneClock, TileRedstoneClock.class, null);
        registerBlock(event, blockGrinder, TileGrinder.class, null);
    }

    @SubscribeEvent
    public void registerItemBlocks(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(blockCrafter).setRegistryName(blockCrafter.getRegistryName()));
        event.getRegistry().register(new ItemBlock(blockFlushableChest).setRegistryName(blockFlushableChest.getRegistryName()));
        event.getRegistry().register(new ItemBlock(blockPlayerPlate).setRegistryName(blockPlayerPlate.getRegistryName()));
        event.getRegistry().register(new ItemBlock(blockRedstoneClock).setRegistryName(blockRedstoneClock.getRegistryName()));
        event.getRegistry().register(new ItemBlock(blockGrinder).setRegistryName(blockGrinder.getRegistryName()));
    }

    /*******************************************************************************************************************
     * Helper Methods                                                                                                  *
     *******************************************************************************************************************/

    /**
     * Helper Method to register a block
     * @param block The block to register
     * @param tileEntity The tile class
     * @param oreDict The ore dict tag
     * @return The block registered
     */
    public static <T extends Block> T registerBlock(RegistryEvent.Register<Block> event, T block,
                                                    @Nullable Class<? extends TileEntity> tileEntity,
                                                    @Nullable String oreDict) {

        event.getRegistry().register(block);

        if(tileEntity != null)
            GameRegistry.registerTileEntity(tileEntity, block.getRegistryName().toString());
        if(oreDict != null)
            OreDictionary.registerOre(oreDict, block);

        return block;
    }
}
