package com.teambrmodding.assistedprogression.common.blocks.storage;

import com.teambr.bookshelf.util.WorldUtils;
import com.teambrmodding.assistedprogression.common.blocks.BaseBlock;
import com.teambrmodding.assistedprogression.common.tiles.storage.TileFlushableChest;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Arrays;

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
public class BlockFlushableChest extends BaseBlock {

    private static PropertyDirection FOUR_WAY =
            PropertyDirection.create("facing", Arrays.asList(EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST));

    protected BlockFlushableChest() {
        super(Material.IRON, "blockFlushableChest", TileFlushableChest.class);

        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    }

    @Override
    public void observedNeighborChange(IBlockState state, World world, BlockPos pos, Block block, BlockPos changedBlockPos) {
        if (!world.isRemote)
            if (world.isBlockPowered(pos)) {
                ((TileFlushableChest) world.getTileEntity(pos)).clear();
                world.setBlockState(pos, state, 6);
            }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        int playerFacingDirection = placer == null ? 0 : MathHelper.floor((placer.rotationYaw / 90.F) + 0.5F) & 3;
        EnumFacing facing = EnumFacing.getHorizontal(playerFacingDirection).getOpposite();
        worldIn.setBlockState(pos, getDefaultState().withProperty(FOUR_WAY, facing));
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing side) {
        NBTTagCompound tag = new NBTTagCompound();
        world.getTileEntity(pos).writeToNBT(tag);
        if(side != EnumFacing.UP && side != EnumFacing.DOWN)
            world.setBlockState(pos, world.getBlockState(pos).withProperty(FOUR_WAY, side))
        else
            world.setBlockState(pos, world.getBlockState(pos).withProperty(FOUR_WAY, WorldUtils.rotateRight(world.getBlockState(pos).getValue(FOUR_WAY))));

        if (tag != null)
            world.getTileEntity(pos).readFromNBT(tag);

        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return true;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FOUR_WAY);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FOUR_WAY, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FOUR_WAY).getIndex();
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IInventory)
            return Container.calcRedstoneFromInventory((IInventory) tile);

        return 0;
    }
}
