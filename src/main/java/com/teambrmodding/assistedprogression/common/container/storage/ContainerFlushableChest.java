package com.teambrmodding.assistedprogression.common.container.storage;

import com.teambr.bookshelf.common.container.BaseContainer;
import com.teambrmodding.assistedprogression.common.tiles.storage.TileFlushableChest;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.IItemHandler;

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
public class ContainerFlushableChest extends BaseContainer {

    /**
     * Creates the contianer object
     *
     * @param playerInventory The players inventory
     * @param inventory       The tile/object inventory
     */
    public ContainerFlushableChest(InventoryPlayer playerInventory, TileFlushableChest inventory) {
        super(playerInventory, inventory);
        addInventoryGrid(8, 13, 9, 0);
        addPlayerInventorySlots(84);
    }
}
