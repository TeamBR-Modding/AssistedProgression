package com.teambrmodding.assistedprogression.common.blocks

import com.teambr.bookshelf.common.blocks.traits.DropsItems
import com.teambr.bookshelf.common.tiles.traits.OpensGui
import com.teambrmodding.assistedprogression.AssistedProgression
import com.teambrmodding.assistedprogression.client.gui.GuiCrafter
import com.teambrmodding.assistedprogression.common.container.ContainerCrafter
import com.teambrmodding.assistedprogression.common.tiles.TileCrafter
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 * This file was created for NeoTech
 *
 * NeoTech is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 18, 2015
 */
class BlockCrafter(name: String, tileEntity: Class[_ <: TileEntity]) extends BaseBlock(Material.WOOD, name, tileEntity)
        with OpensGui with DropsItems {

    setHardness(1.5F)
    setCreativeTab(AssistedProgression.tabAssistedProgression)

    override def getRenderType(state : IBlockState) : EnumBlockRenderType = EnumBlockRenderType.MODEL

    override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
        new ContainerCrafter(player.inventory, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileCrafter])
    }

    @SideOnly(Side.CLIENT)
    override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
        new GuiCrafter(player.inventory, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileCrafter])
    }
}
