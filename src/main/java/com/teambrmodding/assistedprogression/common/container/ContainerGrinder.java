package com.teambrmodding.assistedprogression.common.container;

import com.teambr.bookshelf.common.container.BaseContainer;
import com.teambrmodding.assistedprogression.common.tiles.TileGrinder;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

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
public class ContainerGrinder extends BaseContainer {

    /**
     * Creates the container object
     *
     * @param playerInventory The players inventory
     * @param inventory       The tile/object inventory
     */
    public ContainerGrinder(InventoryPlayer playerInventory, TileGrinder inventory) {
        super(playerInventory, inventory);
        addInventoryLine(62, 19, 0, 3);
        addSlotToContainer(new SlotItemHandler(inventory, 3, 80, 38) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return false;
            }
        });
        addInventoryLine(62, 57, 4, 3);
        addPlayerInventorySlots(8, 84);
    }
}
