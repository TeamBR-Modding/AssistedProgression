package com.teambrmodding.assistedprogression.client.gui.storage

import java.awt.Color

import com.teambr.bookshelf.client.gui.component.BaseComponent
import com.teambr.bookshelf.client.gui.component.control.{GuiComponentCheckBox, GuiComponentSetNumber}
import com.teambr.bookshelf.client.gui.component.display.{GuiComponentText, GuiTabCollection}
import com.teambr.bookshelf.client.gui.{GuiBase, GuiColor}
import com.teambrmodding.assistedprogression.common.container.storage.ContainerFlushableChest
import com.teambrmodding.assistedprogression.common.tiles.storage.TileFlushableChest
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.{Item, ItemStack}

import scala.collection.mutable.ArrayBuffer

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

    override def addRightTabs(tabs: GuiTabCollection): Unit = {
        val infoTab = new ArrayBuffer[BaseComponent]()
        infoTab += new GuiComponentSetNumber(72, 21, 30, tile.getFlushInterval, 1, 60) {
            override def setValue(i: Int): Unit = {
                tile.setFlushInterval(i)
                tile.sendValueToServer(tile.FLUSH_INTERVAL, i.toDouble)
            }
        }
        infoTab += new GuiComponentCheckBox(10, 20, "AutoFlush", tile.getAutoFlush) {
            override def setValue(bool: Boolean): Unit = {
                tile.setAutoFlush(bool)
                tile.sendValueToServer(tile.AUTO_FLUSH, if (bool) 0 else -1)
            }
        }
        infoTab += new GuiComponentCheckBox(10, 30, "Sound", tile.getFlushSound) {
            override def setValue(bool: Boolean): Unit = {
                tile.setFlushSound(bool)
                tile.sendValueToServer(tile.FLUSH_SOUND, if (bool) 0 else -1)
            }
        }
        infoTab += new GuiComponentText(GuiColor.ORANGE + I18n.format("assistedprogression.text.config"), 26, 6)
        tabs.addTab(infoTab.toList, 115, 50, new Color(0, 155,0), new ItemStack(Item.getItemFromBlock(Blocks.STONE_BUTTON)))
    }
}
