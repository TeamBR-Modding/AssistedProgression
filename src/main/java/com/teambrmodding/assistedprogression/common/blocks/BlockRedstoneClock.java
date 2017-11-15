package com.teambrmodding.assistedprogression.common.blocks;

import com.teambrmodding.assistedprogression.common.tiles.TileRedstoneClock;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

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
public class BlockRedstoneClock extends BaseBlock {

    /**
     * Constructor
     */
    public BlockRedstoneClock() {
        super(Material.ROCK, "blockredstoneclock", TileRedstoneClock.class);
        setDefaultState(blockState.getBaseState().withProperty(BlockPressurePlate.POWERED, false));
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
    public void updateState(World worldIn, BlockPos pos, IBlockState state, boolean max) {
        int redstonePower = max ? 15 : 0;
        worldIn.setBlockState(pos, setRedstoneStrength(state, redstonePower), 4);
        updateNeighbors(worldIn, pos);
    }

    /**
     * Set state with redstone power
     * @param state The blockstate
     * @param strength The power
     * @return Blockstate with power applied
     */
    protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
        return state.withProperty(BlockPressurePlate.POWERED, strength > 0);
    }

    /**
     * Used to tell if state is powered
     * @param state The state
     * @return Power
     */
    protected int getRedstoneStrength(IBlockState state) {
        return state.getValue(BlockPressurePlate.POWERED) ? 15 : 0;
    }

    /**
     * Sends updates to itself and down for all directions
     * @param world The world
     * @param pos The clock pos
     */
    protected void updateNeighbors(World world, BlockPos pos) {
        world.notifyNeighborsOfStateChange(pos, this, true);
        world.notifyNeighborsOfStateChange(pos.down(), this, true);
    }

    /*******************************************************************************************************************
     * Block                                                                                                           *
     *******************************************************************************************************************/

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return getRedstoneStrength(blockState);
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return getRedstoneStrength(blockState);
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    /*******************************************************************************************************************
     * BlockState                                                                                                      *
     *******************************************************************************************************************/

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockPressurePlate.POWERED);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockPressurePlate.POWERED, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockPressurePlate.POWERED) ? 1 : 0;
    }
}
