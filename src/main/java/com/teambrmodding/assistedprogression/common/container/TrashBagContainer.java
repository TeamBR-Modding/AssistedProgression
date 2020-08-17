package com.teambrmodding.assistedprogression.common.container;

import com.mojang.datafixers.util.Pair;
import com.teambr.nucleus.common.container.BaseContainer;
import com.teambr.nucleus.common.container.slots.PhantomSlot;
import com.teambr.nucleus.common.items.InventoryHandlerItem;
import com.teambr.nucleus.helper.GuiHelper;
import com.teambr.nucleus.util.RenderUtils;
import com.teambrmodding.assistedprogression.common.item.TrashBagItem;
import com.teambrmodding.assistedprogression.managers.ContainerManager;
import com.teambrmodding.assistedprogression.managers.ItemManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 9/5/2019
 */
public class TrashBagContainer extends BaseContainer {

    // Holds instance of the itemstack
    public ItemStack trashBag;

    /**
     * Main container constructor, called on server side
     *
     * @param windowID        The window id
     * @param playerInventory Player's Inventory
     * @param inventory       The trash bag inventory
     * @param trashBagStack   The trashbag stack
     */
    public TrashBagContainer(int windowID, IInventory playerInventory,
                             InventoryHandlerItem inventory, ItemStack trashBagStack) {
        super(ContainerManager.trash_bag, windowID, playerInventory, inventory);
        this.trashBag = trashBagStack;

        // Add trash bag slots
        if (trashBag.getItem() == ItemManager.trash_bag)
            addSlot(new PhantomSlot(inventory, 0, 79, 32) {
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack) {
                    return true;
                }
            });
        else
            addInventoryGridPhantom(8, 32, 9);

        addPlayerInventorySlots(84);

        // Create the slot that will replace the trash bag itself, create a trashable slot
        Slot replacer = new Slot(new InventoryNull(), 0, -1000, 0) {
            @Override
            public Pair<ResourceLocation, ResourceLocation> getBackground() {
                if (trashBag.getItem() == ItemManager.trash_bag)
                    return Pair.of(RenderUtils.MC_ITEMS_RESOURCE_LOCATION,
                            new ResourceLocation("assistedprogression:items/trash_bag"));
                else
                    return Pair.of(RenderUtils.MC_ITEMS_RESOURCE_LOCATION,
                            new ResourceLocation("assistedprogression:items/hefty_bag"));            }

            @Override
            public boolean isItemValid(ItemStack p_75214_1_) {
                return false;
            }
        };
        addSlot(replacer);
    }

    /**
     * Client side call
     *
     * @param windowId  The window
     * @param playerInv The player inventory
     * @param extraData Extra data packet, for fml
     */
    public TrashBagContainer(int windowId, PlayerInventory playerInv, PacketBuffer extraData) {
        this(windowId, playerInv,
                (InventoryHandlerItem) Minecraft.getInstance().player.getHeldItemMainhand()
                        .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null),
                Minecraft.getInstance().player.getHeldItemMainhand());
    }

    /**
     * Used to add a grid of phantom slots a container
     *
     * @param offsetX The x start
     * @param offsetY The y start
     * @param width   How wide the grid is
     */
    private void addInventoryGridPhantom(int offsetX, int offsetY, int width) {
        int height = (int) Math.ceil((double) inventorySize / width);
        int slotId = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                addSlot(new PhantomSlot(inventory, slotId, offsetX + x * 18, offsetY + y * 18) {
                    @Override
                    public boolean isItemValid(@Nonnull ItemStack stack) {
                        return true;
                    }
                });
                slotId++;
            }
        }
    }

    /*******************************************************************************************************************
     * Container                                                                                                       *
     *******************************************************************************************************************/

    /**
     * Called when the container is exited
     *
     * @param playerIn The player
     */
    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);

        if (!playerIn.world.isRemote) {
            if (ItemStack.areItemsEqual(playerIn.getHeldItemMainhand(), trashBag)) {
                ((InventoryHandlerItem) playerIn.getHeldItemMainhand()
                        .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null))
                        .writeToNBT(trashBag.write(new CompoundNBT()));
            } else if (ItemStack.areItemsEqual(playerIn.getHeldItemOffhand(), trashBag))
                ((InventoryHandlerItem) playerIn.getHeldItemOffhand()
                        .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null))
                        .writeToNBT(trashBag.write(new CompoundNBT()));
        }
    }

    /**
     * This is not needed in this context, do vanilla logic
     */
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        return ItemStack.EMPTY;
    }
}