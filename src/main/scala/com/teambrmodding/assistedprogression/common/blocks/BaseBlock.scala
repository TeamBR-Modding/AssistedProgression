package com.teambrmodding.assistedprogression.common.blocks

import com.teambrmodding.assistedprogression.AssistedProgression
import com.teambrmodding.assistedprogression.lib.Reference
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

/**
  * This file was created for AssistedProgression
  *
  * AssistedProgression is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Dyonovan
  * @since 1/16/2017
  */
class BaseBlock(material: Material, name: String, tileEntity: Class[_ <: TileEntity]) extends BlockContainer(material){

    //Constructor
    setUnlocalizedName(Reference.MOD_ID + ":" + name)
    setCreativeTab(getCreativeTab)
    setHardness(getHardness)


    /**
      * Used to tell if this should be in a creative tab, and if so which one
      *
      * @return Null if none, defaults to the main NeoTech Tab
      */
    def getCreativeTab: CreativeTabs = AssistedProgression.tabAssistedProgression

    /**
      * Used to change the hardness of a block, but will default to 2.0F if not overwritten
      *
      * @return The hardness value, default 2.0F
      */
    def getHardness: Float = 2.0F

    override def createNewTileEntity(world: World, meta: Int): TileEntity = {
        if (tileEntity != null) tileEntity.newInstance() else null
    }
}
