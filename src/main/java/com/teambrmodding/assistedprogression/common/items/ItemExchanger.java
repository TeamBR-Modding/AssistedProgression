package com.teambrmodding.assistedprogression.common.items;

import com.teambr.bookshelf.util.BlockUtils;
import com.teambr.bookshelf.util.WorldUtils;
import com.teambrmodding.assistedprogression.AssistedProgression;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * This file was created for Assisted Progression
 * <p>
 * Assisted Progression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan - Paul Davis
 * @since 10/8/2016 - 01/18/2017
 */
public class ItemExchanger extends Item {

    private static final String SIZE_NBT_TAG = "size";
    private static final String EXCHANGE_NBT_TAG = "exchanging";

    public ItemExchanger() {
        super();
        this.setUnlocalizedName(Reference.MOD_ID + ":item_exchanger");
        setRegistryName(Reference.MOD_ID, "item_exchanger");
        this.setCreativeTab(AssistedProgression.tabAssistedProgression);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ItemStack stack = player.getHeldItem(hand);
            if (player.isSneaking()) {
                if (world.getTileEntity(pos) == null) {
                    setExchangeBlock(stack, new ItemStack(world.getBlockState(pos).getBlock(), 1, world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos))));
                    String blockAdded = I18n.translateToLocal("assistedprogression.text.exchanger.blockSet") + " " + getExchangingStack(stack).getDisplayName();
                    player.sendMessage(new TextComponentString(blockAdded));
                }
            } else if (getExchangingStack(stack) != null) { // If we have a stack defined
                // Create a block list for what we are looking for
                List<BlockPos> posList = BlockUtils.getBlockList(getSize(stack), facing, pos, world);
                // Create a master stack based off what is in world
                ItemStack compareStack = new ItemStack(world.getBlockState(pos).getBlock(), 1, world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)));
                posList.stream().filter(blockPos -> world.getTileEntity(blockPos) == null).forEach(blockPos -> {
                    // If this stack matches master, swap
                    ItemStack changeStack = new ItemStack(world.getBlockState(blockPos).getBlock(), 1, world.getBlockState(blockPos).getBlock().getMetaFromState(world.getBlockState(blockPos)));
                    if (compareStack.isItemEqual(changeStack)) {
                        // Make sure it is a valid block to swap
                        if ((world.isAirBlock(blockPos.offset(facing)) || pos.equals(blockPos)) && world.getBlockState(blockPos).getBlockHardness(world, blockPos) > 0)
                            // Allow free in creative, must have stack to place if not
                            if (player.capabilities.isCreativeMode || (player.inventory.clearMatchingItems(getExchangingStack(stack).getItem(), getExchangingStack(stack).getItemDamage(), 1, null) == 1)) {
                                // Grab the world block
                                Block worldBlock = world.getBlockState(blockPos).getBlock();
                                // Get what this drops
                                List<ItemStack> drops = worldBlock.getDrops(world, blockPos, world.getBlockState(blockPos), 0);
                                // Spawn them at the player, if not in creative
                                if(!player.capabilities.isCreativeMode)
                                    drops.forEach(dropStack ->  WorldUtils.dropStack(world, dropStack, player.getPosition()));
                                // Set the block to what we want
                                world.setBlockState(blockPos, Block.getBlockFromItem(getExchangingStack(stack).getItem()).getStateFromMeta(getExchangingStack(stack).getItemDamage()));
                            }
                    }
                });
            }
        }

        if (world.isRemote)
            world.playSound(player, pos, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.HOSTILE, 0.5F, 1.0F);

        return EnumActionResult.SUCCESS;
    }

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote && player.isSneaking()) {
            if (getSize(stack) == 4)
                setSize(stack, 1);
            else
                setSize(stack, getSize(stack) + 1);

            String newSize = I18n.translateToLocal("assistedprogression.text.exchanger.sizeSet") + " " + getSizeString(stack);
            player.sendMessage(new TextComponentString(newSize));
        }
        return new ActionResult(EnumActionResult.PASS, stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        if (getExchangingStack(stack) != null)
            tooltip.add("Set Block: " + getExchangingStack(stack).getDisplayName());
        else
            tooltip.add("Set Block: No Block Set!");
    }

    /*******************************************************************************************************************
     * Helper Methods                                                                                                  *
     *******************************************************************************************************************/

    private void validateNBT(ItemStack stack) {
        // If we don't have a tag
        if(!stack.hasTagCompound() || (!stack.getTagCompound().hasKey(SIZE_NBT_TAG) || !stack.getTagCompound().hasKey(EXCHANGE_NBT_TAG))) {
            NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();

            // Set initial Size
            tag.setInteger(SIZE_NBT_TAG, 1);

            // Set block to air
            tag.setTag(EXCHANGE_NBT_TAG, new ItemStack(Blocks.AIR).writeToNBT(new NBTTagCompound()));

            // Write to the stack
            stack.setTagCompound(tag);
        }
    }


    /*******************************************************************************************************************
     * Accessors and Mutators                                                                                          *
     *******************************************************************************************************************/

    /**
     * Gets the current size the Exchanger is set to
     *
     * @return  current set size
     */
    public int getSize(ItemStack stack) {
        validateNBT(stack);
        return stack.getTagCompound().getInteger(SIZE_NBT_TAG);
    }

    /***
     * Used to set the size of the exchange radius
     * @param stack Stack to modify
     * @param newSize New size to set
     */
    public void setSize(ItemStack stack, int newSize) {
        validateNBT(stack);
        stack.getTagCompound().setInteger(SIZE_NBT_TAG, newSize);
    }

    /**
     * Returns the stack that we are using to store the swapped block
     * @param stack The stack to read
     * @return The stack swapping
     */
    public ItemStack getExchangingStack(ItemStack stack) {
        validateNBT(stack);
        ItemStack returnStack = new ItemStack(stack.getTagCompound().getCompoundTag(EXCHANGE_NBT_TAG));
        return returnStack != null && returnStack.getItem() != null && Block.getBlockFromItem(returnStack.getItem()) != Blocks.AIR
                ? returnStack : null;
    }

    /**
     * Used to set the block we are exchanging
     * @param stack The in stack
     * @param toWrite The stack to store
     */
    public void setExchangeBlock(ItemStack stack, ItemStack toWrite) {
        validateNBT(stack);
        stack.getTagCompound().setTag(EXCHANGE_NBT_TAG, toWrite.writeToNBT(new NBTTagCompound()));
    }

    /**
     * Returns a string value for size
     *
     * @param stack     The {@link net.minecraft.item.ItemStack} to get the size from
     * @return          String of the size
     */
    private String getSizeString(ItemStack stack) {
        switch (getSize(stack)) {
            case 1:
                return "3x3";
            case 2:
                return "5x5";
            case 3:
                return "7x7";
            case 4:
                return "9x9";
            default:
                return "Opps. Something went wrong...";
        }
    }
}
