package com.teambrmodding.assistedprogression.common.container;

import com.teambr.bookshelf.common.container.BaseContainer;
import com.teambr.bookshelf.common.container.slots.PhantomSlot;
import com.teambr.bookshelf.common.items.InventoryHandlerItem;
import com.teambrmodding.assistedprogression.managers.ItemManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

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
public class ContainerTrashBag extends BaseContainer {

    // Holds instance of the itemstack
    private ItemStack trashBag;

    /**
     * Creates the container object
     *
     * @param playerInventory The players inventory
     * @param inventory       The tile/object inventory
     */
    public ContainerTrashBag(IInventory playerInventory, InventoryHandlerItem inventory, ItemStack trashBagStack) {
        super(playerInventory, inventory);
        this.trashBag = trashBagStack;

        addPlayerInventorySlots(84);

        // Create the slot that will replace the trash bag itself, create a trashable slot
        Slot replacer = new Slot(new InventoryNull(), 0, -1000, 0) {
            @Nullable
            @Override
            public String getSlotTexture() {
                if(trashBag.getItem() == ItemManager.itemTrashBag())
                    return "assistedprogression:items/trashBag";
                else
                    return "assistedprogression:items/heftyBag";
            }
        };
        addSlotToContainer(replacer);

        // Set replacer over the normal slot
        for(Slot slot : inventorySlots) {
            if(slot != null) {
                if(!slot.getStack().isEmpty() && ItemStack.areItemStacksEqual(slot.getStack(), trashBag)) {
                    int x = slot.xPos;
                    int y = slot.yPos;
                    slot.xPos = -1000;
                    replacer.xPos = x;
                    replacer.yPos = y;
                    break;
                }
            }
        }

        if(trashBag.getItem() == ItemManager.itemTrashBag())
            addSlotToContainer(new PhantomSlot(inventory, 0, 79, 32));
        else
            addInventoryGridPhantom(8, 32, 9);
    }

    /**
     * Used to add a grid of phantom slots a container
     * @param offsetX The x start
     * @param offsetY The y start
     * @param width How wide the grid is
     */
    private void addInventoryGridPhantom(int offsetX, int offsetY, int width) {
        int height = (int) Math.ceil((double) inventorySize / width);
        int slotId = 0;
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                addSlotToContainer(new PhantomSlot(inventory, slotId, offsetX + x * 18, offsetY + y * 18));
                slotId++;
            }
        }
    }
}
