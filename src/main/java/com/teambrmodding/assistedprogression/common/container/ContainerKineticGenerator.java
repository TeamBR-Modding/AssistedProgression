package com.teambrmodding.assistedprogression.common.container;

import com.teambr.nucleus.common.container.BaseContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 11/18/17
 */
public class ContainerKineticGenerator extends BaseContainer {

    /**
     * Creates the container object
     *
     * @param playerInventory The players inventory
     * @param inventory       The tile/object inventory
     */
    public ContainerKineticGenerator(IInventory playerInventory, IItemHandler inventory) {
        super(playerInventory, inventory);
        addSlotToContainer(new SlotItemHandler(inventory, 0, 37, 58){
            @Nullable
            @Override
            public String getSlotTexture() {
                GlStateManager.enableBlend();
                return "assistedprogression:gui/components/in";
            }
        });

        addPlayerInventorySlots(84);
    }
}