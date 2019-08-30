package com.teambrmodding.assistedprogression.common.tile;

import com.teambr.nucleus.common.tiles.InventoryHandler;
import com.teambrmodding.assistedprogression.common.container.CrafterContainer;
import com.teambrmodding.assistedprogression.common.container.GrinderContainer;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.TileEntityManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;

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
public class CrafterTile extends InventoryHandler implements INamedContainerProvider {

    public CrafterTile() {
        super(TileEntityManager.crafter);
    }

    /*******************************************************************************************************************
     * InventoryHandler                                                                                                *
     *******************************************************************************************************************/

    /**
     * The initial size of the inventory
     *
     * @return How big to make the inventory on creation
     */
    @Override
    protected int getInventorySize() {
        return 20;
    }

    /**
     * Used to define if an item is valid for a slot
     *
     * @param index The slot id
     * @param stack The stack to check
     * @return True if you can put this there
     */
    @Override
    protected boolean isItemValidForSlot(int index, ItemStack stack) {
        return index != 9 && index != 18;
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
        // No - Op
    }

    /**
     * Used to get the field on the server, this will fetch the server value and overwrite the current
     *
     * @param id The field id
     * @return The value on the server, now set to ourselves
     */
    @Override
    public Double getVariable(int id) {
        return null;
    }

    /*******************************************************************************************************************
     * INamedContainerProvider                                                                                         *
     *******************************************************************************************************************/

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("block.assistedprogression.crafter");
    }

    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new CrafterContainer(windowID, playerInventory, this);
    }
}
