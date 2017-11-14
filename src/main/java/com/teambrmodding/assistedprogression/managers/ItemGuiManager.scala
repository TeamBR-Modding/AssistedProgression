package com.teambrmodding.assistedprogression.managers

import com.teambr.bookshelf.common.items.InventoryHandlerItem
import com.teambrmodding.assistedprogression.client.gui.GuiTrashBag
import com.teambrmodding.assistedprogression.common.container.ContainerTrashBag
import com.teambrmodding.assistedprogression.common.items.ItemTrashBag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import net.minecraftforge.items.CapabilityItemHandler

/**
  * This file was created for NeoTech
  *
  * NeoTech is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Paul Davis <pauljoda>
  * @since 1/26/2016
  */
object ItemGuiManager {
    lazy val TRASH_BAG_GUI_ID = 0
}

class ItemGuiManager extends IGuiHandler {
    override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
        ID match {
            case ItemGuiManager.TRASH_BAG_GUI_ID =>
                if(player.getHeldItemMainhand != null && player.getHeldItemMainhand.getItem.isInstanceOf[ItemTrashBag])
                    new GuiTrashBag( player.getHeldItemMainhand.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null),
        player.inventory, player.getHeldItemMainhand)
                else if(player.getHeldItemOffhand != null && player.getHeldItemOffhand.getItem.isInstanceOf[ItemTrashBag])
                    new GuiTrashBag(ItemTrashBag.getInventory(player.getHeldItemOffhand), player.inventory, player.getHeldItemOffhand)
                else
                    null
            case _ => null
        }
    }

    override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
        ID match {
            case ItemGuiManager.TRASH_BAG_GUI_ID =>
                if(player.getHeldItemMainhand != null && player.getHeldItemMainhand.getItem.isInstanceOf[ItemTrashBag])
                    new ContainerTrashBag(ItemTrashBag.getInventory(player.getHeldItemMainhand), player.inventory, player.getHeldItemMainhand)
                else if(player.getHeldItemOffhand != null && player.getHeldItemOffhand.getItem.isInstanceOf[ItemTrashBag])
                    new ContainerTrashBag(ItemTrashBag.getInventory(player.getHeldItemOffhand), player.inventory, player.getHeldItemOffhand)
                else
                    null
            case _ => null
        }
    }
}
