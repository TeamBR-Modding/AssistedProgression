package com.teambrmodding.assistedprogression.common.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.teambr.nucleus.client.gui.GuiColor;
import com.teambr.nucleus.common.IAdvancedToolTipProvider;
import com.teambr.nucleus.util.ClientUtils;
import com.teambrmodding.assistedprogression.AssistedProgression;
import com.teambrmodding.assistedprogression.managers.ItemManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 8/25/2019
 */
public class PipetteItem extends Item {

    public PipetteItem() {
        super(new Properties()
                .maxStackSize(1)
                .group(ItemManager.itemGroupAssistedProgression));
        setRegistryName("pipette");
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new FluidHandlerItemStack(stack, FluidAttributes.BUCKET_VOLUME);
    }

    /*******************************************************************************************************************
     * Item                                                                                                            *
     *******************************************************************************************************************/

    /**
     * This is called when the item is used, before the block is activated.
     *
     * @return Return PASS to allow vanilla handling, any other to skip normal code.
     */
    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        if(!context.getWorld().isRemote) {
            if(stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).isPresent()
                    && context.getWorld().getTileEntity(context.getPos()) != null
                    && context.getWorld().getTileEntity(context.getPos())
                    .getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, context.getFace()).isPresent()) {
                IFluidHandler fluidTile = context.getWorld().getTileEntity(context.getPos())
                        .getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, context.getFace()).orElse(null);
                if(FluidUtil.interactWithFluidHandler(context.getPlayer(), context.getHand(), fluidTile)) {
                    context.getWorld().notifyBlockUpdate(context.getPos()
                            , context.getWorld().getBlockState(context.getPos()),
                            context.getWorld().getBlockState(context.getPos()), 3);
                    return  ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.PASS;
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent(
                GuiColor.ORANGE +
                        ClientUtils.translate("assistedprogression.text.pipette.fluidStored")));
        if(stack.hasTag()) {
            FluidStack currentStored = FluidUtil.getFluidContained(stack).orElse(null);
            if(currentStored == null)
                return;

            tooltip.add(new StringTextComponent("  " +
                    ClientUtils.translate(currentStored.getFluid().getDefaultState().getBlockState().getBlock().getTranslationKey()) //TODO: Go back to fluid name when forge fixes
                    + ": " + ClientUtils.formatNumber(currentStored.getAmount()) + " mb"));
        } else
            tooltip.add(new StringTextComponent("  "
                    + ChatFormatting.RED + ClientUtils.translate("assistedprogression.text.pipette.empty")));
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    public void fillItemGroup(ItemGroup itemIn, NonNullList<ItemStack> tab) {
        if(itemIn == ItemManager.itemGroupAssistedProgression) {
            tab.add(new ItemStack(this));

            // Add for all fluids
            ForgeRegistries.FLUIDS.getValues().stream()
                    .filter(fluid -> fluid.isSource(fluid.getDefaultState()))
                    .forEach(fluid -> {
                        ItemStack pipetteStack = new ItemStack(this);
                        FluidHandlerItemStack fluidStack = new FluidHandlerItemStack(pipetteStack, FluidAttributes.BUCKET_VOLUME);
                        if (fluidStack.fill(new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE)
                                == FluidAttributes.BUCKET_VOLUME)
                            tab.add(pipetteStack);
                    });
        }
    }
}
