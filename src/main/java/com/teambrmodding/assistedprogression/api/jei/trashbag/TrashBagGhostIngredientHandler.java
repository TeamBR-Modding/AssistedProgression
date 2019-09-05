package com.teambrmodding.assistedprogression.api.jei.trashbag;

import com.teambr.nucleus.common.container.slots.IPhantomSlot;
import com.teambrmodding.assistedprogression.client.screen.TrashBagScreen;
import com.teambrmodding.assistedprogression.common.container.TrashBagContainer;
import com.teambrmodding.assistedprogression.network.PacketManager;
import com.teambrmodding.assistedprogression.network.packet.NotifyServerOfTrashBagChanges;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;

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
public class TrashBagGhostIngredientHandler<T extends TrashBagScreen> implements IGhostIngredientHandler<T> {

    /**
     * Called when a player wants to drag an ingredient on to your gui.
     * Return the targets that can accept the ingredient.
     * <p>
     * This is called when a player hovers over an ingredient with doStart=false,
     * and called again when they pick up the ingredient with doStart=true.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <I> List<Target<I>> getTargets(T gui, I ingredient, boolean doStart) {
        List<Target<I>> targets = new ArrayList<>();
        if(ingredient instanceof ItemStack) {
            for(Slot slot : gui.trashBagContainer.inventorySlots) {
                if(slot instanceof IPhantomSlot)
                    targets.add(new SlotTarget(gui, slot));
            }
        }
        return targets;
    }

    /**
     * Called when the player is done dragging an ingredient.
     * If the drag succeeded, {@link Target#accept(Object)} was called before this.
     * Otherwise, the player failed to drag an ingredient to a {@link Target}.
     */
    @Override
    public void onComplete() {

    }

    private class SlotTarget implements Target {

        private T guiParent;
        private Slot slot;

        SlotTarget(T guiParent, Slot slotIn) {
            this.guiParent = guiParent;
            this.slot = slotIn;
        }

        /**
         * @return the area (in screen coordinates) where the ingredient can be dropped.
         */
        @Override
        public Rectangle2d getArea() {
            return new Rectangle2d(guiParent.getGuiLeft() + slot.xPos,
                    guiParent.getGuiTop() + slot.yPos, 16, 16);
        }

        /**
         * Called with the ingredient when it is dropped on the target.
         */
        @Override
        public void accept(Object ingredient) {
            if(ingredient instanceof ItemStack) {
                slot.putStack((ItemStack) ingredient);
                PacketManager.INSTANCE
                        .sendToServer(new NotifyServerOfTrashBagChanges(guiParent.trashBagContainer.trashBag.getTag()));
            }
        }
    }
}
