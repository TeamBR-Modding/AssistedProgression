package com.teambrmodding.assistedprogression.client.gui;

import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.common.items.InventoryHandlerItem;
import com.teambrmodding.assistedprogression.common.container.ContainerTrashBag;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.ItemManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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
public class GuiTrashBag extends GuiBase<ContainerTrashBag> {

    /**
     * Main constructor for Guis
     *
     * @param inventory The container
     * @param width     The width of the gui
     * @param height    The height of the gui
     * @param title     The title of the gui
     * @param texture   The location of the background texture
     */
    public GuiTrashBag(InventoryHandlerItem inventory, InventoryPlayer inventoryPlayer, ItemStack bag) {
        super(new ContainerTrashBag(inventoryPlayer, inventory, bag), 175, 165, bag.getDisplayName(),
                new ResourceLocation(Reference.MOD_ID,
                        "textures/gui/" + (bag.getItem() == ItemManager.itemTrashBag ? "trashbag.png" : "heftybag.png")));
    }

    /**
     * This will be called after the GUI has been initialized and should be where you add all components.
     */
    @Override
    protected void addComponents() {
        // No Op
    }
}
