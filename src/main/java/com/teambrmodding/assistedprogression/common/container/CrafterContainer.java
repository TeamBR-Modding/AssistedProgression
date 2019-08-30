package com.teambrmodding.assistedprogression.common.container;

import com.teambr.nucleus.Nucleus;
import com.teambr.nucleus.client.gui.ISyncingTileScreen;
import com.teambr.nucleus.common.container.BaseContainer;
import com.teambr.nucleus.network.PacketManager;
import com.teambrmodding.assistedprogression.common.tile.CrafterTile;
import com.teambrmodding.assistedprogression.managers.ContainerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 8/29/2019
 */
public class CrafterContainer extends BaseContainer {

    /*******************************************************************************************************************
     * Variables                                                                                                       *
     *******************************************************************************************************************/

    public CrafterTile crafter;

    public PlayerEntity player;

    protected CraftResultInventory craftResult1;
    protected CraftResultInventory craftResult2;

    protected DummyCraftingInventory craftingGrid1;
    protected DummyCraftingInventory craftingGrid2;


    /**
     * Creates the contianer object
     *
     * @param playerInventory The players inventory
     * @param tile       The tile/object inventory
     */
    public CrafterContainer(int windowID, PlayerInventory playerInventory, CrafterTile tile) {
        super(ContainerManager.crafter, windowID, playerInventory, tile);

        crafter = tile;

        player = playerInventory.player;

        craftResult1 = new CraftResultInventory();
        craftResult2 = new CraftResultInventory();

        craftingGrid1 = new DummyCraftingInventory(this, 0);
        craftingGrid2 = new DummyCraftingInventory(this, 10);

        addSlot(new CraftingResultSlot(playerInventory.player, craftingGrid1, craftResult1, 0, 80, 31));
        addSlot(new CraftingResultSlot(playerInventory.player, craftingGrid2, craftResult2, 1, 80, 59));

        addCraftingGrid(craftingGrid1, 0, 8,   27, 3, 3);
        addCraftingGrid(craftingGrid2, 0, 116, 27, 3, 3);

        addPlayerInventorySlots(8, 84);

        onCraftMatrixChanged(craftingGrid1);
        onCraftMatrixChanged(craftingGrid2);
    }

    public CrafterContainer(int windowId, PlayerInventory playerInv, PacketBuffer extraData) {
        this(windowId, playerInv,
                ((CrafterTile) Minecraft.getInstance().world.getTileEntity(extraData.readBlockPos()))); // Only used on client
    }

    /*******************************************************************************************************************
     * ContainerCrafter                                                                                                *
     *******************************************************************************************************************/

    /**
     * Create a crafting grid
     * @param inventory Inventory
     * @param startSlot Start slot
     * @param x X pos
     * @param y Y pos
     * @param width Width
     * @param height height
     */
    private void addCraftingGrid(IInventory inventory, int startSlot, int x, int y, int width, int height) {
        int i = 0;
        for(int h = 0; h < height; h++) {
            for(int w = 0; w < width; w++) {
                addSlot(new Slot(inventory, startSlot + i, x + (w * 18), y + (h * 18)));
                i++;
            }
        }
    }

    /*******************************************************************************************************************
     * Container                                                                                                       *
     *******************************************************************************************************************/

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        World world = crafter.getWorld();
        if (inventoryIn == craftingGrid1)
            if(world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, craftingGrid1, world).isPresent())
                craftResult1.setInventorySlotContents(0,
                        world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, craftingGrid1, world).get().getCraftingResult(craftingGrid1));
            else
                craftResult1.setInventorySlotContents(0, ItemStack.EMPTY);
        else if (inventoryIn == craftingGrid2)
            if(world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, craftingGrid2, world).isPresent())
                craftResult2.setInventorySlotContents(0,
                        world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, craftingGrid2, world).get().getCraftingResult(craftingGrid2));
            else
                craftResult2.setInventorySlotContents(0, ItemStack.EMPTY);

    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return !(slotIn instanceof CraftingResultSlot);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        if(index < 0 || index > inventorySlots.size())
            return super.transferStackInSlot(playerIn, index);
        Slot slot = inventorySlots.get(index);
        if(slot != null && slot.getHasStack()) {
            ItemStack itemToTransfer = slot.getStack();
            ItemStack copy = itemToTransfer.copy();

            if(index < getInventorySizeNotPlayer()) {
                if (!mergeItemStack(itemToTransfer, getInventorySizeNotPlayer(), inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            } else if(!mergeItemStack(itemToTransfer, 0, getInventorySizeNotPlayer(), false))
                return ItemStack.EMPTY;

            if(index == 0)
                slot.onTake(playerIn, copy);
            else if(index == 1)
                slot.onTake(playerIn, copy);

            if(itemToTransfer.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
                slot.onSlotChanged();
            }
            else
                slot.onSlotChanged();

            onCraftMatrixChanged(craftingGrid1);
            onCraftMatrixChanged(craftingGrid2);

            if(itemToTransfer.getCount() != copy.getCount())
                return copy;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getInventorySizeNotPlayer() {
        return 20;
    }

    /*******************************************************************************************************************
     * Crafting dummy, allow for more than one crafting grid                                                           *
     *******************************************************************************************************************/

    private class DummyCraftingInventory extends CraftingInventory {
        // Crafter instance
        protected CrafterContainer container;
        int offset;

        public DummyCraftingInventory(CrafterContainer ctainer, int offsetNum) {
            super(null, 3, 3);
            offset = offsetNum;
            container = ctainer;
        }

        @Override
        public int getSizeInventory() {
            return 9;
        }

        @Override
        public ItemStack getStackInSlot(int index) {
            if(index >= getSizeInventory())
                return ItemStack.EMPTY;
            else
                return container.crafter.getStackInSlot(index + offset);
        }

        @Override
        public ItemStack decrStackSize(int index, int count) {
            ItemStack stack = container.crafter.extractItem(index + offset, count, false);
            container.onCraftMatrixChanged(this);
            return stack;
        }

        @Override
        public void setInventorySlotContents(int index, ItemStack stack) {
            container.crafter.setStackInSlot(index + offset, stack);
            container.onCraftMatrixChanged(this);
        }
    }
}