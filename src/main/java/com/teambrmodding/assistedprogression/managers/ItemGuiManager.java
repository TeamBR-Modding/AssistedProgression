package com.teambrmodding.assistedprogression.managers;

import com.teambr.bookshelf.common.items.InventoryHandlerItem;
import com.teambrmodding.assistedprogression.client.gui.GuiTrashBag;
import com.teambrmodding.assistedprogression.common.container.ContainerTrashBag;
import com.teambrmodding.assistedprogression.common.items.ItemTrashBag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.CapabilityItemHandler;

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
public class ItemGuiManager implements IGuiHandler {
    public static final int TRASH_BAG_GUI_ID = 0;

    /**
     * Returns a Server side Container to be displayed to the user.
     *
     * @param ID     The Gui ID Number
     * @param player The player viewing the Gui
     * @param world  The current world
     * @param x      X Position
     * @param y      Y Position
     * @param z      Z Position
     * @return A GuiScreen/Container to be displayed to the user, null if none.
     */
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case TRASH_BAG_GUI_ID :
                if(!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof ItemTrashBag)
                    return new GuiTrashBag((InventoryHandlerItem) player.getHeldItemMainhand().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null),
                            player.inventory, player.getHeldItemMainhand());
                else if(!player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() instanceof ItemTrashBag)
                    return new GuiTrashBag((InventoryHandlerItem) player.getHeldItemOffhand().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null),
                            player.inventory, player.getHeldItemOffhand());
            default :
                return null;
        }
    }

    /**
     * Returns a Container to be displayed to the user. On the client side, this
     * needs to return a instance of GuiScreen On the server side, this needs to
     * return a instance of Container
     *
     * @param ID     The Gui ID Number
     * @param player The player viewing the Gui
     * @param world  The current world
     * @param x      X Position
     * @param y      Y Position
     * @param z      Z Position
     * @return A GuiScreen/Container to be displayed to the user, null if none.
     */
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case TRASH_BAG_GUI_ID :
                if(!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof ItemTrashBag)
                    return new ContainerTrashBag(player.inventory,
                            (InventoryHandlerItem) player.getHeldItemMainhand().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null),
                            player.getHeldItemMainhand());
                else if(!player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() instanceof ItemTrashBag)
                    return new ContainerTrashBag(player.inventory,
                            (InventoryHandlerItem) player.getHeldItemOffhand().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null),
                             player.getHeldItemOffhand());
            default :
                return null;
        }    }
}
