package com.teambrmodding.assistedprogression.client.gui.storage

import com.teambr.bookshelf.client.gui.GuiBase
import com.teambrmodding.assistedprogression.common.container.storage.ContainerFlushableChest
import com.teambrmodding.assistedprogression.common.tiles.storage.TileFlushableChest
import net.minecraft.entity.player.EntityPlayer

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
class GuiFlushableChest(player: EntityPlayer, tile: TileFlushableChest) extends
        GuiBase[ContainerFlushableChest](new ContainerFlushableChest(player.inventory, tile), 175, 165, "assistedprogression.flushchest.title"){

    override def addComponents(): Unit = { }
}
