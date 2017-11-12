package com.teambrmodding.assistedprogression.client.gui

import com.teambr.bookshelf.client.gui.GuiBase
import com.teambr.bookshelf.common.tiles.traits.Inventory
import com.teambrmodding.assistedprogression.common.container.ContainerTrashBag
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack

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
class GuiTrashBag(inventory : Inventory, inventoryPlayer: InventoryPlayer, bag : ItemStack) extends
    GuiBase[ContainerTrashBag](new ContainerTrashBag(inventory, inventoryPlayer, bag), 175, 165, bag.getDisplayName) {
    override def addComponents(): Unit = {}
}
