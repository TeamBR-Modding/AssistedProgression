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
 *
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author James Rogers - Dyonovan
 * @since 08/30/19
 */
public class EnchantmentToolTip {

    private static final KeyBinding keyBindSneak = Minecraft.getInstance().gameSettings.keyBindSneak;

    public EnchantmentToolTip() {
    }

    @SubscribeEvent
    public void changeToolTip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();

       if (ConfigManager.GENERAL.showEnchantTooltip.get() && (itemStack.isEnchanted() || itemStack.getItem() instanceof EnchantedBookItem)) {
           List<ITextComponent> tooltip = event.getToolTip();

           if (!InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), keyBindSneak.getKey().getKeyCode())) {
               tooltip.add(new StringTextComponent(
                       I18n.format("assistedprogression:tooltip.activate",
                       ChatFormatting.RED,
                       I18n.format(keyBindSneak.getTranslationKey()),
                       ChatFormatting.GRAY)));
           } else {
               ListNBT listNBT;
               if (itemStack.getItem() instanceof EnchantedBookItem) {
                   listNBT = EnchantedBookItem.getEnchantments(itemStack);
               } else {
                   listNBT = itemStack.getEnchantmentTagList();
               }
               for (int i = 0; i < listNBT.size(); i++) {
                   CompoundNBT nbt = listNBT.getCompound(i);
                   String s = nbt.getString("id");
                   tooltip.add(new StringTextComponent(I18n.format("assistedprogression:" + s)));
               }
           }
       }
    }
}
