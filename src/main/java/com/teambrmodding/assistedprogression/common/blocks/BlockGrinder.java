package com.teambrmodding.assistedprogression.common.blocks;

import com.teambr.bookshelf.common.IOpensGui;
import com.teambrmodding.assistedprogression.client.gui.GuiGrinder;
import com.teambrmodding.assistedprogression.common.container.ContainerGrinder;
import com.teambrmodding.assistedprogression.common.tiles.TileGrinder;
import com.teambrmodding.assistedprogression.managers.BlockManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumBlockRenderType;
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
 * @since 11/12/17
 */
public class BlockGrinder extends BaseBlock implements IOpensGui {

    /**
     * Block constructor
     */
    public BlockGrinder() {
        super(Material.ROCK, "blockGrinder", TileGrinder.class);
    }

    /*******************************************************************************************************************
     * Block                                                                                                           *
     *******************************************************************************************************************/

    @Override
    public void onLanded(World worldIn, Entity entityIn) {
        super.onLanded(worldIn, entityIn);

        if(entityIn instanceof EntityPlayer && entityIn.fallDistance > 0.0 &&
                !worldIn.isAirBlock(new BlockPos(entityIn.posX, entityIn.posY, entityIn.posZ))) {
            Block landedBlock = worldIn.getBlockState(new BlockPos(entityIn.posX, entityIn.posY, entityIn.posZ)).getBlock();
            if(landedBlock == Blocks.WOODEN_PRESSURE_PLATE)
                ((TileGrinder)worldIn.getTileEntity(new BlockPos(entityIn.posX, entityIn.posY - 1, entityIn.posZ)))
                        .activateGrinder((int) entityIn.fallDistance, 1.0);
            else if(landedBlock == Blocks.STONE_PRESSURE_PLATE)
                ((TileGrinder)worldIn.getTileEntity(new BlockPos(entityIn.posX, entityIn.posY - 1, entityIn.posZ)))
                        .activateGrinder((int) entityIn.fallDistance, 1.25);
            else if(landedBlock == BlockManager.blockPlayerPlate)
                ((TileGrinder)worldIn.getTileEntity(new BlockPos(entityIn.posX, entityIn.posY - 1, entityIn.posZ)))
                        .activateGrinder((int) entityIn.fallDistance, 1.5);
            else if(landedBlock == Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE)
                ((TileGrinder)worldIn.getTileEntity(new BlockPos(entityIn.posX, entityIn.posY - 1, entityIn.posZ)))
                        .activateGrinder((int) entityIn.fallDistance, 1.75);
            else if(landedBlock == Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE)
                ((TileGrinder)worldIn.getTileEntity(new BlockPos(entityIn.posX, entityIn.posY - 1, entityIn.posZ)))
                        .activateGrinder((int) entityIn.fallDistance, 2.0);
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    /*******************************************************************************************************************
     * IOpensGui                                                                                                       *
     *******************************************************************************************************************/

    /**
     * Return the container for this tile
     *
     * @param id     Id, probably not needed but could be used for multiple guis
     * @param player The player that is opening the gui
     * @param world  The world
     * @param x      X Pos
     * @param y      Y Pos
     * @param z      Z Pos
     * @return The container to open
     */
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerGrinder(player.inventory, (TileGrinder) world.getTileEntity(new BlockPos(x, y, z)));
    }

    /**
     * Return the gui for this tile
     *
     * @param id     Id, probably not needed but could be used for multiple guis
     * @param player The player that is opening the gui
     * @param world  The world
     * @param x      X Pos
     * @param y      Y Pos
     * @param z      Z Pos
     * @return The gui to open
     */
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return new GuiGrinder(player, (TileGrinder) world.getTileEntity(new BlockPos(x, y, z)));
    }
}
