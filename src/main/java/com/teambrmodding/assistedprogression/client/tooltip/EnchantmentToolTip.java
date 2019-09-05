package com.teambrmodding.assistedprogression.client.tooltip;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.teambrmodding.assistedprogression.managers.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author James Rogers - Dyonovan
 * @since 08/30/19
 */
public class EnchantmentToolTip {

    /**
     * Called on tool tip, here we are checking if there is a translation for the item, and adding it in the list
     * for the tooltip
     */
    @SubscribeEvent
    public static void changeToolTip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();

        // Check if able to show, if so check if has enchant or is book
        if (ConfigManager.GENERAL.showEnchantTooltip.get() && (itemStack.isEnchanted()
                || itemStack.getItem() instanceof EnchantedBookItem)) {
            List<ITextComponent> tooltip = event.getToolTip();

            // If not sneaking, display tip to activate
            if (!InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(),
                    Minecraft.getInstance().gameSettings.keyBindSneak.getKey().getKeyCode())) {
                tooltip.add(new StringTextComponent(
                        I18n.format("assistedprogression:tooltip.activate",
                                ChatFormatting.RED,
                                I18n.format(Minecraft.getInstance().gameSettings.keyBindSneak.getTranslationKey()),
                                ChatFormatting.WHITE)));
            } else {
                // Add info
                ListNBT listNBT = (itemStack.getItem() instanceof EnchantedBookItem) ?
                        EnchantedBookItem.getEnchantments(itemStack) :
                        itemStack.getEnchantmentTagList();

                for (int i = 0; i < listNBT.size(); i++) {
                    CompoundNBT nbt = listNBT.getCompound(i);
                    String s = nbt.getString("id");
                    String formatted = I18n.format("assistedprogression:" + s);
                    if (!formatted.equals("assistedprogression:" + s))
                        tooltip.add(new StringTextComponent(I18n.format("assistedprogression:" + s)));
                }
            }
        }
    }
}
