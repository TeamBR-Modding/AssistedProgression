package com.teambrmodding.assistedprogression.common.container;

import com.teambr.nucleus.common.container.BaseContainer;
import com.teambr.nucleus.common.tiles.InventoryHandler;
import com.teambrmodding.assistedprogression.common.tile.GrinderTile;
import com.teambrmodding.assistedprogression.managers.ContainerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 8/27/2019
 */
public class GrinderContainer extends BaseContainer {
    public GrinderTile tile;

    /**
     * Creates the container object
     *
     * @param id
     * @param playerInventory The players inventory
     * @param inventory       The tile/object inventory
     */
    public GrinderContainer(int id, IInventory playerInventory, IItemHandler inventory) {
        super(ContainerManager.grinder, id, playerInventory, inventory);
        addInventoryLine(62, 19, 0, 3);
        addSlot(new SlotItemHandler(inventory, 3, 80, 38) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return false;
            }
        });
        this.tile = (GrinderTile) inventory;
        addInventoryLine(62, 57, 4, 3);
        addPlayerInventorySlots(8, 84);
    }

    public GrinderContainer(int windowId, PlayerInventory playerInv, PacketBuffer extraData) {
        this(windowId, playerInv,
                ((GrinderTile) Minecraft.getInstance().world.getTileEntity(extraData.readBlockPos()))); // Only used on client
    }
}
