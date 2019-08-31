package com.teambrmodding.assistedprogression.client.tooltip;

import com.mojang.realmsclient.gui.ChatFormatting;
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

       if (itemStack.isEnchanted() || itemStack.getItem() instanceof EnchantedBookItem) {
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
                   switch (s) {
                       case "minecraft:protection":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.protection")));
                           break;
                       case "minecraft:fire_protection":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.fireprotection")));
                           break;
                       case "minecraft:feather_falling":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.featherfalling")));
                           break;
                       case "minecraft:blast_protection":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.blastprotection")));
                           break;
                       case "minecraft:projectile_protection":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.projectileprotection")));
                           break;
                       case "minecraft:respiration":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.respiration")));
                           break;
                       case "minecraft:aqua_affinity":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.aquainfinity")));
                           break;
                       case "minecraft:thorns":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.thorns")));
                           break;
                       case "minecraft:depth_strider":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.depthstrider")));
                           break;
                       case "minecraft:frost_walker":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.frostwalker")));
                           break;
                       case "minecraft:binding_curse":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.curseofbinding")));
                           break;
                       case "minecraft:sharpness":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.sharpness")));
                           break;
                       case "minecraft:smite":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.smite")));
                           break;
                       case "minecraft:bane_of_arthropods":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.bane")));
                           break;
                       case "minecraft:knockback":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.knockback")));
                           break;
                       case "minecraft:fire_aspect":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.fireaspect")));
                           break;
                       case "minecraft:looting":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.looting")));
                           break;
                       case "minecraft:sweeping":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.sweepingedge")));
                           break;
                       case "minecraft:efficiency":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.efficiency")));
                           break;
                       case "minecraft:silk_touch":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.silktouch")));
                           break;
                       case "minecraft:unbreaking":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.unbreaking")));
                           break;
                       case "minecraft:fortune":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.fortune")));
                           break;
                       case "minecraft:power":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.power")));
                           break;
                       case "minecraft:punch":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.punch")));
                           break;
                       case "minecraft:flame":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.flame")));
                           break;
                       case "minecraft:infinity":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.infinity")));
                           break;
                       case "minecraft:luck_of_the_sea":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.luckofthesea")));
                           break;
                       case "minecraft:lure":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.lure")));
                           break;
                       case "minecraft:mending":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.mending")));
                           break;
                       case "minecraft:vanishing_curse":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.vanishing")));
                           break;
                       case "minecraft:loyalty":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.loyalty")));
                           break;
                       case "minecraft:impaling":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.impaling")));
                           break;
                       case "minecraft:riptide":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.riptide")));
                           break;
                       case "minecraft:channeling":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.channeling")));
                           break;
                       case "minecraft:multishot":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.multishot")));
                           break;
                       case "minecraft:quick_charge":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.quick_charge")));
                           break;
                       case "minecraft:piercing":
                           tooltip.add(new StringTextComponent(I18n.format("assistedprogression:enchant.piercing")));
                           break;
                   }
               }
           }
       }
    }
}
