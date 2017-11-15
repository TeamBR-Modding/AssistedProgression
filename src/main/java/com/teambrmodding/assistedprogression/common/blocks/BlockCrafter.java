package com.teambrmodding.assistedprogression.common.blocks;

import com.teambr.bookshelf.common.IOpensGui;
import com.teambrmodding.assistedprogression.client.gui.GuiCrafter;
import com.teambrmodding.assistedprogression.common.container.ContainerCrafter;
import com.teambrmodding.assistedprogression.common.tiles.TileCrafter;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
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
public class BlockCrafter extends BaseBlock implements IOpensGui {

    /**
     * Constructor
     */
    public BlockCrafter() {
        super(Material.WOOD, "block_crafter", TileCrafter.class);
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
        return new ContainerCrafter(player.inventory, (TileCrafter) world.getTileEntity(new BlockPos(x, y, z)));
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
        return new GuiCrafter(player.inventory, (TileCrafter) world.getTileEntity(new BlockPos(x, y, z)));
    }
}
