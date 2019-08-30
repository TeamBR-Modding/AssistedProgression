package com.teambrmodding.assistedprogression.client.tooltip;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class EnchantmentToolTip {

    private static final KeyBinding keyBindSneak = Minecraft.getInstance().gameSettings.keyBindSneak;

    public EnchantmentToolTip() {
    }

    @SubscribeEvent
    public void changeToolTip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();

       if (itemStack.isEnchanted()) {
           List<ITextComponent> tooltip = event.getToolTip();

           if (!InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), keyBindSneak.getKey().getKeyCode())) {
               tooltip.add(new StringTextComponent(
                       I18n.format("namedenchantments:tooltip.activate",
                       ChatFormatting.RED,
                       I18n.format(keyBindSneak.getTranslationKey()),
                       ChatFormatting.GRAY)));
           } else {

           }
       }
    }
}
