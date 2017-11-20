package com.teambrmodding.assistedprogression.common.items;

import com.teambr.nucleus.client.gui.GuiColor;
import com.teambr.nucleus.client.gui.GuiTextFormat;
import com.teambr.nucleus.util.ClientUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

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
public class ItemSpawnerRelocator extends BaseItem {

    /**
     * Base constructor for all items
     *
     * @param itemName     The item registryName
     * @param maxStackSize Maximum stack size
     */
    public ItemSpawnerRelocator() {
        super("item_spawner_relocator", 1);
    }

    /*******************************************************************************************************************
     * Item                                                                                                            *
     *******************************************************************************************************************/

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 7200;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if(timeLeft <= 7180 && entityLiving instanceof EntityPlayer) {
            RayTraceResult movingObjectPosition = rayTrace(worldIn, (EntityPlayer) entityLiving, false);
            if(movingObjectPosition != null && movingObjectPosition.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos pos = movingObjectPosition.getBlockPos();
                if(!stack.hasTagCompound()) {
                    TileEntity tile = worldIn.getTileEntity(pos);
                    if(tile instanceof TileEntityMobSpawner) {
                        NBTTagCompound tag = new NBTTagCompound();
                        tile.writeToNBT(tag);
                        stack.setTagCompound(tag);
                        worldIn.setBlockToAir(pos);
                        worldIn.setBlockState(pos, worldIn.getBlockState(pos), 3);
                    }
                } else {
                    BlockPos newPosition = pos.offset(movingObjectPosition.sideHit);
                    if(!worldIn.isAirBlock(newPosition)) return;

                    NBTTagCompound tag = stack.getTagCompound();
                    tag.setInteger("x", newPosition.getX());
                    tag.setInteger("y", newPosition.getY());
                    tag.setInteger("z", newPosition.getZ());
                    worldIn.setBlockState(newPosition, Blocks.MOB_SPAWNER.getDefaultState());
                    TileEntityMobSpawner spawnerTile = (TileEntityMobSpawner) worldIn.getTileEntity(newPosition);
                    spawnerTile.readFromNBT(tag);
                    stack.setTagCompound(null);
                }
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        if(stack.hasTagCompound())
            tooltip.add("" + GuiColor.ORANGE + GuiTextFormat.ITALICS +
                    ClientUtils.translate("assistedprogression.text.spawnerRelocator.type") + " " +
                    stack.getTagCompound().getTagList("SpawnPotentials", 10).getStringTagAt(0).split("\"")[1]);

    }
}
