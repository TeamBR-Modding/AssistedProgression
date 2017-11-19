package com.teambrmodding.assistedprogression.common.blocks;

import com.teambr.nucleus.common.IOpensGui;
import com.teambrmodding.assistedprogression.client.gui.GuiKineticGenerator;
import com.teambrmodding.assistedprogression.common.container.ContainerKineticGenerator;
import com.teambrmodding.assistedprogression.common.tiles.TileKineticGenerator;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
 * @since 11/18/17
 */
public class BlockKineticGenerator extends BaseBlock implements IOpensGui {

    /**
     * Constructor
     */
    public BlockKineticGenerator() {
        super(Material.ROCK, "block_kinetic_generator", TileKineticGenerator.class);
    }

    @Override
    public void onNeighborChange(IBlockAccess worldIn, BlockPos pos, BlockPos neighbor) {
        World world = ((TileKineticGenerator) worldIn.getTileEntity(pos)).getWorld();
        if(!world.isRemote &&
                world.getBlockState(neighbor).getBlock() instanceof BlockPistonExtension) {
            TileKineticGenerator generator = (TileKineticGenerator) world.getTileEntity(pos);
            generator.energyStorage.receivePower(10, true);
            generator.sendValueToClient(TileKineticGenerator.UPDATE_ENERGY_ID, generator.getEnergyStored());
        }
        super.onNeighborChange(world, pos, neighbor);
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.NORMAL;
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
        return new ContainerKineticGenerator(player.inventory, (TileKineticGenerator) world.getTileEntity(new BlockPos(x, y, z)));
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
        return new GuiKineticGenerator(player, (TileKineticGenerator) world.getTileEntity(new BlockPos(x, y, z)));
    }
}
