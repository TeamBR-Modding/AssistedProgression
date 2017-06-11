package com.teambrmodding.assistedprogression.common.tiles;

import com.teambr.bookshelf.common.tiles.Syncable;
import com.teambrmodding.assistedprogression.common.blocks.BlockRedstoneClock;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since 6/2/2017
 */
public class TileRedstoneClock extends Syncable {

    private int delay = 20;
    int ticker = 0;

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        delay = tag.getInteger("delay");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("delay", delay);
        return tag;
    }

    @Override
    public void setVariable(int id, double value) {
        delay = (int) value;
    }

    @Override
    public Double getVariable(int id) {
        return (double) delay;
    }

    @Override
    public void onServerTick() {
        ticker += 1;
        boolean power = getWorld().getBlockState(pos).getValue(BlockPressurePlate.POWERED);
        if (!isPowered() && ticker % delay == 0 || power)
            ((BlockRedstoneClock) getWorld().getBlockState(pos).getBlock()).updateState(getWorld(), pos, getWorld().getBlockState(pos), !power);
    }

    private boolean isPowered() {
        return isPoweringTo(getWorld(),  getPos().offset(EnumFacing.UP),    EnumFacing.DOWN)  ||
                isPoweringTo(getWorld(), getPos().offset(EnumFacing.DOWN),  EnumFacing.UP)    ||
                isPoweringTo(getWorld(), getPos().offset(EnumFacing.NORTH), EnumFacing.SOUTH) ||
                isPoweringTo(getWorld(), getPos().offset(EnumFacing.SOUTH), EnumFacing.NORTH) ||
                isPoweringTo(getWorld(), getPos().offset(EnumFacing.EAST),  EnumFacing.WEST)  ||
                isPoweringTo(getWorld(), getPos().offset(EnumFacing.WEST),  EnumFacing.EAST);
    }

    private boolean isPoweringTo(World world, BlockPos pos, EnumFacing side) {
        return world.getBlockState(pos).getBlock().getWeakPower(world.getBlockState(pos), world, pos, side) > 0;
    }
}
