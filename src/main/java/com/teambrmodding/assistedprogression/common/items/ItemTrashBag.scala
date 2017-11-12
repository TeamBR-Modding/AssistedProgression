package com.teambrmodding.assistedprogression.common.items

import com.teambr.bookshelf.client.gui.GuiColor
import com.teambr.bookshelf.common.tiles.traits.Inventory
import com.teambrmodding.assistedprogression.AssistedProgression
import com.teambrmodding.assistedprogression.managers.{ItemGuiManager, ItemManager}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.{ActionResult, EnumActionResult, EnumHand, SoundCategory}
import net.minecraft.world.World
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

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
class ItemTrashBag(var name: String, var size : Int) extends BaseItem(name, 1) {

    override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer, hand: EnumHand) : ActionResult[ItemStack] = {
        if(!world.isRemote)
            player.openGui(AssistedProgression, ItemGuiManager.TRASH_BAG_GUI_ID, world, player.posX.toInt, player.posY.toInt, player.posZ.toInt)
        new ActionResult[ItemStack](EnumActionResult.SUCCESS, stack)
    }

    @SideOnly(Side.CLIENT)
    override def addInformation(stack: ItemStack, player: EntityPlayer, list: java.util.List[String], boolean: Boolean): Unit = {
        if (stack.hasTagCompound) {
            val inventory = ItemTrashBag.getInventory(stack)
            list.add(GuiColor.YELLOW + "Trashing: ")
            for(x <- 0 until size) {
                if(inventory.getStackInSlot(x) != null)
                    list.add(inventory.getStackInSlot(x).getDisplayName)
            }
        }
    }
}

object ItemTrashBag {

    def getInventory(stack : ItemStack) : Inventory = {
        if(stack.getItem.isInstanceOf[ItemTrashBag])
            return new TrashBagInventory(stack)
        null
    }

    class TrashBagInventory(stack : ItemStack) extends Inventory {
        if(stack != null && stack.hasTagCompound)
            readFromNBT(stack.getTagCompound)
        override def initialSize: Int = stack.getItem.asInstanceOf[ItemTrashBag].size
    }

    @SubscribeEvent
    def onItemPickUp(event : EntityItemPickupEvent): Unit = {
        val player = event.getEntityPlayer
        val pickedUp = event.getItem.getEntityItem

        if(pickedUp == null || player == null) return

        breakable {
            for (x <- 0 until player.inventory.getSizeInventory) {
                val stack = player.inventory.getStackInSlot(x)

                if (stack != null && stack.getItem.isInstanceOf[ItemTrashBag] && stack.hasTagCompound) {
                    val trashBagInventory = new TrashBagInventory(stack)
                    for (i <- 0 until trashBagInventory.getSizeInventory) {
                        val containedStack = trashBagInventory.getStackInSlot(i)
                        if (containedStack != null) {
                            if (containedStack.getItem == pickedUp.getItem && containedStack.getItemDamage == pickedUp.getItemDamage &&
                                    ItemStack.areItemStackTagsEqual(containedStack, pickedUp)) {
                                pickedUp.stackSize = 0
                                event.getEntity.worldObj.playSound(null.asInstanceOf[EntityPlayer],
                                    new BlockPos(event.getEntity.posX, event.getEntity.posY, event.getEntity.posZ),
                                    SoundEvents.ENTITY_ITEM_PICKUP,
                                    SoundCategory.BLOCKS, 0.3F, 0.5F)
                                break
                            }
                        }
                    }
                }
            }
        }
    }
}
