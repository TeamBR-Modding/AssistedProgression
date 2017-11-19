package com.teambrmodding.assistedprogression.client;

import com.teambr.nucleus.helper.ItemRenderHelper;
import com.teambrmodding.assistedprogression.managers.BlockManager;
import com.teambrmodding.assistedprogression.managers.ItemManager;

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
public class ItemRenderManager {

    /**
     * Call in preInit, registers block models for in hand model
     */
    public static void registerBlockRenderers() {
        ItemRenderHelper.registerBlockModel(BlockManager.blockCrafter,         "normal");
        ItemRenderHelper.registerBlockModel(BlockManager.blockPlayerPlate,     "powered=false");
        ItemRenderHelper.registerBlockModel(BlockManager.blockRedstoneClock,   "powered=false");
        ItemRenderHelper.registerBlockModel(BlockManager.blockGrinder,         "normal");
        ItemRenderHelper.registerBlockModel(BlockManager.blockKineticGenerator,"normal");
    }

    /**
     * Call from init, registers item models
     */
    public static void registerItemRenderers() {
        ItemRenderHelper.registerItem(ItemManager.itemCheapMagnet);
        ItemRenderHelper.registerItem(ItemManager.itemElectroMagnet);
        ItemRenderHelper.registerItem(ItemManager.itemTrashBag);
        ItemRenderHelper.registerItem(ItemManager.itemHeftyBag);
        ItemRenderHelper.registerItem(ItemManager.itemSpawnerRelocator);
        ItemRenderHelper.registerItem(ItemManager.itemPipette);
        ItemRenderHelper.registerItem(ItemManager.itemExchanger);
    }
}
