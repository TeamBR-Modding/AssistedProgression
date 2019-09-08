package com.teambrmodding.assistedprogression.client.screen;

import com.teambr.nucleus.client.gui.GuiBase;
import com.teambr.nucleus.client.gui.component.display.GuiComponentText;
import com.teambr.nucleus.common.items.InventoryHandlerItem;
import com.teambrmodding.assistedprogression.common.container.TrashBagContainer;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.managers.ItemManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

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
public class TrashBagScreen extends GuiBase<TrashBagContainer> {

    public TrashBagContainer trashBagContainer;

    /**
     * Main constructor for the trash bag
     */
    public TrashBagScreen(TrashBagContainer container, PlayerInventory inventoryPlayer, ITextComponent title) {
        super(container, inventoryPlayer, title, 175, 165,
                new ResourceLocation(Reference.MOD_ID, "textures/gui/" +
                        (Minecraft.getInstance().player.getHeldItemMainhand().getItem() == ItemManager.trash_bag
                                ? "trash_bag.png" : "hefty_bag.png")));
        this.trashBagContainer = container;
    }

    @Override
    protected void addComponents() {
        components.add(new GuiComponentText(this, 7, 20, "Filters", null));
        components.add(new GuiComponentText(this, 7, 73, "Inventory", null));
    }
}
