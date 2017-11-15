package com.teambrmodding.assistedprogression.common.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.teambr.bookshelf.client.gui.GuiColor;
import com.teambr.bookshelf.util.ClientUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nullable;
import java.util.List;

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
public class ItemPipette extends BaseItem {

    /**
     * Base constructor for all items
     *
     * @param itemName     The item name
     * @param maxStackSize Maximum stack size
     */
    public ItemPipette() {
        super("item_pipette", 1);
    }

    /**
     * Called from ItemStack.setItem, will hold extra data for the life of this ItemStack.
     * Can be retrieved from stack.getCapabilities()
     * The NBT can be null if this is not called from readNBT or if the item the stack is
     * changing FROM is different then this item, or the previous item had no capabilities.
     *
     * This is called BEFORE the stacks item is set so you can use stack.getItem() to see the OLD item.
     * Remember that getItem CAN return null.
     *
     * @param stack The ItemStack
     * @param nbt NBT of this item serialized, or null.
     * @return A holder instance associated with this ItemStack where you can hold capabilities for the life of this item.
     */
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new FluidHandlerItemStack(stack, Fluid.BUCKET_VOLUME);
    }

    /*******************************************************************************************************************
     * Item                                                                                                            *
     *******************************************************************************************************************/

    /**
     * This is called when the item is used, before the block is activated.
     * @param stack The Item Stack
     * @param player The Player that used the item
     * @param world The Current World
     * @param pos Target position
     * @param side The side of the target hit
     * @param hand Which hand the item is being held in.
     * @return Return PASS to allow vanilla handling, any other to skip normal code.
     */
    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos,
                                           EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if(!world.isRemote) {
            ItemStack stack = player.getHeldItem(hand);
            if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)
                    && world.getTileEntity(pos) != null
                    && world.getTileEntity(pos).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)) {
                IFluidHandler fluidTile = world.getTileEntity(pos).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
                if(FluidUtil.interactWithFluidHandler(player, hand, fluidTile)) {
                    world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                    return  EnumActionResult.SUCCESS;
                }
            }
        }
        return EnumActionResult.PASS;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(GuiColor.ORANGE + ClientUtils.translate("assistedprogression.text.pipette.fluidStored"));
        if(stack.hasTagCompound()) {
            FluidStack currentStored = FluidUtil.getFluidContained(stack);
            if(currentStored == null)
                return;

            tooltip.add("  " + currentStored.getLocalizedName() + ": " + ClientUtils.formatNumber(currentStored.amount) + " mb");
        } else
            tooltip.add("  " + ChatFormatting.RED + ClientUtils.translate("assistedprogression.text.pipette.empty"));
    }

    @Override
    public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
        tab.add(new ItemStack(this));

        // Add for all fluids
        for(Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
            ItemStack pipetteStack = new ItemStack(this);
            FluidHandlerItemStack fluidStack = new FluidHandlerItemStack(pipetteStack, Fluid.BUCKET_VOLUME);
            if(fluidStack.fill(new FluidStack(fluid, Fluid.BUCKET_VOLUME), true) == Fluid.BUCKET_VOLUME)
                tab.add(pipetteStack);
        }
    }
}
