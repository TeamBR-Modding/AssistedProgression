package com.teambrmodding.assistedprogression.common.blocks;

import com.teambrmodding.assistedprogression.AssistedProgression;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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
public class BlockPlayerPlate extends BlockBasePressurePlate {

    /**
     * Constructor, since we are not extending BaseBlock we need to do some manual stuff
     */
    protected BlockPlayerPlate() {
        super(Material.IRON);
        setDefaultState(blockState.getBaseState().withProperty(BlockPressurePlate.POWERED, false));
        setUnlocalizedName(Reference.MOD_ID() + ":" + "blockPlayerPlate");
        setCreativeTab(AssistedProgression.tabAssistedProgression());
        setHardness(2.0F);
    }

    /*******************************************************************************************************************
     * BlockBasePressurePlate                                                                                          *
     *******************************************************************************************************************/

    @Override
    protected void playClickOnSound(World world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_ON,
                SoundCategory.BLOCKS, 0.3F, 0.6F);
    }

    @Override
    protected void playClickOffSound(World world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_OFF,
                SoundCategory.BLOCKS, 0.3F, 0.5F);
    }

    @Override
    protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
        return worldIn.getEntitiesWithinAABB(EntityPlayer.class,
                new AxisAlignedBB(pos.getX() + 0.125F, pos.getY(), pos.getZ() + 0.125F,
                        pos.getX() + 1 - 0.125F, pos.getY() + 0.25D, pos.getZ() + 1 - 0.125F)).isEmpty() ?
                15 : 0;
    }

    @Override
    protected int getRedstoneStrength(IBlockState state) {
        return state.getValue(BlockPressurePlate.POWERED) ? 15 : 0;
    }

    @Override
    protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
        return state.withProperty(BlockPressurePlate.POWERED, strength > 0);
    }

    /*******************************************************************************************************************
     * BlockState Methods                                                                                              *
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
