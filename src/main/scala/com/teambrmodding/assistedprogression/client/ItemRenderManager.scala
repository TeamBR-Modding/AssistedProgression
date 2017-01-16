package com.teambrmodding.assistedprogression.client

import com.teambrmodding.assistedprogression.lib.Reference
import com.teambrmodding.assistedprogression.managers.BlockManager
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader

/**
  * This file was created for Assisted-Progression
  *
  * Assisted-Progression is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * The purpose of this class to to manage the item rendering for items/blocks
  *
  * @author Paul Davis - pauljoda
  * @since 1/16/2017
  */
object ItemRenderManager {

    def preInit(): Unit = {
        registerBlockModel(BlockManager.blockCrafter, "blockCrafter", "normal")
        registerBlockModel(BlockManager.playerPlate, "playerPlate", "powered=false")
    }


    /***
      * Registers an item to a model
      * @param item The item to map a model to
      */
    def registerItem(item: Item): Unit = {
        Minecraft.getMinecraft.getRenderItem.getItemModelMesher.register(item, 0,
            new ModelResourceLocation(item.getUnlocalizedName.substring(5), "inventory"))
        ModelLoader.setCustomModelResourceLocation(item, 0,
            new ModelResourceLocation(item.getUnlocalizedName.substring(5), "inventory"))
    }

    /***
      * Sets the item model for a block
      * @param block The block
      * @param name The name
      * @param variants Variants (set defaults here from block file)
      * @param meta Meta info
      */
    def registerBlockModel(block : Block, name : String, variants : String, meta : Int = 0) : Unit = {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block),
            meta, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, name), variants))
    }

    /***
      * Sets the item model for a item
      * @param item The item
      * @param name The name
      * @param variants Variants (set defaults here from model file)
      * @param meta Meta info
      */
    def registerItemModel(item : Item, name : String, variants : String, meta : Int = 0) : Unit = {
        ModelLoader.setCustomModelResourceLocation(item,
            meta, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, name), variants))
    }

}
