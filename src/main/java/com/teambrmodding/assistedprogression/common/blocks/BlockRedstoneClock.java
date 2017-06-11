package com.teambrmodding.assistedprogression.common.blocks;

import com.teambrmodding.assistedprogression.common.tiles.TileRedstoneClock;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
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
 * @author Dyonovan
 * @since 6/2/2017
 */
public class BlockRedstoneClock extends BaseBlock {

    protected BlockRedstoneClock() {
        super(Material.ROCK, "blockRedstoneClock", TileRedstoneClock.class);

        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPressurePlate.POWERED, false));
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    public IBlockState setRedstoneStrength(IBlockState state, int strength) {
        return state.withProperty(BlockPressurePlate.POWERED, strength > 0);
    }

    public int getRedstoneStrength(IBlockState state) {
        return state.getValue(BlockPressurePlate.POWERED) ? 15 : 0;
    }

    public void updateState(World world, BlockPos pos, IBlockState state, boolean toMax) {
        int i = toMax ? 15 : 0;
        world.setBlockState(pos, this.setRedstoneStrength(state, i), 4);
        updateNeighbors(world, pos);
    }

    protected void updateNeighbors(World world, BlockPos pos) {
        world.notifyNeighborsOfStateChange(pos, this, false);
        world.notifyNeighborsOfStateChange(pos.down(), this, false);
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return this.getRedstoneStrength(state);
    }

    @Override
    public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return this.getRedstoneStrength(state);
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BlockPressurePlate.POWERED, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockPressurePlate.POWERED) ? 1 : 0;
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockPressurePlate.POWERED);
    }
}
