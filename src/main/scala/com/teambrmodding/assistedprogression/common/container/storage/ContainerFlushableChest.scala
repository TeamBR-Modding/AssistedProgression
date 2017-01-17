package com.teambrmodding.assistedprogression.common.container.storage

import com.teambr.bookshelf.common.container.BaseContainer
import com.teambrmodding.assistedprogression.common.tiles.storage.TileFlushableChest
import net.minecraft.entity.player.{EntityPlayer, InventoryPlayer}

/**
  * This file was created for AssistedProgression
  *
  * AssistedProgression is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Dyonovan
  * @since 1/16/2017
  */
class ContainerFlushableChest(inventory: InventoryPlayer, tile: TileFlushableChest) extends BaseContainer(inventory, tile) {

    addInventoryGrid(8, 16, 9)
    addPlayerInventorySlots(8, 84)
    tile.openInventory(inventory.player)

    override def onContainerClosed(player : EntityPlayer): Unit = {
        super.onContainerClosed(player)
        tile.closeInventory(player)
    }
}
