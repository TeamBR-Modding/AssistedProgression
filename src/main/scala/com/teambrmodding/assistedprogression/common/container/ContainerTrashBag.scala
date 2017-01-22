package com.teambrmodding.assistedprogression.common.container

import com.teambr.bookshelf.common.container.BaseContainer
import com.teambr.bookshelf.common.container.slots.{PhantomSlot, SLOT_SIZE}
import com.teambr.bookshelf.common.tiles.traits.Inventory
import com.teambrmodding.assistedprogression.common.container.slot.SlotNull
import com.teambrmodding.assistedprogression.common.items.ItemTrashBag
import com.teambrmodding.assistedprogression.managers.ItemManager
import com.teambrmodding.assistedprogression.utils.PlayerUtils
import net.minecraft.entity.player.{EntityPlayer, InventoryPlayer}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}
import net.minecraft.util.EnumHand

import scala.util.control.Breaks._

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
class ContainerTrashBag(inventory : Inventory, playerInventory : InventoryPlayer, bag : ItemStack)
        extends BaseContainer(playerInventory, inventory) {
    addPlayerInventorySlots(85)
    val replacer = new SlotNull(bag.getItem.asInstanceOf[ItemTrashBag].size, SLOT_SIZE.STANDARD, new InventoryNull, 0, -1000, 0)
    addSlotToContainer(replacer)

    breakable {
        for (x <- 0 until this.inventorySlots.size()) {
            val slot = inventorySlots.get(x)
            if (slot != null) {
                if (slot.getStack != null && slot.getStack.equals(bag)) {
                    val x = slot.xDisplayPosition
                    val y = slot.yDisplayPosition
                    slot.xDisplayPosition = -1000
                    replacer.xDisplayPosition = x
                    replacer.yDisplayPosition = y
                    break
                }
            }
        }
    }

    if(bag.getItem == ItemManager.itemTrashBag)
        addSlotToContainer(new PhantomSlot(inventory, 0, 80, 35))
    else
        addInventoryGridPhantom(8, 35, 9)

    def addInventoryGridPhantom(xOffset : Int, yOffset : Int, width : Int) : Unit = {
        val height = Math.ceil(inventorySize.toDouble / width).asInstanceOf[Int]
        var slotId = 0
        for(y <- 0 until height) {
            for(x <- 0 until width) {
                addSlotToContainer(new PhantomSlot(inventory, slotId, xOffset + x * 18, yOffset + y * 18))
                slotId += 1
            }
        }
    }

    override def onContainerClosed(player : EntityPlayer) : Unit = {
        if(!player.worldObj.isRemote) {
            val hand = PlayerUtils.getHandStackIsIn(player, bag)
            val compound = writeToNBT(inventory, new NBTTagCompound, "")
            bag.setTagCompound(compound)
            if (hand == EnumHand.MAIN_HAND)
                player.inventory.setInventorySlotContents(player.inventory.currentItem, bag)
            else
                player.inventory.offHandInventory(0) = bag
        }
    }

    /**
      * Used to save the inventory to an NBT tag
      *
      * We have isolated this to attempt to avoid abstract methods errors that seems to happen on compile
      *
      * @param tag The tag to save to
      * @param inventoryName The name, in case you have more than one
      */
    def writeToNBT(inventory: Inventory, tag : NBTTagCompound, inventoryName : String) : NBTTagCompound = {
        tag.setInteger("Size:" + inventoryName, inventory.getSizeInventory)
        val nbttaglist = new NBTTagList
        for(i <- 0 until inventory.inventoryContents.size()) {
            if(inventory.inventoryContents.get(i) != null) {
                val stackTag = new NBTTagCompound
                stackTag.setByte("Slot:" + inventoryName, i.asInstanceOf[Byte])
                inventory.inventoryContents.get(i).writeToNBT(stackTag)
                nbttaglist.appendTag(stackTag)
            }
        }
        tag.setTag("Items:" + inventoryName, nbttaglist)
        tag
    }
}
