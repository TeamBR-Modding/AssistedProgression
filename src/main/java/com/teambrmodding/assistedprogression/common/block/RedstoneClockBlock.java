package com.teambrmodding.assistedprogression.common.block;

import com.teambrmodding.assistedprogression.common.tile.RedstoneClockTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.material.Material;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 8/29/2019
 */
@SuppressWarnings("ALL")
public class RedstoneClockBlock extends BaseBlock {

    /**
     * Constructor
     */
    public RedstoneClockBlock() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(2.0F).harvestTool(ToolType.PICKAXE),
                "redstone_clock", RedstoneClockTile.class);
        setDefaultState(getStateContainer().getBaseState().with(PressurePlateBlock.POWERED, false));
    }

    /*******************************************************************************************************************
     * BlockRedstoneClock                                                                                              *
     *******************************************************************************************************************/

    /**
     * Triggers a redstone signal change
     * @param worldIn The world
     * @param pos The position
     * @param state The state
     * @param max old power
     */
    public void updateState(World worldIn, BlockPos pos, BlockState state, boolean max) {
        int redstonePower = max ? 15 : 0;
        worldIn.setBlockState(pos, setRedstoneStrength(state, redstonePower), 3);
        updateNeighbors(worldIn, pos);
    }

    /**
     * Set state with redstone power
     * @param state The blockstate
     * @param strength The power
     * @return Blockstate with power applied
     */
    protected BlockState setRedstoneStrength(BlockState state, int strength) {
        return state.with(PressurePlateBlock.POWERED, strength > 0);
    }

    /**
     * Used to tell if state is powered
     * @param state The state
     * @return Power
     */
    protected int getRedstoneStrength(BlockState state) {
        return state.get(PressurePlateBlock.POWERED) ? 15 : 0;
    }

    /**
     * Sends updates to itself and down for all directions
     * @param world The world
     * @param pos The clock pos
     */
    protected void updateNeighbors(World world, BlockPos pos) {
        world.notifyNeighborsOfStateChange(pos, this);
        for(Direction dir : Direction.values())
            world.notifyNeighborsOfStateChange(pos.offset(dir), this);
    }

    /*******************************************************************************************************************
     * Block                                                                                                           *
     *******************************************************************************************************************/

    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return getRedstoneStrength(blockState);
    }

    @Override
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return getRedstoneStrength(blockState);
    }

    @Override
    public boolean shouldCheckWeakPower(BlockState state, IWorldReader world, BlockPos pos, Direction side) {
        return false;
    }

    @Override
    public boolean canProvidePower(BlockState state) {
        return true;
    }

    /*******************************************************************************************************************
     * BlockState                                                                                                      *
     *******************************************************************************************************************/

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(PressurePlateBlock.POWERED);
    }
}