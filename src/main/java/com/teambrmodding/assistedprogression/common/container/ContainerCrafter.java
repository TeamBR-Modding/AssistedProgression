package com.teambrmodding.assistedprogression.common.container;

import com.teambr.nucleus.common.container.BaseContainer;
import com.teambrmodding.assistedprogression.common.tiles.TileCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

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
public class ContainerCrafter extends BaseContainer {

    /*******************************************************************************************************************
     * Variables                                                                                                       *
     *******************************************************************************************************************/

    TileCrafter crafter;

    InventoryCraftResult craftResult1;
    InventoryCraftResult craftResult2;

    InventoryCrafting craftingGrid1;
    InventoryCrafting craftingGrid2;


    /**
     * Creates the contianer object
     *
     * @param playerInventory The players inventory
     * @param inventory       The tile/object inventory
     */
    public ContainerCrafter(InventoryPlayer playerInventory, TileCrafter tile) {
        super(playerInventory, tile);

        crafter = tile;

        craftResult1 = new InventoryCraftResult();
        craftResult2 = new InventoryCraftResult();

        craftingGrid1 = new DummyCraftingInventory(crafter, this, 0);
        craftingGrid2 = new DummyCraftingInventory(crafter, this, 10);

        addSlotToContainer(new SlotCrafting(playerInventory.player, craftingGrid1, craftResult1, 0, 80, 31));
        addSlotToContainer(new SlotCrafting(playerInventory.player, craftingGrid2, craftResult2, 1, 80, 59));

        addCraftingGrid(craftingGrid1, 0, 8,   27, 3, 3);
        addCraftingGrid(craftingGrid2, 0, 116, 27, 3, 3);

        addPlayerInventorySlots(8, 84);

        onCraftMatrixChanged(craftingGrid1);
        onCraftMatrixChanged(craftingGrid2);
    }

    /*******************************************************************************************************************
     * ContainerCrafter                                                                                                *
     *******************************************************************************************************************/

    /**
     * Create a crafting grid
     * @param inventory Inventory
     * @param startSlot Start slot
     * @param x X pos
     * @param y Y pos
     * @param width Width
     * @param height height
     */
    private void addCraftingGrid(IInventory inventory, int startSlot, int x, int y, int width, int height) {
        int i = 0;
        for(int h = 0; h < height; h++) {
            for(int w = 0; w < width; w++) {
                addSlotToContainer(new Slot(inventory, startSlot + i, x + (w * 18), y + (h * 18)));
                i++;
            }
        }
    }

    /*******************************************************************************************************************
     * Container                                                                                                       *
     *******************************************************************************************************************/

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        if(inventoryIn == craftingGrid1)
            craftResult1.setInventorySlotContents(0, CraftingManager.findMatchingRecipe(craftingGrid1, crafter.getWorld()));
        else if(inventoryIn == craftingGrid2)
            craftResult2.setInventorySlotContents(0, CraftingManager.findMatchingRecipe(craftingGrid2, crafter.getWorld()));
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return !(slotIn instanceof SlotCrafting);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        if(index < 0 || index > inventorySlots.size())
            return super.transferStackInSlot(playerIn, index);
        Slot slot = inventorySlots.get(index);
        if(slot != null && slot.getHasStack()) {
            ItemStack itemToTransfer = slot.getStack();
            ItemStack copy = itemToTransfer.copy();

            if(index < getInventorySizeNotPlayer()) {
                if (!mergeItemStack(itemToTransfer, getInventorySizeNotPlayer(), inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            } else if(!mergeItemStack(itemToTransfer, 0, getInventorySizeNotPlayer(), false))
                return ItemStack.EMPTY;

            if(index == 0)
                slot.onTake(playerIn, copy);
            else if(index == 1)
                slot.onTake(playerIn, copy);

            if(itemToTransfer.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
                slot.onSlotChanged();
            }
            else
                slot.onSlotChanged();

            onCraftMatrixChanged(craftingGrid1);
            onCraftMatrixChanged(craftingGrid2);

            if(itemToTransfer.getCount() != copy.getCount())
                return copy;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getInventorySizeNotPlayer() {
        return 20;
    }

    /*******************************************************************************************************************
     * Crafting dummy, allow for more than one crafting grid                                                           *
     *******************************************************************************************************************/

    private class DummyCraftingInventory extends InventoryCrafting {
        // Crafter instance
        TileCrafter crafter;
        Container container;
        int offset;

        public DummyCraftingInventory(TileCrafter tile, Container ctainer, int offsetNum) {
            super(null, 3, 3);
            crafter = tile;
            offset = offsetNum;
            container = ctainer;
        }

        @Override
        public int getSizeInventory() {
            return 9;
        }

        @Override
        public ItemStack getStackInSlot(int index) {
            if(index >= getSizeInventory())
                return ItemStack.EMPTY;
            else
                return crafter.getStackInSlot(index + offset);
        }

        @Override
        public ItemStack getStackInRowAndColumn(int row, int column) {
            if(row >= 0 && row < 3) {
                int slot = row + column * 3;
                return getStackInSlot(slot);
            } else
                return ItemStack.EMPTY;
        }

        @Override
        public ItemStack decrStackSize(int index, int count) {
            ItemStack stack = crafter.extractItem(index + offset, count, false);
            container.onCraftMatrixChanged(this);
            return stack;
        }

        @Override
        public void setInventorySlotContents(int index, ItemStack stack) {
            crafter.setStackInSlot(index + offset, stack);
            container.onCraftMatrixChanged(this);
        }
    }
}
