package com.teambrmodding.assistedprogression.common.items;

import com.teambr.bookshelf.common.items.InventoryHandlerItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

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
public class ItemTrashBag extends BaseItem {

    // Variables
    int bagSize;

    /**
     * Base constructor for all items
     *
     * @param itemName     The item name
     * @param maxStackSize Maximum stack size
     */
    public ItemTrashBag(String name, int size) {
        super(name, 1);
        bagSize = size;
    }

    /*******************************************************************************************************************
     * Item                                                                                                            *
     *******************************************************************************************************************/

    /**
     * Called from ItemStack.setItem, will hold extra data for the life of this ItemStack.
     * Can be retrieved from stack.getCapabilities()
     * The NBT can be null if this is not called from readNBT or if the item the stack is
     * changing FROM is different then this item, or the previous item had no capabilities.
     *
     * This is called BEFORE the stacks item is set so you can use stack.getItem() to see the OLD item.
     * Remember that getItem CAN return null.
     *
     * @param stack The ItemStack
     * @param nbt NBT of this item serialized, or null.
     * @return A holder instance associated with this ItemStack where you can hold capabilities for the life of this item.
     */
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new InventoryHandlerItem(stack) {
            @Override
            protected int getInventorySize() {
                return bagSize;
            }

            @Override
            protected boolean isItemValidForSlot(int index, ItemStack stack) {
                return true;
            }
        };
    }

    /*******************************************************************************************************************
     * Event                                                                                                           *
     *******************************************************************************************************************/

    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        EntityPlayer player     = event.getEntityPlayer();
        ItemStack    pickedItem = event.getItem().getEntityItem();

        if(pickedItem == null || player == null) return;

        for(ItemStack stack : player.inventory.mainInventory) {
            if(!stack.isEmpty() && stack.getItem() instanceof ItemTrashBag && stack.hasTagCompound()) {
                IItemHandler trashBagHandler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                for(int x = 0; x < trashBagHandler.getSlots(); x++) {
                    ItemStack trashKey = trashBagHandler.getStackInSlot(x);
                    if(!trashKey.isEmpty()) {
                        if(ItemStack.areItemStacksEqual(trashKey, pickedItem)) {
                            pickedItem.shrink(pickedItem.getCount());
                            player.world.playSound(null,
                                    new BlockPos(player.posX, player.posY, player.posZ),
                                    SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS,
                                    0.3F, 0.5F);
                            break;
                        }
                    }
                }
            }
        }
    }
}
