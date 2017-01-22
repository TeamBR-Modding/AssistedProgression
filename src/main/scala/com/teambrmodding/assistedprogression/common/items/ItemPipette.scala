package com.teambrmodding.assistedprogression.common.items

import java.util

import com.teambr.bookshelf.client.gui.GuiColor
import com.teambrmodding.assistedprogression.utils.ClientUtils
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.util.{EnumActionResult, EnumFacing, EnumHand}
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.fluids.{Fluid, FluidRegistry, FluidStack, FluidUtil}
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.JavaConversions._

/**
  * This file was created for Assisted-Progression
  *
  * Assisted-Progression is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Paul Davis - pauljoda
  * @since 1/22/2017
  */
class ItemPipette extends BaseItem("itemPipette", 1) {

    override def initCapabilities(stack: ItemStack, nbt: NBTTagCompound): ICapabilityProvider = {
        new FluidHandlerItemStack(stack, 1000)
    }

    override def onItemUseFirst(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos,
                                side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, hand: EnumHand): EnumActionResult = {
        if(!world.isRemote) {
            if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP)
                    && world.getTileEntity(pos) != null
                    && world.getTileEntity(pos).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)) {
                val fluidHandlerTileEntity = world.getTileEntity(pos).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
                if (FluidUtil.interactWithFluidHandler(stack, fluidHandlerTileEntity, player)) {
                    world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3)
                    return EnumActionResult.SUCCESS
                }
            }
        }
        EnumActionResult.PASS
    }

    override def addInformation(stack: ItemStack, playerIn: EntityPlayer, tooltip: util.List[String], advanced: Boolean): Unit = {
        if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP)) {
            val fluidHandlerItemStack = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP)
            val fluidInfo = fluidHandlerItemStack.getTankProperties()(0)
            if(fluidInfo != null) {
                if(fluidInfo.getContents != null) {
                    tooltip.add(GuiColor.ORANGE + "" + ClientUtils.translate("assistedprogression.text.pipette.fluid"))
                    tooltip.add(fluidInfo.getContents.getLocalizedName)
                    tooltip.add(ClientUtils.formatNumber(fluidInfo.getContents.amount) + "mb" + " / 1,000mb")
                } else {
                    tooltip.add(ClientUtils.translate("assistedprogression.text.pipette.empty"))
                }
            }
        }
    }

    // Add to creative
    @SideOnly(Side.CLIENT)
    override def getSubItems(item: Item, tab: CreativeTabs, subItems: util.List[ItemStack]): Unit = {
        subItems.add(new ItemStack(item))

        for(fluid <- FluidRegistry.getRegisteredFluids.values()) {
            val itemStack = new ItemStack(item)
            val fluidHandlerItemStack = new FluidHandlerItemStack(itemStack, 1000)
            if(fluidHandlerItemStack.fill(new FluidStack(fluid, Fluid.BUCKET_VOLUME), true) == Fluid.BUCKET_VOLUME) {
                subItems.add(itemStack)
            }
        }
    }
}
