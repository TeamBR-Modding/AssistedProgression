package com.teambrmodding.assistedprogression.common.item;

import com.teambr.nucleus.common.IAdvancedToolTipProvider;
import com.teambr.nucleus.common.items.InventoryHandlerItem;
import com.teambr.nucleus.util.ClientUtils;
import com.teambrmodding.assistedprogression.common.container.TrashBagContainer;
import com.teambrmodding.assistedprogression.managers.ItemManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 9/4/2019
 */
public class TrashBagItem extends Item implements INamedContainerProvider, IAdvancedToolTipProvider {

    // The size of this inventory
    public int bagInventorySize;

    /**
     * Creates a trash bag item, this should only ever be used to create, not on the fly
     * @param registryName Registry name for this item
     * @param size The size of the inventory
     */
    public TrashBagItem(String registryName, int size) {
        super(new Properties()
                .maxStackSize(1)
                .group(ItemManager.itemGroupAssistedProgression));
        setRegistryName(registryName);
        this.bagInventorySize = size;
    }

    /*******************************************************************************************************************
     * Item                                                                                                            *
     *******************************************************************************************************************/

    /**
     * Called from ItemStack.setItem, will hold extra data for the life of this ItemStack.
     * Can be retrieved from stack.getCapabilities()
     * The NBT can be null if this is not called from readNBT or if the item the stack is
     * changing FROM is different then this item, or the previous item had no capabilities.
     * <p>
     * This is called BEFORE the stacks item is set so you can use stack.getItem() to see the OLD item.
     * Remember that getItem CAN return null.
     *
     * @param stack The ItemStack
     * @param nbt   NBT of this item serialized, or null.
     * @return A holder instance associated with this ItemStack where you can hold capabilities for the life of this item.
     */
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return new InventoryHandlerItem(stack, nbt) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }

            @Override
            protected int getInventorySize() {
                return bagInventorySize;
            }

            @Override
            protected boolean isItemValidForSlot(int index, ItemStack stack) {
                return true;
            }
        };
    }

    /**
     * Called when this item is right clicked
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if(!world.isRemote && hand == Hand.MAIN_HAND && player.getHeldItem(hand).getItem() instanceof TrashBagItem) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (TrashBagItem) player.getHeldItem(hand).getItem());
            return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
        }
        return super.onItemRightClick(world, player, hand);
    }

    /*******************************************************************************************************************
     * Event                                                                                                           *
     *******************************************************************************************************************/

    /**
     * Called when an item is picked up, here we test if we should void the items
     * @param event The item pickup event
     */
    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        PlayerEntity player = event.getPlayer();
        ItemStack pickedItem = event.getItem().getItem();

        if (pickedItem.isEmpty() || player == null) return;

        // Look for trashbags
        for (ItemStack stack : player.inventory.mainInventory) {
            // If we have a valid trashbag
            if (!stack.isEmpty() && stack.getItem() instanceof TrashBagItem
                    && stack.hasTag()
                    && stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).isPresent()) {
                IItemHandler trashBagHandler
                        = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);

                // Check against filter
                for (int x = 0; x < trashBagHandler.getSlots(); x++) {
                    ItemStack trashKey = trashBagHandler.getStackInSlot(x);
                    if (!trashKey.isEmpty()) {
                        if (ItemStack.areItemsEqual(trashKey, pickedItem) &&
                                ItemStack.areItemStackTagsEqual(trashKey, pickedItem)) {
                            pickedItem.shrink(pickedItem.getCount());
                            player.world.playSound(null,
                                    new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ()),
                                    SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS,
                                    0.3F, 0.5F);
                            return; // Items voided, no need to continue
                        }
                    }
                }
            }
        }
    }

    /*******************************************************************************************************************
     * INamedContainerProvider                                                                                         *
     *******************************************************************************************************************/

    /**
     * Get the name of the gui
     * @return Translated name
     */
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("item." + getRegistryName().toString().replace(":", "."));
    }

    /**
     * Create the container on the server
     * @param i Window id
     * @param playerInventory Player's inventory
     * @param playerEntity The player
     * @return A container with all data passed, this will be from the server side
     */
    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new TrashBagContainer(i, playerInventory,
                (InventoryHandlerItem) playerEntity.getHeldItemMainhand().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null),
                playerEntity.getHeldItemMainhand());
    }

    /*******************************************************************************************************************
     * IAdvancedToolTipProvider                                                                                        *
     *******************************************************************************************************************/

    /**
     * Get the tool tip to present when shift is pressed
     *
     * @param stack The itemstack
     * @return The list to display
     */
    @Nullable
    @Override
    public List<String> getAdvancedToolTip(@Nonnull ItemStack stack) {
        return Collections.singletonList(ClientUtils.translate(getRegistryName().getPath() + ".desc"));
    }
}
