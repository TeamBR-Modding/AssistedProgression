package com.teambrmodding.assistedprogression.common.tiles;

import com.teambr.bookshelf.common.tiles.Syncable;
import com.teambrmodding.assistedprogression.common.blocks.BlockPlayerPlate;
import com.teambrmodding.assistedprogression.common.blocks.BlockRedstoneClock;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.nbt.NBTTagCompound;

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
public class TileRedstoneClock extends Syncable {

    /*******************************************************************************************************************
     * Variables                                                                                                       *
     *******************************************************************************************************************/

    // Syncable IDS
    public static final int DELATY_VAR_ID = 0;

    // Delay between pulses
    private int delay = 20;
    // Ticking variables
    private int ticker = 0;

    /*******************************************************************************************************************
     * TileRedstoneClock                                                                                               *
     *******************************************************************************************************************/

    @Override
    protected void onServerTick() {
        ticker++;
        boolean power = world.getBlockState(pos).getValue(BlockPressurePlate.POWERED);
        if(!isPowered() && ticker % delay == 0 || power)
            ((BlockRedstoneClock)world.getBlockState(pos).getBlock()).updateState(world, pos, world.getBlockState(pos), !power);
    }

    /*******************************************************************************************************************
     * TileEntity                                                                                                      *
     *******************************************************************************************************************/

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        delay = compound.getInteger("Delay");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("Delay", delay);
        return compound;
    }

    /*******************************************************************************************************************
     * Syncable                                                                                                        *
     *******************************************************************************************************************/

    /**
     * Used to set the value of a field
     *
     * @param id    The field id
     * @param value The value of the field
     */
    @Override
    public void setVariable(int id, double value) {
        switch (id) {
            case DELATY_VAR_ID :
                delay = (int) value;
                break;
            default :
        }
    }

    /**
     * Used to get the field on the server, this will fetch the server value and overwrite the current
     *
     * @param id The field id
     * @return The value on the server, now set to ourselves
     */
    @Override
    public Double getVariable(int id) {
        switch (id) {
            case DELATY_VAR_ID :
                return (double) delay;
            default :
                return 0.0D;
        }
    }

    /*******************************************************************************************************************
     * Helpers                                                                                                         *
     *******************************************************************************************************************/

    /**
     * Checks if this block is recieving redstone
     *
     * @return True if has power
     */
    public boolean isPowered() {
        return world.isBlockIndirectlyGettingPowered(pos) > 0 || world.isBlockPowered(pos);
    }
}
